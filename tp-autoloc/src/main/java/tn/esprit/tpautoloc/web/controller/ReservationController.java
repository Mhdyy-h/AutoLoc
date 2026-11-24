package tn.esprit.tpautoloc.web.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import tn.esprit.tpautoloc.service.IReservationService;
import tn.esprit.tpautoloc.web.dto.ReservationRequestDTO;
import tn.esprit.tpautoloc.web.dto.ReservationResponseDTO;
import tn.esprit.tpautoloc.web.validator.ReservationRequestValidator;

import java.util.List;

@Tag(name = "Reservations", description = "Gestion des réservations de véhicules")
@RestController
@RequestMapping("/reservations")
@RequiredArgsConstructor
public class ReservationController {

    private final IReservationService reservationService;
    private final ReservationRequestValidator reservationRequestValidator;

    @Operation(summary = "Lister toutes les réservations")
    @ApiResponse(responseCode = "200", description = "Liste des réservations récupérée avec succès")
    @GetMapping
    public ResponseEntity<List<ReservationResponseDTO>> findAll() {
        return ResponseEntity.ok(reservationService.findAll());
    }

    @Operation(summary = "Consulter une réservation")
    @ApiResponse(responseCode = "200", description = "Réservation trouvée")
    @ApiResponse(responseCode = "404", description = "Réservation introuvable")
    @GetMapping("/{id}")
    public ResponseEntity<ReservationResponseDTO> findById(@Parameter(description = "Identifiant de la réservation") @PathVariable Long id) {
        return ResponseEntity.ok(reservationService.findById(id));
    }

    @Operation(summary = "Créer une réservation")
    @ApiResponse(responseCode = "201", description = "Réservation créée avec succès")
    @ApiResponse(responseCode = "400", description = "Données invalides ou dates incohérentes")
    @PostMapping
    public ResponseEntity<?> create(
            @Valid @RequestBody ReservationRequestDTO dto,
            BindingResult result) {

        reservationRequestValidator.validate(dto, result);

        if (result.hasErrors()) {
            return ResponseEntity.badRequest().body(
                    result.getFieldErrors().stream()
                            .map(error -> error.getField() + " : " + error.getDefaultMessage())
                            .toList()
            );
        }

        return ResponseEntity.status(HttpStatus.CREATED).body(reservationService.create(dto));
    }

    @Operation(summary = "Modifier une réservation")
    @ApiResponse(responseCode = "200", description = "Réservation modifiée avec succès")
    @ApiResponse(responseCode = "400", description = "Données invalides ou dates incohérentes")
    @ApiResponse(responseCode = "404", description = "Réservation introuvable")
    @PutMapping("/{id}")
    public ResponseEntity<?> update(
            @Parameter(description = "Identifiant de la réservation") @PathVariable Long id,
            @Valid @RequestBody ReservationRequestDTO dto,
            BindingResult result) {

        reservationRequestValidator.validate(dto, result);

        if (result.hasErrors()) {
            return ResponseEntity.badRequest().body(
                    result.getFieldErrors().stream()
                            .map(error -> error.getField() + " : " + error.getDefaultMessage())
                            .toList()
            );
        }

        return ResponseEntity.ok(reservationService.update(id, dto));
    }

    @Operation(summary = "Supprimer une réservation")
    @ApiResponse(responseCode = "204", description = "Réservation supprimée avec succès")
    @ApiResponse(responseCode = "404", description = "Réservation introuvable")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@Parameter(description = "Identifiant de la réservation") @PathVariable Long id) {
        reservationService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Annuler une réservation")
    @ApiResponse(responseCode = "200", description = "Réservation annulée avec succès")
    @ApiResponse(responseCode = "404", description = "Réservation introuvable")
    @PutMapping("/{id}/annuler")
    public ResponseEntity<ReservationResponseDTO> annulerReservation(@Parameter(description = "Identifiant de la réservation") @PathVariable Long id) {
        return ResponseEntity.ok(reservationService.annulerReservation(id));
    }
}
