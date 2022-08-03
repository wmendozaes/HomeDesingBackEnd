/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bo.com.micrium.modulobase.resources.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UsuarioResponse implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer id;
    private String nombreUsuario;
    private String nombreCompleto;
    private int tipo;
    private int rolId;
    private String rolNombre;
    private int estado;
    
}
