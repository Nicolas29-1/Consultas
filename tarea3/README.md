# tarea3 - Realizar publicacion / Realizar compra

Misma estructura que `tarea2` (`cu/<caso>/{Controller,Service,request,response}` + `dominio/{entity,repository}`).

## Configuracion
Las credenciales se leen de variables de entorno (no se guardan en el repo):

    DB_URL=jdbc:postgresql://HOST/BASE?sslmode=require
    DB_USER=...
    DB_PASSWORD=...

Arranca la app una vez (crea tablas y secuencias) y luego ejecuta `datos-prueba.sql`.

## Modelo
Usuario (vendedor/comprador) y Producto son catalogos (como Pasajero/Plato).
Publicacion -> PublicacionItem -> Producto, con vendedor (Usuario).
Compra -> CompraItem -> Producto, con comprador (Usuario).

## Endpoints
POST /publicacion/nueva

    {"titulo":"Oferta tech","idVendedor":1,
     "items":[{"idProducto":1,"cantidad":1,"precioUnitario":2500},
              {"idProducto":2,"cantidad":2,"precioUnitario":80}]}

POST /compra/nueva

    {"idComprador":2,
     "items":[{"idProducto":1,"cantidad":1,"precioUnitario":2500}]}

GET /publicacion/todas | /publicacion/{id}
GET /compra/todas | /compra/{id}

## Consultas (RepoPublicacion / RepoCompra)
JPQL (@Query, varias entidades):
1. GET /publicacion/producto/{idProducto}                    buscarPorProductoId
2. GET /publicacion/vendedor?nombre=ana                      buscarPorVendedorNombre
3. GET /publicacion/categoria?categoria=Tecnologia           buscarPorCategoriaProducto
4. GET /compra/documento/{documento}                         buscarPorCompradorDocumento
5. GET /compra/producto?nombre=laptop                        buscarPorProductoNombre
6. GET /compra/comprador/{idComprador}/categoria?categoria=  buscarPorCompradorYCategoria

Nativas (nativeQuery = true, varias tablas):
1. GET /publicacion/resumen                                  obtenerResumenPorPublicacion
2. GET /compra/comprador/{idComprador}/resumen-productos     obtenerResumenProductosPorComprador
