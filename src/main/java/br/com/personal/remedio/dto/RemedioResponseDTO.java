package br.com.personal.remedio.dto;


import br.com.personal.insfrastructre.role.StatusRole;
import lombok.Data;

import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class RemedioResponseDTO {

    private Long id;
    private String nome;
    private Double quantidade;
    private Double usoDiario;
    private LocalDate proxCompra;
    private StatusRole status;
    Time horaConsumo;
    private Long pessoaId;
}