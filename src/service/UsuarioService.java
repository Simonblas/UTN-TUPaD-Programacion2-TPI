/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package service;

import exception.EntidadNoEncontradaException;
import exception.ValidacionException;
import java.util.ArrayList;
import java.util.List;
import entities.Usuario;
import enums.Rol;

/**
 *
 * @author simob
 */
public class UsuarioService {
// Almacenamiento en memoria compartido, el final bloquea la referencia de la lista pero deja manipularla

    private static final List<Usuario> usuarios = new ArrayList<>();
    private static Long contadorId = 1L;//la L indica que es un long y java pueda compilar

    // HU-USR-02: Crear usuario con validacion de mail unico
    public Usuario crear(String nombre, String apellido, String mail, String celular, String contrasenia, Rol rol)
            throws ValidacionException {

        if (nombre == null || nombre.trim().isEmpty() || apellido == null || apellido.trim().isEmpty()) {
            throw new ValidacionException("Nombre y apellido son obligatorios.");
        }
        if (mail == null || mail.trim().isEmpty()) {
            throw new ValidacionException("El email es obligatorio.");
        }
        validarFormatoEmail(mail.trim());
        if (contrasenia == null || contrasenia.trim().isEmpty()) {
            throw new ValidacionException("La contrasena no puede estar vacia.");
        }

        // Validacion de email unico
        for (Usuario usr : usuarios) {
            if (!usr.isEliminado() && usr.getMail().equalsIgnoreCase(mail.trim())) {
                throw new ValidacionException("El email '" + mail + "' ya se encuentra registrado.");
            }
        }

        Usuario nuevo = new Usuario(nombre.trim(), apellido.trim(), mail.trim(), celular, contrasenia, rol);
        nuevo.setId(contadorId++);
        usuarios.add(nuevo);
        return nuevo;
    }

    // HU-USR-01: Listar usuarios activos
    public List<Usuario> listarActivos() {
        List<Usuario> activos = new ArrayList<>();
        for (Usuario usr : usuarios) {
            if (!usr.isEliminado()) {
                activos.add(usr);
            }
        }
        return activos;
    }

    // Buscar por ID
    public Usuario buscarPorId(Long id) throws EntidadNoEncontradaException {
        if (id == null) {
            throw new EntidadNoEncontradaException("El ID del usuario no puede ser nulo.");
        }

        for (Usuario usr : usuarios) {
            if (usr.getId().equals(id) && !usr.isEliminado()) {
                return usr;
            }
        }
        throw new EntidadNoEncontradaException("Usuario con ID " + id + " no encontrado o dado de baja.");
    }
// HU-USR-03: Editar usuario con cambios opcionales y validación estricta de mail

    public void editar(Long id, String nombre, String apellido, String mail, String celular, String contrasenia, Rol rol)
            throws EntidadNoEncontradaException, ValidacionException {

        Usuario usr = buscarPorId(id);

        if (nombre != null && !nombre.trim().isEmpty()) {
            usr.setNombre(nombre.trim());
        }
        if (apellido != null && !apellido.trim().isEmpty()) {
            usr.setApellido(apellido.trim());
        }
        if (celular != null && !celular.trim().isEmpty()) {
            usr.setCellular(celular.trim());
        }
        if (contrasenia != null && !contrasenia.trim().isEmpty()) {
            usr.setContrasenia(contrasenia); // no se usa trim en contrasena por si se desea utilizar espacios
        }
        if (rol != null) {
            usr.setRol(rol);
        }

        // control de mail
        if (mail != null && !mail.trim().isEmpty()) {
            String mailLimpio = mail.trim();

            // Si el usuario efectivamente cambio su mail original, validam todo de nuevo
            if (!usr.getMail().equalsIgnoreCase(mailLimpio)) {
                validarFormatoEmail(mailLimpio); // Valida formato regex

                // Valida unicidad contra el resto del sistema
                for (Usuario u : usuarios) {
                    if (!u.getId().equals(id) && !u.isEliminado() && u.getMail().equalsIgnoreCase(mailLimpio)) {
                        throw new ValidacionException("No se puede actualizar: El email '" + mailLimpio + "' ya esta en uso.");
                    }
                }
                usr.setMail(mailLimpio);
            }
        }
    }

    // HU-USR-04: Eliminar usuario
    public void eliminar(Long id) throws EntidadNoEncontradaException {
        Usuario usr = buscarPorId(id);
        usr.setEliminado(true);
    }

    /**
     * Valida si el formato del email contiene un usuario, un @, un dominio y
     * una extension valida.
     */
    private void validarFormatoEmail(String mail) throws ValidacionException {
        // Expresion regular
        String regex = "^[\\w-\\.]+@[\\w-]+\\.[a-zA-Z]{2,}$";

        if (!mail.matches(regex)) {
            throw new ValidacionException("El formato del email '" + mail + "' no es valido. Ejemplo: usuario@dominio.com");
        }
    }
}
