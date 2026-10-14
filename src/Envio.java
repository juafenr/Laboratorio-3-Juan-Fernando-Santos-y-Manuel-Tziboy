public abstract class Envio {
    protected String id;
    protected String remitente;
    protected String destinatario;
    protected double peso;
    protected boolean urbano;
    protected String estado;
    protected int intentos;
    private double ingresoRegistro;
    private double ingresoReintentos;
    private boolean cobroRegistrado;

    public Envio(String id, String remitente, String destinatario, double peso, boolean urbano) {
        if (id == null || id.trim().isEmpty() || remitente == null || remitente.trim().isEmpty()
                || destinatario == null || destinatario.trim().isEmpty()) {
            throw new IllegalArgumentException("El codigo, remitente y destinatario no pueden estar vacios.");
        }
        if (!Double.isFinite(peso) || peso <= 0) {
            throw new IllegalArgumentException("El peso debe ser un numero positivo y finito.");
        }
        this.id = id.trim();
        this.remitente = remitente.trim();
        this.destinatario = destinatario.trim();
        this.peso = peso;
        this.urbano = urbano;
        this.estado = "Bodega";
        this.intentos = 0;
        this.ingresoRegistro = 0;
        this.ingresoReintentos = 0;
        this.cobroRegistrado = false;
    }

    public abstract double calcularCosto();

    public abstract int calcularTiempoEntrega();

    public abstract String getCategoria();

    public int getMaxIntentos() {
        return 3;
    }

    public boolean esCompatibleCon(Vehiculo vehiculo) {
        return vehiculo != null;
    }

    public boolean puedeAsignarse() {
        return this.estado.equals("Bodega") && this.intentos < this.getMaxIntentos();
    }

    public void asignar() {
        if (!this.puedeAsignarse()) {
            throw new IllegalArgumentException("El envio no esta en bodega o agoto sus intentos.");
        }
        this.estado = "Asignado";
    }

    public void retirarAsignacion() {
        if (!this.estado.equals("Asignado")) {
            throw new IllegalArgumentException("El envio no esta asignado.");
        }
        this.estado = "Bodega";
    }

    public void registrarCobroInicial() {
        if (this.cobroRegistrado || !this.estado.equals("Bodega") || this.intentos != 0) {
            throw new IllegalArgumentException("No se puede registrar nuevamente el cobro inicial.");
        }
        double costo = this.calcularCosto();
        if (!Double.isFinite(costo) || costo <= 0) {
            throw new IllegalArgumentException("El costo esta fuera de rango.");
        }
        this.ingresoRegistro = costo;
        this.cobroRegistrado = true;
    }

    public void registrarSalida() {
        if (!this.estado.equals("Asignado") || this.intentos >= this.getMaxIntentos()) {
            throw new IllegalArgumentException("El envio no puede salir en este estado.");
        }
        this.intentos++;
        if (this.intentos > 1) {
            this.ingresoReintentos += 15;
        }
        this.estado = "En ruta";
    }

    public void registrarResultado(boolean entregado) {
        if (!this.estado.equals("En ruta")) {
            throw new IllegalArgumentException("El envio no tiene una entrega pendiente.");
        }
        if (entregado) {
            this.estado = "Entregado";
        } else if (this.intentos < this.getMaxIntentos()) {
            this.estado = "Bodega";
        } else {
            this.estado = "Devuelto al remitente";
        }
    }

    public String getId() {
        return this.id;
    }

    public String getRemitente() {
        return this.remitente;
    }

    public String getDestinatario() {
        return this.destinatario;
    }

    public double getPeso() {
        return this.peso;
    }

    public boolean isUrbano() {
        return this.urbano;
    }

    public String getEstado() {
        return this.estado;
    }

    public int getIntentos() {
        return this.intentos;
    }

    public double getIngresoRegistro() {
        return this.ingresoRegistro;
    }

    public double getIngresoReintentos() {
        return this.ingresoReintentos;
    }

    @Override
    public String toString() {
        String destino = "Departamental";
        if (this.urbano) {
            destino = "Urbano";
        }
        return this.id + " | " + this.getCategoria() + " | " + this.remitente + " -> "
                + this.destinatario + " | " + this.peso + " kg | " + destino + " | "
                + this.estado + " | Intentos: " + this.intentos + "/" + this.getMaxIntentos();
    }
}
