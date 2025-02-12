package estructuras.diccionario;
@SuppressWarnings("rawtypes")

public class NodoAVLDicc {

    private Comparable clave;
    private Object dato;
    private NodoAVLDicc izquierdo;
    private NodoAVLDicc derecho;
    private int altura;

    public NodoAVLDicc(Comparable clave, Object dato) {
        this.clave = clave;
        this.dato = dato;
        this.izquierdo = null;
        this.derecho = null;
        this.altura = 0;
    }

    public NodoAVLDicc(Comparable clave, Object dato, NodoAVLDicc izquierdo, NodoAVLDicc derecho) {
        this.clave = clave;
        this.dato = dato;
        this.izquierdo = izquierdo;
        this.derecho = derecho;
        this.recalcularAltura();
    }

    public Comparable getClave() {
        return this.clave;
    }

    /*public void setClave(Comparable clave) {
        this.clave = clave;
    }*/

    public Object getDato() {
        return this.dato;
    }

    public void setDato(Object dato) {
        this.dato = dato;
    }

    public NodoAVLDicc getIzquierdo(){
        return this.izquierdo;
    }
    
    public void setIzquierdo(NodoAVLDicc izquierdo){
        this.izquierdo = izquierdo;
    }

    public NodoAVLDicc getDerecho(){
        return this.derecho;
    }

    public void setDerecho(NodoAVLDicc derecho){
        this.derecho = derecho;
    }    

    public int getAltura(){
        return this.altura;
    }

    public void setAltura(int altura) {
        this.altura = altura;
    }

    public int balance() {
        //Modulo que calcula el balance de un nodoAVLDicc
        int altIzq = -1; //Altura de null es -1
        int altDer = -1;
        if (this.izquierdo != null) {
            altIzq = this.izquierdo.getAltura();
        }
        if (this.derecho != null) {
            altDer = this.derecho.getAltura();
        }
        return (altIzq - altDer); 
    }

    public void recalcularAltura() {
        //Realiza el calculo de la altura de un nodo en base a la altura de sus hijos
        int altIzq = -1; //Altura de null es -1
        int altDer = -1;
        if (this.izquierdo != null) { //Si tiene HI calculo su altura
            altIzq = (this.izquierdo).altura;
        }
        if (this.derecho != null) { //Si tiene HD calculo su altura
            altDer = (this.derecho).altura;
        }
        this.altura = Math.max(altIzq, altDer) + 1;
    }

}
