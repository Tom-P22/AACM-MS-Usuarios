package cl.municipalidad.usuarios.service;

import java.util.List;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import cl.municipalidad.usuarios.dto.UsuarioRequestDTO;
import cl.municipalidad.usuarios.dto.UsuarioResponseDTO;
import cl.municipalidad.usuarios.model.Usuario; 
import cl.municipalidad.usuarios.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor

public class  UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    public UsuarioResponseDTO crearUsuario(UsuarioRequestDTO request) {

        if(usuarioRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("El email ya está registrado");
        }

        if(usuarioRepository.existsByRut(request.getRut())) {
            throw new RuntimeException("El RUT ya está registrado");
        }

        Usuario nuevoUsuario = Usuario.builder()
                .rut(request.getRut())
                .nombre(request.getNombre())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .rolUsuario(request.getRolUsuario())
                .tipoUsuario(request.getTipoUsuario())
                .build();

        Usuario usuarioGuardado = usuarioRepository.save(nuevoUsuario);

        return new UsuarioResponseDTO(
                usuarioGuardado.getId(),
                usuarioGuardado.getRut(),
                usuarioGuardado.getNombre(),
                usuarioGuardado.getEmail(),
                usuarioGuardado.getTipoUsuario(),
                usuarioGuardado.getRolUsuario()
            );
        }

    public List<UsuarioResponseDTO> obtenerTodosLosUsuarios() {
        List<Usuario> usuarios = usuarioRepository.findByActivoTrue();

        return usuarios.stream()
                .map(usuario -> new UsuarioResponseDTO(
                        usuario.getId(),
                        usuario.getRut(),
                        usuario.getNombre(),
                        usuario.getEmail(),
                        usuario.getTipoUsuario(),
                        usuario.getRolUsuario()
                ))
                .toList();
        }   

    public UsuarioResponseDTO obtenerUsuarioPorId(Long id) {
        Usuario usuario = usuarioRepository.findByIdAndActivoTrue(id)
                .orElseThrow(() -> new RuntimeException("Usuario inactivo o no existente con ID: " + id));

                return UsuarioResponseDTO.builder()
                        .id(usuario.getId())
                        .rut(usuario.getRut())
                        .nombre(usuario.getNombre())
                        .email(usuario.getEmail())
                        .rolUsuario(usuario.getRolUsuario())
                        .tipoUsuario(usuario.getTipoUsuario())
                        .build();
    }

    public void eliminarUsuario(Long id) {
        Usuario usuario = usuarioRepository.findByIdAndActivoTrue(id)
                .orElseThrow(() -> new RuntimeException("Usuario inactivo o no existente con ID: " + id));

        usuario.setActivo(false);
        usuarioRepository.save(usuario);
    }


    public UsuarioResponseDTO actualizarUsuario(Long id, UsuarioRequestDTO request) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario inactivo o no existente con ID: " + id));

        if (!usuario.getEmail().equals(request.getEmail()) && 
            usuarioRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("El email ya está registrado");
        }

        if (!usuario.getRut().equals(request.getRut()) && 
            usuarioRepository.existsByRut(request.getRut())) {
            throw new RuntimeException("El RUT ya está registrado");
        }

        usuario.setRut(request.getRut());
        usuario.setNombre(request.getNombre());
        usuario.setEmail(request.getEmail());
        usuario.setRolUsuario(request.getRolUsuario());
        usuario.setTipoUsuario(request.getTipoUsuario());
        if (request.getPassword() != null && !request.getPassword().isEmpty()) {
            usuario.setPassword(passwordEncoder.encode(request.getPassword()));
        }

        Usuario usuarioActualizado = usuarioRepository.save(usuario);

        return new UsuarioResponseDTO(
                usuarioActualizado.getId(),
                usuarioActualizado.getRut(),
                usuarioActualizado.getNombre(),
                usuarioActualizado.getEmail(),
                usuarioActualizado.getTipoUsuario(),
                usuarioActualizado.getRolUsuario()
        );
    }

    public UsuarioResponseDTO obtenerUsuarioPorEmail(String email) {
        Usuario usuario = usuarioRepository.findByEmailAndActivoTrue(email)
                .orElseThrow(() -> new RuntimeException("Usuario inactivo o no existente con email: " + email));

        return new UsuarioResponseDTO(
                usuario.getId(),
                usuario.getRut(),
                usuario.getNombre(),
                usuario.getEmail(),
                usuario.getTipoUsuario(),
                usuario.getRolUsuario()
        );
    }

    public UsuarioResponseDTO obtenerUsuarioPorRut(String rut) {
        Usuario usuario = usuarioRepository.findByRutAndActivoTrue(rut)
                .orElseThrow(() -> new RuntimeException("Usuario inactivo o no existente con RUT: " + rut));

        return new UsuarioResponseDTO(
                usuario.getId(),
                usuario.getRut(),
                usuario.getNombre(),
                usuario.getEmail(),
                usuario.getTipoUsuario(),
                usuario.getRolUsuario()
        );
    }

}
