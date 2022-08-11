package bo.com.micrium.modulobase.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "HD_HERRAMIENTA_TIPO")
public class HerramientaTipo implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @SequenceGenerator(name = "HD_HERRAMIENTA_TIPO_GENERATOR", sequenceName = "SEQ_HD_HERRAMIENTA_TIPO", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "HD_HERRAMIENTA_TIPO_GENERATOR")
    private Integer id;
    private String nombre;
    private String descripcion;
    private Integer estado;
}
