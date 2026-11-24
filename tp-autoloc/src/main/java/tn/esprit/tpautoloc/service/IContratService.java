package tn.esprit.tpautoloc.service;

import java.util.List;
import tn.esprit.tpautoloc.web.dto.ContratDTO;

public interface IContratService {
    ContratDTO create(ContratDTO dto);
    default ContratDTO creer(ContratDTO dto) {
        return create(dto);
    }

    ContratDTO findById(Long id);
    default ContratDTO obtenirParId(Long id) {
        return findById(id);
    }

    List<ContratDTO> findAll();
    default List<ContratDTO> listerTous() {
        return findAll();
    }

    ContratDTO update(Long id, ContratDTO dto);
    void deleteById(Long id);
}
