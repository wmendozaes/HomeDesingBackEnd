package bo.com.micrium.modulobase.validator;

import bo.com.micrium.modulobase.repository.IDepartamentoRepository;
import bo.com.micrium.modulobase.resources.dto.DepartamentoRequest;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;

@Log4j2
@Component
public class DepartamentoValidator extends GlobalValidator {

    @Autowired
    protected IDepartamentoRepository repository;

    public void validate(DepartamentoRequest input, Integer id, Errors errors) {

    }
}
