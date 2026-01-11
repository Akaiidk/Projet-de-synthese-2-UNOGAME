# UNOGAME - Jeu de UNO Multijoueur en JavaFX

UNOGAME est une adaptation numérique du célèbre jeu de cartes **UNO**, développée en Java. Ce projet se distingue par son architecture **Client-Serveur**, permettant à plusieurs joueurs de s'affronter en temps réel via un réseau.

## 🚀 Présentation
L'objectif de ce projet est de recréer l'expérience sociale du UNO. Contrairement aux versions classiques contre ordinateur, UNOGAME mise tout sur l'interaction humaine grâce à un système de jeu en réseau. Le projet gère l'intégralité du cycle de vie d'une partie, de la synchronisation des joueurs à l'application stricte des règles du jeu.

## 🌐 Architecture Réseau (Client-Serveur)
Le cœur du projet repose sur une communication bidirectionnelle :

* **Le Serveur (Server Side)** : 
    * Gère les connexions entrantes (Sockets).
    * Héberge la logique de la partie et arbitre les coups.
    * Synchronise l'état du jeu pour tous les clients connectés.
    * Distribue les cartes et gère la file d'attente des tours.
* **Le Client (Client Side)** : 
    * Fournit l'interface graphique (JavaFX).
    * Envoie les actions du joueur au serveur.
    * Reçoit et affiche les mises à jour en temps réel (cartes jouées par les adversaires, pioche, etc.).



## 🛠️ Caractéristiques Techniques

### 1. Interface Graphique JavaFX
Une interface réactive conçue pour le jeu en ligne :
* **Lobby de connexion** : Permet de rejoindre le serveur en renseignant son pseudonyme.
* **Table de jeu dynamique** : Visualisation de sa main, du sens de jeu (horaire/anti-horaire) et du joueur actuel.
* **Notifications réseau** : Alertes visuelles quand un joueur doit piocher ou changer la couleur (Joker).

### 2. Logique et Règles du Jeu
* **Validation côté serveur** : Pour éviter toute triche, c'est le serveur qui valide si une carte peut être posée.
* **Cartes Spéciales** : Implémentation complète des effets (Inversion, Saut de tour, +2 et +4).
* **Gestion du Flux** : Algorithme gérant le sens de rotation et l'attribution des malus aux joueurs suivants.



### 3. Synchronisation et Sockets
Le projet utilise les Sockets Java pour une transmission de données rapide :
* **Protocoles de communication** : Envoi de messages structurés pour définir les actions.
* **Multi-threading** : Le serveur utilise des threads pour gérer chaque client simultanément sans bloquer la partie.

## 📸 Aperçu
<img width="999" height="597" alt="Capture d’écran 2026-01-11 à 03 26 47" src="https://github.com/user-attachments/assets/0e693929-a8ed-493a-b648-c34546cf0d93" />
<img width="999" height="595" alt="Capture d’écran 2026-01-11 à 03 31 38" src="https://github.com/user-attachments/assets/60f8c7be-d411-4acd-8c21-abbebeae9eb3" />
<img width="1002" height="599" alt="Capture d’écran 2026-01-11 à 03 30 48" src="https://github.com/user-attachments/assets/a17dbdd4-0aa7-4cfe-bbaf-279b80535b18" />

## 📁 Structure du Projet
* `/src/server` : Code source du serveur et gestionnaire de la logique de jeu.
* `/src/client` : Code source de l'interface JavaFX et de la réception réseau.
* `/resources` : Assets graphiques (cartes, icônes) et styles CSS.

---
*Projet réalisé dans le cadre d'un projet de synthèse informatique sur les systèmes distribués.*
