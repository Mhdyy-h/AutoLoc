package tn.esprit.tpautoloc.service.impl;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tn.esprit.tpautoloc.domain.Paiement;
import tn.esprit.tpautoloc.exception.ResourceNotFoundException;
import tn.esprit.tpautoloc.repository.IPaiementRepository;
import tn.esprit.tpautoloc.service.IPaiementService;
import tn.esprit.tpautoloc.web.dto.PaiementDTO;
import tn.esprit.tpautoloc.web.mapper.PaiementMapper;

/**
 * Service Paiement en lecture seule.
 * La création, la modification et la suppression d'un paiement passent
 * exclusivement par le Contrat (cascade CascadeType.ALL).
 */
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PaiementServiceImpl implements IPaiementService {

    private final IPaiementRepository paiementRepository;
    private final PaiementMapper paiementMapper;

    @Override
    public PaiementDTO findById(Long id) {
        Paiement paiement = paiementRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Paiement", id));
        return paiementMapper.toDto(paiement);
    }

    @Override
    public List<PaiementDTO> findAll() {
        return paiementRepository.findAll().stream()
                .map(paiementMapper::toDto)
                .toList();
    }
}
