public class Paquete extends Envio {
    private double largo;
    private double ancho;
    private double alto;
    private boolean fragil;
    private double valorDeclarado;

    public Paquete(String id, String remitente, String destinatario, double peso, boolean urbano,
            double largo, double ancho, double alto, boolean fragil, double valorDeclarado) {
        super(id, remitente, destinatario, peso, urbano);
        if (!Double.isFinite(largo) || !Double.isFinite(ancho) || !Double.isFinite(alto)
                || largo <= 0 || ancho <= 0 || alto <= 0) {
            throw new IllegalArgumentException("Las dimensiones deben ser positivas y finitas.");
        }
        if (!Double.isFinite(valorDeclarado) || valorDeclarado < 0) {
            throw new IllegalArgumentException("El valor declarado no puede ser negativo ni infinito.");
        }
        this.largo = largo;
        this.ancho = ancho;
        this.alto = alto;
        this.fragil = fragil;
        this.valorDeclarado = valorDeclarado;
        if (!Double.isFinite(this.calcularCosto())) {
            throw new IllegalArgumentException("Las dimensiones, el peso o el valor exceden el rango permitido.");
        }
    }

    public double calcularPesoVolumetrico() {
        double pesoVolumetrico = (largo * ancho * alto) / 5000;
        return pesoVolumetrico;
    }

    public double calcularPesoFacturable() {
        double pesoFacturable = 0;
        double pesoVolumetrico = this.calcularPesoVolumetrico();
        if (pesoVolumetrico >= this.peso) {
            pesoFacturable = pesoVolumetrico;
        } else {
            pesoFacturable = this.peso;
        }
        return pesoFacturable;
    }

    @Override
    public double calcularCosto() {
        double costo = 0;
        double pesoFacturable = Math.ceil(this.calcularPesoFacturable());
        if (this.urbano) {
            costo = 25;
        } else {
            costo = 40;
        }
        costo += 8 * pesoFacturable;
        if (this.fragil) {
            costo += 15 + (this.valorDeclarado * 0.03);
        }
        return costo;
    }

    @Override
    public int calcularTiempoEntrega() {
        int dias = 0;
        if (this.urbano) {
            dias = 2;
        } else {
            dias = 4;
        }
        if (this.fragil) {
            dias++;
        }
        return dias;
    }

    @Override
    public int getMaxIntentos() {
        if (this.fragil) {
            return 2;
        } else {
            return 3;
        }
    }

    @Override
    public String getCategoria() {
        if (this.fragil) {
            return "Paquete fragil";
        } else {
            return "Paquete";
        }
    }

    @Override
    public String toString() {
        String detalle = super.toString() + " | Medidas: " + this.largo + " x " + this.ancho
                + " x " + this.alto + " cm";
        if (this.fragil) {
            detalle += String.format(java.util.Locale.US, " | Valor declarado: Q%.2f", this.valorDeclarado);
        }
        return detalle;
    }
}
