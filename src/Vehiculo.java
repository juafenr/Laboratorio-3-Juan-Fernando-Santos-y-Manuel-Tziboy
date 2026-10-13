public class Vehiculo {
    private String placa;
    private double capacidad;
    private boolean refrigeracion;
    private boolean disponible;

    public Vehiculo(String placa, double capacidad, boolean refrigeracion) {
        this.placa = placa;
        this.capacidad = capacidad;
        this.refrigeracion = refrigeracion;
        this.disponible = true;
    }
}