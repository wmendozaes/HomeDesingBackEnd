/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bo.com.micrium.modulobase.repository;

import bo.com.micrium.modulobase.model.Usuario;
import java.util.Collection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IUsuarioRepository extends JpaRepository<Usuario, Integer> {

    Usuario findByNombreUsuarioAndEstadoIn(String nombreUsuario, Collection<Integer> estados);

    Page<Usuario> findAllByTipoNotAndEstadoIn(int tipo, Collection<Integer> estados, Pageable pageable);

    long countByEstadoInAndRolIdId(Collection<Integer> estados, int rolId);
    
}
