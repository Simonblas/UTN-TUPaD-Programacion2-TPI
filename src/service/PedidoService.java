/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package service;

import enums.FormaPago;
import exception.EntidadNoEncontradaException;
import exception.ValidacionException;

import java.util.ArrayList;
import java.util.List;

import entities.Pedido;
import entities.Usuario;
import entities.Producto;
import enums.Estado;

/**
 * @author simob
 */
public class PedidoService {

    // Almacenamiento en memoria compartido, el final bloquea la referencia de la lista pero deja manipularla
    private static final List<Pedido> pedidos = new ArrayList<>();
    private static Long contadorId = 1L;//la L indica que es un long y java pueda compilar

    private final UsuarioService usuarioService = new UsuarioService();
    private final ProductoService productoService = new ProductoService();

    // HU-PED-02: Crear pedido con validacion atomica de Stock
    public Pedido crear(Long usuarioId, FormaPago formaPago, List<Long> productosIds, List<Integer> cantidades)
            throws EntidadNoEncontradaException, ValidacionException {

        // Validar usuario (Si esta eliminado, buscarPorId ya lanza la excepcion)
        Usuario usuario = usuarioService.buscarPorId(usuarioId);

        if (productosIds == null || productosIds.isEmpty() || productosIds.size() != cantidades.size()) {
            throw new ValidacionException("El pedido debe contener al menos un producto con su cantidad correspondiente.");
        }

        // primer pasada: validar que todos los productos existan y tengan stock suficiente
        // Hace esto antes de modificar algo para que si uno falla, no rompa el stock de los anteriores.
        for (int i = 0; i < productosIds.size(); i++) {
            Long prodId = productosIds.get(i);
            int cantidad = cantidades.get(i);

            if (cantidad <= 0) {
                throw new ValidacionException("La cantidad de cada producto debe ser mayor a 0.");
            }

            Producto prod = productoService.buscarPorId(prodId); // Valida existencia y que no este eliminado
            if (prod.getStock() < cantidad) {
                throw new ValidacionException("Stock insuficiente para '" + prod.getNombre()
                        + "'. Stock actual: " + prod.getStock() + ", solicitado: " + cantidad);
            }
        }

        // segunda pasada: Si esta todo bien, crea el objeto temporal y procesa la compra
        Pedido nuevoPedido = new Pedido(formaPago, usuario);

        for (int i = 0; i < productosIds.size(); i++) {
            Long prodId = productosIds.get(i);
            int cantidad = cantidades.get(i);

            Producto prod = productoService.buscarPorId(prodId);

            // Descuenta el stock fisicamente
            productoService.disminuirStock(prodId, cantidad);

            // Calcula el subtotal
            Double subtotal = cantidad * prod.getPrecio();

            
            nuevoPedido.addDetallePedido(cantidad, subtotal, prod);
        }

        // Guardado definitivo en la coleccion de memoria
        nuevoPedido.setId(contadorId++);
        pedidos.add(nuevoPedido);

        return nuevoPedido;
    }

    // HU-PED-01: Listar pedidos activos
    public List<Pedido> listarActivos() {
        List<Pedido> activos = new ArrayList<>();
        for (Pedido p : pedidos) {
            if (!p.isEliminado()) {
                activos.add(p);
            }
        }
        return activos;
    }

    // HU-PED-01 (Opcional): Filtrar historial de pedidos por Usuario (incluso si el usuario fue eliminado)
    public List<Pedido> listarPorUsuario(Long usuarioId) {
        List<Pedido> historial = new ArrayList<>();
        for (Pedido p : pedidos) {
            // Compara el ID del objeto usuario guardado dentro del pedido
            if (!p.isEliminado() && p.getUsuario().getId().equals(usuarioId)) {
                historial.add(p);
            }
        }
        return historial;
    }

    // Buscar por ID
    public Pedido buscarPorId(Long id) throws EntidadNoEncontradaException {
        if (id == null) {
            throw new EntidadNoEncontradaException("El ID del pedido no puede ser nulo.");
        }
        for (Pedido p : pedidos) {
            if (p.getId().equals(id) && !p.isEliminado()) {
                return p;
            }
        }
        throw new EntidadNoEncontradaException("Pedido con ID " + id + " no encontrado o fue eliminado.");
    }

    // HU-PED-03: Actualizar estado o forma de pago del pedido
    public void actualizarEstadoYPago(Long id, Estado nuevoEstado, FormaPago nuevaFormaPago)
            throws EntidadNoEncontradaException {

        Pedido p = buscarPorId(id);//usa la logica dentro de buscarporid

        if (nuevoEstado != null) {
            p.setEstado(nuevoEstado);
        }
        if (nuevaFormaPago != null) {
            p.setFormaPago(nuevaFormaPago);
        }
    }

    // HU-PED-04: Eliminar pedido
    public void eliminar(Long id) throws EntidadNoEncontradaException {
        Pedido p = buscarPorId(id);
        p.setEliminado(true);

        // se marcan como eliminados tambien los detalees del pedido a eliminar
        for (int i = 0; i < p.getDetalles().size(); i++) {
            p.getDetalles().get(i).setEliminado(true);
        }
    }
}
