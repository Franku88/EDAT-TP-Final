package estructuras.mapeo;

import estructuras.lineales.Lista;
@SuppressWarnings({"rawtypes", "unchecked"})

public class MapeoAMuchos {

    private NodoAVLMapeoM raiz;

    public MapeoAMuchos() {
        this.raiz = null;
    }

    public boolean asociar(Comparable unDominio, Object unRango) {
        // Asocia unDominio a unRango (unRango agregado a la lista de rangos)
        boolean exito = true;
        if (this.esVacio()) {
            this.raiz = new NodoAVLMapeoM(unDominio, unRango);    
        } else {
            exito = asociarAux(this.raiz, unDominio, unRango, null);
        }
        return exito;
    }

    private boolean asociarAux(NodoAVLMapeoM nodo, Comparable unDominio, Object unRango, NodoAVLMapeoM padre) {
        // unDominio es la clave compuesta (codPostalOrigen concatenado a codPostalDestino)
        boolean exito = true;
        //Comparo unDominio a asociar con Dominio del nodo actual
        int comparacion = unDominio.compareTo(nodo.getDominio());
        if (comparacion == 0) {
            // Ya existe el Dominio, hay que insertar el nuevo elemento rango
            exito = nodo.getRango().insertar(unRango, 1);
        } else {
            if (comparacion < 0) { //Si unDominio es menor al del nodo
                if (nodo.getIzquierdo() == null) { //Lo inserto si no tiene HI
                    nodo.setIzquierdo(new NodoAVLMapeoM(unDominio, unRango));
                } else { //Si tiene HI, realizo invocacion con el mismo
                    exito = this.asociarAux(nodo.getIzquierdo(), unDominio, unRango, nodo);
                }
            } else { //Si unDominio es mayor al del nodo
                if (nodo.getDerecho() == null) { //Lo inserto si no tiene HD
                    nodo.setDerecho(new NodoAVLMapeoM(unDominio, unRango));
                } else {  //Si tiene HD, realizo invocacion con el mismo
                    exito = this.asociarAux(nodo.getDerecho(), unDominio, unRango, nodo);
                }
            }
        }
        if (exito) { //Si se inserto en alguna invocacion, verifico balanceo
            nodo.recalcularAltura(); //Recalculo altura 
            int balance = nodo.balance(); //Obtengo balance de nodo
            if (balance < -1 || balance > 1) { //Si esta desbalanceado
                balancear(nodo, padre);
                nodo.recalcularAltura();
            } 
        }
        return exito;
    }

    public boolean esVacio() {
        return this.raiz == null;
    }

    public boolean eliminar(Comparable unDominio) {
        // Elimina nodo con unDominio (junto con su lista de rangos)
        return eliminarAux(this.raiz, unDominio, null);
    }

    private boolean eliminarAux(NodoAVLMapeoM nodo, Comparable unDominio, NodoAVLMapeoM padre) {
        // Metodo que elimina nodo con unDominio 
        // Elimina referencia del nodo unDominio, por lo que se pierde la referencia de la lista de rangos de igual forma
        // Retorna verdadero si es posible eliminar
        boolean exito = false;
        if (nodo != null) {
            //Hijos de nodo actual
            NodoAVLMapeoM izq = nodo.getIzquierdo();
            NodoAVLMapeoM der = nodo.getDerecho();
            //Comparo unDominio a eliminar con Dominio del nodo actual
            int comparacion = unDominio.compareTo(nodo.getDominio());
            //Casos
            if (comparacion == 0) { //Si se encontró el nodo que contiene unDominio
                
                // Casos de eliminacion de nodo
                if (izq != null && der != null) { //Si el nodo a eliminar tiene dos hijos
                    casoDosHijos(nodo, padre);
                } else {
                    if (izq == null && der == null) { //Si el nodo a eliminar es hoja
                        casoHoja(nodo, padre);
                    } else { //Si el nodo a eliminar tiene solo un hijo
                        casoUnHijo(nodo, padre);
                    }
                }
            
            } else { //Si no se encuentra, se busca en sus hijos
                if (comparacion < 0) { //Si unDominio es menor al del nodo
                    exito = eliminarAux(izq, unDominio, nodo); //Busco por subarbol izquierdo
                } else { //Si unDominio es mayor al del nodo
                    exito = eliminarAux(der, unDominio, nodo); //Busco por subarbol derecho
                }
            }
            if (exito) { //Si se elimino, verifico balanceo en cualquier caso
                nodo.recalcularAltura(); //Recalculo altura 
                int balance = nodo.balance(); //Veo balance de nodo
                if (balance < -1 || balance > 1) { //Si esta desbalanceado
                    balancear(nodo, padre);
                    nodo.recalcularAltura();
                } 
            } 
        }
        return exito;
    }

    public boolean desasociar(Comparable unDominio, Object unRango) {
        // Desasocia rango en posRango ingresada de un Dominio (quita unRango de la lista de rangos de Dominio)
        return desasociarAux(this.raiz, unDominio, unRango, null);
    }

    private boolean desasociarAux(NodoAVLMapeoM nodo, Comparable unDominio, Object unRango, NodoAVLMapeoM padre) {
        // Metodo que desasocia unRango de unDominio
        // Si lista de Rangos de unDominio queda vacia, entonces elimina el par (unDominio,{}) del arbol
        // Retorna verdadero si es posible desasociar
        boolean exito = false;
        if (nodo != null) {
            //Hijos de nodo actual
            NodoAVLMapeoM izq = nodo.getIzquierdo();
            NodoAVLMapeoM der = nodo.getDerecho();
            //Comparo unDominio a eliminar con Dominio del nodo actual
            int comparacion = unDominio.compareTo(nodo.getDominio());
            //Casos
            if (comparacion == 0) { //Si se encontró el nodo que contiene unDominio
                // Localiza unRango
                int posRango = nodo.getRango().localizar(unRango);                
                if (posRango > 0) { // Si posRango esta fuera de rango, no sera posible la desasociacion    
                    // Ahora elimina unRango de la posicion posRango
                    exito = nodo.getRango().eliminar(posRango); 
                }
                // Si lista de rangos queda vacia, debe eliminar elemento (unDominio,{})
                if (nodo.getRango().esVacia()) {
                    if (izq != null && der != null) { //Si el nodo a eliminar tiene dos hijos
                        casoDosHijos(nodo, padre);
                    } else {
                        if (izq == null && der == null) { //Si el nodo a eliminar es hoja
                            casoHoja(nodo, padre);
                        } else { //Si el nodo a eliminar tiene solo un hijo
                            casoUnHijo(nodo, padre);
                        }
                    }
                }
            } else { //Si no se encuentra, se busca en sus hijos
                if (comparacion < 0) { //Si unDominio es menor al del nodo
                    exito = desasociarAux(izq, unDominio, unRango, nodo); //Busco por subarbol izquierdo
                } else { //Si unDominio es mayor al del nodo
                    exito = desasociarAux(der, unDominio, unRango, nodo); //Busco por subarbol derecho
                }
            }
            if (exito) { //Si se elimino unRango, verifico balanceo en cualquier caso
                nodo.recalcularAltura(); //Recalculo altura 
                int balance = nodo.balance(); //Veo balance de nodo
                if (balance < -1 || balance > 1) { //Si esta desbalanceado
                    balancear(nodo, padre);
                    nodo.recalcularAltura();
                } 
            } 
        }
        return exito;
    }

    private void casoHoja(NodoAVLMapeoM nodo, NodoAVLMapeoM padre) {
        //Caso del metodo eliminar, elimina un nodo que es hoja.
        if (padre != null) {
            //Si dominio de nodo es menor al de su padre, elimino HI de padre (es decir, busco que hijo es nodo)
            if ((nodo.getDominio()).compareTo(padre.getDominio()) < 0) { 
                padre.setIzquierdo(null); // Si nodo es HI
            } else { //Si dominio es mayor al de su padre, elimino HD de padre
                padre.setDerecho(null); // Si nodo es HD
            }
        } else { //Si nodo es raiz
            this.raiz = null;
        }
    }

    private void casoUnHijo(NodoAVLMapeoM nodo, NodoAVLMapeoM padre) {
        //Caso del metodo eliminar, elimina nodo que tiene un solo hijo
        //Precondicion: nodo tiene al menos un hijo no nulo
        NodoAVLMapeoM hijo = nodo.getIzquierdo(); //Tomo HI
        if (hijo == null) { //Si HI es null, entonces HD no es null
            hijo = nodo.getDerecho();
        }
        if (padre != null) { //Si nodo no es raiz
            //Si dominio de nodo es menor al de su padre, coloco hijo como HI (es decir, busco que hijo es nodo)
            if ((nodo.getDominio()).compareTo(padre.getDominio()) < 0) {
                padre.setIzquierdo(hijo); // Si nodo es HI
            } else { //Si dominio de nodo es mayor al de su padre, coloco hijo como HD
                padre.setDerecho(hijo); // Si nodo es HD
            }
        } else { //Si nodo es raiz, lo reemplazo por su hijo
            this.raiz = hijo;
        }
    }

    private void casoDosHijos(NodoAVLMapeoM nodo, NodoAVLMapeoM padre) {
        //Caso del metodo eliminar, elimina nodo que tiene ambos hijos 
        //Elijo buscar nodo con clave menor del subarbol derecho
        NodoAVLMapeoM subarbol = nodo.getDerecho();
        NodoAVLMapeoM candidato = menorEnSubarbol(subarbol);
        /* Otra opcion: buscar el mayor del subarbol izquierdo
        NodoAVLMapeoM subarbol = nodo.getIzquierdo();
        NodoAVLMapeoM candidato = mayorEnSubarbol(subarbol); */
        eliminarAux(subarbol, candidato.getDominio(), nodo); //Elimino candidato encontrado
        if (padre != null) { //Si nodo no es raiz
            if ((candidato.getDominio()).compareTo(padre.getDominio()) < 0) {
                padre.setIzquierdo(candidato); //Si candidato es menor a padre
            } else {
                padre.setDerecho(candidato); //Si candidato es mayor a padre
            }         
        } else { //Si nodo es raiz
            this.raiz = candidato; //Convierto candidato encontrado en raiz
        }
        //Establezco hijos del candidato
        candidato.setIzquierdo(nodo.getIzquierdo());
        candidato.setDerecho(nodo.getDerecho());
    }

    private NodoAVLMapeoM menorEnSubarbol(NodoAVLMapeoM nodo) {
        //Metodo que retorna referencia al Nodo con menor clave de un subarbol
        NodoAVLMapeoM izq = nodo.getIzquierdo();
        if (izq != null) { //Si tiene nodo izquierdo, sigo buscando
            nodo = menorEnSubarbol(izq);
        } //Si no tiene nodo izquierdo, entonces ya es el menor
        return nodo;
    }

    @SuppressWarnings("unused")
    private NodoAVLMapeoM mayorEnSubarbol(NodoAVLMapeoM nodo) {
        //Metodo que retorna referencia al Nodo con mayor clave de un subarbol
        NodoAVLMapeoM der = nodo.getDerecho();
        if (der != null) { //Si tiene nodo derecho, sigo buscando
            nodo = mayorEnSubarbol(der);
        } //Si no tiene nodo derecho, entonces ya es el mayor
        return nodo;
    }

    private void balancear(NodoAVLMapeoM nodo, NodoAVLMapeoM padre) {
        /*Metodo aux que balancea el nodo con 4 casos
        padre: es el padre de nodo, usado para asignar a su hijo desbalanceado una vez termine el proceso
        precondicion: nodo no es vacio y balance es 2 o -2*/
        NodoAVLMapeoM aux;
        if (nodo.balance() < -1) { //Si subarbol con raiz nodo esta torcido a derecha
            if (nodo.getDerecho().balance() <= 0) { //Si HD esta torcido a la der (mismo lado que nodo)
                nodo = rotarIzquierda(nodo); //Roto HD a la izq, contrario al lado del padre
                if (padre == null) { //Caso especial, nodo a balancear
                    this.raiz = nodo;
                } else {
                    //Comparo nodo con su padre
                    if ((nodo.getDominio()).compareTo(padre.getDominio()) > 0) { //Si nodo es mayor a padre
                        padre.setDerecho(nodo);
                    } else { //Si nodo es menor a padre
                        padre.setIzquierdo(nodo);
                    }
                    padre.recalcularAltura();
                }
            } else { //Si HD esta torcido a la izq (lado opuesto a nodo)
                aux = rotarDerecha(nodo.getDerecho()); //Rotacion simple a der con pivote HD
                nodo.setDerecho(aux);
                balancear(nodo, padre); //Balanceo nodo (paso recursivo con nuevo balance de HD)
            }
        } else { //Si subarbol con raiz nodo esta torcido a izquierda
            if (nodo.getIzquierdo().balance() >= 0) { //Si HI esta torcido a la izq (mismo lado que nodo)
                nodo = rotarDerecha(nodo); //Rotacion simple a derecha con pivote nodo
                if (padre == null) { //Caso especial, nodo es raiz
                    this.raiz = nodo;
                } else {
                    //Comparo nodo con su padre
                    if (nodo.getDominio().compareTo(padre.getDominio()) > 0) { //Si nodo es mayor a padre
                        padre.setDerecho(nodo);
                    } else { //Si nodo es menor a padre
                        padre.setIzquierdo(nodo);
                    }
                    padre.recalcularAltura();
                }
            } else { //Si HI esta torcido a la der
                aux = rotarIzquierda(nodo.getIzquierdo()); //Rotacion simple a izq con pivote HI
                nodo.setIzquierdo(aux);
                balancear(nodo, padre); //Balanceo nodo (paso recursivo con nuevo balance de HI)
            }
        }
    }

    private NodoAVLMapeoM rotarIzquierda(NodoAVLMapeoM pivote) {
        //Guardo hijo derecho del pivote
        NodoAVLMapeoM hD = pivote.getDerecho();
        //Guardo hijo izquierdo del hijo derecho
        NodoAVLMapeoM temp = hD.getIzquierdo();
        //Establezco pivote como hijo izquierdo de su hijo derecho
        hD.setIzquierdo(pivote);
        //Establezco hijo izquierdo del hijo derecho como hijo derecho del pivote
        pivote.setDerecho(temp);
        //Reestablezco alturas de pivote y hD
        pivote.recalcularAltura();
        hD.recalcularAltura();
        //Retorna nueva raiz de subarbol
        return hD;
    }

    private NodoAVLMapeoM rotarDerecha(NodoAVLMapeoM pivote) {
        //Guardo hijo izquierdo del pivote
        NodoAVLMapeoM hI = pivote.getIzquierdo();
        //Guardo hijo derecho del hijo izquierdo
        NodoAVLMapeoM temp = hI.getDerecho();
        //Establezco pivote como hijo derecho de su hijo izquierdo
        hI.setDerecho(pivote);
        //Establezco hijo derecho del hijo izquierdo como hijo izquierdo del pivote
        pivote.setIzquierdo(temp);
        //Reestablezco alturas de pivote y hI
        pivote.recalcularAltura();
        hI.recalcularAltura();
        //Retorna nueva raiz de subarbol
        return hI;
    }

    public Lista obtenerValores(Comparable unDominio) {
        // Metodo que obtiene Lista de rangos de unDominio (realiza una clonacion)
        // Si unDominio no se encuentra en el Mapeo, retorna una lista vacia
        Lista resultado = obtenerValoresAux(this.raiz, unDominio);
        if (resultado == null) {
            resultado = new Lista();
        }
        return resultado;
    }

    private Lista obtenerValoresAux(NodoAVLMapeoM nodo, Comparable unDominio) {
        // Metodo auxiliar para obtener lista de rangos asociados a unDominio
        Lista resultado = null;
        if(nodo != null) {
            int comparacion = unDominio.compareTo(nodo.getDominio());
            if(comparacion == 0) { // Nodo encontrado
                resultado = nodo.getRango().clone();
            } else {
                if(comparacion < 0) { // Si unDominio es menor al del nodo actual
                    resultado = obtenerValoresAux(nodo.getIzquierdo(), unDominio); // Busca en subarbol izquierdo
                } else { // Si unDominio es mayor al del nodo actual
                    resultado = obtenerValoresAux(nodo.getDerecho(), unDominio); // Busca en subarbol derecho
                }
            }
        }
        return resultado;
    }

    public Lista obtenerConjuntoDominio() {
        // Metodo que retorna una lista con la union de todos los dominios del mapeo
        Lista resultado = new Lista();
        obtenerConjuntoDominioAux(this.raiz, resultado);
        return resultado;
    }

    private void obtenerConjuntoDominioAux(NodoAVLMapeoM nodo, Lista resultado) {
        // Metodo auxiliar que crea una lista con todos los dominios del mapeo menor a mayor, recorrido inorden inverso
        if(nodo != null) {
            obtenerConjuntoDominioAux(nodo.getDerecho(), resultado);
            resultado.insertar(nodo.getDominio(), 1);
            obtenerConjuntoDominioAux(nodo.getIzquierdo(), resultado);       
        }
    }

    public Lista obtenerConjuntoRango() {
        // Metodo que retorna una lista con la union de todos los rangos del mapeo (todas listas de rango concatenedas)
        Lista resultado = new Lista();
        obtenerConjuntoRangoAux(raiz, resultado);
        return resultado;
    }

    private void obtenerConjuntoRangoAux(NodoAVLMapeoM nodo, Lista resultado) {
        // Metodo auxiliar que crea una lista con todos los rangos del mapeo menor a mayor (Orden de sus dominios), recorrido inorden inverso
        if(nodo != null) {
            obtenerConjuntoRangoAux(nodo.getDerecho(), resultado);
            int i = 1, rangoSize = nodo.getRango().longitud();
            while(i <= rangoSize) {
                resultado.insertar(nodo.getRango().recuperar(i), 1);
                i++;
            }
            obtenerConjuntoRangoAux(nodo.getIzquierdo(), resultado);
        }
    }

    public String toString() {
        //Retorna un string que contiene una representacion del Diccionario
        String cad;
        if (this.esVacio()){
            cad = "Mapeo Vacio";
        } else {
            cad = toStringAux(this.raiz);
        }
        return cad;
    }

    private String toStringAux(NodoAVLMapeoM nodo) {
        //Metodo auxiliar que concatena los dominios y rangos del mapeo en un string
        String cad = "";
        if (nodo != null) { //Si el nodo no es nulo
            //Obtengo hijos de nodo
            NodoAVLMapeoM izq = nodo.getIzquierdo();
            NodoAVLMapeoM der = nodo.getDerecho();
            //Concateno clave del nodo
            cad = cad +"("+nodo.getAltura()+") | ("+ nodo.getDominio() + ")("+nodo.getRango().toString()+") ->  ";
            if (izq != null) { //Si tiene HI, lo concateno
                cad = cad + "HI: " + izq.getDominio().toString() + "  ";
            } else {
                cad = cad + "HI: -  ";
            }
            if (der != null) { //Si tiene HD, lo concateno
                cad = cad + "HD: " + der.getDominio().toString() + "\n";
            } else {
                cad = cad + "HD: - \n";
            }
            //Continuo con HI e HD
            cad = cad + toStringAux(izq);
            cad = cad + toStringAux(der);
        }
        return cad;
    }

}