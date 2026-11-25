package tn.esprit.tpautoloc.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import tn.esprit.tpautoloc.domain.Employe;
import tn.esprit.tpautoloc.domain.enums.RoleEmploye;

public interface IEmployeRepository extends JpaRepository<Employe, Long> {
    List<Employe> findByAgenceNom(String nomAgence);

    List<Employe> findByAgence_IdAndRole(Long agenceId, RoleEmploye role);

    List<Employe> findByNomContaining(String nom);
}