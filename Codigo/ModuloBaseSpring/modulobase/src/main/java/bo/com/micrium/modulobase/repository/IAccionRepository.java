/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bo.com.micrium.modulobase.repository;

import bo.com.micrium.modulobase.model.Accion;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface IAccionRepository extends JpaRepository<Accion, Integer> {

    List<Accion> findByFormularioId(int formularioId);

}
