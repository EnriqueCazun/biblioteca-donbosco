package com.biblioteca.core;

public class MaterialReferencia extends RecursoBiblioteca {

    private String tipoMaterial;
    private boolean soloConsulta;

    public MaterialReferencia(int id, String titulo, String ubicacionFisica, int cantidadTotal, int cantidadPrestada, String tipoMaterial, boolean soloConsulta) {
        super(id, titulo, ubicacionFisica, cantidadTotal, cantidadPrestada);
        this.tipoMaterial = tipoMaterial;
        this.soloConsulta = soloConsulta;
    }

    public String getTipoMaterial() {
        return tipoMaterial;
    }

    public boolean isSoloConsulta() {
        return soloConsulta;
    }

    @Override
    public void mostrarDetalles() {
        System.out.println("Material de Referencia: " + titulo);
        System.out.println("Tipo de Material: " + tipoMaterial);
        System.out.println("Solo para consulta: " + (soloConsulta ? "Sí" : "No"));
        System.out.println("Ubicación: " + ubicacionFisica);
        System.out.println("Cantidad Total: " + cantidadTotal);
        System.out.println("Cantidad Prestada: " + cantidadPrestada);
        System.out.println("Cantidad Disponible: " + getCantidadDisponible());
    }

    @Override
    public boolean isDisponible() {
        return soloConsulta || getCantidadDisponible() > 0;
    }
}