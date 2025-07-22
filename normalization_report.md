# Reporte de Normalización - enfermedades.json

## Estado Actual
El archivo `enfermedades.json` **NO está completamente normalizado**. Se encontraron múltiples inconsistencias estructurales y de datos.

## Problemas Identificados

### 1. Estructura Inconsistente
- **Archivo principal**: Estructura jerárquica con sistemas → síntomas
- **Archivos assets**: Estructura plana con array de objetos
- **Solución**: Unificar a una estructura consistente

### 2. Campos Inconsistentes
```json
// Archivo principal usa:
{
  "nombre": "SÍNTOMA",
  "edades": [...],
  "descripcion": "..."
}

// Assets usan:
{
  "sintoma": "SÍNTOMA", 
  "etapas": [...],
  "descripcion": "..."
}
```

### 3. Duplicaciones Masivas
- **41 variantes de ictericia** que podrían consolidarse en tipos específicos
- Síntomas respiratorios duplicados con ligeras variaciones
- **Recomendación**: Consolidar en categorías principales con subcategorías

### 4. Inconsistencias en Nombres de Enfermedades
- Mezcla de MAYÚSCULAS y Formato Título
- Ejemplos: "Pleuroneumonia" vs "PLEURONEUMONIA"
- **Solución**: Estandarizar formato de nomenclatura

### 5. Campos Faltantes o Inconsistentes
- Campo `"tipo"` presente solo en algunos registros
- Campo `"sistema"` solo en archivo custom
- Listas de enfermedades no coinciden entre archivos

## Acciones Recomendadas

### Prioridad Alta
1. **Unificar estructura** de datos entre todos los archivos
2. **Estandarizar nomenclatura** de campos y valores
3. **Consolidar duplicados** de ictericia y otros síntomas repetidos
4. **Normalizar nombres** de enfermedades

### Prioridad Media
1. Agregar campo `"sistema"` consistente a todos los registros
2. Validar correspondencia entre síntomas y enfermedades
3. Estandarizar descripciones médicas

### Prioridad Baja
1. Optimizar estructura para consultas eficientes
2. Agregar validaciones de integridad de datos

## Estado: ❌ NO NORMALIZADO
**Se requiere trabajo de normalización antes de usar en producción.**