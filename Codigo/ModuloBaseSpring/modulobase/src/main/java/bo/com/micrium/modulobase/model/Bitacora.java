package bo.com.micrium.modulobase.model;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
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
@Entity(name = "MU_BITACORA")
public class Bitacora implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @SequenceGenerator(name = "BITACORA_GENERATOR", sequenceName = "SEQ_MU_BITACORA", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "BITACORA_GENERATOR")
    private Long id;
    private String accion;
    private String direccionIp;
    @Temporal(TemporalType.TIMESTAMP)
    private Date fecha;
    private String formulario;
    private String usuario;

}
