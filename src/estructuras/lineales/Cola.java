package estructuras.lineales;

/************* Autores ***********
- Franco Fabian Benitez, Legajo FAI-3169
* Jamiro Zuñiga, Legajo FAI-3429
* Agustín Ezequiel Heredia, Legajo FAI-2876
*/

public class Cola {
    private Nodo frente;
    private Nodo fin;
    
    //Constructor
    public Cola() {
        //Cola vacia
        this.frente = null;
        this.fin = null;
    }

    public boolean poner(Object nuevoElemento) {
        //Metodo que agrega un elemento al fin de la cola
        Nodo nuevo = new Nodo(nuevoElemento, null);
        if (this.esVacia()) { //Caso especial, si esta vacia se asigna el nuevo nodo al frente y final
            this.frente = nuevo;
            this.fin = nuevo;
        } else { //Caso general, enlazo nuevo nodo al fin actual, y luego lo asigno como nuevo fin 
            (this.fin).setEnlace(nuevo);
            this.fin = nuevo;
        }
        return true;
    }

    public boolean sacar() {
        //Metodo que quita el elemento del frente de una cola
        //Retorna verdadero si se saco un elemento
        boolean exito;

        if (this.esVacia()) {
            //Cola vacia, no tiene objetos que quitar
            exito = false; 
        } else { 
            //Al menos contiene un elemento
            this.frente = (this.frente).getEnlace();
            //Si se vacio la cola, se asigna null a fin
            if (this.frente == null) {
                this.fin = null;
            }
            exito = true;
        }

        return exito;
    }
    public boolean esVacia() {
        //Metodo que retorna verdadero si la cola esta vacia
        return this.frente == null;
    }

    public Object obtenerFrente() {
        //Metodo que retorna el elemento del frente, retorna null si esta vacia
        Object elemento;
        if (!this.esVacia()){
            elemento = (this.frente).getElemento();
        } else {
            elemento = null;
        }
        return elemento;
    }

    public void vaciar() {
        //Metodo que vacia la cola de la invocacion
        this.frente = null;
        this.fin = null;
    }

    public Cola clone() {
        //Metodo que realiza clonacion de una cola dinamica
        //Retorna el clon creado
        Cola clon = new Cola();
        if (!this.esVacia()) {
            Nodo aux1, aux2; //aux1 almacena nodos de la cola original, aux2 almacena nodos de la cola clon
            aux1 = this.frente; 
            clon.frente = new Nodo(aux1.getElemento(), null); //Asigna primer nodo de clon con elemento de frente y enlace null
            //Guardo nodo enlace del primer nodo
            aux1 = aux1.getEnlace();
            //Guardo nodo frente del clon
            aux2 = clon.frente;
            while (aux1 != null) { //Mientras el nodo almacenado en aux1 no sea nulo
                //Asigno enlace al nodo actual del clon (aux2) a un nodo nuevo con elemento del enlace original (aux1)
                aux2.setEnlace(new Nodo(aux1.getElemento(), null));
                aux1 = aux1.getEnlace(); //Proximo nodo a copiar, si este es null, corta la iteracion
                aux2 = aux2.getEnlace(); //Proximo nodo a ocupar del clon
            }
            //Ultimo enlace copiado como fin de cola
            clon.fin = aux2;
        }
        return clon;
    }

    public String toString() {
        //Metodo que retorna cadena con los objetos que contiene una cola dinamica
        String cadena = "";
        //Verifica que no este vacia
        if (!this.esVacia()) {
            //Uso aux para manipular repetitiva e imprimir los enlaces
            Nodo aux = this.frente;
            cadena = "[";
            while (aux != null) {
                cadena = cadena + aux.getElemento().toString();
                aux = aux.getEnlace(); //Obtengo puntero al siguiente nodo
                if (aux != null) { //Si aux es null (final de cola), no imprimo coma
                    cadena = cadena + ",";
                }
            }
            cadena = cadena + "]";
        } else {
            cadena = "Cola vacia";
        }
        return cadena;
    }
}