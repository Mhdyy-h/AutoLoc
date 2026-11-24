package tn.esprit.tpautoloc.service.impl;

import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tn.esprit.tpautoloc.domain.Client;
import tn.esprit.tpautoloc.domain.Reservation;
import tn.esprit.tpautoloc.domain.Vehicule;
import tn.esprit.tpautoloc.domain.enums.StatutReservation;
import tn.esprit.tpautoloc.exception.ResourceNotFoundException;
import tn.esprit.tpautoloc.repository.IClientRepository;
import tn.esprit.tpautoloc.repository.IReservationRepository;
import tn.esprit.tpautoloc.repository.IVehiculeRepository;
import tn.esprit.tpautoloc.service.IReservationService;
import tn.esprit.tpautoloc.service.observer.ReservationSubject;
import tn.esprit.tpautoloc.web.dto.ReservationRequestDTO;
import tn.esprit.tpautoloc.web.dto.ReservationResponseDTO;
import tn.esprit.tpautoloc.web.mapper.ReservationMapper;

@Service
@RequiredArgsConstructor
@Transactional
public class ReservationServiceImpl implements IReservationService {

    private final IReservationRepository reservationRepository;
    private final IClientRepository clientRepository;
    private final IVehiculeRepository vehiculeRepository;
    private final ReservationMapper reservationMapper;
    private final ReservationSubject reservationSubject;

    @Override
    public ReservationResponseDTO create(ReservationRequestDTO dto) {
        validerDates(dto.getDateDebut(), dto.getDateFin());

        Client client = null;
        if (dto.getClientId() != null) {
            client = clientRepository.findById(dto.getClientId())
                    .orElseThrow(() -> new ResourceNotFoundException("Client", dto.getClientId()));
        }

        Vehicule vehicule = null;
        if (dto.getVehiculeId() != null) {
            vehicule = vehiculeRepository.findById(dto.getVehiculeId())
                    .orElseThrow(() -> new ResourceNotFoundException("Vehicule", dto.getVehiculeId()));
        }

        Reservation reservation = Reservation.builder()
                .client(client)
                .vehicule(vehicule)
                .dateDebut(dto.getDateDebut())
                .dateFin(dto.getDateFin())
                .statut(StatutReservation.EN_ATTENTE)
                .build();

        Reservation saved = reservationRepository.save(reservation);
        return reservationMapper.toDto(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public ReservationResponseDTO findById(Long id) {
        Reservation reservation = reservationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Reservation", id));
        return reservationMapper.toDto(reservation);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ReservationResponseDTO> findAll() {
        return reservationRepository.findAll().stream()
                .map(reservationMapper::toDto)
                .toList();
    }

    @Override
    public ReservationResponseDTO update(Long id, ReservationRequestDTO dto) {
        validerDates(dto.getDateDebut(), dto.getDateFin());
        Reservation existante = reservationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Reservation", id));

        if (dto.getClientId() != null) {
            Client client = clientRepository.findById(dto.getClientId())
                    .orElseThrow(() -> new ResourceNotFoundException("Client", dto.getClientId()));
            existante.setClient(client);
        }

        if (dto.getVehiculeId() != null) {
            Vehicule vehicule = vehiculeRepository.findById(dto.getVehiculeId())
                    .orElseThrow(() -> new ResourceNotFoundException("Vehicule", dto.getVehiculeId()));
            existante.setVehicule(vehicule);
        }

        existante.setDateDebut(dto.getDateDebut());
        existante.setDateFin(dto.getDateFin());

        return reservationMapper.toDto(reservationRepository.save(existante));
    }

    @Override
    public void deleteById(Long id) {
        if (!reservationRepository.existsById(id)) {
            throw new ResourceNotFoundException("Reservation", id);
        }
        reservationRepository.deleteById(id);
    }

    @Override
    public ReservationResponseDTO annulerReservation(Long id) {
        Reservation existante = reservationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Reservation", id));
        existante.setStatut(StatutReservation.ANNULEE);
        reservationSubject.notifierAnnulation(existante);
        return reservationMapper.toDto(reservationRepository.save(existante));
    }

    /**
     * Crée une réservation et notifie tous les observateurs (compatibilité)
     */
    @Override
    public Reservation creerReservation(Client client) {
        Reservation reservation = Reservation.builder()
                .client(client)
                .dateDebut(LocalDate.now())
                .statut(StatutReservation.CONFIRMEE)
                .build();
        reservationSubject.notifierConfirmation(reservation);
        return reservation;
    }

    /**
     * Annule une réservation existante et notifie tous les observateurs (compatibilité)
     */
    @Override
    public Reservation annulerReservation(Reservation reservation) {
        reservation.setStatut(StatutReservation.ANNULEE);
        reservationSubject.notifierAnnulation(reservation);
        return reservation;
    }

    // ---- règle de gestion ----
    private void validerDates(LocalDate dateDebut, LocalDate dateFin) {
        if (dateDebut == null) {
            throw new IllegalArgumentException(
                    "La date de début de la réservation est obligatoire.");
        }
        if (dateFin == null) {
            throw new IllegalArgumentException(
                    "La date de fin de la réservation est obligatoire.");
        }
        if (!dateFin.isAfter(dateDebut)) {
            throw new IllegalArgumentException(
                    "La date de fin de la réservation doit être postérieure à la date de début.");
        }
    }
}
