public class Sobre {
    public Sobre(String id, String remitente, String destinatario, double peso, boolean urbano) {
        super(id, remitente, destinatario, peso, urbano);
    }

    @Override
    public double calcularCosto() {
        double costo = 0;
        if (this.urbano) {
            costo = 20;
        } else {
            costo = 35;
        }
        return costo;
    }
}