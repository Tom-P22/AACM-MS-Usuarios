package cl.municipalidad.usuarios.dto.request;

import cl.municipalidad.usuarios.enums.RolUsuario;
import cl.municipalidad.usuarios.enums.TipoUsuario;
import cl.municipalidad.usuarios.validations.RutValido;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UsuarioRequestDTO {

    @NotBlank(message = "El campo 'rut' es obligatorio")
    @RutValido(message = "El RUT ingresado no es válido")
    private String rut;

    @NotBlank(message = "El campo 'nombre' es obligatorio")
    private String nombre;

    @NotBlank(message = "El campo 'email' es obligatorio")
    @Email(message = "El campo 'email' debe ser una dirección de correo electrónico válida")
    private String email;

    @NotBlank(message = "El campo 'password' es obligatorio")
    private String password;
    
    @NotNull(message = "El tipo de usuario es obligatorio (PERSONA_NATURAL, EMPRESA_PRIVADA, FUNDACION_BENEFICA o COLEGIO)")
    private TipoUsuario tipoUsuario;

    @NotNull(message = "El campo rolUsuario es obligatorio (ADMIN o USER)")
    private RolUsuario rolUsuario;
} 
