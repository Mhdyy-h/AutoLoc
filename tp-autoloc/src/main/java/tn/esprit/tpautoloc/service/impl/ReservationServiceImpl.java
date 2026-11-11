package tn.esprit.tpautoloc.service.impl;

import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tn.esprit.tpautoloc.domain.Client;
import tn.esprit.tpautoloc.domain.Reservation;
import tn.esprit.tpautoloc.domain.enums.StatutReservation;
import tn.esprit.tpautoloc.exception.ResourceNotFoundException;
import tn.esprit.tpautoloc.repository.IReservationRepository;
import tn.esprit.tpautoloc.service.IReservationService;
import tn.esprit.tpautoloc.service.observer.ReservationSubject;

@Service
@RequiredArgsConstructor
public class ReservationServiceImpl implements IReservationService {

    private final ReservationSubject reservationSubject;
    private final IReservationRepository reservationRepository;

    @Override
    public Reservation create(Reservation reservation) {
        validerDates(reservation);
        return reservationRepository.save(reservation);
    }


    /**
     * Crée une réservation et notifie tous les observateurs
     */
    public Reservation creerReservation(Client client) {
        Reservation reservation = Reservation.builder()
                .client(client)
                .dateDebut(LocalDate.now())
                .statut(StatutReservation.valueOf(StatutReservation.CONFIRMEE.name()))
                .build();
// Déclenche le process métier via le pattern Observer
        reservationSubject.notifierConfirmation(reservation);
        return reservation;
    }

    /**
     * Annule une réservation existante et notifie tous les observateurs
     */
    public Reservation annulerReservation(Reservation reservation) {
        reservation.setStatut(StatutReservation.valueOf(StatutReservation.ANNULEE.name()));
        reservationSubject.notifierAnnulation(reservation);
        return reservation;
    }




    @Override
    public Reservation findById(Long id) {
        return reservationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Reservation", id));
    }

    @Override
    public List<Reservation> findAll() {
        return reservationRepository.findAll();
    }

    @Override
    public Reservation update(Long id, Reservation reservation) {
        validerDates(reservation);
        Reservation existante = findById(id);
        existante.setDateDebut(reservation.getDateDebut());
        existante.setDateFin(reservation.getDateFin());
        existante.setStatut(reservation.getStatut());
        return reservationRepository.save(existante);
    }

    @Override
    public void deleteById(Long id) {
        if (!reservationRepository.existsById(id)) {
            throw new ResourceNotFoundException("Reservation", id);
        }
        reservationRepository.deleteById(id);
    }

    // ---- règle de gestion ----
    private void validerDates(Reservation reservation) {
        if (reservation.getDateDebut() == null) {
            throw new IllegalArgumentException(
                    "La date de début de la réservation est obligatoire.");
        }
        if (reservation.getDateFin() == null) {
            throw new IllegalArgumentException(
                    "La date de fin de la réservation est obligatoire.");
        }
        if (!reservation.getDateFin().isAfter(reservation.getDateDebut())) {
            throw new IllegalArgumentException(
                    "La date de fin de la réservation doit être postérieure à la date de début.");
        }
    }
}
