package relaciones.entity.tarea3.cu.realizarpublicacion.request;

import java.util.List;

public record RequestPublicacion(
    String titulo,
    int idVendedor,
    List<RequestPublicacionItem> items
) {

    public record RequestPublicacionItem(
        int idProducto,
        int cantidad,
        Double precioUnitario
    ) {
    }
}
