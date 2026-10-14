public class Vehiculo {
    private String placa;
    private double capacidad;
    private boolean refrigeracion;
    private boolean disponible;

    public Vehiculo(String placa, double capacidad, boolean refrigeracion) {
        if (placa == null || placa.trim().isEmpty()) {
            throw new IllegalArgumentException("La placa no puede estar vacia.");
        }
        if (!Double.isFinite(capacidad) || capacidad <= 0) {
            throw new IllegalArgumentException("La capacidad debe ser positiva y finita.");
        }
        this.placa = placa.trim();
        this.capacidad = capacidad;
        this.refrigeracion = refrigeracion;
        this.disponible = true;
    }

    public String getPlaca() {
        return this.placa;
    }

    public double getCapacidad() {
        return this.capacidad;
    }

    public boolean tieneRefrigeracion() {
        return this.refrigeracion;
    }

    public boolean isDisponible() {
        return this.disponible;
    }

    public void reservar() {
        if (!this.disponible) {
            throw new IllegalArgumentException("El vehiculo ya tiene un despacho sin cerrar.");
        }
        this.disponible = false;
    }

    public void liberar() {
        this.disponible = true;
    }

    @Override
    public String toString() {
        return this.placa + " | Capacidad: " + this.capacidad + " kg | Refrigeracion: "
                + this.refrigeracion + " | Disponible: " + this.disponible;
    }
}
