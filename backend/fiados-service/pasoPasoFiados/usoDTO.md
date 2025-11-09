📘 ¿Qué es un DTO?

DTO (Data Transfer Object) es una clase o record usada para transferir datos entre capas (por ejemplo: Controller → Service o Service → Frontend).

Su función principal es transportar información, no tener lógica de negocio.

| Tipo de DTO    | Archivo                 | Función                                                                               |
| -------------- | ----------------------- | ------------------------------------------------------------------------------------- |
| DTO de entrada | `FiadoRequestDTO.java`  | Recibe los datos del cliente cuando se crea un nuevo fiado         (`POST /api/fiados`).      |
| DTO de salida  | `FiadoResponseDTO.java` | Envía la información del fiado al cliente en las respuestas    (`GET`, `POST`, `PATCH`). |


⚙️ Por qué usamos DTO en lugar del modelo directamente

Seguridad: evita exponer atributos internos o sensibles del modelo.

Mantenibilidad: si cambia la estructura interna (Fiado.java), no rompemos el contrato del API.

Validación: permite aplicar reglas (@NotBlank, @Positive, etc.) antes de tocar la lógica del sistema.

Claridad: separa los datos de entrada/salida de las reglas del negocio.}

Frontend (JSON)
   ↓
Controller
   ↓ recibe → FiadoRequestDTO
   ↓
Service crea → Fiado (modelo interno)
   ↓
Service genera → FiadoResponseDTO
   ↓
Controller responde → JSON



📄 1️⃣ FiadoRequestDTO.java
💡 Propósito

Recibir los datos enviados por el cliente en la creación de un fiado.
Se usa en el endpoint POST /api/fiados.