/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ui;

import java.util.Scanner;

/**
 *
 * @author simob
 */
public class MenuHelper {
    private static final Scanner scanner = new Scanner(System.in);

    // Lee un entero de forma segura. Si no es un numero, atrapa el error.
    public static int leerEntero(String mensaje) {
        while (true) {
            System.out.print(mensaje);
            try {
                int num = scanner.nextInt();
                scanner.nextLine(); // Limpia el bufer del salto de linea
                return num;
            } catch (Exception e) {
                System.out.println("Error: Debe ingresar un numero entero valido");
                scanner.nextLine(); // Limpia el bufer del texto erroneo
            }
        }
    }

    // Lee un Long de forma segura para los ids (puede devolver null si presiona enter vacio)
    public static Long leerLongOpcional(String mensaje) {
        System.out.print(mensaje);
        String entrada = scanner.nextLine().trim();
        if (entrada.isEmpty()) {
            return null; // El usuario no quiso modificar o ingresar este campo
        }
        try {
            return Long.parseLong(entrada);
        } catch (NumberFormatException e) {
            System.out.println("ID no valido. Se procesara como nulo/vacio.");
            return null;
        }
    }

    // Lee un texto comun
    public static String leerTexto(String mensaje) {
        System.out.print(mensaje);
        return scanner.nextLine();
    }

    // Lee un texto y si esta vacio devuelve null
    public static String leerTextoOpcional(String mensaje) {
        System.out.print(mensaje);
        String entrada = scanner.nextLine().trim();
        return entrada.isEmpty() ? null : entrada;
    }

    // Lee un Double de forma segura
    public static Double leerDoubleOpcional(String mensaje) {
        System.out.print(mensaje);
        String entrada = scanner.nextLine().trim();
        if (entrada.isEmpty()) return null;
        try {
            return Double.parseDouble(entrada);
        } catch (NumberFormatException e) {
            System.out.println("Monto no valido. Se mantendra el valor anterior.");
            return null;
        }
    }
}