package relaciones.entity.tarea3.cu.realizarcompra.request;

import java.util.List;

public record RequestCompra(
    int idComprador,
    List<RequestCompraItem> items
) {

    public record RequestCompraItem(
        int idProducto,
        int cantidad,
        Double precioUnitario
    ) {
    }
}
