package com.restcsv.resource;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
    public ResponseEntity<String> fileUpload(@RequestParam("file") MultipartFile file) {
    	
    	try {
			InputStream input = file.getInputStream();
            InputStreamReader isr = new InputStreamReader(input);
            BufferedReader br = new BufferedReader(isr);	
			input.read();
            String s = br.readLine();
            Set<String> estado = new HashSet<>();
            List<String> cidade = new ArrayList<String>(); 

            while ((s = br.readLine())!= null) {
            	cidade.add(s.toString());
                String[] dados = s.split(",");
                estado.add(dados[1]);
            }

            br.close();
           this.salvaArq.salvaArquivo(estado, cidade); 
    	
    	} catch (IOException e) {
			e.printStackTrace();
		}
    return ResponseEntity.status(HttpStatus.OK).build();
    
    }
    
    @GetMapping(value="/cidade/listar/{id}")
    public ResponseEntity<Cidade> retornaIBGE(@PathVariable("id") Long ibge) {
    	Cidade cidade = salvaArq.buscarIBGE(ibge);
		return ResponseEntity.ok(cidade);
    }
    
    @GetMapping(value="/cidade/listarPorNome/{id}")
    public List<Cidade> listarPorNome(@PathVariable("id") Long id) {
    	List<Cidade> cidades = salvaArq.cidadesPorNome(id);
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
