package bo.com.micrium.modulobase.resources;

import bo.com.micrium.modulobase.model.ProyectoTipo;
import bo.com.micrium.modulobase.repository.IProyectoTipoRepository;
import bo.com.micrium.modulobase.resources.dto.ProyectoTipoRequest;
import bo.com.micrium.modulobase.resources.dto.ProyectoTipoResponse;
import bo.com.micrium.modulobase.util.ConvercionUtil;
import bo.com.micrium.modulobase.validator.ApiException;
import bo.com.micrium.modulobase.validator.ProyectoTipoValidator;
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

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.Optional;


@Log4j2
@RestController
@CrossOrigin
@RequestMapping(value = "/proyectos/tipos", produces = {MediaType.APPLICATION_JSON_VALUE})
public class ProyectoTipoControler extends GenericControler implements ICrudControler<ProyectoTipoRequest, ProyectoTipoResponse, Integer> {

    @Autowired
    protected IProyectoTipoRepository repository;

    @Autowired
    protected ProyectoTipoValidator validator;


    @Override
    public Page<ProyectoTipoResponse> list(String token, String ipClient, String form, Pageable pageRequest) {
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

        Page<ProyectoTipoResponse> out = repository.findAllByEstadoIn(Arrays.asList(1), pageRequest).map(model -> ConvercionUtil.convertir(model));
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
    public ResponseEntity<ProyectoTipoResponse> get(String token, String ipClient, String form, Integer id) throws NoHandlerFoundException {
        return null;
    }

    @Override
    public ResponseEntity<ProyectoTipoResponse> create(String token, String ipClient, String form, ProyectoTipoRequest request, BindingResult result) throws URISyntaxException, ApiException {
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

        ProyectoTipo model  = repository.save(new ProyectoTipo(null, request.getNombre(), request.getDescripcion(), 1));

        guardarBitacora(token, ipClient, form,
                "Se adiciono:" + model);

        ResponseEntity<ProyectoTipoResponse> out = ResponseEntity.created(
                        new URI("/proyectos/tipos/" + model.getId()))
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
    public ResponseEntity<ProyectoTipoResponse> update(String token, String ipClient, String form, ProyectoTipoRequest request, Integer id, BindingResult result) throws ApiException {
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

        ProyectoTipo model = repository.findById(id).get();

        model.setNombre(request.getNombre());
        model.setDescripcion(request.getDescripcion());

        model = repository.save(model);

        guardarBitacora(token, ipClient, form, "Se modifico:" + model);

        ResponseEntity<ProyectoTipoResponse> out = ResponseEntity.ok().body(ConvercionUtil.convertir(model));

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

        Optional<ProyectoTipo> temp = repository.findById(id);

        if (!temp.isPresent()) {
            throw new NoHandlerFoundException("DELETE", "/{id}" + id, HttpHeaders.EMPTY);
        }

        ProyectoTipo model = temp.get();

        model.setEstado(0);

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
