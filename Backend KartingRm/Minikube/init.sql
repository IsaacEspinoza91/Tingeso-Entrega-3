-- 00-init.sql
CREATE DATABASE IF NOT EXISTS plan_db CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
CREATE DATABASE IF NOT EXISTS cliente_db CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
CREATE DATABASE IF NOT EXISTS descuento_grupo_db CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
CREATE DATABASE IF NOT EXISTS dias_especiales_db CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
CREATE DATABASE IF NOT EXISTS rack_semanal_db CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
CREATE DATABASE IF NOT EXISTS reserva_db CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
CREATE DATABASE IF NOT EXISTS reportes_db CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;


-- Poblado
-- 01-plan.sql
USE plan_db;
CREATE TABLE IF NOT EXISTS plan (
    id bigint NOT NULL AUTO_INCREMENT PRIMARY KEY,
    descripcion VARCHAR(100) NOT NULL,
    duracion_total int NOT NULL,
    precio_feriado double NOT NULL,
    precio_fin_semana double NOT NULL,
    precio_regular double NOT NULL
);

INSERT INTO plan (descripcion, duracion_total, precio_feriado, precio_fin_semana, precio_regular) VALUES
('10 vueltas o máx 10 min', 30, 13000, 14000, 15000),
('15 vueltas o máx 15 min', 35, 18000, 19000, 20000),
('20 vueltas o máx 20 min', 40, 22000, 23000, 24000);

-- 02-descuento_grupo.sql
USE descuento_grupo_db;
CREATE TABLE IF NOT EXISTS descuento_grupo (
    id bigint NOT NULL AUTO_INCREMENT PRIMARY KEY,
    max_personas int DEFAULT NULL,
    min_personas int NOT NULL,
    porcentaje_descuento double NOT NULL
);

INSERT INTO descuento_grupo (min_personas, max_personas, porcentaje_descuento) VALUES 
(1, 2, 0),
(3, 5, 0.1),
(6, 10, 0.2),
(11, 15, 0.3);

-- 03-descuento_cliente.sql
USE cliente_db;
CREATE TABLE IF NOT EXISTS descuento_cliente_frecuente (
    id bigint NOT NULL AUTO_INCREMENT PRIMARY KEY,
    max_reservas int NOT NULL,
    min_reservas int NOT NULL,
    porcentaje_descuento double NOT NULL
);

INSERT INTO descuento_cliente_frecuente (min_reservas, max_reservas, porcentaje_descuento) VALUES 
(0, 1, 0),
(2, 4, 0.1),
(5, 6, 0.2),
(7, 99, 0.3);

-- 04-dia_feriado.sql
USE dias_especiales_db;
CREATE TABLE IF NOT EXISTS dia_feriado (
    id bigint NOT NULL AUTO_INCREMENT PRIMARY KEY,
    fecha date NOT NULL,
    nombre VARCHAR(255) DEFAULT NULL
);

-- Poblado tabla dia_feriado
-- Año 2025
INSERT INTO dia_feriado (nombre, fecha) VALUES
('Año Nuevo', '2025-01-01'),
('Viernes Santo', '2025-04-18'),
('Sábado Santo', '2025-04-19'),
('Día Nacional del Trabajo', '2025-05-01'),
('Día de las Glorias Navales', '2025-05-21'),
('Día Nacional de los Pueblos Indígenas', '2025-06-20'),
('San Pedro y San Pablo', '2025-06-29'),
('Elecciones Primarias Presidenciales y Parlamentarias', '2025-06-29'),
('Día de la Virgen del Carmen', '2025-07-16'),
('Asunción de la Virgen', '2025-08-15'),
('Independencia Nacional', '2025-09-18'),
('Día de las Glorias del Ejército', '2025-09-19'),
('Encuentro de Dos Mundos', '2025-10-12'),
('Día de las Iglesias Evangélicas y Protestantes', '2025-10-31'),
('Día de Todos los Santos', '2025-11-01'),
('Elecciones Presidenciales y Parlamentarias', '2025-11-16'),
('Inmaculada Concepción', '2025-12-08'),
('Elecciones Presidenciales (Segunda Vuelta)', '2025-12-14'),
('Navidad', '2025-12-25');

-- Año 2026
INSERT INTO dia_feriado (nombre, fecha) VALUES
('Año Nuevo', '2026-01-01'),
('Viernes Santo', '2026-04-03'),
('Sábado Santo', '2026-04-04'),
('Día Nacional del Trabajo', '2026-05-01'),
('Día de las Glorias Navales', '2026-05-21'),
('Día Nacional de los Pueblos Indígenas', '2026-06-21'),
('San Pedro y San Pablo', '2026-06-29'),
('Día de la Virgen del Carmen', '2026-07-16'),
('Asunción de la Virgen', '2026-08-15'),
('Independencia Nacional', '2026-09-18'),
('Día de las Glorias del Ejército', '2026-09-19'),
('Encuentro de Dos Mundos', '2026-10-12'),
('Día de las Iglesias Evangélicas y Protestantes', '2026-10-31'),
('Día de Todos los Santos', '2026-11-01'),
('Inmaculada Concepción', '2026-12-08'),
('Navidad', '2026-12-25');

-- Año 2027
INSERT INTO dia_feriado (nombre, fecha) VALUES
('Año Nuevo', '2027-01-01'),
('Viernes Santo', '2027-03-26'),
('Sábado Santo', '2027-03-27'),
('Día Nacional del Trabajo', '2027-05-01'),
('Día de las Glorias Navales', '2027-05-21'),
('Día Nacional de los Pueblos Indígenas', '2027-06-21'),
('San Pedro y San Pablo', '2027-06-28'),
('Día de la Virgen del Carmen', '2027-07-16'),
('Asunción de la Virgen', '2027-08-15'),
('Feriado Adicional Fiestas Patrias', '2027-09-17'),
('Independencia Nacional', '2027-09-18'),
('Día de las Glorias del Ejército', '2027-09-19'),
('Encuentro de Dos Mundos', '2027-10-11'),
('Día de las Iglesias Evangélicas y Protestantes', '2027-10-31'),
('Día de Todos los Santos', '2027-11-01'),
('Inmaculada Concepción', '2027-12-08'),
('Navidad', '2027-12-25');
