package br.com.personal.user.repository;

import br.com.personal.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface Repository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);

//crud

}
