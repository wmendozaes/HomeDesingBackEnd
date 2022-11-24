/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bo.com.micrium.modulobase.repository;

import bo.com.micrium.modulobase.model.RolAccion;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface IRolAccionRepository extends JpaRepository<RolAccion, Integer> {
    
//    @Query(value = "select * from MU_ROL_ACCION u where u.ROL_ID = ? ",nativeQuery = true)
    List<RolAccion> findAllByRolId(int rolId);
    
}
