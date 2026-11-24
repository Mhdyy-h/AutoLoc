package tn.esprit.tpautoloc.web.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tn.esprit.tpautoloc.service.IAgenceService;
import tn.esprit.tpautoloc.web.dto.AgenceDTO;

import java.util.List;

@Tag(name = "Agences", description = "Gestion des agences de location")
@RestController
@RequestMapping("/agences")
@RequiredArgsConstructor
public class AgenceController {

    private final IAgenceService agenceService;

    @Operation(summary = "Lister toutes les agences")
    @ApiResponse(responseCode = "200", description = "Liste des agences récupérée avec succès")
    @GetMapping
    public ResponseEntity<List<AgenceDTO>> findAll() {
        return ResponseEntity.ok(agenceService.findAll());
    }

    @Operation(summary = "Consulter une agence")
    @ApiResponse(responseCode = "200", description = "Agence trouvée")
    @ApiResponse(responseCode = "404", description = "Agence introuvable")
    @GetMapping("/{id}")
    public ResponseEntity<AgenceDTO> findById(@Parameter(description = "Identifiant de l'agence") @PathVariable Long id) {
        return ResponseEntity.ok(agenceService.findById(id));
    }

    @Operation(summary = "Créer une agence")
    @ApiResponse(responseCode = "201", description = "Agence créée avec succès")
    @ApiResponse(responseCode = "400", description = "Nom obligatoire manquant ou vide")
    @PostMapping
    public ResponseEntity<AgenceDTO> creer(@jakarta.validation.Valid @RequestBody AgenceDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(agenceService.create(dto));
    }

    @Operation(summary = "Modifier une agence")
    @ApiResponse(responseCode = "200", description = "Agence modifiée avec succès")
    @ApiResponse(responseCode = "400", description = "Données invalides")
    @ApiResponse(responseCode = "404", description = "Agence introuvable")
    @PutMapping("/{id}")
    public ResponseEntity<AgenceDTO> update(
            @Parameter(description = "Identifiant de l'agence") @PathVariable Long id,
            @jakarta.validation.Valid @RequestBody AgenceDTO dto) {
        return ResponseEntity.ok(agenceService.update(id, dto));
    }

    @Operation(summary = "Supprimer une agence")
    @ApiResponse(responseCode = "204", description = "Agence supprimée avec succès")
    @ApiResponse(responseCode = "404", description = "Agence introuvable")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@Parameter(description = "Identifiant de l'agence") @PathVariable Long id) {
        agenceService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
