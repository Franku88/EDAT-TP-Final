package sistema;

public class Solicitud {
    private int origen;
    private int destino;
    private String fecha;
    private String tipoDoc;
    private String numeroDoc;
    private double metrosCubicos;
    private int bultos;
    private String domicilioRetiro;
    private String domicilioEntrega;
    private boolean envioPago;

    public Solicitud(int orig, int dest, String fe, String tDoc, String nDoc, double metroscub, int bul, String retiro, String entrega, boolean envioPagado) {
        this.origen = orig;
        this.destino = dest;
        this.fecha = fe;
        this.tipoDoc = tDoc;
        this.numeroDoc = nDoc;
        this.metrosCubicos = metroscub;
        this.bultos = bul;
        this.domicilioRetiro = retiro;
        this.domicilioEntrega = entrega;
        this.envioPago = envioPagado;
    }

    public int getOrigen() {
        return this.origen;
    }

    public void setOrigen(int orig) {
        this.origen = orig;
    }

    public int getDestino() {
        return this.destino;
    }

    public void setDestino(int dest) {
        this.destino = dest;
    }

    public String getFecha() {
        return this.fecha;
    }

    public void setFecha(String fe) {
        this.fecha = fe;
    }

    public String getTipoDoc() {
        return this.tipoDoc;
    }

    public void setTipoDoc(String tDoc) {
        this.tipoDoc = tDoc;
    }

    public String getNumeroDoc() {
        return this.numeroDoc;
    }

    public void setNumeroDoc(String nDoc) {
        this.numeroDoc = nDoc;
    }

    public double getMetrosCubicos() {
        return this.metrosCubicos;
    }

    public void setMetrosCubicos(double metroscub) {
        this.metrosCubicos = metroscub;
    }

    public int getBultos() {
        return this.bultos;
    }

    public void setBultos(int bul) {
        this.bultos = bul;
    }

    public String getDomicilioRetiro() {
        return this.domicilioRetiro;
    }

    public void setDomicilioRetiro(String retiro) {
        this.domicilioRetiro = retiro;
    }

    public String getDomicilioEntrega() {
        return this.domicilioEntrega;
    }

    public void setDomicilioEntrega(String entrega) {
        this.domicilioEntrega = entrega;
    }

    public boolean getEnvioPago() {
        return this.envioPago;
    }

    public void setEnvioPago(boolean enviopagado) {
        this.envioPago = enviopagado;
    }

    public String toString() {
        return "Origen: "+this.origen+" | Destino: "+this.destino+" | Fecha: "+this.fecha+" | Cliente: "+this.tipoDoc+this.numeroDoc+" | Bultos: "+this.bultos+" | Volumen: "+this.metrosCubicos+" m^3";
    }
}
