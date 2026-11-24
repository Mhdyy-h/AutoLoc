package tn.esprit.tpautoloc.service;

import java.util.List;
import tn.esprit.tpautoloc.web.dto.ClientDTO;

public interface IClientService {
    ClientDTO create(ClientDTO dto);
    default ClientDTO creer(ClientDTO dto) {
        return create(dto);
    }

    ClientDTO findById(Long id);
    default ClientDTO obtenirParId(Long id) {
        return findById(id);
    }

    List<ClientDTO> findAll();
    default List<ClientDTO> listerTous() {
        return findAll();
    }

    ClientDTO update(Long id, ClientDTO dto);
    void deleteById(Long id);
}
