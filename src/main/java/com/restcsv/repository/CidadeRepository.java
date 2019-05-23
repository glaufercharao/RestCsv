package com.restcsv.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.restcsv.entidade.Cidade;

public interface CidadeRepository extends JpaRepository<Cidade, Long>{

	  @Query("SELECT c from Cidade c WHERE c.capital = true ORDER BY c.name")
	  List<Cidade> findByCapital();
	  
	  @Query("SELECT count(*) from Cidade")
	  Long quantidadeRegistro();
	  
	  @Query("SELECT c from Cidade c INNER JOIN c.estado WHERE c.estado.id = ?1 ORDER BY c.name")
	  List<Cidade> findCityById(Long estado_id);
	  
	  @Query("SELECT c from Cidade c INNER JOIN c.estado WHERE c.estado.uf LIKE %?1% ORDER BY c.name")
	  List<Cidade> findCityByName(String uf);
	  
	  @Query("SELECT count(*) from Cidade c WHERE c.estado.id = ?1")
	  Long quantidadeRegistroPorEstado(Long estado_id);
}

