package tn.esprit.tpautoloc.repository;

import java.math.BigDecimal;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import tn.esprit.tpautoloc.domain.Vehicule;
import tn.esprit.tpautoloc.domain.enums.CategorieVehicule;
import tn.esprit.tpautoloc.domain.enums.StatutVehicule;

public interface IVehiculeRepository extends JpaRepository<Vehicule, Long> {
    List<Vehicule> findByAgenceVille(String ville);

    List<Vehicule> findByStatutAndAgence_Id(StatutVehicule statut, Long agenceId);

    Page<Vehicule> findByStatut(StatutVehicule statut, Pageable pageable);

    List<Vehicule> findByAgence_VilleAndCategorie(String ville, CategorieVehicule categorie);

    boolean existsByIdAndStatut(Long vehiculeId, StatutVehicule statut);

    long countByStatut(StatutVehicule statut);

    List<Vehicule> findByTarifJournalierLessThan(BigDecimal tarif);

    List<Vehicule> findByTarifJournalierGreaterThan(BigDecimal tarif);

    List<Vehicule> findAllByOrderByTarifJournalierAsc();
}