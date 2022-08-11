package bo.com.micrium.modulobase.resources.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ContratoTipoRequest implements Serializable {

    private static final Long serialVersionUID = 1L;

    private String nombre;
    private String descripcion;
}
