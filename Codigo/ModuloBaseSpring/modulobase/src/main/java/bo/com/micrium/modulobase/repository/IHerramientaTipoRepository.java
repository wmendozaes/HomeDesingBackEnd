package bo.com.micrium.modulobase.repository;

import bo.com.micrium.modulobase.model.HerramientaTipo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;

@Repository
public interface IHerramientaTipoRepository extends JpaRepository<HerramientaTipo, Integer> {

    Page<HerramientaTipo> findAllByEstadoIn(Collection<Integer> estados, Pageable pageable);
}
