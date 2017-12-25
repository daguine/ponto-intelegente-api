package com.pvaldez.pontointelegente.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import com.pvaldez.pontointelegente.api.entities.Empresa;

public interface EmpresaRepository extends JpaRepository<Empresa, Long>{
	@Transactional(readOnly = true)
	Empresa findByCnpj(String cnpj);
}
