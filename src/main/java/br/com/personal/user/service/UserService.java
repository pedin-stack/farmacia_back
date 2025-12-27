package br.com.personal.user.service;

import br.com.personal.user.dto.UserRequestDTO;
import br.com.personal.user.entity.User;
import br.com.personal.user.repository.Repository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final Repository userRepository;

    public Page<User> findAll(Pageable pageable) {
        return userRepository.findAll(pageable);
    }

    public User findById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado com o ID: " + id));
    }

    @Transactional
    public User save(User user) {
        // Regra extra opcional: verificar se o email já existe antes de salvar
        return userRepository.save(user);
    }

    @Transactional
    public User update(Long id, UserRequestDTO dto) {
        User user = findById(id);
        user.setEmail(dto.getEmail());

        if (dto.getPassword() != null && !dto.getPassword().isBlank()) {
            user.setPassword(dto.getPassword());
        }
        return userRepository.save(user);
    }


    public User login(String email, String password) {

        if (email == null || password == null) {
            throw new IllegalArgumentException("Email e senha são obrigatórios.");
        }

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado."));

        System.out.println("--- DEBUG LOGIN ---");
        System.out.println("Email recebido: " + email);
        System.out.println("Senha no Banco: [" + user.getPassword() + "]");
        System.out.println("Senha Digitada: [" + password + "]");
        System.out.println("As senhas são iguais? " + user.getPassword().equals(password));
        System.out.println("-------------------");


        if (!user.getPassword().equals(password)) {
            throw new RuntimeException("Senha incorreta.");
        }

        return user;
    }

    @Transactional
    public void delete(Long id) {
        User user = findById(id);
        userRepository.delete(user);
    }
}