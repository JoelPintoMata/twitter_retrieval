# Twitter retrieval

## Testing
Tests provided by maven and junit
```
mvn test
```

## Packaging
```
mvn clean install
```

## Executing
```
mvn spring-boot:run
```

, later to view output check `output.txt` in the project root.

## Docker

### Build jar
```
mvn install
```

### Build
```
docker build -t java-exercise .
```

### Run
```
docker run -p 8080:8080 -ti java-exercise .
```

, later to view output:
```
docker exec -ti `docker ps -q --filter ancestor=java-exercise` cat output.txt
```
