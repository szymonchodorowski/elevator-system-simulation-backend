# Elevator System Simulation

## Requirements

- Java 21+

## Backend

```bash
cd elevator-system-simulation-backend
./mvnw spring-boot:run
```

Starts on `http://localhost:8099`

Configuration: `src/main/resources/application.yml` (port, number of floors/elevators, simulation interval).

### Tests

```bash
./mvnw test
```