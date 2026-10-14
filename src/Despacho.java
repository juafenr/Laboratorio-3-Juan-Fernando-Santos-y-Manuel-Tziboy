import java.util.ArrayList;

public class Despacho {
    private Vehiculo vehiculo;
    private ArrayList<Envio> envios;
    private String estado;
    // El resultado pertenece a este despacho, aunque el envio salga despues en
    // otro.
    private ArrayList<String> resultadosRegistrados;

    public Despacho(Vehiculo vehiculo) {
        if (vehiculo == null) {
            throw new IllegalArgumentException("Debe seleccionar un vehiculo existente.");
        }
        vehiculo.reservar();
        this.vehiculo = vehiculo;
        this.envios = new ArrayList<>();
        this.resultadosRegistrados = new ArrayList<>();
        this.estado = "Preparacion";
    }

    public double calcularPesoCargado() {
        double pesoCargado = 0;
        for (Envio e : this.envios) {
            pesoCargado += e.getPeso();
        }
        return pesoCargado;
    }

    public double calcularCapacidadDisponible() {
        return this.vehiculo.getCapacidad() - this.calcularPesoCargado();
    }

    public boolean puedeAgregarEnvio(Envio envio) {
        return envio != null && this.estado.equals("Preparacion") && envio.puedeAsignarse()
                && envio.esCompatibleCon(this.vehiculo)
                && this.calcularPesoCargado() + envio.getPeso() <= this.vehiculo.getCapacidad();
    }

    public void agregarEnvio(Envio envio) {
        if (!this.estado.equals("Preparacion")) {
            throw new IllegalArgumentException("Solo se pueden asignar envios en preparacion.");
        }
        if (envio == null || !envio.puedeAsignarse()) {
            throw new IllegalArgumentException("El envio no esta disponible en bodega.");
        }
        if (!envio.esCompatibleCon(this.vehiculo)) {
            throw new IllegalArgumentException("Este envio requiere un vehiculo refrigerado.");
        }
        if (this.calcularPesoCargado() + envio.getPeso() > this.vehiculo.getCapacidad()) {
            throw new IllegalArgumentException("El peso real excede la capacidad del vehiculo.");
        }
        envio.asignar();
        this.envios.add(envio);
    }

    public void retirarEnvio(String id) {
        if (!this.estado.equals("Preparacion")) {
            throw new IllegalArgumentException("Solo se pueden retirar envios en preparacion.");
        }
        Envio envio = this.buscarEnvio(id);
        envio.retirarAsignacion();
        this.envios.remove(envio);
    }

    private Envio buscarEnvio(String id) {
        for (Envio e : this.envios) {
            if (e.getId().equalsIgnoreCase(id.trim())) {
                return e;
            }
        }
        throw new IllegalArgumentException("El envio no pertenece a este despacho.");
    }

    private void validarSalida() {
        if (!this.estado.equals("Preparacion") || this.envios.isEmpty()) {
            throw new IllegalArgumentException("El despacho debe estar en preparacion y tener envios.");
        }
        if (this.calcularPesoCargado() > this.vehiculo.getCapacidad()) {
            throw new IllegalArgumentException("La carga supera la capacidad.");
        }
        for (Envio e : this.envios) {
            if (!e.getEstado().equals("Asignado") || e.getIntentos() >= e.getMaxIntentos()
                    || !e.esCompatibleCon(this.vehiculo)) {
                throw new IllegalArgumentException("Hay un envio que no puede salir.");
            }
        }
    }

    public void salir() {
        // Se validan todos los envios antes de cambiar estados o cobrar.
        this.validarSalida();
        for (Envio e : this.envios) {
            e.registrarSalida();
        }
        this.estado = "En ruta";
    }

    public void registrarResultado(String id, boolean entregado) {
        if (!this.estado.equals("En ruta")) {
            throw new IllegalArgumentException("El despacho no esta en ruta.");
        }
        Envio envio = this.buscarEnvio(id);
        if (this.resultadosRegistrados.contains(envio.getId())) {
            throw new IllegalArgumentException("Este resultado ya fue registrado en el despacho.");
        }
        envio.registrarResultado(entregado);
        this.resultadosRegistrados.add(envio.getId());
        if (this.todosLosResultadosRegistrados()) {
            this.cerrar();
        }
    }

    private boolean todosLosResultadosRegistrados() {
        return this.resultadosRegistrados.size() == this.envios.size();
    }

    private void cerrar() {
        this.estado = "Cerrado";
        this.vehiculo.liberar();
    }

    public Vehiculo getVehiculo() {
        return this.vehiculo;
    }

    public ArrayList<Envio> getEnvios() {
        return new ArrayList<>(this.envios);
    }

    public String getEstado() {
        return this.estado;
    }

    @Override
    public String toString() {
        return this.vehiculo.getPlaca() + " | " + this.estado + " | Peso cargado: "
                + this.calcularPesoCargado() + " kg | Capacidad total: " + this.vehiculo.getCapacidad()
                + " kg | Capacidad disponible: " + this.calcularCapacidadDisponible()
                + " kg | Resultados: " + this.resultadosRegistrados.size() + "/" + this.envios.size();
    }
}
