package br.com.personal.user.repository;

import br.com.personal.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface Repository extends JpaRepository<User, Long> {

//crud

}
