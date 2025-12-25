package br.com.personal.user.controller;

import br.com.personal.user.dto.UserRequestDTO;
import br.com.personal.user.dto.UserResponseDTO;
import br.com.personal.user.entity.User;
import br.com.personal.user.service.Service;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class Controller {

    private final Service userService;
    private final ModelMapper modelMapper;

    private UserResponseDTO toDto(User user) {
        return modelMapper.map(user, UserResponseDTO.class);
    }

    private User toEntity(UserRequestDTO dto) {
        return modelMapper.map(dto, User.class);
    }

    @GetMapping
    public ResponseEntity<Page<UserResponseDTO>> findAll(@PageableDefault(size = 20) Pageable pageable) {
        Page<User> usersPage = userService.findAll(pageable);
        Page<UserResponseDTO> dtosPage = usersPage.map(this::toDto);
        return ResponseEntity.ok(dtosPage);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponseDTO> findById(@PathVariable Long id) {
        User user = userService.findById(id);
        return ResponseEntity.ok(toDto(user));
    }

    @PostMapping
    public ResponseEntity<UserResponseDTO> save(@RequestBody @Valid UserRequestDTO dto) {
        User userEntity = toEntity(dto);
        User savedUser = userService.save(userEntity);
        return ResponseEntity.status(HttpStatus.CREATED).body(toDto(savedUser));
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserResponseDTO> update(@PathVariable Long id, @RequestBody @Valid UserRequestDTO dto) {
        User userAtualizado = userService.update(id, dto);
        return ResponseEntity.ok(toDto(userAtualizado));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        userService.delete(id);
        return ResponseEntity.noContent().build();
    }

}