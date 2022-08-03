/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bo.com.micrium.modulobase.model;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
//import org.springframework.data.annotation.LastModifiedDate;
//import org.springframework.data.annotation.Version;


@ToString(exclude = "contrasena")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "mu_usuario")
public class Usuario implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @SequenceGenerator(name = "USUARIO_GENERATOR", sequenceName = "SEQ_MU_USUARIO", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "USUARIO_GENERATOR")
    private Integer id;
    private String nombreUsuario;
    private String nombreCompleto;
    private String contrasena;
    private int tipo;
    private int numeroIntentos;
    @Temporal(TemporalType.TIMESTAMP)
    private Date ultimoIntento;
    @ManyToOne
    @JoinColumn(name = "ROL_ID")
    private Rol rolId;
    private int estado;
    
    private Boolean validarFechaLimite;
    private Date fechaLimite;
    private Date fechaActualizacion;

//    public @Version Long version;
//    public @LastModifiedDate Date date;
    
}
