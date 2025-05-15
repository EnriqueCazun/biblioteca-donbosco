package com.biblioteca.core;

import com.biblioteca.dao.LibroDAO;
import com.biblioteca.dao.PrestamoDAO;
import com.biblioteca.dao.UsuarioDAO;
import com.biblioteca.dao.MaterialAudiovisualDAO;
import com.biblioteca.dao.RevistaDAO;

import java.util.List;

public class Biblioteca {
    
    private LibroDAO libroDAO = new LibroDAO();
    private PrestamoDAO prestamoDAO = new PrestamoDAO();
    private UsuarioDAO usuarioDAO = new UsuarioDAO();
    private MaterialAudiovisualDAO audiovisualDAO = new MaterialAudiovisualDAO();
    private RevistaDAO revistaDAO = new RevistaDAO();

    public boolean agregarLibro(Libro libro) {
        return libroDAO.agregarLibro(libro);
    }

    public boolean eliminarLibro(int idLibro) {
        return libroDAO.eliminarLibro(idLibro);
    }

    public boolean agregarMaterialAudiovisual(MaterialAudiovisual material) {
        return audiovisualDAO.agregar(material);
    }

    public boolean eliminarMaterialAudiovisual(int id) {
        return audiovisualDAO.eliminar(id);
    }

    public boolean agregarRevista(Revista revista) {
        return revistaDAO.agregar(revista);
    }

    public boolean eliminarRevista(int id) {
        return revistaDAO.eliminar(id);
    }

    public boolean prestarLibro(String carnetUsuario, int idLibro) {
        Libro libro = libroDAO.getLibroPorId(idLibro);
        if (libro != null && libro.isDisponible()) {
            return prestamoDAO.crearPrestamo(carnetUsuario, idLibro);
        }
        return false;
    }

    public boolean devolverLibro(int idPrestamo) {
        return prestamoDAO.registrarDevolucion(idPrestamo);
    }

    public List<Libro> obtenerLibrosDisponibles() {
        return libroDAO.getDisponibles();
    }

    public List<MaterialAudiovisual> obtenerMaterialesAudiovisualesDisponibles() {
        return audiovisualDAO.getAll();
    }

    public List<Revista> obtenerRevistasDisponibles() {
        return revistaDAO.getAll();
    }

    public List<Prestamo> obtenerHistorialPrestamosPorUsuario(String carnetUsuario) {
        return prestamoDAO.getHistorialPorUsuario(carnetUsuario);
    }

    public List<Prestamo> obtenerHistorialGlobalPrestamos() {
        return prestamoDAO.getTodosLosPrestamos();
    }

    public List<Usuario> obtenerUsuarios() {
        return usuarioDAO.getAllUsuarios();
    }
    
    public boolean verificarDisponibilidadLibro(int idLibro) {
        Libro libro = libroDAO.getLibroPorId(idLibro);
        return libro != null && libro.isDisponible();
    }
}