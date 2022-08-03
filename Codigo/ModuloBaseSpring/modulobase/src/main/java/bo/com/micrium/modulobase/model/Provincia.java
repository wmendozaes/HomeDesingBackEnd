package bo.com.micrium.modulobase.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "HD_PROVINCIA")
public class Provincia implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @SequenceGenerator(name = "HD_PROVINCIA_GENERATOR", sequenceName = "SEQ_HD_PROVINCIA", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "HD_PROVINCIA_GENERATOR")
    private Integer id;
    private String nombre;
    private String descripcion;
    @ManyToOne
    @JoinColumn(name = "DEPARTAMENTO_ID")
    private Departamento departamentoID;
    private Integer estado;
}
