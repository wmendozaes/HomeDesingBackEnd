/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bo.com.micrium.modulobase.resources.dto.jwt;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
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
public class ModuloResponse implements Serializable, Comparable<ModuloResponse> {

    private int id;

    private String nombre;

    private int orden;

    private int tipo;

    private String url;

    private String icono;

    private List<FormularioResponse> formularios = new ArrayList<>();

    public void sortFormularios() {
        Collections.sort(formularios);
    }
    
    @Override
    public int compareTo(ModuloResponse o) {
        return Integer.compare(this.getOrden(), o.getOrden());
    }

}
