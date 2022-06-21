/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bo.com.micrium.modulobase.validator;

import bo.com.micrium.modulobase.ldap.ActiveDirectory;
import bo.com.micrium.modulobase.ldap.LdapContextException;
import bo.com.micrium.modulobase.model.Grupo;
import bo.com.micrium.modulobase.model.Rol;
import bo.com.micrium.modulobase.repository.IGrupoRepository;
import bo.com.micrium.modulobase.repository.IRolRepository;
import bo.com.micrium.modulobase.resources.dto.GrupoRequest;
import bo.com.micrium.modulobase.service.ParametroService;
import bo.com.micrium.modulobase.util.ParametroID;
import java.util.Optional;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;

/**
 *
 * @author alepaco.maton
 */
@Log4j2
@Component
public class GrupoValidator extends GlobalValidator {

    @Autowired
    IGrupoRepository repository;

    @Autowired
    IRolRepository rolRepository;

    @Autowired
    ParametroService parametroService;

    public void validate(GrupoRequest input, Integer id, Errors errors) {
        if (isBlanck(input.getNombre())) {
            errors.rejectValue("nombre", "field.nombre", "La longitud del nombre debe ser mayor a 0 y menor a 255.");
        }

        {
            Optional<Rol> model = rolRepository.findById(input.getRolId());
            if (!model.isPresent()) {
                errors.rejectValue("rolId", "field.rolId", "Seleccione otro rol.");
            }
        }

        if (errors.hasErrors()) {
            return;
        }

        if (id != null) {
            Optional<Grupo> model = repository.findById(id);

            if (!model.isPresent()) {
                errors.rejectValue("id", "field.invalido", "Identificador de usuario invalido.");
            } else {
                Grupo temp = repository.findByNombreAndEstadoTrue(input.getNombre());

                if (!temp.getRolId().equals(model.get().getRolId())) {
                    errors.rejectValue("nombre", "field.invalido", "Ya se encuentra en uso.");
                }
            }
        } else {
            Grupo temp = repository.findByNombreAndEstadoTrue(input.getNombre());

            if (temp != null) {
                errors.rejectValue("nombre", "field.invalido", "Ya se encuentra en uso.");
            }
        }

        if (errors.hasErrors()) {
            return;
        }

        if (!input.getNombre().matches(parametroService.getParametro(ParametroID.EXPRESION_REGULAR_GENERAL).getValor())) {
            errors.rejectValue("nombre", "field.nombre", parametroService.getParametro(ParametroID.MENSAJE_VALIDACION_GENERAL).getValor());
        }

        try {
            if ( !(new ActiveDirectory(parametroService).validarGrupo(input.getNombre().trim()))) {
                errors.rejectValue("nombre", "field.nombre", "No se encontró un usuario en active directory con el valor de usuario ingresado.");
            }
        } catch (LdapContextException e1) {
            log.error("Error de conexion ldap " + e1.getMessage(), e1);
            errors.rejectValue("nombre", "ldap.conexion", "Error de conexion con el active directory, " + e1.getMessage());
        }
    }

}
