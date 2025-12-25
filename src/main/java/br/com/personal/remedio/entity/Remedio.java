package br.com.personal.remedio.entity;

import br.com.personal.insfrastructre.role.StatusRole;
import br.com.personal.pessoa.entity.Pessoa;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import br.com.personal.insfrastructre.persistenceentity.PersistenceEntity;
import java.time.LocalDate;

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

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    StatusRole status;

    @ManyToOne
    @JoinColumn(name = "pessoa_id") // Cria a chave estrangeira na tabela Remedio
    private Pessoa pessoa;


}
