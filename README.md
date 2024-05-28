# challenge-modak

## Prerequisites

Before you begin, ensure you have met the following requirements:
- You have a `Windows/Linux` machine.
- You have installed `Docker` and `Docker Compose`.
- You have installed `Java JDK 17`.
- You have installed `Gradle` (make sure it's configured to use with the projects).

## System Architecture and Operation

### Overview

This system comprises two main components: the Producer and the Consumer. These applications are designed to communicate asynchronously via RabbitMQ, with Redis being used for rate limiting purposes.

### Architecture

- **Producer Application**: Responsible for receiving API requests and sending messages to a RabbitMQ queue. It employs Redis to enforce rate limits, preventing users from exceeding the number of allowed messages per unit time.

- **Consumer Application**: Listens for messages on the RabbitMQ queue and processes them according to the business logic defined. It also interacts with Redis to update and check rate limits.

- **RabbitMQ**: Acts as the message broker between the Producer and Consumer, ensuring reliable message queuing and delivery.

- **Redis**: Used by the Producer for rate limiting. It stores data regarding the number of messages sent and the timestamps to manage rate limits effectively.

### Operation Flow

1. **API Request**: A client sends a request to the Producer to send a message.
2. **Rate Limiting**: The Producer checks with Redis to determine if the client has exceeded their rate limit.
3. **Message Queueing**: If not exceeded, the message is queued in RabbitMQ.
4. **Message Processing**: The Consumer retrieves the message from RabbitMQ and processes it.
5. **Feedback**: The Consumer optionally sends feedback about the processing result back to the Producer or directly to the client.

## Installing and Running the Projects

Follow these steps to get your projects up and running:

### For Windows:

1. **Open PowerShell as Administrator**
    - This is required to set the execution policies to allow the scripts to run.

2. **Navigate to the Project Directory**
    - Use `cd path_to_your_project` to go to your project directory.

3. **Run the Script**
    - Execute `.\build-and-run.ps1` to build the projects and start the services.

### For Linux/macOS:

1. **Open Terminal**

2. **Navigate to the Project Directory**
    - Use `cd path_to_your_project` to go to your project directory.

3. **Make the Script Executable**
    - Run `chmod +x build-and-run.sh`.

4. **Run the Script**
    - Execute `./build-and-run.sh` to build the projects and start the services.

## Testing the API with Postman

This section provides an example of how to test the API endpoints using Postman. Assume that there is an endpoint `/api/messages/send-message` in the Producer application that accepts POST requests to send messages.

### Example Request

**URL**: `http://localhost:8080/api/messages/send-message`

**Method**: `POST`

**Headers**:
- `Content-Type`: `application/json`

**Body**:
```json
{
"userId": "12345",
"type": "status",
"content": "This is a test message"
}
```

# challenge-modak

## Prerrequisitos

Antes de comenzar, asegúrate de cumplir con los siguientes requisitos:
- Tienes una máquina `Windows/Linux`.
- Has instalado `Docker` y `Docker Compose`.
- Has instalado `Java JDK 17`.
- Has instalado `Gradle` (asegúrate de que esté configurado para usar con los proyectos).

## Arquitectura y Funcionamiento del Sistema

### Visión General

Este sistema consta de dos componentes principales: el Productor y el Consumidor. Estas aplicaciones están diseñadas para comunicarse de manera asíncrona a través de RabbitMQ, utilizando Redis para la limitación de tasa.

### Arquitectura

- **Aplicación Productor**: Responsable de recibir solicitudes de API y enviar mensajes a una cola de RabbitMQ. Utiliza Redis para imponer límites de tasa, evitando que los usuarios excedan el número permitido de mensajes por unidad de tiempo.

- **Aplicación Consumidor**: Escucha mensajes en la cola de RabbitMQ y los procesa según la lógica de negocio definida. También interactúa con Redis para actualizar y verificar los límites de tasa.

- **RabbitMQ**: Actúa como el intermediario de mensajes entre el Productor y el Consumidor, asegurando la cola de mensajes y la entrega confiable.

- **Redis**: Utilizado por el Productor para la limitación de tasa. Almacena datos sobre el número de mensajes enviados y las marcas de tiempo para gestionar eficazmente los límites de tasa.

### Flujo de Operación

1. **Solicitud API**: Un cliente envía una solicitud al Productor para enviar un mensaje.
2. **Limitación de Tasa**: El Productor verifica con Redis para determinar si el cliente ha excedido su límite de tasa.
3. **Encolado de Mensajes**: Si no se ha excedido, el mensaje se encola en RabbitMQ.
4. **Procesamiento de Mensajes**: El Consumidor recupera el mensaje de RabbitMQ y lo procesa.
5. **Retroalimentación**: El Consumidor opcionalmente envía retroalimentación sobre el resultado del procesamiento al Productor o directamente al cliente.

## Instalación y Ejecución de los Proyectos

Sigue estos pasos para poner en marcha tus proyectos:

### Para Windows:

1. **Abre PowerShell como Administrador**
    - Esto es necesario para configurar las políticas de ejecución que permitan correr los scripts.

2. **Navega al Directorio del Proyecto**
    - Usa `cd path_to_your_project` para ir al directorio de tu proyecto.

3. **Ejecuta el Script**
    - Ejecuta `.\build-and-run.ps1` para construir los proyectos e iniciar los servicios.

### Para Linux/macOS:

1. **Abre la Terminal**

2. **Navega al Directorio del Proyecto**
    - Usa `cd path_to_your_project` para ir al directorio de tu proyecto.

3. **Haz el Script Ejecutable**
    - Ejecuta `chmod +x build-and-run.sh`.

4. **Ejecuta el Script**
    - Ejecuta `./build-and-run.sh` para construir los proyectos e iniciar los servicios.

## Probando la API con Postman

Esta sección proporciona un ejemplo de cómo probar los endpoints de la API usando Postman. Supongamos que existe un endpoint `/api/messages/send-message` en la aplicación Productor que acepta solicitudes POST para enviar mensajes.

### Ejemplo de Solicitud

**URL**: `http://localhost:8080/api/messages/send-message`

**Método**: `POST`

**Encabezados**:
- `Content-Type`: `application/json`

**Cuerpo**:
```json
{
"userId": "12345",
"type": "status",
"content": "Este es un mensaje de prueba"
}
