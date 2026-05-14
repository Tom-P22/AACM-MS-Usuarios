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

        if(usuarioRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new RuntimeException("El email ya está registrado");
        }

        Usuario nuevoUsuario = Usuario.builder()
                .rut(request.getRut())
                .nombre(request.getNombre())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .rol(request.getRol())
                .build();

        Usuario usuarioGuardado = usuarioRepository.save(nuevoUsuario);

        return new UsuarioResponseDTO(
                usuarioGuardado.getId(),
                usuarioGuardado.getRut(),
                usuarioGuardado.getNombre(),
                usuarioGuardado.getEmail(),
                usuarioGuardado.getRol()
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
                        usuario.getRol()
                ))
                .toList();
        }   

    public UsuarioResponseDTO obtenerUsuarioPorId(Long id) {
        Usuario usuario = usuarioRepository.findByIdAndActivoTrue(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado con ID: " + id));

                return UsuarioResponseDTO.builder()
                        .id(usuario.getId())
                        .rut(usuario.getRut())
                        .nombre(usuario.getNombre())
                        .email(usuario.getEmail())
                        .rol(usuario.getRol())
                        .build();
    }

    public void eliminarUsuario(Long id) {
        Usuario usuario = usuarioRepository.findByIdAndActivoTrue(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado con ID: " + id));

        usuario.setActivo(false);
        usuarioRepository.save(usuario);
    }


    public UsuarioResponseDTO actualizarUsuario(Long id, UsuarioRequestDTO request) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado con ID: " + id));

        if (!usuario.getEmail().equals(request.getEmail()) && 
            usuarioRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new RuntimeException("El email ya está registrado");
        }

        usuario.setRut(request.getRut());
        usuario.setNombre(request.getNombre());
        usuario.setEmail(request.getEmail());
        usuario.setRol(request.getRol());

        if (request.getPassword() != null && !request.getPassword().isEmpty()) {
            usuario.setPassword(passwordEncoder.encode(request.getPassword()));
        }

        Usuario usuarioActualizado = usuarioRepository.save(usuario);

        return new UsuarioResponseDTO(
                usuarioActualizado.getId(),
                usuarioActualizado.getRut(),
                usuarioActualizado.getNombre(),
                usuarioActualizado.getEmail(),
                usuarioActualizado.getRol()
        );
    }

    public UsuarioResponseDTO obtenerUsuarioPorEmail(String email) {
        Usuario usuario = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado con email: " + email));

        return new UsuarioResponseDTO(
                usuario.getId(),
                usuario.getRut(),
                usuario.getNombre(),
                usuario.getEmail(),
                usuario.getRol()
        );
    }

}
