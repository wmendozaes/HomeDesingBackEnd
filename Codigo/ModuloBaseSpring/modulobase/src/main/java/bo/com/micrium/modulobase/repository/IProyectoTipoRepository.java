package bo.com.micrium.modulobase.repository;

import bo.com.micrium.modulobase.model.ProyectoTipo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;

@Repository
public interface IProyectoTipoRepository extends JpaRepository<ProyectoTipo, Integer> {

    Page<ProyectoTipo> findAllByEstadoIn(Collection<Integer> estados, Pageable pageable);
}
