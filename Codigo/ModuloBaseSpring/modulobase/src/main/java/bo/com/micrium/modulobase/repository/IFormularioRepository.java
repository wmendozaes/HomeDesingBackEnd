/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bo.com.micrium.modulobase.repository;

import bo.com.micrium.modulobase.model.Formulario;
import java.util.List;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface IFormularioRepository extends JpaRepository<Formulario, Integer> {

    List<Formulario> findByModuloIdIsNull(Sort sort);
    
    List<Formulario> findByModuloId(int moduloId, Sort sort);

}
