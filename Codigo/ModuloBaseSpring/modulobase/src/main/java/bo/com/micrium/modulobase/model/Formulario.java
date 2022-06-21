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
@Entity(name = "MU_FORMULARIO")
public class Formulario implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private Integer id;
    private String nombre;
    private Integer orden;
    private Integer moduloId;
    private String url;
    private String icono;

}
