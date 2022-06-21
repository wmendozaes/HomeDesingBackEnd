package bo.com.micrium.modulobase.model;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author alepaco.maton
 *
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "MU_ROL")
public class Rol implements Serializable {

    private static final long serialVersionUID = 1L;
    
    public static final int SUPER_ADMINISTRADOR = 1;

    @Id
    @SequenceGenerator(name = "ROL_GENERATOR", sequenceName = "SEQ_MU_ROL", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ROL_GENERATOR")
    private Integer id;
    private String nombre;
    private String descripcion;
    private boolean estado;

}
