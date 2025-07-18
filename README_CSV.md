# 📊 Archivos CSV - Mapeo de Síntomas y Enfermedades Porcinas

## 📁 Archivos Disponibles para Descarga

### 1. `sintomas_enfermedades.csv` - Archivo Principal
**Descripción:** Mapeo completo de síntomas con sus enfermedades asociadas
**Formato:** CSV estándar con separadores por comas
**Columnas:**
- `SÍNTOMA`: Nombre del síntoma
- `ENFERMEDADES`: Lista de enfermedades separadas por punto y coma
- `CONTEO_ENFERMEDADES`: Número total de enfermedades asociadas

### 2. `sintomas_enfermedades_mejorado.csv` - Versión Mejorada
**Descripción:** Versión expandida con columnas separadas para cada enfermedad
**Formato:** CSV con columnas individuales para mejor análisis
**Columnas:**
- `SÍNTOMA`: Nombre del síntoma
- `ENFERMEDAD_1` a `ENFERMEDAD_14`: Columnas individuales para cada enfermedad
- `TOTAL_ENFERMEDADES`: Conteo total
- `ESTADO`: "CON ENFERMEDADES" o "SIN ENFERMEDADES"

### 3. `resumen_sintomas_enfermedades.csv` - Estadísticas
**Descripción:** Resumen estadístico del mapeo
**Contenido:** Estadísticas, porcentajes y recomendaciones

## 🚀 Cómo Descargar los Archivos

### Opción 1: Descarga Directa desde el Proyecto
Los archivos están ubicados en la raíz del proyecto:
```
MyApplication2/
├── sintomas_enfermedades.csv
├── sintomas_enfermedades_mejorado.csv
├── resumen_sintomas_enfermedades.csv
└── README_CSV.md
```

### Opción 2: Usando Git (si tienes el repositorio)
```bash
git clone [URL_DEL_REPOSITORIO]
cd MyApplication2
```

### Opción 3: Copia Manual
1. Navega a la carpeta del proyecto
2. Copia los archivos `.csv` a tu ubicación deseada

## 📊 Cómo Usar los Archivos CSV

### En Excel/LibreOffice Calc
1. Abre Excel o LibreOffice Calc
2. Ve a **Archivo > Abrir**
3. Selecciona el archivo CSV
4. Asegúrate de que la codificación sea **UTF-8**
5. Los datos se mostrarán en columnas separadas

### En Google Sheets
1. Ve a [sheets.google.com](https://sheets.google.com)
2. Crea una nueva hoja
3. Ve a **Archivo > Importar**
4. Sube el archivo CSV
5. Selecciona **UTF-8** como codificación

### En Python (para análisis)
```python
import pandas as pd

# Cargar el archivo principal
df = pd.read_csv('sintomas_enfermedades.csv', encoding='utf-8')

# Cargar la versión mejorada
df_mejorado = pd.read_csv('sintomas_enfermedades_mejorado.csv', encoding='utf-8')

# Cargar el resumen
resumen = pd.read_csv('resumen_sintomas_enfermedades.csv', encoding='utf-8')

# Ver las primeras filas
print(df.head())
```

### En R (para análisis estadístico)
```r
# Cargar los datos
sintomas <- read.csv("sintomas_enfermedades.csv", fileEncoding = "UTF-8")
sintomas_mejorado <- read.csv("sintomas_enfermedades_mejorado.csv", fileEncoding = "UTF-8")
resumen <- read.csv("resumen_sintomas_enfermedades.csv", fileEncoding = "UTF-8")

# Ver estructura
str(sintomas)
head(sintomas)
```

## 🔍 Análisis Recomendados

### 1. Síntomas sin Enfermedades Asociadas
```python
# Filtrar síntomas sin enfermedades
sintomas_sin_enfermedades = df[df['CONTEO_ENFERMEDADES'] == 0]
print(f"Síntomas sin enfermedades: {len(sintomas_sin_enfermedades)}")
```

### 2. Enfermedades Más Frecuentes
```python
# Contar enfermedades por síntoma
enfermedades_frecuentes = df[df['CONTEO_ENFERMEDADES'] > 0].sort_values('CONTEO_ENFERMEDADES', ascending=False)
print(enfermedades_frecuentes.head(10))
```

### 3. Análisis de Correlaciones
```python
# Usar la versión mejorada para análisis de correlación
import numpy as np
from sklearn.feature_extraction.text import CountVectorizer

# Crear matriz de síntomas vs enfermedades
vectorizer = CountVectorizer()
X = vectorizer.fit_transform(df_mejorado.iloc[:, 1:15].fillna(''))
```

## 📱 Integración con la Aplicación Android

### 1. Copiar a la Aplicación
```bash
# Copiar los archivos CSV a la carpeta assets
cp sintomas_enfermedades.csv app/src/main/assets/
cp sintomas_enfermedades_mejorado.csv app/src/main/assets/
```

### 2. Cargar en Kotlin
```kotlin
// En tu Activity o ViewModel
private fun cargarMapeoSintomas() {
    try {
        val inputStream = assets.open("sintomas_enfermedades.csv")
        val reader = BufferedReader(InputStreamReader(inputStream))
        
        // Saltar la primera línea (encabezados)
        reader.readLine()
        
        val mapeoSintomas = mutableMapOf<String, List<String>>()
        
        reader.useLines { lines ->
            lines.forEach { line ->
                val partes = line.split(",")
                if (partes.size >= 2) {
                    val sintoma = partes[0].trim().removeSurrounding("\"")
                    val enfermedades = partes[1].trim().removeSurrounding("\"").split(";")
                    mapeoSintomas[sintoma] = enfermedades
                }
            }
        }
        
        // Usar el mapeo en tu aplicación
        Log.d("MapeoSintomas", "Cargados ${mapeoSintomas.size} síntomas")
        
    } catch (e: Exception) {
        Log.e("MapeoSintomas", "Error cargando CSV: ${e.message}")
    }
}
```

## 📈 Estadísticas Importantes

- **Total de síntomas:** 385
- **Síntomas con enfermedades:** 89 (23.12%)
- **Síntomas sin enfermedades:** 296 (76.88%)
- **Enfermedades principales:** 15 enfermedades identificadas
- **Toxinas incluidas:** 5 tipos de micotoxinas

## 🔄 Actualización del Mapeo

Para actualizar el mapeo:

1. Ejecuta el script `convertir_json_a_csv.kt`
2. Revisa los nuevos archivos generados
3. Valida con expertos veterinarios
4. Actualiza la aplicación Android

## 📞 Soporte

Si tienes problemas con los archivos CSV:

1. Verifica la codificación (debe ser UTF-8)
2. Asegúrate de que las comas estén correctamente escapadas
3. Revisa que no haya caracteres especiales problemáticos

## 📝 Notas Importantes

- Los archivos están codificados en **UTF-8**
- Los síntomas sin enfermedades requieren **revisión manual**
- El mapeo se basa en datos médicos veterinarios existentes
- Se recomienda validación por expertos antes de uso clínico

---

**Fecha de generación:** 19 de diciembre de 2024  
**Versión:** 1.0  
**Autor:** Sistema de Mapeo Automático de Síntomas 