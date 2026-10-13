public class Refrigerada {
    private double temperatura;

    public Refrigerada(String id, String remitente, String destinatario, double peso, boolean urbano, double temperatura) {
        super(id, remitente, destinatario, peso, urbano);
        this.temperatura = temperatura;
    }

    @Override
    public double calcularCosto() {
        double costo = 0;
        costo = 25 + 60 + (12 * this.peso);
        return costo;
    }
}