package relaciones.entity.tarea3.dominio.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "publicacion")
public class Publicacion {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sec_publicacion")
    @SequenceGenerator(
        name = "sec_publicacion",
        sequenceName = "sec_publicacion",
        allocationSize = 1
    )
    private Integer id;

    @Column(nullable = false, length = 150)
    private String titulo;

    @Column(name = "fecha_publicacion", nullable = false)
    private LocalDateTime fechaPublicacion;

    private double total;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_vendedor", nullable = false)
    private Usuario vendedor;

    @OneToMany(
        mappedBy = "publicacion",
        cascade = CascadeType.ALL,
        orphanRemoval = true
    )
    private List<PublicacionItem> items = new ArrayList<>();

    @PrePersist
    protected void prePersist() {
        if (fechaPublicacion == null) {
            fechaPublicacion = LocalDateTime.now();
        }
    }

    public void agregarItem(Producto producto, int cantidad, Double precioUnitario) {
        PublicacionItem item = new PublicacionItem();
        item.setPublicacion(this);
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

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public LocalDateTime getFechaPublicacion() {
        return fechaPublicacion;
    }

    public void setFechaPublicacion(LocalDateTime fechaPublicacion) {
        this.fechaPublicacion = fechaPublicacion;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public Usuario getVendedor() {
        return vendedor;
    }

    public void setVendedor(Usuario vendedor) {
        this.vendedor = vendedor;
    }

    public List<PublicacionItem> getItems() {
        return items;
    }

    public void setItems(List<PublicacionItem> items) {
        this.items = items;
    }
}
