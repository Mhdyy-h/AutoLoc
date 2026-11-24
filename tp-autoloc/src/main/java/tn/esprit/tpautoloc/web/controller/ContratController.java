package tn.esprit.tpautoloc.web.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tn.esprit.tpautoloc.service.IContratService;
import tn.esprit.tpautoloc.web.dto.ContratDTO;

import java.util.List;

@Tag(name = "Contrats", description = "Gestion des contrats de location")
@RestController
@RequestMapping("/contrats")
@RequiredArgsConstructor
public class ContratController {

    private final IContratService contratService;

    @Operation(summary = "Lister tous les contrats")
    @ApiResponse(responseCode = "200", description = "Liste des contrats récupérée avec succès")
    @GetMapping
    public ResponseEntity<List<ContratDTO>> findAll() {
        return ResponseEntity.ok(contratService.findAll());
    }

    @Operation(summary = "Consulter un contrat")
    @ApiResponse(responseCode = "200", description = "Contrat trouvé")
    @ApiResponse(responseCode = "404", description = "Contrat introuvable")
    @GetMapping("/{id}")
    public ResponseEntity<ContratDTO> findById(@Parameter(description = "Identifiant du contrat") @PathVariable Long id) {
        return ResponseEntity.ok(contratService.findById(id));
    }

    @Operation(summary = "Créer un contrat")
    @ApiResponse(responseCode = "201", description = "Contrat créé avec succès")
    @ApiResponse(responseCode = "400", description = "Données invalides ou identifiant fourni")
    @PostMapping
    public ResponseEntity<ContratDTO> create(@jakarta.validation.Valid @RequestBody ContratDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(contratService.create(dto));
    }

    @Operation(summary = "Modifier un contrat")
    @ApiResponse(responseCode = "200", description = "Contrat modifié avec succès")
    @ApiResponse(responseCode = "400", description = "Données invalides")
    @ApiResponse(responseCode = "404", description = "Contrat introuvable")
    @PutMapping("/{id}")
    public ResponseEntity<ContratDTO> update(
            @Parameter(description = "Identifiant du contrat") @PathVariable Long id,
            @jakarta.validation.Valid @RequestBody ContratDTO dto) {
        return ResponseEntity.ok(contratService.update(id, dto));
    }

    @Operation(summary = "Supprimer un contrat")
    @ApiResponse(responseCode = "204", description = "Contrat supprimé avec succès")
    @ApiResponse(responseCode = "404", description = "Contrat introuvable")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@Parameter(description = "Identifiant du contrat") @PathVariable Long id) {
        contratService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
