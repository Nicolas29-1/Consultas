package relaciones.entity.tarea3.cu.realizarcompra.response;

public record ResponseResumenCompraProducto(
    int idProducto,
    String nombreProducto,
    long unidadesCompradas,
    double montoTotal
) {
}
