package sistema;

public class Ciudad {
    private int codigoPostal; //Clave, 4 digitos
    private String nombre;
    private String provincia;

    public Ciudad(int cod, String nom, String prov) {
        this.codigoPostal = cod;
        this.nombre = nom;
        this.provincia = prov;
    }

    public int getCodigoPostal() {
        return this.codigoPostal;
    }
    
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

    public String toString() {
        return "CodPostal: " + this.codigoPostal + " | Ciudad: " + this.nombre + " | Provincia: " + this.provincia;
    }
}
