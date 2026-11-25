package tn.esprit.tpautoloc.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import tn.esprit.tpautoloc.domain.Client;

public interface IClientRepository extends JpaRepository<Client, Long> {
    Optional<Client> findByEmail(String email);

    Optional<Client> findByNumPermis(String numPermis);

    List<Client> findByNomContaining(String nom);

    boolean existsByEmail(String email);
}