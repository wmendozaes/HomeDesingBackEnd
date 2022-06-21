/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bo.com.micrium.modulobase.validator;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Date;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;

import bo.com.micrium.modulobase.ldap.ActiveDirectory;
import bo.com.micrium.modulobase.ldap.LdapContextException;
import bo.com.micrium.modulobase.model.Rol;
import bo.com.micrium.modulobase.model.Usuario;
import bo.com.micrium.modulobase.repository.IRolRepository;
import bo.com.micrium.modulobase.repository.IUsuarioRepository;
import bo.com.micrium.modulobase.resources.dto.CambioContrasenaRequest;
import bo.com.micrium.modulobase.resources.dto.CambioContrasenaRequestLogin;
import bo.com.micrium.modulobase.resources.dto.UsuarioRequest;
import bo.com.micrium.modulobase.service.ParametroService;
import bo.com.micrium.modulobase.util.ParametroID;
import bo.com.micrium.modulobase.util.UsuarioTipo;
import bo.com.micrium.modulobase.util.abm.UsuarioEstado;
import lombok.extern.log4j.Log4j2;

/**
 *
 * @author alepaco.maton
 */
@Log4j2
@Component
public class UsuarioValidator extends GlobalValidator {

    @Autowired
    IUsuarioRepository usuarioRepository;

    @Autowired
    IRolRepository rolRepository;

    @Autowired
    ParametroService parametroService;

    @Autowired
    BCryptPasswordEncoder passwordEncoder;

    public void validate(UsuarioRequest input, Integer id, Errors errors) {
        if (isBlanck(input.getNombreUsuario())) {
            errors.rejectValue("nombreUsuario", "field.nombreUsuario", "La longitud del nombre debe ser mayor a 0 y menor a 50.");
        }

        if (!input.getNombreUsuario().matches(parametroService.getParametro(ParametroID.EXPRESION_REGULAR_NOMBRE_USUARIO).getValor())) {
            errors.rejectValue("nombreUsuario", "field.nombreUsuario", parametroService.getParametro(ParametroID.MENSAJE_VALIDACION_NOMBRE_USUARIO).getValor());
        }

        if (input.getValidarFechaLimite()){
        	if (input.getFechaLimite().compareTo(new Date())<0){
        		errors.rejectValue("validarFechaLimite","field.validarFechaLimite","fecha limite tiene que ser mayor a la fecha actual");
        	}
        }
        
        if (errors.hasErrors()) {
            return;
        }

        if (id != null) {
            Optional<Usuario> model = usuarioRepository.findById(id);

            if (!model.isPresent()) {
                errors.rejectValue("id", "field.invalido", "Identificador de usuario invalido.");
            } else {
                Usuario temp = usuarioRepository.findByNombreUsuarioAndEstadoIn(input.getNombreUsuario(),
                        Arrays.asList(UsuarioEstado.HABILITADO, UsuarioEstado.BLOQUEADO));

                if (temp != null && !temp.getId().equals(model.get().getId())) {
                    errors.rejectValue("nombreUsuario", "field.invalido", "Ya se encuentra en uso.");
                }

                if (temp != null && temp.getTipo() == UsuarioTipo.USUARIO_ACTIVE_DIRECTORY) {
                    String name;
                    try {
                        name = new ActiveDirectory(parametroService).getNombreCompleto(input.getNombreUsuario().trim());

                        if (name.trim().isEmpty()) {
                            errors.rejectValue("nombreUsuario", "field.nombreUsuario", "No se encontró un usuario en active directory con el valor de usuario ingresado.");
                        }
                    } catch (LdapContextException e1) {
                        log.error("Error de conexion ldap " + e1.getMessage(), e1);
                        errors.rejectValue("nombreUsuario", "ldap.conexion", "Error de conexion con el active directory, " + e1.getMessage());
                    }
                }
            }
        } else {
            Usuario temp = usuarioRepository.findByNombreUsuarioAndEstadoIn(input.getNombreUsuario(),
                    Arrays.asList(UsuarioEstado.HABILITADO, UsuarioEstado.BLOQUEADO));

            if (temp != null) {
                errors.rejectValue("nombreUsuario", "field.invalido", "Ya se encuentra en uso.");
            }

            int validacion = ((BigDecimal)  parametroService.getParamVal(ParametroID.VALIDACION_ACTIVE_DIRECTORY)).intValue() ;

            if (validacion==ParametroID.LDAP) {
                String name;
                try {
                    name = new ActiveDirectory(parametroService).getNombreCompleto(input.getNombreUsuario().trim());

                    if (name.trim().isEmpty()) {
                        errors.rejectValue("nombreUsuario", "field.nombreUsuario", "No se encontró un usuario en active directory con el valor de usuario ingresado.");
                    }
                } catch (LdapContextException e1) {
                    log.error("Error de conexion ldap " + e1.getMessage(), e1);
                    errors.rejectValue("nombreUsuario", "ldap.conexion", "Error de conexion con el active directory, " + e1.getMessage());
                }
            }
        }

        Optional<Rol> model = rolRepository.findById(input.getRolId());
        if (!model.isPresent()) {
            errors.rejectValue("rolId", "field.rolId", "Seleccione otro rol.");
        }
    }

    public void validate(Usuario model, CambioContrasenaRequest input, Errors errors) {
        if (isBlanck(input.getContrasenaAntigua())) {
            errors.rejectValue("contrasenaAntigua", "field.contrasenaAntigua", "La longitud de la contraseña debe ser mayor a 0 y menor a 50.");
        }

        if (isBlanck(input.getContrasenaNueva())) {
            errors.rejectValue("contrasenaNueva", "field.contrasenaNueva", "La longitud de la contraseña debe ser mayor a 0 y menor a 50.");
        }

        if (model == null) {
            errors.rejectValue("usuario", "field.usuario", "Usuario invalido.");
            return;
        }

        if (UsuarioTipo.SUPER_USUARIO != model.getTipo() && UsuarioTipo.USUARIO_NORMAL != model.getTipo()) {
            errors.rejectValue("tipo", "field.tipo", "Tipo de usuario no puede cambiar de contraseña.");
        }

        if (!passwordEncoder.matches(input.getContrasenaAntigua(), model.getContrasena())) {
            errors.rejectValue("contrasenaAntigua", "field.contrasenaAntigua", "Contraseña actual incorrecta.");
        }
    }
    
    public void validate(Usuario model, CambioContrasenaRequestLogin input, Errors errors) {
        if (isBlanck(input.getContrasenaAntigua())) {
            errors.rejectValue("contrasenaAntigua", "field.contrasenaAntigua", "La longitud de la contraseña debe ser mayor a 0 y menor a 50.");
        }

        if (isBlanck(input.getContrasenaNueva())) {
            errors.rejectValue("contrasenaNueva", "field.contrasenaNueva", "La longitud de la contraseña debe ser mayor a 0 y menor a 50.");
        }

        if (model == null) {
            errors.rejectValue("usuario", "field.usuario", "Usuario invalido.");
            return;
        }

        if (UsuarioTipo.SUPER_USUARIO != model.getTipo() && UsuarioTipo.USUARIO_NORMAL != model.getTipo()) {
            errors.rejectValue("tipo", "field.tipo", "Tipo de usuario no puede cambiar de contraseña.");
        }

    }

}
