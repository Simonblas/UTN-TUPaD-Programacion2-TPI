# Food Store – TPI FINAL PROGRAMACIÓN 2

Este proyecto es el Trabajo Práctico Integrador para la materia Programación 2 de la Tecnicatura Universitaria en
Programación a Distancia (TUPAD) en la Universidad Tecnológica Nacional (UTN).

Es una aplicación de consola desarrollada en Java 21, diseñada bajo el paradigma de la Programación Orientada a
Objetos (POO) y una arquitectura modularizada en capas. El sistema emula la gestión integral de un negocio de comidas (
categorías, productos, usuarios y pedidos con detalles dinámicos) utilizando persistencia temporal en memoria a través
de Colecciones de Java.

---

## Entregables Obligatorios

* Video Demostrativo: [Enlace al Video en YouTube](AGREGAR_ACA_EL_LINK_DEL_VIDEO)
* Documentación Académica: El informe técnico complementario en formato PDF se encuentra adjunto en la raíz de este
  repositorio.

---

## ️Arquitectura y Paquetes

* `entities`: Clases del modelo de dominio que extienden de la clase abstracta `Base`, implementando la interfaz
  `Calculable`.
* `enums`: Estados fijos del sistema (`Estado`, `FormaPago`, `Rol`).
* `exception`: Excepciones personalizadas para el control seguro del flujo de negocio (`EntidadNoEncontradaException`,
  `ValidacionException`).
* `service`: Capa de lógica de negocio y simulación de persistencia mediante listas estáticas (`ArrayList`). Contiene
  algoritmos de validación atómica y mecanismos de baja lógica (*Soft Delete*).
* `ui`: Capa de presentación por consola. Centraliza la captura defensiva de datos y la sanitización del búfer de
  entrada de teclado.

---

## Requisitos del Sistema

* Java Development Kit (JDK): Versión 21 o superior.
* Gestor de Versiones: Git.
* IDE Recomendado: NetBeans.

---

## Instrucciones de Ejecución

### Desde un Entorno de Desarrollo (IDE)

Clonar el repositorio:

   ```bash
   git clone https://github.com/Simonblas/UTN-TUPaD-Programacion2-TPI 
   ```

Abrir el IDE de su preferencia (NetBeans).

Seleccionar Open Project (Abrir proyecto) y apuntar a la carpeta raíz `UTN-TUPaD-Programacion2-TPI`.

Asegurarse de que el proyecto esté configurado para compilar con JDK 21.

Buscar el archivo Main.java dentro de `UTN-TUPaD-Programacion2-TPI/src/`, hacer clic derecho y seleccionar Run File (Ejecutar).

---

## Reglas de Negocio e Integridad de Datos

* Baja Lógica (Soft Delete): Ninguna entidad se elimina físicamente de las colecciones (`.remove()`). Al dar de baja una categoría, producto, usuario o pedido, su estado cambia a `eliminado = true`, garantizando la integridad de los datos en consultas históricas.
* Validación Atómica de Stock: Al registrar un pedido, la capa de servicios procesa la compra en dos pasos (lectura y luego escritura). Si un solo ítem no cuenta con stock suficiente, la operación se interrumpe por completo lanzando una `ValidacionException`, evitando que el stock quede en un estado inconsistente.
* Control de Unicidad y Formatos: No se permiten nombres duplicados para categorías activas. En el caso de los usuarios, se valida que el email no esté repetido en el sistema y que cumpla con un formato estándar mediante expresiones regulares (Regex).
* Protección de Integridad de Categorías: Se bloquea la baja de cualquier categoría que tenga productos activos asociados, evitando dejar registros huérfanos en el catálogo.

---

## Autor

* Estudiantes: Simón Blas y Macarena Agustina Larrosa
* Carrera: Tecnicatura Universitaria en Programación (TUPAD)
* Institución: Universidad Tecnológica Nacional (UTN)
* Fecha de Entrega: 22 de Junio de 2026
