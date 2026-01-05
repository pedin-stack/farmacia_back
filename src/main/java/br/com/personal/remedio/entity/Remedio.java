package br.com.personal.remedio.entity;

import br.com.personal.insfrastructre.role.StatusRole;
import br.com.personal.pessoa.entity.Pessoa;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import br.com.personal.insfrastructre.persistenceentity.PersistenceEntity;

import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "remedio")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

public class Remedio extends PersistenceEntity {

    @Column(nullable = false)
    String nome;

    @Column(nullable = false)
    double quantidade;

    @Column(nullable = false)
    LocalDate proxCompra;

    @Column(nullable = false)
    double usoDiario;

    @Column(nullable = true)
    private Time horaConsumo;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    StatusRole status;

    @ManyToOne
    @JoinColumn(name = "pessoa_id")
    private Pessoa pessoa;


}
