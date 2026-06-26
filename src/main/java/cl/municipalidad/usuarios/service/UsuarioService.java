package cl.municipalidad.usuarios.service;

import java.util.List;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import cl.municipalidad.usuarios.dto.UsuarioAuthDTO;
import cl.municipalidad.usuarios.dto.request.UsuarioRequestDTO;
import cl.municipalidad.usuarios.dto.response.UsuarioResponseDTO;
import cl.municipalidad.usuarios.exception.BadRequestException;
import cl.municipalidad.usuarios.exception.ResourceNotFoundException;
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
            throw new BadRequestException("El email ya está registrado");
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
                .activo(true)
                .build();

        return mapToDTO(usuarioRepository.save(nuevoUsuario));
    }

    public List<UsuarioResponseDTO> obtenerTodosLosUsuarios() {
        return usuarioRepository.findByActivoTrue().stream()
                .map(this::mapToDTO)
                .toList();
    }

    public UsuarioResponseDTO obtenerUsuarioPorId(Long id) {
        Usuario usuario = usuarioRepository.findByIdAndActivoTrue(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario inactivo o no existente con ID: " + id));
        return mapToDTO(usuario);
    }

    public void eliminarUsuario(Long id) {
        Usuario usuario = usuarioRepository.findByIdAndActivoTrue(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario inactivo o no existente con ID: " + id));
        
        usuario.setActivo(false);
        usuarioRepository.save(usuario);
    }


    public UsuarioResponseDTO actualizarUsuario(Long id, UsuarioRequestDTO request) {
        Usuario usuarioExistente = usuarioRepository.findByIdAndActivoTrue(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario inactivo o no existente con ID: " + id));

        // Validar si intenta cambiar email/rut por uno ya ocupado por otro usuario
        if (!usuarioExistente.getEmail().equals(request.getEmail()) && usuarioRepository.existsByEmail(request.getEmail())) {
            throw new BadRequestException("El email ya está registrado por otro usuario");
        }
        if (!usuarioExistente.getRut().equals(request.getRut()) && usuarioRepository.existsByRut(request.getRut())) {
            throw new BadRequestException("El RUT ya está registrado por otro usuario");
        }

        usuarioExistente.setNombre(request.getNombre());
        usuarioExistente.setEmail(request.getEmail());
        usuarioExistente.setRut(request.getRut());
        usuarioExistente.setTipoUsuario(request.getTipoUsuario());
        usuarioExistente.setRolUsuario(request.getRolUsuario());
        
        if (request.getPassword() != null && !request.getPassword().isBlank()) {
            usuarioExistente.setPassword(passwordEncoder.encode(request.getPassword()));
        }

        return mapToDTO(usuarioRepository.save(usuarioExistente));
    }

    public UsuarioResponseDTO obtenerUsuarioPorEmail(String email) {
        Usuario usuario = usuarioRepository.findByEmailAndActivoTrue(email)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario inactivo o no existente con email: " + email));
        return mapToDTO(usuario);
    }

    public UsuarioResponseDTO obtenerUsuarioPorRut(String rut) {
        Usuario usuario = usuarioRepository.findByRutAndActivoTrue(rut)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario inactivo o no existente con RUT: " + rut));
        return mapToDTO(usuario);
    }

    public UsuarioAuthDTO obtenerUsuarioParaAuth(String email) {
        Usuario usuario = usuarioRepository.findByEmailAndActivoTrue(email)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario inactivo o no existente con email: " + email));

        return UsuarioAuthDTO.builder()
                .email(usuario.getEmail())
                .password(usuario.getPassword()) 
                .nombre(usuario.getNombre())
                .rolUsuario(usuario.getRolUsuario().name()) 
                .activo(usuario.isActivo())
                .build();
    }
    
    private UsuarioResponseDTO mapToDTO(Usuario usuario) {
        return UsuarioResponseDTO.builder()
                .id(usuario.getId())
                .rut(usuario.getRut())
                .nombre(usuario.getNombre())
                .email(usuario.getEmail())
                .tipoUsuario(usuario.getTipoUsuario())
                .rolUsuario(usuario.getRolUsuario())
                .activo(usuario.isActivo())   
                .build();
    }
}

