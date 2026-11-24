package tn.esprit.tpautoloc;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import tn.esprit.tpautoloc.domain.*;
import tn.esprit.tpautoloc.domain.enums.*;
import tn.esprit.tpautoloc.service.*;
import tn.esprit.tpautoloc.web.dto.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class TpAutolocApplicationTests {

    private static final Logger log = LoggerFactory.getLogger(TpAutolocApplicationTests.class);

    // =========================================================
    // Partie 2 — IAgenceService
    // =========================================================
    @Autowired
    private IAgenceService agenceService;

    // =========================================================
    // Partie 3 — IClientService
    // =========================================================
    @Autowired
    private IClientService clientService;

    // =========================================================
    // Partie 4 — IVehiculeService
    // =========================================================
    @Autowired
    private IVehiculeService vehiculeService;

    // =========================================================
    // Partie 5 — Exercice autonome (3 services au choix)
    // =========================================================
    @Autowired
    private IEmployeService employeService;

    @Autowired
    private IMaintenanceService maintenanceService;

    @Autowired
    private IEquipementService equipementService;

    // ---------------------------------------------------------
    @Test
    void contextLoads() {
        assertNotNull(agenceService, "Le contexte Spring doit injecter agenceService.");
    }

    // =========================================================
    // Partie 2 — testAgenceService
    // =========================================================
    @Test
    void testAgenceService() {
        log.info("\n========== TEST AGENCE SERVICE ==========");

        // --- Création d'une agence ---
        AgenceDTO agence = new AgenceDTO();
        agence.setNom("Agence Tunis Centre");
        agence.setVille("Tunis");
        agence.setAdresse("12 Avenue Habib Bourguiba");
        agence.setTelephone("71123456");

        // --- Enregistrement ---
        AgenceDTO agenceSauvegardee = agenceService.create(agence);
        log.info("Agence enregistrée : {}", agenceSauvegardee);
        log.info("  ID  : {}", agenceSauvegardee.getId());
        log.info("  Nom : {}", agenceSauvegardee.getNom());
        assertNotNull(agenceSauvegardee.getId(),
                "L'ID généré ne doit pas être null après persistance.");

        // --- findAll() ---
        List<AgenceDTO> toutesLesAgences = agenceService.findAll();
        log.info("\nListe de toutes les agences ({}) :", toutesLesAgences.size());
        toutesLesAgences.forEach(a -> log.info("  - [{}] {}", a.getId(), a.getNom()));
        assertFalse(toutesLesAgences.isEmpty(), "La liste des agences ne doit pas être vide.");

        // --- findById() ---
        AgenceDTO agenceTrouvee = agenceService.findById(agenceSauvegardee.getId());
        log.info("\nAgence trouvée par ID {} : {}", agenceSauvegardee.getId(), agenceTrouvee.getNom());
        assertNotNull(agenceTrouvee, "L'agence recherchée par ID ne doit pas être null.");
        assertEquals(agenceSauvegardee.getId(), agenceTrouvee.getId());

        // --- count() (via findAll().size()) ---
        long nombreAgences = agenceService.findAll().size();
        log.info("\nNombre d'agences en base : {}", nombreAgences);
        assertTrue(nombreAgences > 0, "Il doit y avoir au moins une agence en base.");

        // --- deleteById() + vérification ---
        agenceService.deleteById(agenceSauvegardee.getId());
        log.info("\nAgence supprimée (ID {}).", agenceSauvegardee.getId());

        // Vérification : findById doit lever ResourceNotFoundException
        assertThrows(
                Exception.class,
                () -> agenceService.findById(agenceSauvegardee.getId()),
                "findById après suppression doit lever une exception."
        );
        log.info("Vérification OK : l'agence n'existe plus en base.");
        log.info("=========================================\n");
    }

    // =========================================================
    // Partie 3 — testClientService
    // =========================================================
    @Test
    void testClientService() {
        log.info("\n========== TEST CLIENT SERVICE ==========");

        // --- Création de deux clients ---
        ClientDTO client1 = new ClientDTO();
        client1.setNom("Ben Salah");
        client1.setPrenom("Amira");
        client1.setEmail("amira.bensalah@email.com");
        client1.setTelephone("98111222");
        client1.setNumPermis("TN123456");
        client1.setDateInscription(LocalDate.now(ZoneId.systemDefault()));

        ClientDTO client2 = new ClientDTO();
        client2.setNom("Trabelsi");
        client2.setPrenom("Yassine");
        client2.setEmail("yassine.trabelsi@email.com");
        client2.setTelephone("98333444");
        client2.setNumPermis("TN789012");
        client2.setDateInscription(LocalDate.now(ZoneId.systemDefault()));

        // --- Enregistrement des deux clients ---
        ClientDTO c1Sauvegarde = clientService.create(client1);
        ClientDTO c2Sauvegarde = clientService.create(client2);
        log.info("Client 1 enregistré : [{}] {} {}", c1Sauvegarde.getId(), c1Sauvegarde.getNom(), c1Sauvegarde.getPrenom());
        log.info("Client 2 enregistré : [{}] {} {}", c2Sauvegarde.getId(), c2Sauvegarde.getNom(), c2Sauvegarde.getPrenom());
        assertNotNull(c1Sauvegarde.getId(), "L'ID du client 1 ne doit pas être null.");
        assertNotNull(c2Sauvegarde.getId(), "L'ID du client 2 ne doit pas être null.");
        assertNotEquals(c1Sauvegarde.getId(), c2Sauvegarde.getId(),
                "Les deux clients doivent avoir des IDs différents.");

        // --- findAll() ---
        List<ClientDTO> tousLesClients = clientService.findAll();
        log.info("\nListe de tous les clients ({}) :", tousLesClients.size());
        tousLesClients.forEach(c -> log.info("  - [{}] {} {} | email: {}", c.getId(), c.getNom(), c.getPrenom(), c.getEmail()));
        assertTrue(tousLesClients.size() >= 2,
                "Il doit y avoir au moins 2 clients après l'enregistrement.");

        // --- findById() ---
        ClientDTO clientTrouve = clientService.findById(c1Sauvegarde.getId());
        log.info("\nClient trouvé par ID {} : {} {}", c1Sauvegarde.getId(), clientTrouve.getNom(), clientTrouve.getPrenom());
        assertNotNull(clientTrouve, "Le client recherché par ID ne doit pas être null.");
        assertEquals("Ben Salah", clientTrouve.getNom());

        // --- count() (via findAll().size()) ---
        long nombreClients = clientService.findAll().size();
        log.info("\nNombre de clients en base : {}", nombreClients);
        assertTrue(nombreClients >= 2);

        // --- deleteById() client 1 + vérification ---
        clientService.deleteById(c1Sauvegarde.getId());
        log.info("\nClient 1 supprimé (ID {}).", c1Sauvegarde.getId());
        assertThrows(
                Exception.class,
                () -> clientService.findById(c1Sauvegarde.getId()),
                "findById après suppression doit lever une exception."
        );
        log.info("Vérification OK : le client 1 n'existe plus en base.");

        // Nettoyage client 2
        clientService.deleteById(c2Sauvegarde.getId());
        log.info("Client 2 supprimé (nettoyage).");
        log.info("=========================================\n");
    }

    // =========================================================
    // Partie 4 — testVehiculeService
    // =========================================================
    @Test
    void testVehiculeService() {
        log.info("\n========== TEST VEHICULE SERVICE ==========");

        // --- Création d'un véhicule ---
        VehiculeDTO vehicule = new VehiculeDTO();
        vehicule.setImmatriculation("123 TUN 4567");
        vehicule.setMarque("Renault");
        vehicule.setModele("Clio");
        vehicule.setCategorie(CategorieVehicule.CITADINE);
        vehicule.setTarifJournalier(new BigDecimal("80.00"));
        vehicule.setStatut(StatutVehicule.DISPONIBLE);

        // --- Enregistrement ---
        VehiculeDTO vehiculeSauvegarde = vehiculeService.create(vehicule);
        log.info("Véhicule enregistré : [{}] {} {} | tarif : {} TND/j",
                vehiculeSauvegarde.getId(), vehiculeSauvegarde.getMarque(),
                vehiculeSauvegarde.getModele(), vehiculeSauvegarde.getTarifJournalier());
        assertNotNull(vehiculeSauvegarde.getId(),
                "L'ID du véhicule ne doit pas être null.");
        assertEquals(new BigDecimal("80.00"), vehiculeSauvegarde.getTarifJournalier());

        // --- findAll() ---
        List<VehiculeDTO> tousLesVehicules = vehiculeService.findAll();
        log.info("\nListe de tous les véhicules ({}) :", tousLesVehicules.size());
        tousLesVehicules.forEach(v -> log.info("  - [{}] {} {} | statut: {}",
                v.getId(), v.getMarque(), v.getModele(), v.getStatut()));
        assertFalse(tousLesVehicules.isEmpty());

        // --- findById() ---
        VehiculeDTO vehiculeTrouve = vehiculeService.findById(vehiculeSauvegarde.getId());
        log.info("\nVéhicule trouvé par ID {} : {} {}", vehiculeSauvegarde.getId(), vehiculeTrouve.getMarque(), vehiculeTrouve.getModele());
        assertNotNull(vehiculeTrouve);
        assertEquals("Renault", vehiculeTrouve.getMarque());

        // --- count() (via findAll().size()) ---
        long nombreVehicules = vehiculeService.findAll().size();
        log.info("\nNombre de véhicules en base : {}", nombreVehicules);
        assertTrue(nombreVehicules > 0);

        // --- deleteById() + vérification ---
        vehiculeService.deleteById(vehiculeSauvegarde.getId());
        log.info("\nVéhicule supprimé (ID {}).", vehiculeSauvegarde.getId());
        assertThrows(
                Exception.class,
                () -> vehiculeService.findById(vehiculeSauvegarde.getId()),
                "findById après suppression doit lever une exception."
        );
        log.info("Vérification OK : le véhicule n'existe plus en base.");
        log.info("==========================================\n");
    }

    // =========================================================
    // Partie 5 — Exercice autonome : EmployeService
    // =========================================================
    @Test
    void testEmployeService() {
        log.info("\n========== TEST EMPLOYE SERVICE ==========");

        // --- Création ---
        EmployeDTO employe = new EmployeDTO();
        employe.setNom("Khemiri");
        employe.setPrenom("Sami");
        employe.setRole(RoleEmploye.AGENT);

        // save()
        EmployeDTO employeSauvegarde = employeService.create(employe);
        log.info("Employé enregistré : [{}] {} {} | rôle : {}",
                employeSauvegarde.getId(), employeSauvegarde.getNom(),
                employeSauvegarde.getPrenom(), employeSauvegarde.getRole());
        assertNotNull(employeSauvegarde.getId());

        // findAll()
        List<EmployeDTO> tousLesEmployes = employeService.findAll();
        log.info("\nListe des employés ({}) :", tousLesEmployes.size());
        tousLesEmployes.forEach(e -> log.info("  - [{}] {} | {}", e.getId(), e.getNom(), e.getRole()));
        assertFalse(tousLesEmployes.isEmpty());

        // findById()
        EmployeDTO employeTrouve = employeService.findById(employeSauvegarde.getId());
        assertNotNull(employeTrouve);
        assertEquals("Khemiri", employeTrouve.getNom());
        log.info("\nEmployé trouvé par ID : {}", employeTrouve.getNom());

        // existsById() vérifié sans exception
        EmployeDTO existant = employeService.findById(employeSauvegarde.getId());
        assertNotNull(existant, "L'employé doit exister avant suppression.");
        log.info("L'employé existe avant suppression : true");

        // deleteById()
        employeService.deleteById(employeSauvegarde.getId());
        log.info("\nEmployé supprimé (ID {}).", employeSauvegarde.getId());
        assertThrows(Exception.class,
                () -> employeService.findById(employeSauvegarde.getId()));
        log.info("Vérification OK : l'employé n'existe plus.");
        log.info("==========================================\n");
    }

    // =========================================================
    // Partie 5 — Exercice autonome : MaintenanceService
    // =========================================================
    @Test
    void testMaintenanceService() {
        log.info("\n========== TEST MAINTENANCE SERVICE ==========");

        // --- Création ---
        Maintenance maintenance = new Maintenance();
        maintenance.setDateDebut(LocalDate.now(ZoneId.systemDefault()));
        maintenance.setDateFin(LocalDate.now(ZoneId.systemDefault()).plusDays(3));
        maintenance.setDescription("Vidange et contrôle freins");

        // save()
        Maintenance maintenanceSauvegardee = maintenanceService.create(maintenance);
        log.info("Maintenance enregistrée : [{}] {} | du {} au {}",
                maintenanceSauvegardee.getIdMaintenance(), maintenanceSauvegardee.getDescription(),
                maintenanceSauvegardee.getDateDebut(), maintenanceSauvegardee.getDateFin());
        assertNotNull(maintenanceSauvegardee.getIdMaintenance());

        // findAll()
        List<Maintenance> toutesLesMaintenances = maintenanceService.findAll();
        log.info("\nListe des maintenances ({}) :", toutesLesMaintenances.size());
        toutesLesMaintenances.forEach(m -> log.info("  - [{}] {}", m.getIdMaintenance(), m.getDescription()));
        assertFalse(toutesLesMaintenances.isEmpty());

        // findById()
        Maintenance maintenanceTrouvee = maintenanceService.findById(maintenanceSauvegardee.getIdMaintenance());
        assertNotNull(maintenanceTrouvee);
        assertEquals("Vidange et contrôle freins", maintenanceTrouvee.getDescription());
        log.info("\nMaintenance trouvée par ID : {}", maintenanceTrouvee.getDescription());

        // existsById() vérifié sans exception
        Maintenance existante = maintenanceService.findById(maintenanceSauvegardee.getIdMaintenance());
        assertNotNull(existante, "La maintenance doit exister avant suppression.");
        log.info("La maintenance existe avant suppression : true");

        // deleteById()
        maintenanceService.deleteById(maintenanceSauvegardee.getIdMaintenance());
        log.info("\nMaintenance supprimée (ID {}).", maintenanceSauvegardee.getIdMaintenance());
        assertThrows(Exception.class,
                () -> maintenanceService.findById(maintenanceSauvegardee.getIdMaintenance()));
        log.info("Vérification OK : la maintenance n'existe plus.");
        log.info("==============================================\n");
    }

    // =========================================================
    // Partie 5 — Exercice autonome : EquipementService
    // =========================================================
    @Test
    void testEquipementService() {
        log.info("\n========== TEST EQUIPEMENT SERVICE ==========");

        // --- Création ---
        Equipement equipement = new Equipement();
        equipement.setLibelle("GPS Navigation");

        // save()
        Equipement equipementSauvegarde = equipementService.create(equipement);
        log.info("Équipement enregistré : [{}] {}", equipementSauvegarde.getIdEquipement(), equipementSauvegarde.getLibelle());
        assertNotNull(equipementSauvegarde.getIdEquipement());
        assertEquals("GPS Navigation", equipementSauvegarde.getLibelle());

        // findAll()
        List<Equipement> tousLesEquipements = equipementService.findAll();
        log.info("\nListe des équipements ({}) :", tousLesEquipements.size());
        tousLesEquipements.forEach(e -> log.info("  - [{}] {}", e.getIdEquipement(), e.getLibelle()));
        assertFalse(tousLesEquipements.isEmpty());

        // findById()
        Equipement equipementTrouve = equipementService.findById(equipementSauvegarde.getIdEquipement());
        assertNotNull(equipementTrouve);
        assertEquals("GPS Navigation", equipementTrouve.getLibelle());
        log.info("\nÉquipement trouvé par ID : {}", equipementTrouve.getLibelle());

        // existsById() vérifié sans exception
        Equipement existant = equipementService.findById(equipementSauvegarde.getIdEquipement());
        assertNotNull(existant, "L'équipement doit exister avant suppression.");
        log.info("L'équipement existe avant suppression : true");

        // deleteById()
        equipementService.deleteById(equipementSauvegarde.getIdEquipement());
        log.info("\nÉquipement supprimé (ID {}).", equipementSauvegarde.getIdEquipement());
        assertThrows(Exception.class,
                () -> equipementService.findById(equipementSauvegarde.getIdEquipement()));
        log.info("Vérification OK : l'équipement n'existe plus.");
        log.info("==============================================\n");
    }

    // Test des observateurs de création et annulation de réservations

    @Autowired
    private IReservationService reservationService;

    @Test
    void testReservationService() {

        Client client = Client.builder()
                .idClient(31L)
                .nom("Dupont")
                .email("jean.dupont@gmail.com")
                .telephone("0612345678")
                .build();

        Reservation reservation = reservationService.creerReservation(client);
        assertNotNull(reservation, "La réservation créée ne doit pas être null.");
        assertEquals(StatutReservation.CONFIRMEE, reservation.getStatut());
        log.info("Réservation créée pour le test des observateurs : ID {}", reservation.getId());

        // 2. Annulation -> déclenche les observateurs d'annulation
        Reservation reservationAnnulee = reservationService.annulerReservation(reservation);
        assertNotNull(reservationAnnulee, "La réservation annulée ne doit pas être null.");
        assertEquals(StatutReservation.ANNULEE, reservationAnnulee.getStatut());
        log.info("Réservation annulée avec succès.");
    }

}
