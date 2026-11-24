package tn.esprit.tpautoloc.service;

import java.util.List;
import tn.esprit.tpautoloc.web.dto.AgenceDTO;

public interface IAgenceService {
    AgenceDTO create(AgenceDTO dto);
    default AgenceDTO creer(AgenceDTO dto) {
        return create(dto);
    }

    AgenceDTO findById(Long id);
    default AgenceDTO obtenirParId(Long id) {
        return findById(id);
    }

    List<AgenceDTO> findAll();
    default List<AgenceDTO> listerToutes() {
        return findAll();
    }

    AgenceDTO update(Long id, AgenceDTO dto);
    void deleteById(Long id);
}
