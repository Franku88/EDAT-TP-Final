package estructuras.grafos;

public class NodoAdy {
    private double etiqueta; //Etiqueta del arco
    private NodoVert vertice; //Destino del arco
    private NodoAdy sigAdyacente; //Siguiente arco

    public NodoAdy(NodoVert nodo, double etiq) {
        this.vertice = nodo;
        this.sigAdyacente = null;
        this.etiqueta = etiq;
    }

    public NodoAdy(NodoVert nodo, NodoAdy ady, double etiq) {
        this.vertice = nodo;
        this.sigAdyacente = ady;
        this.etiqueta = etiq;
    }

    public double getEtiqueta() {
        return this.etiqueta;
    }  

    /*public void setEtiqueta(double etiqueta) {
        this.etiqueta = etiqueta;
    }*/

    public NodoVert getVertice() {
        return this.vertice;
    }

    public void setVertice(NodoVert vertice) {
        this.vertice = vertice;
    }

    public NodoAdy getSigAdyacente() {
        return this.sigAdyacente;
    }

    public void setSigAdyacente(NodoAdy sigAdyacente) {
        this.sigAdyacente = sigAdyacente;
    }
}