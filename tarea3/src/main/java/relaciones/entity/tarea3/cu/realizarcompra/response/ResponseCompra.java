package relaciones.entity.tarea3.cu.realizarcompra.response;

import java.time.LocalDateTime;
import java.util.List;

public record ResponseCompra(
    int idCompra,
    String nombreComprador,
    LocalDateTime fechaCompra,
    double total,
    List<ResponseCompraItem> items
) {

    public record ResponseCompraItem(
        String nombreProducto,
        int cantidad,
        Double precioUnitario
    ) {
    }
}
