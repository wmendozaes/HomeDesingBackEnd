/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bo.com.micrium.modulobase.validator;

import bo.com.micrium.modulobase.model.Parametro;
import bo.com.micrium.modulobase.repository.IParametroRepository;
import bo.com.micrium.modulobase.repository.ITipoParametroRepository;
import bo.com.micrium.modulobase.resources.dto.ParametroRequest;
import bo.com.micrium.modulobase.service.ParametroService;
import bo.com.micrium.modulobase.util.ParametroID;
import bo.com.micrium.modulobase.util.ParametroTipo;
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
public class ParametroValidator extends GlobalValidator {

    @Autowired
    IParametroRepository repository;

    @Autowired
    ITipoParametroRepository tipoParametroRepository;

    @Autowired
    ParametroService parametroService;

    public void validate(ParametroRequest input, Integer id, Errors errors) {
        if (id != null) {
            Optional<Parametro> model = repository.findById(id);

            if (!model.isPresent()) {
                errors.rejectValue("id", "field.invalido", "Identificador de usuario invalido.");
            } else {
                Parametro temp = repository.findByNombre(input.getNombre());

                if (temp != null && !temp.getId().equals(model.get().getId())) {
                    errors.rejectValue("nombre", "field.invalido", "Ya se encuentra en uso.");
                }
            }
        } else {
            Parametro temp = repository.findByNombre(input.getNombre());

            if (temp != null) {
                errors.rejectValue("nombre", "field.invalido", "Ya se encuentra en uso.");
            }
        }

        if (isBlanck(input.getNombre())) {
            errors.rejectValue("nombre", "field.nombre", "La longitud del nombre debe ser mayor a 0 y menor a 255.");
        }
        
        if (input.getValor() != null && input.getValor().length()>4000) {
            errors.rejectValue("valor", "field.valor", "La longitud del nombre debe ser menor a 4000.");
        }
        
        if (!ParametroTipo.esValido(input.getTipo())) {
            errors.rejectValue("tipo", "field.tipo", "Tipo de valor invalido.");
        }

        if (errors.hasErrors()) {
            return;
        }

        if (!input.getNombre().matches(parametroService.getParametro(ParametroID.EXPRESION_REGULAR_GENERAL).getValor())) {
            errors.rejectValue("nombre", "field.nombre", parametroService.getParametro(ParametroID.MENSAJE_VALIDACION_GENERAL).getValor());
        }
        
        if (!tipoParametroRepository.findById(input.getTipoParametroId()).isPresent()) {
            errors.rejectValue("tipoParametroId", "field.tipoParametroId", "Tipo de parametro invalido.");    
        }
    }

}
