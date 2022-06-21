/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bo.com.micrium.modulobase.resources;

import java.math.BigDecimal;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import bo.com.micrium.modulobase.model.Accion;
import bo.com.micrium.modulobase.model.Formulario;
import bo.com.micrium.modulobase.model.RolAccion;
import bo.com.micrium.modulobase.model.RolTipoParametroPermiso;
import bo.com.micrium.modulobase.model.TipoParametro;
import bo.com.micrium.modulobase.repository.IAccionRepository;
import bo.com.micrium.modulobase.repository.IFormularioRepository;
import bo.com.micrium.modulobase.repository.IRolAccionRepository;
import bo.com.micrium.modulobase.repository.IRolTipoparametroRepository;
import bo.com.micrium.modulobase.repository.ITipoParametroRepository;
import bo.com.micrium.modulobase.resources.dto.PermisoRequest;
import bo.com.micrium.modulobase.resources.dto.RolTipoParametroPermisoRequest;
import bo.com.micrium.modulobase.resources.dto.RolTipoParametroPermisoResponse;
import bo.com.micrium.modulobase.resources.dto.jwt.AccionResponse;
import bo.com.micrium.modulobase.resources.dto.jwt.FormularioResponse;
import bo.com.micrium.modulobase.resources.dto.jwt.ModuloResponse;
import bo.com.micrium.modulobase.security.jwt.JwtTokenUtil;
import bo.com.micrium.modulobase.service.ParametroService;
import bo.com.micrium.modulobase.util.ParametroID;
import bo.com.micrium.modulobase.util.PermisoTipo;
import bo.com.micrium.modulobase.util.PrivilegioTipo;
import bo.com.micrium.modulobase.validator.ApiException;
import lombok.extern.log4j.Log4j2;

/**
 *
 * @author alepaco.maton
 */
@Log4j2
@RestController
@Service
@CrossOrigin
@RequestMapping(value = "/permisos", produces = {MediaType.APPLICATION_JSON_VALUE})
public class PermisoController extends GenericControler {

    @Autowired
    IFormularioRepository formularioRepository;

    @Autowired
    IAccionRepository accionRepository;

    @Autowired
    IRolAccionRepository rolAccionRepository;

    @Autowired
    IRolTipoparametroRepository rolTipoparametroRepository;

    @Autowired
    ITipoParametroRepository tipoparametroRepository;

    @Autowired
    ParametroService parametroService;

    @GetMapping
    List<ModuloResponse> obtenerPlantillaPermisos(@RequestHeader(value = JwtTokenUtil.KEY_TOKEN) String token,
            @RequestHeader(value = JwtTokenUtil.IP_CLIENT) String ipClient,
            @RequestHeader(value = JwtTokenUtil.FORM) String form) {
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
                append("------------------------------------------------\n");

        log.info(sb.toString());

        List<ModuloResponse> modulos = new ArrayList<>();
        int validacionActiveDirectory = ((BigDecimal) parametroService.getParamVal(ParametroID.VALIDACION_ACTIVE_DIRECTORY)).intValue();
        Boolean validacionGrupoLDAP=(Boolean) parametroService.getParamVal(ParametroID.ACTIVE_DIRECTORY_GRUPOLDAP);
        
        for (Formulario modulo : formularioRepository.findByModuloIdIsNull(Sort.by(Sort.Direction.ASC, "orden"))) {
            List<FormularioResponse> formularios = new ArrayList<>();

            for (Formulario formulario : formularioRepository.findByModuloId(modulo.getId(), Sort.by(Sort.Direction.ASC, "orden"))) {
                List<AccionResponse> acciones = new ArrayList<>();

                for (Accion accion : accionRepository.findByFormularioId(formulario.getId())) {

                    acciones.add(new AccionResponse(accion.getId(), accion.getNombre(), PrivilegioTipo.PERMISO_TIPO_ACCION));
                }

                if (validacionActiveDirectory == ParametroID.LOCAL) {
                    if (formulario.getId() != ParametroID.FORMULARIO_GRUPO) {
                        formularios.add(new FormularioResponse(formulario.getId(), formulario.getNombre(),
                                formulario.getOrden(), PrivilegioTipo.PERMISO_TIPO_FORMULARIO,
                                formulario.getUrl(), formulario.getIcono(), acciones));
                    }
                } else {
                	//if (!validacionGrupoLDAP){
                	    formularios.add(new FormularioResponse(formulario.getId(), formulario.getNombre(),
                                formulario.getOrden(), PrivilegioTipo.PERMISO_TIPO_FORMULARIO,
                                formulario.getUrl(), formulario.getIcono(), acciones));
                	//}  
                }
           

            }

            modulos.add(new ModuloResponse(modulo.getId(), modulo.getNombre(), modulo.getOrden(),
                    PrivilegioTipo.PERMISO_TIPO_MODULO, modulo.getUrl(), modulo.getIcono(),
                    formularios));
        }

        sb = new StringBuilder();
        sb.append("-------------------RESPONSE----------------------\n").
                append("token ").append(token).append(", \n").
                append("DATA ").append(modulos).append(", \n").
                append("------------------------------------------------\n");

        log.info(sb.toString());

        return modulos;
    }

    @GetMapping("/rol/{rolId}")
    List<AccionResponse> obtenerPlantillaPermisos(@RequestHeader(value = JwtTokenUtil.KEY_TOKEN) String token,
            @RequestHeader(value = JwtTokenUtil.IP_CLIENT) String ipClient,
            @RequestHeader(value = JwtTokenUtil.FORM) String form, @PathVariable @Min(value = 1, message = "Identificador invalido, no puede ser menor a 1")
            @Max(value = 999999999, message = "Identificador invalido") int rolId) {
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
                append("rolId ").append(rolId).append(", \n").
                append("------------------------------------------------\n");

        log.info(sb.toString());

        List<AccionResponse> acciones = new ArrayList<>();
        int validacionActiveDirectory = ((BigDecimal) parametroService.getParamVal(ParametroID.VALIDACION_ACTIVE_DIRECTORY)).intValue();
        Boolean validacionGrupoLDAP=(Boolean) parametroService.getParamVal(ParametroID.ACTIVE_DIRECTORY_GRUPOLDAP);
        
        for (RolAccion rolAccion : rolAccionRepository.findAllByRolId(rolId)) {
            Optional<Accion> optinal = accionRepository.findById(rolAccion.getAccionId());

            if (optinal.isPresent()) {
                Accion accion = optinal.get();
                //quitar la visibilidad en parametro 1 ldap, 2 local, 3 hibrido
                if (validacionActiveDirectory == ParametroID.LOCAL) {
                    if (accion.getFormularioId() != ParametroID.FORMULARIO_GRUPO ) {
                        acciones.add(new AccionResponse(accion.getId(), accion.getNombre(), PrivilegioTipo.PERMISO_TIPO_ACCION));
                    }
                } else {
                    acciones.add(new AccionResponse(accion.getId(), accion.getNombre(), PrivilegioTipo.PERMISO_TIPO_ACCION));
                }
            }
        }

        sb = new StringBuilder();
        sb.append("-------------------RESPONSE----------------------\n").
                append("token ").append(token).append(", \n").
                append("DATA ").append(acciones).append(", \n").
                append("------------------------------------------------\n");

        log.info(sb.toString());

        return acciones;
    }

    @PostMapping
    ResponseEntity<?> guardarPermisos(@RequestHeader(value = JwtTokenUtil.KEY_TOKEN) String token,
            @RequestHeader(value = JwtTokenUtil.IP_CLIENT) String ipClient,
            @RequestHeader(value = JwtTokenUtil.FORM) String form,
            @Valid @RequestBody PermisoRequest request) throws URISyntaxException, ApiException {
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

        rolAccionRepository.findAllByRolId(request.getRolId()).stream().forEach(ra -> {
            rolAccionRepository.delete(ra);
        });

        List<RolAccion> rolAcciones = new ArrayList<>();

        request.getAcciones().forEach((accionId) -> {
            rolAcciones.add(rolAccionRepository.save(new RolAccion(null, request.getRolId(), accionId)));
        });

        guardarBitacora(token, ipClient, form, "Actualizando permisos del rol " + request.getRolId() + ", " + rolAcciones);

        ResponseEntity<Object> out = ResponseEntity.ok().build();

        sb = new StringBuilder();
        sb.append("-------------------RESPONSE----------------------\n").
                append("token ").append(token).append(", \n").
                append("DATA ").append(out).append(", \n").
                append("------------------------------------------------\n");

        log.info(sb.toString());

        return out;
    }

    @GetMapping("/tipos/parametros/rol/{rolId}")
    List<RolTipoParametroPermisoResponse> obtenerTiposParametrosPermisos(@RequestHeader(value = JwtTokenUtil.KEY_TOKEN) String token,
            @RequestHeader(value = JwtTokenUtil.IP_CLIENT) String ipClient,
            @RequestHeader(value = JwtTokenUtil.FORM) String form,
            @PathVariable @Min(value = 1, message = "Identificador invalido, no puede ser menor a 1")
            @Max(value = 999999999, message = "Identificador invalido") int rolId) {
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
                append("rolId ").append(rolId).append(", \n").
                append("------------------------------------------------\n");

        log.info(sb.toString());

        List<RolTipoParametroPermisoResponse> temp = rolTipoparametroRepository.findAllByRolId(rolId).stream().map(m -> {
            return new RolTipoParametroPermisoResponse(m.getId(), m.getRolId(), m.getTipoParametroId(),
                    tipoparametroRepository.findById(m.getTipoParametroId()).get().getNombre(), m.getTipoPermiso());
        }).collect(Collectors.toList());

        int validacionActiveDirectory = ((BigDecimal) parametroService.getParamVal(ParametroID.VALIDACION_ACTIVE_DIRECTORY)).intValue();
        Boolean validacionGrupoLDAP=(Boolean) parametroService.getParamVal(ParametroID.ACTIVE_DIRECTORY_GRUPOLDAP);
        
        if (validacionActiveDirectory == ParametroID.LOCAL) {
            temp.remove(0);
        }

        for (TipoParametro tipoParametro : tipoparametroRepository.findAll()) {
            if (!temp.stream().anyMatch(t -> t.getTipoParametroId().equals(tipoParametro.getId()))) {
                RolTipoParametroPermiso model = new RolTipoParametroPermiso(null, rolId, tipoParametro.getId(), PermisoTipo.PERMISO_NINGUNO);
                model = rolTipoparametroRepository.save(model);
                guardarBitacora(token, ipClient, form, "Se adiciono automaticamente al rol " + rolId + ", el rol tipo de paramaetro permiso " + model);
            }
        }

        sb = new StringBuilder();
        sb.append("-------------------RESPONSE----------------------\n").
                append("token ").append(token).append(", \n").
                append("DATA ").append(temp).append(", \n").
                append("------------------------------------------------\n");

        log.info(sb.toString());

        return temp;
    }

    @PostMapping("/tipos/parametros")
    ResponseEntity<?> guardarTiposParametrosPermisos(@RequestHeader(value = JwtTokenUtil.KEY_TOKEN) String token,
            @RequestHeader(value = JwtTokenUtil.IP_CLIENT) String ipClient,
            @RequestHeader(value = JwtTokenUtil.FORM) String form,
            @Valid @RequestBody RolTipoParametroPermisoRequest request) {
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

        rolTipoparametroRepository.findAllByRolId(request.getRolId()).stream().forEach(ra -> {
            rolTipoparametroRepository.delete(ra);
        });

        List<RolTipoParametroPermiso> rolTipoParametroPermisos = new ArrayList<>();

        request.getTiposParametrosPermisos().forEach((tipoParametro) -> {
            rolTipoParametroPermisos.add(rolTipoparametroRepository.save(
                    new RolTipoParametroPermiso(null, request.getRolId(), tipoParametro.getTipoParametroId(), tipoParametro.getTipoPermiso())));
        });

        guardarBitacora(token, ipClient, form, "Actualizando tipos de parametros permisos del rol " + request.getRolId() + ", " + rolTipoParametroPermisos);

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
