package bo.com.micrium.modulobase.validator;

import bo.com.micrium.modulobase.repository.ITrabajoTipoRepository;
import bo.com.micrium.modulobase.resources.dto.TrabajoTipoRequest;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;

@Log4j2
@Component
public class TrabajoTipoValidator extends GlobalValidator {

    @Autowired
    protected ITrabajoTipoRepository repository;

    public void validate(TrabajoTipoRequest input, Integer id, Errors errors) {

    }
}
