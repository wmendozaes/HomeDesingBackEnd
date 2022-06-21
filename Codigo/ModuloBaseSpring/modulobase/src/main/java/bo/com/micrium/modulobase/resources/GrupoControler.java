/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bo.com.micrium.modulobase.resources;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Optional;
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
import bo.com.micrium.modulobase.model.Grupo;
import bo.com.micrium.modulobase.repository.IGrupoRepository;
import bo.com.micrium.modulobase.repository.IRolRepository;
import bo.com.micrium.modulobase.resources.dto.GrupoRequest;
import bo.com.micrium.modulobase.resources.dto.GrupoResponse;
import bo.com.micrium.modulobase.util.ConvercionUtil;
import bo.com.micrium.modulobase.util.abm.GrupoEstado;
import bo.com.micrium.modulobase.validator.ApiException;
import bo.com.micrium.modulobase.validator.GrupoValidator;
import lombok.extern.log4j.Log4j2;

/**
 *
 * @author alepaco.maton
 */
@Log4j2
@RestController
@CrossOrigin
@RequestMapping(value = "/grupos", produces = {MediaType.APPLICATION_JSON_VALUE})
public class GrupoControler extends GenericControler implements ICrudControler<GrupoRequest, GrupoResponse, Integer> {

    @Autowired
    private IGrupoRepository repository;

    @Autowired
    private IRolRepository rolRepository;

    @Autowired
    private GrupoValidator validator;

    @Override
    public Page<GrupoResponse> list(String token, String ipClient, String form, Pageable pageRequest) {
        if (ipClient == null || ipClient.isEmpty() || ipClient.equals("127.0.0.1") || ipClient.equals("localhost")) {
            ipClient = httpServletRequest.getRemoteAddr();
        }

        StringBuilder sb = new StringBuilder();
        sb.append("-------------------REQUEST----------------------\n").
                append("token ").append(token).append(", \n").
                append("form ").append(form).append(", \n").
                append("ipClient ").append(ipClient).append(", \n").
                append("url ").append(httpServletRequest.getRequestURL()).append(", \n").
                append("pageRequest ").append(pageRequest).append(", \n").
                append("------------------------------------------------\n");
        log.info(sb.toString());

        Page<GrupoResponse> out = repository.findAllByEstadoTrue(pageRequest).map(model -> ConvercionUtil.convertir(model));

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
    public ResponseEntity<GrupoResponse> get(String token, String ipClient, String form,
            Integer id) throws NoHandlerFoundException {
        if (ipClient == null || ipClient.isEmpty() || ipClient.equals("127.0.0.1") || ipClient.equals("localhost")) {
            ipClient = httpServletRequest.getRemoteAddr();
        }

        StringBuilder sb = new StringBuilder();
        sb.append("-------------------REQUEST----------------------\n").
                append("token ").append(token).append(", \n").
                append("form ").append(form).append(", \n").
                append("ipClient ").append(ipClient).append(", \n").
                append("url ").append(httpServletRequest.getRequestURL()).append(", \n").
                append("id ").append(id).append(", \n").
                append("------------------------------------------------\n");

        log.info(sb.toString());

        Optional<Grupo> model = repository.findById(id);
        if (model.isPresent()) {
            ResponseEntity<GrupoResponse> out = ResponseEntity.ok().body(ConvercionUtil.convertir(model.get()));

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
    public ResponseEntity<GrupoResponse> create(String token, String ipClient, String form,
            GrupoRequest request, BindingResult result) throws URISyntaxException, ApiException {
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

        Grupo model = repository.save(new Grupo(null, request.getNombre(), request.getDescripcion(),
                rolRepository.findById(request.getRolId()).get(), GrupoEstado.HABILITADO));

        guardarBitacora(token, ipClient, form, "Se adiciono:" + model);

        ResponseEntity<GrupoResponse> out = ResponseEntity.created(new URI("/grupos/" + model.getId()))
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
    public ResponseEntity<GrupoResponse> update(String token, String ipClient, String form,
            GrupoRequest request, Integer id, BindingResult result) throws ApiException {
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

        Grupo model = repository.findById(id).get();

        model.setNombre(request.getNombre());
        model.setDescripcion(request.getDescripcion());
        model.setRolId(rolRepository.findById(request.getRolId()).get());

        model = repository.save(model);

        guardarBitacora(token, ipClient, form, "Se modifico:" + model);

        return ResponseEntity.ok().body(ConvercionUtil.convertir(model));
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

        Optional<Grupo> temp = repository.findById(id);

        if (!temp.isPresent()) {
            throw new NoHandlerFoundException("DELETE", "/{id}" + id, HttpHeaders.EMPTY);
        }

        Grupo model = temp.get();

        if (model.isEstado() == GrupoEstado.INHABILITADO) {
            return ResponseEntity.noContent().build();
        }

        model.setEstado(GrupoEstado.INHABILITADO);

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
