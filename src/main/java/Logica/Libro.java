package Logica;

import java.util.Date;

public class Libro {

    // Atributos.
    private int id_libro;
    private String isbn;
    private String titulo;
    private String autor;
    private String genero;
    private Date fecha_inicio;
    private Date fecha_fin;
    private String estado_lectura;
    private Integer calificacion;
    private String sinopsis;
    private int numero_paginas;
    private String formato;
    private String idioma;
    private int fecha_publicacion;
    private String editorial;
    private String saga;
    private String portada;

    // Constructores.
    public Libro() {
    }

    public Libro(int id_libro, String isbn, String titulo, String autor, String genero, Date fecha_inicio, Date fecha_fin, String estado_lectura, Integer calificacion, String sinopsis, int numero_paginas, String formato, String idioma, int fecha_publicacion, String editorial, String saga, String portada) {
        this.id_libro = id_libro;
        this.isbn = isbn;
        this.titulo = titulo;
        this.autor = autor;
        this.genero = genero;
        this.fecha_inicio = fecha_inicio;
        this.fecha_fin = fecha_fin;
        this.estado_lectura = estado_lectura;
        this.calificacion = calificacion;
        this.sinopsis = sinopsis;
        this.numero_paginas = numero_paginas;
        this.formato = formato;
        this.idioma = idioma;
        this.fecha_publicacion = fecha_publicacion;
        this.editorial = editorial;
        this.saga = saga;
        this.portada = portada;
    }

    // Getters y Setters.
    public int getId_libro() {
        return id_libro;
    }

    public void setId_libro(int id_libro) {
        this.id_libro = id_libro;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getAutor() {
        return autor;
    }

    public void setAutor(String autor) {
        this.autor = autor;
    }

    public String getGenero() {
        return genero;
    }

    public void setGenero(String genero) {
        this.genero = genero;
    }

    public Date getFecha_inicio() {
        return fecha_inicio;
    }

    public void setFecha_inicio(Date fecha_inicio) {
        this.fecha_inicio = fecha_inicio;
    }

    public Date getFecha_fin() {
        return fecha_fin;
    }

    public void setFecha_fin(Date fecha_fin) {
        this.fecha_fin = fecha_fin;
    }

    public String getEstado_lectura() {
        return estado_lectura;
    }

    public void setEstado_lectura(String estado_lectura) {
        this.estado_lectura = estado_lectura;
    }

    public Integer getCalificacion() {
        return calificacion;
    }

    public void setCalificacion(Integer calificacion) {
        this.calificacion = calificacion;
    }

    public String getSinopsis() {
        return sinopsis;
    }

    public void setSinopsis(String sinopsis) {
        this.sinopsis = sinopsis;
    }

    public int getNumero_paginas() {
        return numero_paginas;
    }

    public void setNumero_paginas(int numero_paginas) {
        this.numero_paginas = numero_paginas;
    }

    public String getFormato() {
        return formato;
    }

    public void setFormato(String formato) {
        this.formato = formato;
    }

    public String getIdioma() {
        return idioma;
    }

    public void setIdioma(String idioma) {
        this.idioma = idioma;
    }

    public int getFecha_publicacion() {
        return fecha_publicacion;
    }

    public void setFecha_publicacion(int fecha_publicacion) {
        this.fecha_publicacion = fecha_publicacion;
    }

    public String getEditorial() {
        return editorial;
    }

    public void setEditorial(String editorial) {
        this.editorial = editorial;
    }

    public String getSaga() {
        return saga;
    }

    public void setSaga(String saga) {
        this.saga = saga;
    }

    public String getPortada() {
        return portada;
    }

    public void setPortada(String portada) {
        this.portada = portada;
    }

    @Override
    public String toString() {
        return "Libro{" + "id_libro=" + id_libro + ", isbn=" + isbn + ", titulo=" + titulo + ", autor=" + autor + ", genero=" + genero + ", fecha_inicio=" + fecha_inicio + ", fecha_fin=" + fecha_fin + ", estado_lectura=" + estado_lectura + ", calificacion=" + calificacion + ", sinopsis=" + sinopsis + ", numero_paginas=" + numero_paginas + ", formato=" + formato + ", idioma=" + idioma + ", fecha_publicacion=" + fecha_publicacion + ", editorial=" + editorial + ", saga=" + saga + ", portada=" + portada + '}';
    }

}
