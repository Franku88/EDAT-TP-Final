package sistema;
import estructuras.lineales.Lista;

public class Ciudad {
    private int codigoPostal; //4 digitos
    private String nombre;
    private String provincia;
    private Lista solicitudes;

    public Ciudad(int cod, String nom, String prov) {
        this.codigoPostal = cod;
        this.nombre = nom;
        this.provincia = prov;
        this.solicitudes = new Lista();
    }

    public int getCodigoPostal() {
        return this.codigoPostal;
    }

    /*public void setCodigoPostal(int cod) {
        this.codigoPostal = cod;
    } */
    
    public String getNombre() {
        return this.nombre;
    }

    public void setNombre(String nom) {
        this.nombre = nom;
    }

    public String getProvincia() {
        return this.provincia;
    }

    public void setProvincia(String prov) {
        this.provincia = prov;
    }

    public Lista getSolicitudes() {
        return this.solicitudes;
    }
}
