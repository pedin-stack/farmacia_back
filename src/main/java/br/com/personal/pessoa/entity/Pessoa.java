package br.com.personal.pessoa.entity;

import br.com.personal.insfrastructre.persistenceentity.PersistenceEntity;
import br.com.personal.remedio.entity.Remedio;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@Table(name = "pessoas")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Pessoa extends PersistenceEntity {

    @Column(nullable = false)
    String nome;

    @OneToMany(mappedBy = "pessoa", fetch = FetchType.EAGER)
    private List<Remedio> remedios;
}
