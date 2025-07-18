# Generador Automático de Mapeo Síntomas-Enfermedades

## Descripción

Este script automatiza la generación de un mapeo completo entre síntomas veterinarios porcinos y sus enfermedades correspondientes. El resultado es un archivo JSON que puede ser utilizado en aplicaciones Android para diagnóstico veterinario.

## Archivos Generados

### 1. `symptom_disease_mapping_complete.json`
Archivo JSON principal que contiene el mapeo completo de todos los síntomas a sus enfermedades correspondientes.

### 2. `generar_mapeo_automatico.kt`
Script principal en Kotlin que automatiza todo el proceso de generación.

## Estructura del JSON

```json
{
  "symptomToDiseases": {
    "SÍNTOMA": [
      "ENFERMEDAD1",
      "ENFERMEDAD2"
    ],
    "OTRO_SÍNTOMA": []
  }
}
```

## Cómo Usar el Script

### Opción 1: Ejecutar desde Android Studio
1. Abre el archivo `generar_mapeo_automatico.kt` en Android Studio
2. Ejecuta la función `main()` 
3. El archivo JSON se generará automáticamente

### Opción 2: Ejecutar desde línea de comandos
```bash
kotlinc generar_mapeo_automatico.kt -include-runtime -d mapeo.jar
java -jar mapeo.jar
```

## Estadísticas del Mapeo

El script genera las siguientes estadísticas:
- **Total de síntomas**: 300+ síntomas veterinarios
- **Síntomas con enfermedades asociadas**: Síntomas que tienen al menos una enfermedad relacionada
- **Síntomas sin enfermedades**: Síntomas que no tienen enfermedades asociadas (requieren revisión manual)
- **Total de enfermedades únicas**: Número de enfermedades diferentes en el mapeo

## Enfermedades Incluidas

El script incluye mapeo para las siguientes enfermedades:

1. **Estreptococosis** - Infección bacteriana
2. **Pleuroneumonia** - Enfermedad respiratoria
3. **PRRS** - Síndrome reproductivo y respiratorio porcino
4. **Circovirus** - Enfermedad viral
5. **Salmonelosis** - Infección bacteriana intestinal
6. **Brucelosis** - Enfermedad bacteriana reproductiva
7. **Erisipela** - Enfermedad bacteriana sistémica
8. **Clostridiosis** - Infección bacteriana intestinal
9. **ZEARALENONA** - Micotoxina
10. **AFLATOXINA** - Micotoxina
11. **OCRATOXINA** - Micotoxina
12. **TRICOTECENOS** - Micotoxinas
13. **FUMONISINA** - Micotoxina
14. **CAMPILOBACTERIOSIS** - Infección bacteriana
15. **COCCIDIOSIS** - Enfermedad parasitaria
16. **ASCARIS SUUM** - Parásito intestinal
17. **ESTRONGILOIDES** - Parásito intestinal

## Algoritmo de Búsqueda

El script utiliza un algoritmo inteligente que busca coincidencias por:

1. **Coincidencia exacta**: Síntomas idénticos
2. **Coincidencia parcial**: Un síntoma contiene al otro
3. **Búsqueda semántica**: Usando palabras clave y sinónimos médicos

### Palabras Clave Incluidas
- FIEBRE, DIARREA, VOMITO, TOS
- DOLOR, ANOREXIA, DEPRESION
- CONVULSIONES, COJERA, ARTRITIS
- ABORTO, ANEMIA, ICTERICIA
- DESHIDRATACION, LETARGIA, DISNEA
- EDEMA, NECROSIS, HEMORRAGIA
- INFLAMACION, ULCERAS, DERMATITIS
- MENINGITIS, NEUMONIA, ENTERITIS
- MASTITIS, METRITIS, ORQUITIS

## Uso en la Aplicación Android

### 1. Cargar el JSON
```kotlin
val jsonString = assets.open("symptom_disease_mapping_complete.json").bufferedReader().use { it.readText() }
val gson = Gson()
val mapping = gson.fromJson(jsonString, SymptomDiseaseMapping::class.java)
```

### 2. Buscar enfermedades por síntoma
```kotlin
fun buscarEnfermedadesPorSintoma(sintoma: String): List<String> {
    return mapping.symptomToDiseases[sintoma.uppercase()] ?: emptyList()
}
```

### 3. Diagnóstico por múltiples síntomas
```kotlin
fun diagnosticarPorSintomas(sintomas: List<String>): Map<String, Int> {
    val enfermedades = mutableMapOf<String, Int>()
    
    sintomas.forEach { sintoma ->
        val enfermedadesSintoma = buscarEnfermedadesPorSintoma(sintoma)
        enfermedadesSintoma.forEach { enfermedad ->
            enfermedades[enfermedad] = enfermedades.getOrDefault(enfermedad, 0) + 1
        }
    }
    
    return enfermedades.toList()
        .sortedByDescending { it.second }
        .toMap()
}
```

## Mantenimiento y Actualización

### Agregar Nuevas Enfermedades
1. Edita la variable `enfermedadesConSintomas` en el script
2. Agrega la nueva enfermedad con sus síntomas
3. Ejecuta el script para regenerar el JSON

### Agregar Nuevos Síntomas
1. Agrega el nuevo síntoma a la lista `sintomasUsuario`
2. Ejecuta el script para incluir el nuevo síntoma en el mapeo

### Mejorar el Algoritmo de Búsqueda
1. Edita la función `buscarSimilitud()`
2. Agrega nuevas palabras clave en el mapa `palabrasClave`
3. Ejecuta el script para aplicar las mejoras

## Validación y Revisión

### Síntomas Sin Enfermedades
El script identifica síntomas que no tienen enfermedades asociadas. Estos requieren revisión manual para:
- Verificar si son síntomas reales
- Asignar enfermedades correspondientes
- Corregir nombres o descripciones

### Verificación de Mapeo
Revisa periódicamente el mapeo para:
- Confirmar la precisión de las asociaciones
- Agregar nuevas enfermedades o síntomas
- Corregir errores de mapeo

## Notas Importantes

1. **Precisión**: El mapeo es una herramienta de apoyo, no reemplaza el diagnóstico veterinario profesional
2. **Actualización**: Mantén actualizada la base de datos de enfermedades
3. **Validación**: Revisa regularmente los síntomas sin enfermedades asociadas
4. **Backup**: Mantén copias de seguridad de los archivos JSON generados

## Contacto y Soporte

Para preguntas o mejoras del script, revisa:
- La documentación del código
- Los comentarios en el archivo `.kt`
- Las estadísticas generadas por el script

---

**Versión**: 1.0  
**Fecha**: Diciembre 2024  
**Compatibilidad**: Android API 21+  
**Lenguaje**: Kotlin 