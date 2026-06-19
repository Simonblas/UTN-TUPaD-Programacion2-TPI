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

    // HU-PROD-03: Editar producto
    public void editar(Long id, String nombre, Double precio, String descripcion, int stock, String imagen, boolean disponible, Long categoriaId)
            throws EntidadNoEncontradaException, ValidacionException {

        Producto prod = buscarPorId(id);

        if (nombre == null || nombre.trim().isEmpty()) {
            throw new ValidacionException("El nombre no puede estar vacio.");
        }
        if (precio < 0 || stock < 0) {
            throw new ValidacionException("Precio y stock deben ser mayores o iguales a 0.");
        }

        Categoria categoria = categoriaService.buscarPorId(categoriaId);//utiliza validaciones dentro de categoriaserviec
        //usa los setters una vez pasada las validaciones
        prod.setNombre(nombre.trim());
        prod.setPrecio(precio);
        prod.setDescripcion(descripcion);
        prod.setStock(stock);
        prod.setImagen(imagen);
        prod.setDisponible(disponible);
        prod.setCategoria(categoria);
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
