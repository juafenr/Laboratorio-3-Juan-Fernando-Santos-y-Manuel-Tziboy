public class Refrigerada extends Envio {
    private double temperatura;

    public Refrigerada(String id, String remitente, String destinatario, double peso,
            boolean urbano, double temperatura) {
        super(id, remitente, destinatario, peso, urbano);
        if (!urbano) {
            throw new IllegalArgumentException("La carga refrigerada solo acepta destino urbano.");
        }
        if (!Double.isFinite(temperatura) || temperatura < -25 || temperatura > 8) {
            throw new IllegalArgumentException("La temperatura debe estar entre -25 y 8 grados Celsius.");
        }
        this.temperatura = temperatura;
        if (!Double.isFinite(this.calcularCosto())) {
            throw new IllegalArgumentException("El peso excede el rango permitido.");
        }
    }

    @Override
    public double calcularCosto() {
        double costo = 0;
        costo = 25 + 60 + (12 * this.peso);
        return costo;
    }

    @Override
    public int calcularTiempoEntrega() {
        return 1;
    }

    @Override
    public int getMaxIntentos() {
        return 1;
    }

    @Override
    public String getCategoria() {
        return "Carga refrigerada";
    }

    @Override
    public boolean esCompatibleCon(Vehiculo vehiculo) {
        return vehiculo != null && vehiculo.tieneRefrigeracion();
    }

    @Override
    public String toString() {
        return super.toString() + " | Temperatura: " + this.temperatura + " C";
    }
}
