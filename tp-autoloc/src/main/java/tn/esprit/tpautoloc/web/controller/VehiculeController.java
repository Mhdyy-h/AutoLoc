package tn.esprit.tpautoloc.web.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tn.esprit.tpautoloc.service.IVehiculeService;
import tn.esprit.tpautoloc.web.dto.VehiculeDTO;

import java.util.List;

@Tag(name = "Vehicules", description = "Gestion du parc de véhicules")
@RestController
@RequestMapping("/vehicules")
@RequiredArgsConstructor
public class VehiculeController {

    private final IVehiculeService vehiculeService;

    @Operation(summary = "Lister tous les véhicules")
    @ApiResponse(responseCode = "200", description = "Liste des véhicules récupérée avec succès")
    @GetMapping
    public ResponseEntity<List<VehiculeDTO>> findAll() {
        return ResponseEntity.ok(vehiculeService.findAll());
    }

    @Operation(summary = "Consulter un véhicule")
    @ApiResponse(responseCode = "200", description = "Véhicule trouvé")
    @ApiResponse(responseCode = "404", description = "Véhicule introuvable")
    @GetMapping("/{id}")
    public ResponseEntity<VehiculeDTO> findById(@Parameter(description = "Identifiant du véhicule") @PathVariable Long id) {
        return ResponseEntity.ok(vehiculeService.findById(id));
    }

    @Operation(summary = "Créer un véhicule")
    @ApiResponse(responseCode = "201", description = "Véhicule créé avec succès")
    @ApiResponse(responseCode = "400", description = "Valeur numérique invalide ou tarif négatif")
    @PostMapping
    public ResponseEntity<VehiculeDTO> create(@jakarta.validation.Valid @RequestBody VehiculeDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(vehiculeService.create(dto));
    }

    @Operation(summary = "Modifier un véhicule")
    @ApiResponse(responseCode = "200", description = "Véhicule modifié avec succès")
    @ApiResponse(responseCode = "400", description = "Données invalides ou tarif négatif")
    @ApiResponse(responseCode = "404", description = "Véhicule introuvable")
    @PutMapping("/{id}")
    public ResponseEntity<VehiculeDTO> update(
            @Parameter(description = "Identifiant du véhicule") @PathVariable Long id,
            @jakarta.validation.Valid @RequestBody VehiculeDTO dto) {
        return ResponseEntity.ok(vehiculeService.update(id, dto));
    }

    @Operation(summary = "Supprimer un véhicule")
    @ApiResponse(responseCode = "204", description = "Véhicule supprimé avec succès")
    @ApiResponse(responseCode = "404", description = "Véhicule introuvable")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@Parameter(description = "Identifiant du véhicule") @PathVariable Long id) {
        vehiculeService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    // ---- Partie 1 : Affectation / Désaffectation ----

    @Operation(summary = "Affecter un véhicule à une agence")
    @ApiResponse(responseCode = "204", description = "Véhicule affecté avec succès")
    @ApiResponse(responseCode = "404", description = "Véhicule ou agence introuvable")
    @PutMapping("/{vehiculeId}/affecter/{agenceId}")
    public ResponseEntity<Void> affecterVehiculeAAgence(
            @Parameter(description = "Identifiant du véhicule") @PathVariable Long vehiculeId,
            @Parameter(description = "Identifiant de l'agence") @PathVariable Long agenceId) {
        vehiculeService.affecterVehiculeAAgence(vehiculeId, agenceId);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Désaffecter un véhicule de son agence")
    @ApiResponse(responseCode = "204", description = "Véhicule désaffecté avec succès")
    @ApiResponse(responseCode = "404", description = "Véhicule introuvable")
    @ApiResponse(responseCode = "400", description = "Véhicule non rattaché à une agence")
    @PutMapping("/{vehiculeId}/desaffecter")
    public ResponseEntity<Void> desaffecterVehiculeDeAgence(
            @Parameter(description = "Identifiant du véhicule") @PathVariable Long vehiculeId) {
        vehiculeService.desaffecterVehiculeDeAgence(vehiculeId);
        return ResponseEntity.noContent().build();
    }
}
