/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bo.com.micrium.modulobase.validator;

import bo.com.micrium.modulobase.model.Rol;
import bo.com.micrium.modulobase.repository.IRolRepository;
import bo.com.micrium.modulobase.resources.dto.RolRequest;
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
public class RolValidator extends GlobalValidator {

    @Autowired
    IRolRepository repository;

    @Autowired
    ParametroService parametroService;

    public void validate(RolRequest input, Integer id, Errors errors) {
        if (isBlanck(input.getNombre())) {
            errors.rejectValue("nombre", "field.nombre", "La longitud del nombre debe ser mayor a 0 y menor a 50.");
        }

        if (!input.getNombre().matches(parametroService.getParametro(ParametroID.EXPRESION_REGULAR_GENERAL).getValor())) {
            errors.rejectValue("nombre", "field.nombre", parametroService.getParametro(ParametroID.MENSAJE_VALIDACION_GENERAL).getValor());
        }

        if (errors.hasErrors()) {
            return;
        }

        if (id != null) {
            Optional<Rol> model = repository.findById(id);

            if (!model.isPresent()) {
                errors.rejectValue("id", "field.invalido", "Identificador de usuario invalido.");
            } else {
                Rol temp = repository.findByNombreAndEstadoTrue(input.getNombre());

                if (temp != null && !temp.getId().equals(model.get().getId())) {
                    errors.rejectValue("nombre", "field.invalido", "Ya se encuentra en uso.");
                }
            }
        } else {
            Rol temp = repository.findByNombreAndEstadoTrue(input.getNombre());

            if (temp != null) {
                errors.rejectValue("nombre", "field.invalido", "Ya se encuentra en uso.");
            }
        }
    }

}
