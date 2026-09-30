package relaciones.entity.tarea3.dominio.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import relaciones.entity.tarea3.dominio.entity.Producto;

public interface RepoProducto extends JpaRepository<Producto, Integer> {
}
