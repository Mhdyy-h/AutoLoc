package tn.esprit.tpautoloc;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import tn.esprit.tpautoloc.domain.*;
import tn.esprit.tpautoloc.domain.enums.*;
import tn.esprit.tpautoloc.service.*;
import tn.esprit.tpautoloc.web.dto.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class TpAutolocApplicationTests {

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
        // Vérifie que le contexte Spring démarre sans erreur
    }

    // =========================================================
    // Partie 2 — testAgenceService
    // =========================================================
    @Test
    void testAgenceService() {
        System.out.println("\n========== TEST AGENCE SERVICE ==========");

        // --- Création d'une agence ---
        AgenceDTO agence = new AgenceDTO();
        agence.setNom("Agence Tunis Centre");
        agence.setVille("Tunis");
        agence.setAdresse("12 Avenue Habib Bourguiba");
        agence.setTelephone("71123456");

        // --- Enregistrement ---
        AgenceDTO agenceSauvegardee = agenceService.create(agence);
        System.out.println("Agence enregistrée : " + agenceSauvegardee);
        System.out.println("  ID  : " + agenceSauvegardee.getId());
        System.out.println("  Nom : " + agenceSauvegardee.getNom());
        assertNotNull(agenceSauvegardee.getId(),
                "L'ID généré ne doit pas être null après persistance.");

        // --- findAll() ---
        List<AgenceDTO> toutesLesAgences = agenceService.findAll();
        System.out.println("\nListe de toutes les agences (" + toutesLesAgences.size() + ") :");
        toutesLesAgences.forEach(a -> System.out.println("  - [" + a.getId() + "] " + a.getNom()));
        assertFalse(toutesLesAgences.isEmpty(), "La liste des agences ne doit pas être vide.");

        // --- findById() ---
        AgenceDTO agenceTrouvee = agenceService.findById(agenceSauvegardee.getId());
        System.out.println("\nAgence trouvée par ID " + agenceSauvegardee.getId()
                + " : " + agenceTrouvee.getNom());
        assertNotNull(agenceTrouvee, "L'agence recherchée par ID ne doit pas être null.");
        assertEquals(agenceSauvegardee.getId(), agenceTrouvee.getId());

        // --- count() (via findAll().size()) ---
        long nombreAgences = agenceService.findAll().size();
        System.out.println("\nNombre d'agences en base : " + nombreAgences);
        assertTrue(nombreAgences > 0, "Il doit y avoir au moins une agence en base.");

        // --- deleteById() + vérification ---
        agenceService.deleteById(agenceSauvegardee.getId());
        System.out.println("\nAgence supprimée (ID " + agenceSauvegardee.getId() + ").");

        // Vérification : findById doit lever ResourceNotFoundException
        assertThrows(
                Exception.class,
                () -> agenceService.findById(agenceSauvegardee.getId()),
                "findById après suppression doit lever une exception."
        );
        System.out.println("Vérification OK : l'agence n'existe plus en base.");
        System.out.println("=========================================\n");
    }

    // =========================================================
    // Partie 3 — testClientService
    // =========================================================
    @Test
    void testClientService() {
        System.out.println("\n========== TEST CLIENT SERVICE ==========");

        // --- Création de deux clients ---
        ClientDTO client1 = new ClientDTO();
        client1.setNom("Ben Salah");
        client1.setPrenom("Amira");
        client1.setEmail("amira.bensalah@email.com");
        client1.setTelephone("98111222");
        client1.setNumPermis("TN123456");
        client1.setDateInscription(LocalDate.now());

        ClientDTO client2 = new ClientDTO();
        client2.setNom("Trabelsi");
        client2.setPrenom("Yassine");
        client2.setEmail("yassine.trabelsi@email.com");
        client2.setTelephone("98333444");
        client2.setNumPermis("TN789012");
        client2.setDateInscription(LocalDate.now());

        // --- Enregistrement des deux clients ---
        ClientDTO c1Sauvegarde = clientService.create(client1);
        ClientDTO c2Sauvegarde = clientService.create(client2);
        System.out.println("Client 1 enregistré : [" + c1Sauvegarde.getId()
                + "] " + c1Sauvegarde.getNom() + " " + c1Sauvegarde.getPrenom());
        System.out.println("Client 2 enregistré : [" + c2Sauvegarde.getId()
                + "] " + c2Sauvegarde.getNom() + " " + c2Sauvegarde.getPrenom());
        assertNotNull(c1Sauvegarde.getId(), "L'ID du client 1 ne doit pas être null.");
        assertNotNull(c2Sauvegarde.getId(), "L'ID du client 2 ne doit pas être null.");
        assertNotEquals(c1Sauvegarde.getId(), c2Sauvegarde.getId(),
                "Les deux clients doivent avoir des IDs différents.");

        // --- findAll() ---
        List<ClientDTO> tousLesClients = clientService.findAll();
        System.out.println("\nListe de tous les clients (" + tousLesClients.size() + ") :");
        tousLesClients.forEach(c -> System.out.println(
                "  - [" + c.getId() + "] " + c.getNom() + " " + c.getPrenom()
                        + " | email: " + c.getEmail()));
        assertTrue(tousLesClients.size() >= 2,
                "Il doit y avoir au moins 2 clients après l'enregistrement.");

        // --- findById() ---
        ClientDTO clientTrouve = clientService.findById(c1Sauvegarde.getId());
        System.out.println("\nClient trouvé par ID " + c1Sauvegarde.getId()
                + " : " + clientTrouve.getNom() + " " + clientTrouve.getPrenom());
        assertNotNull(clientTrouve, "Le client recherché par ID ne doit pas être null.");
        assertEquals("Ben Salah", clientTrouve.getNom());

        // --- count() (via findAll().size()) ---
        long nombreClients = clientService.findAll().size();
        System.out.println("\nNombre de clients en base : " + nombreClients);
        assertTrue(nombreClients >= 2);

        // --- deleteById() client 1 + vérification ---
        clientService.deleteById(c1Sauvegarde.getId());
        System.out.println("\nClient 1 supprimé (ID " + c1Sauvegarde.getId() + ").");
        assertThrows(
                Exception.class,
                () -> clientService.findById(c1Sauvegarde.getId()),
                "findById après suppression doit lever une exception."
        );
        System.out.println("Vérification OK : le client 1 n'existe plus en base.");

        // Nettoyage client 2
        clientService.deleteById(c2Sauvegarde.getId());
        System.out.println("Client 2 supprimé (nettoyage).");
        System.out.println("=========================================\n");
    }

    // =========================================================
    // Partie 4 — testVehiculeService
    // =========================================================
    @Test
    void testVehiculeService() {
        System.out.println("\n========== TEST VEHICULE SERVICE ==========");

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
        System.out.println("Véhicule enregistré : [" + vehiculeSauvegarde.getId()
                + "] " + vehiculeSauvegarde.getMarque() + " " + vehiculeSauvegarde.getModele()
                + " | tarif : " + vehiculeSauvegarde.getTarifJournalier() + " TND/j");
        assertNotNull(vehiculeSauvegarde.getId(),
                "L'ID du véhicule ne doit pas être null.");
        assertEquals(new BigDecimal("80.00"), vehiculeSauvegarde.getTarifJournalier());

        // --- findAll() ---
        List<VehiculeDTO> tousLesVehicules = vehiculeService.findAll();
        System.out.println("\nListe de tous les véhicules (" + tousLesVehicules.size() + ") :");
        tousLesVehicules.forEach(v -> System.out.println(
                "  - [" + v.getId() + "] " + v.getMarque()
                        + " " + v.getModele() + " | statut: " + v.getStatut()));
        assertFalse(tousLesVehicules.isEmpty());

        // --- findById() ---
        VehiculeDTO vehiculeTrouve = vehiculeService.findById(vehiculeSauvegarde.getId());
        System.out.println("\nVéhicule trouvé par ID " + vehiculeSauvegarde.getId()
                + " : " + vehiculeTrouve.getMarque() + " " + vehiculeTrouve.getModele());
        assertNotNull(vehiculeTrouve);
        assertEquals("Renault", vehiculeTrouve.getMarque());

        // --- count() (via findAll().size()) ---
        long nombreVehicules = vehiculeService.findAll().size();
        System.out.println("\nNombre de véhicules en base : " + nombreVehicules);
        assertTrue(nombreVehicules > 0);

        // --- deleteById() + vérification ---
        vehiculeService.deleteById(vehiculeSauvegarde.getId());
        System.out.println("\nVéhicule supprimé (ID " + vehiculeSauvegarde.getId() + ").");
        assertThrows(
                Exception.class,
                () -> vehiculeService.findById(vehiculeSauvegarde.getId()),
                "findById après suppression doit lever une exception."
        );
        System.out.println("Vérification OK : le véhicule n'existe plus en base.");
        System.out.println("==========================================\n");
    }

    // =========================================================
    // Partie 5 — Exercice autonome : EmployeService
    // =========================================================
    @Test
    void testEmployeService() {
        System.out.println("\n========== TEST EMPLOYE SERVICE ==========");

        // --- Création ---
        EmployeDTO employe = new EmployeDTO();
        employe.setNom("Khemiri");
        employe.setPrenom("Sami");
        employe.setRole(RoleEmploye.AGENT);

        // save()
        EmployeDTO employeSauvegarde = employeService.create(employe);
        System.out.println("Employé enregistré : [" + employeSauvegarde.getId()
                + "] " + employeSauvegarde.getNom() + " " + employeSauvegarde.getPrenom()
                + " | rôle : " + employeSauvegarde.getRole());
        assertNotNull(employeSauvegarde.getId());

        // findAll()
        List<EmployeDTO> tousLesEmployes = employeService.findAll();
        System.out.println("\nListe des employés (" + tousLesEmployes.size() + ") :");
        tousLesEmployes.forEach(e -> System.out.println(
                "  - [" + e.getId() + "] " + e.getNom() + " | " + e.getRole()));
        assertFalse(tousLesEmployes.isEmpty());

        // findById()
        EmployeDTO employeTrouve = employeService.findById(employeSauvegarde.getId());
        assertNotNull(employeTrouve);
        assertEquals("Khemiri", employeTrouve.getNom());
        System.out.println("\nEmployé trouvé par ID : " + employeTrouve.getNom());

        // existsById() — simulé via findById sans exception
        boolean existe = true;
        try {
            employeService.findById(employeSauvegarde.getId());
        } catch (Exception ex) {
            existe = false;
        }
        assertTrue(existe, "L'employé doit exister avant suppression.");
        System.out.println("L'employé existe avant suppression : " + existe);

        // deleteById()
        employeService.deleteById(employeSauvegarde.getId());
        System.out.println("\nEmployé supprimé (ID " + employeSauvegarde.getId() + ").");
        assertThrows(Exception.class,
                () -> employeService.findById(employeSauvegarde.getId()));
        System.out.println("Vérification OK : l'employé n'existe plus.");
        System.out.println("==========================================\n");
    }

    // =========================================================
    // Partie 5 — Exercice autonome : MaintenanceService
    // =========================================================
    @Test
    void testMaintenanceService() {
        System.out.println("\n========== TEST MAINTENANCE SERVICE ==========");

        // --- Création ---
        Maintenance maintenance = new Maintenance();
        maintenance.setDateDebut(LocalDate.now());
        maintenance.setDateFin(LocalDate.now().plusDays(3));
        maintenance.setDescription("Vidange et contrôle freins");

        // save()
        Maintenance maintenanceSauvegardee = maintenanceService.create(maintenance);
        System.out.println("Maintenance enregistrée : [" + maintenanceSauvegardee.getIdMaintenance()
                + "] " + maintenanceSauvegardee.getDescription()
                + " | du " + maintenanceSauvegardee.getDateDebut()
                + " au " + maintenanceSauvegardee.getDateFin());
        assertNotNull(maintenanceSauvegardee.getIdMaintenance());

        // findAll()
        List<Maintenance> toutesLesMaintenances = maintenanceService.findAll();
        System.out.println("\nListe des maintenances (" + toutesLesMaintenances.size() + ") :");
        toutesLesMaintenances.forEach(m -> System.out.println(
                "  - [" + m.getIdMaintenance() + "] " + m.getDescription()));
        assertFalse(toutesLesMaintenances.isEmpty());

        // findById()
        Maintenance maintenanceTrouvee = maintenanceService.findById(maintenanceSauvegardee.getIdMaintenance());
        assertNotNull(maintenanceTrouvee);
        assertEquals("Vidange et contrôle freins", maintenanceTrouvee.getDescription());
        System.out.println("\nMaintenance trouvée par ID : " + maintenanceTrouvee.getDescription());

        // existsById() — simulé via findById
        boolean existe = true;
        try {
            maintenanceService.findById(maintenanceSauvegardee.getIdMaintenance());
        } catch (Exception ex) {
            existe = false;
        }
        assertTrue(existe, "La maintenance doit exister avant suppression.");
        System.out.println("La maintenance existe avant suppression : " + existe);

        // deleteById()
        maintenanceService.deleteById(maintenanceSauvegardee.getIdMaintenance());
        System.out.println("\nMaintenance supprimée (ID " + maintenanceSauvegardee.getIdMaintenance() + ").");
        assertThrows(Exception.class,
                () -> maintenanceService.findById(maintenanceSauvegardee.getIdMaintenance()));
        System.out.println("Vérification OK : la maintenance n'existe plus.");
        System.out.println("==============================================\n");
    }

    // =========================================================
    // Partie 5 — Exercice autonome : EquipementService
    // =========================================================
    @Test
    void testEquipementService() {
        System.out.println("\n========== TEST EQUIPEMENT SERVICE ==========");

        // --- Création ---
        Equipement equipement = new Equipement();
        equipement.setLibelle("GPS Navigation");

        // save()
        Equipement equipementSauvegarde = equipementService.create(equipement);
        System.out.println("Équipement enregistré : [" + equipementSauvegarde.getIdEquipement()
                + "] " + equipementSauvegarde.getLibelle());
        assertNotNull(equipementSauvegarde.getIdEquipement());
        assertEquals("GPS Navigation", equipementSauvegarde.getLibelle());

        // findAll()
        List<Equipement> tousLesEquipements = equipementService.findAll();
        System.out.println("\nListe des équipements (" + tousLesEquipements.size() + ") :");
        tousLesEquipements.forEach(e -> System.out.println(
                "  - [" + e.getIdEquipement() + "] " + e.getLibelle()));
        assertFalse(tousLesEquipements.isEmpty());

        // findById()
        Equipement equipementTrouve = equipementService.findById(equipementSauvegarde.getIdEquipement());
        assertNotNull(equipementTrouve);
        assertEquals("GPS Navigation", equipementTrouve.getLibelle());
        System.out.println("\nÉquipement trouvé par ID : " + equipementTrouve.getLibelle());

        // existsById() — simulé via findById
        boolean existe = true;
        try {
            equipementService.findById(equipementSauvegarde.getIdEquipement());
        } catch (Exception ex) {
            existe = false;
        }
        assertTrue(existe, "L'équipement doit exister avant suppression.");
        System.out.println("L'équipement existe avant suppression : " + existe);

        // deleteById()
        equipementService.deleteById(equipementSauvegarde.getIdEquipement());
        System.out.println("\nÉquipement supprimé (ID " + equipementSauvegarde.getIdEquipement() + ").");
        assertThrows(Exception.class,
                () -> equipementService.findById(equipementSauvegarde.getIdEquipement()));
        System.out.println("Vérification OK : l'équipement n'existe plus.");
        System.out.println("==============================================\n");
    }

    // Test des observateurs de création et annulation de réservations

    @Autowired
    private IReservationService reservationService;

    @Test
    void ReservationServiceTest() {

        Client client = Client.builder()
                .idClient(31L)
                .nom("Dupont")
                .email("jean.dupont@gmail.com")
                .telephone("0612345678")
                .build();

        Reservation reservation = reservationService.creerReservation(client);

        System.out.println("........");

        // 2. Annulation -> déclenche les observateurs d'annulation
        reservationService.annulerReservation(reservation);
    }

}
