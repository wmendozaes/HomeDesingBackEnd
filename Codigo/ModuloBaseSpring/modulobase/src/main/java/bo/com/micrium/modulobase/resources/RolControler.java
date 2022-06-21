/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bo.com.micrium.modulobase.resources;

import bo.com.micrium.modulobase.model.Rol;
import bo.com.micrium.modulobase.model.RolTipoParametroPermiso;
import bo.com.micrium.modulobase.model.TipoParametro;
import bo.com.micrium.modulobase.repository.IGrupoRepository;
import bo.com.micrium.modulobase.repository.IRolRepository;
import bo.com.micrium.modulobase.repository.IRolTipoparametroRepository;
import bo.com.micrium.modulobase.repository.ITipoParametroRepository;
import bo.com.micrium.modulobase.repository.IUsuarioRepository;
import bo.com.micrium.modulobase.resources.dto.RolRequest;
import bo.com.micrium.modulobase.resources.dto.RolResponse;
import bo.com.micrium.modulobase.util.ConvercionUtil;
import bo.com.micrium.modulobase.util.PermisoTipo;
import bo.com.micrium.modulobase.util.abm.RolEstado;
import bo.com.micrium.modulobase.util.abm.UsuarioEstado;
import bo.com.micrium.modulobase.validator.ApiException;
import bo.com.micrium.modulobase.validator.RolValidator;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.Optional;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.NoHandlerFoundException;

/**
 *
 * @author alepaco.maton
 */
@Log4j2
@RestController
@CrossOrigin
@RequestMapping(value = "/roles", produces = {MediaType.APPLICATION_JSON_VALUE})
public class RolControler extends GenericControler implements ICrudControler<RolRequest, RolResponse, Integer> {

    @Autowired
    IRolRepository repository;

    @Autowired
    IUsuarioRepository usuarioRepository;

    @Autowired
    IGrupoRepository grupoRepository;

    @Autowired
    RolValidator validator;

    @Autowired
    ITipoParametroRepository tipoParametroRepository;

    @Autowired
    IRolTipoparametroRepository rolTipoparametroRepository;

    @Override
    public Page<RolResponse> list(String token, String ipClient, String form, Pageable pageRequest) {
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

        Page<RolResponse> out = repository.findAllByIdNotAndEstadoTrue(Rol.SUPER_ADMINISTRADOR, pageRequest).map(model -> ConvercionUtil.convertir(model));

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
    public ResponseEntity<RolResponse> get(String token, String ipClient, String form, Integer id) throws NoHandlerFoundException {
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

        Optional<Rol> model = repository.findById(id);
        if (model.isPresent()) {
            ResponseEntity<RolResponse> out = ResponseEntity.ok().body(ConvercionUtil.convertir(model.get()));

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
    public ResponseEntity<RolResponse> create(String token, String ipClient, String form,
            RolRequest request, BindingResult result) throws URISyntaxException, ApiException {
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

        validator.validate(request,
                null, result);

        if (result.hasErrors()) {
            throw new ApiException(result, "Errores en la validacion");
        }

        Rol model = repository.save(new Rol(null, request.getNombre(), request.getDescripcion(), RolEstado.HABILITADO));

        guardarBitacora(token, ipClient, form,
                "Se adiciono:" + model);

        for (TipoParametro tipoParametro : tipoParametroRepository.findAll()) {
            RolTipoParametroPermiso modelRTP = new RolTipoParametroPermiso(null, model.getId(), tipoParametro.getId(), PermisoTipo.PERMISO_NINGUNO);
            modelRTP = rolTipoparametroRepository.save(modelRTP);
            guardarBitacora(token, ipClient, form, "Se adiciono " + modelRTP);
        }

        ResponseEntity<RolResponse> out = ResponseEntity.created(
                new URI("/roles/" + model.getId()))
                .body(ConvercionUtil.convertir(model));
        
        sb = new StringBuilder();
        sb.append("-------------------RESPONSE----------------------\n").
                append("token ").append(token).append(", \n").
                append("DATA ").append(out).append(", \n").
                append("------------------------------------------------\n");

        log.info(sb.toString());

        return out;
    }

    @Override
    public ResponseEntity<RolResponse> update(String token, String ipClient, String form,
            RolRequest request, Integer id, BindingResult result) throws ApiException {
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

        Rol model = repository.findById(id).get();

        model.setNombre(request.getNombre());
        model.setDescripcion(request.getDescripcion());

        model = repository.save(model);

        guardarBitacora(token, ipClient, form, "Se modifico:" + model);

        ResponseEntity<RolResponse> out = ResponseEntity.ok().body(ConvercionUtil.convertir(model));
        
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

        Optional<Rol> temp = repository.findById(id);

        if (!temp.isPresent()) {
            throw new NoHandlerFoundException("DELETE", "/{id}" + id, HttpHeaders.EMPTY);
        }

        Rol model = temp.get();

        if (model.isEstado() == RolEstado.INHABILITADO) {
            return ResponseEntity.noContent().build();
        }

        long numeroUsuariosConRol = usuarioRepository.countByEstadoInAndRolIdId(Arrays.asList(UsuarioEstado.HABILITADO, UsuarioEstado.BLOQUEADO), id);

        if (numeroUsuariosConRol > 0) {
            throw new RuntimeException("No puede ser eliminado porque esta en uso por almenos un usuario");
        }

        long numeroGruposConRol = grupoRepository.countByEstadoTrueAndRolIdId(id);

        if (numeroGruposConRol > 0) {
            throw new RuntimeException("No puede ser eliminado porque esta en uso por almenos un grupo");
        }

        model.setEstado(RolEstado.INHABILITADO);

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

}
