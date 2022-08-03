package bo.com.micrium.modulobase.repository;

import bo.com.micrium.modulobase.model.Departamento;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;

@Repository
public interface IDepartamentoRepository extends JpaRepository<Departamento, Integer> {

    Page<Departamento> findAllByEstadoIn(Collection<Integer> estados, Pageable pageable);
}
