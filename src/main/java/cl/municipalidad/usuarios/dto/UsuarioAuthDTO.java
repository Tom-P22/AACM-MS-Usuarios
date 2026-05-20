package cl.municipalidad.usuarios.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UsuarioAuthDTO {
    private String email;
    private String password;
    private String nombre;
    private String rolUsuario;
    private Boolean activo;
}