public class Paquete {
    private double largo;
    private double ancho;
    private double alto;
    private boolean fragil;
    private double valorDeclarado;

    public Paquete(String id, String remitente, String destinatario, double peso, boolean urbano, double largo, double ancho, double alto, boolean fragil, double valorDeclarado) {
        super(id, remitente, destinatario, peso, urbano);
        this.largo = largo;
        this.ancho = ancho;
        this.alto = alto;
        this.fragil = fragil;
        this.valorDeclarado = valorDeclarado;
    }

    public double calcularPesoVolumetrico() {
        double pesoVolumetrico = (largo * ancho * alto)/5000;
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
        double pesoFacturable = this.calcularPesoFacturable;
        if (this.urbano) {
            costo = 25;
        } else {
            costo = 40;
        }
        costo += (8 * pesoFacturable);

        if (this.fragil) {
            costo += 15 +(this.valorDeclarado * 0.03);
        }

        return costo;
    }
}