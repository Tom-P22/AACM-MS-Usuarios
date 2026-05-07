package cl.municipalidad.usuarios.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import cl.municipalidad.usuarios.model.usuariosModel;

public interface UsuarioRepositrory extends JpaRepository<usuariosModel, Long> {
    Optional<usuariosModel> findByRut(String rut);
    Optional<usuariosModel> findByEmail(String email);

}
