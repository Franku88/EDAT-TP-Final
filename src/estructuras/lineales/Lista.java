package estructuras.lineales;

/************* Autores ***********
- Franco Fabian Benitez, Legajo FAI-3169
* Jamiro Zuñiga, Legajo FAI-3429
* Agustín Ezequiel Heredia, Legajo FAI-2876
*/

public class Lista {

    private Nodo cabecera;
    private int longitud;

    public Lista() {
        this.cabecera = null;
        this.longitud = 0;
    }

    public int longitud() {
        //Retorna longitud de la lista
        return this.longitud;
    }

    public boolean esVacia() {
        //Verifica si la lista esta vacia
        return this.cabecera == null;
    }

    public boolean insertar(Object elemento, int pos) {
        //Realiza la insercion del elemento en la posicion ingresada por parametro
        boolean exito = true;
        if (pos < 1 || this.longitud + 1 < pos) { //Error fuera de rango
            exito = false;
        } else {
            if (pos == 1) {//Crea nuevo nodo y lo enlaza en la cabecera
                cabecera = new Nodo(elemento, this.cabecera);
            } else {
                //A partir de cabecera, recupera los enlaces hasta el de la pos-1
                Nodo aux = this.cabecera;
                int i = 1;
                while(i < pos-1) { //Busco el nodo de la pos-1
                    aux = aux.getEnlace(); //Ultima iteracion aux es el nodo anterior al que quiero reemplazar
                    i++;//Ultima iteracion i == pos-1
                }
                //Enlazo nodo de la pos al nuevo nodo
                Nodo nuevo = new Nodo(elemento, aux.getEnlace());
                //Enlazo nuevo nodo al de la pos-1
                aux.setEnlace(nuevo);
            }
            this.longitud++;
        }
        return exito;
    }

    public boolean eliminar(int pos) {
        //Elimina nodo en la posicion pos, enlaza el de pos-1 con pos+1
        boolean exito = true;
        if (pos < 1 || this.longitud + 1 < pos) {//Error fuera de rango
            exito = false;
        } else {
            Nodo aux = this.cabecera;
            if (aux != null) {
                if (pos == 1) {
                    this.cabecera = aux.getEnlace();
                } else {
                    int i = 1;
                    while (i < pos-1) { //Busco nodo anterior al que quiero eliminar
                        aux = aux.getEnlace(); //Consigue nodo anterior
                        i++; //En la ultima iteracion, i == pos-1
                    }
                    //Enlazo el aux con el que sigue al de la posicion pos
                    aux.setEnlace(aux.getEnlace().getEnlace());
                }
                this.longitud--;
            }
        }
        return exito;
    }

    public Object recuperar(int pos){
        //Metodo que retorna el elemento del nodo en la posicion pos
        //Retorna null si la lista esta vacia o si la posicion no apunta a un nodo 
        Object elemento;
        if (!this.esVacia() && pos >= 1 && pos <= this.longitud) {//Si no es vacia y se encuentra elemento
            Nodo aux = this.cabecera;
            int i = 1;
            while (i < pos) {//Itero hasta obtener nodo que necesito
                aux = aux.getEnlace();
                i++;//En la ultima iteracion, i==pos
            }
            elemento = aux.getElemento();
        } else {//Si no cumple las condiciones, retorna null
            elemento = null;
        }
        return elemento;
    }

    public int localizar(Object elem){
        //Metodo que retorna la poisicion en la lista de la primer ocurrencia de elem
        int pos = localizarAux(this.cabecera, elem, 1);
        return pos;
    }

    private int localizarAux(Nodo nodo, Object elem, int pos){
        //Modulo auxiliar de localizar
        int ret;
        if (nodo != null) {
            if (nodo.getElemento() == elem){ //Elemento encontrado, retorna pos
                ret = pos;
            } else { //Si no se encontro, compara con siguiente nodo
                ret = localizarAux(nodo.getEnlace(), elem, pos+1);
            }
        } else {
            ret = -1;
        }
        return ret;
    }

    public void vaciar() {
        this.cabecera = null;
    }

    public Lista clone() {
        //Metodo que retorna un clon de la lista de la invocacion
        Lista clon = new Lista();
        if (!this.esVacia()) {
            clon.longitud = this.longitud;
            clon.cabecera = cloneR(this.cabecera);
        }
        return clon;
    }

    private Nodo cloneR(Nodo cabeceraO) {
        //Metodo recursivo que realiza la clonacion de una lista en base a su nodo cabecera
        //Retorna una copia del nodo cabeceraO con su nodo enlace tambien clonado recursivamente
        Nodo retorno;
        //Si el nodo cabecera es null, retorna null
        if (cabeceraO != null) {
            //Crea un nodo con el elemento de cabecera y con una copia de su enlace
            //Su enlace se copia y crea recursivamente
            retorno = new Nodo(cabeceraO.getElemento(), cloneR(cabeceraO.getEnlace()));
        } else {
            retorno = null;
        }
        return retorno;
    }

    public String toString() {
        String cad;
        if (!this.esVacia()) {
            cad = "|"+toStringR(this.cabecera);
        } else {
            cad = "||";
        }
        return cad;
    }

    private String toStringR(Nodo nodo) {
        /*Metodo recursivo que retorna el elemento  de un nodo concatenado
        al elemento de su enlace*/
        String cad;
        if (nodo.getEnlace() == null) {//Si el nodo ingresado en null, entonces es el fin de la lista
            cad = nodo.getElemento()+"|";
        } else {
            cad = nodo.getElemento()+","+ toStringR(nodo.getEnlace());
        }
        return cad;
    }

    public void invertir() {
        //Metodo que modifica la lista por su invertida
        if (this.longitud != 0) {
            invertirR(this.cabecera,1);
        }
    }

    private void invertirR(Nodo nodoPos, int pos) {
        //Metodo recursivo que invierte una lista a partir de su nodo cabecera
        //Itera hasta el ultimo nodo, lo establece como cabecera y recursivamente establece sus enlaces
        //nodoPos: nodo que se encuentra en la posicion pos de la lista
        //pos: posicion del nodoPos
        if (pos == this.longitud) { //Asigna ultimo nodo como cabecera
            this.cabecera = nodoPos;
        } else {
            Nodo nodoSig = nodoPos.getEnlace(); //Guarda siguiente nodo
            if (pos != 1) {
                invertirR(nodoSig,pos+1);
                nodoSig.setEnlace(nodoPos); //Se lo establece como enlace de su enlace
            } else {
                invertirR(nodoSig,pos+1);
                nodoSig.setEnlace(nodoPos);
                nodoPos.setEnlace(null); //Enlaza ultimo nodo con null
            }
        }
    }
}