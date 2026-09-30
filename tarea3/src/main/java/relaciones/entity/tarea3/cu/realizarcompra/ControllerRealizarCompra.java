package relaciones.entity.tarea3.cu.realizarcompra;

import org.springframework.web.bind.annotation.*;
import relaciones.entity.tarea3.cu.realizarcompra.request.RequestCompra;
import relaciones.entity.tarea3.cu.realizarcompra.response.ResponseCompra;
import relaciones.entity.tarea3.cu.realizarcompra.response.ResponseResumenCompraProducto;

import java.util.List;

@RestController
@RequestMapping("/compra")
public class ControllerRealizarCompra {

    private final ServiceRealizarCompra serviceRealizarCompra;

    public ControllerRealizarCompra(ServiceRealizarCompra serviceRealizarCompra) {
        this.serviceRealizarCompra = serviceRealizarCompra;
    }

    @PostMapping("/nueva")
    public ResponseCompra guardarCompra(@RequestBody RequestCompra request) {
        return serviceRealizarCompra.realizarCompra(request);
    }

    @GetMapping("/todas")
    public List<ResponseCompra> listarTodas() {
        return serviceRealizarCompra.listarCompras();
    }

    @GetMapping("/{id}")
    public ResponseCompra consultarCompra(@PathVariable int id) {
        return serviceRealizarCompra.consultarCompra(id);
    }

    @GetMapping("/documento/{documento}")
    public List<ResponseCompra> buscarPorDocumento(@PathVariable String documento) {
        return serviceRealizarCompra.buscarPorCompradorDocumento(documento);
    }

    @GetMapping("/producto")
    public List<ResponseCompra> buscarPorProducto(@RequestParam("nombre") String nombre) {
        return serviceRealizarCompra.buscarPorProductoNombre(nombre);
    }

    @GetMapping("/comprador/{idComprador}/categoria")
    public List<ResponseCompra> buscarPorCompradorYCategoria(@PathVariable int idComprador,
                                                             @RequestParam("categoria") String categoria) {
        return serviceRealizarCompra.buscarPorCompradorYCategoria(idComprador, categoria);
    }

    @GetMapping("/comprador/{idComprador}/resumen-productos")
    public List<ResponseResumenCompraProducto> resumenProductos(@PathVariable int idComprador) {
        return serviceRealizarCompra.obtenerResumenProductosPorComprador(idComprador);
    }
}
