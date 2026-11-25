package tn.esprit.tpautoloc.repository;

import java.time.LocalDate;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import tn.esprit.tpautoloc.domain.Reservation;
import tn.esprit.tpautoloc.domain.enums.StatutReservation;

public interface IReservationRepository extends JpaRepository<Reservation, Long> {
    List<Reservation> findByClientIdClient(Long idClient);

    List<Reservation> findByVehicule_IdAndStatutNot(Long vehiculeId, StatutReservation statut);

    List<Reservation> findByStatut(StatutReservation statut);

    List<Reservation> findByDateDebutBetween(LocalDate debut, LocalDate fin);

    List<Reservation> findByDateFinAfter(LocalDate date);

    List<Reservation> findByStatutAndVehiculeAgenceId(StatutReservation statut, Long agenceId);

    boolean existsByVehicule_IdAndStatut(Long vehiculeId, StatutReservation statut);
}