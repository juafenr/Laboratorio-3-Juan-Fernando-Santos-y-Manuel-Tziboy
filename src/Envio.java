public abstract class Envio {
    protected String id;
    protected String remitente;
    protected String destinatario;
    protected double peso;
    protected boolean urbano;
    protected String estado;
    protected int intentos;

    public Envio(String id, String remitente, String destinatario, double peso, boolean urbano) {
        this.id = id;
        this.remitente = remitente;
        this.destinatario = destinatario;
        this.peso = peso;
        this.urbano = urbano;
        this.estado = "Bodega";
        this.intentos = 0;
    }

    public abstract double calcularCosto();

    public abstract int calcularTiempoEntrega();
}