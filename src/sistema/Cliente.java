package sistema;

public class Cliente {
    private String tipoDoc;
    private int numeroDoc;
    private String nombre;
    private String apellido;
    private String telefono;
    private String email;

    public Cliente(String tDoc, int nDoc, String nom, String ape) {
        this.tipoDoc = tDoc;
        this.numeroDoc = nDoc;
        this.nombre = nom;
        this.apellido = ape;
        this.telefono = "Sin telefono";
        this.email = "Sin email";
    }

    public Cliente(String tDoc, int nDoc, String nom, String ape, String tel, String ema) {
        this.tipoDoc = tDoc;
        this.numeroDoc = nDoc;
        this.nombre = nom;
        this.apellido = ape;
        this.telefono = tel;
        this.email = ema;
    }

    public String getTipoDoc() {
        return this.tipoDoc;
    }
    
    public int getNumeroDoc() {
        return this.numeroDoc;
    }

    public String getNombre() {
        return this.nombre;
    }

    public void setNombre(String nom) {
        this.nombre = nom;
    }

    public String getApellido() {
        return this.apellido;
    }

    public void setApellido(String ape) {
        this.apellido = ape;
    }

    public String getTelefono() {
        return this.telefono;
    }

    public void setTelefono(String tel) {
        this.telefono = tel;
    }

    public String getEmail() {
        return this.email;
    }

    public void setEmail(String ema) {
        this.email = ema;
    }
}
