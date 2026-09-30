package relaciones.entity.tarea3.dominio.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import relaciones.entity.tarea3.dominio.entity.Compra;

import java.util.List;

public interface RepoCompra extends JpaRepository<Compra, Integer> {

    // JPQL 4: Compra + Usuario (comprador)
    @Query("""
        SELECT c
        FROM Compra c
        JOIN c.comprador comprador
        WHERE comprador.documento = :documento
    """)
    List<Compra> buscarPorCompradorDocumento(@Param("documento") String documento);

    // JPQL 5: Compra + CompraItem + Producto
    @Query("""
        SELECT DISTINCT c
        FROM Compra c
        JOIN c.items item
        JOIN item.producto producto
        WHERE LOWER(producto.nombre) LIKE LOWER(CONCAT('%', :productoNombre, '%'))
    """)
    List<Compra> buscarPorProductoNombre(@Param("productoNombre") String productoNombre);

    // JPQL 6: Compra + Usuario + CompraItem + Producto
    @Query("""
        SELECT DISTINCT c
        FROM Compra c
        JOIN c.comprador comprador
        JOIN c.items item
        JOIN item.producto producto
        WHERE comprador.id = :compradorId
          AND producto.categoria = :categoria
    """)
    List<Compra> buscarPorCompradorYCategoria(@Param("compradorId") int compradorId,
                                              @Param("categoria") String categoria);

    // Nativo 2: compra + compra_item + producto
    @Query(value = """
    SELECT
        pr.id AS producto_id,
        pr.nombre AS producto_nombre,
        COALESCE(SUM(cit.cantidad), 0) AS unidades_compradas,
        COALESCE(SUM(cit.cantidad * cit.precio_unitario), 0) AS monto_total
    FROM compra c
    JOIN compra_item cit ON cit.id_compra = c.id
    JOIN producto pr ON pr.id = cit.id_producto
    WHERE c.id_comprador = :compradorId
    GROUP BY pr.id, pr.nombre
    ORDER BY monto_total DESC
    """, nativeQuery = true)
    List<Object[]> obtenerResumenProductosPorComprador(@Param("compradorId") int compradorId);
}
