package relaciones.entity.tarea3.dominio.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import relaciones.entity.tarea3.dominio.entity.Publicacion;

import java.util.List;

public interface RepoPublicacion extends JpaRepository<Publicacion, Integer> {

    // JPQL 1: Publicacion + PublicacionItem + Producto
    @Query("""
        SELECT DISTINCT p
        FROM Publicacion p
        JOIN p.items item
        JOIN item.producto producto
        WHERE producto.id = :productoId
    """)
    List<Publicacion> buscarPorProductoId(@Param("productoId") int productoId);

    // JPQL 2: Publicacion + Usuario (vendedor)
    @Query("""
        SELECT p
        FROM Publicacion p
        JOIN p.vendedor vendedor
        WHERE LOWER(vendedor.nombre) LIKE LOWER(CONCAT('%', :vendedorNombre, '%'))
    """)
    List<Publicacion> buscarPorVendedorNombre(@Param("vendedorNombre") String vendedorNombre);

    // JPQL 3: Publicacion + PublicacionItem + Producto (por categoria)
    @Query("""
        SELECT DISTINCT p
        FROM Publicacion p
        JOIN p.items item
        JOIN item.producto producto
        WHERE producto.categoria = :categoria
    """)
    List<Publicacion> buscarPorCategoriaProducto(@Param("categoria") String categoria);

    // Nativo 1: publicacion + usuario + publicacion_item
    @Query(value = """
    SELECT
        p.id AS publicacion_id,
        u.nombre AS vendedor,
        COUNT(pit.id) AS cantidad_items,
        COALESCE(SUM(pit.cantidad), 0) AS unidades_totales
    FROM publicacion p
    JOIN usuario u ON u.id = p.id_vendedor
    LEFT JOIN publicacion_item pit ON pit.id_publicacion = p.id
    GROUP BY p.id, u.nombre
    ORDER BY p.id
    """, nativeQuery = true)
    List<Object[]> obtenerResumenPorPublicacion();
}
