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

/**
 *
 * @author alepaco.maton
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ParametroResponse implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer id;
    private String nombre;
    private int tipo;
    private String valor;
    private String descripcion;
    private int tipoParametroId;
    private String tipoParametroNombre;
    private boolean editable;

}
