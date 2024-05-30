#!/bin/bash

rootDirectory="$(dirname "$(realpath "$0")")"

chmod +x "$rootDirectory/error-producer/gradlew"
chmod +x "$rootDirectory/errorconsumer/gradlew"

if [ ! -f "$rootDirectory/error-producer/gradle/wrapper/gradle-wrapper.jar" ]; then
    echo "Configurando Gradle Wrapper..."
    (cd "$rootDirectory/error-producer" && gradle wrapper)
fi

if [ ! -f "$rootDirectory/errorconsumer/gradle/wrapper/gradle-wrapper.jar" ]; then
    echo "Configurando Gradle Wrapper..."
    (cd "$rootDirectory/errorconsumer" && gradle wrapper)
fi

echo "Construyendo el proyecto Productor..."
(cd "$rootDirectory/error-producer" && gradle clean build)

echo "Construyendo el proyecto Consumidor..."
(cd "$rootDirectory/errorconsumer" && gradle clean build)

echo "Navegando al directorio de Docker Compose..."
cd "$rootDirectory"
docker-compose up --build

echo "Proceso completado."
