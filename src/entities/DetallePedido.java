/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package entities;

import exception.ValidacionException; // Importamos tu excepción propia

/**
 *
 * @author simob
 */
public class DetallePedido extends Base {

    private int cantidad;
    private Double subtotal;
    private Producto producto;

    public DetallePedido() {
    }

    // Constructor lleno adaptado con throws y usando los setters para validar
    public DetallePedido(int cantidad, Producto producto) throws ValidacionException {
        super();
        setProducto(producto); // Setear primero el producto es clave para calcular el subtotal inicial
        setCantidad(cantidad);
    }

    public int getCantidad() {
        return cantidad;
    }

    // Setter con validación: cantidad > 0 y recálculo automático de subtotal
    public void setCantidad(int cantidad) throws ValidacionException {
        if (cantidad <= 0) {
            throw new ValidacionException("La cantidad de cada producto debe ser mayor a 0.");
        }
        this.cantidad = cantidad;
        recalcularSubtotal();
    }

    public Double getSubtotal() {
        return subtotal;
    }

    // Eliminamos el setter público de subtotal para proteger la integridad del dato
    private void recalcularSubtotal() {
        if (this.producto != null) {
            this.subtotal = this.cantidad * this.producto.getPrecio();
        } else {
            this.subtotal = 0.0;
        }
    }

    public Producto getProducto() {
        return producto;
    }

    // Setter con validación de no nulo y recálculo automático de subtotal
    public void setProducto(Producto producto) throws ValidacionException {
        if (producto == null) {
            throw new ValidacionException("El producto asociado al detalle no puede ser nulo.");
        }
        this.producto = producto;
        recalcularSubtotal();
    }

    @Override
    public String toString() {
        String prodNombre = (producto != null) ? producto.getNombre() : "Producto no asignado";
        return String.format("   -> %s x %d | Subtotal: $%.2f", prodNombre, cantidad, subtotal);
    }
}
