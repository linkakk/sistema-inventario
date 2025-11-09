#!/usr/bin/env bash
set -euo pipefail

# ====== CONFIGURA AQUÍ ======
SERVER_IP="192.168.0.100"                      # IP estática del server
SERVER_USER="totoserb"                         # tu usuario en el server (no root)
SERVER_APP_DIR="/home/totoserb/app-inventario" # carpeta donde irá el jar en el server
LOCAL_PROJECT_DIR="$HOME/Proyectos/sistema-inventario"
LOCAL_JAR="$LOCAL_PROJECT_DIR/backend/inventario/target/inventario-0.0.1-SNAPSHOT.jar"
SERVER_LOG="$SERVER_APP_DIR/app.log"
PORT="8080"

# ====== PASO 1: Compilar .jar ======
echo "👉 1/5 Compilando jar (skip tests)…"
cd "$LOCAL_PROJECT_DIR"
./mvnw clean package -DskipTests

# ====== PASO 2: Crear carpetas en el server ======
echo "👉 2/5 Creando estructura en el server (si no existe)…"
ssh "$SERVER_USER@$SERVER_IP" "mkdir -p \"$SERVER_APP_DIR\""

# ====== PASO 3: Enviar .jar al servidor ======
echo "👉 3/5 Enviando jar al server…"
scp "$LOCAL_JAR" "$SERVER_USER@$SERVER_IP:$SERVER_APP_DIR/app.jar"

# ====== PASO 4: Detener cualquier instancia previa ======
echo "👉 4/5 Deteniendo backend anterior si existe…"
ssh "$SERVER_USER@$SERVER_IP" "pkill -f 'app.jar' || true"

# ====== PASO 5: Iniciar backend en segundo plano ======
echo "👉 5/5 Iniciando backend en el server…"
ssh "$SERVER_USER@$SERVER_IP" "nohup java -jar \"$SERVER_APP_DIR/app.jar\" --server.port=$PORT > \"$SERVER_LOG\" 2>&1 &"

echo "✅ Despliegue completado. Backend corriendo en http://$SERVER_IP:$PORT"

