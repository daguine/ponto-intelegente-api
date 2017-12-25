package com.pvaldez.pontointelegente.api.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class PasswordUtil {

	private static final Logger log = LoggerFactory.getLogger(PasswordUtil.class);	
	
	public PasswordUtil() {
		
	}
	
	public static String gerarBCript(String senha) {
		
		if (senha == null) {
			return senha;
		}
		
		log.info("Gerando hash com o BCrypt");
		BCryptPasswordEncoder bcCryptPasswordEncoder = new BCryptPasswordEncoder();
		return bcCryptPasswordEncoder.encode(senha);
	}
}
