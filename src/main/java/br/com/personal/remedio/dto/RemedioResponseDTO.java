package br.com.personal.remedio.dto;


import br.com.personal.insfrastructre.role.StatusRole;
import lombok.Data;
import java.time.LocalDate;

@Data
public class RemedioResponseDTO {

    private Long id;
    private String nome;
    private Double quantidade;
    private Double usoDiario;
    private LocalDate proxCompra;
    private StatusRole status;
    private Long pessoaId;
}