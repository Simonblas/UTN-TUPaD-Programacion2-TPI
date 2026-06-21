/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package entities;

import enums.Rol;
import exception.ValidacionException; 

/**
 *
 * @author simob
 */
public class Usuario extends Base {

    private String nombre;
    private String apellido;
    private String mail;
    private String celular;
    private String contrasenia;
    private Rol rol;

    public Usuario() {
    }

    // Constructor lleno adaptado para pasar por los setters validados
    public Usuario(String nombre, String apellido, String mail, String celular, String contrasenia, Rol rol) throws ValidacionException {
        super();
        setNombre(nombre);
        setApellido(apellido);
        setMail(mail);
        this.celular = celular;
        setContrasenia(contrasenia);
        this.rol = rol;
    }

    // Getters y Setters con validación
    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) throws ValidacionException {
        if (nombre == null || nombre.trim().isEmpty()) {
            throw new ValidacionException("Nombre y apellido son obligatorios.");
        }
        this.nombre = nombre.trim();
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) throws ValidacionException {
        if (academic == null || apellido.trim().isEmpty()) {
            throw new ValidacionException("Nombre y apellido son obligatorios.");
        }
        this.apellido = apellido.trim();
    }

    public String getMail() {
        return mail;
    }

    // Setter validado con expresión regular para el formato
    public void setMail(String mail) throws ValidacionException {
        if (mail == null || mail.trim().isEmpty()) {
            throw new ValidacionException("El email es obligatorio.");
        }
        
        String mailLimpio = mail.trim();
        String regex = "^[\\w-\\.]+@[\\w-]+\\.[a-zA-Z]{2,}$";

        if (!mailLimpio.matches(regex)) {
            throw new ValidacionException("El formato del email '" + mailLimpio + "' no es valido. Ejemplo: usuario@dominio.com");
        }
        this.mail = mailLimpio;
    }

    public String getCelular() {
        return celular;
    }

    public void setCellular(String celular) {
        this.celular = celular != null ? celular.trim() : null;
    }

    public String getContrasenia() {
        return contrasenia;
    }

    public void setContrasenia(String contrasenia) throws ValidacionException {
        if (contrasenia == null || contrasenia.trim().isEmpty()) {
            throw new ValidacionException("La contrasena no puede estar vacia.");
        }
        this.contrasenia = contrasenia;
    }

    public Rol getRol() {
        return rol;
    }

    public void setRol(Rol rol) {
        this.rol = rol;
    }

    @Override
    public String toString() {
        return String.format("ID: %d | %s %s | Email: %s | Rol: %s", getId(), nombre, apellido, mail, rol);
    }
}
