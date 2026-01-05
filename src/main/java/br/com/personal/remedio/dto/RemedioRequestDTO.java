package br.com.personal.remedio.dto;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
    public class RemedioRequestDTO {

        @NotBlank(message = "O nome do remédio é obrigatório")
        private String nome;

        @NotNull(message = "A quantidade é obrigatória")
        @Positive(message = "A quantidade deve ser maior que zero")
        private Double quantidade;

        @NotNull(message = "O uso diário é obrigatório")
        @Positive(message = "O uso diário deve ser maior que zero")
        private Double usoDiario;

        Time horaConsumo;

        private Long pessoaId;

        LocalDate proxCompra;

}
