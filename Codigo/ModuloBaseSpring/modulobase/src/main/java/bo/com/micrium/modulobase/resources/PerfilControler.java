/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bo.com.micrium.modulobase.resources;

import java.util.Arrays;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import bo.com.micrium.modulobase.model.Usuario;
import bo.com.micrium.modulobase.repository.IUsuarioRepository;
import bo.com.micrium.modulobase.resources.dto.CambioContrasenaRequest;
import bo.com.micrium.modulobase.resources.dto.CambioContrasenaRequestLogin;
import bo.com.micrium.modulobase.security.jwt.JwtTokenUtil;
import bo.com.micrium.modulobase.util.abm.UsuarioEstado;
import bo.com.micrium.modulobase.validator.ApiException;
import bo.com.micrium.modulobase.validator.UsuarioValidator;
import lombok.extern.log4j.Log4j2;

/**
 *
 * @author alepaco.maton
 */
@Log4j2
@RestController
@CrossOrigin
@RequestMapping(value = "/perfiles", produces = {MediaType.APPLICATION_JSON_VALUE})
public class PerfilControler extends GenericControler {

    public static final String RESOURCE_CAMBIOLOGIN = "/perfiles" + "/contrasena/cambioLogin";
	   
    @Autowired
    protected IUsuarioRepository repository;

    @Autowired
    protected UsuarioValidator validator;

    @Autowired
    BCryptPasswordEncoder passwordEncoder;

    @PostMapping("/contrasena/cambioLogin")
    public ResponseEntity<?> cambiarContrasenaLogin(@RequestHeader(value = JwtTokenUtil.KEY_TOKEN) String token,
            @RequestHeader(value = JwtTokenUtil.IP_CLIENT) String ipClient,
            @RequestHeader(value = JwtTokenUtil.FORM) String form,
            @Valid @RequestBody CambioContrasenaRequestLogin request,
            BindingResult result) throws ApiException {
        if (ipClient == null || ipClient.isEmpty() || ipClient.equals("127.0.0.1") || ipClient.equals("localhost")) {
            ipClient = httpServletRequest.getRemoteAddr();
        }

        Usuario model = repository.findByNombreUsuarioAndEstadoIn(request.getUserName(),
                Arrays.asList(UsuarioEstado.HABILITADO, UsuarioEstado.BLOQUEADO));

        validator.validate(model, request, result);

        if (result.hasErrors()) {
            throw new ApiException(result, "Errores en la validacion");
        }

        model.setContrasena(this.passwordEncoder.encode(request.getContrasenaNueva()));

        model = repository.save(model);

        guardarBitacoraSinToken(request.getUserName(), ipClient, form, "Se cambio la contraseña:" + model);

        ResponseEntity<Object> out = ResponseEntity.ok().build();
        
        return out;
    }
    
    @PostMapping("/contrasena/cambio")
    public ResponseEntity<?> cambiarContrasena(@RequestHeader(value = JwtTokenUtil.KEY_TOKEN) String token,
            @RequestHeader(value = JwtTokenUtil.IP_CLIENT) String ipClient,
            @RequestHeader(value = JwtTokenUtil.FORM) String form,
            @Valid @RequestBody CambioContrasenaRequest request,
            BindingResult result) throws ApiException {
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

        Usuario model = repository.findByNombreUsuarioAndEstadoIn(
                this.jwtTokenUtil.getUsernameFromToken(token),
                Arrays.asList(UsuarioEstado.HABILITADO, UsuarioEstado.BLOQUEADO));

        validator.validate(model, request, result);

        if (result.hasErrors()) {
            throw new ApiException(result, "Errores en la validacion");
        }

        model.setContrasena(this.passwordEncoder.encode(request.getContrasenaNueva()));

        model = repository.save(model);

        guardarBitacora(token, ipClient, form, "Se cambio la contraseña:" + model);

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
