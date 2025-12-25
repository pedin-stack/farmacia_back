package br.com.personal.pessoa.dto;

import br.com.personal.remedio.dto.RemedioResponseDTO;
import lombok.Data;

import java.util.List;

@Data
public class PessoaResponseDTO {

    private Long id;
    private String nome;

    // Retorna a lista completa de remédios associados a essa pessoa
    private List<RemedioResponseDTO> remedios;
}