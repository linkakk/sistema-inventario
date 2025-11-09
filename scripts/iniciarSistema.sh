#!/bin/bash

echo "🔄 Iniciando contenedor pg16..."

# Guardamos la ruta del script actual
SCRIPT_DIR="$(cd "$(dirname "$0")" && pwd)"
BACKEND_DIR="$SCRIPT_DIR/../backend/inventario"
ERRORES_DIR="$SCRIPT_DIR/../errores"

# Cambiar al directorio del backend
cd "$BACKEND_DIR" || {
    echo "❌ No se pudo cambiar al directorio del backend" > "$ERRORES_DIR/errores.txt"
    echo "❌ No se han corrido los servicios de manera correcta"
    exit 1
}

# Verificamos si el proyecto es válido (sin ejecutar todavía)
mvn validate > "$ERRORES_DIR/salida.log" 2> "$ERRORES_DIR/errores.txt"

# Si hay errores, los reportamos
if grep -q "\[ERROR\]" "$ERRORES_DIR/errores.txt"; then
    echo "❌ No se han corrido los servicios de manera correcta"
else
    echo "✅ Proyecto válido, abriendo terminal externa para ejecutarlo..."

    # Ejecutar iniciarVentana.sh desde su ruta absoluta
    bash "$SCRIPT_DIR/iniciarVentana.sh"
fi

