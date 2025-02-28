package estructuras.grafos;
import estructuras.lineales.Lista;
import estructuras.lineales.Cola;

public class GrafoEtiquetado {
    private NodoVert inicio;

    public GrafoEtiquetado() {
        this.inicio = null;
    }

    public boolean insertarVertice(Object elem) {
        boolean noExiste = !existeVertice(elem);
        if (noExiste) { //Si no existe, lo inserta al principio
            this.inicio = new NodoVert(elem, this.inicio);
        }
        return noExiste; //Si no existe, entonces se inserta con exito
    }

    private NodoVert ubicarVertice(Object buscado) {
        //Busca y retorna vertice con elemento buscado del grafo
        //Si no se encuentra, retorna null
        NodoVert aux = this.inicio;
        while(aux != null && !buscado.equals(aux.getElemento())) {
            aux = aux.getSigVertice(); //Para comparar en la siguiente iteración
        }
        return aux;
    }

    public boolean existeVertice(Object buscado) {
        //Retorna falso si el vertice no es ubicado (ubicarVertice retorna null)
        return (ubicarVertice(buscado) != null);
    }

    public boolean eliminarVertice(Object buscado) {
        //Metodo que elimina vertice con elemento buscado
        //Retorna verdadero si es eliminado, falso en caso de no encontrarlo
        return eliminarVerticeAux(this.inicio, null, buscado);
    }

    private boolean eliminarVerticeAux(NodoVert nodo, NodoVert anterior, Object buscado) {
        //Metodo auxiliar para eliminar un vertice y sus respectivos arcos
        //Retorna verdadero si es eliminado, falso si no se encuentra el elemento buscado
        boolean exito = false;
        if (nodo != null) { //Si es null, entonces anterior tiene al ultimo vertice del grafo
            exito = buscado.equals(nodo.getElemento()); //Compara elemento buscado con el de nodo
            if (exito) { //Si se encuentra el elemento
                eliminarArcos(nodo); //Elimina todos los arcos enlazados al nodo
                if (anterior == null) { //Si anterior es null, entonces nodo es inicio
                    this.inicio = nodo.getSigVertice();
                } else { //Si no, enlazo anterior vertice al siguiente vertice del eliminado
                    anterior.setSigVertice(nodo.getSigVertice());
                }
            } else { //Si no se encuentra, paso recursivo con siguiente vertice
                exito = eliminarVerticeAux(nodo.getSigVertice(), nodo, buscado);
            }
        }
        return exito;
    }

    public boolean insertarArco(Object origen , Object destino, double etiqueta) {
        /*Metodo que inserta un arco con elementos origen y destino ingresado, 
        ademas de la etiqueta del arco*/
        boolean exito = false;
        NodoVert aux = this.inicio, nodoOrig = null, nodoDest = null;
        //No uso obtenerVertice ya que recorreria dos veces los vertices en el peor de los casos
        while (aux != null && (nodoOrig == null || nodoDest == null)) { //Hasta encontrar ambos nodos o llegar al ultimo vertice
            if (origen.equals(aux.getElemento())) { //Compara con origen
                nodoOrig = aux;
            } else { //Si no es origen
                if (destino.equals(aux.getElemento())) { //Compara con destino
                    nodoDest = aux;
                }
            }
            aux = aux.getSigVertice(); //Referencio al siguiente vertice
        }
        exito = (nodoOrig != null && nodoDest != null); //Si encontro ambos vertices
        if (exito) { //Conecta ambos arcos (igual etiqueta pero ambos casos de orgen/destino)
            conectarAdy(nodoOrig, nodoDest, etiqueta);
            conectarAdy(nodoDest, nodoOrig, etiqueta);
        }
        return exito;
    }

    private void conectarAdy(NodoVert origen, NodoVert destino, double etiqueta){
        //Conecta un nodo origen con nodo destino, Precondicion: no deben ser nulos
        //Reemplaza el primer adyacente de origen con el ingresado, enlaza el antiguo primer al nuevo
        NodoAdy nuevo = new NodoAdy(destino, origen.getPrimerAdyacente(), etiqueta);
        origen.setPrimerAdyacente(nuevo);
    }

    public void eliminarArcos(NodoVert nodo) {
        //Metodo que elimina todos los arcos enlazados a nodo (como inicio y destino)
        //Precondicion: nodo no es null
        NodoAdy ady = nodo.getPrimerAdyacente();
        while (ady != null) {
            eliminarArcoAux(ady.getVertice(), nodo.getElemento()); //Elimina arco inverso (desde destino a inicio)
            ady = ady.getSigAdyacente(); //Siguiente arco
        }
        nodo.setPrimerAdyacente(null); //Desenlaza todos los adyacentes desde nodo
    }

    public boolean eliminarArco(Object origen, Object destino) {
        //Metodo que elimina el arco (arcos) de origen a destino (y destino a origen)
        //Retorna falso si origen no es encontrado o si no existe arco entre ambos
        /*Se busca origen, luego destino es buscado en adyacentes de origen, al buscar 
        en los adyacentes se verifica la existencia o no del arco sin necesidad de buscar
        a vertice destino*/
        boolean exito = false;
        NodoVert nodoOrig = ubicarVertice(origen);
        if (nodoOrig != null) { //Si encuentra origen, busco en sus adyacentes al destino
            NodoAdy ady = nodoOrig.getPrimerAdyacente(), anterior = null;
            NodoVert nodoDest = null;
            //No uso ubicarArco pues necesito referencia al anterior (ubicarArco no mantiene dicho valor)
            while (!exito && ady != null) { //Busco destino en cada ady
                exito = destino.equals(ady.getVertice().getElemento());
                if (exito) { //Si se encuentra vertice destino
                    nodoDest = ady.getVertice();
                } else { //Si no se encuentra vertice, siguiente vertice
                    anterior = ady;
                    ady = ady.getSigAdyacente();
                }
            }
            if (exito) { //Si estan conectados, elimino arco (ambos sentidos)
                pisarArco(nodoOrig, anterior, ady); //Reemplazo referencia de arco desde el origen
                eliminarArcoAux(nodoDest, origen); //Elimino arco desde el destino
            }
        }
        return exito;
    }

    private boolean eliminarArcoAux(NodoVert nodo, Object elem) {
        //Busca y elimina arco desde nodo a elem, retorna verdadero si es eliminado
        NodoAdy anterior = null, aux = nodo.getPrimerAdyacente();
        boolean exito = false;
        while (!exito && aux != null) {
            exito = elem.equals(aux.getVertice().getElemento());
            if (!exito) { //Si adyacente aux actual no enlaza a elem
                anterior = aux;
                aux = aux.getSigAdyacente();
            }
        }
        if (exito) { //Si se encontro arco
            pisarArco(nodo, anterior, aux); 
        }
        return exito;
    }

    private void pisarArco(NodoVert nodo, NodoAdy anterior, NodoAdy eliminado) {
        //Metodo auxiliar que reemplaza la referencia al nodoAdy eliminado
        //Preconidiciones: nodo y eliminado no son nulos
        if (anterior == null) { //Caso en que eliminado sea primer ady
            nodo.setPrimerAdyacente(eliminado.getSigAdyacente());
        } else {
            anterior.setSigAdyacente(eliminado.getSigAdyacente());
        }
    }

    public boolean existeArco(Object origen, Object destino) {
        /*Metodo que retorna verdadero si existe arco desde un vertice con 
        elemento origen hasta vertice con elemento destino*/
        NodoVert nodoOrig = ubicarVertice(origen); //Busca origen
        return (ubicarArco(nodoOrig, destino) != null);
    }

    private NodoAdy ubicarArco(NodoVert origen, Object destino) {
        //Retorna arco referenciado en vertice origen y con vertice de elemento destino
        //Precondicion: origen no es null
        NodoAdy ady = null;
        boolean encontrado = false;
        if (origen != null) {
            ady = origen.getPrimerAdyacente();
            while (ady != null && !encontrado) { //Busco destino en cada ady
                encontrado = destino.equals(ady.getVertice().getElemento());
                if (!encontrado) { //Si no se encuentra arco con destino
                    ady = ady.getSigAdyacente(); //Busca al siguiente
                }
            }
        }
        return ady;
    }

    public boolean esVacio(){
        return this.inicio == null;
    }

    public void vaciar(){
        this.inicio = null;
    }

    public boolean existeCamino(Object origen, Object destino) {
        boolean exito = false;
        NodoVert auxOrig = null;
        NodoVert auxDest = null;
        NodoVert aux = this.inicio;
        while (aux != null && (auxOrig == null || auxDest == null)) { //Busco ambos nodos orig y dest
            if (origen.equals(aux.getElemento())) {
                auxOrig = aux;
            }
            if (destino.equals(aux.getElemento())) {
                auxDest = aux;
            }
            aux = aux.getSigVertice();
        }
        if (auxOrig != null && auxDest != null) { //Si fueron encontrados, busco un camino
            Lista visitados = new Lista();
            exito = existeCaminoAux(auxOrig, destino, visitados);
        }
        return exito;
    }

    public boolean existeCaminoAux(NodoVert nodo, Object destino, Lista visitados){
        //Metodo auxiliar, recorre los caminos desde nodo hasta encontrar a destino
        //Almacena en visitados los arcos recorridos
        boolean exito = false;
        if (nodo != null){
            if (destino.equals(nodo.getElemento())) { //Si nodo es destino, entonces hay camino
                exito = true;
            } else { //Si no es destino, verifico si hay camino entre nodo y destino
                visitados.insertar(nodo.getElemento(), visitados.longitud()+1); //Agrego como visitado
                NodoAdy ady = nodo.getPrimerAdyacente();
                while (!exito && ady != null) { //Busco en todos sus adyacentes hasta encontrar destino
                    if (visitados.localizar(ady.getVertice().getElemento()) < 0) { //Si no fue visitado
                        exito = existeCaminoAux(ady.getVertice(), destino, visitados); //Lo visita
                    }
                    ady = ady.getSigAdyacente(); //Siguiente adyacente
                }
            }
        }
        return exito;
    }

    public Lista caminoMasCorto(Object origen, Object destino) {
        Lista camCorto = new Lista(); //Camino resultante
        NodoVert aux = this.inicio; 
        NodoVert auxOrig = null;
        NodoVert auxDest = null;
        //No utilizo ubicarVertice dos veces pues es ineficiente
        while (aux != null && (auxOrig == null || auxDest == null)) { //Busco ambos nodos
            if (origen.equals(aux.getElemento())) { //Si encuentra origen
                auxOrig = aux;
            } else {
                if (destino.equals(aux.getElemento())) { //Si encuentra destino
                    auxDest = aux;
                }
            }
            aux = aux.getSigVertice(); //Siguiente vertice
        }
        if (auxOrig != null && auxDest != null) { //Fueron encontrados, busco un camino
            Lista camActual = new Lista();
            camCorto = caminoCortoAux(auxOrig, destino, camCorto, camActual);
        }
        return camCorto;
    }

    private Lista caminoCortoAux(NodoVert nodo, Object destino, Lista camCorto, Lista camActual) {
        //Metodo auxiliar, recursivamente recorre nodos de un grafo, retorna el camino mas corto desde nodo a destino
        if (nodo != null) {
            //Verifica si no supera al mas corto con la siguiente insercion (ya que no tendria sentido seguir buscando en ese punto)
            if (camCorto.esVacia() || ((camActual.longitud()+1) < camCorto.longitud())) {
                Object elem = nodo.getElemento();
                camActual.insertar(elem, camActual.longitud()+1); //Inserta nodo actual (pues es recorrido)
                if (elem.equals(destino)) { //Si encuentra destino
                    camCorto = camActual.clone(); //Clona camActual para no modificar la referencia
                } else {
                    //Verifica si no supera al mas corto con la siguiente insercion (ya que no tendria sentido seguir buscando en ese punto)
                    if (camCorto.esVacia() || ((camActual.longitud()+1) < camCorto.longitud())) {
                        NodoAdy ady = nodo.getPrimerAdyacente();
                        NodoVert auxVert = null;
                        while (ady != null) { //Recorro sus adyacentes (arcos)
                            auxVert = ady.getVertice(); 
                            //Si no fue visitado en este camino, entonces lo visita
                            if (camActual.localizar(auxVert.getElemento()) < 0) { 
                                camCorto = caminoCortoAux(auxVert, destino, camCorto, camActual); 
                            }
                            ady = ady.getSigAdyacente(); //Siguiente arco
                        }
                    }
                }
                //Quita nodo actual (ultimo insertado) porque puede haber mas caminos que lo visiten
                camActual.eliminar(camActual.longitud());
            }
        }
        return camCorto;
    }

    public Lista caminoMasLargo(Object origen, Object destino) {
        Lista camLargo = new Lista();
        NodoVert aux = this.inicio; 
        NodoVert auxOrig = null;
        NodoVert auxDest = null;
        //No utilizo ubicarVertice dos veces pues es ineficiente
        while (aux != null && (auxOrig == null || auxDest == null)) { //Busco ambos nodos
            if (origen.equals(aux.getElemento())) { //Si encuentra origen
                auxOrig = aux;
            } else {
                if (destino.equals(aux.getElemento())) { //Si encuentra destino
                    auxDest = aux;
                }
            }
            aux = aux.getSigVertice(); //Siguiente vertice
        }
        if (auxOrig != null && auxDest != null) { //Fueron encontrados, busco un camino
            Lista camActual = new Lista();
            camLargo = caminoLargoAux(auxOrig, destino, camLargo, camActual);
        }
        return camLargo;
    }

    private Lista caminoLargoAux(NodoVert nodo, Object destino, Lista camLargo, Lista camActual) {
        //Metodo auxiliar, recursivamente recorre nodos de un grafo, retorna el camino mas largo
         if (nodo != null) {
            //No verifica como en caminoCortoAux pues no es necesario (justamente se busca el mas largo)
            Object elem = nodo.getElemento();
            camActual.insertar(elem, camActual.longitud()+1); //Inserta nodo actual
            if (elem.equals(destino)) { //Si encuentra destino
                //Verifica que camActual es mayor a camLargo
                if (camActual.longitud() > camLargo.longitud()) {
                    //Clona camActual para no modificar la referencia
                    camLargo = camActual.clone(); 
                }
            } else {
                //No verifica como en caminoCortoAux pues no es necesario (justamente se busca el mas largo)
                NodoAdy ady = nodo.getPrimerAdyacente();
                NodoVert auxVert = null;
                while (ady != null) { //Recorro sus adyacentes (arcos)
                    auxVert = ady.getVertice();
                    //Si no fue visitado en este camino, entonces lo visita
                    if (camActual.localizar(auxVert.getElemento()) < 0) {
                        camLargo = caminoLargoAux(auxVert, destino, camLargo, camActual); 
                    }
                    ady = ady.getSigAdyacente(); //Siguiente arco
                }
            }
            //Quita nodo actual (ultimo nodo insertado)) ya que puede haber mas caminos que lo visiten
            camActual.eliminar(camActual.longitud());
        }
        return camLargo;
    }    

    public Lista caminosCruzandoIntermedio(Object origen, Object destino, Object intermedio) {
        //Lista los caminos desde origen a destino, pasando una vez por intermedio y sin recorrer mas de una vez ningun nodo
        Lista resultado = new Lista();
        NodoVert aux = this.inicio; 
        NodoVert auxOrig = null;
        NodoVert auxDest = null;
        NodoVert auxInt = null;

        //No utilizo ubicarVertice dos veces pues es ineficiente
        while (aux != null && (auxOrig == null || auxDest == null || auxInt == null)) { //Busco ambos nodos
            if (origen.equals(aux.getElemento())) { //Si encuentra origen
                auxOrig = aux;
            } else {
                if (destino.equals(aux.getElemento())) { //Si encuentra destino
                    auxDest = aux;
                } else {
                    if (intermedio.equals(aux.getElemento())) { //Si encuentra intermedio
                        auxInt = aux;
                    }
                }
            }
            aux = aux.getSigVertice(); //Siguiente vertice
        }

        if(auxDest != null && auxDest != null && auxInt != null) {
            Lista camActual = new Lista();            
            resultado = caminosCruzandoIntermedioAux(auxOrig, destino, intermedio, resultado, camActual, false);
        }
        return resultado;
    }

    private Lista caminosCruzandoIntermedioAux(NodoVert nodo, Object destino, Object intermedio, Lista resultado, Lista camActual, boolean flag) {
        //Metodo auxiliar, recursivamente recorre nodos de un grafo, retorna lista de caminos de A a B que pasan por C
        //flag: indica si se cruzo por C en el recorrido actual
        if (nodo != null) {
            Object elem = nodo.getElemento();
            NodoAdy ady = null;
            NodoVert auxVert = null;
            camActual.insertar(elem, camActual.longitud()+1); //Inserta nodo actual (pues es recorrido)
            if (flag) { //Verifica que no cruze primero por el destino
                if (elem.equals(destino)) { //Si encuentra destino
                    Lista aux = camActual.clone();                    
                    resultado.insertar(aux, resultado.longitud()+1); //Clona camActual para no modificar la referencia                    
                } else { // si no lo encuentra recorre sus adyacentes para buscar el camino
                    ady = nodo.getPrimerAdyacente();                    
                    while (ady != null) { //Recorro sus adyacentes (arcos)
                        auxVert = ady.getVertice(); 
                        //Si no fue visitado en este camino, entonces lo visita
                        if (camActual.localizar(auxVert.getElemento()) < 0) { 
                            resultado = caminosCruzandoIntermedioAux(ady.getVertice(), destino, intermedio, resultado, camActual, flag);
                        }
                        ady = ady.getSigAdyacente(); //Siguiente arco
                    }
                }
            } else {
                ady = nodo.getPrimerAdyacente();
                flag = elem.equals(intermedio);
                while (ady != null) { //Recorro sus adyacentes (arcos)
                    auxVert = ady.getVertice(); 
                    //Si no fue visitado en este camino, entonces lo visita
                    if (camActual.localizar(auxVert.getElemento()) < 0) { 
                        //Si el nodo agregado es punto intermedio, avisa en la siguiente invocacion
                        resultado = caminosCruzandoIntermedioAux(ady.getVertice(), destino, intermedio, resultado, camActual, flag);
                    }
                    ady = ady.getSigAdyacente(); //Siguiente arco
                }
            }  
            //Quita nodo actual (ultimo insertado) porque puede haber mas caminos que lo visiten
            camActual.eliminar(camActual.longitud());
        }        
        return resultado;
    }

    public Lista caminoMasCortoEtiqueta(Object origen, Object destino) {
        //Lista camino de origen a destino donde la sumatoria de las etiquetas de arcos es minimal
        Lista resultado = new Lista();
        NodoVert aux = this.inicio; 
        NodoVert auxOrig = null;
        NodoVert auxDest = null;        

        //No utilizo ubicarVertice dos veces pues es ineficiente
        while (aux != null && (auxOrig == null || auxDest == null)) { //Busco ambos nodos
            if (origen.equals(aux.getElemento())) { //Si encuentra origen
                auxOrig = aux;
            } else {
                if (destino.equals(aux.getElemento())) { //Si encuentra destino
                    auxDest = aux;
                }
            }
            aux = aux.getSigVertice(); //Siguiente vertice
        }

        if(auxDest != null && auxDest != null) {
            Lista camActual = new Lista();
            double[] kmResultado = {0.0};
            resultado = caminoCortoEtiquetaAux(auxOrig, destino, resultado, kmResultado, camActual, 0);
        }
        return resultado;
    }

    private Lista caminoCortoEtiquetaAux(NodoVert nodo, Object destino, Lista resultado, double[] kmResultado, Lista camActual, double kmActual){
        //Metodo auxiliar, recursivamente recorre nodos de un grafo, retorna el camino con menos km
        if (nodo != null) {
            //Verifica si no supera al mas corto (en suma de etiquetas) con la siguiente insercion (ya que no tendria sentido seguir buscando en ese punto)
            if (resultado.esVacia() || (kmActual < kmResultado[0])) {
                Object elem = nodo.getElemento(); 
                camActual.insertar(elem, camActual.longitud()+1); //Inserta nodo actual (pues es recorrido)
                if (elem.equals(destino)) { //Si encuentra destino
                    resultado = camActual.clone(); //Clona camActual para no modificar la referencia
                    kmResultado[0] = kmActual; //Modifica el nuevo
                } else { // si no lo encuentra recorre sus adyacentes para buscar el camino
                    NodoAdy ady = nodo.getPrimerAdyacente();
                    NodoVert auxVert = null;
                    while (ady != null) { //Recorro sus adyacentes (arcos)
                        auxVert = ady.getVertice(); 
                        //Si no fue visitado en este camino, entonces lo visita
                        if (camActual.localizar(auxVert.getElemento()) < 0) { 
                            resultado = caminoCortoEtiquetaAux(ady.getVertice(), destino, resultado, kmResultado, camActual, kmActual+ady.getEtiqueta()); 
                            
                        }
                        ady = ady.getSigAdyacente(); //Siguiente arco
                    }
                }
                //Quita nodo actual (ultimo insertado) porque puede haber mas caminos que lo visiten
                camActual.eliminar(camActual.longitud());
            }
        }
        return resultado;
    }
  
    public boolean verificarCaminoMenosKm(Object origen, Object destino, double kms) {        
        NodoVert aux = this.inicio; 
        NodoVert auxOrig = null;
        NodoVert auxDest = null;
        boolean respuesta = false;
        //No utilizo ubicarVertice dos veces pues es ineficiente
        while (aux != null && (auxOrig == null || auxDest == null)) { //Busco ambos nodos
            if (origen.equals(aux.getElemento())) { //Si encuentra origen
                auxOrig = aux;
            } else {
                if (destino.equals(aux.getElemento())) { //Si encuentra destino
                    auxDest = aux;
                }
            }
            aux = aux.getSigVertice(); //Siguiente vertice
        }
        if (auxOrig != null && auxDest != null) { //Fueron encontrados, busco un camino
            Lista camActual = new Lista();
            respuesta = verificarCaminoMenosKmAux(auxOrig, destino, camActual, 0, kms);
        }
        return respuesta;
    }

    private boolean verificarCaminoMenosKmAux(NodoVert nodo, Object destino, Lista camActual, double kmActual, double kms) {
        //Metodo auxiliar, recursivamente recorre nodos de un grafo, retorna verdadero si encuentra un camino que recorra menos de kms kilometros
        boolean respuesta = false;
        if (nodo != null) {
            //Verifica si no supera al mas corto (en suma de etiquetas) con la siguiente insercion (ya que no tendria sentido seguir buscando en ese punto)
            if (kmActual < kms) {
                Object elem = nodo.getElemento(); 
                camActual.insertar(elem, camActual.longitud()+1); //Inserta nodo actual (pues es recorrido)
                respuesta = elem.equals(destino); //Verifica si llego a destino
                if (!respuesta) { //Si no encuentra destino, sigue buscando
                    NodoAdy ady = nodo.getPrimerAdyacente();
                    NodoVert auxVert = null;
                    while (ady != null && !respuesta) { //Recorro sus adyacentes (arcos), hasta encontrar destino
                        auxVert = ady.getVertice(); 
                        //Si no fue visitado en este camino, entonces lo visita
                        if (camActual.localizar(auxVert.getElemento()) < 0) { 
                            respuesta = verificarCaminoMenosKmAux(ady.getVertice(), destino, camActual, kmActual+ady.getEtiqueta(), kms);
                        }
                        ady = ady.getSigAdyacente(); //Siguiente arco
                    }
                }
                //Quita nodo actual (ultimo insertado) porque puede haber mas caminos que lo visiten
                camActual.eliminar(camActual.longitud());
            }
        }
        return respuesta;
    }

    public Lista listarEnProfundidad() {
        //Visita cada vertice y lista en profundidad cada adyacente
        Lista visitados = new Lista();
        //Define vertice desde donde comenzar
        NodoVert aux = this.inicio;
        while (aux != null) { 
            if (visitados.localizar(aux.getElemento()) < 0) { //Si el vertice no fue visitado
                listarEnProfundidadAux(aux, visitados);
            }
            aux = aux.getSigVertice();
        }
        return visitados;
    }

    private void listarEnProfundidadAux(NodoVert nodo, Lista visitados) {
        //Lista cada nodo adyacente al nodo vertice ingresado si no fueron listados
        if (nodo != null) {
            //Marca nodo como visitado
            visitados.insertar(nodo.getElemento(), visitados.longitud()+1);
            NodoAdy ady = nodo.getPrimerAdyacente();
            while (ady != null) { //Visita a los adyacentes que no fueron visitados
                if (visitados.localizar(ady.getVertice().getElemento()) < 0) {
                    listarEnProfundidadAux(ady.getVertice(), visitados);
                }
                ady = ady.getSigAdyacente();
            }
        }
    }

    public Lista listarEnAnchura() {
        //Visita cada vertice y lista en anchura cada adyacente
        Lista visitados = new Lista();
        //Define vertice desde donde comenzar
        NodoVert aux = this.inicio;
        while (aux != null) { 
            if (visitados.localizar(aux.getElemento()) < 0) { //Si el vertice no fue visitado
                anchuraDesde(aux, visitados);
            }
            aux = aux.getSigVertice();
        }
        return visitados;
    }

    private void anchuraDesde(NodoVert inicio, Lista visitados) {
        Cola q = new Cola();
        visitados.insertar(inicio.getElemento(), visitados.longitud()+1);
        q.poner(inicio);
        while (!q.esVacia()) {
            NodoVert aux = (NodoVert) q.obtenerFrente();
            q.sacar();
            NodoAdy ady = aux.getPrimerAdyacente();
            while (ady != null) { //Para cada adyacente de aux (sacado de cola)
                NodoVert auxVert = ady.getVertice();
                if (visitados.localizar(auxVert.getElemento()) < 0) { //Si no fue visitado
                    //Visito y recorro por anchura
                    visitados.insertar(auxVert.getElemento(), visitados.longitud()+1);
                    q.poner(auxVert); //Sera agregado a la cola del primer while
                }
                ady = ady.getSigAdyacente();
            }
        }
    }
    
    public String toString(){
        String cad;
        if (this.esVacio()) {
            cad = "Grafo vacio";
        } else {
            cad = toStringAux(this.inicio);
        }
        return cad;
    }

    private String toStringAux(NodoVert nodo) {
        //Retorna una cadena con el contenido del grafo, nodo no debe ser vacio
        String ret = "";
        NodoAdy ady = nodo.getPrimerAdyacente();
        ret = "+ ("+nodo.getElemento()+"): \n       "; //Concatena vertice
        if (ady != null) { //Concatena todos sus adyacentes
            while (ady != null){
                ret = ret + "->  ("+ady.getVertice().getElemento()+"): "+ady.getEtiqueta()+"\n       ";
                ady = ady.getSigAdyacente();
            }
        } else {
            ret = ret + "------ \n";
        }
        nodo = nodo.getSigVertice(); //Concatena siguiente vertice
        if (nodo != null){
            ret = ret + "\n "+toStringAux(nodo);
        }
        return ret;
    }

    /*Clone: clonar cada nodo vertice y sus adyacentes*/

}