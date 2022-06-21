package bo.com.micrium.modulobase.model;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.Id;
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
@Entity(name = "MU_PARAMETRO")
public class Parametro implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private Integer id;
    private String nombre;
    private int tipo;
    private String valor;
    private String descripcion;
    private int tipoParametroId;

}
