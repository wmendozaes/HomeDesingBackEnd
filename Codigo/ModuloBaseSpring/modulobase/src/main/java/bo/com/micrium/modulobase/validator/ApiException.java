/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bo.com.micrium.modulobase.validator;

import java.io.Serializable;
import lombok.Data;
import org.springframework.validation.BindingResult;

/**
 *
 * @author alepaco.maton
 */
@Data
public class ApiException extends Exception implements Serializable {

    private BindingResult errors;

    public ApiException(BindingResult errors, String message) {
        super(message);
        this.errors = errors;
    }
    
}
