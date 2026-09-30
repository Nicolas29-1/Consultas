package relaciones.entity.tarea3.cu.realizarpublicacion;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import relaciones.entity.tarea3.cu.realizarpublicacion.request.RequestPublicacion;
import relaciones.entity.tarea3.cu.realizarpublicacion.request.RequestPublicacion.RequestPublicacionItem;
import relaciones.entity.tarea3.cu.realizarpublicacion.response.ResponsePublicacion;
import relaciones.entity.tarea3.cu.realizarpublicacion.response.ResponseResumenPublicacion;
import relaciones.entity.tarea3.dominio.entity.Producto;
import relaciones.entity.tarea3.dominio.entity.Publicacion;
import relaciones.entity.tarea3.dominio.entity.Usuario;
import relaciones.entity.tarea3.dominio.repository.RepoProducto;
import relaciones.entity.tarea3.dominio.repository.RepoPublicacion;
import relaciones.entity.tarea3.dominio.repository.RepoUsuario;

import java.util.List;

@Service
public class ServiceRealizarPublicacion {

    private final RepoUsuario repoUsuario;
    private final RepoProducto repoProducto;
    private final RepoPublicacion repoPublicacion;

    public ServiceRealizarPublicacion(RepoUsuario repoUsuario,
                                      RepoProducto repoProducto,
                                      RepoPublicacion repoPublicacion) {
        this.repoUsuario = repoUsuario;
        this.repoProducto = repoProducto;
        this.repoPublicacion = repoPublicacion;
    }

    @Transactional
    public ResponsePublicacion realizarPublicacion(RequestPublicacion request) {
        if (request.items() == null || request.items().isEmpty()) {
            throw new RuntimeException("La publicacion debe tener al menos un item");
        }

        Usuario vendedor = repoUsuario.findById(request.idVendedor())
                .orElseThrow(() -> new RuntimeException(
                        "Vendedor no encontrado con id: " + request.idVendedor()));

        Publicacion publicacion = new Publicacion();
        publicacion.setTitulo(request.titulo());
        publicacion.setVendedor(vendedor);

        double total = 0.0;

        for (RequestPublicacionItem item : request.items()) {
            if (item.cantidad() <= 0) {
                throw new RuntimeException("La cantidad debe ser mayor a 0");
            }
            if (item.precioUnitario() == null || item.precioUnitario() < 0) {
                throw new RuntimeException("El precio unitario no es valido");
            }

            Producto producto = repoProducto.findById(item.idProducto())
                    .orElseThrow(() -> new RuntimeException(
                            "Producto no encontrado con id: " + item.idProducto()));

            publicacion.agregarItem(producto, item.cantidad(), item.precioUnitario());
            total += item.precioUnitario() * item.cantidad();
        }

        publicacion.setTotal(total);
        repoPublicacion.save(publicacion);

        return toResponse(publicacion);
    }

    @Transactional(readOnly = true)
    public ResponsePublicacion consultarPublicacion(int id) {
        Publicacion publicacion = repoPublicacion.findById(id)
                .orElseThrow(() -> new RuntimeException("Publicacion no encontrada con id: " + id));
        return toResponse(publicacion);
    }

    @Transactional(readOnly = true)
    public List<ResponsePublicacion> listarPublicaciones() {
        return toResponses(repoPublicacion.findAll());
    }

    // ---- Consultas JPQL ----

    @Transactional(readOnly = true)
    public List<ResponsePublicacion> buscarPorProductoId(int idProducto) {
        return toResponses(repoPublicacion.buscarPorProductoId(idProducto));
    }

    @Transactional(readOnly = true)
    public List<ResponsePublicacion> buscarPorVendedorNombre(String nombreVendedor) {
        return toResponses(repoPublicacion.buscarPorVendedorNombre(nombreVendedor));
    }

    @Transactional(readOnly = true)
    public List<ResponsePublicacion> buscarPorCategoriaProducto(String categoria) {
        return toResponses(repoPublicacion.buscarPorCategoriaProducto(categoria));
    }

    // ---- Consulta nativa ----

    @Transactional(readOnly = true)
    public List<ResponseResumenPublicacion> obtenerResumenPublicaciones() {
        return repoPublicacion.obtenerResumenPorPublicacion().stream()
                .map(fila -> new ResponseResumenPublicacion(
                        ((Number) fila[0]).intValue(),
                        (String) fila[1],
                        ((Number) fila[2]).longValue(),
                        ((Number) fila[3]).longValue()
                ))
                .toList();
    }

    private List<ResponsePublicacion> toResponses(List<Publicacion> publicaciones) {
        return publicaciones.stream()
                .map(this::toResponse)
                .toList();
    }

    private ResponsePublicacion toResponse(Publicacion publicacion) {
        List<ResponsePublicacion.ResponsePublicacionItem> items = publicacion.getItems().stream()
                .map(i -> new ResponsePublicacion.ResponsePublicacionItem(
                        i.getProducto().getNombre(),
                        i.getCantidad(),
                        i.getPrecioUnitario()
                ))
                .toList();

        return new ResponsePublicacion(
                publicacion.getId(),
                publicacion.getTitulo(),
                publicacion.getVendedor().getNombre(),
                publicacion.getFechaPublicacion(),
                publicacion.getTotal(),
                items
        );
    }
}
