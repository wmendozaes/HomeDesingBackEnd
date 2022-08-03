package bo.com.micrium.modulobase.validator;

import bo.com.micrium.modulobase.repository.IContratoTipoRepository;
import bo.com.micrium.modulobase.resources.dto.ContratoTipoRequest;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;

@Log4j2
@Component
public class ContratoTipoValidator extends GlobalValidator {

    @Autowired
    protected IContratoTipoRepository repository;

    public void validate(ContratoTipoRequest input, Integer id, Errors errors) {

    }
}
