package com.pvaldez.pontointelegente.api.service;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import com.pvaldez.pontointelegente.api.entities.Lancamento;

public interface LancamentoService {
	
	/**
	 * retornar uma lista paginada de lancamento de um determinado funcionario 
	 * @param funcionarioId
	 * @param pageRequest
	 * @return Page<Lancamento>
	 */
	Page<Lancamento> buscarPorFuncionarioId(Long funcionarioId, PageRequest pageRequest);
	
	/**
	 * Retornar um lancamento por ID
	 * 
	 * @param id
	 * @return Optional<Lancamento>
	 */
	Optional<Lancamento> buscarPorId(Long id);
	
	/**
	 * Persiste um lancamento na base de dados
	 * @param lancamento
	 * @return Lancamento
	 */
	Lancamento persistir(Lancamento lancamento);
	
	/**
	 * Remover um lancamento da base de dados
	 * 
	 * @param id
	 */
	void remover(Long id);
}
