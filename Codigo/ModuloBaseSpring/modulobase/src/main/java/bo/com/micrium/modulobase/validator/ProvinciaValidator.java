package bo.com.micrium.modulobase.validator;

import bo.com.micrium.modulobase.repository.IProvinciaRepository;
import bo.com.micrium.modulobase.resources.dto.ProvinciaRequest;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;

@Log4j2
@Component
public class ProvinciaValidator extends GlobalValidator {

    @Autowired
    protected IProvinciaRepository repository;

    public void validate(ProvinciaRequest input, Integer id, Errors errors) {

    }
}
