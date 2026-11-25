package tn.esprit.tpautoloc;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.transaction.annotation.Transactional;
import tn.esprit.tpautoloc.domain.*;
import tn.esprit.tpautoloc.domain.enums.*;
import tn.esprit.tpautoloc.repository.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class Atelier8QueryMethodsTests {

    @Autowired
    private IVehiculeRepository vehiculeRepository;
    @Autowired
    private IClientRepository clientRepository;
    @Autowired
    private IReservationRepository reservationRepository;
    @Autowired
    private IEmployeRepository employeRepository;
    @Autowired
    private IAgenceRepository agenceRepository;
    @Autowired
    private IPaiementRepository paiementRepository;
    @Autowired
    private IContratRepository contratRepository;

    private Agence creerAgence(String nom, String ville) {
        Agence a = new Agence();
        a.setNom(nom);
        a.setVille(ville);
        a.setAdresse("10 Rue Test");
        a.setTelephone("71000000");
        return agenceRepository.save(a);
    }

    private Vehicule creerVehicule(String immat, CategorieVehicule cat, StatutVehicule statut, BigDecimal tarif, Agence agence) {
        Vehicule v = new Vehicule();
        v.setImmatriculation(immat);
        v.setMarque("TestMarque");
        v.setModele("TestModele");
        v.setCategorie(cat);
        v.setStatut(statut);
        v.setTarifJournalier(tarif);
        v.setAgence(agence);
        return vehiculeRepository.save(v);
    }

    @Test
    void exercice1_vehiculeQueryMethods() {
        Agence agence = creerAgence("AgenceA8Test", "VilleA8Tunis");
        Vehicule v1 = creerVehicule("A8-100", CategorieVehicule.BERLINE, StatutVehicule.DISPONIBLE, new BigDecimal("80.00"), agence);
        Vehicule v2 = creerVehicule("A8-101", CategorieVehicule.SUV, StatutVehicule.LOUE, new BigDecimal("150.00"), agence);

        // findByAgenceVille
        assertEquals(2, vehiculeRepository.findByAgenceVille("VilleA8Tunis").size());
        assertTrue(vehiculeRepository.findByAgenceVille("VilleInexistanteA8").isEmpty());

        // findByStatutAndAgence_Id
        assertEquals(1, vehiculeRepository.findByStatutAndAgence_Id(StatutVehicule.DISPONIBLE, agence.getIdAgence()).size());
        assertTrue(vehiculeRepository.findByStatutAndAgence_Id(StatutVehicule.MAINTENANCE, agence.getIdAgence()).isEmpty());

        // findByStatut with Pageable
        Page<Vehicule> page = vehiculeRepository.findByStatut(StatutVehicule.DISPONIBLE, PageRequest.of(0, 5));
        assertFalse(page.isEmpty());
        assertTrue(page.getTotalElements() >= 1);
        assertTrue(page.getContent().size() <= 5);
        assertTrue(page.getContent().stream().allMatch(v -> v.getStatut() == StatutVehicule.DISPONIBLE));

        // findByAgence_VilleAndCategorie
        assertEquals(1, vehiculeRepository.findByAgence_VilleAndCategorie("VilleA8Tunis", CategorieVehicule.BERLINE).size());
        assertTrue(vehiculeRepository.findByAgence_VilleAndCategorie("VilleA8Tunis", CategorieVehicule.UTILITAIRE).isEmpty());

        // existsByIdAndStatut
        assertTrue(vehiculeRepository.existsByIdAndStatut(v1.getIdVehicule(), StatutVehicule.DISPONIBLE));
        assertFalse(vehiculeRepository.existsByIdAndStatut(v1.getIdVehicule(), StatutVehicule.LOUE));

        // countByStatut
        assertTrue(vehiculeRepository.countByStatut(StatutVehicule.DISPONIBLE) >= 1);

        // findByTarifJournalierLessThan & GreaterThan
        assertTrue(vehiculeRepository.findByTarifJournalierLessThan(new BigDecimal("100.00"))
                .stream().anyMatch(v -> v.getIdVehicule().equals(v1.getIdVehicule())));
        assertTrue(vehiculeRepository.findByTarifJournalierGreaterThan(new BigDecimal("120.00"))
                .stream().anyMatch(v -> v.getIdVehicule().equals(v2.getIdVehicule())));

        // findAllByOrderByTarifJournalierAsc
        List<Vehicule> tries = vehiculeRepository.findAllByOrderByTarifJournalierAsc();
        for (int i = 1; i < tries.size(); i++) {
            if (tries.get(i - 1).getTarifJournalier() != null && tries.get(i).getTarifJournalier() != null) {
                assertTrue(tries.get(i - 1).getTarifJournalier().compareTo(tries.get(i).getTarifJournalier()) <= 0);
            }
        }
    }

    @Test
    void exercice2_clientQueryMethods() {
        Client c = new Client();
        c.setNom("BenSalahA8");
        c.setPrenom("Ali");
        c.setEmail("ali.a8@autoloc.tn");
        c.setTelephone("22000111");
        c.setNumPermis("PERM-A8-999");
        c.setDateInscription(LocalDate.now());
        clientRepository.save(c);

        assertTrue(clientRepository.findByEmail("ali.a8@autoloc.tn").isPresent());
        assertTrue(clientRepository.findByEmail("inconnu@autoloc.tn").isEmpty());

        assertTrue(clientRepository.findByNumPermis("PERM-A8-999").isPresent());
        assertTrue(clientRepository.findByNumPermis("PERM-INCONNU").isEmpty());

        assertEquals(1, clientRepository.findByNomContaining("BenSalahA8").size());
        assertTrue(clientRepository.findByNomContaining("NomInexistantA8").isEmpty());

        assertTrue(clientRepository.existsByEmail("ali.a8@autoloc.tn"));
        assertFalse(clientRepository.existsByEmail("inconnu@autoloc.tn"));
    }

    @Test
    void exercice3_reservationQueryMethods() {
        Agence agence = creerAgence("AgenceResaA8", "VilleResaA8");
        Vehicule vehicule = creerVehicule("A8-RESA-1", CategorieVehicule.CITADINE, StatutVehicule.DISPONIBLE, new BigDecimal("60.00"), agence);

        Client client = new Client();
        client.setNom("TrabelsiA8");
        client.setPrenom("Sami");
        client.setEmail("sami.a8@autoloc.tn");
        client.setNumPermis("PERM-A8-RESA");
        client = clientRepository.save(client);

        Reservation r = new Reservation();
        r.setClient(client);
        r.setVehicule(vehicule);
        r.setDateDebut(LocalDate.of(2026, 12, 10));
        r.setDateFin(LocalDate.of(2026, 12, 15));
        r.setStatut(StatutReservation.CONFIRMEE);
        reservationRepository.save(r);

        assertEquals(1, reservationRepository.findByClientIdClient(client.getIdClient()).size());
        assertTrue(reservationRepository.findByClientIdClient(-999L).isEmpty());

        assertEquals(1, reservationRepository.findByVehicule_IdAndStatutNot(vehicule.getIdVehicule(), StatutReservation.ANNULEE).size());
        assertTrue(reservationRepository.findByVehicule_IdAndStatutNot(vehicule.getIdVehicule(), StatutReservation.CONFIRMEE).isEmpty());

        assertTrue(reservationRepository.findByStatut(StatutReservation.CONFIRMEE)
                .stream().anyMatch(res -> res.getIdReservation().equals(r.getIdReservation())));

        assertTrue(reservationRepository.findByDateDebutBetween(LocalDate.of(2026, 12, 1), LocalDate.of(2026, 12, 20))
                .stream().anyMatch(res -> res.getIdReservation().equals(r.getIdReservation())));
        assertTrue(reservationRepository.findByDateDebutBetween(LocalDate.of(2025, 1, 1), LocalDate.of(2025, 1, 5)).isEmpty());

        assertTrue(reservationRepository.findByDateFinAfter(LocalDate.of(2026, 12, 14))
                .stream().anyMatch(res -> res.getIdReservation().equals(r.getIdReservation())));

        assertEquals(1, reservationRepository.findByStatutAndVehiculeAgenceId(StatutReservation.CONFIRMEE, agence.getIdAgence()).size());
        assertTrue(reservationRepository.findByStatutAndVehiculeAgenceId(StatutReservation.ANNULEE, agence.getIdAgence()).isEmpty());

        assertTrue(reservationRepository.existsByVehicule_IdAndStatut(vehicule.getIdVehicule(), StatutReservation.CONFIRMEE));
        assertFalse(reservationRepository.existsByVehicule_IdAndStatut(vehicule.getIdVehicule(), StatutReservation.ANNULEE));
    }

    @Test
    void exercice4_employeQueryMethods() {
        Agence agence = creerAgence("AgenceEmpA8Unique", "VilleEmpA8");

        Employe e = new Employe();
        e.setNom("MansouriA8");
        e.setPrenom("Nadia");
        e.setRole(RoleEmploye.MANAGER);
        e.setAgence(agence);
        employeRepository.save(e);

        assertEquals(1, employeRepository.findByAgenceNom("AgenceEmpA8Unique").size());
        assertTrue(employeRepository.findByAgenceNom("AgenceInexistanteA8").isEmpty());

        assertEquals(1, employeRepository.findByAgence_IdAndRole(agence.getIdAgence(), RoleEmploye.MANAGER).size());
        assertTrue(employeRepository.findByAgence_IdAndRole(agence.getIdAgence(), RoleEmploye.AGENT).isEmpty());

        assertEquals(1, employeRepository.findByNomContaining("MansouriA8").size());
        assertTrue(employeRepository.findByNomContaining("IntrouvableA8").isEmpty());
    }

    @Test
    void exercice5_agenceQueryMethods() {
        creerAgence("AgenceUniqueA8Nom", "VilleUniqueA8");

        Optional<Agence> parVille = agenceRepository.findByVille("VilleUniqueA8");
        assertTrue(parVille.isPresent());
        assertTrue(agenceRepository.findByVille("VilleAbsenteA8").isEmpty());

        Optional<Agence> parNom = agenceRepository.findByNomContaining("UniqueA8Nom");
        assertTrue(parNom.isPresent());
        assertTrue(agenceRepository.findByNomContaining("NomAbsentA8").isEmpty());
    }

    @Test
    void exercice6_paiementQueryMethods() {
        Contrat contrat = new Contrat();
        contrat.setDateSignature(LocalDate.of(2026, 11, 20));
        contrat.setMontantTotal(450.0);
        contrat.setValide(true);
        contrat = contratRepository.save(contrat);

        Paiement p = new Paiement();
        p.setMontant(450.0);
        p.setDatePaiement(LocalDate.of(2026, 11, 22));
        p.setModePaiement(ModePaiement.CARTE);
        p.setContrat(contrat);
        paiementRepository.save(p);

        assertEquals(1, paiementRepository.findByContrat_Id(contrat.getIdContrat()).size());
        assertTrue(paiementRepository.findByContrat_Id(-999L).isEmpty());

        assertTrue(paiementRepository.findByDatePaiementBetween(LocalDate.of(2026, 11, 1), LocalDate.of(2026, 11, 30))
                .stream().anyMatch(pay -> pay.getIdPaiement().equals(p.getIdPaiement())));
        assertTrue(paiementRepository.findByDatePaiementBetween(LocalDate.of(2020, 1, 1), LocalDate.of(2020, 1, 5)).isEmpty());
    }
}
