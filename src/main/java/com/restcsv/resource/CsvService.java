package com.restcsv.resource;

import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.restcsv.entidade.Cidade;
import com.restcsv.service.SalvaArquivoService;

@RestController
@RequestMapping("/api")
public class CsvService {
	
	@Autowired
	private SalvaArquivoService salvaArq;
	
    @PostMapping("/upload")
    public ResponseEntity<String> fileUpload(@RequestParam MultipartFile file) {
       this.salvaArq.salvaArquivo(file);  	
       return ResponseEntity.status(HttpStatus.OK).build();
    }
    
    @GetMapping(value="/cidade/listar/{id}")
    public ResponseEntity<Cidade> retornaIBGE(@PathVariable("id") Long ibge) {
    	Cidade cidade = salvaArq.buscarIBGE(ibge);
		return ResponseEntity.ok(cidade);
    }
    
    @GetMapping(value="/cidade/listarPorUF/{uf}")
    public List<Cidade> listarPorNome(@PathVariable("uf") String uf) {
    	List<Cidade> cidades = salvaArq.cidadesPorNome(uf.toUpperCase());
		return cidades;
    }
    
    @GetMapping(value="/cidade/listarPorId/{id}")
    public List<Cidade> listarPorId(@PathVariable("id") Long id) {
    	List<Cidade> cidades = salvaArq.cidadesPorId(id);
		return cidades;
    }
    
    @GetMapping(value="/cidade/listaCapital/")
    public List<Cidade> retornaCapital() {
    	List<Cidade> cidades = salvaArq.retornaCapital();
		return cidades;
    }
    
    @GetMapping(value="/cidade/quantidade/")
    public Long retornaQuantidade() {
    	return salvaArq.quantidade();
    }
    
    @GetMapping(value="/cidade/quantidadeCidadePorEstado/")
    @ResponseBody
    public List<HashMap<String, String>> retornaQuantidadeCidade() {
       return salvaArq.retornQuantidadeCidadePorEstado();
    }
    
    @GetMapping(value="/cidade/maiorMenorCidade/")
    @ResponseBody
    public List<HashMap<String, String>> retornaMaiorMenorCidade() {
       return salvaArq.maiorMenorCidadeEstado();
    }
    
    @PostMapping(value = "/cidade/add")
    public ResponseEntity<Cidade> adicionarCidade(@RequestBody Cidade cidade) {
    		salvaArq.adicionaCidade(cidade);
         return ResponseEntity.status(HttpStatus.CREATED).build();
    }
    
    @DeleteMapping("/cidade/remover/{id}")
    public ResponseEntity<?> deletarCidade(@PathVariable("id") Long ibge){
    	salvaArq.removerCidade(ibge);
    	return ResponseEntity.ok().build();
    	
    } 
}
