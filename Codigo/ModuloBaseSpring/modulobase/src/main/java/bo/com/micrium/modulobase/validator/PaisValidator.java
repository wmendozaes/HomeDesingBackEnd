package bo.com.micrium.modulobase.validator;

import bo.com.micrium.modulobase.repository.IPaisRepository;
import bo.com.micrium.modulobase.resources.dto.DepartamentoRequest;
import bo.com.micrium.modulobase.resources.dto.PaisRequest;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;

@Log4j2
@Component
public class PaisValidator extends GlobalValidator {

    @Autowired
    protected IPaisRepository repository;

    public void validate(PaisRequest input, Integer id, Errors errors) {

    }
}
