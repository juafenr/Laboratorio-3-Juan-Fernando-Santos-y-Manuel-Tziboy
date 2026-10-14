public class Sobre extends Envio {
    public Sobre(String id, String remitente, String destinatario, double peso, boolean urbano) {
        super(id, remitente, destinatario, peso, urbano);
        if (peso > 0.5) {
            throw new IllegalArgumentException("Un sobre no puede pesar mas de 0.5 kg.");
        }
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

    @Override
    public int calcularTiempoEntrega() {
        if (this.urbano) {
            return 1;
        } else {
            return 2;
        }
    }

    @Override
    public String getCategoria() {
        return "Sobre";
    }
}
