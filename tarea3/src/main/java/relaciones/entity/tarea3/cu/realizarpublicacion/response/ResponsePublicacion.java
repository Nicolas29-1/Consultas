package relaciones.entity.tarea3.cu.realizarpublicacion.response;

import java.time.LocalDateTime;
import java.util.List;

public record ResponsePublicacion(
    int idPublicacion,
    String titulo,
    String nombreVendedor,
    LocalDateTime fechaPublicacion,
    double total,
    List<ResponsePublicacionItem> items
) {

    public record ResponsePublicacionItem(
        String nombreProducto,
        int cantidad,
        Double precioUnitario
    ) {
    }
}
