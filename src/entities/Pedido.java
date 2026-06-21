/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package entities;

import enums.Estado;
import enums.FormaPago;
import exception.ValidacionException; // Importamos las validaciones
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

    // Constructor lleno adaptado para validar que no se cree sin usuario
    public Pedido(FormaPago formaPago, Usuario usuario) throws ValidacionException {
        this();
        this.formaPago = formaPago;
        setUsuario(usuario); // Usa el setter para forzar la validación de negocio
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
        return null; 
    }

    /**
     * Agrega un detalle al pedido. Si el producto ya existia, acumula; si no,
     * lo crea.
     */
    public void addDetallePedido(int cantidad, Double subtotal, Producto producto) {
        // Reutiliza el metodo de busqueda
        DetallePedido detalleExistente = findDetallePedidoByProducto(producto);

        try {
            if (detalleExistente != null) {
                detalleExistente.setCantidad(detalleExistente.getCantidad() + cantidad);
            } else {
                //  Instancia el nuevo detalle usando el constructor validado que calcula internamente el subtotal
                DetallePedido nuevoDetalle = new DetallePedido(cantidad, producto);
                detalles.add(nuevoDetalle);
            }
        } catch (ValidacionException e) {
            // Captura defensiva; la capa Service ya filtró las cantidades, pero protege el dominio
            System.out.println("Error al estructurar el detalle: " + e.getMessage());
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
 
            detalleAEliminar.setEliminado(true);
        }
 
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

    // Setter validado: No se permite crear o asociar un pedido sin un cliente válido
    public void setUsuario(Usuario usuario) throws ValidacionException {
        if (usuario == null) {
            throw new ValidacionException("Regla de negocio: No se puede generar un pedido sin un usuario asociado.");
        }
        this.usuario = usuario;
    }

    public List<DetallePedido> getDetalles() {
        return detalles;
    }

    @Override
    public String toString() {
        String clienteNombre = (usuario != null) ? (usuario.getNombre() + " " + usuario.getApellido()) : "Sin Cliente";
        // Encabezado del pedido
        String resultado = String.format("Pedido ID: %d | Fecha: %s | Cliente: %s | Estado: %s | Pago: %s | Total: $%.2f\n",
                getId(), fecha, clienteNombre, estado, formaPago, total);

        for (DetallePedido d : detalles) {
            if (!d.isEliminado()) {
                resultado += d.toString() + "\n";
            }
        }

        return resultado.trim();
    }
}
        return resultado.trim();
    }
}
