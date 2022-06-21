/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bo.com.micrium.modulobase.security.jwt;

import bo.com.micrium.modulobase.model.Accion;
import bo.com.micrium.modulobase.model.Formulario;
import bo.com.micrium.modulobase.model.Rol;
import bo.com.micrium.modulobase.model.RolAccion;
import bo.com.micrium.modulobase.repository.IAccionRepository;
import bo.com.micrium.modulobase.repository.IFormularioRepository;
import bo.com.micrium.modulobase.repository.IRolAccionRepository;
import bo.com.micrium.modulobase.resources.GenericControler;
import bo.com.micrium.modulobase.resources.dto.jwt.AccionResponse;
import bo.com.micrium.modulobase.resources.dto.jwt.FormularioResponse;
import bo.com.micrium.modulobase.resources.dto.jwt.ModuloResponse;
import bo.com.micrium.modulobase.service.ParametroService;
import bo.com.micrium.modulobase.resources.dto.jwt.ExceptionResponse;
import bo.com.micrium.modulobase.resources.dto.jwt.AuteticacionResponse;
import bo.com.micrium.modulobase.resources.dto.jwt.AuteticacionRequest;
import bo.com.micrium.modulobase.util.ParametroID;
import bo.com.micrium.modulobase.util.PrivilegioTipo;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import javax.validation.Valid;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AccountStatusException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;


@Log4j2
@RestController
@CrossOrigin
public class JwtAuthenticationController extends GenericControler implements Serializable {

    private static final long serialVersionUID = 1L;

    public static final String METODO_VERSION = "/version";
    public static final String METODO_AUTENTICACION = "/autenticacion";

    @Autowired
    private AuthenticationLdapManager authenticationManager;

    @Autowired
    private IFormularioRepository formularioRepository;

    @Autowired
    private IRolAccionRepository rolAccionRepository;

    @Autowired
    private IAccionRepository accionRepository;

    @Autowired
    ParametroService parametroService;

    @RequestMapping(value = METODO_VERSION, method = RequestMethod.GET)
    public ResponseEntity<?> version() {
        return ResponseEntity.ok("Version 2.0");
    }

    @RequestMapping(value = METODO_AUTENTICACION, method = RequestMethod.POST)
    public ResponseEntity<?> createAuthenticationToken(
            @RequestHeader(value = JwtTokenUtil.IP_CLIENT) String ipClient,
            @RequestHeader(value = JwtTokenUtil.FORM) String form,
            @Valid @RequestBody AuteticacionRequest authenticationRequest, BindingResult result) {
        if (ipClient == null || ipClient.isEmpty() || ipClient.equals("127.0.0.1") || ipClient.equals("localhost")) {
            ipClient = httpServletRequest.getRemoteAddr();
        }

        StringBuilder sb = new StringBuilder();
        sb.append("-------------------REQUEST----------------------\n").
                append("form ").append(form).append(", \n").
                append("ipClient ").append(ipClient).append(", \n").
                append("url ").append(httpServletRequest.getRequestURL()).append(", \n").
                append("metodo ").append(httpServletRequest.getMethod()).append(", \n").
                append("request ").append(authenticationRequest).append(", \n").
                append("------------------------------------------------\n");

        log.info(sb.toString());
        Rol rol = null;

        try {
            rol = authenticationManager.validarLdap(authenticationRequest.getNombreUsuario(), authenticationRequest.getContrasena());
        } catch (DisabledException e) {
            log.info("Error DisabledException " + e.getMessage(), e);
            guardarBitacora(null, ipClient, form, "Autenticacion fallida, " + e.getMessage());
            return ResponseEntity.badRequest().body(new ExceptionResponse(HttpStatus.BAD_REQUEST, "Error procesamiento", e.getMessage()));
        } catch (BadCredentialsException | InsufficientAuthenticationException | AccountStatusException e) {
            log.info("Error BadCredentialsException | InsufficientAuthenticationException | AccountStatusException  " + e.getMessage(), e);
            guardarBitacora(null, ipClient, form, "Autenticacion fallida, " + e.getMessage());
            return ResponseEntity.badRequest().body(new ExceptionResponse(HttpStatus.BAD_REQUEST, "Error procesamiento", e.getMessage()));
        } catch (Exception e) {
            log.error("Error al procesar la peticion " + e.getMessage(), e);
            guardarBitacora(null, ipClient, form, "Autenticacion fallida, " + e.getMessage());
            return ResponseEntity.badRequest().body(new ExceptionResponse(HttpStatus.BAD_REQUEST, "Error procesamiento", "Error al procesar la peticion, comuniquese con el administrador."));
        }

        final String token = jwtTokenUtil.generateToken(authenticationRequest.getNombreUsuario(), rol.getNombre());

        List<ModuloResponse> modulos = new ArrayList<>();
        List<Integer> formularios = new ArrayList<>();

        int validacionActiveDirectory = ((BigDecimal) parametroService.getParamVal(ParametroID.VALIDACION_ACTIVE_DIRECTORY)).intValue();

        for (RolAccion rolAccion : rolAccionRepository.findAllByRolId(rol.getId())) {
            Optional<Accion> optionalAcc = accionRepository.findById(rolAccion.getAccionId());

            Accion accion = optionalAcc.get();

            Optional<Formulario> optionalForm = formularioRepository.findById(accion.getFormularioId());

            Formulario formulario = optionalForm.get();

            if (!(validacionActiveDirectory == ParametroID.LOCAL && formulario.getId() == ParametroID.FORMULARIO_GRUPO)) {

                if (!formularios.stream().anyMatch(id -> id.equals(accion.getFormularioId()))) {
                    formularios.add(accion.getFormularioId());

                    Optional<Formulario> optionalMod = formularioRepository.findById(formulario.getModuloId());

                    Formulario modulo = optionalMod.get();

                    List<AccionResponse> tempAcciones = new ArrayList<>();
                    tempAcciones.add(new AccionResponse(accion.getId(), accion.getNombre(), PrivilegioTipo.PERMISO_TIPO_ACCION));

                    FormularioResponse tempFormulario = new FormularioResponse(formulario.getId(),
                            formulario.getNombre(), formulario.getOrden(),
                            PrivilegioTipo.PERMISO_TIPO_FORMULARIO, formulario.getUrl(), formulario.getIcono(), tempAcciones);

                    Optional<ModuloResponse> optionalModuloRespo = modulos.stream().
                            filter(m -> m.getId() == formulario.getModuloId()).findFirst();

                    ModuloResponse moduloResponse;

                    if (optionalModuloRespo.isPresent()) {
                        moduloResponse = optionalModuloRespo.get();
                    } else {
                        moduloResponse = new ModuloResponse(modulo.getId(), modulo.getNombre(), modulo.getOrden(),
                                PrivilegioTipo.PERMISO_TIPO_MODULO, modulo.getUrl(), modulo.getIcono(), new ArrayList<>());

                        modulos.add(moduloResponse);
                    }
                    moduloResponse.getFormularios().add(tempFormulario);

                } else {
                    ModuloResponse moduloResponse = modulos.stream().
                            filter(m -> m.getId() == formulario.getModuloId()).findFirst().get();

                    Optional<FormularioResponse> optionalFormRes = moduloResponse.getFormularios().
                            stream().filter(f -> f.getId() == formulario.getId()).findFirst();
                    optionalFormRes.get().getAcciones().add(
                            new AccionResponse(accion.getId(), accion.getNombre(),
                                    PrivilegioTipo.PERMISO_TIPO_ACCION));
                }
            }

        }

        Collections.sort(modulos);

        modulos.forEach(m -> {
            m.sortFormularios();
        });

        //guardarBitacora("Bearer " + token, ipClient, form, "Autenticacion del usuario " + authenticationRequest.getNombreUsuario() + ", modulos " + modulos);
        guardarBitacora("Bearer " + token, ipClient, form, "Autenticacion del usuario " + authenticationRequest.getNombreUsuario());

        ResponseEntity<AuteticacionResponse> out = ResponseEntity.ok(new AuteticacionResponse(token, rol.getId(), rol.getNombre(), modulos));

        sb = new StringBuilder();
        sb.append("-------------------RESPONSE----------------------\n").
                append("token ").append(token).append(", \n").
                append("DATA ").append(out).append(", \n").
                append("------------------------------------------------\n");

        log.info(sb.toString());

        return out;
    }

}
