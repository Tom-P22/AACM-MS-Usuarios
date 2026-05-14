package cl.municipalidad.usuarios.model;


import cl.municipalidad.usuarios.enums.RolUsuario;
import cl.municipalidad.usuarios.enums.TipoUsuario;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.persistence.EnumType;



@Entity
@Table(name = "usuarios")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Usuario {
@Id
@GeneratedValue(strategy = GenerationType.IDENTITY)
private Long id;

@Column(unique = true, nullable = false)
private String rut;

@Column(nullable = false)
private String nombre;

@Column(unique = true,nullable = false)
private String email;

@Column(nullable = false)
private String password;

@Builder.Default
private boolean activo = true;

@Enumerated(EnumType.STRING)
@Builder.Default
private TipoUsuario tipoUsuario = TipoUsuario.PERSONA_NATURAL;

@Enumerated(EnumType.STRING)
@Builder.Default
private RolUsuario rolUsuario = RolUsuario.USER;

}
