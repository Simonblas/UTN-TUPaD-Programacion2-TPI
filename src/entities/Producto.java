package entities;

import exception.ValidacionException; 
/**
 *
 * @author simob
 */
public class Producto extends Base {

    private String nombre;
    private Double precio;
    private String descripcion;
    private int stock;
    private String imagen;
    private boolean disponible;
    private Categoria categoria;

    public Producto() {
    }

    public Producto(String nombre, Double precio, String descripcion, int stock, String imagen, boolean disponible, Categoria categoria) throws ValidacionException {
        super();
        setNombre(nombre); // Usa el setter para validar [cite: 205]
        setPrecio(precio); // Usa el setter para validar [cite: 205]
        this.descripcion = descripcion;
        setStock(stock);   // Usa el setter para validar [cite: 205]
        this.imagen = imagen;
        this.disponible = disponible;
        this.categoria = categoria;
    }

    // Getters y setters con validacion 
    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) throws ValidacionException {
        if (nombre == null || nombre.trim().isEmpty()) {
            throw new ValidacionException("El nombre del producto no puede estar vacio.");
        }
        this.nombre = nombre.trim();
    }

    public Double getPrecio() {
        return precio;
    }

    public void setPrecio(Double precio) throws ValidacionException {
        if (precio == null || precio < 0) {
            throw new ValidacionException("El precio no puede ser menor a 0.");
        }
        this.precio = precio;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) throws ValidacionException {
        if (stock < 0) {
            throw new ValidacionException("El stock no puede ser menor a 0.");
        }
        this.stock = stock;
    }

    public String getImagen() {
        return imagen;
    }

    public void setImagen(String imagen) {
        this.imagen = imagen;
    }

    public boolean isDisponible() {
        return disponible;
    }

    public void setDisponible(boolean disponible) {
        this.disponible = disponible;
    }

    public Categoria getCategoria() {
        return categoria;
    }

    public void setCategoria(Categoria categoria) {
        this.categoria = categoria;
    }

    @Override
    public String toString() {
        String catNombre = (categoria != null) ? categoria.getNombre() : "Sin Categoria";
        return String.format("ID: %d | %s | Precio: $%.2f | Stock: %d | Cat: %s | Disp: %s",
                getId(), nombre, precio, stock, catNombre, disponible ? "SI" : "NO");
    }
}
