package relaciones.entity.tarea3.cu.realizarpublicacion.response;

public record ResponseResumenPublicacion(
    int idPublicacion,
    String nombreVendedor,
    long cantidadItems,
    long unidadesTotales
) {
}
