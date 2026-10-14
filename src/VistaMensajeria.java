import java.util.ArrayList;
import java.util.Locale;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class VistaMensajeria {
    private Scanner sc = new Scanner(System.in);

    public int mostrarMenu() {
        System.out.println("\n----- EnviaGT -----");
        System.out.println("1. Cotizar envio");
        System.out.println("2. Registrar envio");
        System.out.println("3. Registrar vehiculo");
        System.out.println("4. Crear despacho");
        System.out.println("5. Asignar envio a despacho");
        System.out.println("6. Retirar envio de despacho");
        System.out.println("7. Dar salida a despacho");
        System.out.println("8. Registrar resultado de entrega");
        System.out.println("9. Consultar envios");
        System.out.println("10. Consultar vehiculos");
        System.out.println("11. Consultar despachos");
        System.out.println("12. Reporte de envios");
        System.out.println("13. Reporte de ingresos");
        System.out.println("14. Reporte de despachos abiertos");
        System.out.println("0. Salir");
        return this.leerEntero("Seleccione una opcion: ");
    }

    public String leerTexto(String mensaje) {
        while (true) {
            System.out.print(mensaje);
            if (!this.sc.hasNextLine()) {
                throw new NoSuchElementException("Entrada finalizada.");
            }
            String texto = this.sc.nextLine().trim();
            if (!texto.isEmpty()) {
                return texto;
            }
            this.mostrarError("El dato no puede estar vacio.");
        }
    }

    public int leerEntero(String mensaje) {
        while (true) {
            String texto = this.leerTexto(mensaje);
            try {
                return Integer.parseInt(texto);
            } catch (NumberFormatException e) {
                this.mostrarError("Ingrese un numero entero valido.");
            }
        }
    }

    public double leerDouble(String mensaje) {
        while (true) {
            String texto = this.leerTexto(mensaje);
            try {
                double numero = Double.parseDouble(texto);
                if (Double.isFinite(numero)) {
                    return numero;
                }
                this.mostrarError("El numero debe ser finito.");
            } catch (NumberFormatException e) {
                this.mostrarError("Ingrese un numero valido; use punto para los decimales.");
            }
        }
    }

    public boolean leerBoolean(String mensaje) {
        while (true) {
            String texto = this.leerTexto(mensaje + " (s/n): ");
            if (texto.equalsIgnoreCase("s") || texto.equalsIgnoreCase("si") || texto.equalsIgnoreCase("sí")) {
                return true;
            }
            if (texto.equalsIgnoreCase("n") || texto.equalsIgnoreCase("no")) {
                return false;
            }
            this.mostrarError("Responda s o n.");
        }
    }

    public void mostrarMensaje(String mensaje) {
        System.out.println(mensaje);
    }

    public void mostrarError(String mensaje) {
        System.out.println("Error: " + mensaje);
    }

    public void mostrarReporte(String reporte) {
        System.out.println(reporte);
    }

    public void mostrarCotizacion(double costo, int dias) {
        System.out.printf(
                Locale.US,
                "Costo: Q%.2f | Tiempo estimado: %d dia(s) habil(es).%n",
                costo,
                dias);
    }

    public void mostrarEnvios(ArrayList<Envio> envios) {
        if (envios.isEmpty()) {
            this.mostrarMensaje("No hay envios registrados.");
        }
        for (Envio e : envios) {
            this.mostrarMensaje(e.toString());
        }
    }

    public void mostrarVehiculos(ArrayList<Vehiculo> vehiculos) {
        if (vehiculos.isEmpty()) {
            this.mostrarMensaje("No hay vehiculos registrados.");
        }
        for (Vehiculo v : vehiculos) {
            this.mostrarMensaje(v.toString());
        }
    }

    public void mostrarDespachos(ArrayList<Despacho> despachos) {
        if (despachos.isEmpty()) {
            this.mostrarMensaje("No hay despachos registrados.");
        }
        for (int i = 0; i < despachos.size(); i++) {
            this.mostrarMensaje("Despacho " + (i + 1) + ": " + despachos.get(i));
            for (Envio e : despachos.get(i).getEnvios()) {
                this.mostrarMensaje("  Envio: " + e.getId() + " | Estado actual: " + e.getEstado());
            }
        }
    }
}
