package sistema;

public class Cliente {
    private String tipoDoc; //Clave
    private String numeroDoc; //Clave
    private String nombre;
    private String apellido;
    private String telefono;
    private String email;

    public Cliente(String tDoc, String nDoc, String nom, String ape) {
        this.tipoDoc = tDoc;
        this.numeroDoc = nDoc;
        this.nombre = nom;
        this.apellido = ape;
        this.telefono = "Sin telefono";
        this.email = "Sin email";
    }

    public Cliente(String tDoc, String nDoc, String nom, String ape, String tel) {
        this.tipoDoc = tDoc;
        this.numeroDoc = nDoc;
        this.nombre = nom;
        this.apellido = ape;
        this.telefono = tel;
        this.email = "Sin email";
    }

    public Cliente(String tDoc, String nDoc, String nom, String ape, String tel, String ema) {
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
    
    public String getNumeroDoc() {
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

    public String toString(){
        return "Tipo Doc: "+this.tipoDoc+
        " | Num Doc: "+this.numeroDoc+
        " | Nombre/s: "+this.nombre+
        " | Apellido/s: "+this.apellido+
        " | Telefono: "+this.telefono+
        " | E-mail: "+this.email;
    }
}
