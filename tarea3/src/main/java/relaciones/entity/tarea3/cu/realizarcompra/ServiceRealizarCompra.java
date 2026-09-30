package relaciones.entity.tarea3.cu.realizarcompra;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import relaciones.entity.tarea3.cu.realizarcompra.request.RequestCompra;
import relaciones.entity.tarea3.cu.realizarcompra.request.RequestCompra.RequestCompraItem;
import relaciones.entity.tarea3.cu.realizarcompra.response.ResponseCompra;
import relaciones.entity.tarea3.cu.realizarcompra.response.ResponseResumenCompraProducto;
import relaciones.entity.tarea3.dominio.entity.Compra;
import relaciones.entity.tarea3.dominio.entity.Producto;
import relaciones.entity.tarea3.dominio.entity.Usuario;
import relaciones.entity.tarea3.dominio.repository.RepoCompra;
import relaciones.entity.tarea3.dominio.repository.RepoProducto;
import relaciones.entity.tarea3.dominio.repository.RepoUsuario;

import java.util.List;

@Service
public class ServiceRealizarCompra {

    private final RepoUsuario repoUsuario;
    private final RepoProducto repoProducto;
    private final RepoCompra repoCompra;

    public ServiceRealizarCompra(RepoUsuario repoUsuario,
                                 RepoProducto repoProducto,
                                 RepoCompra repoCompra) {
        this.repoUsuario = repoUsuario;
        this.repoProducto = repoProducto;
        this.repoCompra = repoCompra;
    }

    @Transactional
    public ResponseCompra realizarCompra(RequestCompra request) {
        if (request.items() == null || request.items().isEmpty()) {
            throw new RuntimeException("La compra debe tener al menos un item");
        }

        Usuario comprador = repoUsuario.findById(request.idComprador())
                .orElseThrow(() -> new RuntimeException(
                        "Comprador no encontrado con id: " + request.idComprador()));

        Compra compra = new Compra();
        compra.setComprador(comprador);

        double total = 0.0;

        for (RequestCompraItem item : request.items()) {
            if (item.cantidad() <= 0) {
                throw new RuntimeException("La cantidad debe ser mayor a 0");
            }
            if (item.precioUnitario() == null || item.precioUnitario() < 0) {
                throw new RuntimeException("El precio unitario no es valido");
            }

            Producto producto = repoProducto.findById(item.idProducto())
                    .orElseThrow(() -> new RuntimeException(
                            "Producto no encontrado con id: " + item.idProducto()));

            compra.agregarItem(producto, item.cantidad(), item.precioUnitario());
            total += item.precioUnitario() * item.cantidad();
        }

        compra.setTotal(total);
        repoCompra.save(compra);

        return toResponse(compra);
    }

    @Transactional(readOnly = true)
    public ResponseCompra consultarCompra(int id) {
        Compra compra = repoCompra.findById(id)
                .orElseThrow(() -> new RuntimeException("Compra no encontrada con id: " + id));
        return toResponse(compra);
    }

    @Transactional(readOnly = true)
    public List<ResponseCompra> listarCompras() {
        return toResponses(repoCompra.findAll());
    }

    // ---- Consultas JPQL ----

    @Transactional(readOnly = true)
    public List<ResponseCompra> buscarPorCompradorDocumento(String documento) {
        return toResponses(repoCompra.buscarPorCompradorDocumento(documento));
    }

    @Transactional(readOnly = true)
    public List<ResponseCompra> buscarPorProductoNombre(String nombreProducto) {
        return toResponses(repoCompra.buscarPorProductoNombre(nombreProducto));
    }

    @Transactional(readOnly = true)
    public List<ResponseCompra> buscarPorCompradorYCategoria(int idComprador, String categoria) {
        return toResponses(repoCompra.buscarPorCompradorYCategoria(idComprador, categoria));
    }

    // ---- Consulta nativa ----

    @Transactional(readOnly = true)
    public List<ResponseResumenCompraProducto> obtenerResumenProductosPorComprador(int idComprador) {
        return repoCompra.obtenerResumenProductosPorComprador(idComprador).stream()
                .map(fila -> new ResponseResumenCompraProducto(
                        ((Number) fila[0]).intValue(),
                        (String) fila[1],
                        ((Number) fila[2]).longValue(),
                        ((Number) fila[3]).doubleValue()
                ))
                .toList();
    }

    private List<ResponseCompra> toResponses(List<Compra> compras) {
        return compras.stream()
                .map(this::toResponse)
                .toList();
    }

    private ResponseCompra toResponse(Compra compra) {
        List<ResponseCompra.ResponseCompraItem> items = compra.getItems().stream()
                .map(i -> new ResponseCompra.ResponseCompraItem(
                        i.getProducto().getNombre(),
                        i.getCantidad(),
                        i.getPrecioUnitario()
                ))
                .toList();

        return new ResponseCompra(
                compra.getId(),
                compra.getComprador().getNombre(),
                compra.getFechaCompra(),
                compra.getTotal(),
                items
        );
    }
}
