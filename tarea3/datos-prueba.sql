-- Ejecutar DESPUES de arrancar la aplicacion una vez
-- (ddl-auto=update crea las tablas y las secuencias).
INSERT INTO usuario (id, nombre, documento) VALUES
  (nextval('sec_usuario'), 'Ana Torres',  '10000001'),
  (nextval('sec_usuario'), 'Luis Quispe', '10000002');

INSERT INTO producto (id, nombre, categoria, precio) VALUES
  (nextval('sec_producto'), 'Laptop Lenovo',    'Tecnologia', 2500),
  (nextval('sec_producto'), 'Mouse Logitech',   'Tecnologia', 80),
  (nextval('sec_producto'), 'Silla Ergonomica', 'Hogar',      600);
