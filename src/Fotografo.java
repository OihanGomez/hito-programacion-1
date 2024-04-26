public class Fotografo {
    // Atributos
    private int idFotografo;
    private String nombre;
    private boolean premiado;

    // Constructor
    public Fotografo(int idFotografo, String nombre, boolean premiado) {
        this.idFotografo = idFotografo;
        this.nombre = nombre;
        this.premiado = premiado;
    }

    // Getters y setters
    public int getIdFotografo() {
        return idFotografo;
    }

    public void setIdFotografo(int idFotografo) {
        this.idFotografo = idFotografo;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public boolean isPremiado() {
        return premiado;
    }

    public void setPremiado(boolean premiado) {
        this.premiado = premiado;
    }

    // Método toString para representar el objeto como String
    @Override
    public String toString() {
        return "Fotografo{" +
                "idFotografo=" + idFotografo +
                ", nombre='" + nombre + '\'' +
                ", premiado=" + premiado +
                '}';
    }
}


