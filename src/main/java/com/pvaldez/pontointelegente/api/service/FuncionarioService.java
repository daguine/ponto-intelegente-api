package com.pvaldez.pontointelegente.api.service;

import java.util.Optional;

import com.pvaldez.pontointelegente.api.entities.Funcionario;

public interface FuncionarioService {
	/**
	 * Persiste um funcionario na base de dados
	 * @param funcionario
	 * @return Funcionario
	 */
	Funcionario persistir(Funcionario funcionario);

	/**
	 * Busca e retorna um funcionario dada um cpf
	 * @param cpf
	 * @return Optional<Funcionario>
	 */
	Optional<Funcionario> buscarPorCpf(String cpf);
	
	/**
	 * Busca e retorna um funcionario dada um email
	 * @param email
	 * @return
	 */
	Optional<Funcionario> buscarPorEmail(String email);
	
	/**
	 * 
	 * @param id
	 * @return Optional<Funcionario>
	 */
	Optional<Funcionario> buscarPorId(Long id);
}
