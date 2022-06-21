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
@Entity(name = "MU_ROL_TIPO_PARAMETRO_PERMISO")
public class RolTipoParametroPermiso implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @SequenceGenerator(name = "ROL_TIPO_PAR_GENERATOR", sequenceName = "SEQ_MU_ROL_TIPO_PAR", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ROL_TIPO_PAR_GENERATOR")
    private Integer id;
    private Integer rolId;
    private Integer tipoParametroId;
    private Integer tipoPermiso;

}
