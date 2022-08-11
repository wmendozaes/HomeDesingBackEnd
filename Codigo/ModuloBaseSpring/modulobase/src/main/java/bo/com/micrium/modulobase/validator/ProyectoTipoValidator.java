package bo.com.micrium.modulobase.validator;

import bo.com.micrium.modulobase.repository.IProyectoTipoRepository;
import bo.com.micrium.modulobase.resources.dto.ProyectoTipoRequest;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;

@Log4j2
@Component
public class ProyectoTipoValidator extends GlobalValidator {

    @Autowired
    protected IProyectoTipoRepository repository;

    public void validate(ProyectoTipoRequest input, Integer id, Errors errors) {

    }
}
