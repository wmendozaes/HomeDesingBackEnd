/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bo.com.micrium.modulobase.resources;

import java.math.BigDecimal;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import bo.com.micrium.modulobase.ldap.ActiveDirectory;
import bo.com.micrium.modulobase.ldap.LdapContextException;
import bo.com.micrium.modulobase.model.Usuario;
import bo.com.micrium.modulobase.repository.IRolRepository;
import bo.com.micrium.modulobase.repository.IUsuarioRepository;
import bo.com.micrium.modulobase.resources.dto.UsuarioRequest;
import bo.com.micrium.modulobase.resources.dto.UsuarioResponse;
import bo.com.micrium.modulobase.security.jwt.JwtTokenUtil;
import bo.com.micrium.modulobase.service.ParametroService;
import bo.com.micrium.modulobase.util.ConvercionUtil;
import bo.com.micrium.modulobase.util.ParametroID;
import bo.com.micrium.modulobase.util.UsuarioTipo;
import bo.com.micrium.modulobase.util.abm.UsuarioEstado;
import bo.com.micrium.modulobase.validator.ApiException;
import bo.com.micrium.modulobase.validator.UsuarioValidator;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.NoHandlerFoundException;
import lombok.extern.log4j.Log4j2;

/**
 *
 * @author alepaco.maton
 */
@Log4j2
@RestController
@CrossOrigin
@RequestMapping(value = "/usuarios", produces = {MediaType.APPLICATION_JSON_VALUE})
public class UsuarioControler extends GenericControler implements ICrudControler<UsuarioRequest, UsuarioResponse, Integer> {

    @Autowired
    protected IUsuarioRepository repository;

    @Autowired
    protected UsuarioValidator validator;

    @Autowired
    private IRolRepository rolRepository;

    @Autowired
    private ParametroService parametroService;

    @Override
    public Page<UsuarioResponse> list(String token, String ipClient, String form, Pageable pageRequest) {
        if (ipClient == null || ipClient.isEmpty() || ipClient.equals("127.0.0.1") || ipClient.equals("localhost")) {
            ipClient = httpServletRequest.getRemoteAddr();
        }

        StringBuilder sb = new StringBuilder();
        sb.append("-------------------REQUEST----------------------\n").
                append("token ").append(token).append(", \n").
                append("form ").append(form).append(", \n").
                append("ipClient ").append(ipClient).append(", \n").
                append("url ").append(httpServletRequest.getRequestURL()).append(", \n").
                append("metodo ").append(httpServletRequest.getMethod()).append(", \n").
                append("pageRequest ").append(pageRequest).append(", \n").
                append("------------------------------------------------\n");

        log.info(sb.toString());

        Page<UsuarioResponse> out = repository.findAllByTipoNotAndEstadoIn(UsuarioTipo.SUPER_USUARIO,
                Arrays.asList(UsuarioEstado.HABILITADO, UsuarioEstado.BLOQUEADO),
                pageRequest).map(model -> ConvercionUtil.convertir(model));

        sb = new StringBuilder();
        sb.append("-------------------RESPONSE----------------------\n").
                append("token ").append(token).append(", \n").
                append("DATA ").append(out).append(", \n").
                append("CONTENT ").append(out.getContent()).append(", \n").
                append("------------------------------------------------\n");

        log.info(sb.toString());

        return out;
    }

    @Override
    public ResponseEntity<UsuarioResponse> get(String token, String ipClient, String form, Integer id) throws NoHandlerFoundException {
        if (ipClient == null || ipClient.isEmpty() || ipClient.equals("127.0.0.1") || ipClient.equals("localhost")) {
            ipClient = httpServletRequest.getRemoteAddr();
        }

        StringBuilder sb = new StringBuilder();
        sb.append("-------------------REQUEST----------------------\n").
                append("token ").append(token).append(", \n").
                append("form ").append(form).append(", \n").
                append("ipClient ").append(ipClient).append(", \n").
                append("url ").append(httpServletRequest.getRequestURL()).append(", \n").
                append("metodo ").append(httpServletRequest.getMethod()).append(", \n").
                append("id ").append(id).append(", \n").
                append("------------------------------------------------\n");

        log.info(sb.toString());

        Optional<Usuario> model = repository.findById(id);
        if (model.isPresent()) {
            ResponseEntity<UsuarioResponse> out = ResponseEntity.ok().body(ConvercionUtil.convertir(model.get()));

            sb = new StringBuilder();
            sb.append("-------------------RESPONSE----------------------\n").
                    append("token ").append(token).append(", \n").
                    append("DATA ").append(out).append(", \n").
                    append("------------------------------------------------\n");

            log.info(sb.toString());

            return out;
        }

        throw new NoHandlerFoundException("GET", "/{id}" + id, HttpHeaders.EMPTY);
    }

    @Override
    public ResponseEntity<UsuarioResponse> create(String token, String ipClient, String form,
            UsuarioRequest request, BindingResult result) throws URISyntaxException, ApiException {
        if (ipClient == null || ipClient.isEmpty() || ipClient.equals("127.0.0.1") || ipClient.equals("localhost")) {
            ipClient = httpServletRequest.getRemoteAddr();
        }

        StringBuilder sb = new StringBuilder();
        sb.append("-------------------REQUEST----------------------\n").
                append("token ").append(token).append(", \n").
                append("form ").append(form).append(", \n").
                append("ipClient ").append(ipClient).append(", \n").
                append("url ").append(httpServletRequest.getRequestURL()).append(", \n").
                append("metodo ").append(httpServletRequest.getMethod()).append(", \n").
                append("request ").append(request).append(", \n").
                append("------------------------------------------------\n");

        log.info(sb.toString());

        validator.validate(request, null, result);

        if (result.hasErrors()) {
            throw new ApiException(result, "Errores en la validacion");
        }

        String name = request.getNombreUsuario();

        if (request.getTipo()) {

            try {
                name = new ActiveDirectory(parametroService).getNombreCompleto(request.getNombreUsuario().trim());
            } catch (LdapContextException e1) {
                log.error("Error de conexion ldap " + e1.getMessage(), e1);
            }
        }
        int dias = ((BigDecimal) parametroService.getParamVal(ParametroID.BLOQUEO_USUARIOS_DIAS)).intValue();

        Date fechaActualizacion = sumarDiasAFecha(new Date(), dias);

        Usuario model = repository.save(new Usuario(null, request.getNombreUsuario(), name,
                (String) parametroService.getParamVal(ParametroID.CONTRASENA_POR_DEFECTO),
                (request.getTipo() ? UsuarioTipo.USUARIO_ACTIVE_DIRECTORY : UsuarioTipo.USUARIO_NORMAL),
                0, null, rolRepository.findById(request.getRolId()).get(), UsuarioEstado.HABILITADO,
                request.getValidarFechaLimite(), request.getFechaLimite(), fechaActualizacion));

        guardarBitacora(token, ipClient, form, "Se adiciono:" + model);

        ResponseEntity<UsuarioResponse> out = ResponseEntity.created(new URI("/usuarios/" + model.getId()))
                .body(ConvercionUtil.convertir(model));

        sb = new StringBuilder();
        sb.append("-------------------RESPONSE----------------------\n").
                append("token ").append(token).append(", \n").
                append("DATA ").append(out).append(", \n").
                append("------------------------------------------------\n");

        log.info(sb.toString());

        return out;
    }

    public Date sumarDiasAFecha(Date fecha, int dias) {
        if (dias == 0) {
            return fecha;
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(fecha);
        calendar.add(Calendar.DAY_OF_YEAR, dias);
        return calendar.getTime();
    }

    @Override
    public ResponseEntity<UsuarioResponse> update(String token, String ipClient, String form,
            UsuarioRequest request, Integer id, BindingResult result) throws ApiException {
        if (ipClient == null || ipClient.isEmpty() || ipClient.equals("127.0.0.1") || ipClient.equals("localhost")) {
            ipClient = httpServletRequest.getRemoteAddr();
        }

        StringBuilder sb = new StringBuilder();
        sb.append("-------------------REQUEST----------------------\n").
                append("token ").append(token).append(", \n").
                append("form ").append(form).append(", \n").
                append("ipClient ").append(ipClient).append(", \n").
                append("url ").append(httpServletRequest.getRequestURL()).append(", \n").
                append("metodo ").append(httpServletRequest.getMethod()).append(", \n").
                append("request ").append(request).append(", \n").
                append("id ").append(id).append(", \n").
                append("------------------------------------------------\n");

        log.info(sb.toString());

        validator.validate(request, id, result);

        if (result.hasErrors()) {
            throw new ApiException(result, "Errores en la validacion");
        }

        Usuario model = repository.findById(id).get();

        if (model.getTipo() == UsuarioTipo.USUARIO_ACTIVE_DIRECTORY) {
            try {
                model.setNombreCompleto(new ActiveDirectory(parametroService).getNombreCompleto(request.getNombreUsuario()));
            } catch (LdapContextException ex) {
                log.error("Error de conexion ldap " + ex.getMessage(), ex);
            }
        }

        model.setNombreUsuario(request.getNombreUsuario());
        model.setRolId(rolRepository.findById(request.getRolId()).get());

        model = repository.save(model);

        guardarBitacora(token, ipClient, form, "Se modifico:" + model);

        ResponseEntity<UsuarioResponse> out = ResponseEntity.ok().body(ConvercionUtil.convertir(model));

        sb = new StringBuilder();
        sb.append("-------------------RESPONSE----------------------\n").
                append("token ").append(token).append(", \n").
                append("DATA ").append(out).append(", \n").
                append("------------------------------------------------\n");

        log.info(sb.toString());

        return out;
    }

    @Override
    public ResponseEntity<?> delete(String token, String ipClient, String form,
            Integer id) throws NoHandlerFoundException, Exception {
        if (ipClient == null || ipClient.isEmpty() || ipClient.equals("127.0.0.1") || ipClient.equals("localhost")) {
            ipClient = httpServletRequest.getRemoteAddr();
        }

        StringBuilder sb = new StringBuilder();
        sb.append("-------------------REQUEST----------------------\n").
                append("token ").append(token).append(", \n").
                append("form ").append(form).append(", \n").
                append("ipClient ").append(ipClient).append(", \n").
                append("url ").append(httpServletRequest.getRequestURL()).append(", \n").
                append("metodo ").append(httpServletRequest.getMethod()).append(", \n").
                append("id ").append(id).append(", \n").
                append("------------------------------------------------\n");

        log.info(sb.toString());

        Optional<Usuario> temp = repository.findById(id);

        if (!temp.isPresent()) {
            throw new NoHandlerFoundException("DELETE", "/{id}" + id, HttpHeaders.EMPTY);
        }

        Usuario model = temp.get();

        if (model.getEstado() == UsuarioEstado.INHABILITADO) {
            return ResponseEntity.noContent().build();
        }

        model.setEstado(UsuarioEstado.INHABILITADO);

        repository.save(model);

        guardarBitacora(token, ipClient, form, "Se elimino:" + model);

        ResponseEntity<Object> out = ResponseEntity.ok().build();

        sb = new StringBuilder();
        sb.append("-------------------RESPONSE----------------------\n").
                append("token ").append(token).append(", \n").
                append("DATA ").append(out).append(", \n").
                append("------------------------------------------------\n");

        log.info(sb.toString());

        return out;
    }

    @GetMapping("/contrasena/reseteo/{id}")
    public ResponseEntity<?> resetearContrasena(@RequestHeader(value = JwtTokenUtil.KEY_TOKEN) String token,
            @RequestHeader(value = JwtTokenUtil.IP_CLIENT) String ipClient,
            @RequestHeader(value = JwtTokenUtil.FORM) String form,
            @PathVariable @Min(value = 1, message = "Identificador invalido, no puede ser menor a 1")
            @Max(value = 999999999, message = "Identificador invalido") int id) throws RuntimeException {
        if (ipClient == null || ipClient.isEmpty() || ipClient.equals("127.0.0.1") || ipClient.equals("localhost")) {
            ipClient = httpServletRequest.getRemoteAddr();
        }

        StringBuilder sb = new StringBuilder();
        sb.append("-------------------REQUEST----------------------\n").
                append("token ").append(token).append(", \n").
                append("form ").append(form).append(", \n").
                append("ipClient ").append(ipClient).append(", \n").
                append("url ").append(httpServletRequest.getRequestURL()).append(", \n").
                append("metodo ").append(httpServletRequest.getMethod()).append(", \n").
                append("id ").append(id).append(", \n").
                append("------------------------------------------------\n");

        log.info(sb.toString());

        Optional<Usuario> optional = repository.findById(id);

        if (!optional.isPresent()) {
            throw new RuntimeException("Identificador de usuario invalido.");
        }

        Usuario model = optional.get();

        model.setContrasena((String) parametroService.getParamVal(ParametroID.CONTRASENA_POR_DEFECTO));

        model = repository.save(model);

        guardarBitacora(token, ipClient, form, "Se reseteo la contraseña:" + model);

        ResponseEntity<Object> out = ResponseEntity.ok().build();

        sb = new StringBuilder();
        sb.append("-------------------RESPONSE----------------------\n").
                append("token ").append(token).append(", \n").
                append("DATA ").append(out).append(", \n").
                append("------------------------------------------------\n");

        log.info(sb.toString());

        return out;
    }

    @PostMapping("/cambiarEstado")
    ResponseEntity<?> cambiarEstado(@RequestHeader(value = JwtTokenUtil.KEY_TOKEN) String token,
            @RequestHeader(value = JwtTokenUtil.IP_CLIENT) String ipClient,
            @RequestHeader(value = JwtTokenUtil.FORM) String form,
            @RequestBody String id
    ) {
        if (ipClient == null || ipClient.isEmpty() || ipClient.equals("127.0.0.1") || ipClient.equals("localhost")) {
            ipClient = httpServletRequest.getRemoteAddr();
        }

        StringBuilder sb = new StringBuilder();
        sb.append("-------------------REQUEST----------------------\n").
                append("token ").append(token).append(", \n").
                append("form ").append(form).append(", \n").
                append("ipClient ").append(ipClient).append(", \n").
                append("url ").append(httpServletRequest.getRequestURL()).append(", \n").
                append("metodo ").append(httpServletRequest.getMethod()).append(", \n").
                append("id ").append(id).append(", \n").
                append("------------------------------------------------\n");

        log.info(sb.toString());

        Integer idusuario = Integer.parseInt(id);
        Optional<Usuario> optional = repository.findById(idusuario);

        Usuario model = optional.get();
        if (model.getEstado() == UsuarioEstado.HABILITADO) {
            model.setEstado(UsuarioEstado.BAJA);
        } else {
            model.setEstado(UsuarioEstado.HABILITADO);
        }
        model = repository.save(model);

        guardarBitacora(token, ipClient, form, "Se cambio estado usuario: " + model);

        ResponseEntity<Object> out = ResponseEntity.ok().build();

        sb = new StringBuilder();
        sb.append("-------------------RESPONSE----------------------\n").
                append("token ").append(token).append(", \n").
                append("DATA ").append(out).append(", \n").
                append("------------------------------------------------\n");

        log.info(sb.toString());

        return out;

    }

}
