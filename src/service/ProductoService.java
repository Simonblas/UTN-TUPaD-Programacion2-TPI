/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package service;

import java.util.ArrayList;
import java.util.List;
import entities.Producto;
import entities.Categoria;
import exception.EntidadNoEncontradaException;
import exception.ValidacionException;

/**
 *
 * @author simob
 */
public class ProductoService {

// Almacenamiento en memoria compartido, el final bloquea la referencia de la lista pero deja manipularla
    private static final List<Producto> productos = new ArrayList<>();
    private static Long contadorId = 1L; //la L indica que es un long y java pueda compilar
    private final CategoriaService categoriaService = new CategoriaService();

    // HU-PROD-02: Crear producto con validaciones de negocio
    public Producto crear(String nombre, Double precio, String descripcion, int stock, String imagen, boolean disponible, Long categoriaId)
            throws ValidacionException, EntidadNoEncontradaException {

        if (nombre == null || nombre.trim().isEmpty()) {
            throw new ValidacionException("El nombre del producto no puede estar vacio.");
        }
        if (precio < 0) {
            throw new ValidacionException("El precio no puede ser menor a 0.");
        }
        if (stock < 0) {
            throw new ValidacionException("El stock no puede ser menor a 0.");
        }

        // Validar y asociar categoria existente, si no se encuentra arroja exception
        Categoria categoria = categoriaService.buscarPorId(categoriaId);

        Producto nuevo = new Producto(nombre.trim(), precio, descripcion, stock, imagen, disponible, categoria);
        nuevo.setId(contadorId++);
        productos.add(nuevo);
        return nuevo;
    }

    // HU-PROD-01: Listar productos activos
    public List<Producto> listarActivos() {
        List<Producto> activas = new ArrayList<>();
        for (Producto prod : productos) {
            if (!prod.isEliminado()) {
                activas.add(prod);
            }
        }
        return activas;
    }

    // Buscar por ID
    public Producto buscarPorId(Long id) throws EntidadNoEncontradaException {
        if (id == null) {
            throw new EntidadNoEncontradaException("El ID del producto no puede ser nulo.");
        }

        for (Producto prod : productos) {
            if (prod.getId().equals(id) && !prod.isEliminado()) {
                return prod;
            }
        }
        throw new EntidadNoEncontradaException("Producto con ID " + id + " no encontrado.");
    }

// HU-PROD-03: Editar producto (Con resguardo de valores anteriores)
    public void editar(Long id, String nombre, Double precio, String descripcion, Integer stock, String imagen, Boolean disponible, Long categoriaId)
            throws EntidadNoEncontradaException, ValidacionException {

        Producto prod = buscarPorId(id);

        // Validacion y seteo condicional de Nombre
        if (nombre != null && !nombre.trim().isEmpty()) {
            prod.setNombre(nombre.trim());
        }

        // Validacion y seteo condicional de Precio
        if (precio != null) {
            if (precio < 0) {
                throw new ValidacionException("El precio no puede ser menor a 0.");
            }
            prod.setPrecio(precio);
        }

        // Validacion y seteo condicional de Stock
        if (stock != null) {
            if (stock < 0) {
                throw new ValidacionException("El stock no puede ser menor a 0.");
            }
            prod.setStock(stock);
        }

        // Campos de texto simples
        if (descripcion != null && !descripcion.trim().isEmpty()) {
            prod.setDescripcion(descripcion.trim());
        }
        if (imagen != null && !imagen.trim().isEmpty()) {
            prod.setImagen(imagen.trim());
        }

        // Estado de disponibilidad
        if (disponible != null) {
            prod.setDisponible(disponible);
        }

        // Cambio de Categoria (Solo si ingreso un ID nuevo)
        if (categoriaId != null) {
            Categoria nuevaCategoria = categoriaService.buscarPorId(categoriaId); //usa las validaciones del metodo y service importado
            prod.setCategoria(nuevaCategoria);
        }
    }

    // HU-PROD-04: Eliminar producto (Soft Delete)
    public void eliminar(Long id) throws EntidadNoEncontradaException {
        Producto prod = buscarPorId(id);
        prod.setEliminado(true);
    }

    // Metodo extra util para cuando el usuario compre: descontar stock
    public void disminuirStock(Long id, int cantidad) throws EntidadNoEncontradaException, ValidacionException {
        Producto prod = buscarPorId(id);
        if (prod.getStock() < cantidad) {
            throw new ValidacionException("Stock insuficiente para el producto: " + prod.getNombre());
        }
        prod.setStock(prod.getStock() - cantidad);
    }

    // Revisa si hay productos activos vinculados a una categoria
    public boolean tieneProductosAsociados(Long categoriaId) {
        for (Producto prod : productos) {
            if (!prod.isEliminado() && prod.getCategoria().getId().equals(categoriaId)) {
                return true; // Encontro al menos uno activo
            }
        }
        return false; // Ninguno asociado
    }
}
