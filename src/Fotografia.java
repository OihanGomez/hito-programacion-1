import java.sql.Date;

public class Fotografia {
    private int idFoto;
    private String titulo;
    private Date fecha;
    private String archivo;
    private int visitas;
    private int idFotografo;

    public Fotografia(int idFoto, String titulo, Date fecha, String archivo, int visitas, int idFotografo) {
        this.idFoto = idFoto;
        this.titulo = titulo;
        this.fecha = fecha;
        this.archivo = archivo;
        this.visitas = visitas;
        this.idFotografo = idFotografo;
    }

    // Getters y Setters
    public int getIdFoto() {
        return idFoto;
    }

    public void setIdFoto(int idFoto) {
        this.idFoto = idFoto;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public String getArchivo() {
        return archivo;
    }

    public void setArchivo(String archivo) {
        this.archivo = archivo;
    }

    public int getVisitas() {
        return visitas;
    }

    public void setVisitas(int visitas) {
        this.visitas = visitas;
    }

    public int getIdFotografo() {
        return idFotografo;
    }

    public void setIdFotografo(int idFotografo) {
        this.idFotografo = idFotografo;
    }

    // Método toString para imprimir la información de la fotografía
    @Override
    public String toString() {
        return titulo; // Devuelve solo el título de la fotografía
    }

}
