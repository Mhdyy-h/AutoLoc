package tn.esprit.tpautoloc.service;

import java.util.List;
import tn.esprit.tpautoloc.web.dto.EmployeDTO;

public interface IEmployeService {
    EmployeDTO create(EmployeDTO dto);
    default EmployeDTO creer(EmployeDTO dto) {
        return create(dto);
    }

    EmployeDTO findById(Long id);
    default EmployeDTO obtenirParId(Long id) {
        return findById(id);
    }

    List<EmployeDTO> findAll();
    default List<EmployeDTO> listerTous() {
        return findAll();
    }

    EmployeDTO update(Long id, EmployeDTO dto);
    void deleteById(Long id);

    // Partie 1 — Affectation / Désaffectation
    void affecterEmployeAAgence(Long employeId, Long agenceId);
    void desaffecterEmployeDeAgence(Long employeId);
}
