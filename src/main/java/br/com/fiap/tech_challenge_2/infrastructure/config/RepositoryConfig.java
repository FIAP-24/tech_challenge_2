package br.com.fiap.tech_challenge_2.infrastructure.config;

import br.com.fiap.tech_challenge_2.domain.repository.ItemCardapioRepository;
import br.com.fiap.tech_challenge_2.domain.repository.RestauranteRepository;
import br.com.fiap.tech_challenge_2.domain.repository.UsuarioRepository;
import br.com.fiap.tech_challenge_2.infrastructure.persistence.repository.ItemCardapioJpaRepository;
import br.com.fiap.tech_challenge_2.infrastructure.persistence.repository.ItemCardapioRepositoryImpl;
import br.com.fiap.tech_challenge_2.infrastructure.persistence.repository.RestauranteJpaRepository;
import br.com.fiap.tech_challenge_2.infrastructure.persistence.repository.RestauranteRepositoryImpl;
import br.com.fiap.tech_challenge_2.infrastructure.persistence.repository.UsuarioJpaRepository;
import br.com.fiap.tech_challenge_2.infrastructure.persistence.repository.UsuarioRepositoryImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RepositoryConfig {
    // Removido todos os beans manuais de repositórios para evitar conflito com o Spring Data JPA
} 