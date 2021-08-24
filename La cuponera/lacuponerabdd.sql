-- phpMyAdmin SQL Dump
-- version 4.8.4
-- https://www.phpmyadmin.net/
--
-- Servidor: 127.0.0.1
-- Tiempo de generación: 29-08-2019 a las 01:13:55
-- Versión del servidor: 10.1.37-MariaDB
-- Versión de PHP: 7.3.1

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET AUTOCOMMIT = 0;
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Base de datos: `lacuponerabdd`
--
CREATE DATABASE IF NOT EXISTS `lacuponerabdd` DEFAULT CHARACTER SET utf8 COLLATE utf8_spanish2_ci;
USE `lacuponerabdd`;

DELIMITER $$
--
-- Procedimientos
--
DROP PROCEDURE IF EXISTS `sp_eliminarDependiente`$$
CREATE DEFINER=`root`@`localhost` PROCEDURE `sp_eliminarDependiente` (IN `_codigo` VARCHAR(30))  NO SQL
delete from dependientes where correo = _codigo$$

DROP PROCEDURE IF EXISTS `sp_eliminarEmpresa`$$
CREATE DEFINER=`root`@`localhost` PROCEDURE `sp_eliminarEmpresa` (IN `_codigo` VARCHAR(30))  NO SQL
delete from empresasofertantes where codigo = _codigo$$

DROP PROCEDURE IF EXISTS `sp_eliminarRubro`$$
CREATE DEFINER=`root`@`localhost` PROCEDURE `sp_eliminarRubro` (IN `_codigo` INT)  NO SQL
delete from rubros where codigo = _codigo$$

DROP PROCEDURE IF EXISTS `sp_eliminarUsuario`$$
CREATE DEFINER=`root`@`localhost` PROCEDURE `sp_eliminarUsuario` (IN `_dui` VARCHAR(30))  NO SQL
delete from usuarios where dui = _dui$$

DROP PROCEDURE IF EXISTS `sp_insertarCliente`$$
CREATE DEFINER=`root`@`localhost` PROCEDURE `sp_insertarCliente` (IN `_dui` VARCHAR(30), IN `_nombres` VARCHAR(30), IN `_apellidos` VARCHAR(30), IN `_telefono` INT(8), IN `_direccion` VARCHAR(300), IN `_correo` VARCHAR(50), IN `_password` VARCHAR(30))  NO SQL
insert into usuarios(dui,nombres,apellidos,telefono,direccion,tipo,correo,password) values (_dui, _nombres, _apellidos, _telefono, _direccion, "Cliente", _correo, _password)$$

DROP PROCEDURE IF EXISTS `sp_insertarCupon`$$
CREATE DEFINER=`root`@`localhost` PROCEDURE `sp_insertarCupon` (IN `_codigo` VARCHAR(30), IN `_dui` VARCHAR(30), IN `_oferta` INT, IN `_estado` VARCHAR(30), IN `_fechaVencimiento` DATE)  NO SQL
insert into cupones(codigo,dui,codigoOferta,estado,fechaVencimiento) values(_codigo,_dui,_oferta,_estado,_fechaVencimiento)$$

DROP PROCEDURE IF EXISTS `sp_insertarDependiente`$$
CREATE DEFINER=`root`@`localhost` PROCEDURE `sp_insertarDependiente` (IN `_correo` VARCHAR(30), IN `_nombres` VARCHAR(30), IN `_apellidos` VARCHAR(30), IN `_clave` VARCHAR(30), IN `_codigoEmpresa` VARCHAR(30))  NO SQL
insert into dependientes(correo, nombres, apellidos, password, codigoEmpresa) values (_correo, _nombres, _apellidos, _clave, _codigoEmpresa)$$

DROP PROCEDURE IF EXISTS `sp_insertarEmpresa`$$
CREATE DEFINER=`root`@`localhost` PROCEDURE `sp_insertarEmpresa` (IN `_codigo` VARCHAR(30), IN `_nombre` VARCHAR(300), IN `_telefono` INT(8), IN `_contacto` VARCHAR(30), IN `_direccion` VARCHAR(300), IN `_comision` INT, IN `_codigoRubro` INT, IN `_correo` VARCHAR(300), IN `_password` VARCHAR(30))  NO SQL
INSERT INTO empresasofertantes(codigo, nombre, telefono, contacto, direccion, comision, codigoRubro, correo, password) values (_codigo, _nombre, _telefono, _contacto, _direccion, _comision, _codigoRubro, _correo, _password)$$

DROP PROCEDURE IF EXISTS `sp_insertarOferta`$$
CREATE DEFINER=`root`@`localhost` PROCEDURE `sp_insertarOferta` (IN `_titulo` VARCHAR(300), IN `_precio` DOUBLE, IN `_fechaInicio` DATE, IN `_fechaFinal` DATE, IN `_fechaFinalCupon` DATE, IN `_cuponesDisponibles` INT, IN `_descripcion` VARCHAR(300), IN `_detalles` VARCHAR(300), IN `_codigoEmpresa` VARCHAR(30))  NO SQL
insert into ofertas(titulo, precio, fechaInicio, fechaFinal, fechaFinalCupon, cuponesIniciales, cuponesDisponibles, descripcion, detalles, codigoEmpresa, estado) values (_titulo, _precio, _fechaInicio, _fechaFinal, _fechaFinalCupon, _cuponesDisponibles, _cuponesDisponibles, _descripcion, _detalles, _codigoEmpresa, "Pendiente")$$

DROP PROCEDURE IF EXISTS `sp_insertarRubro`$$
CREATE DEFINER=`root`@`localhost` PROCEDURE `sp_insertarRubro` (IN `_nombre` VARCHAR(30), IN `_descripcion` VARCHAR(300))  NO SQL
INSERT INTO rubros(nombre, descripcion) values (_nombre, _descripcion)$$

DROP PROCEDURE IF EXISTS `sp_listarDependientes`$$
CREATE DEFINER=`root`@`localhost` PROCEDURE `sp_listarDependientes` (IN `_codigo` VARCHAR(30))  NO SQL
select * from dependientes where codigoEmpresa = _codigo$$

DROP PROCEDURE IF EXISTS `sp_listarEmpresas`$$
CREATE DEFINER=`root`@`localhost` PROCEDURE `sp_listarEmpresas` ()  NO SQL
SELECT e.codigo, e.nombre, e.telefono, e.contacto, e.direccion, e.comision, r.nombre as nombreRubro, e.codigoRubro, e.correo, e.correlativo FROM `empresasofertantes` as e inner join rubros as r on r.codigo = e.codigoRubro$$

DROP PROCEDURE IF EXISTS `sp_listarOfertas`$$
CREATE DEFINER=`root`@`localhost` PROCEDURE `sp_listarOfertas` ()  NO SQL
SELECT o.codigo, o.titulo, o.precio, o.fechaInicio, o.fechaFinal, o.fechaFinalCupon, o.cuponesIniciales, o.cuponesDisponibles, o.cuponesVendidos, o.descripcion, o.detalles, o.codigoEmpresa, e.nombre as nombreEmpresa, o.estado FROM `ofertas` as o inner join empresasofertantes as e on e.codigo = o.codigoEmpresa$$

DROP PROCEDURE IF EXISTS `sp_listarOfertasDeEmpresa`$$
CREATE DEFINER=`root`@`localhost` PROCEDURE `sp_listarOfertasDeEmpresa` (IN `_codigo` VARCHAR(30))  NO SQL
select * from ofertas where codigoEmpresa = _codigo$$

DROP PROCEDURE IF EXISTS `sp_listarOfertasDisponibles`$$
CREATE DEFINER=`root`@`localhost` PROCEDURE `sp_listarOfertasDisponibles` (IN `_fecha` DATE)  NO SQL
SELECT o.codigo, o.titulo, o.precio, o.fechaInicio, o.fechaFinal, o.fechaFinalCupon, o.cuponesIniciales, o.cuponesDisponibles, o.cuponesVendidos, o.descripcion, o.detalles, o.codigoEmpresa, e.nombre as nombreEmpresa, o.estado FROM `ofertas` as o inner join empresasofertantes as e on e.codigo = o.codigoEmpresa where o.estado = "Aprobada" and o.cuponesDisponibles > 0 and o.fechaInicio <= _fecha$$

DROP PROCEDURE IF EXISTS `sp_listarOfertasPendientes`$$
CREATE DEFINER=`root`@`localhost` PROCEDURE `sp_listarOfertasPendientes` ()  NO SQL
SELECT o.codigo, o.titulo, o.precio, o.fechaInicio, o.fechaFinal, o.fechaFinalCupon, o.cuponesIniciales, o.cuponesDisponibles, o.cuponesVendidos, o.descripcion, o.detalles, o.codigoEmpresa, e.nombre as nombreEmpresa, o.estado FROM `ofertas` as o inner join empresasofertantes as e on e.codigo = o.codigoEmpresa where o.estado = 'Pendiente'$$

DROP PROCEDURE IF EXISTS `sp_listarOfertasPendientesDeEmpresa`$$
CREATE DEFINER=`root`@`localhost` PROCEDURE `sp_listarOfertasPendientesDeEmpresa` (IN `_codigo` VARCHAR(30))  NO SQL
SELECT o.codigo, o.titulo, o.precio, o.fechaInicio, o.fechaFinal, o.fechaFinalCupon, o.cuponesIniciales, o.cuponesDisponibles, o.cuponesVendidos, o.descripcion, o.detalles, o.codigoEmpresa, e.nombre as nombreEmpresa, o.estado FROM `ofertas` as o inner join empresasofertantes as e on e.codigo = o.codigoEmpresa where o.estado = 'Pendiente' and o.codigoEmpresa = _codigo$$

DROP PROCEDURE IF EXISTS `sp_listarRubros`$$
CREATE DEFINER=`root`@`localhost` PROCEDURE `sp_listarRubros` ()  NO SQL
select * from rubros order by codigo$$

DROP PROCEDURE IF EXISTS `sp_listarUsuarios`$$
CREATE DEFINER=`root`@`localhost` PROCEDURE `sp_listarUsuarios` ()  NO SQL
SELECT * FROM usuarios where tipo="cliente" ORDER BY nombres$$

DROP PROCEDURE IF EXISTS `sp_modificarRubro`$$
CREATE DEFINER=`root`@`localhost` PROCEDURE `sp_modificarRubro` (IN `_nombre` INT(30), IN `_descripcion` VARCHAR(300), IN `_codigo` INT)  NO SQL
UPDATE rubros SET nombre = _nombre, descripcion = _descripcion WHERE codigo = _codigo$$

DROP PROCEDURE IF EXISTS `sp_obtenerCupones`$$
CREATE DEFINER=`root`@`localhost` PROCEDURE `sp_obtenerCupones` (IN `_codigo_usuario` VARCHAR(30))  NO SQL
SELECT c.codigo, o.Titulo as Oferta, c.estado, c.codigoOferta FROM `cupones` as c inner join ofertas as o on o.codigo = c.codigoOferta where c.dui = _codigo_usuario order by estado$$

DROP PROCEDURE IF EXISTS `sp_obtenerDependiente`$$
CREATE DEFINER=`root`@`localhost` PROCEDURE `sp_obtenerDependiente` (IN `_correo` VARCHAR(30))  NO SQL
select * from dependientes where correo = _correo$$

DROP PROCEDURE IF EXISTS `sp_obtenerEmpresa`$$
CREATE DEFINER=`root`@`localhost` PROCEDURE `sp_obtenerEmpresa` (IN `_codigo` VARCHAR(30))  NO SQL
SELECT e.codigo, e.nombre, e.telefono, e.contacto, e.direccion, e.comision, r.nombre as nombreRubro, e.codigoRubro, e.correo, e.correlativo FROM `empresasofertantes` as e inner join rubros as r on r.codigo = e.codigoRubro WHERE e.codigo = _codigo$$

DROP PROCEDURE IF EXISTS `sp_obtenerOferta`$$
CREATE DEFINER=`root`@`localhost` PROCEDURE `sp_obtenerOferta` (IN `_codigo` INT)  NO SQL
SELECT o.codigo, o.titulo, o.precio, o.fechaInicio, o.fechaFinal, o.fechaFinalCupon, o.cuponesIniciales, o.cuponesDisponibles, o.cuponesVendidos, o.descripcion, o.detalles, o.codigoEmpresa, e.nombre as nombreEmpresa, e.comision as comisionEmpresa, o.estado, o.justificacion FROM `ofertas` as o inner join empresasofertantes as e on e.codigo = o.codigoEmpresa where o.codigo = _codigo$$

DROP PROCEDURE IF EXISTS `sp_obtenerRubro`$$
CREATE DEFINER=`root`@`localhost` PROCEDURE `sp_obtenerRubro` (IN `_codigo` INT)  NO SQL
SELECT * FROM rubros WHERE codigo = _codigo$$

DROP PROCEDURE IF EXISTS `sp_obtenerUsuario`$$
CREATE DEFINER=`root`@`localhost` PROCEDURE `sp_obtenerUsuario` (IN `_codigo_usuario` VARCHAR(30))  NO SQL
SELECT * FROM usuarios WHERE dui = _codigo_usuario$$

DELIMITER ;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `cupones`
--

DROP TABLE IF EXISTS `cupones`;
CREATE TABLE IF NOT EXISTS `cupones` (
  `codigo` varchar(30) COLLATE utf8_spanish2_ci NOT NULL,
  `dui` varchar(30) COLLATE utf8_spanish2_ci NOT NULL,
  `codigoOferta` int(11) NOT NULL,
  `estado` varchar(30) COLLATE utf8_spanish2_ci NOT NULL,
  `correlativo` int(11) NOT NULL AUTO_INCREMENT,
  `fechaVencimiento` date NOT NULL,
  PRIMARY KEY (`codigo`),
  UNIQUE KEY `correlativo` (`correlativo`),
  KEY `dui` (`dui`),
  KEY `codigoOferta` (`codigoOferta`)
) ENGINE=InnoDB AUTO_INCREMENT=16 DEFAULT CHARSET=utf8 COLLATE=utf8_spanish2_ci;

--
-- Volcado de datos para la tabla `cupones`
--

INSERT INTO `cupones` (`codigo`, `dui`, `codigoOferta`, `estado`, `correlativo`, `fechaVencimiento`) VALUES
('EMP001-0048770-9', '69821457-8', 2, 'Vencido', 9, '2019-08-27'),
('EMP001-5264871-3', '01568472-5', 2, 'Vencido', 3, '2019-08-27'),
('EMP001-7845847-1', '01568472-5', 2, 'Vencido', 1, '2019-08-27'),
('EMP004-7452618-4', '01568472-5', 3, 'Vencido', 4, '2019-08-17'),
('EMP004-7562414-2', '01568472-5', 3, 'Vencido', 2, '2019-08-17'),
('EMP005-0026834-6', '69821457-8', 4, 'Vencido', 6, '2019-08-28'),
('EMP005-0052027-10', '45217854-5', 4, 'Vencido', 10, '2019-08-28'),
('EMP005-0128033-8', '69821457-8', 4, 'Vencido', 8, '2019-08-28'),
('EMP005-0232240-7', '69821457-8', 4, 'Vencido', 7, '2019-08-28'),
('EMP008-8547126-5', '06075567-3', 5, 'Disponible', 5, '2019-08-31'),
('EMP010-0338942-15', '06075567-9', 17, 'Disponible', 15, '2019-08-29'),
('EMP017-0000932-13', '06075567-9', 14, 'Vencido', 13, '2019-08-28'),
('EMP017-0083249-14', '06075567-9', 15, 'Disponible', 14, '2019-09-01'),
('EMP017-0091533-12', '06075567-9', 14, 'Vencido', 12, '2019-08-28'),
('EMP017-0142414-11', '06075567-9', 13, 'Disponible', 11, '2019-09-01');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `dependientes`
--

DROP TABLE IF EXISTS `dependientes`;
CREATE TABLE IF NOT EXISTS `dependientes` (
  `correo` varchar(50) COLLATE utf8_spanish2_ci NOT NULL,
  `nombres` varchar(30) COLLATE utf8_spanish2_ci NOT NULL,
  `apellidos` varchar(30) COLLATE utf8_spanish2_ci NOT NULL,
  `password` varchar(30) COLLATE utf8_spanish2_ci NOT NULL,
  `codigoEmpresa` varchar(30) COLLATE utf8_spanish2_ci NOT NULL,
  PRIMARY KEY (`correo`),
  KEY `codigoEmpresa` (`codigoEmpresa`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_spanish2_ci;

--
-- Volcado de datos para la tabla `dependientes`
--

INSERT INTO `dependientes` (`correo`, `nombres`, `apellidos`, `password`, `codigoEmpresa`) VALUES
('adam@gmail.com', 'Adam  Enrique', 'Martinez Ferrer', 'I9XPMK', 'EMP008'),
('anitha@gmail.com', 'Ana Sara', 'Nuñez Garcia', '341OAB', 'EMP008'),
('bertita@gmail.com', 'Berta Inés', 'Santos Gutierrez', '3WGOOD', 'EMP005'),
('gerard@gmail.com', 'Gerard Guillermo', 'Vidal Cruz', 'KUASZT', 'EMP001'),
('mayor.cesm@gmail.com', 'Carlos Enrique', 'Serrano Majano', 'B16954', 'EMP005'),
('monica@gmail.com', 'Monica Abigail', 'Perez Montes', 'AE756A', 'EMP017'),
('samuel@gmail.com', 'Samuel Gonzalo', 'Dominguez Mora', 'C25GXG', 'EMP001');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `empresasofertantes`
--

DROP TABLE IF EXISTS `empresasofertantes`;
CREATE TABLE IF NOT EXISTS `empresasofertantes` (
  `codigo` varchar(30) COLLATE utf8_spanish2_ci NOT NULL,
  `nombre` varchar(300) COLLATE utf8_spanish2_ci NOT NULL,
  `telefono` int(8) NOT NULL,
  `contacto` varchar(30) COLLATE utf8_spanish2_ci NOT NULL,
  `direccion` varchar(300) COLLATE utf8_spanish2_ci NOT NULL,
  `comision` int(11) NOT NULL,
  `codigoRubro` int(11) NOT NULL,
  `correo` varchar(50) COLLATE utf8_spanish2_ci NOT NULL,
  `password` varchar(30) COLLATE utf8_spanish2_ci NOT NULL,
  `correlativo` int(11) NOT NULL AUTO_INCREMENT,
  PRIMARY KEY (`codigo`),
  UNIQUE KEY `UNIQUE_Correlativo_empresasOfertantes` (`correlativo`),
  UNIQUE KEY `correo` (`correo`),
  KEY `codigoRubro` (`codigoRubro`)
) ENGINE=InnoDB AUTO_INCREMENT=20 DEFAULT CHARSET=utf8 COLLATE=utf8_spanish2_ci;

--
-- Volcado de datos para la tabla `empresasofertantes`
--

INSERT INTO `empresasofertantes` (`codigo`, `nombre`, `telefono`, `contacto`, `direccion`, `comision`, `codigoRubro`, `correo`, `password`, `correlativo`) VALUES
('EMP001', 'Hotel Intercontinental de El Salvador', 22547863, 'Miguel Valladares', 'Blvd. Los Heroes, San Salvador', 8, 1, 'hotel@internacional.sv', 'hotel', 1),
('EMP002', 'Hotel Sinaloa', 22145875, 'Mónica Gaztambide', 'Col. Miralvalle, San Salvador', 7, 1, 'hotel@sinaloa.sv', '7Q68HE', 2),
('EMP003', 'Lacalaca', 22145687, 'Sergio Martínez', 'CC. Plaza Mundo, San Salvador', 25, 3, 'laca@laca.sv', '23FHLF', 3),
('EMP004', 'Kentucky Fried Chicken', 22145786, 'Paul Diaz', 'Blvd. del Ejercito, Soyapango', 23, 3, 'kentucky@friedchicken.sv', 'RJSXEV', 4),
('EMP005', 'Auto Parts Plus', 22547836, 'Abril Alonso', '29 Cl. Pte. Mejicanos', 12, 2, 'auto@parts.plus', 'auto', 5),
('EMP006', 'DryClean', 22147859, 'Alba Gallego', 'CC. Plaza Mundo, Soyapango', 5, 4, 'dry@clean.sv', 'RL6CG5', 6),
('EMP007', 'Intercontinental Autoparts', 22587416, 'Jordi Pons', '29. Cl. Pte. Mejicanos', 36, 2, 'intercontinental@autoparts.sv', '6Z45B3', 7),
('EMP008', 'Kiss Fresh Nightclub & Bar', 22964715, 'Mateo León', 'Blvd. Constitución, San Salvador', 15, 5, 'kiss@fresh.sv', 'RFCXBE', 8),
('EMP009', 'Super Repuestos', 23695475, 'Beatriz GÃ³mez', 'Av. Las Margaritas, Cuscatancingo', 10, 2, 'super@repuestos.sv', 'G3W59M', 9),
('EMP010', 'LIPS Nightblub & Bar', 21487596, 'Vanessa Hernández', 'Paseo Gral. Escalón, San Salvador', 14, 5, 'lisp@nightclub.sv', 'lips', 10),
('EMP011', 'Hotel Bella Ciao', 22547856, 'Sergio Marquina', 'Calle El Pedregal, Santa Tecla', 3, 1, 'bella@ciao.com', '2299ED', 11),
('EMP012', 'El Zocalo', 22145789, 'Juan Marquez', 'CC. Galerias, San Salvador', 14, 3, 'zocalo@zocalo2019.com', '343E44', 12),
('EMP013', 'Luxor', 22347855, 'Andrea Payes', 'Blvd. Constitucion, San Salvador', 23, 5, 'luxor_19@nightclub.com', '70BA43', 13),
('EMP014', 'Pollos Real', 22145635, 'Mario Rudamas', 'Av. Las Flores, Mejicanos', 15, 3, 'mayor.cesm@gmail.com', '1E27A7', 15),
('EMP016', 'Super Selectos', 21004564, 'Carlos Calleja', 'Col. San Benito, Santa Tecla', 45, 6, 'charliehcontacts@gmail.com', '21C9D5', 16),
('EMP017', 'Hardbard Bar & Disco El Salvador', 75462839, 'Jaqueline Henriquez', 'Paseo del Carmen, Santa Tecla', 14, 11, 'charliehcontact@gmail.com', 'hardbard', 19);

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `ofertas`
--

DROP TABLE IF EXISTS `ofertas`;
CREATE TABLE IF NOT EXISTS `ofertas` (
  `codigo` int(11) NOT NULL AUTO_INCREMENT,
  `titulo` varchar(300) COLLATE utf8_spanish2_ci NOT NULL,
  `precio` double NOT NULL DEFAULT '0',
  `fechaInicio` date NOT NULL,
  `fechaFinal` date NOT NULL,
  `fechaFinalCupon` date NOT NULL,
  `cuponesIniciales` int(11) NOT NULL DEFAULT '0',
  `cuponesDisponibles` int(11) NOT NULL DEFAULT '0',
  `cuponesVendidos` int(11) NOT NULL DEFAULT '0',
  `descripcion` varchar(300) COLLATE utf8_spanish2_ci NOT NULL DEFAULT 'No hay descripción',
  `detalles` varchar(300) COLLATE utf8_spanish2_ci DEFAULT 'No hay detalles',
  `codigoEmpresa` varchar(30) COLLATE utf8_spanish2_ci NOT NULL,
  `estado` varchar(30) COLLATE utf8_spanish2_ci NOT NULL DEFAULT 'En espera',
  `justificacion` varchar(300) COLLATE utf8_spanish2_ci DEFAULT NULL,
  PRIMARY KEY (`codigo`),
  KEY `codigoEmpresa` (`codigoEmpresa`)
) ENGINE=InnoDB AUTO_INCREMENT=18 DEFAULT CHARSET=utf8 COLLATE=utf8_spanish2_ci;

--
-- Volcado de datos para la tabla `ofertas`
--

INSERT INTO `ofertas` (`codigo`, `titulo`, `precio`, `fechaInicio`, `fechaFinal`, `fechaFinalCupon`, `cuponesIniciales`, `cuponesDisponibles`, `cuponesVendidos`, `descripcion`, `detalles`, `codigoEmpresa`, `estado`, `justificacion`) VALUES
(1, 'Medio anio con nosotros', 57, '2019-08-28', '2019-08-31', '2019-09-14', 100, 100, 0, 'Ven ahora en esta increÃ­ble oferta, por el precio de $57 USD recibes habitaciÃ³n doble para ti y un acompaÃ±ante', 'Incluye IVA y desayuno. Valido para 1 noche, 2 dÃ­as', 'EMP001', 'Descartada', 'No me agrada'),
(2, 'El bombazo habitacional', 3504.3, '2019-08-18', '2019-08-20', '2019-08-27', 50, 47, 3, 'Recibe una habitación sencilla por solo $14 USD ', 'Válido para estadía de un solo día. Precio no incluye IVA', 'EMP001', 'Vencida', NULL),
(3, 'Come gratis', 0, '2019-08-16', '2019-08-17', '2019-08-17', 100, 98, 2, 'Si te llamas \"Vanessa\" comes gratis. Presenta tu DUI en la sucursal mas cercana y disfruta de esta súper promoción', 'No hay detalles', 'EMP004', 'Vencida', NULL),
(4, 'Stocks en descuento', 25, '2019-08-27', '2019-08-30', '2019-08-28', 100, 96, 4, 'Descuentos de hasta el 70% en toda nuestra mercaderi­a de stocks para vehi­culos', 'No hay detalles', 'EMP005', 'Aprobada', NULL),
(5, 'En tu cumpleaños, ¡la pasas de lo mejor!', 10.5, '2019-08-01', '2019-08-31', '2019-08-31', 100, 99, 1, 'Si estás cumpliendo años, tu cover se convierte en consumible dentro de establecimiento', 'No aplica para bailes privados', 'EMP008', 'Aprobada', NULL),
(6, 'Desayuno con descuento', 1.5, '2019-09-01', '2019-09-30', '2019-09-30', 100, 100, 0, 'Todo septiembre recibe un 25% de descuento al comprar tu cupon', 'No hay detalles', 'EMP001', 'Pendiente', NULL),
(7, 'Almuerzos con descuento', 1.5, '2019-09-01', '2019-09-30', '2019-09-30', 100, 100, 0, 'Todo septiembre recibe almuerzos con el 25% de descuento al comprar tu cupon', 'No hay detalles', 'EMP001', 'Aprobada', NULL),
(8, 'Cenas con descuento', 1.5, '2019-09-01', '2019-09-30', '2019-09-30', 100, 100, 0, 'Todo septiembre recibe 25% de descuento en tus cenas al comprar tu cupon', 'No hay detalles', 'EMP001', 'Pendiente', NULL),
(9, 'Medidores analogos ya disponibles', 0, '2019-08-26', '2019-08-27', '2019-08-27', 100, 0, 0, 'Ven este 27 de agosto por tus medidores anologos', 'No hay detalles', 'EMP005', 'Vencida', NULL),
(10, 'LED en oferta', 6.95, '2019-09-26', '2019-09-26', '2019-09-30', 50, 50, 0, 'Tenemos nuestros LED para modificar silvines en oferta, trae tu cupon', 'Aplica presentando el dui y cupon', 'EMP005', 'Aprobada', NULL),
(11, '50% Descuento', 6, '2019-08-28', '2019-08-28', '2019-09-01', 25, 25, 0, 'Solo hoy, adquiere tu cupon y recibe cover a mitad de precio toda la noche del sabado 31 de agosto o domingo 1 de septiembre', '6PM a 8PM cerveza nacional a $1.50. Solo para las primeras 25 personas', 'EMP010', 'Vencida', NULL),
(12, 'Ultima semana de agosto', 0.25, '2019-08-25', '2019-08-27', '2019-08-31', 100, 100, 0, 'Al ingresar recibe una HEINEKEN de 500 ml', 'Valido de 6pm a 8pm', 'EMP010', 'Vencida', NULL),
(13, 'Chicas 50% cover', 4.5, '2019-08-28', '2019-09-01', '2019-09-01', 100, 99, 1, 'Ven este domingo, presenta el cupon y recibe cover a mitad de precio', 'No valido para hombres', 'EMP017', 'Aprobada', NULL),
(14, 'Chicos 10% cover', 9, '2019-08-27', '2019-08-28', '2019-08-28', 100, 98, 2, 'Presenta este cupon y recibe 10% de descuento en tu cover', 'No validos para mujeres', 'EMP017', 'Vencida', NULL),
(15, 'Bebidas al 2 x 1', 1.5, '2019-08-28', '2019-09-01', '2019-09-01', 100, 99, 1, 'Ben a disfrutar de este buenisima promocion en nuestras instalaciones', 'Valido para todo tipo de bebidas', 'EMP017', 'Aprobada', NULL),
(16, 'Beer & Drinks night', 1, '2019-08-30', '2019-09-01', '2019-09-01', 100, 100, 0, 'Todas las bebidas a $1.00 a partir de las 8PM', 'No hay detalles', 'EMP010', 'Aprobada', NULL),
(17, 'Girls night', 5, '2019-08-28', '2019-08-29', '2019-08-29', 100, 99, 1, 'Todas las chicas reciben cover a mitad de precio', 'No hay detalles', 'EMP010', 'Aprobada', NULL);

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `rubros`
--

DROP TABLE IF EXISTS `rubros`;
CREATE TABLE IF NOT EXISTS `rubros` (
  `codigo` int(11) NOT NULL AUTO_INCREMENT,
  `nombre` varchar(30) COLLATE utf8_spanish2_ci NOT NULL,
  `descripcion` varchar(300) COLLATE utf8_spanish2_ci NOT NULL,
  PRIMARY KEY (`codigo`)
) ENGINE=InnoDB AUTO_INCREMENT=12 DEFAULT CHARSET=utf8 COLLATE=utf8_spanish2_ci;

--
-- Volcado de datos para la tabla `rubros`
--

INSERT INTO `rubros` (`codigo`, `nombre`, `descripcion`) VALUES
(1, 'Hotelería', 'Categoría para empresas destinadas a la gestión hotelera del territorio nacional'),
(2, 'Mantenimiento automotriz', 'Categoría para empresas encargadas del mantenimiento de automotores'),
(3, 'Gastronomía', 'Categoría para empresas enfocadas en el consumo de alimentos gastronómicos'),
(4, 'Limpieza', 'Categoría para empresas de mantenimiento sanitario'),
(5, 'Nightclubs', 'Categoría para empresas dentro del área de nightclubs y entretenimiento para adultos'),
(6, 'Supermercados', 'Categorí­a para empresas dedicadas a la venta de productos'),
(10, 'Educacion', 'Categoria para empresas enfocadas en el area educacional como colegios'),
(11, 'Bar', 'Categoria para empresas enfocadas en el consumo de bebidas alcoholicas');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `usuarios`
--

DROP TABLE IF EXISTS `usuarios`;
CREATE TABLE IF NOT EXISTS `usuarios` (
  `dui` varchar(30) COLLATE utf8_spanish2_ci NOT NULL,
  `nombres` varchar(30) COLLATE utf8_spanish2_ci NOT NULL,
  `apellidos` varchar(30) COLLATE utf8_spanish2_ci NOT NULL,
  `telefono` int(8) NOT NULL,
  `direccion` varchar(300) COLLATE utf8_spanish2_ci NOT NULL,
  `tipo` varchar(30) COLLATE utf8_spanish2_ci NOT NULL,
  `estado` int(11) NOT NULL DEFAULT '0',
  `correo` varchar(50) COLLATE utf8_spanish2_ci NOT NULL,
  `password` varchar(30) COLLATE utf8_spanish2_ci NOT NULL,
  `correlativo` int(11) NOT NULL AUTO_INCREMENT,
  PRIMARY KEY (`dui`),
  UNIQUE KEY `correlativo` (`correlativo`),
  UNIQUE KEY `correo` (`correo`)
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8 COLLATE=utf8_spanish2_ci;

--
-- Volcado de datos para la tabla `usuarios`
--

INSERT INTO `usuarios` (`dui`, `nombres`, `apellidos`, `telefono`, `direccion`, `tipo`, `estado`, `correo`, `password`, `correlativo`) VALUES
('01568472-5', 'Lorena Beatriz', 'Marroquín Benitez', 74213635, 'Moncagua, San Miguel', 'Cliente', 0, 'lore147@gmail.com', 'lore147', 4),
('06075567-3', 'Carlos Enrique', 'Serrano Majano', 72213636, 'Ciudad Credisa, Soyapango', 'Administrador', 1, 'admin@gmail.com', 'admin', 1),
('06075567-9', 'Carlos', 'Serrano', 72213636, 'Ciudad Credisa, Soyapango, San Salvador', 'Cliente', 1, 'charliehcontact@gmail.com', 'carlos', 9),
('42654872-7', 'Andres Joel', 'Arturizo Gonzales', 74589632, 'Col. San Jose, Soyapango', 'Cliente', 0, 'andresito@gmail.com', 'andres', 5),
('45217854-5', 'Juan Ricardo', 'Rodríguez Vega', 74568216, 'Col. Layco, Mejicanos', 'Cliente', 1, 'juan1994@gmail.com', 'juan1994', 2),
('56248163-4', 'Luis Armando', 'Vidal Lara', 78496213, 'Apulo, Ilopango', 'Cliente', 0, 'luis159@gmail.com', 'luis159', 3),
('69821457-8', 'Luis Antonio', 'Rivas Bonilla', 72285463, 'Santa Tecla, San Salvador', 'Cliente', 1, 'mayor.cesm@gmail.com', '525691', 7);

--
-- Restricciones para tablas volcadas
--

--
-- Filtros para la tabla `cupones`
--
ALTER TABLE `cupones`
  ADD CONSTRAINT `cupones_ibfk_1` FOREIGN KEY (`dui`) REFERENCES `usuarios` (`dui`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `cupones_ibfk_2` FOREIGN KEY (`codigoOferta`) REFERENCES `ofertas` (`codigo`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Filtros para la tabla `dependientes`
--
ALTER TABLE `dependientes`
  ADD CONSTRAINT `dependientes_ibfk_1` FOREIGN KEY (`codigoEmpresa`) REFERENCES `empresasofertantes` (`codigo`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Filtros para la tabla `empresasofertantes`
--
ALTER TABLE `empresasofertantes`
  ADD CONSTRAINT `empresasofertantes_ibfk_1` FOREIGN KEY (`codigoRubro`) REFERENCES `rubros` (`codigo`);

--
-- Filtros para la tabla `ofertas`
--
ALTER TABLE `ofertas`
  ADD CONSTRAINT `ofertas_ibfk_1` FOREIGN KEY (`codigoEmpresa`) REFERENCES `empresasofertantes` (`codigo`) ON DELETE CASCADE ON UPDATE CASCADE;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
