
import java.util.List;
import service.CategoriaService;
import service.ProductoService;
import ui.MenuHelper;
import entities.Categoria;
import entities.Producto;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
/**
 *
 * @author simob
 */
public class Main {

    private static final CategoriaService categoriaService = new CategoriaService();
    private static final ProductoService productoService = new ProductoService();

    public static void main(String[] args) {
        int opcion = -1;

        while (opcion != 0) {
            System.out.println("\n ----- SISTEMA DE PEDIDOS (FOOD STORE) ------");
            System.out.println("1. Categorias");
            System.out.println("2. Productos (Pendiente)");
            System.out.println("3. Usuarios (Pendiente)");
            System.out.println("4. Pedidos (Pendiente)");
            System.out.println("0. Salir");

            opcion = MenuHelper.leerEntero("Seleccione una opcion: ");

            switch (opcion) {
                case 1:
                    menuCategorias();
                    break;
                case 2:
                    menuProductos();
                    break;
                case 3:
                    System.out.println("a hacer");
                    break;
                case 4:
                    System.out.println("a hacer");
                    break;
                case 0:
                    System.out.println("Saliendo del sistema...");
                    break;
                default:
                    System.out.println("Opcion fuera de rango. Intente nuevamente.");
            }
        }
    }

    // SUBMENU DE CATEGORIAS (epica 1)
    private static void menuCategorias() {
        int opcion = -1;
        while (opcion != 0) {
            System.out.println("\n--- GESTION DE CATEGORIAS ---");
            System.out.println("1. Listar Categorias");
            System.out.println("2. Crear Categoria");
            System.out.println("3. Editar Categoria");
            System.out.println("4. Eliminar Categoria");
            System.out.println("0. Volver al Menu Principal");

            opcion = MenuHelper.leerEntero("Seleccione: ");

            try {
                switch (opcion) {
                    case 1: // HU-CAT-01: Listar
                        List<Categoria> activas = categoriaService.listarActivas();
                        if (activas.isEmpty()) {
                            System.out.println("No hay categorias cargadas.");
                        } else {
                            System.out.println("\n--- Listado de Categorias ---");
                            for (Categoria cat : activas) {
                                System.out.println(cat); // Usa el toString() simplificado
                            }
                        }
                        break;

                    case 2: // HU-CAT-02: Crear
                        System.out.println("\n--- Nueva Categoria ---");
                        String nombre = MenuHelper.leerTexto("Nombre de la categoria: ");
                        String desc = MenuHelper.leerTexto("Descripcion: ");
                        Categoria nueva = categoriaService.crear(nombre, desc);
                        System.out.println("Categoria creada con exito. ID generado: " + nueva.getId());
                        break;

                    case 3: // HU-CAT-03: Editar
                        System.out.println("\n--- Editar Categoria ---");
                        Long idEditar = MenuHelper.leerLongOpcional("Ingrese el ID de la categoria a editar: ");
                        String nuevoNombre = MenuHelper.leerTextoOpcional("Nuevo Nombre (deje vacio para mantener actual): ");
                        String nuevaDesc = MenuHelper.leerTextoOpcional("Nueva Descripcion (deje vacio para mantener actual): ");

                        categoriaService.editar(idEditar, nuevoNombre, nuevaDesc);
                        System.out.println("Categoria actualizada correctamente.");
                        break;

                    case 4: // HU-CAT-04: Eliminar
                        System.out.println("\n--- Eliminar Categoria /soft delete/ ---");
                        Long idEliminar = MenuHelper.leerLongOpcional("Ingrese el ID de la categoria a eliminar: ");

                        String confirma = MenuHelper.leerTexto("Esta seguro de eliminar? (S/N): ");
                        if (confirma.equalsIgnoreCase("S")) {
                            // Le pasa el productoService para cumplir la regla de integridad
                            categoriaService.eliminar(idEliminar, productoService);
                            System.out.println("Categoria eliminada logicamente con exito.");
                        } else {
                            System.out.println("Operacion cancelada por el usuario.");
                        }
                        break;

                    case 0:
                        break;
                    default:
                        System.out.println("Opcion invalida.");
                }
            } catch (Exception e) {
                // Atrapa cualquier EntidadNoEncontradaException o ValidacionException sin crashear
                System.out.println("\nERROR: " + e.getMessage());
            }
        }
    }

    // SUBMENU DE PRODUCTOS (epica 2)
    private static void menuProductos() {
        int opcion = -1;
        while (opcion != 0) {
            System.out.println("\n--- GESTION DE PRODUCTOS ---");
            System.out.println("1. Listar Productos");
            System.out.println("2. Crear Producto");
            System.out.println("3. Editar Producto");
            System.out.println("4. Eliminar Producto");
            System.out.println("0. Volver al Menu Principal");

            opcion = MenuHelper.leerEntero("Seleccione: ");

            try {
                switch (opcion) {
                    case 1: // HU-PROD-01: Listar
                        List<Producto> activos = productoService.listarActivos();
                        if (activos.isEmpty()) {
                            System.out.println("No hay productos cargados en el catalogo.");
                        } else {
                            System.out.println("\n--- Catalogo de Productos ---");
                            for (Producto prod : activos) {
                                System.out.println(prod); // Usa el toString() sobreescrito
                            }
                        }
                        break;

                    case 2: // HU-PROD-02: Crear
                        System.out.println("\n--- Nuevo Producto ---");
                        String nombre = MenuHelper.leerTexto("Nombre del producto: ");
                        String desc = MenuHelper.leerTexto("Descripcion: ");

                        // Validacion de entrada que no pongan letras donde van numeros
                        Double precio = null;
                        while (precio == null) {
                            precio = MenuHelper.leerDoubleOpcional("Precio: $");
                            if (precio == null) {
                                System.out.println("El precio es obligatorio para crear.");
                            }
                        }

                        Integer stock = null;
                        while (stock == null) {
                            int s = MenuHelper.leerEntero("Stock inicial: ");
                            if (s >= 0) {
                                stock = s;
                            }
                        }

                        String imagen = MenuHelper.leerTexto("URL de la imagen: ");

                        String dispInput = MenuHelper.leerTexto("¿Esta disponible para la venta? (S/N): ");
                        boolean disponible = dispInput.equalsIgnoreCase("S");

                        // Para facilitar la prueba, lista las categorias antes de pedir el ID
                        System.out.println("\nCategorias disponibles:");
                        List<Categoria> cats = categoriaService.listarActivas();
                        for (Categoria c : cats) {
                            System.out.println("  ID: " + c.getId() + " - " + c.getNombre());
                        }

                        Long catId = MenuHelper.leerLongOpcional("Ingrese el ID de la categoria a asociar: ");

                        Producto nuevo = productoService.crear(nombre, precio, desc, stock, imagen, disponible, catId);
                        System.out.println("Producto creado con exito. ID generado: " + nuevo.getId());
                        break;

                    case 3: // HU-PROD-03: Editar
                        System.out.println("\n--- Editar Producto ---");
                        Long idEditar = MenuHelper.leerLongOpcional("Ingrese el ID del producto a editar: ");

                        // Carga de datos. Si da Enter, va null y el servicio no lo toca.
                        String nuevoNombre = MenuHelper.leerTextoOpcional("Nuevo Nombre (Enter para mantener): ");
                        String nuevaDesc = MenuHelper.leerTextoOpcional("Nueva Descripcion (Enter para mantener): ");
                        Double nuevoPrecio = MenuHelper.leerDoubleOpcional("Nuevo Precio (Enter para mantener): $");

                        Long nuevoStockLong = MenuHelper.leerLongOpcional("Nuevo Stock (Enter para mantener): ");
                        Integer nuevoStock = (nuevoStockLong != null) ? nuevoStockLong.intValue() : null;

                        String nuevaImagen = MenuHelper.leerTextoOpcional("Nueva Imagen (Enter para mantener): ");

                        String nuevaDispInput = MenuHelper.leerTextoOpcional("Disponible? (S/N - Enter para mantener): ");
                        Boolean nuevaDisp = null;
                        if (nuevaDispInput != null) {
                            nuevaDisp = nuevaDispInput.equalsIgnoreCase("S");
                        }

                        Long nuevaCatId = MenuHelper.leerLongOpcional("Nuevo ID de Categoria (Enter para mantener): ");

                        productoService.editar(idEditar, nuevoNombre, nuevoPrecio, nuevaDesc, nuevoStock, nuevaImagen, nuevaDisp, nuevaCatId);
                        System.out.println("Producto actualizado correctamente.");
                        break;

                    case 4: // HU-PROD-04: Eliminar
                        System.out.println("\n--- Eliminar Producto /soft delete/ ---");
                        Long idEliminar = MenuHelper.leerLongOpcional("Ingrese el ID del producto a eliminar: ");

                        String confirma = MenuHelper.leerTexto("Estas seguro de retirar este producto del catalogo? (S/N): ");
                        if (confirma.equalsIgnoreCase("S")) {
                            productoService.eliminar(idEliminar);
                            System.out.println("Producto dado de baja con exito.");
                        } else {
                            System.out.println("Operacion cancelada.");
                        }
                        break;

                    case 0:
                        break;
                    default:
                        System.out.println("Opcion invalida.");
                }
            } catch (Exception e) {
                System.out.println("\nERROR: " + e.getMessage());
            }
        }
    }

}
