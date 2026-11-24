package tn.esprit.tpautoloc.web.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.*;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder


public class AgenceDTO {
    private long id ;
    @NotBlank
    private String nom;
    @NotBlank
    private String ville;
    private String adresse;
    private String telephone;
}
