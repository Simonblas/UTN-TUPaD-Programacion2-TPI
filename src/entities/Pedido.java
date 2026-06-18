/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package entities;

import enums.Estado;
import enums.FormaPago;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author simob
 */
public class Pedido extends Base implements Calculable {

    private LocalDate fecha;
    private Estado estado;
    private Double total;
    private FormaPago formaPago;
    private Usuario usuario;
    private List<DetallePedido> detalles;

    public Pedido() {
        super();
        this.fecha = LocalDate.now();
        this.estado = Estado.PENDIENTE;
        this.total = 0.0;
        this.detalles = new ArrayList<>();
    }

    public Pedido(FormaPago formaPago, Usuario usuario) {
        this();
        this.formaPago = formaPago;
        this.usuario = usuario;
    }

    /**
     * Busca un detalle de pedido especifico segun el producto pasado por
     * parametro.
     */
    public DetallePedido findDetallePedidoByProducto(Producto producto) {
        for (DetallePedido detalle : detalles) {
            if (detalle.getProducto().getId().equals(producto.getId())) {
                return detalle; // Si lo encuentra, lo retorna inmediatamente
            }
        }
        return null; // Si termina el bucle y no lo encontro, retorna null
    }

    /**
     * Agrega un detalle al pedido. Si el producto ya existia, acumula; si no,
     * lo crea.
     */
    public void addDetallePedido(int cantidad, Double subtotal, Producto producto) {
        // Reutiliza el metodo de busqueda
        DetallePedido detalleExistente = findDetallePedidoByProducto(producto);

        if (detalleExistente != null) {
            // Si ya existe, modifica el objeto que ya esta dentro de la lista
            detalleExistente.setCantidad(detalleExistente.getCantidad() + cantidad);
            detalleExistente.setSubtotal(detalleExistente.getSubtotal() + subtotal);
        } else {
            // Si es un producto nuevo en el pedido, crea el detalle y lo suma
            DetallePedido nuevoDetalle = new DetallePedido();
            nuevoDetalle.setCantidad(cantidad);
            nuevoDetalle.setProducto(producto);
            nuevoDetalle.setSubtotal(subtotal);
            detalles.add(nuevoDetalle);
        }

        // Al final, recalcula el total general usando el metodo de la interfaz
        this.total = calcularTotal();
    }

    /**
     * Cambia el estado del detalle a eliminado en vez de borrarlo físicamente.
     */
    public void deleteDetallePedidoByProducto(Producto producto) {
        DetallePedido detalleAEliminar = findDetallePedidoByProducto(producto);

        if (detalleAEliminar != null) {
            // En vez de detalles.remove(detalleAEliminar), hacemos soft delete:
            detalleAEliminar.setEliminado(true);
        }

        // Recalculamos el total ignorando lo eliminado
        this.total = calcularTotal();
    }

    // implementacion de la interfaz calculable
    @Override
    public Double calcularTotal() {
        Double suma = 0.0;
        for (DetallePedido detalle : detalles) {
            if (!detalle.isEliminado()) {
                suma += detalle.getSubtotal();
            }
        }
        return suma;
    }

    // getters y setters
    public LocalDate getFecha() {
        return fecha;
    }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }

    public Estado getEstado() {
        return estado;
    }

    public void setEstado(Estado estado) {
        this.estado = estado;
    }

    public Double getTotal() {
        return total;
    }

    public void setTotal(Double total) {
        this.total = total;
    }

    public FormaPago getFormaPago() {
        return formaPago;
    }

    public void setFormaPago(FormaPago formaPago) {
        this.formaPago = formaPago;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public List<DetallePedido> getDetalles() {
        return detalles;
    }

    @Override
    public String toString() {
        // Encabezado del pedido
        String resultado = String.format("Pedido ID: %d | Fecha: %s | Cliente: %s | Estado: %s | Pago: %s | Total: $%.2f\n",
                getId(), fecha, usuario.getNombre() + " " + usuario.getApellido(), estado, formaPago, total);

        // Concatena los detalles activos usando un for-each
        for (DetallePedido d : detalles) {
            if (!d.isEliminado()) {
                resultado += d.toString() + "\n";
            }
        }

        return resultado.trim();
    }
}
