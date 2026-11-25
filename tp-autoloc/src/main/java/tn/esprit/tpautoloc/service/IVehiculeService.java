package tn.esprit.tpautoloc.service;

import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import tn.esprit.tpautoloc.domain.enums.StatutVehicule;
import tn.esprit.tpautoloc.web.dto.VehiculeDTO;

public interface IVehiculeService {
    VehiculeDTO create(VehiculeDTO dto);
    default VehiculeDTO creer(VehiculeDTO dto) {
        return create(dto);
    }

    VehiculeDTO findById(Long id);
    default VehiculeDTO obtenirParId(Long id) {
        return findById(id);
    }

    List<VehiculeDTO> findAll();
    default List<VehiculeDTO> listerTous() {
        return findAll();
    }

    VehiculeDTO update(Long id, VehiculeDTO dto);
    void deleteById(Long id);

    // Partie 1 — Affectation / Désaffectation
    void affecterVehiculeAAgence(Long vehiculeId, Long agenceId);
    void desaffecterVehiculeDeAgence(Long vehiculeId);

    // Atelier 8 — Query Methods
    List<VehiculeDTO> findByAgenceVille(String ville);
    Page<VehiculeDTO> findByStatut(StatutVehicule statut, Pageable pageable);
}
