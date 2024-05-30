# Establecer política de ejecución para permitir la ejecución de scripts (ejecutar como administrador si es necesario)
Set-ExecutionPolicy -Scope Process -ExecutionPolicy Bypass

# Detener la ejecución si ocurre un error
$ErrorActionPreference = "Stop"

# Directorio raíz donde está ubicado el script
$rootDirectory = Split-Path -Parent $MyInvocation.MyCommand.Path

# Navega al directorio del proyecto productor y construye el proyecto
Write-Host "Construyendo el proyecto Productor..."
Set-Location "$rootDirectory\error-producer"
gradle clean build

# Navega al directorio del proyecto consumidor y construye el proyecto
Write-Host "Construyendo el proyecto Consumidor..."
Set-Location "$rootDirectory\errorconsumer"
gradle clean build

# Vuelve al directorio donde está tu docker-compose.yml
Write-Host "Navegando al directorio de Docker Compose..."
Set-Location $rootDirectory

# Construye y levanta los servicios de Docker Compose
Write-Host "Construyendo y levantando servicios con Docker Compose..."
docker-compose up --build

Write-Host "Proceso completado."
