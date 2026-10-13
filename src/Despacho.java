import java.util.ArrayList;
public class Despacho {
    private Vehiculo vehiculo;
    private ArrayList<Envio> envios;
    private String estado;

    public Despacho(Vehiculo vehiculo) {
        this.vehiculo = vehiculo;
        this.envios = new ArrayList<>();
        this.estado = "Preparacion";
    }

    public double calcularPesoCargado() {
        double pesoCargado = 0;
        for (Envio e : this.envios) {
            pesoCargado += e.calcularCosto;
        }
        return pesoCargado;
    }
}