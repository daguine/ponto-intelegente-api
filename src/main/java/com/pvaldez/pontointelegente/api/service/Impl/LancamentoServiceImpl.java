package com.pvaldez.pontointelegente.api.service.Impl;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.pvaldez.pontointelegente.api.entities.Lancamento;
import com.pvaldez.pontointelegente.api.repository.LancamentoRepository;
import com.pvaldez.pontointelegente.api.service.LancamentoService;

@Service
public class LancamentoServiceImpl implements LancamentoService{

	private static final Logger log = LoggerFactory.getLogger(LancamentoServiceImpl.class);
	
	@Autowired
	private LancamentoRepository lancamentoRepository;
	
	@Override
	public Page<Lancamento> buscarPorFuncionarioId(Long funcionarioId, PageRequest pageRequest) {
		log.info("Buscando lancamento para o funcionario ID {}" , funcionarioId);
		
		return this.lancamentoRepository.findByFuncionarioId(funcionarioId, pageRequest);
	}

	@Override
	public Optional<Lancamento> buscarPorId(Long id) {
		log.info("Buscando um lancamento pelo ID {}", id);
		
		return Optional.ofNullable(this.lancamentoRepository.findOne(id));
	}

	@Override
	public Lancamento persistir(Lancamento lancamento) {
		log.info("Persistindo o lancamento: {}", lancamento);
		
		return this.lancamentoRepository.save(lancamento);
	}

	@Override
	public void remover(Long id) {
		log.info("remover um lancamento pelo id {}", id);
		
		this.lancamentoRepository.delete(id);
				
	}

}
