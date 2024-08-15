package estructuras.grafos;

public class NodoVert {
    private Object elemento; //Elemento del nodo
    private NodoAdy primerAdyacente; //Primer arco
    private NodoVert sigVertice; //Siguiente nodo

    public NodoVert(Object elem) {
        this.elemento = elem;
        this.sigVertice = null;
        this.primerAdyacente = null;
    }

    public NodoVert(Object elem, NodoVert sigVert) {
        this.elemento = elem;
        this.sigVertice = sigVert;
        this.primerAdyacente = null;
    }

    public NodoVert(Object elem, NodoVert sigVert, NodoAdy ady) {
        this.elemento = elem;
        this.sigVertice = sigVert;
        this.primerAdyacente = ady;
    }

    public Object getElemento() {
        return this.elemento;
    }

    /*public void setElemento(Object elem) {
        this.elemento = elem;
    }*/

    public NodoVert getSigVertice() {
        return this.sigVertice;
    }

    public void setSigVertice(NodoVert sigVert) {
        this.sigVertice = sigVert;
    }

    public NodoAdy getPrimerAdyacente() {
        return this.primerAdyacente;
    }

    public void setPrimerAdyacente(NodoAdy ady) {
        this.primerAdyacente = ady;
    }

    public String toString() {
        String sigVert = "-", ady = "-";
        
        if (this.sigVertice != null){
            sigVert = sigVert + this.sigVertice.getElemento();
        }
        if (this.primerAdyacente != null){
            ady = ady + this.primerAdyacente.getVertice().getElemento();
        }
        return "["+this.elemento+", SV: "+sigVert+", PA: "+ady;
    }
}