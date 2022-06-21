package bo.com.micrium.modulobase.model;

import java.io.Serializable;
import javax.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 *
 * @author alepaco.maton
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "MU_TIPO_PARAMETRO")
public class TipoParametro implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private Integer id;
    private String nombre;
    private String descripcion;

}
