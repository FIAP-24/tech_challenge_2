package br.com.fiap.tech_challenge_2.application.usecase.impl;

import br.com.fiap.tech_challenge_2.application.dto.request.TipoUsuarioDTO;
import br.com.fiap.tech_challenge_2.application.mapper.TipoUsuarioMapper;
import br.com.fiap.tech_challenge_2.application.usecase.FindTipoUsuarioUseCase;
import br.com.fiap.tech_challenge_2.domain.model.TipoUsuario;
import br.com.fiap.tech_challenge_2.domain.repository.TipoUsuarioRepository;
import br.com.fiap.tech_challenge_2.interfaces.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
@RequiredArgsConstructor
public class FindTipoUsuarioUseCaseImpl implements FindTipoUsuarioUseCase {

    private final TipoUsuarioRepository repository;
    private final TipoUsuarioMapper mapper;

    @Override
    @Transactional(readOnly = true)
    public List<TipoUsuarioDTO> findAll() {
        List<TipoUsuario> tipos = repository.findAll();
        return tipos.stream()
                .map(mapper::toDTO)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public TipoUsuarioDTO findById(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("ID cannot be null");
        }
        
        TipoUsuario tipoUsuario = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Tipo de usuário não encontrado com id: " + id));
        
        return mapper.toDTO(tipoUsuario);
    }
} 