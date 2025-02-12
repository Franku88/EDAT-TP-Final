package sistema;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Scanner;

import estructuras.diccionario.Diccionario;
import estructuras.grafos.GrafoEtiquetado;
import estructuras.mapeo.MapeoAMuchos;

public class MudanzasCompartidas {
    
    // ARRANCO CON ABMCiudades
    private static final Scanner sc = new Scanner(System.in);
    private static final DataIO io = new DataIO(); // Objeto para la entrada y salida de datos
    private static Diccionario ciudades = new Diccionario(); // Informacion sobre ciudades, cada ciudad almacena su lista de solicitudes
    private static GrafoEtiquetado mapa = new GrafoEtiquetado(); // Mapa de rutas, nodos codCiudad y etiquetas con km entre ciudades
    private static MapeoAMuchos solicitudes = new MapeoAMuchos(); // MapeoAMuchos (Dominio: codOrigen+codDestino, Rangos: Solicitudes)
    private static HashMap<String, Cliente> clientes = new HashMap<String, Cliente>(); // HashMap de clientes (clave con su objeto Cliente)

    public static void main(String[] args) {
        clearTerminal();
        try {
            menu();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

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
            System.out.println("    5. ABM Pedidos");
            System.out.println("*------------------------------------*");
            System.out.println("    6. Consultar Cliente");
            System.out.println("    7. Consultar Ciudades");
            System.out.println("    8. Consultar Viajes");
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
                    ABMPedidos();
                    System.out.println();
                    break;
                case 6:
                    System.out.println();
                    System.out.println();
                    break;
                case 7:
                    System.out.println();
                    System.out.println();
                    break;
                case 8:
                    System.out.println();
                    System.out.println();
                    break;
                case 9:
                    System.out.println();
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

    }

    public static void cargaInicial() {
        // Opcion 1 del menu
        io.cargarDatos(ciudades, mapa, clientes, solicitudes);
    }

    public static void ABMCiudades() {
        int opcion = 0;
        io.crearLog();

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
                    //modificarCiudad();
                    break;
                case 4:
                    //mostrarCiudades();
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
                }
            } else {
                System.out.println("El codigo ingresado debe ser positivo.");
            }
        } while (existe);
        
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
                                io.escribir("CIUDAD ELIMINADA: "+ciu.toString());
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

    public static void ABMRutas() {
        System.out.println("RUTAS");
    }

    public static void ABMClientes() {
        System.out.println("CLIENTES");
    }

    public static void ABMPedidos() {
        System.out.println("PEDIDOS");
    }

    public static void mostrarSistema() {
        System.out.println("<------------- Ciudades ------------->");
        System.out.println(ciudades.toString());
        System.out.println();
        System.out.println("<--------------- Mapa --------------->");
        System.out.println(mapa.toString());
        System.out.println();
        System.out.println("<------------- Clientes ------------->");
        System.out.println(clientes.toString());
        System.out.println();
        System.out.println("<----------- Solicitudes ------------>");
        System.out.println(solicitudes.toString());
        System.out.println();
        io.escribir("Se ha mostrado el sistema.");
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

}