package tn.esprit.tpautoloc.service.impl;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tn.esprit.tpautoloc.domain.Agence;
import tn.esprit.tpautoloc.exception.ResourceNotFoundException;
import tn.esprit.tpautoloc.repository.IAgenceRepository;
import tn.esprit.tpautoloc.service.IAgenceService;
import tn.esprit.tpautoloc.web.dto.AgenceDTO;
import tn.esprit.tpautoloc.web.mapper.AgenceMapper;

@Service
@RequiredArgsConstructor
@Transactional
public class AgenceServiceImpl implements IAgenceService {

    private final IAgenceRepository agenceRepository;
    private final AgenceMapper agenceMapper;

    @Override
    public AgenceDTO create(AgenceDTO dto) {
        Agence agence = agenceMapper.toEntity(dto);
        return agenceMapper.toDto(agenceRepository.save(agence));
    }

    @Override
    @Transactional(readOnly = true)
    public AgenceDTO findById(Long id) {
        Agence agence = agenceRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Agence", id));
        return agenceMapper.toDto(agence);
    }

    @Override
    @Transactional(readOnly = true)
    public List<AgenceDTO> findAll() {
        return agenceRepository.findAll().stream()
                .map(agenceMapper::toDto)
                .toList();
    }

    @Override
    public AgenceDTO update(Long id, AgenceDTO dto) {
        Agence existante = agenceRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Agence", id));
        existante.setNom(dto.getNom());
        existante.setVille(dto.getVille());
        existante.setAdresse(dto.getAdresse());
        existante.setTelephone(dto.getTelephone());
        return agenceMapper.toDto(agenceRepository.save(existante));
    }

    @Override
    public void deleteById(Long id) {
        if (!agenceRepository.existsById(id)) {
            throw new ResourceNotFoundException("Agence", id);
        }
        agenceRepository.deleteById(id);
    }
}
