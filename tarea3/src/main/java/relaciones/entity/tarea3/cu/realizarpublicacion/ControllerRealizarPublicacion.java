package relaciones.entity.tarea3.cu.realizarpublicacion;

import org.springframework.web.bind.annotation.*;
import relaciones.entity.tarea3.cu.realizarpublicacion.request.RequestPublicacion;
import relaciones.entity.tarea3.cu.realizarpublicacion.response.ResponsePublicacion;
import relaciones.entity.tarea3.cu.realizarpublicacion.response.ResponseResumenPublicacion;

import java.util.List;

@RestController
@RequestMapping("/publicacion")
public class ControllerRealizarPublicacion {

    private final ServiceRealizarPublicacion serviceRealizarPublicacion;

    public ControllerRealizarPublicacion(ServiceRealizarPublicacion serviceRealizarPublicacion) {
        this.serviceRealizarPublicacion = serviceRealizarPublicacion;
    }

    @PostMapping("/nueva")
    public ResponsePublicacion guardarPublicacion(@RequestBody RequestPublicacion request) {
        return serviceRealizarPublicacion.realizarPublicacion(request);
    }

    @GetMapping("/todas")
    public List<ResponsePublicacion> listarTodas() {
        return serviceRealizarPublicacion.listarPublicaciones();
    }

    @GetMapping("/{id}")
    public ResponsePublicacion consultarPublicacion(@PathVariable int id) {
        return serviceRealizarPublicacion.consultarPublicacion(id);
    }

    @GetMapping("/producto/{idProducto}")
    public List<ResponsePublicacion> buscarPorProducto(@PathVariable int idProducto) {
        return serviceRealizarPublicacion.buscarPorProductoId(idProducto);
    }

    @GetMapping("/vendedor")
    public List<ResponsePublicacion> buscarPorVendedor(@RequestParam("nombre") String nombre) {
        return serviceRealizarPublicacion.buscarPorVendedorNombre(nombre);
    }

    @GetMapping("/categoria")
    public List<ResponsePublicacion> buscarPorCategoria(@RequestParam("categoria") String categoria) {
        return serviceRealizarPublicacion.buscarPorCategoriaProducto(categoria);
    }

    @GetMapping("/resumen")
    public List<ResponseResumenPublicacion> resumenPublicaciones() {
        return serviceRealizarPublicacion.obtenerResumenPublicaciones();
    }
}
