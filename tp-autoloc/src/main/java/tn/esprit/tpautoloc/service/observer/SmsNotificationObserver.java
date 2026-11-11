package tn.esprit.tpautoloc.service.observer;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import tn.esprit.tpautoloc.domain.Reservation;

/**
 * Observateur concret : notification par SMS (simulee via logs).
 */
@Slf4j
@Component
public class SmsNotificationObserver implements IReservationObserver {
    @Override
    public void onReservationConfirmee(Reservation reservation) {
        log.info("[SMS] Confirmation envoyee au {} pour la reservation #{}",
                reservation.getClient().getTelephone(), reservation.getId());
    }
    @Override
    public void onReservationAnnulee(Reservation reservation) {
        log.info("[SMS] Annulation notifiee au {} pour la reservation #{}",
                reservation.getClient().getTelephone(), reservation.getId());
    }
}