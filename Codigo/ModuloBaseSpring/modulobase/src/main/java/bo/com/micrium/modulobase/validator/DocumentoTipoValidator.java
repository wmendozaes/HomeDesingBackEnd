package bo.com.micrium.modulobase.validator;

import bo.com.micrium.modulobase.repository.IDocumentoTipoRepository;
import bo.com.micrium.modulobase.resources.dto.DocumentoTipoRequest;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;

@Log4j2
@Component
public class DocumentoTipoValidator extends GlobalValidator {

    @Autowired
    protected IDocumentoTipoRepository repository;

    public void validate(DocumentoTipoRequest input, Integer id, Errors errors) {

    }
}
