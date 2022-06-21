/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bo.com.micrium.modulobase.repository;

import bo.com.micrium.modulobase.model.Bitacora;
import java.util.Date;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

/**
 *
 * @author alepaco.maton
 */
@Repository
public interface IBitacoraRespository extends JpaRepository<Bitacora, Long> {

    @Query(value = "select * "
            + "from MU_BITACORA u "
            + "where (((-1 = ? or u.fecha >= ?) and (-1 = ? or u.fecha <= ?)) and (-1 = ? or (to_char(u.fecha, 'DD/MM/YYYY HH24:MI:SS') LIKE ?))) "
            + "and (-1 = ? or UPPER(accion) like ?) "
            + "and (-1 = ? or UPPER(direccion_ip) like ?) "
            + "and (-1 = ? or UPPER(formulario) like ?) "
            + "and (-1 = ? or UPPER(usuario) like ?)", nativeQuery = true)
    Page<Bitacora> filter(int fechaIniBool, Date fechaIni, int fechaFinBool, Date fechaFin, int fechaBool, String fecha,
            int accionBool, String accion, int direccionIpBool, String direccionIp, int formularioBool, String formulario,
            int usuarioBool, String usuario, Pageable pageable);

    Page<Bitacora> findByIdAndAccion(int id, String accion, Pageable pageable);

}
