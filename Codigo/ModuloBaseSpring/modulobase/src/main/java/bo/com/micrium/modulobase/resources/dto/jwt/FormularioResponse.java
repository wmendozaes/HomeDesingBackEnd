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
public class FormularioResponse implements Serializable, Comparable<FormularioResponse> {

    private int id;

    private String nombre;

    private int orden;

    private int tipo;

    private String url;

    private String icono;

    private List<AccionResponse> acciones;

    @Override
    public int compareTo(FormularioResponse o) {
        return Integer.compare(this.getOrden(), o.getOrden());
    }

}
