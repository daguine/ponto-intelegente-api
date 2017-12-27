package com.pvaldez.pontointelegente.api.controllers;

import java.security.NoSuchAlgorithmException;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.pvaldez.pontointelegente.api.dtos.RegistoPJDto;
import com.pvaldez.pontointelegente.api.entities.Empresa;
import com.pvaldez.pontointelegente.api.entities.Funcionario;
import com.pvaldez.pontointelegente.api.enums.PerfilEnum;
import com.pvaldez.pontointelegente.api.response.Response;
import com.pvaldez.pontointelegente.api.service.EmpresaService;
import com.pvaldez.pontointelegente.api.service.FuncionarioService;
import com.pvaldez.pontointelegente.api.utils.PasswordUtil;

@RestController
@RequestMapping("/api/registo-pj")
@CrossOrigin(origins = "*")
public class RegistoPJController {

	private static final Logger log = LoggerFactory.getLogger(RegistoPJController.class);
	
	@Autowired
	private FuncionarioService funcionarioService;
	
	@Autowired
	private EmpresaService empresaService;
	
	public RegistoPJController() {
		// TODO Auto-generated constructor stub
	}
	
	/**
	 * 
	 * @param registoPJDto
	 * @param result
	 * @return ResponseEntity<Response<RegistoPJDto>>
	 * @throws NoSuchAlgorithmException
	 */
	@PostMapping
	public ResponseEntity<Response<RegistoPJDto>> registo(@Valid @RequestBody RegistoPJDto registoPJDto,
			BindingResult result) throws NoSuchAlgorithmException{
		log.info("Registando PJ: {}", registoPJDto.toString());
		Response<RegistoPJDto> response = new Response<>();
		
		validarDadosExistentes(registoPJDto,result);
		Empresa empresa = this.converterDtoParaEmpresa(registoPJDto,result);
		Funcionario funcionario = this.converterDtoParaFuncionario(registoPJDto,result);
		
		if(result.hasErrors()) {
			log.error("Erro validando dados de registo PJ: {}",result.getAllErrors());
			result.getAllErrors().forEach(error->response.getErros().add(error.getDefaultMessage()));
			return ResponseEntity.badRequest().body(response);
		}
		
		this.empresaService.persistir(empresa);
		funcionario.setEmpresa(empresa);
		this.funcionarioService.persistir(funcionario);
		
		response.setData(this.converterRegistoPJDto(funcionario));
		return ResponseEntity.ok(response);
	}

	/**
	 * Verifica se a empresa ou funcionario ja existem na base de dados.
	 * @param registoPJDto
	 * @param result
	 */
	private void validarDadosExistentes(RegistoPJDto registoPJDto, BindingResult result) {
		this.empresaService.buscarPorCnpj(registoPJDto.getCnpj())
				.ifPresent(emp -> result.addError(new ObjectError("empresa", "Empresa Ja existente")));
		
		this.funcionarioService.buscarPorCpf(registoPJDto.getCpf())
				.ifPresent(func -> result.addError(new ObjectError("funcionario", "CPF ja existente")));
		
		this.funcionarioService.buscarPorEmail(registoPJDto.getEmail())
				.ifPresent(func -> result.addError(new ObjectError("funcionario","Email ja existente")));
		
	}
	
	/**
	 * Converter os dados do DTO para empresa
	 * 
	 * @param registoPJDto
	 * @param result
	 * @return empresa
	 */
	private Empresa converterDtoParaEmpresa(RegistoPJDto registoPJDto, BindingResult result) {
		Empresa empresa = new Empresa();
		empresa.setCnpj(registoPJDto.getCnpj());
		empresa.setRazaoSocial(registoPJDto.getRazaoSocial());
		
		return empresa;
	}
	
	/**
	 * Converter os dados do DTO para funcionario
	 * 
	 * @param registoPJDto
	 * @param result
	 * @return funcionario
	 * @throws NoSuchAlgorithmException
	 */
	private Funcionario converterDtoParaFuncionario(RegistoPJDto registoPJDto, BindingResult result)
	throws NoSuchAlgorithmException{
		Funcionario funcionario = new Funcionario();
		funcionario.setName(registoPJDto.getNome());
		funcionario.setEmail(registoPJDto.getEmail());
		funcionario.setCpf(registoPJDto.getCnpj());
		funcionario.setPerfil(PerfilEnum.ROLE_ADMIN);
		funcionario.setSenha(PasswordUtil.gerarBCript(registoPJDto.getSenha()));
		
		return funcionario;
	}

	/**
	 *  Popular o DTO de registo co os dados do funcionario e empresa
	 *  
	 * @param funcionario
	 * @return RegistoPJDto
	 */
	private RegistoPJDto converterRegistoPJDto(Funcionario funcionario) {
		RegistoPJDto dto = new RegistoPJDto();
		dto.setId(funcionario.getId());
		dto.setNome(funcionario.getName());
		dto.setEmail(funcionario.getEmail());
		dto.setCpf(funcionario.getCpf());
		dto.setRazaoSocial(funcionario.getEmpresa().getRazaoSocial());
		dto.setCnpj(funcionario.getEmpresa().getCnpj());
		
		return dto;
	}

	
}
