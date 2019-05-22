package com.restcsv.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.restcsv.entidade.Cidade;

public interface CidadeRepository extends JpaRepository<Cidade, Long>{

	  @Query("select c from Cidade c where c.capital = true ORDER BY c.name")
	  List<Cidade> findByCapital();
	  
	  @Query("select count(*) from Cidade")
	  Long quantidadeRegistro();
	  
	  @Query("select c from Cidade c INNER JOIN c.estado where c.estado.id = ?1 ORDER BY c.name")
	  List<Cidade> findCityById(Long estado_id);
	  
	  @Query("select c from Cidade c INNER JOIN c.estado where c.estado.uf like %?1% ORDER BY c.name")
	  List<Cidade> findCityByName(String uf);
}

