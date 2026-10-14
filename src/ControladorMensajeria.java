import java.util.ArrayList;
import java.util.Locale;
import java.util.NoSuchElementException;

public class ControladorMensajeria {
    private VistaMensajeria vista;
    private ArrayList<Envio> envios;
    private ArrayList<Vehiculo> vehiculos;
    private ArrayList<Despacho> despachos;

    public ControladorMensajeria() {
        this.vista = new VistaMensajeria();
        this.envios = new ArrayList<>();
        this.vehiculos = new ArrayList<>();
        this.despachos = new ArrayList<>();
        this.cargarDatosIniciales();
    }

    private void cargarDatosIniciales() {
        // Estos envios comienzan en bodega y no generan ingresos iniciales.
        this.envios.add(new Sobre("S1", "Ana", "Luis", 0.3, true));
        this.envios.add(new Sobre("S2", "Juan", "Maria", 0.5, false));
        this.envios.add(new Paquete("P1", "Carlos", "Elena", 3.2, true, 40, 30, 20, false, 0));
        this.envios.add(new Paquete("P2", "Sofia", "Pedro", 2.3, false, 10, 10, 10, false, 0));
        this.envios.add(new Paquete("F1", "Manuel", "Lucia", 3.2, true, 40, 30, 20, true, 1200));
        this.envios.add(new Paquete("F2", "Diego", "Sara", 1.2, false, 10, 10, 10, true, 500));
        this.envios.add(new Refrigerada("R1", "Julia", "Pablo", 2.5, true, -25));
        this.envios.add(new Refrigerada("R2", "Mario", "Laura", 5, true, 8));
        this.vehiculos.add(new Vehiculo("P001ABC", 5, false));
        this.vehiculos.add(new Vehiculo("P002ABC", 25, false));
        this.vehiculos.add(new Vehiculo("C003ABC", 50, true));
    }

    public void iniciar() {
        boolean continuar = true;
        while (continuar) {
            try {
                int opcion = this.vista.mostrarMenu();
                if (opcion == 0) {
                    continuar = false;
                } else {
                    this.ejecutarOpcion(opcion);
                }
            } catch (IllegalArgumentException e) {
                this.vista.mostrarError(e.getMessage());
            } catch (NoSuchElementException e) {
                continuar = false;
            }
        }
        this.vista.mostrarMensaje("Programa finalizado.");
    }

    private void ejecutarOpcion(int opcion) {
        switch (opcion) {
            case 1:
                this.cotizarEnvio(this.solicitarDatosEnvio());
                break;
            case 2:
                Envio envio = this.solicitarDatosEnvio();
                this.registrarEnvio(envio);
                this.vista.mostrarMensaje("Envio registrado en bodega. Cobro realizado:");
                this.vista.mostrarCotizacion(
                        envio.getIngresoRegistro(),
                        envio.calcularTiempoEntrega());
                break;
            case 3:
                String placa = this.vista.leerTexto("Placa: ");
                double capacidad = this.vista.leerDouble("Capacidad en kg: ");
                boolean refrigeracion = this.vista.leerBoolean("Tiene refrigeracion");
                this.registrarVehiculo(new Vehiculo(placa, capacidad, refrigeracion));
                this.vista.mostrarMensaje("Vehiculo registrado.");
                break;
            case 4:
                this.consultarVehiculos();
                this.crearDespacho(this.vista.leerTexto("Placa del vehiculo: "));
                this.vista.mostrarMensaje("Despacho creado: " + this.despachos.size());
                break;
            case 5:
                this.vista.mostrarDespachos(this.despachos);
                this.asignarEnvio(this.vista.leerEntero("Numero de despacho: "),
                        this.vista.leerTexto("Codigo del envio: "));
                this.vista.mostrarMensaje("Envio asignado.");
                break;
            case 6:
                this.vista.mostrarDespachos(this.despachos);
                this.retirarEnvio(this.vista.leerEntero("Numero de despacho: "),
                        this.vista.leerTexto("Codigo del envio: "));
                this.vista.mostrarMensaje("Envio retirado; regreso a bodega sin cobros.");
                break;
            case 7:
                this.vista.mostrarDespachos(this.despachos);
                this.salirDespacho(this.vista.leerEntero("Numero de despacho: "));
                this.vista.mostrarMensaje("Despacho en ruta. Se registraron los intentos y cargos correspondientes.");
                break;
            case 8:
                this.vista.mostrarDespachos(this.despachos);
                int indice = this.vista.leerEntero("Numero de despacho: ");
                String id = this.vista.leerTexto("Codigo del envio: ");
                boolean entregado = this.vista.leerBoolean("El envio fue entregado");
                this.registrarResultado(indice, id, entregado);
                this.vista.mostrarMensaje(
                        "Resultado registrado. Envio: " + this.buscarEnvio(id).getEstado()
                                + " | Despacho: " + this.buscarDespacho(indice).getEstado());
                break;
            case 9:
                this.consultarEnvios();
                break;
            case 10:
                this.consultarVehiculos();
                break;
            case 11:
                this.vista.mostrarDespachos(this.despachos);
                break;
            case 12:
                this.vista.mostrarReporte(this.generarReporteEnvios());
                break;
            case 13:
                this.vista.mostrarReporte(this.generarReporteIngresos());
                break;
            case 14:
                this.vista.mostrarReporte(this.generarReporteDespachos());
                break;
            default:
                this.vista.mostrarError("La opcion no existe.");
        }
    }

    private Envio solicitarDatosEnvio() {
        this.vista.mostrarMensaje("1. Sobre | 2. Paquete | 3. Paquete fragil | 4. Carga refrigerada");
        int categoria = this.vista.leerEntero("Categoria: ");
        if (categoria < 1 || categoria > 4) {
            throw new IllegalArgumentException("La categoria no existe.");
        }
        String id = this.vista.leerTexto("Codigo: ");
        String remitente = this.vista.leerTexto("Remitente: ");
        String destinatario = this.vista.leerTexto("Destinatario: ");
        double peso = this.vista.leerDouble("Peso real en kg: ");
        boolean urbano = this.vista.leerBoolean("Destino urbano (n = departamental)");
        if (categoria == 1) {
            return new Sobre(id, remitente, destinatario, peso, urbano);
        } else if (categoria == 2 || categoria == 3) {
            double largo = this.vista.leerDouble("Largo en cm: ");
            double ancho = this.vista.leerDouble("Ancho en cm: ");
            double alto = this.vista.leerDouble("Alto en cm: ");
            boolean fragil = categoria == 3;
            double valorDeclarado = 0;
            if (fragil) {
                valorDeclarado = this.vista.leerDouble("Valor declarado en quetzales: ");
            }
            return new Paquete(
                    id, remitente, destinatario, peso, urbano,
                    largo, ancho, alto, fragil, valorDeclarado);
        } else {
            double temperatura = this.vista.leerDouble("Temperatura en grados Celsius: ");
            return new Refrigerada(id, remitente, destinatario, peso, urbano, temperatura);
        }
    }

    public void cotizarEnvio(Envio envio) {
        if (envio == null) {
            throw new IllegalArgumentException("Debe indicar un envio.");
        }
        this.vista.mostrarCotizacion(envio.calcularCosto(), envio.calcularTiempoEntrega());
    }

    public void registrarEnvio(Envio envio) {
        if (envio == null) {
            throw new IllegalArgumentException("Debe indicar un envio.");
        }
        for (Envio e : this.envios) {
            if (e.getId().equalsIgnoreCase(envio.getId())) {
                throw new IllegalArgumentException("Ya existe un envio con ese codigo.");
            }
        }
        envio.registrarCobroInicial();
        this.envios.add(envio);
    }

    public void registrarVehiculo(Vehiculo vehiculo) {
        if (vehiculo == null) {
            throw new IllegalArgumentException("Debe indicar un vehiculo.");
        }
        for (Vehiculo v : this.vehiculos) {
            if (v.getPlaca().equalsIgnoreCase(vehiculo.getPlaca())) {
                throw new IllegalArgumentException("Ya existe un vehiculo con esa placa.");
            }
        }
        this.vehiculos.add(vehiculo);
    }

    public Despacho crearDespacho(String placa) {
        Vehiculo vehiculo = this.buscarVehiculo(placa);
        Despacho despacho = new Despacho(vehiculo);
        this.despachos.add(despacho);
        return despacho;
    }

    public void asignarEnvio(int indiceDespacho, String idEnvio) {
        this.buscarDespacho(indiceDespacho).agregarEnvio(this.buscarEnvio(idEnvio));
    }

    public void retirarEnvio(int indiceDespacho, String idEnvio) {
        this.buscarDespacho(indiceDespacho).retirarEnvio(idEnvio);
    }

    public void salirDespacho(int indiceDespacho) {
        this.buscarDespacho(indiceDespacho).salir();
    }

    public void registrarResultado(int indiceDespacho, String idEnvio, boolean entregado) {
        this.buscarDespacho(indiceDespacho).registrarResultado(idEnvio, entregado);
    }

    private Envio buscarEnvio(String id) {
        if (id != null) {
            for (Envio e : this.envios) {
                if (e.getId().equalsIgnoreCase(id.trim())) {
                    return e;
                }
            }
        }
        throw new IllegalArgumentException("No existe un envio con ese codigo.");
    }

    private Vehiculo buscarVehiculo(String placa) {
        if (placa != null) {
            for (Vehiculo v : this.vehiculos) {
                if (v.getPlaca().equalsIgnoreCase(placa.trim())) {
                    return v;
                }
            }
        }
        throw new IllegalArgumentException("No existe un vehiculo con esa placa.");
    }

    private Despacho buscarDespacho(int indice) {
        // En el menu los despachos se numeran desde 1.
        if (indice < 1 || indice > this.despachos.size()) {
            throw new IllegalArgumentException("El numero de despacho no existe.");
        }
        return this.despachos.get(indice - 1);
    }

    public void consultarEnvios() {
        this.vista.mostrarEnvios(this.envios);
    }

    public void consultarVehiculos() {
        this.vista.mostrarVehiculos(this.vehiculos);
    }

    private ArrayList<String> obtenerCategorias() {
        ArrayList<String> categorias = new ArrayList<>();
        for (Envio e : this.envios) {
            if (!categorias.contains(e.getCategoria())) {
                categorias.add(e.getCategoria());
            }
        }
        return categorias;
    }

    public String generarReporteEnvios() {
        String reporte = "----- Envios por categoria y estado -----\n";
        String[] estados = {
                "Bodega", "Asignado", "En ruta", "Entregado", "Devuelto al remitente"
        };
        for (String categoria : this.obtenerCategorias()) {
            int total = 0;
            reporte += categoria + ":\n";
            for (String estado : estados) {
                int cantidad = 0;
                for (Envio e : this.envios) {
                    if (e.getCategoria().equals(categoria) && e.getEstado().equals(estado)) {
                        cantidad++;
                    }
                }
                total += cantidad;
                reporte += "  " + estado + ": " + cantidad + "\n";
            }
            reporte += "  Total: " + total + "\n";
        }
        reporte += "Total de envios: " + this.envios.size() + "\n";
        for (String estado : estados) {
            int cantidad = 0;
            for (Envio e : this.envios) {
                if (e.getEstado().equals(estado)) {
                    cantidad++;
                }
            }
            reporte += "Total " + estado + ": " + cantidad + "\n";
        }
        return reporte;
    }

    public String generarReporteIngresos() {
        double registros = 0;
        double reintentos = 0;
        String reporte = "----- Ingresos -----\n";
        for (String categoria : this.obtenerCategorias()) {
            double registroCategoria = 0;
            double reintentoCategoria = 0;
            for (Envio e : this.envios) {
                if (e.getCategoria().equals(categoria)) {
                    registroCategoria += e.getIngresoRegistro();
                    reintentoCategoria += e.getIngresoReintentos();
                }
            }
            registros += registroCategoria;
            reintentos += reintentoCategoria;
            reporte += String.format(
                    Locale.US, "%s: registro Q%.2f | reintentos Q%.2f | total Q%.2f%n",
                    categoria, registroCategoria, reintentoCategoria, registroCategoria + reintentoCategoria);
        }
        reporte += String.format(Locale.US,
                "Total por registro: Q%.2f%nTotal por reintentos: Q%.2f%nIngreso total: Q%.2f%n",
                registros, reintentos, registros + reintentos);
        return reporte;
    }

    public String generarReporteDespachos() {
        String reporte = "----- Despachos no cerrados -----\n";
        int cantidad = 0;
        for (int i = 0; i < this.despachos.size(); i++) {
            Despacho despacho = this.despachos.get(i);
            if (!despacho.getEstado().equals("Cerrado")) {
                reporte += "Despacho " + (i + 1) + ": " + despacho + "\n";
                cantidad++;
            }
        }
        if (cantidad == 0) {
            reporte += "No hay despachos abiertos.\n";
        }
        return reporte;
    }
}
