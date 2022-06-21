/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bo.com.micrium.modulobase.validator;

import bo.com.micrium.modulobase.repository.IEtiquetaRepository;
import bo.com.micrium.modulobase.resources.dto.EtiquetaRequest;
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
public class EtiquetaValidator extends GlobalValidator {

    @Autowired
    IEtiquetaRepository repository;

    public void validate(EtiquetaRequest input, Integer id, Errors errors) {
    }

}
