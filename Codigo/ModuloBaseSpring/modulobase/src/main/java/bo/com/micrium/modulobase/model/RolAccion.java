/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bo.com.micrium.modulobase.model;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
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
@Entity(name = "MU_ROL_ACCION")
public class RolAccion implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @SequenceGenerator(name = "ROL_ACCION_GENERATOR", sequenceName = "SEQ_MU_ROL_ACCION", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ROL_ACCION_GENERATOR")
    private Integer id;
    private Integer rolId;
    private Integer accionId;

}
