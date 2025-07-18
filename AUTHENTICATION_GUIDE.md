# Guía del Sistema de Autenticación

## Descripción General

La aplicación utiliza Firebase Authentication y Firestore Database para manejar el sistema de autenticación y almacenamiento de datos de usuario.

## Componentes del Sistema

### 1. Firebase Authentication
- **Propósito**: Maneja la autenticación de usuarios (registro, inicio de sesión, cierre de sesión)
- **Configuración**: Archivo `google-services.json` en la carpeta `app/`
- **Funcionalidades**:
  - Registro con email y contraseña
  - Inicio de sesión con email y contraseña
  - Verificación de sesión activa
  - Cierre de sesión

### 2. Firestore Database
- **Propósito**: Almacena datos adicionales del usuario
- **Colección**: `users`
- **Documento**: ID único del usuario (UID de Firebase Auth)
- **Estructura de datos**:
  ```kotlin
  data class UserData(
      val uid: String = "",
      val fullName: String = "",
      val city: String = "",
      val age: Int = 0,
      val gender: String = "",
      val email: String = "",
      val profileImageUrl: String? = null,
      val points: Int = 0,
      val cardNumber: String? = null
  )
  ```

### 3. UserManager (Clase Helper)
- **Propósito**: Centraliza las operaciones relacionadas con usuarios
- **Funcionalidades**:
  - Obtener datos del usuario actual
  - Verificar existencia de datos en Firestore
  - Crear datos básicos de usuario
  - Actualizar datos del usuario

## Flujo de Autenticación

### Registro de Usuario
1. Usuario llena formulario en `RegisterActivity`
2. Se crea cuenta en Firebase Authentication
3. Se almacenan datos adicionales en Firestore
4. Se redirige al usuario a `LoginActivity`

### Inicio de Sesión
1. Usuario ingresa credenciales en `LoginActivity`
2. Se autentica con Firebase Authentication
3. Se verifica existencia de datos en Firestore
4. Si no existen datos, se crean datos básicos
5. Se redirige al usuario a `MainActivity`

### Visualización de Datos del Usuario
1. `MainActivity` carga datos del usuario desde Firestore
2. Se muestra nombre y email en la barra superior
3. Se carga imagen de perfil si existe
4. Se maneja casos de error y datos faltantes

## Archivos Principales

### Activities
- `LoginActivity.kt`: Maneja el inicio de sesión
- `RegisterActivity.kt`: Maneja el registro de usuarios
- `MainActivity.kt`: Pantalla principal con datos del usuario
- `ProfileActivity.kt`: Vista detallada del perfil
- `EditProfileActivity.kt`: Edición de datos del perfil

### Data Classes
- `UserData.kt`: Modelo de datos del usuario
- `UserManager.kt`: Clase helper para operaciones de usuario

### Layouts
- `activity_login.xml`: Interfaz de inicio de sesión
- `activity_register.xml`: Interfaz de registro
- `activity_main.xml`: Interfaz principal con información del usuario
- `activity_profile.xml`: Interfaz del perfil

## Configuración de Firebase

### 1. Proyecto Firebase
- **ID del proyecto**: `curepet-28b0b`
- **Bucket de Storage**: `curepet-28b0b.firebasestorage.app`

### 2. Servicios Habilitados
- **Authentication**: Email/Password
- **Firestore Database**: Colección `users`
- **Storage**: Imágenes de perfil

### 3. Dependencias (build.gradle.kts)
```kotlin
implementation(platform("com.google.firebase:firebase-bom:32.2.0"))
implementation("com.google.firebase:firebase-auth-ktx")
implementation("com.google.firebase:firebase-firestore-ktx")
implementation("com.google.firebase:firebase-storage-ktx")
```

## Características Implementadas

### ✅ Funcionalidades Completadas
- [x] Registro de usuarios con validación
- [x] Inicio de sesión con manejo de errores
- [x] Almacenamiento de datos en Firestore
- [x] Visualización de datos del usuario en MainActivity
- [x] Edición de perfil
- [x] Carga de imágenes de perfil
- [x] Cierre de sesión
- [x] Verificación de sesión activa
- [x] Manejo de errores y casos edge

### 🔧 Mejoras Implementadas
- [x] Interfaz mejorada para mostrar datos del usuario
- [x] Indicadores de progreso durante operaciones
- [x] Validación de datos en Firestore
- [x] Creación automática de datos básicos
- [x] Clase helper UserManager para operaciones centralizadas
- [x] Manejo robusto de errores
- [x] Actualización automática de datos

## Uso del Sistema

### Para Desarrolladores
1. **Agregar nuevos campos**: Modificar `UserData.kt` y actualizar Firestore
2. **Nuevas operaciones**: Usar `UserManager` para operaciones de usuario
3. **Validaciones**: Agregar en las Activities correspondientes

### Para Usuarios
1. **Registro**: Completar formulario con datos personales
2. **Inicio de sesión**: Usar email y contraseña registrados
3. **Editar perfil**: Acceder desde menú → Ajustes → Editar perfil
4. **Ver perfil**: Acceder desde menú → Mi Perfil
5. **Cerrar sesión**: Acceder desde menú → Ajustes → Cerrar sesión

## Seguridad

- Las contraseñas se almacenan de forma segura en Firebase Authentication
- Los datos sensibles se validan antes de almacenar
- Se implementa manejo de errores para evitar crashes
- Se verifica la autenticación antes de acceder a datos

## Troubleshooting

### Problemas Comunes
1. **Error de conexión**: Verificar conexión a internet
2. **Datos no se cargan**: Verificar configuración de Firestore
3. **Imagen no se carga**: Verificar permisos de Storage
4. **Error de autenticación**: Verificar credenciales de Firebase

### Logs
- Usar TAG "LoginActivity", "RegisterActivity", "MainActivity" para debugging
- Los errores se registran en Logcat con detalles específicos 