
import java.util.List;
import service.CategoriaService;
import service.ProductoService;
import ui.MenuHelper;
import entities.Categoria;

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
                    System.out.println("a hacer");
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
}
