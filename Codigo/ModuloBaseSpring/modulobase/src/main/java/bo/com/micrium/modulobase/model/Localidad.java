package bo.com.micrium.modulobase.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "HD_LOCALIDAD")
public class Localidad implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @SequenceGenerator(name = "HD_LOCALIDAD_GENERATOR", sequenceName = "SEQ_HD_LOCALIDAD", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "HD_LOCALIDAD_GENERATOR")
    private Integer id;
    private String nombre;
    private String descripcion;
    @ManyToOne
    @JoinColumn(name = "PROVINCIA_ID")
    private Provincia provinciaId;
    private Integer estado;
}
