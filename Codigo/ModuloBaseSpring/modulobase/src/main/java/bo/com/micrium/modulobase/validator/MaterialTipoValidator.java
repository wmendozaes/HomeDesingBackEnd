package bo.com.micrium.modulobase.validator;

import bo.com.micrium.modulobase.repository.IMaterialTipoRepository;
import bo.com.micrium.modulobase.resources.dto.MaterialTipoRequest;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;

@Log4j2
@Component
public class MaterialTipoValidator extends GlobalValidator {

    @Autowired
    protected IMaterialTipoRepository repository;

    public void validate(MaterialTipoRequest input, Integer id, Errors errors) {

    }
}
