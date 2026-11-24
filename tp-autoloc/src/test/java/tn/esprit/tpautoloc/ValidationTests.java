package tn.esprit.tpautoloc;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;
import tn.esprit.tpautoloc.domain.enums.CategorieVehicule;
import tn.esprit.tpautoloc.domain.enums.ModePaiement;
import tn.esprit.tpautoloc.web.dto.*;
import tn.esprit.tpautoloc.web.validator.ReservationRequestValidator;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class ValidationTests {

    private static Validator validator;
    private final ReservationRequestValidator reservationValidator = new ReservationRequestValidator();

    @BeforeAll
    static void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    @DisplayName("5.6 — Créer un client avec un email invalide : la requête doit être rejetée")
    void testClientEmailInvalide() {
        ClientDTO client = ClientDTO.builder()
                .nom("Ben Salah")
                .prenom("Amira")
                .email("email-invalide-sans-arobase")
                .build();

        Set<ConstraintViolation<ClientDTO>> violations = validator.validate(client);
        assertFalse(violations.isEmpty(), "La validation doit échouer pour un email invalide");

        boolean emailErrorFound = violations.stream()
                .anyMatch(v -> v.getPropertyPath().toString().equals("email"));
        assertTrue(emailErrorFound, "Il doit y avoir une violation sur le champ 'email'");
    }

    @Test
    @DisplayName("5.6 — Créer un véhicule sans immatriculation : la requête doit être rejetée")
    void testVehiculeSansImmatriculation() {
        VehiculeDTO vehicule = VehiculeDTO.builder()
                .immatriculation("") // vide
                .marque("Renault")
                .modele("Clio")
                .categorie(CategorieVehicule.CITADINE)
                .tarifJournalier(new BigDecimal("80.00"))
                .agenceId(1L)
                .build();

        Set<ConstraintViolation<VehiculeDTO>> violations = validator.validate(vehicule);
        assertFalse(violations.isEmpty(), "La validation doit échouer sans immatriculation");

        boolean immatErrorFound = violations.stream()
                .anyMatch(v -> v.getPropertyPath().toString().equals("immatriculation"));
        assertTrue(immatErrorFound, "Il doit y avoir une violation sur le champ 'immatriculation'");
    }

    @Test
    @DisplayName("5.6 — Créer un paiement avec un montant nul ou négatif : la requête doit être rejetée")
    void testPaiementMontantNegatifOuNul() {
        PaiementDTO paiementNegatif = PaiementDTO.builder()
                .montant(-50.0)
                .modePaiement(ModePaiement.CARTE)
                .contratId(1L)
                .build();

        Set<ConstraintViolation<PaiementDTO>> violationsNegatif = validator.validate(paiementNegatif);
        assertFalse(violationsNegatif.isEmpty(), "Le montant négatif doit être rejeté");

        PaiementDTO paiementZero = PaiementDTO.builder()
                .montant(0.0)
                .modePaiement(ModePaiement.CARTE)
                .contratId(1L)
                .build();

        Set<ConstraintViolation<PaiementDTO>> violationsZero = validator.validate(paiementZero);
        assertFalse(violationsZero.isEmpty(), "Le montant égal à zéro doit être rejeté");
    }

    @Test
    @DisplayName("5.6 — Envoyer une réservation sans date de début ou de fin : la requête doit être rejetée")
    void testReservationSansDates() {
        ReservationRequestDTO dto = ReservationRequestDTO.builder()
                .clientId(1L)
                .vehiculeId(1L)
                .dateDebut(null)
                .dateFin(null)
                .build();

        Set<ConstraintViolation<ReservationRequestDTO>> violations = validator.validate(dto);
        assertFalse(violations.isEmpty(), "La réservation sans dates doit échouer");

        boolean dateDebutNull = violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("dateDebut"));
        boolean dateFinNull = violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("dateFin"));

        assertTrue(dateDebutNull, "Violation sur dateDebut attendue");
        assertTrue(dateFinNull, "Violation sur dateFin attendue");
    }

    @Test
    @DisplayName("5.6 — Envoyer une réservation dont la date de fin est antérieure ou égale à date de début : erreur attendue")
    void testReservationDateFinAnterieureOuEgale() {
        // Cas 1 : dateFin antérieure à dateDebut
        ReservationRequestDTO dtoAnterieure = ReservationRequestDTO.builder()
                .clientId(1L)
                .vehiculeId(1L)
                .dateDebut(LocalDate.now(ZoneId.systemDefault()).plusDays(5))
                .dateFin(LocalDate.now(ZoneId.systemDefault()).plusDays(2))
                .build();

        Errors errorsAnterieure = new BeanPropertyBindingResult(dtoAnterieure, "reservationRequestDTO");
        reservationValidator.validate(dtoAnterieure, errorsAnterieure);
        assertTrue(errorsAnterieure.hasFieldErrors("dateFin"),
                "Le validateur personnalisé doit rejeter une date de fin antérieure");

        // Cas 2 : dateFin égale à dateDebut
        ReservationRequestDTO dtoEgale = ReservationRequestDTO.builder()
                .clientId(1L)
                .vehiculeId(1L)
                .dateDebut(LocalDate.now(ZoneId.systemDefault()).plusDays(3))
                .dateFin(LocalDate.now(ZoneId.systemDefault()).plusDays(3))
                .build();

        Errors errorsEgale = new BeanPropertyBindingResult(dtoEgale, "reservationRequestDTO");
        reservationValidator.validate(dtoEgale, errorsEgale);
        assertTrue(errorsEgale.hasFieldErrors("dateFin"),
                "Le validateur personnalisé doit rejeter une date de fin égale à la date de début");
    }

    @Test
    @DisplayName("5.6 — Envoyer une réservation avec des dates cohérentes : la validation doit réussir")
    void testReservationDatesCoherentes() {
        ReservationRequestDTO dtoValide = ReservationRequestDTO.builder()
                .clientId(1L)
                .vehiculeId(1L)
                .dateDebut(LocalDate.now(ZoneId.systemDefault()).plusDays(1))
                .dateFin(LocalDate.now(ZoneId.systemDefault()).plusDays(5))
                .build();

        // 1. Validation Bean Validation
        Set<ConstraintViolation<ReservationRequestDTO>> violations = validator.validate(dtoValide);
        assertTrue(violations.isEmpty(), "Bean Validation ne doit trouver aucune erreur");

        // 2. Validation personnalisée avec ReservationRequestValidator
        Errors errors = new BeanPropertyBindingResult(dtoValide, "reservationRequestDTO");
        reservationValidator.validate(dtoValide, errors);
        assertFalse(errors.hasErrors(), "ReservationRequestValidator ne doit trouver aucune erreur pour des dates cohérentes");
    }
}
