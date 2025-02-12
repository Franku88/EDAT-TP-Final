package sistema;

import java.io.*;
import java.util.HashMap;
import java.util.StringTokenizer;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import estructuras.diccionario.Diccionario;
import estructuras.grafos.GrafoEtiquetado;
import estructuras.mapeo.MapeoAMuchos;

public class DataIO {
    
    private String exeTimestamp; 
    private String logPath;
    private String dataPath;

    public DataIO() {
        this.exeTimestamp = (LocalDateTime.now()).format(DateTimeFormatter.ofPattern("yyyy-MM-dd_HHmmss")); //Toma timestamp actual
        this.logPath = "src/sistema/logs/log"+this.exeTimestamp+".txt";
        this.dataPath = "src/sistema/data/datos.txt";
    }

    public String getExeTimestamp() {
        return this.exeTimestamp;
    }

    public void cargarDatos(Diccionario ciudades, GrafoEtiquetado mapa, HashMap<String, Cliente> clientes, MapeoAMuchos solicitudes){
        try {
            BufferedReader archivo = new BufferedReader(new FileReader(this.dataPath));
            String linea = archivo.readLine(); 

            while (linea != null) {
                analizarLinea(linea, ciudades, mapa, clientes, solicitudes);
                linea = archivo.readLine();
            }
            archivo.close();
            escribir("Carga inicial completada");
        } catch (FileNotFoundException ex) {
            System.err.println(ex.getMessage());
        } catch (IOException ex) {
            System.err.println("Error leyendo o escribiendo en algun archivo.");
        }
    }

    public void crearLog() {
        // Crea archivo log.txt vacio (lo reemplaza si este existiera)
        try {
            PrintWriter log = new PrintWriter(new File(this.logPath));
            log.close(); // Lo cierra pues no se escribira nada en el mismo
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void escribir(String entrada) {
        // Escribe el String ingresado por parametro en una nueva linea del archivo especificado en FileWriter
        // En este caso, siempre escribe en log.txt
		FileWriter fichero = null;
        PrintWriter pw = null;
        // System.out.println(entrada);

        try {
            // Intenta obtener archivo de ruta especificada
            fichero = new FileWriter(this.logPath, true);
            
            // Usa PrintWriter para escribir en el fichero obtenido
            pw = new PrintWriter(fichero);
            // Obtengo timestamp actual
            String timestamp = (LocalDateTime.now()).format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            // Escribe en el archivo con pw
            pw.println(timestamp+" <|> "+entrada);
        } catch (Exception e) { // Excepción durante apertura, escritura o cierre del archivo 
            e.printStackTrace();
        } finally { // Cierro el archivo
            try {
                if (fichero != null) { // Si el archivo fue encontrado
                    fichero.close();
                }
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
	}

    public void analizarLinea(String linea, Diccionario ciudades, GrafoEtiquetado mapa, HashMap<String, Cliente> clientes, MapeoAMuchos solicitudes) {
        /* Metodo que analiza una linea y crea un objeto segun especificaciones previas
        linea: String que contiene atributos de un objeto separados por ';' segun un formato dado
        mapa: Grafo de rutas 
        ciudades: Diccionario con informacion de ciudades 
        solicitudes: MapeoAMuchos con solicitudes como Rangos y codOrigen+codDestino como dominios
        clientes: HashMap con informacion de clientes */

        // Tokeniza linea ingresada por parametro
        StringTokenizer tokens = new StringTokenizer(linea, ";");
        if (tokens.hasMoreTokens()) {
            String op = tokens.nextToken().trim();
            switch (op) {
                case "C": // Ciudad: C;CodigoPostal;Nombre;Provincia (ej: C;8300;Neuquén;Neuquén)
                    int codigoPostal = Integer.parseInt(tokens.nextToken().trim());
                    String nombre = tokens.nextToken().trim();
                    String provincia = tokens.nextToken().trim();
                    
                    Ciudad ciu = new Ciudad(codigoPostal, nombre, provincia);
                    if (ciudades.insertar(codigoPostal, ciu) && mapa.insertarVertice(codigoPostal)) {
                        this.escribir("CIUDAD CARGADA: "+ciu.toString());
                    }
                    break;
                case "R": // Ruta: R;CodPos1;CodPos2;Kms (ej: R;5000;8324;1108.5)
                    // Verifica que exista dicha ciudad
                    int codPos1 = Integer.parseInt(tokens.nextToken().trim());
                    if(ciudades.existeClave(codPos1)) {
                        // Verifica que exista dicha ciudad
                        int codPos2 = Integer.parseInt(tokens.nextToken().trim());
                        if (ciudades.existeClave(codPos2)) {
                            double kms = Double.parseDouble(tokens.nextToken().trim().replace(",","."));
                            if (mapa.insertarArco(codPos1, codPos2, kms)) { // Inserta en una sola direccion pues no es un digrafo (acepta ambas direcciones)
                                this.escribir("RUTA CARGADA: ("+codPos1+") <--- "+kms+" km ---> ("+codPos2+")");
                            } else {
                                this.escribir("NO FUE POSIBLE CARGAR LA RUTA: ("+codPos1+") <--- "+kms+" km ---> ("+codPos2+")");
                            }
                        } else {
                            this.escribir("NO FUE POSIBLE CARGAR LA RUTA. : ("+codPos2+") CIUDAD NO ENCONTRADA");
                        }
                    } else {
                        this.escribir("NO FUE POSIBLE CARGAR LA RUTA. : ("+codPos1+") CIUDAD NO ENCONTRADA");
                    }
                    break;
                case "P": // Cliente: P;TipoDoc;NumDoc;Apellidos;Nombres;Telefono;Email (ej: P;DNI;35678965;FERNANDEZ;JUAN CARLOS;299-4495117;jcarlos@mail.com)
                    String tipoDoc = tokens.nextToken().trim();
                    String nroDoc = tokens.nextToken().trim();
                    if(!clientes.containsKey(tipoDoc+""+nroDoc)) {
                        String ape = tokens.nextToken().trim();
                        String nom = tokens.nextToken().trim();
                        String tel = tokens.nextToken().trim();
                        String mail = tokens.nextToken().trim();

                        Cliente cli = new Cliente(tipoDoc, nroDoc, nom, ape, tel, mail);
                        clientes.put(tipoDoc+""+nroDoc, cli);
                        this.escribir("CLIENTE CARGADO: "+cli.toString());
                    } else {
                        this.escribir("CLIENTE "+tipoDoc+"."+nroDoc+" YA SE ENCUENTRA REGISTRADO.");
                    }
                    break;
                case "S": // Solicitud: S;CodOrigen;CodDestino;FechaSolicitud;TipoCliente;DniCliente;MCubicos;Bultos;DomRetiro;DomEntrega;EnvioPago (ej S;5000;8300;15/06/2023;DNI;35678965;13;5;Sarmiento 3400;Roca 2100;T)
                    int codOrigen = Integer.parseInt(tokens.nextToken().trim());
                    Ciudad ciudadOrigen = (Ciudad) ciudades.obtenerDato(codOrigen);
                    if (ciudadOrigen != null) {
                        int codDestino = Integer.parseInt(tokens.nextToken().trim());
                        Ciudad ciudadDestino = (Ciudad) ciudades.obtenerDato(codDestino);
                        if (ciudadDestino != null) {
                            String fechaSolicitud = tokens.nextToken().trim();
                            String tipoCliente = tokens.nextToken().trim();
                            String nroCliente = tokens.nextToken().trim();

                            Cliente cliente = clientes.get(tipoCliente+""+nroCliente);

                            if (cliente != null) {
                                double metrosCubicos = Double.parseDouble(tokens.nextToken().trim().replace(",","."));
                                int bultos = Integer.parseInt(tokens.nextToken().trim());
                                String domRetiro = tokens.nextToken().trim();
                                String domEntrega = tokens.nextToken().trim();
                                boolean envioPago = tokens.nextToken().trim().equalsIgnoreCase("T");

                                Solicitud soli = new Solicitud(codOrigen, codDestino, fechaSolicitud, tipoCliente, nroCliente, metrosCubicos, bultos, domRetiro, domEntrega, envioPago);

                                if (solicitudes.asociar(codOrigen+""+codDestino, soli)) {
                                    this.escribir("SOLICITUD REGISTRADA. ORIGEN: "+codOrigen+" DESTINO: "+codDestino+" FECHA: "+fechaSolicitud+" CLIENTE: "+tipoCliente+""+nroCliente+" BULTOS: "+bultos+" VOLUMEN: "+metrosCubicos+" m^3");
                                }

                            } else {
                                this.escribir("CLIENTE NO ENCONTRADO, NO SE INSERTARÁ SOLICITUD. CLIENTE ESPECIFICADO: "+(tipoCliente+""+nroCliente));
                            }
                        } else {
                            this.escribir("DESTINO NO ENCONTRADO, NO SE INSERTARÁ SOLICITUD. DESTINO ESPECIFICADO: "+codDestino);
                        }
                    } else {
                        this.escribir("ORIGEN NO ENCONTRADO, NO SE INSERTARÁ SOLICITUD. ORIGEN ESPECIFICADO: "+codOrigen);
                    }
                    break;
            }
        } else {
            this.escribir("Linea vacia.");
        }
    }
}