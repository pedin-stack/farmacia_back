package br.com.personal.remedio.controller;

import br.com.personal.remedio.dto.RemedioRequestDTO;
import br.com.personal.remedio.dto.RemedioResponseDTO;
import br.com.personal.remedio.entity.Remedio;
import br.com.personal.remedio.service.RemedioService;
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
@RequestMapping("/remedios")
@RequiredArgsConstructor
public class RemedioController {

    private final RemedioService remedioService;
    private final ModelMapper modelMapper;

    private RemedioResponseDTO toDto(Remedio remedio) {
        return modelMapper.map(remedio, RemedioResponseDTO.class);
    }

    private Remedio toEntity(RemedioRequestDTO dto) {
        return modelMapper.map(dto, Remedio.class);
    }

    @GetMapping
    public ResponseEntity<Page<RemedioResponseDTO>> findAll(@PageableDefault(size = 20) Pageable pageable) {
        Page<Remedio> page = remedioService.findAll(pageable);
        return ResponseEntity.ok(page.map(this::toDto));
    }

    @GetMapping("/{id}")
    public ResponseEntity<RemedioResponseDTO> findById(@PathVariable Long id) {
        Remedio remedio = remedioService.findById(id);
        return ResponseEntity.ok(toDto(remedio));
    }

    @PostMapping
    public ResponseEntity<RemedioResponseDTO> save(@RequestBody @Valid RemedioRequestDTO dto) {
        // Agora passamos o DTO direto para o service
        Remedio saved = remedioService.save(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(toDto(saved));
    }

    @PutMapping("/{id}")
    public ResponseEntity<RemedioResponseDTO> update(@PathVariable Long id, @RequestBody @Valid RemedioRequestDTO dto) {
        Remedio updated = remedioService.update(id, dto);
        return ResponseEntity.ok(toDto(updated));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        remedioService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
