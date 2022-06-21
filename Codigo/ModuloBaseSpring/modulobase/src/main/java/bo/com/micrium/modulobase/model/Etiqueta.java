/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
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
 *
 * @author alepaco.maton
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "MU_ETIQUETA")
public class Etiqueta implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @Id
    @SequenceGenerator(name = "MU_ETIQUETA_AD_ETIQUETAID_GENERATOR", sequenceName = "SEQ_MU_ETIQUETAS", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "MU_ETIQUETA_AD_ETIQUETAID_GENERATOR")
    private Integer id;
    private String llave;
    private String valor;
    private String grupo;
    private boolean estado;
    
}
