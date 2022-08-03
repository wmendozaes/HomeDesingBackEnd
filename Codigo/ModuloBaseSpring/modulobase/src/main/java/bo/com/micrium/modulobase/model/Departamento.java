package bo.com.micrium.modulobase.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "HD_DEPARTAMENTO")
public class Departamento implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @SequenceGenerator(name = "HD_DEPARTAMENTO_GENERATOR", sequenceName = "SEQ_HD_DEPARTAMENTO", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "HD_DEPARTAMENTO_GENERATOR")
    private Integer id;
    private String nombre;
    private String descripcion;
    @ManyToOne
    @JoinColumn(name = "PAIS_ID")
    private Pais paisID;
    private Integer estado;
}
