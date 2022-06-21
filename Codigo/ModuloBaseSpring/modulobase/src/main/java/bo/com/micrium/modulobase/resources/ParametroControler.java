/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bo.com.micrium.modulobase.resources;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
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
import bo.com.micrium.modulobase.model.Parametro;
import bo.com.micrium.modulobase.model.RolTipoParametroPermiso;
import bo.com.micrium.modulobase.repository.IEtiquetaRepository;
import bo.com.micrium.modulobase.repository.IParametroRepository;
import bo.com.micrium.modulobase.repository.IRolRepository;
import bo.com.micrium.modulobase.repository.IRolTipoparametroRepository;
import bo.com.micrium.modulobase.repository.ITipoParametroRepository;
import bo.com.micrium.modulobase.resources.dto.ParametroRequest;
import bo.com.micrium.modulobase.resources.dto.ParametroResponse;
import bo.com.micrium.modulobase.service.ParametroService;
import bo.com.micrium.modulobase.util.ConvercionUtil;
import bo.com.micrium.modulobase.util.ParametroID;
import bo.com.micrium.modulobase.util.PermisoTipo;
import bo.com.micrium.modulobase.validator.ApiException;
import bo.com.micrium.modulobase.validator.ParametroValidator;
import lombok.extern.log4j.Log4j2;

/**
 *
 * @author alepaco.maton
 */
@Log4j2
@RestController
@CrossOrigin
@RequestMapping(value = "/parametros", produces = {MediaType.APPLICATION_JSON_VALUE})
public class ParametroControler extends GenericControler implements ICrudControler<ParametroRequest, ParametroResponse, Integer> {

    @Autowired
    private IParametroRepository repository;

    @Autowired
    private ITipoParametroRepository tipoParametroRepository;

    @Autowired
    private IRolRepository rolRepository;

    @Autowired
    private IRolTipoparametroRepository rolTipoparametroRepository;

    @Autowired
    private ParametroValidator validator;

    @Autowired
    private ParametroService parametroService;
    
    @Autowired
    private IEtiquetaRepository etiquetaRepository;

    @Override
    public Page<ParametroResponse> list(String token, String ipClient, String form, Pageable pageRequest) {
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

        List<RolTipoParametroPermiso> tiposparametrosIds = rolTipoparametroRepository.
                findAllByRolId(rolRepository.findByNombreAndEstadoTrue(obtenerToken()).getId()).
                stream().filter(p -> p.getTipoPermiso() != PermisoTipo.PERMISO_NINGUNO).collect(Collectors.toList());

        Page<ParametroResponse> out = repository.findAllByTipoParametroIdIn(tiposparametrosIds.stream().map(t -> t.getTipoParametroId()).collect(Collectors.toList()), pageRequest).
                map(model -> ConvercionUtil.convertir(model,
                tipoParametroRepository.findById(model.getTipoParametroId()).get(),
                tiposparametrosIds.stream().filter(
                        o -> o.getTipoParametroId() == model.getTipoParametroId()).
                        findFirst().get().getTipoPermiso() == PermisoTipo.PERMISO_LECTURA_ESCRITURA));

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
    public ResponseEntity<ParametroResponse> get(String token, String ipClient, String form,
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

        Optional<Parametro> model = repository.findById(id);
        if (model.isPresent()) {
            ResponseEntity<ParametroResponse> out = ResponseEntity.ok().body(ConvercionUtil.convertir(model.get(),
                    tipoParametroRepository.findById(model.get().getTipoParametroId()).get(), false));

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
    public ResponseEntity<ParametroResponse> create(String token, String ipClient, String form,
            ParametroRequest request, BindingResult result) throws URISyntaxException, ApiException {
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

        Parametro model = repository.save(new Parametro(null, request.getNombre(),
                request.getTipo(), request.getValor(), request.getDescripcion(),
                request.getTipoParametroId()));

        guardarBitacora(token, ipClient, form, "Se adiciono:" + model);

        parametroService.restartParameter();

        ResponseEntity<ParametroResponse> out = ResponseEntity.created(new URI("/parametros/" + model.getId()))
                .body(ConvercionUtil.convertir(model,
                        tipoParametroRepository.findById(model.getTipoParametroId()).get(), false));

        sb = new StringBuilder();
        sb.append("-------------------RESPONSE----------------------\n").
                append("token ").append(token).append(", \n").
                append("DATA ").append(out).append(", \n").
                append("------------------------------------------------\n");

        log.info(sb.toString());

        return out;
    }

    @Override
    public ResponseEntity<ParametroResponse> update(String token, String ipClient, String form,
            ParametroRequest request, Integer id, BindingResult result) throws ApiException {
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

        Parametro model = repository.findById(id).get();

        if (model.getId()==ParametroID.VALIDACION_ACTIVE_DIRECTORY){
        	if (request.getValor().trim().equals(ParametroID.LDAP) || request.getValor().trim().equals(ParametroID.HIBRIDO)){
        		//etiquetaRepository.updateByEstado("grupo",false);
        	}
        }
        model.setNombre(request.getNombre());
        model.setTipo(request.getTipo());
        model.setValor(request.getValor());
        model.setDescripcion(request.getDescripcion());
        model.setTipoParametroId(request.getTipoParametroId());

        model = repository.save(model);

        guardarBitacora(token, ipClient, form, "Se modifico:" + model);

        parametroService.restartParameter();

        ResponseEntity<ParametroResponse> out = ResponseEntity.ok().body(ConvercionUtil.convertir(model,
                tipoParametroRepository.findById(model.getTipoParametroId()).get(), false));

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

        Optional<Parametro> temp = repository.findById(id);

        if (!temp.isPresent()) {
            throw new NoHandlerFoundException("DELETE", "/{id}" + id, HttpHeaders.EMPTY);
        }

        repository.delete(temp.get());

        guardarBitacora(token, ipClient, form, "Se elimino:" + temp.get());

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
