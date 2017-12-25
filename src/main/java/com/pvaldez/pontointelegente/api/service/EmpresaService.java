package com.pvaldez.pontointelegente.api.service;

import java.util.Optional;

import com.pvaldez.pontointelegente.api.entities.Empresa;

public interface EmpresaService {
	/**
	 * Retornar uma empresa dado um CNPJ
	 * @param cnpj
	 * @return
	 */
	Optional<Empresa> buscarPorCnpj(String cnpj);
	
	/**
	 * Registar uma nova empresa na base de dados
	 * @param empresa
	 * @return
	 */
	Empresa persistir(Empresa empresa);
}
