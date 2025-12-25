package br.com.personal.pessoa.service;

import br.com.personal.pessoa.dto.PessoaRequestDTO;
import br.com.personal.pessoa.entity.Pessoa;
import br.com.personal.pessoa.repository.PessoaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;

@Service
@RequiredArgsConstructor
public class PessoaService {

    private final PessoaRepository pessoaRepository;

    public Page<Pessoa> findAll(Pageable pageable) {
        return pessoaRepository.findAll(pageable);
    }

    public Pessoa findById(Long id) {
        return pessoaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Pessoa não encontrada com ID: " + id));
    }

    @Transactional
    public Pessoa save(Pessoa pessoa) {
        // Inicializa a lista se vier nula para evitar NullPointerException
        if (pessoa.getRemedios() == null) {
            pessoa.setRemedios(new ArrayList<>());
        }
        return pessoaRepository.save(pessoa);
    }

    @Transactional
    public Pessoa update(Long id, PessoaRequestDTO dto) {
        Pessoa pessoa = findById(id);
        pessoa.setNome(dto.getNome());
        return pessoaRepository.save(pessoa);
    }

    @Transactional
    public void delete(Long id) {
        Pessoa pessoa = findById(id);
        pessoaRepository.delete(pessoa);
    }
}