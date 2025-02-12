package estructuras.lineales;

public class Nodo {
    
    private Object elemento;
    private Nodo enlace;

    //Constructor
    public Nodo(Object elem, Nodo link) {
        //Elemento que contiene y su enlace al siguiente nodo
        this.elemento = elem;
        this.enlace = link;
    }

    //Getters
    public Object getElemento() {
        //Retorna el elemento del nodo actual
        return this.elemento;
    }

    public Nodo getEnlace() {
        //Retorna el enlace del nodo actual
        return this.enlace;
    }
    
    //Setters
    public void setElemento(Object element) {
        //Modifica el elemento del nodo actual
        this.elemento = element;
    }

    public void setEnlace(Nodo link) {
        //Modifica el enlace del nodo actual
        this.enlace = link;
    }
}
