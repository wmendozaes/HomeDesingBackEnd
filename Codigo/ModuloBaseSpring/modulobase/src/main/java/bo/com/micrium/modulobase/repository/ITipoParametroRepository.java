/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bo.com.micrium.modulobase.repository;

import bo.com.micrium.modulobase.model.TipoParametro;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 *
 * @author alepaco.maton
 */
@Repository
public interface ITipoParametroRepository extends JpaRepository<TipoParametro, Integer> {

    TipoParametro findByNombre(String nombre);

}
