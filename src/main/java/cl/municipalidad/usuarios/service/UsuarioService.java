package cl.municipalidad.usuarios.service;

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
}
