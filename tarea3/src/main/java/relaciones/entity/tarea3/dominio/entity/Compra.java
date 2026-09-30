package relaciones.entity.tarea3.dominio.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "compra")
public class Compra {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sec_compra")
    @SequenceGenerator(
        name = "sec_compra",
        sequenceName = "sec_compra",
        allocationSize = 1
    )
    private Integer id;

    @Column(name = "fecha_compra", nullable = false)
    private LocalDateTime fechaCompra;

    private double total;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_comprador", nullable = false)
    private Usuario comprador;

    @OneToMany(
        mappedBy = "compra",
        cascade = CascadeType.ALL,
        orphanRemoval = true
    )
    private List<CompraItem> items = new ArrayList<>();

    @PrePersist
    protected void prePersist() {
        if (fechaCompra == null) {
            fechaCompra = LocalDateTime.now();
        }
    }

    public void agregarItem(Producto producto, int cantidad, Double precioUnitario) {
        CompraItem item = new CompraItem();
        item.setCompra(this);
        item.setProducto(producto);
        item.setCantidad(cantidad);
        item.setPrecioUnitario(precioUnitario);
        items.add(item);
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public LocalDateTime getFechaCompra() {
        return fechaCompra;
    }

    public void setFechaCompra(LocalDateTime fechaCompra) {
        this.fechaCompra = fechaCompra;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public Usuario getComprador() {
        return comprador;
    }

    public void setComprador(Usuario comprador) {
        this.comprador = comprador;
    }

    public List<CompraItem> getItems() {
        return items;
    }

    public void setItems(List<CompraItem> items) {
        this.items = items;
    }
}
