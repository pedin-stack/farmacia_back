package br.com.personal.pessoa.controller;

import br.com.personal.pessoa.dto.PessoaRequestDTO;
import br.com.personal.pessoa.dto.PessoaResponseDTO;
import br.com.personal.pessoa.entity.Pessoa;
import br.com.personal.pessoa.service.PessoaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/pessoas")
@RequiredArgsConstructor
public class PessoaController {

    private final PessoaService pessoaService;
    private final ModelMapper modelMapper;

    private PessoaResponseDTO toDto(Pessoa pessoa) {
        return modelMapper.map(pessoa, PessoaResponseDTO.class);
    }

    private Pessoa toEntity(PessoaRequestDTO dto) {
        return modelMapper.map(dto, Pessoa.class);
    }

    @GetMapping
    public ResponseEntity<Page<PessoaResponseDTO>> findAll(@PageableDefault(size = 10) Pageable pageable) {
        Page<Pessoa> page = pessoaService.findAll(pageable);
        return ResponseEntity.ok(page.map(this::toDto));
    }

    @GetMapping("/{id}")
    public ResponseEntity<PessoaResponseDTO> findById(@PathVariable Long id) {
        Pessoa pessoa = pessoaService.findById(id);
        return ResponseEntity.ok(toDto(pessoa));
    }

    @PostMapping
    public ResponseEntity<PessoaResponseDTO> save(@RequestBody @Valid PessoaRequestDTO dto) {
        Pessoa entity = toEntity(dto);
        Pessoa saved = pessoaService.save(entity);
        return ResponseEntity.status(HttpStatus.CREATED).body(toDto(saved));
    }

    @PutMapping("/{id}")
    public ResponseEntity<PessoaResponseDTO> update(@PathVariable Long id, @RequestBody @Valid PessoaRequestDTO dto) {
        Pessoa updated = pessoaService.update(id, dto);
        return ResponseEntity.ok(toDto(updated));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        pessoaService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
