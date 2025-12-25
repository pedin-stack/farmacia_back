package br.com.personal.user.entity;

import br.com.personal.insfrastructre.persistenceentity.PersistenceEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "users")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class User extends PersistenceEntity {
//por ser um sistema extremamente simples um sistema rebuscado de login é desnecessário
    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;

}
