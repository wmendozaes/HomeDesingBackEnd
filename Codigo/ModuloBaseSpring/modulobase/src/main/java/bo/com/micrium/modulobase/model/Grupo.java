package bo.com.micrium.modulobase.model;

import java.io.Serializable;
import javax.persistence.Column;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author alepaco.maton
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "MU_GRUPO_AD")
public class Grupo implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @SequenceGenerator(name = "MU_GRUPO_AD_GRUPOID_GENERATOR", sequenceName = "SEQ_MU_GRUPO_AD", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "MU_GRUPO_AD_GRUPOID_GENERATOR")
    private Integer id;
    private String nombre;
    private String descripcion;
    @ManyToOne
    @JoinColumn(name = "ROL_ID")
    private Rol rolId;
    private boolean estado;

}
