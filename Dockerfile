# Uso de imagen base: java 21
FROM openjdk:21-jdk-slim

# Directorio raiz del contenedor
WORKDIR /app

# Copiar el archivo jar dentro del contenedor, en /app
COPY target/movies-0.0.1.jar /app/movies.jar

# Puerto donde se ejecuta el contenedor (Informativo)
EXPOSE 8080

# Ejecuta la aplicación
ENTRYPOINT ["java","-jar","movies.jar"]
