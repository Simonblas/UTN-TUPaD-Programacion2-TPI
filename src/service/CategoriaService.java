/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package service;

import java.util.ArrayList;
import java.util.List;
import entities.Categoria;
import exception.EntidadNoEncontradaException;
import exception.ValidacionException;

/**
 *
 * @author simob
 */
public class CategoriaService {

    // Almacenamiento en memoria compartido, el final bloquea la referencia de la lista pero deja manipularla
    private static final List<Categoria> categorias = new ArrayList<>();
    private static Long contadorId = 1L; //la L indica que es un long y java pueda compilar
 
    // HU-CAT-02: Crear categoria con validacion de nombre unico
    public Categoria crear(String nombre, String descripcion) throws ValidacionException {
        if (nombre == null || nombre.trim().isEmpty()) {
            throw new ValidacionException("El nombre de la categoria no puede estar vacio.");
        }

        // Validacion de unicidad
        for (Categoria cat : categorias) {
            if (!cat.isEliminado() && cat.getNombre().equalsIgnoreCase(nombre.trim())) {
                throw new ValidacionException("Ya existe una categoria activa con el nombre: " + nombre);
            }
        }

        Categoria nueva = new Categoria(nombre.trim(), descripcion);
        nueva.setId(contadorId++);
        categorias.add(nueva);
        return nueva;
    }

    // HU-CAT-01: Listar solo las categorias que NO estan eliminadas
    public List<Categoria> listarActivas() {
        List<Categoria> activas = new ArrayList<>();
        for (Categoria cat : categorias) {
            if (!cat.isEliminado()) {
                activas.add(cat);
            }
        }
        return activas;
    }

// Buscar por ID (Uso interno y para validaciones)
    public Categoria buscarPorId(Long id) throws EntidadNoEncontradaException {
        // Validacion defensiva
        if (id == null) {
            throw new EntidadNoEncontradaException("El ID provisto no puede ser nulo.");
        }

        for (Categoria cat : categorias) {
            if (cat.getId().equals(id) && !cat.isEliminado()) {
                return cat;
            }
        }
        throw new EntidadNoEncontradaException("Categoria con ID " + id + " no encontrada o fue eliminada.");
    }

    // HU-CAT-03: Editar categoria
    public void editar(Long id, String nuevoNombre, String nuevaDescripcion) throws EntidadNoEncontradaException, ValidacionException {
        Categoria cat = buscarPorId(id);

        if (nuevoNombre == null || nuevoNombre.trim().isEmpty()) {
            throw new ValidacionException("El nombre no puede estar vacio.");
        }

        // Validar que el nuevo nombre no lo tenga otra categoria
        for (Categoria c : categorias) {
            if (!c.getId().equals(id) && !c.isEliminado() && c.getNombre().equalsIgnoreCase(nuevoNombre.trim())) {
                throw new ValidacionException("Ya existe otra categoria con el nombre: " + nuevoNombre);
            }
        }

        cat.setNombre(nuevoNombre.trim());
        cat.setDescripcion(nuevaDescripcion);
    }

// HU-CAT-04: Eliminar categoria con validacion de integridad
    public void eliminar(Long id, ProductoService productoService) throws EntidadNoEncontradaException, ValidacionException {
        Categoria cat = buscarPorId(id);

        // Aplica la regla de negocio estricta
        if (productoService.tieneProductosAsociados(id)) {
            throw new ValidacionException("No se puede eliminar la categoria '" + cat.getNombre()
                    + "' porque tiene productos activos asociados. Elimine o reubique los productos primero.");
        }

        cat.setEliminado(true); // Soft delete seguro
    }
}
