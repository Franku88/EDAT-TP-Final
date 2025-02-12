package estructuras.mapeo;
import estructuras.lineales.Lista;

public class NodoAVLMapeoM {
    private Comparable<Object> dominio;
    private Lista rango;
    private NodoAVLMapeoM izquierdo;
    private NodoAVLMapeoM derecho;
    private int altura;

    public NodoAVLMapeoM(Comparable<Object> dominio, Object valor) {
        this.dominio = dominio;
        this.rango = new Lista();
        this.rango.insertar(valor, 1);
        this.izquierdo = null;
        this.derecho = null;
        this.recalcularAltura();
    }

    public NodoAVLMapeoM(Comparable<Object> dominio, Object valor, NodoAVLMapeoM izq, NodoAVLMapeoM der) {
        this.dominio = dominio;
        this.rango = new Lista();
        this.rango.insertar(valor, 1);
        this.izquierdo = izq;
        this.derecho = der;
        this.recalcularAltura();
    }

    public Lista getRango(){
        return this.rango;
    }

    public void setRango(Lista unRango){
        this.rango = unRango;
    }

    public Comparable<Object> getDominio(){
        return this.dominio;
    }

    public void setDominio(Comparable<Object> unDominio){
        this.dominio = unDominio;
    }

    public int getAltura() {
        return this.altura;
    }

    public NodoAVLMapeoM getIzquierdo(){
        return this.izquierdo;
    }

    public void setIzquierdo(NodoAVLMapeoM hijoIzq){
        this.izquierdo = hijoIzq;
        this.recalcularAltura();
    }

    public NodoAVLMapeoM getDerecho(){
        return this.derecho;
    }

    public void setDerecho(NodoAVLMapeoM hijoDer){
        this.derecho = hijoDer;
        recalcularAltura();
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
