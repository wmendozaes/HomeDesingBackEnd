/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bo.com.micrium.modulobase.repository;

import bo.com.micrium.modulobase.model.Rol;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 *
 * @author alepaco.maton
 */
@Repository
public interface IRolRepository extends JpaRepository<Rol, Integer> {

    Rol findByNombreAndEstadoTrue(String nombre);

    Page<Rol> findAllByIdNotAndEstadoTrue(int id, Pageable pageable);
    
}
