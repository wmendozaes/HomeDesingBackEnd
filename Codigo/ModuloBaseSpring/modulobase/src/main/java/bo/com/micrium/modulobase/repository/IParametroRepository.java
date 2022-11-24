/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bo.com.micrium.modulobase.repository;

import bo.com.micrium.modulobase.model.Parametro;
import java.util.Collection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface IParametroRepository extends JpaRepository<Parametro, Integer> {
 
    Page<Parametro> findAllByTipoParametroIdIn(Collection<Integer> tipoParametroIds, Pageable pageable);
    
    Parametro findByNombre(String nombre);
    
}
