package br.com.personal.user.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class UserRequestDTO {

    private String email;

    private String password;
}
