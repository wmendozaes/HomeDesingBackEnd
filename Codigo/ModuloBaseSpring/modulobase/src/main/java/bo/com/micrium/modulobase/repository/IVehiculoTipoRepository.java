package bo.com.micrium.modulobase.repository;

import bo.com.micrium.modulobase.model.VehiculoTipo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;

@Repository
public interface IVehiculoTipoRepository extends JpaRepository<VehiculoTipo, Integer> {

    Page<VehiculoTipo> findAllByEstadoIn(Collection<Integer> estados, Pageable pageable);
}
