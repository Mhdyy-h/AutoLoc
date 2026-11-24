package tn.esprit.tpautoloc.service;

import java.util.List;
import tn.esprit.tpautoloc.domain.Client;
import tn.esprit.tpautoloc.domain.Reservation;
import tn.esprit.tpautoloc.web.dto.ReservationRequestDTO;
import tn.esprit.tpautoloc.web.dto.ReservationResponseDTO;

public interface IReservationService {
    ReservationResponseDTO create(ReservationRequestDTO dto);
    default ReservationResponseDTO creer(ReservationRequestDTO dto) {
        return create(dto);
    }

    ReservationResponseDTO findById(Long id);
    default ReservationResponseDTO obtenirParId(Long id) {
        return findById(id);
    }

    List<ReservationResponseDTO> findAll();
    default List<ReservationResponseDTO> listerToutes() {
        return findAll();
    }

    ReservationResponseDTO update(Long id, ReservationRequestDTO dto);
    void deleteById(Long id);

    ReservationResponseDTO annulerReservation(Long id);

    // Compatibilité tests
    Reservation annulerReservation(Reservation reservation);
    Reservation creerReservation(Client client);
}
