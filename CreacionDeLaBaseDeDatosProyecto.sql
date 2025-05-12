create database Banco;
use Banco;
create table cuenta( 
IBAN varchar(24) primary key,
saldo float default 0
);
create table cliente(
Id int auto_increment primary key,
DNI varchar(10) unique,
Nombre varchar(50),
Apellidos varchar(100),
Direccion varchar(150),
ciudad varchar(20),
fechaNac date,
IBAN varchar(24) unique,
contraseña varchar(20)
);
alter table cliente add constraint ClienteIBAN foreign key (IBAN) references Cuenta(IBAN) on delete cascade on update cascade;
alter table cliente add telefono int;
select * from cuenta;
select * from cliente;
create table acciones (
idAccion int auto_increment primary key,
cajero int,
IBAN varchar(24),
cantidad float,
fecha TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
foreign key (IBAN) references Cuenta(IBAN) on delete cascade on update cascade
);
alter table acciones add column tipo varchar(20) not null;
alter table acciones add constraint chkeckTipo check(tipo in ('TRANSFERENCIA','INGRESO','RETIRADA'));

create table transferencia(
idTransferencia int auto_increment primary key,
IBANDestino varchar(24),
idAccion int,
foreign key(idAccion) references acciones(idAccion) on delete cascade on update cascade
);

create table retirada(
idretirada int auto_increment primary key,
idAccion int,
foreign key(idAccion) references acciones(idAccion) on delete cascade on update cascade
);

select * from Ingreso;
create table Ingreso(
idIngreso int auto_increment primary key,
idAccion int,
foreign key(idAccion) references acciones(idAccion) on delete cascade on update cascade
);


DELIMITER $$
CREATE TRIGGER insertarIBAN
BEFORE INSERT ON cliente
FOR EACH ROW
BEGIN
    IF NOT EXISTS (
        SELECT 1 FROM cuenta WHERE IBAN = NEW.IBAN
    ) THEN
        INSERT INTO cuenta (IBAN) VALUES (NEW.IBAN);
    END IF;
END$$
DELIMITER ;

DELETE FROM CLIENTE WHERE DNI = '12';

DELIMITER $$
CREATE TRIGGER ELIMINARCUENTA
AFTER DELETE ON CLIENTE
FOR EACH ROW
BEGIN
	delete from cuenta where iban = old.iban;
END$$
DELIMITER ;
DELIMITER $$
create procedure insertarIbanDestino (in IbDest varchar(24), in ibanOR varchar(24))
begin
	DECLARE IDACC INT;
    select idAccion into IDACC from acciones where iban = ibanOR order by fecha desc limit 1;
    update transferencia set IBANDestino = ibDest where idAccion = IDACC;
end $$
delimiter ;

DELIMITER $$

CREATE TRIGGER INSERTAR_ACCION
AFTER INSERT ON acciones
FOR EACH ROW
BEGIN
    IF NEW.tipo = 'INGRESO' THEN
        INSERT INTO ingreso (idAccion) VALUES (NEW.idAccion);
    END IF;

    IF NEW.tipo = 'TRANSFERENCIA' THEN
        INSERT INTO transferencia (idAccion) VALUES (NEW.idAccion);
    END IF;

    IF NEW.tipo = 'RETIRADA' THEN
        INSERT INTO retirada (idAccion) VALUES (NEW.idAccion);
    END IF;
END$$
DELIMITER ;


SELECT * FROM (
    select acciones.idAccion, acciones.cajero, acciones.IBAN, acciones.cantidad, acciones.fecha, acciones.tipo, 
           'Movimiento sin iban destino' as IBANDestino
    from acciones 
    join retirada on acciones.idAccion = retirada.idaccion
    where iban like ?
    union

    select acciones.idAccion, acciones.cajero, acciones.IBAN, acciones.cantidad, acciones.fecha, acciones.tipo, 
           'Movimiento sin iban destino' as IBANDestino
    from acciones 
    join ingreso on acciones.idAccion = ingreso.idaccion
	where iban like ?
    union

    select acciones.idAccion, acciones.cajero, acciones.IBAN, acciones.cantidad, acciones.fecha, acciones.tipo, 
           transferencia.IBANDestino
    from acciones 
    join transferencia on acciones.idAccion = transferencia.idaccion
    where iban like ?
) as movimientos
ORDER BY fecha DESC;

create index idx_dni on cliente(dni);
create index idx_iban on cliente(iban);
