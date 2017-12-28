package com.pvaldez.pontointelegente.api.controllers;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.pvaldez.pontointelegente.api.dtos.EmpresaDto;
import com.pvaldez.pontointelegente.api.entities.Empresa;
import com.pvaldez.pontointelegente.api.response.Response;
import com.pvaldez.pontointelegente.api.service.EmpresaService;

@RestController
@RequestMapping("/api/empresas")
@CrossOrigin(origins = "*")
public class EmpresaController {
	private Logger log = LoggerFactory.getLogger(EmpresaController.class);
	
	@Autowired
	private EmpresaService  empresaService;
	
	public EmpresaController() {
		// TODO Auto-generated constructor stub
	}
	
	/**
	 *  retorna uma empresa dada um CNPJ
	 * @param cnpj
	 * @return ResponseEntity<Response<EmpresaDto>>
	 */
	@GetMapping(value = "/cnpj/{cnpj}")
	public ResponseEntity<Response<EmpresaDto>> buscarPorCnpj(@PathVariable("cnpj") String cnpj){
		log.info("Buscando empresa por CNPJ: {}", cnpj);
		Response<EmpresaDto> response = new Response<>();
		Optional<Empresa> empresa = empresaService.buscarPorCnpj(cnpj);
		
		if(!empresa.isPresent()) {
			log.info("empresa nao encontroda para o CNPJ: {}", cnpj);
			response.getErros().add("empresa nao encontrada para o CNPJ" + cnpj);
			return ResponseEntity.badRequest().body(response);
		}
		
		response.setData(this.converterEmpresaDto(empresa.get()));
		return ResponseEntity.ok(response);
	}

	/**
	 * Popula um DTO com os dados de uma empresa
	 * 
	 * @param empresa
	 * @return EmpresaDto
	 */
	private EmpresaDto converterEmpresaDto(Empresa empresa) {
		EmpresaDto dto = new EmpresaDto();
		dto.setId(empresa.getId());
		dto.setCnpj(empresa.getCnpj());
		dto.setRazaoSocial(empresa.getRazaoSocial());
		
		return dto;
	}
}
