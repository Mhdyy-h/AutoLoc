package tn.esprit.tpautoloc.repository;

import java.time.LocalDate;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import tn.esprit.tpautoloc.domain.Paiement;

public interface IPaiementRepository extends JpaRepository<Paiement, Long> {
    List<Paiement> findByContrat_Id(Long contratId);

    List<Paiement> findByDatePaiementBetween(LocalDate debut, LocalDate fin);
}