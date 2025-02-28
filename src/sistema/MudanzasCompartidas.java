package sistema;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Scanner;
import java.util.regex.Pattern;

import estructuras.diccionario.Diccionario;
import estructuras.grafos.GrafoEtiquetado;
import estructuras.mapeo.MapeoAMuchos;
import estructuras.lineales.Lista;

public class MudanzasCompartidas {
    // ConsultarViaje lista. Sigue Verificar Viajes.
    private static final Scanner sc = new Scanner(System.in);
    private static final DataIO io = new DataIO(); // Objeto para la entrada y salida de datos
    private static Diccionario ciudades = new Diccionario(); // Informacion sobre ciudades, cada ciudad almacena su lista de solicitudes
    private static GrafoEtiquetado mapa = new GrafoEtiquetado(); // Mapa de rutas, nodos codCiudad y etiquetas con km entre ciudades
    private static MapeoAMuchos solicitudes = new MapeoAMuchos(); // MapeoAMuchos (Dominio: codOrigen+codDestino, Rangos: Solicitudes)
    private static HashMap<String, Cliente> clientes = new HashMap<String, Cliente>(); // HashMap de clientes (clave con su objeto Cliente)

    public static void menu() throws FileNotFoundException, IOException {
        int opcion = 0;
        io.crearLog();

        do {
            System.out.println("---------------- Menu ----------------");
            System.out.println("    1. Carga inicial del sistema");
            System.out.println("*------------------------------------*");
            System.out.println("    2. ABM Ciudades");
            System.out.println("    3. ABM Red de rutas");
            System.out.println("    4. ABM Clientes");
            System.out.println("    5. ABM Solicitudes");
            System.out.println("*------------------------------------*");
            System.out.println("    6. Consultar Cliente");
            System.out.println("    7. Consultar Ciudad");
            System.out.println("    8. Consultar Viaje");
            System.out.println("    9. Verificar Viaje");
            System.out.println("--------------------------------------");
            System.out.println("    10. Mostrar Sistema");
            System.out.println("--------------------------------------");
            System.out.println("    0. Salir");
            System.out.println("--------------------------------------");
            
            System.out.print("Ingrese una opción: ");
            opcion = sc.nextInt();
            clearTerminal();
            System.out.println("Opcion "+opcion+" seleccionada.");
            switch (opcion) {
                case 0:
                    // Corta la iteracion
                    System.out.println();
                    System.out.println("<---------- ¡Hasta pronto! ---------->");
                    System.out.println();
                    break;
                case 1:
                    System.out.println();
                    cargaInicial();
                    System.out.println();
                    break;
                case 2:
                    System.out.println();
                    ABMCiudades();
                    break;
                case 3:
                    System.out.println();
                    ABMRutas();
                    System.out.println();
                    break;
                case 4:
                    System.out.println();
                    ABMClientes();
                    System.out.println();
                    break;
                case 5:
                    System.out.println();
                    ABMSolicitudes();
                    System.out.println();
                    break;
                case 6:
                    System.out.println();
                    consultarCliente();
                    System.out.println();
                    break;
                case 7:
                    System.out.println();
                    consultarCiudad();
                    System.out.println();
                    break;
                case 8:
                    System.out.println();
                    consultarViaje();
                    System.out.println();
                    break;
                case 9:
                    System.out.println();
                    verificarViaje();
                    System.out.println();
                    break;
                case 10:
                    mostrarSistema();
                    break;
                default:
                    System.out.println("Valor ingresado no es válido.");
                    break;
            }            
        } while (opcion != 0);
        io.escribir("EJECICIÓN FINALIZADA.");
    }

    public static void cargaInicial() {
        // Opcion 1 del menu
        io.cargarDatos(ciudades, mapa, clientes, solicitudes);
    }

    public static void ABMCiudades() {
        int opcion = 0;
        do {
            System.out.println("---------------- Ciudades ----------------");
            System.out.println("    1. Cargar Ciudad");
            System.out.println("    2. Eliminar Ciudad");
            System.out.println("    3. Modificar Ciudad");
            System.out.println("--------------------------------------");
            System.out.println("    4. Mostrar Ciudades");
            System.out.println("--------------------------------------");
            System.out.println("    0. Atras");
            System.out.println("--------------------------------------");
            
            System.out.print("Ingrese una opción: ");
            opcion = sc.nextInt();
            clearTerminal();
            switch (opcion) {
                case 0:
                    // No realiza nada y corta la iteracion
                    break;
                case 1:
                    cargarCiudad();
                    break;
                case 2:
                    eliminarCiudad();
                    break;
                case 3:
                    modificarCiudad();
                    break;
                case 4:
                    mostrarCiudades();
                    break;
                default:
                    System.out.println("Valor ingresado no es válido.");
                    break;
            }
        } while (opcion != 0);
    }

    public static void cargarCiudad() {
        int codPostal = -1;
        String nombreCiudad = "";
        String nombreProvincia = "";
        boolean existe = true;

        System.out.println("<----------- Cargando Ciudad --------->");
        System.out.println("Se le solicitará: Código Postal, Nombre de Ciudad, Nombre de Provincia");
        
        do {
            System.out.print("Ingrese el código postal (4 dígitos): ");
            codPostal = sc.nextInt();
            sc.nextLine();
            if (codPostal >= 0) {
                existe = ciudades.existeClave(codPostal);
                if (existe) {
                    System.out.println("El codigo ingresado ya se encuentra registrado.");
                } else {
                    System.out.print("Ingrese el nombre de la ciudad: ");
                    nombreCiudad = sc.nextLine().trim();
                    System.out.print("Ingrese el nombre de la provincia: ");
                    nombreProvincia = sc.nextLine().trim();
                    
                    Ciudad nuevaCiudad = new Ciudad(codPostal, nombreCiudad, nombreProvincia);
                    System.out.println("Ciudad cargada: "+ nuevaCiudad.toString());
                    if (ciudades.insertar(codPostal, nuevaCiudad) && mapa.insertarVertice(codPostal)) {
                        io.escribir("CIUDAD CARGADA: "+nuevaCiudad.toString());
                    }
                }
            } else {
                System.out.println("El codigo ingresado debe ser positivo.");
            }
        } while (existe);
    }

    public static void eliminarCiudad() {
        int codPostal = -1;
        Ciudad ciu = null;
        System.out.println("<----------- Eliminando Ciudad --------->");
        System.out.println("Se le solicitará: Código Postal de una ciudad cargada.");
        System.out.println("Ingrese un numero menor a 0 para cancelar esta operación.");
        do {
            System.out.print("Ingrese el código postal (4 dígitos): ");
            codPostal = sc.nextInt();
            sc.nextLine();
            if (codPostal >= 0) {
                ciu = (Ciudad) ciudades.obtenerDato(codPostal); // Obtiene objeto ciudad
                if (ciu != null) {
                    System.out.print("¿Esta seguro? Se eliminara del mapa junto a sus rutas y solicitudes (S/N): ");
                    switch (sc.next().toUpperCase().charAt(0)) {
                        case 'S':
                            if (ciudades.eliminar(codPostal) && mapa.eliminarVertice(codPostal)) {
                                eliminarSolicitudesConCiudad(codPostal);
                                io.escribir("CIUDAD ELIMINADA: "+ciu.toString());
                                System.out.println("Ciudad eliminada con éxito.");
                            } else {
                                io.escribir("ERROR AL ELIMINAR CIUDAD: "+ciu.toString());
                            }
                            break;
                        default:
                            System.out.println("Operación cancelada.");
                            codPostal = -1;
                            break;
                    }
                } else {
                    if (codPostal >= 0) {
                        System.out.println("El codigo ingresado no se encuentra registrado");
                    }
                }
            } else {
                System.out.println("Operación cancelada.");
            }
        } while (codPostal >= 0 && ciu == null);
    }

    public static void eliminarSolicitudesConCiudad(int codCiudad) {
        // Elimina toda solicitud (lista de solicitudes) cuando codCiudad se encuentra en el dominio
        Lista dominios = solicitudes.obtenerConjuntoDominio(); //Obtiene conjunto dominio
        int cantDominios = dominios.longitud();
        String unDom = "";
        for (int i = 1; i <= cantDominios; i++) { 
            unDom = (String) dominios.recuperar(i); // Obtiene un dominio
            if (unDom.contains(codCiudad+"")) { // Si el dominio tiene codCiudad (origen/destino)
                io.escribir("SOLICITUDES ELIMINADAS: "+ solicitudes.obtenerValores(unDom).toString());
                solicitudes.eliminar(unDom); // Elimina dicho dominio (junto su lista de solicitudes)
            }
        }
    }

    public static void modificarCiudad() {
        // En este caso, se puede modificar nombre de ciudad y nombre de provincia
        int codPostal = -1;
        Ciudad ciu = null;
        System.out.println("<----------- Modificando Ciudad --------->");
        System.out.println("Se le solicitará: Código Postal de una ciudad cargada.");
        System.out.println("Ingrese un numero menor a 0 para cancelar esta operación.");
        do {
            System.out.print("Ingrese el código postal (4 dígitos): ");
            codPostal = sc.nextInt();
            sc.nextLine();
            if (codPostal >= 0) {
                ciu = (Ciudad) ciudades.obtenerDato(codPostal); // Obtiene objeto ciudad
                if (ciu != null) {
                    String prevData = ciu.toString();
                    String laterData = prevData;
                    int opcion = 0;
                    System.out.println("-------- Modificando: "+ciu.getCodigoPostal()+" --------");
                    System.out.println("    1. Nombre de Ciudad: "+ciu.getNombre());
                    System.out.println("    2. Nombre de Provincia: "+ciu.getProvincia());
                    System.out.println("--------------------------------------");
                    System.out.println("    0. Listo");
                    System.out.println("--------------------------------------");
                    do {
                        System.out.print("¿Que dato desea modificar?: ");
                        opcion = sc.nextInt();
                        sc.nextLine();
                        switch (opcion) {
                            case 0:
                                //No realiza accion y vuelve al menu anterior
                                break;
                            case 1:
                                System.out.print("Ingrese nuevo nombre de ciudad (Actual: "+ciu.getNombre()+"): ");
                                ciu.setNombre(sc.nextLine().trim());
                                System.out.println("Nombre de ciudad modificado.");
                                break;
                            case 2:
                                System.out.print("Ingrese nuevo nombre de provincia (Actual: "+ciu.getProvincia()+"): ");
                                ciu.setProvincia(sc.nextLine().trim());
                                System.out.println("Nombre de provincia modificado.");
                                break;
                            default:
                                System.out.println("Valor ingresado no es válido.");
                                codPostal = -1;
                                break;
                        }    
                    } while (opcion != 0);
                    laterData = ciu.toString();
                    if (!prevData.equals(laterData)) {
                        io.escribir("CIUDAD MODIFICADA: ANTES: "+prevData+" AHORA: "+laterData);
                    }
                } else {
                    if (codPostal >= 0) {
                        System.out.println("El codigo ingresado no se encuentra registrado");
                    }
                }
            } else {
                System.out.println("Operación cancelada.");
            }
        } while (codPostal >= 0 && ciu == null);
    }

    public static void mostrarCiudades() {
        System.out.println("<------------- Ciudades ------------->");
        System.out.println(ciudades.toString());
        System.out.println();
    }

    public static void ABMRutas() {
        int opcion = 0;
        do {
            System.out.println("---------------- Rutas ----------------");
            System.out.println("    1. Cargar Ruta");
            System.out.println("    2. Eliminar Ruta");
            System.out.println("    3. Modificar Ruta");
            System.out.println("--------------------------------------");
            System.out.println("    4. Mostrar Rutas");
            System.out.println("--------------------------------------");
            System.out.println("    0. Atras");
            System.out.println("--------------------------------------");
            System.out.print("Ingrese una opción: ");
            opcion = sc.nextInt();
            clearTerminal();
            switch (opcion) {
                case 0:
                    // No realiza nada y corta la iteracion
                    break;
                case 1:
                    cargarRuta();
                    break;
                case 2:
                    eliminarRuta();
                    break;
                case 3:
                    modificarRuta();
                    break;
                case 4:
                    mostrarRutas();
                    break;
                default:
                    System.out.println("Valor ingresado no es válido.");
                    break;
            }
        } while (opcion != 0);
    }

    public static void cargarRuta() {
        int primerCodPostal = -1;
        int segundoCodPostal = -1;
        double kms = 0;
        boolean existe = false;

        System.out.println("<----------- Cargando Ruta --------->");
        System.out.println("Se le solicitará: Códigos postales de dos ciudades cargadas, Distancia en kilometros de la ruta");
        
        do {
            System.out.print("Ingrese el primer código postal (4 dígitos): ");
            primerCodPostal = sc.nextInt();
            sc.nextLine();
            if (primerCodPostal >= 0) {
                existe = ciudades.existeClave(primerCodPostal);
                if (existe) {
                    existe = false;
                    do {
                        System.out.print("Ingrese el segundo código postal (4 dígitos): ");
                        segundoCodPostal = sc.nextInt();
                        sc.nextLine();
                        if (segundoCodPostal >= 0 && segundoCodPostal != primerCodPostal) { //Deben ser diferentes
                            existe = ciudades.existeClave(segundoCodPostal);
                            if (existe) {
                                if (mapa.existeArco(primerCodPostal, segundoCodPostal)) {
                                    System.out.println("Ya existe una ruta entre las ciudades dadas. Modifique la misma o eliminela para insertar otra.");
                                } else {
                                    existe = false;
                                    do {
                                        System.out.print("Ingrese la distancia en kilometros de la ruta (ej: 10.2): ");
                                        kms = sc.nextDouble();
                                        sc.nextLine();
                                        existe = kms > 0;
                                        if (existe) {
                                            mapa.insertarArco(primerCodPostal, segundoCodPostal, kms);
                                            io.escribir("RUTA CARGADA: ("+primerCodPostal+") <--- "+kms+" km ---> ("+segundoCodPostal+")");
                                            System.out.println("Ruta cargada con éxito.");
                                        } else {
                                            System.out.println("La distancia debe ser positiva.");
                                        }
                                    } while(!existe);
                                }
                            } else { 
                                System.out.println("El codigo ingresado no se encuentra registrado.");
                            }
                        } else {
                            System.out.println("El codigo ingresado debe ser positivo y diferente al anterior ingresado.");
                        }
                    } while (!existe);
                } else {
                    System.out.println("El codigo ingresado no se encuentra registrado.");
                }
            } else {
                System.out.println("El codigo ingresado debe ser positivo.");
            }
        } while (!existe);
    }

    public static void eliminarRuta() {
        int primerCodPostal = -1;
        int segundoCodPostal = -1;
        boolean existe = false;

        System.out.println("<----------- Eliminando Ruta --------->");
        System.out.println("Se le solicitará: Códigos postales de dos ciudades cargadas, las cuales tengan una ruta en comun.");
        do {
            System.out.print("Ingrese el primer código postal (4 dígitos): ");
            primerCodPostal = sc.nextInt();
            sc.nextLine();
            if (primerCodPostal >= 0) {
                existe = ciudades.existeClave(primerCodPostal);
                if (existe) {
                    existe = false;
                    do {
                        System.out.print("Ingrese el segundo código postal (4 dígitos): ");
                        segundoCodPostal = sc.nextInt();
                        sc.nextLine();
                        if (segundoCodPostal >= 0 && segundoCodPostal != primerCodPostal) { //Deben ser diferentes
                            existe = ciudades.existeClave(segundoCodPostal);
                            if (existe) {
                                if (mapa.existeArco(primerCodPostal, segundoCodPostal)) {
                                    System.out.print("¿Esta seguro? Puede verificar la ruta a eliminar visualizando el mapa (S/N): ");
                                    switch (sc.next().toUpperCase().charAt(0)) {
                                        case 'S':
                                            if (mapa.eliminarArco(primerCodPostal, segundoCodPostal)) {
                                                io.escribir("RUTA ELIMINADA: ("+primerCodPostal+") <--- / --->  ("+segundoCodPostal+")");
                                                System.out.println("Ruta eliminada con éxito.");
                                            } else {
                                                io.escribir("ERROR AL ELIMINAR RUTA.");
                                            }
                                            break;
                                        default:
                                            System.out.println("Operación cancelada.");                                            
                                            break;
                                    }
                                } else {
                                    System.out.println("No existe ruta entre las ciudades dadas.");
                                }
                            } else { 
                                System.out.println("El codigo ingresado no se encuentra registrado.");
                            }
                        } else {
                            System.out.println("El codigo ingresado debe ser positivo y diferente al anterior ingresado.");
                        }
                    } while (!existe);
                } else {
                    System.out.println("El codigo ingresado no se encuentra registrado.");
                }
            } else {
                System.out.println("El codigo ingresado debe ser positivo.");
            }
        } while (!existe);
    }

    public static void modificarRuta() {
        // En este caso, se puede modificar etiqueta (kms)
        int primerCodPostal = -1;
        int segundoCodPostal = -1;
        double kms = -1;
        boolean existe = false;

        System.out.println("<----------- Modificando Ruta --------->");
        System.out.println("Se le solicitará: Códigos postales de dos ciudades cargadas, las cuales tengan una ruta en comun.");
        do {
            System.out.print("Ingrese el primer código postal (4 dígitos): ");
            primerCodPostal = sc.nextInt();
            sc.nextLine();
            if (primerCodPostal >= 0) {
                existe = ciudades.existeClave(primerCodPostal);
                if (existe) {
                    existe = false;
                    do {
                        System.out.print("Ingrese el segundo código postal (4 dígitos): ");
                        segundoCodPostal = sc.nextInt();
                        sc.nextLine();
                        if (segundoCodPostal >= 0 && segundoCodPostal != primerCodPostal) { //Deben ser diferentes
                            existe = ciudades.existeClave(segundoCodPostal);
                            if (existe) {
                                if (mapa.existeArco(primerCodPostal, segundoCodPostal)) {
                                    do {
                                        System.out.print("Ingrese la nueva distancia en kilometros de la ruta (ej: 10.2): ");
                                        kms = sc.nextDouble();
                                        sc.nextLine();
                                        existe = kms > 0;
                                        if (existe) {
                                            System.out.print("¿Esta seguro? Puede verificar la ruta a modificar visualizando el mapa (S/N): ");
                                            switch (sc.next().toUpperCase().charAt(0)) {
                                                case 'S':
                                                    if (mapa.eliminarArco(primerCodPostal, segundoCodPostal)) {
                                                        if (mapa.insertarArco(primerCodPostal, segundoCodPostal, kms)) {
                                                            io.escribir("RUTA MODIFICADA: ("+primerCodPostal+") <--- "+kms+" km --->  ("+segundoCodPostal+")");
                                                            System.out.println("Ruta modificada con éxito.");
                                                        } else {
                                                            io.escribir("ERROR AL INSERTAR RUTA.");
                                                        }
                                                    } else {
                                                        io.escribir("ERROR AL ELIMINAR RUTA.");
                                                    }
                                                    break;
                                                default:
                                                    System.out.println("Operación cancelada.");
                                                    break;
                                            }
                                        } else {
                                            System.out.println("La distancia debe ser positiva.");
                                        }
                                    } while (!existe);

                                } else {
                                    System.out.println("No existe ruta entre las ciudades dadas.");
                                }
                            } else { 
                                System.out.println("El codigo ingresado no se encuentra registrado.");
                            }
                        } else {
                            System.out.println("El codigo ingresado debe ser positivo y diferente al anterior ingresado.");
                        }
                    } while (!existe);
                } else {
                    System.out.println("El codigo ingresado no se encuentra registrado.");
                }
            } else {
                System.out.println("El codigo ingresado debe ser positivo.");
            }
        } while (!existe);
    }

    public static void mostrarRutas() {
        System.out.println("<--------------- Rutas --------------->");
        System.out.println(mapa.toString());
        System.out.println();
    }

    public static void ABMClientes() {
        int opcion = 0;
        do {
            System.out.println("---------------- Clientes ----------------");
            System.out.println("    1. Cargar Cliente");
            System.out.println("    2. Eliminar Cliente");
            System.out.println("    3. Modificar Cliente");
            System.out.println("--------------------------------------");
            System.out.println("    4. Mostrar Clientes");
            System.out.println("--------------------------------------");
            System.out.println("    0. Atras");
            System.out.println("--------------------------------------");
            System.out.print("Ingrese una opción: ");
            opcion = sc.nextInt();
            clearTerminal();
            switch (opcion) {
                case 0:
                    // No realiza nada y corta la iteracion
                    break;
                case 1:
                    cargarCliente();
                    break;
                case 2:
                    eliminarCliente();
                    break;
                case 3:
                    modificarCliente();
                    break;
                case 4:
                    mostrarClientes();
                    break;
                default:
                    System.out.println("Valor ingresado no es válido.");
                    break;
            }
        } while (opcion != 0);
    }

    public static void cargarCliente() {
        String tipoDoc = "";
        int nroDoc = -1;
        int opcion = 0;
        String nombreCliente = "";
        String apellidoCliente = "";
        String telefono = "";
        String correo = "";
        boolean flag = false; // Decide si sigue la operacion o si se repite la entrada de datos

        System.out.println("<----------- Cargando Cliente --------->");
        System.out.println("Se le solicitará: Tipo de Documento, Numero de Documento, Nombre/s, Apellido/s, Telefono (opcional), E-mail (opcional)");
        
        System.out.println("------------ Tipos de Documento ------------");
        System.out.println("    1. Documento Nacional de Identidad");
        System.out.println("    2. Pasaporte");
        do {
            System.out.print("Ingrese el tipo de documento: ");
            opcion = sc.nextInt();
            sc.nextLine();
            flag = opcion > 0 && opcion < 3;
            if (flag) {
                switch (opcion) {
                    case 1:
                        tipoDoc = "DNI";
                        break;
                    case 2:
                        tipoDoc = "PAS";
                        break;
                }
                do {
                    System.out.print("Ingrese el numero de documento: ");
                    nroDoc = sc.nextInt();
                    sc.nextLine();
                    flag = nroDoc > 0;
                    if (flag) {
                        if (!clientes.containsKey(tipoDoc+""+nroDoc)) {
                            System.out.print("Ingrese nombre/s del cliente (Separados por espacios): ");
                            nombreCliente = sc.nextLine().trim();

                            System.out.print("Ingrese apellido/s del cliente (Separados por espacios): ");
                            apellidoCliente = sc.nextLine().trim();

                            System.out.print("Ingrese telefono del cliente (Enter para omitir): ");
                            telefono = sc.nextLine().trim();

                            System.out.print("Ingrese correo electrónico del cliente (Enter para omitir): ");
                            correo = sc.nextLine().trim();

                            Cliente nuevoCliente = new Cliente(tipoDoc, nroDoc+"", nombreCliente, apellidoCliente, telefono, correo);
                            clientes.put((tipoDoc+""+nroDoc), nuevoCliente);
                            System.out.println("Cliente cargado: "+ nuevoCliente.toString());
                            io.escribir("CLIENTE CARGADO: "+nuevoCliente.toString());
                        } else {
                            System.out.println("Ya existe un cliente con tipo y numero de documento ingresados. Volviendo al menu...");                            
                        }
                    } else {
                        System.out.println("El numero ingresado debe ser positivo.");
                    }
                } while (!flag);
            } else {
                System.out.println("Opcion inválida.");
            }            
        } while (!flag);
    }

    public static void eliminarCliente() {
        String tipoDoc = "";
        int nroDoc = -1;
        int opcion = 0;
        boolean flag = false; // Decide si sigue la operacion o si se repite la entrada de datos
        Cliente cli = null;
        do {
            clearTerminal();
            System.out.println("<----------- Eliminando Cliente --------->");
            System.out.println("Se le solicitará: Tipo de Documento, Numero de Documento");
            
            System.out.println("------------ Tipos de Documento ------------");
            System.out.println("    0. Salir");
            System.out.println("    1. Documento Nacional de Identidad");
            System.out.println("    2. Pasaporte");
            System.out.print("Ingrese el tipo de documento: ");
            opcion = sc.nextInt();
            sc.nextLine();
            switch (opcion) {
                case 0:
                    tipoDoc = "";
                    break;
                case 1:
                    tipoDoc = "DNI";
                    break;
                case 2:
                    tipoDoc = "PAS";
                    break;
                default:
                    tipoDoc = "";
                    break;
            }
            flag = tipoDoc != "";
            if (flag) {
                System.out.println("Ingrese un número menor o igual a 0 para cancelar esta operación.");
                do {
                    System.out.print("Ingrese el número de documento: ");
                    nroDoc = sc.nextInt();
                    sc.nextLine();
                    if (nroDoc > 0) {
                        cli = clientes.get(tipoDoc+""+nroDoc);
                        if (cli != null) {
                            System.out.println("Cliente a eliminar: "+cli.toString());
                            System.out.print("¿Esta seguro que desea eliminarlo? Se eliminaran todas sus solicitudes (S/N): ");
                                switch (sc.next().toUpperCase().charAt(0)) {
                                    case 'S':
                                        eliminarSolicitudesConCliente(tipoDoc+""+nroDoc);
                                        clientes.remove(tipoDoc+""+nroDoc);
                                        io.escribir("CLIENTE ELIMINADO: "+cli.toString());
                                        System.out.println("Cliente eliminado con éxito.");
                                        break;
                                    default:
                                        System.out.println("Operación cancelada.");
                                        break;
                                }
                        } else {
                            System.out.println("No existe un cliente con tipo y numero de documento ingresados.");
                        }
                    } else {
                        System.out.println("Operación cancelada.");
                    }
                } while (nroDoc > 0 && cli == null);
            } else {
                if (opcion != 0) {
                    System.out.println("Opcion inválida.");
                }
            }         
        } while (!flag && opcion != 0);
    }

    public static void eliminarSolicitudesConCliente(String idCliente) {
        // Elimina toda solicitud (lista de solicitudes) cuando cliente con idCliente (tipoDoc+nroDoc) sea duenio de la solicitud
        Lista rangos = solicitudes.obtenerConjuntoRango(); //Obtiene conjunto rango
        int cantRangos = rangos.longitud();
        Solicitud unRango = null; //Cada rango es una solicitud
        for (int i = 1; i <= cantRangos; i++) { 
            unRango = (Solicitud) rangos.recuperar(i); // Obtiene un rango
            if ((unRango.getTipoDoc()+""+unRango.getNumeroDoc()).compareTo(idCliente) == 0) { // Si el rango tiene idCliente (tipoDoc+nroDoc)
                io.escribir("SOLICITUD ELIMINADA: "+ unRango.toString());
                solicitudes.desasociar(unRango.getOrigen()+""+unRango.getDestino(), unRango); // Elimina dicho rango (solicitud)
            }
        }
    }

    public static void modificarCliente() {
        // En este caso, se puede modificar nombres, apellidos, telefono y correo
        int nroDoc = -1;
        int opcion = -1;
        String tipoDoc = "";
        boolean flag = false;
        Cliente cli = null;
        System.out.println("<----------- Modificando Cliente --------->");
        System.out.println("Se le solicitará: Tipo y Numero de documento de un cliente cargado.");

        System.out.println("------------ Tipos de Documento ------------");
        System.out.println("    0. Salir");
        System.out.println("    1. Documento Nacional de Identidad");
        System.out.println("    2. Pasaporte");
        do {
            System.out.print("Ingrese el tipo de documento: ");
            opcion = sc.nextInt();
            sc.nextLine();
            switch (opcion) {
                case 0:
                    tipoDoc = "";
                    break;
                case 1:
                    tipoDoc = "DNI";
                    break;
                case 2:
                    tipoDoc = "PAS";
                    break;
                default:
                    tipoDoc = "";
                    break;
            }
            flag = tipoDoc != "";
            if (flag) {
                System.out.println("Ingrese un numero menor o igual a 0 para cancelar esta operación.");
                do {
                    System.out.print("Ingrese el número de documento: ");
                    nroDoc = sc.nextInt();
                    sc.nextLine();
                    if (nroDoc > 0) {
                        cli = (Cliente) clientes.get(tipoDoc+""+nroDoc); // Obtiene objeto cliente
                        if (cli != null) {
                            String prevData = cli.toString();
                            String laterData = prevData;
                            opcion = 0;                            
                            do {
                                clearTerminal();
                                System.out.println("-------- Modificando: "+cli.getTipoDoc()+""+cli.getNumeroDoc()+" --------");
                                System.out.println("    1. Nombre/s: "+cli.getNombre());
                                System.out.println("    2. Apellido/s: "+cli.getApellido());
                                System.out.println("    3. Telefono: "+cli.getTelefono());
                                System.out.println("    4. Correo: "+cli.getEmail());
                                System.out.println("--------------------------------------");
                                System.out.println("    0. Listo");
                                System.out.println("--------------------------------------");
                                System.out.print("¿Que dato desea modificar?: ");
                                opcion = sc.nextInt();
                                sc.nextLine();
                                switch (opcion) {
                                    case 0:
                                        //No realiza accion y vuelve al menu anterior
                                        break;
                                    case 1:
                                        System.out.print("Ingrese nuevo/s nombre/s (Actual: "+cli.getNombre()+"): ");
                                        cli.setNombre(sc.nextLine().trim());
                                        System.out.println("Nombre modificado.");
                                        break;
                                    case 2:
                                        System.out.print("Ingrese nuevo/s apellido/s (Actual: "+cli.getApellido()+"): ");
                                        cli.setApellido(sc.nextLine().trim());
                                        System.out.println("Apellido modificado.");
                                        break;
                                    case 3:
                                        System.out.print("Ingrese nuevo telefono (Actual: "+cli.getTelefono()+"): ");
                                        cli.setTelefono(sc.nextLine().trim());
                                        System.out.println("Telefono modificado.");
                                        break;
                                    case 4:
                                        System.out.print("Ingrese nuevo correo (Actual: "+cli.getEmail()+"): ");
                                        cli.setEmail(sc.nextLine().trim());
                                        System.out.println("Correo modificado.");
                                        break;
                                    default:
                                        System.out.println("Valor ingresado no es válido.");
                                        nroDoc = -1;
                                        break;
                                }    
                            } while (opcion != 0);
                            laterData = cli.toString();
                            if (!prevData.equals(laterData)) {
                                io.escribir("CLIENTE MODIFICADO: ANTES: "+prevData+" AHORA: "+laterData);
                            }
                        } else {
                            if (nroDoc >= 0) {
                                System.out.println("El documento ingresado no se encuentra registrado.");
                            }
                        }
                    } else {
                        System.out.println("Operación cancelada.");
                    }
                } while (nroDoc > 0 && cli == null);
            } else {
                if (opcion != 0) {
                    System.out.println("Opcion no válida.");
                }
            }
        } while (!flag && opcion != 0);
    }

    public static void mostrarClientes() {
        System.out.println("<------------- Clientes ------------->");
        System.out.println(clientes.toString());
        System.out.println();
    }

    public static void ABMSolicitudes() {
        int opcion = 0;
        do {
            System.out.println("---------------- Solicitudes ----------------");
            System.out.println("    1. Cargar Solicitud");
            System.out.println("    2. Eliminar Solicitud");
            System.out.println("    3. Modificar Solicitud");
            System.out.println("--------------------------------------");
            System.out.println("    4. Mostrar Solicitudes");
            System.out.println("--------------------------------------");
            System.out.println("    0. Atras");
            System.out.println("--------------------------------------");
            System.out.print("Ingrese una opción: ");
            opcion = sc.nextInt();
            clearTerminal();
            switch (opcion) {
                case 0:
                    // No realiza nada y corta la iteracion
                    break;
                case 1:
                    cargarSolicitud();
                    break;
                case 2:
                    eliminarSolicitud();
                    break;
                case 3:
                    modificarSolicitud();
                    break;
                case 4:
                    mostrarSolicitudes();
                    break;
                default:
                    System.out.println("Valor ingresado no es válido.");
                    break;
            }
        } while (opcion != 0);
    }

    public static void cargarSolicitud() {        
        int orig = -1;
        int dest = -1;
        String fecha = "0000-00-00";
        String tipoDoc = "";
        int nroDoc = -1;
        double metrosCub = 0;
        int bultos = -1;
        String domicilioRetiro = "";
        String domicilioEntrega = "";
        boolean envioPago = false;
        Cliente cli = null;

        int opcion = 0;
        boolean flag = false; // Decide si sigue la operacion o si se repite la entrada de datos

        System.out.println("<----------- Cargando Solicitud --------->");
        System.out.println("Se le solicitará: Clave de Cliente cargado (Tipo y Numero de Doc), CP de Origen y Destino (Ciudades cargadas), Fecha, Volumen (m^3), Bultos, Domicilios de Retiro/Entrega, Envio pagado (T/F)");
        
        System.out.println("------------ Tipos de Documento ------------");
        System.out.println("    0. Salir");
        System.out.println("    1. Documento Nacional de Identidad");
        System.out.println("    2. Pasaporte");
        do {
            System.out.print("Ingrese el tipo de documento: ");
            opcion = sc.nextInt();
            sc.nextLine();
            switch (opcion) {
                case 0:
                    tipoDoc = "";
                    break;
                case 1:
                    tipoDoc = "DNI";
                    break;
                case 2:
                    tipoDoc = "PAS";
                    break;
                default:
                    tipoDoc = "";
                    break;
            }
            flag = tipoDoc != "";
            if (flag) {
                System.out.println("Ingrese un numero menor o igual a 0 para cancelar esta operación.");
                do {
                    System.out.print("Ingrese el número de documento: ");
                    nroDoc = sc.nextInt();
                    sc.nextLine();
                    if (nroDoc > 0) {
                        cli = (Cliente) clientes.get(tipoDoc+""+nroDoc); // Obtiene objeto cliente
                        if (cli != null) {

                            System.out.println("Ingrese un numero menor o igual a 0 para cancelar esta operación.");
                            do {
                                System.out.print("Ingrese codigo postal de origen: ");
                                orig = sc.nextInt();
                                sc.nextLine();
                                if (orig > 0) {
                                    flag = ciudades.existeClave(orig);
                                    if (flag) {

                                        System.out.println("Ingrese un numero menor o igual a 0 para cancelar esta operación.");
                                        do {
                                            System.out.print("Ingrese codigo postal de destino: ");
                                            dest = sc.nextInt();
                                            sc.nextLine();
                                            if (dest > 0) {
                                                flag = ciudades.existeClave(dest);
                                                if (flag) {
                                                    do {
                                                        System.out.print("Ingrese fecha de solicitud (AAAA/MM/DD): ");
                                                        fecha = sc.nextLine();
                                                        flag = Pattern.compile("\\b\\d{4}/(0[1-9]|1[0-2])/(0[1-9]|[12]\\d|3[01])\\b").matcher(fecha).matches(); //Verifica formato de fecha
                                                        if (flag) {
                                                            do {
                                                                System.out.print("Ingrese volumen de solicitud (m^3): ");
                                                                metrosCub = sc.nextDouble();
                                                                sc.nextLine();
                                                                flag = metrosCub > 0;
                                                                if (flag) {
                                                                    do {
                                                                        System.out.print("Ingrese cantidad de bultos: ");
                                                                        bultos = sc.nextInt();
                                                                        sc.nextLine();
                                                                        flag = bultos > 0;
                                                                        if (flag) {
                                                                            System.out.print("Ingrese domicilio de retiro: ");
                                                                            domicilioRetiro = sc.nextLine().trim();
                                                                            System.out.print("Ingrese domicilio de entrega: ");
                                                                            domicilioEntrega = sc.nextLine().trim();

                                                                            System.out.print("¿El envio está pago? (S/N): ");
                                                                            envioPago = sc.nextLine().toUpperCase().charAt(0) == 'S';

                                                                            Solicitud soli = new Solicitud(orig, dest, fecha, tipoDoc, nroDoc+"", metrosCub, bultos, domicilioRetiro, domicilioEntrega, envioPago);
                                                                            if (solicitudes.asociar(orig+""+dest, soli)) {
                                                                                io.escribir("SOLICITUD REGISTRADA. ORIGEN: "+orig+" DESTINO: "+dest+" FECHA: "+fecha+" CLIENTE: "+tipoDoc+""+nroDoc+" BULTOS: "+bultos+" VOLUMEN: "+metrosCub+" m^3");
                                                                            }
                                                                            
                                                                        } else {
                                                                            System.out.println("Bultos debe ser mayor a 0.");
                                                                        }
                                                                    } while(!flag);        
                                                                    
                                                                } else {
                                                                    System.out.println("Metros cubicos debe ser mayor a 0.");
                                                                }
                                                            } while(!flag);
                                                        } else {
                                                            System.out.println("Fecha inválida.");
                                                        }
                                                    } while(!flag);

                                                } else {
                                                    if (dest > 0) {
                                                        System.out.println("El codigo postal ingresado no se encuentra registrado.");
                                                    }
                                                }
                                            } else {
                                                System.out.println("Operación cancelada.");
                                            }
                                        } while (dest > 0 && !flag);
                                        
                                        

                                    } else {
                                        if (dest > 0) {
                                            System.out.println("El codigo postal ingresado no se encuentra registrado.");
                                        }
                                    }
                                } else {
                                    System.out.println("Operación cancelada.");
                                }
                            } while (orig > 0 && !flag);                            

                        } else {
                            if (nroDoc > 0) {
                                System.out.println("El documento ingresado no se encuentra registrado.");
                            }
                        }
                    } else {
                        System.out.println("Operación cancelada.");
                    }
                } while (nroDoc > 0 && cli == null);
            } else {
                if (opcion != 0) {
                    System.out.println("Opcion no válida.");
                }
            }
        } while (!flag && opcion != 0);
    }

    public static void eliminarSolicitud() {
        int codOrigen = -1;
        int codDestino = -1;
        int opcion = -1;
        boolean flag = false; // Decide si sigue la operacion o si se repite la entrada de datos
        Solicitud soli = null;
        do {
            clearTerminal();
            System.out.println("<----------- Eliminando Solicitud --------->");
            System.out.println("Se le solicitará: Codigo origen y destino, Solicitud a eliminar (posición)");
            
            System.out.println("Ingrese un número menor o igual a 0 para cancelar esta operación.");
            System.out.print("Ingrese codigo postal de ciudad origen: ");
            codOrigen = sc.nextInt();
            sc.nextLine();
            
            if (codOrigen > 0) {
                flag = ciudades.existeClave(codOrigen);
                if (flag) {
                    do {
                        System.out.println("Ingrese un número menor o igual a 0 para cancelar esta operación.");
                        System.out.print("Ingrese codigo postal de ciudad destino: ");
                        codDestino = sc.nextInt();
                        sc.nextLine();
                        if (codDestino > 0) {
                            flag = ciudades.existeClave(codDestino);
                            if (flag) {
                                Lista listaSolicitudes = solicitudes.obtenerValores(codOrigen+""+codDestino);
                                if (listaSolicitudes.longitud() > 0) {
                                    System.out.println("Lista de solicitudes con origen "+codOrigen+" y destino "+codDestino+": ");
                                    listar(listaSolicitudes);
                                    do {
                                        System.out.print("Ingrese posicion de solicitud a eliminar (menor o igual a 0 para cancelar): ");
                                        opcion = sc.nextInt();
                                        sc.nextLine();
                                        
                                        if (opcion > 0) {
                                            flag =  opcion <= listaSolicitudes.longitud();
                                            if (flag) {
                                                soli = (Solicitud) listaSolicitudes.recuperar(opcion);

                                                System.out.println("Se eliminara la siguiente solicitud: "+soli.toString());
                                                System.out.print("¿Esta seguro? (S/N): ");
                                                switch (sc.next().toUpperCase().charAt(0)) {
                                                    case 'S':
                                                        solicitudes.desasociar(codOrigen+""+codDestino, soli);
                                                        io.escribir("SOLICITUD ELIMINADA: "+soli.toString());
                                                        System.out.println("Solicitud eliminada con éxito.");
                                                        break;
                                                    default:
                                                        System.out.println("Operación cancelada.");
                                                        break;
                                                }
                                            } else {
                                                System.out.println("Posición incorrecta.");
                                            }
                                        } else {
                                            System.out.println("Operación cancelada");
                                        }    
                                    } while (!flag && opcion > 0);
                                } else {
                                    System.out.println("No existen solicitudes entre las ciudades dadas.");
                                }
                            } else {
                                System.out.println("No existe ciudad con codigo ingresado.");
                            }
                        } else {
                            System.out.println("Operación cancelada.");
                        }
                    } while (!flag && codDestino > 0);
                } else {
                    System.out.println("No existe ciudad con codigo ingresado.");
                }
            } else {
                System.out.println("Operación cancelada.");
            }
        } while (!flag && codOrigen > 0);
    }

    public static void listar(Lista list) {
        // Muestra una lista con sus indices
        int cant = list.longitud();
        for (int i = 1; i <= cant; i++) {
            System.out.println("    ["+i+"] "+(list.recuperar(i).toString()));
        }
    }

    public static void modificarSolicitud() {
        int codOrigen = -1;
        int codDestino = -1;
        int opcion = -1;
        boolean flag = false; // Decide si sigue la operacion o si se repite la entrada de datos
        Solicitud soli = null;
        do {
            clearTerminal();
            System.out.println("<----------- Modificando Solicitud --------->");
            System.out.println("Se le solicitará: Codigo origen y destino, Solicitud a modificar (posición)");
            
            System.out.println("Ingrese un número menor o igual a 0 para cancelar esta operación.");
            System.out.print("Ingrese codigo postal de ciudad origen: ");
            codOrigen = sc.nextInt();
            sc.nextLine();
            
            if (codOrigen > 0) {
                flag = ciudades.existeClave(codOrigen);
                if (flag) {
                    do {
                        System.out.println("Ingrese un número menor o igual a 0 para cancelar esta operación.");
                        System.out.print("Ingrese codigo postal de ciudad destino: ");
                        codDestino = sc.nextInt();
                        sc.nextLine();
                        if (codDestino > 0) {
                            flag = ciudades.existeClave(codDestino);
                            if (flag) {
                                Lista listaSolicitudes = solicitudes.obtenerValores(codOrigen+""+codDestino);
                                if (listaSolicitudes.longitud() > 0) {
                                    System.out.println("Lista de solicitudes con origen "+codOrigen+" y destino "+codDestino+": ");
                                    listar(listaSolicitudes);
                                    do {
                                        System.out.print("Ingrese posicion de solicitud a modificar (menor o igual a 0 para cancelar): ");
                                        opcion = sc.nextInt();
                                        sc.nextLine();
                                        
                                        if (opcion > 0) {
                                            flag =  opcion <= listaSolicitudes.longitud();
                                            if (flag) {
                                                soli = (Solicitud) listaSolicitudes.recuperar(opcion);
                                                String prevData = soli.toString();
                                                String laterData = prevData;
                                                opcion = 0;                                            
                                                do {
                                                    clearTerminal();
                                                    System.out.println("-------- Modificando: "+soli.toString()+" --------");
                                                    System.out.println("    1. Fecha: "+soli.getFecha());
                                                    System.out.println("    2. Metros Cubicos: "+soli.getMetrosCubicos());
                                                    System.out.println("    3. Bultos: "+soli.getBultos());
                                                    System.out.println("    4. Domicilio Retiro: "+soli.getDomicilioRetiro());
                                                    System.out.println("    5. Domicilio Entrega: "+soli.getDomicilioEntrega());
                                                    System.out.println("    6. Envio abonado: "+(soli.getEnvioPago()? "Si" : "No"));
                                                    System.out.println("--------------------------------------");
                                                    System.out.println("    0. Listo");
                                                    System.out.println("--------------------------------------");
                                                    System.out.print("¿Que dato desea modificar?: ");
                                                    opcion = sc.nextInt();
                                                    sc.nextLine();
                                                    switch (opcion) {
                                                        case 0:
                                                            //No realiza accion y vuelve al menu anterior
                                                            break;
                                                        case 1:
                                                            String nuevaFe = "";                                                        
                                                            do {
                                                                System.out.print("Ingrese nueva fecha de solicitud (AAAA/MM/DD) (Actual: "+soli.getFecha()+"): ");
                                                                nuevaFe = sc.nextLine().trim();
                                                                flag = Pattern.compile("\\b\\d{4}/(0[1-9]|1[0-2])/(0[1-9]|[12]\\d|3[01])\\b").matcher(nuevaFe).matches();
                                                                if (flag) {
                                                                    soli.setFecha(nuevaFe);
                                                                    System.out.println("Fecha modificada.");
                                                                } else {
                                                                    System.out.println("Formato de fecha inválido.");
                                                                }    
                                                            } while (!flag);
                                                            break;
                                                        case 2:
                                                            double mts = 0;
                                                            System.out.print("Ingrese nuevos metros cubicos de solicitud (Actual: "+soli.getMetrosCubicos()+"): ");
                                                            mts = sc.nextDouble();
                                                            sc.nextLine();
                                                            if (mts > 0) {
                                                                soli.setMetrosCubicos(mts);
                                                                System.out.println("Metros Cubicos modificados.");
                                                            } else {
                                                                System.out.println("El valor debe ser positivo");
                                                            }
                                                            break;
                                                        case 3:
                                                            int bult = 0;
                                                            System.out.print("Ingrese nueva cantidad de bultos (Actual: "+soli.getBultos()+"): ");
                                                            bult = sc.nextInt();
                                                            sc.nextLine();
                                                            if (bult > 0) {
                                                                soli.setBultos(bult);
                                                                System.out.println("Cantidad de bultos modificada.");
                                                            } else {
                                                                System.out.println("El valor debe ser positivo");
                                                            }
                                                            break;
                                                        case 4:
                                                            System.out.print("Ingrese nuevo domicilio de retiro (Actual: "+soli.getDomicilioRetiro()+"): ");
                                                            soli.setDomicilioRetiro(sc.nextLine().trim());
                                                            System.out.println("Domicilio de retiro modificado.");
                                                            break;
                                                        case 5:
                                                            System.out.print("Ingrese nuevo domicilio de entrega (Actual: "+soli.getDomicilioEntrega()+"): ");
                                                            soli.setDomicilioEntrega(sc.nextLine().trim());
                                                            System.out.println("Domicilio de entrega modificado.");
                                                            break;
                                                        case 6:
                                                            System.out.print("¿El envio está pago? (S/N) (Actual: "+(soli.getEnvioPago()?"Si": "No")+"): ");
                                                            soli.setEnvioPago(sc.next().toUpperCase().charAt(0) == 'S');
                                                            System.out.println("Estado del pago de envio modificado.");
                                                            break;
                                                        default:
                                                            System.out.println("Valor ingresado no es válido.");
                                                            break;
                                                    }    
                                                } while (opcion != 0);
                                                laterData = soli.toString();
                                                if (!prevData.equals(laterData)) {
                                                    io.escribir("SOLICITUD MODIFICADA: ANTES: ["+prevData+"] AHORA: ["+laterData+"]");
                                                }
                                            } else {
                                                System.out.println("Posición incorrecta.");
                                            }
                                        } else {
                                            System.out.println("Operación cancelada");
                                        }    
                                    } while (!flag && opcion > 0);
                                } else {
                                    System.out.println("No existen solicitudes entre las ciudades dadas.");
                                }
                            } else {
                                System.out.println("No existe ciudad con codigo ingresado.");
                            }
                        } else {
                            System.out.println("Operación cancelada.");
                        }
                    } while (!flag && codDestino > 0);
                } else {
                    System.out.println("No existe ciudad con codigo ingresado.");
                }
            } else {
                System.out.println("Operación cancelada.");
            }
        } while (!flag && codOrigen > 0);
    }

    public static void mostrarSolicitudes() {
        System.out.println("<----------- Solicitudes ------------>");
        System.out.println(solicitudes.toString());
        System.out.println();
    }

    public static void mostrarSistema() {
        mostrarCiudades();
        mostrarRutas();
        mostrarClientes();
        mostrarSolicitudes();
        io.escribir("Se ha mostrado el sistema.");
    }

    public static void consultarCliente() {
        int opcion = -1;
        int nroDoc = -1;
        String tipoDoc = "";
        boolean flag = false;
        Cliente cli = null;
        do {            
            System.out.println("---------------- Consultar Cliente ----------------");
            System.out.println("Se le solicitará tipo y numero de documento");

            System.out.println("------------ Tipos de Documento ------------");
            System.out.println("    0. Salir");
            System.out.println("    1. Documento Nacional de Identidad");
            System.out.println("    2. Pasaporte");
            System.out.print("Ingrese el tipo de documento: ");
            opcion = sc.nextInt();
            sc.nextLine();
            switch (opcion) {
                case 0:
                    tipoDoc = "";
                    break;
                case 1:
                    tipoDoc = "DNI";
                    break;
                case 2:
                    tipoDoc = "PAS";
                    break;
                default:
                    tipoDoc = "";
                    break;
            }
            flag = tipoDoc != "";
            if (flag) {
                System.out.println("Ingrese un número menor o igual a 0 para cancelar esta operación.");
                do {
                    System.out.print("Ingrese el número de documento: ");
                    nroDoc = sc.nextInt();
                    sc.nextLine();
                    if (nroDoc > 0) {
                        cli = clientes.get(tipoDoc+""+nroDoc);
                        if (cli != null) {
                            io.escribir("SE HA CONSULTADO CLIENTE: "+cli.toString());
                            System.out.println("---------------- Cliente ----------------");
                            System.out.println(cli.toString());
                            System.out.print("Pulse Enter para salir: ");
                            sc.nextLine();
                        } else {
                            System.out.println("No existe un cliente con tipo y numero de documento ingresados.");
                        }
                    } else {
                            System.out.println("Operación cancelada.");
                        }
                    } while (nroDoc > 0 && cli == null);
            } else {
                if (opcion != 0) {
                    System.out.println("Opcion inválida.");
                }
            }
        } while (!flag && opcion != 0);
        clearTerminal();
    }

    public static void consultarCiudad() {
        int opcion = 0;
        do {
            System.out.println("---------------- Consultar Ciudad ----------------");
            System.out.println("    1. Con codigo postal");
            System.out.println("    2. Con prefijo");            
            System.out.println("--------------------------------------");
            System.out.println("    0. Atras");
            System.out.println("--------------------------------------");
            
            System.out.print("Ingrese una opción: ");
            opcion = sc.nextInt();
            switch (opcion) {
                case 0:
                    // No realiza nada y corta la iteracion
                    break;
                case 1:
                    consultarCiudadConCodigo();
                    break;
                case 2:
                    consultarCiudadConPrefijo();
                    break;
                default:
                    System.out.println("Valor ingresado no es válido.");
                    break;
            }
            clearTerminal();
        } while (opcion != 0);
    }

    public static void consultarCiudadConCodigo() {
        int codPost = -1;
        boolean flag = false;
        Ciudad ciu = null;
        do {            
            System.out.print("Ingrese el codigo postal (Ingrese numero menor o igual a 0 para cancelar): ");
            codPost = sc.nextInt();
            sc.nextLine();
            flag = codPost > 0;
            if (flag) {                                
                ciu = (Ciudad) ciudades.obtenerDato(codPost);
                if (ciu != null) {
                    io.escribir("SE HA CONSULTADO CIUDAD CON CODIGO POSTAL: "+ciu.toString());
                    System.out.println("---------------- Ciudad ----------------");
                    System.out.println(ciu.toString());
                    System.out.print("Pulse Enter para salir: ");
                    sc.nextLine();
                } else {
                    System.out.println("No existe una ciudad con codigo postal ingresado.");
                }
            } else {
                System.out.println("Operación cancelada.");
            }
        } while (!flag && codPost > 0);
    }

    public static void consultarCiudadConPrefijo() {
        int prefijo = -1;
        int base = 0;
        int min = 0;
        int max = 0;
        boolean flag = false;
        Lista listaCiudades = new Lista();
        do {
            System.out.print("Ingrese el prefijo de codigo postal (Ingrese numero menor o igual a 0 para cancelar): ");
            prefijo = sc.nextInt();
            sc.nextLine();
            flag = prefijo > 0;
            if (flag) {
                base = prefijo;                
                switch((prefijo+"").length()) { //Segun cantidad de cifras, se asigna el rango
                    case 1:
                        min = base*1000;
                        max = min+999;
                    break;
                    case 2:
                        min = base*100;
                        max = min+99;
                    break;
                    case 3:
                        min = base*10;
                        max = min+9;
                    break;
                    case 4:
                        min = base;
                        max = base;
                    break;
                    default:
                        flag = false;
                    break;
                }
                if (flag) {
                    listaCiudades = ciudades.listarRango(min, max);
                    if (!listaCiudades.esVacia()) {
                        io.escribir("SE HAN CONSULTADO CIUDADES CON PREFIJO "+prefijo+": "+listaCiudades.toString());
                        System.out.println("---------------- Ciudades ----------------");
                        listar(listaCiudades);
                    } else {
                        System.out.println("No existen ciudades con prefijo ingresado.");
                    }
                    System.out.print("Pulse Enter para salir: ");
                    sc.nextLine();
                } else {
                    System.out.println("El prefijo debe tener de 1 a 4 cifras.");
                }
            } else {
                System.out.println("Operación cancelada.");
            }
        } while (!flag && prefijo > 0);
    }

    public static void consultarViaje() {
        int opcion = 0;
        do {
            System.out.println("---------------- Consultar Viaje ----------------");
            System.out.println("    1. Camino de A a B que recorra menos ciudades.");
            System.out.println("    2. Camino de A a B en menos kilometros.");
            System.out.println("    3. Caminos de A a B que pasen por C (sin recorrer dos veces la misma)."); 
            System.out.println("    4. Verificar si es posible llegar de A a B en menos de X kilometros."); 
            System.out.println("--------------------------------------");
            System.out.println("    0. Atras");
            System.out.println("--------------------------------------");
            System.out.print("Ingrese una opción: ");
            opcion = sc.nextInt();
            sc.nextLine();

            switch(opcion) {
                case 1:
                    caminoMenosCiudades();
                    break;
                case 2:
                    caminoMenosKilometros();
                    break;
                case 3:
                    caminosPasandoCiudad();
                    break;
                case 4:
                    verificarCaminoMenosXKilometros();
                    break;
                default:
                    System.out.println("Valor ingresado no es válido.");
                    break;
            }
            clearTerminal();
        } while(opcion != 0);
    }

    public static void caminoMenosCiudades() {
        int codOrigen = -1;
        int codDestino = -1;
        Lista camino = null;
        boolean flag = false;
        do {
            System.out.print("Ingrese el codigo postal de la ciudad origen (Ingrese un numero menor o igual a 0 para salir): ");
            codOrigen = sc.nextInt();
            sc.nextLine();
            if (codOrigen > 0) {
                flag = ciudades.existeClave(codOrigen); 
                if (flag) {
                    do {
                        System.out.print("Ingrese el codigo postal de la ciudad destino (Ingrese un numero menor o igual a 0 para salir): ");
                        codDestino = sc.nextInt();
                        sc.nextLine();
                        if (codDestino > 0) {
                            flag = ciudades.existeClave(codDestino);
                            if (flag) {
                                camino = mapa.caminoMasCorto(codOrigen, codDestino);
                                if(!camino.esVacia()) {
                                    io.escribir("SE CONSULTÓ CAMINO QUE RECORRE MENOS CIUDADES DE "+codOrigen+" A "+codDestino+": "+camino.toString());
                                    System.out.println("---------------- Camino con menos ciudades ("+codOrigen+" <-> "+codDestino+") ----------------");
                                    listar(camino);
                                } else {
                                    System.out.println("No existe camino entre ciudades.");
                                }
                                System.out.print("Presione enter para salir: ");
                                sc.nextLine();
                            } else {
                                System.out.println("No existe ciudad con codigo ingresado.");
                            }
                        }
                    } while(!flag && codDestino > 0);
                } else {
                    System.out.println("No existe ciudad con codigo ingresado.");
                }
            }
        } while(!flag && codOrigen > 0);
    }

    public static void caminoMenosKilometros() {
        int codOrigen = -1;
        int codDestino = -1;
        Lista camino = null;
        boolean flag = false;
        do {
            System.out.print("Ingrese el codigo postal de la ciudad origen (Ingrese un numero menor o igual a 0 para salir): ");
            codOrigen = sc.nextInt();
            sc.nextLine();
            if (codOrigen > 0) {
                flag = ciudades.existeClave(codOrigen); 
                if (flag) {
                    do {
                        System.out.print("Ingrese el codigo postal de la ciudad destino (Ingrese un numero menor o igual a 0 para salir): ");
                        codDestino = sc.nextInt();
                        sc.nextLine();
                        if (codDestino > 0) {
                            flag = ciudades.existeClave(codDestino);
                            if (flag) {
                                camino = mapa.caminoMasCortoEtiqueta(codOrigen, codDestino); //ACA
                                if(!camino.esVacia()) {
                                    io.escribir("SE CONSULTÓ CAMINO QUE RECORRE MENOS KILOMETROS DE "+codOrigen+" A "+codDestino+": "+camino.toString());
                                    System.out.println("---------------- Camino con menos kilometros ("+codOrigen+" <-> "+codDestino+") ----------------");
                                    listar(camino);
                                } else {
                                    System.out.println("No existe camino entre ciudades.");
                                }
                                System.out.print("Presione enter para salir: ");
                                sc.nextLine();
                            } else {
                                System.out.println("No existe ciudad con codigo ingresado.");
                            }
                        }
                    } while(!flag && codDestino > 0);
                } else {
                    System.out.println("No existe ciudad con codigo ingresado.");
                }
            }
        } while(!flag && codOrigen > 0);  
    }

    public static void caminosPasandoCiudad() {
        int codOrigen = -1;
        int codDestino = -1;
        int codIntermedio = -1;
        Lista caminos = null;
        boolean flag = false;
        do {
            System.out.print("Ingrese el codigo postal de la ciudad origen (Ingrese un numero menor o igual a 0 para salir): ");
            codOrigen = sc.nextInt();
            sc.nextLine();
            if (codOrigen > 0) {
                flag = ciudades.existeClave(codOrigen); 
                if (flag) {
                    do {
                        System.out.print("Ingrese el codigo postal de la ciudad destino (Ingrese un numero menor o igual a 0 para salir): ");
                        codDestino = sc.nextInt();
                        sc.nextLine();
                        if (codDestino > 0) {
                            flag = ciudades.existeClave(codDestino);
                            if (flag) {
                                do {
                                    System.out.print("Ingrese el codigo postal de la ciudad intermedia (Ingrese un numero menor o igual a 0 para salir): ");
                                    codIntermedio = sc.nextInt();
                                    sc.nextLine();
                                    if (codIntermedio > 0) {
                                        flag = ciudades.existeClave(codIntermedio);
                                        if (flag) {
                                            caminos = mapa.caminosCruzandoIntermedio(codOrigen, codDestino, codIntermedio);
                                            if(!caminos.esVacia()) {
                                                String cadena = "{\n";
                                                for(int i = 1; i <= caminos.longitud();i++) {
                                                    cadena += caminos.recuperar(i)+"\n";
                                                }
                                                cadena += "}";
                                                io.escribir("SE CONSULTÓ CAMINOS QUE RECORREN "+codIntermedio+" DE "+codOrigen+" A "+codDestino+": "+cadena);
                                                System.out.println("---------------- Caminos que cruzan por un punto ("+codOrigen+" <-> "+codIntermedio+" <-> "+codDestino+") ----------------");
                                                listar(caminos);
                                            } else {
                                                System.out.println("No existen camino entre dos ciudades que pasen por otra especificada.");
                                            }
                                            System.out.print("Presione enter para salir: ");
                                            sc.nextLine();

                                        } else {
                                            System.out.println("No existe ciudad con codigo ingresado.");
                                        }
                                    }
                                } while(!flag && codIntermedio > 0);
                            } else {
                                System.out.println("No existe ciudad con codigo ingresado.");
                            }
                        }
                    } while(!flag && codDestino > 0);
                } else {
                    System.out.println("No existe ciudad con codigo ingresado.");
                }
            }
        } while(!flag && codOrigen > 0);
    }

    public static void verificarCaminoMenosXKilometros() {
        int codOrigen = -1;
        int codDestino = -1;
        double kmsMax = -1;
        boolean respuesta = false;
        boolean flag = false;
        do {
            System.out.print("Ingrese el codigo postal de la ciudad origen (Ingrese un numero menor o igual a 0 para salir): ");
            codOrigen = sc.nextInt();
            sc.nextLine();
            if (codOrigen > 0) {
                flag = ciudades.existeClave(codOrigen); 
                if (flag) {
                    do {
                        System.out.print("Ingrese el codigo postal de la ciudad destino (Ingrese un numero menor o igual a 0 para salir): ");
                        codDestino = sc.nextInt();
                        sc.nextLine();
                        if (codDestino > 0) {
                            flag = ciudades.existeClave(codDestino);
                            if (flag) {
                                do {
                                    System.out.print("Ingrese distancia maxima a recorrer en kms (Ingrese un numero menor o igual a 0 para salir): ");
                                    kmsMax = sc.nextDouble();
                                    sc.nextLine();
                                    if (kmsMax > 0) {
                                        respuesta = mapa.verificarCaminoMenosKm(codOrigen, codDestino, kmsMax);
                                        String cadena = (respuesta? "Si": "No")+" es posible llegar en menos de "+kmsMax+" kms desde "+codOrigen+" hasta "+codDestino+".";
                                        io.escribir("SE VERIFICÓ SI HAY UN CAMINO QUE RECORRE MENOS DE "+kmsMax+" Km DE "+codOrigen+" A "+codDestino+": "+cadena);
                                        System.out.println("---- "+cadena);
                                        System.out.print("Presione enter para salir: ");
                                        sc.nextLine();
                                    }
                                } while(!flag && kmsMax > 0);
                            } else {
                                System.out.println("No existe ciudad con codigo ingresado.");
                            }
                        }
                    } while(!flag && codDestino > 0);
                } else {
                    System.out.println("No existe ciudad con codigo ingresado.");
                }
            }
        } while(!flag && codOrigen > 0);   
    }

    public static void verificarViaje() {
        
    }

    public static void clearTerminal() {
        // Limpia terminal
        try {
            // Verificar el sistema operativo
            String os = System.getProperty("os.name");

            // Si es Windows, ejecutar el comando "cls"
            if (os.contains("Windows")) {
                new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
            } 
            // Si es Unix (Linux, Mac), ejecutar el comando "clear"
            else {
                new ProcessBuilder("clear").inheritIO().start().waitFor();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        clearTerminal();
        try {
            menu();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}