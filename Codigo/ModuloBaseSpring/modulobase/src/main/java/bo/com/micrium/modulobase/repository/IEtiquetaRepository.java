/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bo.com.micrium.modulobase.repository;

import java.util.Collection;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import bo.com.micrium.modulobase.model.Etiqueta;

/**
 *
 * @author alepaco.maton
 */
@Repository
public interface IEtiquetaRepository extends JpaRepository<Etiqueta, Integer> {

    Page<Etiqueta> findAllByEstadoTrue(Pageable pagination);

    List<Etiqueta> findAllByLlaveIn(Collection<String> llaves);

    List<Etiqueta> findAllByGrupoIn(Collection<String> grupos);

}
