package br.com.fiap.tech_challenge_2.application.service.impl;

import br.com.fiap.tech_challenge_2.application.dto.request.TipoUsuarioDTO;
import br.com.fiap.tech_challenge_2.interfaces.exception.ResourceNotFoundException;
import br.com.fiap.tech_challenge_2.domain.model.TipoUsuario;
import br.com.fiap.tech_challenge_2.infrastructure.repository.TipoUsuarioRepository;
import br.com.fiap.tech_challenge_2.application.service.TipoUsuarioService;
import br.com.fiap.tech_challenge_2.application.mapper.TipoUsuarioToDtoMapper;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TipoUsuarioServiceImpl implements TipoUsuarioService {

    private final TipoUsuarioRepository repository;
    private final TipoUsuarioToDtoMapper mapper;

    @Override
    @Transactional
    public TipoUsuarioDTO create(TipoUsuarioDTO dto) {
        TipoUsuario tipoUsuario = mapper.toEntity(dto);
        return mapper.toDTO(repository.save(tipoUsuario));
    }

    @Override
    @Transactional(readOnly = true)
    public TipoUsuarioDTO findById(Long id) {
        return repository.findById(id)
                .map(mapper::toDTO)
                .orElseThrow(() -> new ResourceNotFoundException("Tipo de usuário não encontrado com id: " + id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<TipoUsuarioDTO> findAll() {
        return repository.findAll().stream()
                .map(mapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public TipoUsuarioDTO update(Long id, TipoUsuarioDTO dto) {
        TipoUsuario tipoUsuario = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Tipo de usuário não encontrado com id: " + id));
        tipoUsuario.setNome(dto.nome());
        return mapper.toDTO(repository.save(tipoUsuario));
    }

    @Override
    @Transactional
    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("Tipo de usuário não encontrado com id: " + id);
        }
        repository.deleteById(id);
    }
}