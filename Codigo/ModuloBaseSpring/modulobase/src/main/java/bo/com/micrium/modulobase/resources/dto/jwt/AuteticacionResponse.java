/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bo.com.micrium.modulobase.resources.dto.jwt;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 *
 * @author alepaco.maton
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@AllArgsConstructor
public class AuteticacionResponse implements Serializable {
    
    private final String token;
    
    private final int rolId;
    
    private final String rol;
    
    private final List<ModuloResponse> modulos;
    
}
