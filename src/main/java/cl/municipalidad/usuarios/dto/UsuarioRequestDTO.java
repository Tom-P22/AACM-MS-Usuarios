package cl.municipalidad.usuarios.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UsuarioRequestDTO {

    @NotBlank(message = "El campo 'rut' es obligatorio")
    String rut;

    @NotBlank(message = "El campo 'nombre' es obligatorio")
    String nombre;

    @NotBlank(message = "El campo 'email' es obligatorio")
    @Email(message = "El campo 'email' debe ser una dirección de correo electrónico válida")
    String email;

    @NotBlank(message = "El campo 'password' es obligatorio")
    String password;

    @NotBlank(message = "El rol es obligatorio (ADMIN o CIUDADANO")
    String rol;
} 
