package bo.com.micrium.modulobase.validator;

import bo.com.micrium.modulobase.repository.IVehiculoTipoRepository;
import bo.com.micrium.modulobase.resources.dto.VehiculoTipoRequest;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;

@Log4j2
@Component
public class VehiculoTipoValidator extends GlobalValidator {

    @Autowired
    protected IVehiculoTipoRepository repository;

    public void validate(VehiculoTipoRequest input, Integer id, Errors errors) {

    }
}