package com.restcsv.service;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

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

	public void salvaArquivo(MultipartFile file) {
        Set<String> estado = new HashSet<>();
        List<String> cidade = new ArrayList<String>(); 
		
		try {
			InputStream input = file.getInputStream();
	        InputStreamReader isr = new InputStreamReader(input);
	        BufferedReader br = new BufferedReader(isr);
	        
			input.read();
	        String s = br.readLine();
	        
	        while ((s = br.readLine())!= null) {
	        	cidade.add(s.toString());
	            String[] dados = s.split(",");
	            estado.add(dados[1]);
	        }

	        br.close();
		} catch (Exception e) {
			 e.getStackTrace();
		}
		
		for (String es : estado) {	
			Estado estadoObj = new Estado();
			estadoObj.setUf(es);
			estadoRepo.save(estadoObj);
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
	
	public List<Cidade> cidadesPorId(Long id){
		return cidadeRepo.findCityById(id);
	}
	public List<Cidade> cidadesPorNome(String uf){
		return cidadeRepo.findCityByName(uf);
	}
	
	public List<HashMap<String, String>> retornQuantidadeCidadePorEstado(){
		List<Estado> estado = estadoRepo.findAll();
		HashMap<String, String> map;
		List<HashMap<String, String>> quantidadeCidade = new ArrayList<>();
		for (Estado es : estado) {
			map = new HashMap<String, String>();
			Long valor = cidadeRepo.quantidadeRegistroPorEstado(es.getId());
			map.put("UF", es.getUf());
			map.put("Quantidade de Cidades", valor.toString());
			quantidadeCidade.add(map);
		}
		return quantidadeCidade;
	}
	
	public List<HashMap<String, String>> maiorMenorCidadeEstado(){
		List<Estado> estado = estadoRepo.findAll();
		HashMap<String, String> mapMaior = null;
		HashMap<String, String> mapMenor = null;
		List<HashMap<String, String>> quantidadeCidade = new ArrayList<>();
		Long menor,maior,temp;
		menor = maior = temp= 0L;
		for (Estado es : estado) {
			
			temp = cidadeRepo.quantidadeRegistroPorEstado(es.getId());
			if( menor == 0 && maior == 0) {
				menor = temp;
				maior = temp;
			}
			
			if(temp >= maior) {
				mapMaior = new HashMap<String, String>();
				Long valor = cidadeRepo.quantidadeRegistroPorEstado(es.getId());
				mapMaior.put("UF", es.getUf());
				mapMaior.put("Quantidade de Cidades", valor.toString());
				maior = temp;
			}
			if(maior <= menor) {
				mapMenor = new HashMap<String, String>();
				Long valor = cidadeRepo.quantidadeRegistroPorEstado(es.getId());
				mapMenor.put("UF", es.getUf());
				mapMenor.put("Quantidade de Cidades", valor.toString());
				menor = maior;
			}
		}
		quantidadeCidade.add(mapMaior);
		quantidadeCidade.add(mapMenor);
		
		return quantidadeCidade;
	}
}
