package com.restcsv.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.restcsv.entidade.Cidade;
import com.restcsv.entidade.Estado;
import com.restcsv.repository.CidadeRepository;
import com.restcsv.repository.EstadoRepository;

@Service
public class SalvaArquivoService {

	@Autowired
	private CidadeRepository cidadeRepo;
	@Autowired
	private EstadoRepository estadoRepo;

	public void salvaArquivo(Set<String> estado, List<String> cidade) {
		
		for (String es : estado) {	
			Estado estadoObj = new Estado();
			estadoObj.setUf(es);

			for (int i=0;i< cidade.size(); i++) {
				String[] dados = cidade.get(i).split(",");
				if(dados[1].equals(es)){
					
					Cidade cidadeObj = new Cidade();
					cidadeObj.setIbge_id(Long.parseLong(dados[0]));
					cidadeObj.setName(dados[2]);
					cidadeObj.setCapital(Boolean.parseBoolean(dados[3]));
					cidadeObj.setLon(dados[4]);
					cidadeObj.setLat(dados[5]);
					cidadeObj.setNo_accents(dados[6]);
					cidadeObj.setAlternative_names(dados[7]);
					cidadeObj.setMicroregion(dados[8]);
					cidadeObj.setMesoregion(dados[9]);
					cidadeObj.setEstado(estadoObj);
					
					estadoObj.getCidades().addAll(Arrays.asList(cidadeObj));
					cidadeRepo.saveAll(Arrays.asList(cidadeObj));
				
				}
				estadoRepo.saveAll(Arrays.asList(estadoObj));
			}
		}
	}
	
	public Cidade buscarIBGE(Long ibge) {
		Optional<Cidade> cidade = cidadeRepo.findById(ibge);
		return cidade.orElse(null);
	}
	
	public List<Cidade> retornaCapital() {
		return cidadeRepo.findByCapital();
	}
	
	public Long quantidade() {
		return cidadeRepo.quantidadeRegistro();
	}
	
	public void adicionaCidade(Cidade cidade) {
		cidadeRepo.save(cidade);
	}
	
	public void removerCidade(Long ibge) {
		cidadeRepo.deleteById(ibge);	
	}
	
	public List<Cidade> cidadesPorNome(Long id){
		return cidadeRepo.findCityByName(id);
	}
}