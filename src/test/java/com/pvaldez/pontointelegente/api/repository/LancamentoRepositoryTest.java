package com.pvaldez.pontointelegente.api.repository;

import static org.junit.Assert.assertEquals;

import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import com.pvaldez.pontointelegente.api.entities.Empresa;
import com.pvaldez.pontointelegente.api.entities.Funcionario;
import com.pvaldez.pontointelegente.api.entities.Lancamento;
import com.pvaldez.pontointelegente.api.enums.PerfilEnum;
import com.pvaldez.pontointelegente.api.enums.TipoEnum;
import com.pvaldez.pontointelegente.api.utils.PasswordUtil;
import com.pvaldez.pontointelegente.api.utils.PasswordUtilTest;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
public class LancamentoRepositoryTest {
	@Autowired
	private LancamentoRepository lancamentoRepository;
	
	@Autowired
	private EmpresaRepository empresaRepository;
	
	@Autowired
	private FuncionarioRepository funcionarioRepository;
	
	private Long funcionarioId;
	
	@Before
	public void setUp() throws Exception {
		Empresa empresa = this.empresaRepository.save(obterDadosEmpresa());
		
		Funcionario funcionario = this.funcionarioRepository.save(obterDadosFuncionario(empresa));
		this.funcionarioId = funcionario.getId();
		
		this.lancamentoRepository.save(obterDadosLancamentos(funcionario));
		this.lancamentoRepository.save(obterDadosLancamentos(funcionario));
		
	}
	
	@After
	public void teraDown() throws Exception {
		this.empresaRepository.deleteAll();
	}
	
	@Test
	public void testBuscarLancamentosPorFuncionario() {
		List<Lancamento> lancamentos = this.lancamentoRepository.findByFuncionarioId(funcionarioId);
		
		assertEquals(2, lancamentos.size());
	}
	
	@Test
	public void testBuscarLancamentosPorFuncionarioIdPaginado() {
		PageRequest page = new PageRequest(0, 10);
		Page<Lancamento>lancamento = this.lancamentoRepository.findByFuncionarioId(funcionarioId,page);
		
		assertEquals(2, lancamento.getTotalElements());
	}
	
	private Lancamento obterDadosLancamentos(Funcionario funcionario) {
		Lancamento lancamento = new Lancamento();
		lancamento.setData(new Date());
		lancamento.setTipo(TipoEnum.INICIO_ALMOCO);
		lancamento.setFuncionario(funcionario);
		return lancamento;
	}
	
	private Funcionario obterDadosFuncionario(Empresa empresa) throws NoSuchAlgorithmException {
		Funcionario funcionario = new Funcionario();
		funcionario.setName("Paul");
		funcionario.setPerfil(PerfilEnum.ROLE_USER);
		funcionario.setSenha(PasswordUtil.gerarBCript("123456"));
		funcionario.setCpf("2429173474");
		funcionario.setEmail("email@email.com");
		funcionario.setEmpresa(empresa);
		
		return funcionario;
	}
	
	private Empresa obterDadosEmpresa() {
		Empresa empresa = new Empresa();
		empresa.setRazaoSocial("Empresa de exempla");
		empresa.setCnpj("51463645000100");
		return empresa;
	}
}
