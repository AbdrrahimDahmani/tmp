# Kafka Streaming Platform

Ce dépôt fournit un exemple complet de plateforme de streaming en temps réel bâtie autour de Kafka, Spring Boot et Spring Cloud Stream. L'architecture est composée de plusieurs microservices spécialisés et d'une application Web de visualisation.

## Architecture

| Module | Description |
| --- | --- |
| `producer-service` | Expose une API REST (`POST /api/messages`) qui publie des messages JSON dans Kafka à l'aide de Spring Cloud Stream. |
| `consumer-service` | Consomme les messages produits, les journalise et expose un endpoint de diagnostic (`GET /api/messages/recent`). |
| `supplier-service` | Utilise un `Supplier<MessageEnvelope>` Spring Cloud Stream pour envoyer automatiquement des messages de type *heartbeat*. |
| `analytics-service` | Implémente un pipeline Kafka Streams qui agrège les messages en fenêtres de 30 secondes et publie les résultats sous forme JSON. |
| `web-app` | Application Web réactive (SSE) qui écoute le topic d'analytics et affiche les résultats en direct. |
| `common-model` | Module partagé contenant les DTO (`MessageEnvelope`, `AnalyticsResult`). |

Tous les services sont configurés pour se connecter à un broker Kafka accessible via la variable d'environnement `KAFKA_BROKERS` (par défaut `localhost:9092`).

## Prérequis

- Java 17+
- Maven 3.9+
- Un cluster Kafka accessible (Kafka + Zookeeper ou Kafka KRaft).

## Démarrage rapide

1. **Compiler l'ensemble des modules**
   ```bash
   mvn clean package
   ```
2. **Lancer un broker Kafka** (par exemple avec Docker Compose ou Confluent Platform) et créer les topics nécessaires :
   - `raw-messages`
   - `analytics-results`

3. **Démarrer les microservices** dans des terminaux séparés :
   ```bash
   # Producteur REST
   mvn spring-boot:run -pl producer-service

   # Consumer
   mvn spring-boot:run -pl consumer-service

   # Supplier (génération automatique)
   mvn spring-boot:run -pl supplier-service

   # Analytics (Kafka Streams)
   mvn spring-boot:run -pl analytics-service

   # Application Web
   mvn spring-boot:run -pl web-app
   ```

4. **Publier un message** via l'API REST :
   ```bash
   curl -X POST http://localhost:8081/api/messages \
        -H 'Content-Type: application/json' \
        -d '{"type":"order","payload":"{\"id\":123}","source":"http-client"}'
   ```

5. **Consulter le dashboard Web** sur [http://localhost:8080](http://localhost:8080) pour observer les agrégations temps réel.

Les services exposent également des endpoints Actuator `health` et `info` pour faciliter le monitoring.
