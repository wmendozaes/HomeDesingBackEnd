/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bo.com.micrium.modulobase.repository;

import bo.com.micrium.modulobase.model.Grupo;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface IGrupoRepository extends JpaRepository<Grupo, Integer> {

    Page<Grupo> findAllByEstadoTrue(Pageable pagination);
    
    Grupo findByNombreAndEstadoTrue(String nombre);

    List<Grupo> findAllByRolIdAndEstadoTrue(int rolId);

    long countByEstadoTrueAndRolIdId(int rolId);

}
