package bo.com.micrium.modulobase.validator;

import bo.com.micrium.modulobase.repository.IHerramientaTipoRepository;
import bo.com.micrium.modulobase.resources.dto.HerramientaTipoRequest;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;

@Log4j2
@Component
public class HerramientaTipoValidator extends GlobalValidator {

    @Autowired
    protected IHerramientaTipoRepository repository;

    public void validate(HerramientaTipoRequest input, Integer id, Errors errors) {

    }
}