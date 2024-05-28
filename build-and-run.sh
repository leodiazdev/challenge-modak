#!/bin/bash

# Detener la ejecución si ocurre un error
set -e

# Directorio raíz donde está ubicado el script
rootDirectory="$(dirname "$(realpath "$0")")"

# Navega al directorio del proyecto productor y construye el proyecto
echo "Construyendo el proyecto Productor..."
cd "$rootDirectory/error-producer"
./gradlew clean build

# Navega al directorio del proyecto consumidor y construye el proyecto
echo "Construyendo el proyecto Consumidor..."
cd "$rootDirectory/errorconsumer"
./gradlew clean build

# Vuelve al directorio donde está tu docker-compose.yml
echo "Navegando al directorio de Docker Compose..."
cd "$rootDirectory"

# Construye y levanta los servicios de Docker Compose
echo "Construyendo y levantando servicios con Docker Compose..."
docker-compose up --build

echo "Proceso completado."
