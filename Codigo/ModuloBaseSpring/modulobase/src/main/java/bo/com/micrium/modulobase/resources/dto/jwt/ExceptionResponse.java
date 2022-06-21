/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bo.com.micrium.modulobase.resources.dto.jwt;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.Arrays;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.http.HttpStatus;

/**
 *
 * @author alepaco.maton
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@AllArgsConstructor
public class ExceptionResponse implements Serializable {

    private HttpStatus estatus;
    private String mensaje;
    private List<String> errores;

    public ExceptionResponse(HttpStatus estatus, String mensaje, String errores) {
        this.estatus = estatus;
        this.mensaje = mensaje;
        this.errores = Arrays.asList(errores);
    }
    
}
