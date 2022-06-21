/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bo.com.micrium.modulobase.resources;

import bo.com.micrium.modulobase.model.Etiqueta;
import bo.com.micrium.modulobase.repository.IEtiquetaRepository;
import bo.com.micrium.modulobase.resources.dto.EtiquetaRequest;
import bo.com.micrium.modulobase.resources.dto.EtiquetaResponse;
import bo.com.micrium.modulobase.util.ConvercionUtil;
import bo.com.micrium.modulobase.util.abm.EtiquetaEstado;
import bo.com.micrium.modulobase.validator.ApiException;
import bo.com.micrium.modulobase.validator.EtiquetaValidator;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import javax.validation.Valid;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
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
@RequestMapping(value = EtiquetaControler.RESOURCE, produces = {MediaType.APPLICATION_JSON_VALUE})
public class EtiquetaControler extends GenericControler implements ICrudControler<EtiquetaRequest, EtiquetaResponse, Integer> {

    public static final String RESOURCE = "/etiquetas";
    public static final String RESOURCE_BY_LLAVE = RESOURCE + "/by/llave";
    public static final String RESOURCE_BY_GRUPO = RESOURCE + "/by/grupo";

    @Autowired
    IEtiquetaRepository repository;

    @Autowired
    private EtiquetaValidator validator;

    @PostMapping("/by/llave")
    List<EtiquetaResponse> findAllByLlaveIn(@Valid @RequestBody List<String> request) {
        StringBuilder sb = new StringBuilder();
        sb.append("-------------------REQUEST----------------------\n").
                append("ipClient ").append(httpServletRequest.getRemoteAddr()).append(", \n").
                append("url ").append(httpServletRequest.getRequestURL()).append(", \n").
                append("metodo ").append(httpServletRequest.getMethod()).append(", \n").
                append("request ").append(request).append(", \n").
                append("------------------------------------------------\n");

        log.info(sb.toString());

        List<EtiquetaResponse> out = repository.findAllByLlaveIn(request).stream().map(model -> ConvercionUtil.convertir(model)).collect(Collectors.toList());

        sb = new StringBuilder();
        sb.append("-------------------RESPONSE----------------------\n").
                append("ipClient ").append(httpServletRequest.getRemoteAddr()).append(", \n").
                append("DATA ").append(out).append(", \n").
                append("------------------------------------------------\n");

        log.info(sb.toString());

        return out;
    }

    @PostMapping("/by/grupo")
    List<EtiquetaResponse> findAllByGrupo(@Valid @RequestBody List<String> request) {
        StringBuilder sb = new StringBuilder();
        sb.append("-------------------REQUEST----------------------\n").
                append("ipClient ").append(httpServletRequest.getRemoteAddr()).append(", \n").
                append("url ").append(httpServletRequest.getRequestURL()).append(", \n").
                append("metodo ").append(httpServletRequest.getMethod()).append(", \n").
                append("request ").append(request).append(", \n").
                append("------------------------------------------------\n");

        log.info(sb.toString());

        List<EtiquetaResponse> out = repository.findAllByGrupoIn(request).stream().map(model -> ConvercionUtil.convertir(model)).collect(Collectors.toList());

        sb = new StringBuilder();
        sb.append("-------------------RESPONSE----------------------\n").
                append("ipClient ").append(httpServletRequest.getRemoteAddr()).append(", \n").
                append("DATA ").append(out).append(", \n").
                append("------------------------------------------------\n");

        log.info(sb.toString());

        return out;
    }

    @Override
    public Page<EtiquetaResponse> list(String token, String ipClient, String form, Pageable pageRequest) {
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

        Page<EtiquetaResponse> out = repository.findAllByEstadoTrue(pageRequest).map(model -> ConvercionUtil.convertir(model));

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
    public ResponseEntity<EtiquetaResponse> get(String token, String ipClient, String form,
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
                append("metodo ").append(httpServletRequest.getMethod()).append(", \n").
                append("id ").append(id).append(", \n").
                append("------------------------------------------------\n");

        log.info(sb.toString());

        Optional<Etiqueta> model = repository.findById(id);
        if (model.isPresent()) {
            ResponseEntity<EtiquetaResponse> out = ResponseEntity.ok().body(ConvercionUtil.convertir(model.get()));
            
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
    public ResponseEntity<EtiquetaResponse> create(String token, String ipClient, String form,
            EtiquetaRequest request, BindingResult result) throws URISyntaxException, ApiException {
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

        Etiqueta model = repository.save(new Etiqueta(null, request.getLlave(), request.getValor(), request.getGrupo(),true));

        guardarBitacora(token, ipClient, form, "Se adiciono:" + model);

        ResponseEntity<EtiquetaResponse> out = ResponseEntity.created(new URI("/etiquetas/" + model.getId()))
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
    public ResponseEntity<EtiquetaResponse> update(String token, String ipClient, String form,
            EtiquetaRequest request, Integer id, BindingResult result) throws ApiException {
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

        Etiqueta model = repository.findById(id).get();

        model.setValor(request.getValor());

        model = repository.save(model);

        guardarBitacora(token, ipClient, form, "Se modifico:" + model);

        ResponseEntity<EtiquetaResponse> out = ResponseEntity.ok().body(ConvercionUtil.convertir(model));

        sb = new StringBuilder();
        sb.append("-------------------RESPONSE----------------------\n").
                append("token ").append(token).append(", \n").
                append("DATA ").append(out).append(", \n").
                append("------------------------------------------------\n");

        log.info(sb.toString());

        return out;
    }

    @Override
    public ResponseEntity<?> delete(String token, String ipClient, String form, Integer id) throws NoHandlerFoundException, Exception {
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

        Optional<Etiqueta> temp = repository.findById(id);

        if (!temp.isPresent()) {
            throw new NoHandlerFoundException("DELETE", "/{id}" + id, HttpHeaders.EMPTY);
        }

        Etiqueta model = temp.get();

        repository.delete(model);

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
