package com.pvaldez.pontointelegente.api.controllers;

import java.math.BigDecimal;
import java.security.NoSuchAlgorithmException;
import java.util.Optional;

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

import com.pvaldez.pontointelegente.api.dtos.RegistoPFDto;
import com.pvaldez.pontointelegente.api.entities.Empresa;
import com.pvaldez.pontointelegente.api.entities.Funcionario;
import com.pvaldez.pontointelegente.api.enums.PerfilEnum;
import com.pvaldez.pontointelegente.api.response.Response;
import com.pvaldez.pontointelegente.api.service.EmpresaService;
import com.pvaldez.pontointelegente.api.service.FuncionarioService;
import com.pvaldez.pontointelegente.api.utils.PasswordUtil;


@RestController
@RequestMapping("/api/registo-pf")
@CrossOrigin(origins = "*")
public class RegistoPFController {

private static final Logger log = LoggerFactory.getLogger(RegistoPJController.class);
	
	@Autowired
	private FuncionarioService funcionarioService;
	
	@Autowired
	private EmpresaService empresaService;
	
	public RegistoPFController() {
		// TODO Auto-generated constructor stub
	}
	
	@PostMapping
	public ResponseEntity<Response<RegistoPFDto>> registar(@Valid @RequestBody RegistoPFDto registoPFDto, 
			BindingResult result) throws NoSuchAlgorithmException{
		log.info("Registar PF: {}", registoPFDto.toString());
		Response<RegistoPFDto> response = new Response<>();
		
		validarDadosExistentes(registoPFDto,result);
		Funcionario funcionario = this.converterDtoParaFuncionario(registoPFDto,result);
		
		if(result.hasErrors()) {
			log.error("Erro validando dados de registo PF: {}", result.getAllErrors());
			result.getAllErrors().forEach(error -> response.getErros().add(error.getDefaultMessage()));
			return ResponseEntity.badRequest().body(response);
		}
		
		Optional<Empresa> empresa = this.empresaService.buscarPorCnpj(registoPFDto.getCnpj());
		empresa.ifPresent(emp -> funcionario.setEmpresa(emp));
		this.funcionarioService.persistir(funcionario);
		
		response.setData(this.converterRegistoPFDto(funcionario));
		return ResponseEntity.ok(response);
	}



	private Funcionario converterDtoParaFuncionario(RegistoPFDto registoPFDto, BindingResult result) 
	throws NoSuchAlgorithmException{
		Funcionario funcionario = new Funcionario();
		funcionario.setName(registoPFDto.getNome());
		funcionario.setEmail(registoPFDto.getEmail());
		funcionario.setCpf(registoPFDto.getCpf());
		funcionario.setPerfil(PerfilEnum.ROLE_USER);
		funcionario.setSenha(PasswordUtil.gerarBCript(registoPFDto.getSenha()));
		registoPFDto.getQtdHorasAlmoco()
			.ifPresent(qtHorasAlmoco -> funcionario.setQtdHorasAlmoco(Float.valueOf(qtHorasAlmoco)));
		registoPFDto.getQtdHorasTrabalhoDia()
			.ifPresent(qtdHorasTrabDia -> funcionario.setQtdHorasTrabalhoDia(Float.valueOf(qtdHorasTrabDia)));
		registoPFDto.getValorHora().ifPresent(valorHora->funcionario.setValorHora(new BigDecimal(valorHora)));
		
		return funcionario;
	}

	private void validarDadosExistentes(RegistoPFDto registoPFDto, BindingResult result) {
		Optional<Empresa> empresa = this.empresaService.buscarPorCnpj(registoPFDto.getCnpj());
		if(!empresa.isPresent()) {
			result.addError(new ObjectError("empresa", "Empresa nao registada"));
		}
		
		this.funcionarioService.buscarPorCpf(registoPFDto.getCpf())
				.ifPresent(func -> result.addError(new ObjectError("funcionario", "funcionario ja existente")));
		
		this.funcionarioService.buscarPorCpf(registoPFDto.getEmail())
		.ifPresent(func -> result.addError(new ObjectError("funcionario", "funcionario ja existente")));
		
	}
	
	private RegistoPFDto converterRegistoPFDto(Funcionario funcionario) {
		RegistoPFDto dto = new RegistoPFDto();
		dto.setId(funcionario.getId());
		dto.setNome(funcionario.getName());
		dto.setEmail(funcionario.getEmail());
		dto.setCpf(funcionario.getCpf());
		dto.setCnpj(funcionario.getEmpresa().getCnpj());
		funcionario.getQtdHorasAlmocoOpt().ifPresent(
				qtdHorasAlmoco ->dto.setQtdHorasAlmoco(Optional.of(Float.toString(qtdHorasAlmoco))));
		funcionario.getQtdHorasTrabalhoDiaOpt().ifPresent(
				qtdHorasTabDia ->dto.setQtdHorasTrabalhoDia(Optional.of(Float.toString(qtdHorasTabDia))));
		funcionario.getValorHoraOpt().ifPresent(
				valorHora ->dto.setValorHora(Optional.of(valorHora.toString())));
		
		return dto;
		
	}
	
}
