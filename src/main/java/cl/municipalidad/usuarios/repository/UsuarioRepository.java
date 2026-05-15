package cl.municipalidad.usuarios.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import cl.municipalidad.usuarios.model.Usuario;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    boolean existsByRut(String rut);

    boolean existsByEmail(String email);

    Optional<Usuario> findByEmailAndActivoTrue(String email);

    Optional<Usuario> findByRutAndActivoTrue(String rut);

    List<Usuario> findByActivoTrue();

    Optional<Usuario> findByIdAndActivoTrue(Long id);
}
