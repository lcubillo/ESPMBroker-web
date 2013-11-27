--
-- Create Schema Script 
--   Database Version   : 9.2.0.7.0 
--   Toad Version       : 10.0.0.41 
--   DB Connect String  : CM 
--   Schema             : ASP 
--   Script Created by  : ASP 
--   Script Created at  : 05/05/2011 10:58:37 
--   Physical Location  :  
--   Notes              :  
--

-- Object Counts: 
--   Functions: 2       Lines of Code: 57 
--   Indexes: 10        Columns: 13         
--   Procedures: 3      Lines of Code: 108 
--   Sequences: 2 
--   Tables: 11         Columns: 86         
--   Triggers: 1 


CREATE SEQUENCE SQ_ENVIO
  START WITH 170
  MAXVALUE 999999999999999999999999999
  MINVALUE 1
  NOCYCLE
  NOCACHE
  NOORDER;


CREATE SEQUENCE SQ_LISTA
  START WITH 5677
  MAXVALUE 999999999999999999999999999
  MINVALUE 1
  NOCYCLE
  CACHE 20
  NOORDER;


CREATE TABLE CLIENTES
(
  ID_CLIENTE      VARCHAR2(12 BYTE)             NOT NULL,
  NOMBRE          VARCHAR2(40 BYTE),
  PASSWORD        VARCHAR2(16 BYTE),
  FECHA_CREACION  DATE,
  PIE_HTML        CLOB,
  PIE_TEXTO       CLOB,
  REMITENTE       VARCHAR2(60 BYTE),
  URL_BAJA        VARCHAR2(150 BYTE)
)
LOGGING 
NOCOMPRESS 
NOCACHE
NOPARALLEL
NOMONITORING;


CREATE TABLE ENVIADOS
(
  ID_ENVIO   NUMBER(6)                          NOT NULL,
  ID_LISTA   NUMBER(6)                          NOT NULL,
  ID_EMAIL   NUMBER(13)                         NOT NULL,
  FEC_ENVIO  DATE,
  LECTURAS   NUMBER(3),
  CLICKS     NUMBER(3),
  ESTADO     VARCHAR2(1 BYTE)
)
LOGGING 
NOCOMPRESS 
NOCACHE
NOPARALLEL
NOMONITORING;


CREATE TABLE ENVIOS
(
  ID_CLIENTE    VARCHAR2(12 BYTE)               NOT NULL,
  ID_ENVIO      NUMBER(6)                       NOT NULL,
  ETIQUETA      VARCHAR2(20 BYTE),
  DESCRIPCION   VARCHAR2(80 BYTE),
  ESTADO        VARCHAR2(1 BYTE)                NOT NULL,
  FECHA_ENVIO   DATE,
  HTML          CLOB,
  TEXTO         CLOB,
  REMITENTE     VARCHAR2(60 BYTE),
  ASUNTO        VARCHAR2(100 BYTE),
  REPLYTO       VARCHAR2(60 BYTE),
  INICIO_ENVIO  DATE,
  MAQUINA       VARCHAR2(20 BYTE)
)
LOGGING 
NOCOMPRESS 
NOCACHE
NOPARALLEL
NOMONITORING;


CREATE TABLE ENVIOS_LISTAS
(
  ID_ENVIO  NUMBER(6)                           NOT NULL,
  ID_LISTA  NUMBER(6)                           NOT NULL
)
LOGGING 
NOCOMPRESS 
NOCACHE
NOPARALLEL
NOMONITORING;


CREATE TABLE FBL
(
  ID_ENVIO  NUMBER(6)                           NOT NULL,
  ID_LISTA  NUMBER(6)                           NOT NULL,
  ID_EMAIL  NUMBER(13)                          NOT NULL,
  FECHA     DATE,
  TIPO      VARCHAR2(1 BYTE)
)
LOGGING 
NOCOMPRESS 
NOCACHE
NOPARALLEL
NOMONITORING;


CREATE TABLE LISTAS
(
  ID_CLIENTE      VARCHAR2(12 BYTE)             NOT NULL,
  ID_LISTA        NUMBER(6)                     NOT NULL,
  NOMBRE_LISTA    VARCHAR2(40 BYTE)             NOT NULL,
  NUM_REGISTROS   NUMBER(8),
  ESTADO          VARCHAR2(1 BYTE),
  FECHA_CREACION  DATE,
  FECHA_BAJA      DATE,
  ETIQUETA_T1     VARCHAR2(12 BYTE),
  ETIQUETA_T2     VARCHAR2(12 BYTE),
  ETIQUETA_T3     VARCHAR2(12 BYTE)
)
LOGGING 
NOCOMPRESS 
NOCACHE
NOPARALLEL
NOMONITORING;


CREATE TABLE MONOGRAFIAS
(
  EMAIL   VARCHAR2(100 BYTE),
  PAIS    VARCHAR2(50 BYTE),
  SEXO    VARCHAR2(1 BYTE),
  NOMBRE  VARCHAR2(100 BYTE)
)
LOGGING 
NOCOMPRESS 
NOCACHE
NOPARALLEL
NOMONITORING;


CREATE TABLE REBOTES
(
  ID_ENVIO  NUMBER(6)                           NOT NULL,
  ID_LISTA  NUMBER(6)                           NOT NULL,
  ID_EMAIL  NUMBER(13)                          NOT NULL,
  FECHA     DATE
)
LOGGING 
NOCOMPRESS 
NOCACHE
NOPARALLEL
NOMONITORING;


CREATE TABLE TRACKING
(
  ID_ENVIO  NUMBER(6)                           NOT NULL,
  ID_LISTA  NUMBER(6)                           NOT NULL,
  ID_EMAIL  NUMBER(13)                          NOT NULL,
  FECHA     DATE,
  TIPO      VARCHAR2(1 BYTE),
  IP        VARCHAR2(31 BYTE),
  URL       VARCHAR2(100 BYTE),
  NUM       NUMBER(2)
)
LOGGING 
NOCOMPRESS 
NOCACHE
NOPARALLEL
NOMONITORING;


CREATE TABLE USUARIOS
(
  ID_EMAIL           NUMBER(13)                 NOT NULL,
  ID_LISTA           NUMBER(6)                  NOT NULL,
  EMAIL              VARCHAR2(80 BYTE)          NOT NULL,
  NOMBRE             VARCHAR2(30 BYTE),
  APELLIDOS          VARCHAR2(40 BYTE),
  TRATAMIENTO        VARCHAR2(6 BYTE),
  FECHA_CREACION     DATE,
  FEC_ULT_ACTIVIDAD  DATE,
  T1                 VARCHAR2(20 BYTE),
  T2                 VARCHAR2(20 BYTE),
  T3                 VARCHAR2(20 BYTE)
)
LOGGING 
NOCOMPRESS 
NOCACHE
NOPARALLEL
NOMONITORING;


CREATE TABLE USUARIOS_BORRADOS
(
  ID_EMAIL           NUMBER(13)                 NOT NULL,
  ID_LISTA           NUMBER(6)                  NOT NULL,
  EMAIL              VARCHAR2(80 BYTE)          NOT NULL,
  NOMBRE             VARCHAR2(30 BYTE),
  APELLIDOS          VARCHAR2(40 BYTE),
  TRATAMIENTO        VARCHAR2(6 BYTE),
  FECHA_CREACION     DATE,
  FEC_ULT_ACTIVIDAD  DATE,
  T1                 VARCHAR2(20 BYTE),
  T2                 VARCHAR2(20 BYTE),
  T3                 VARCHAR2(20 BYTE),
  FECHA_BAJA         DATE,
  MOTIVO_BAJA        VARCHAR2(1 BYTE),
  IP_BAJA            VARCHAR2(31 BYTE)
)
LOGGING 
NOCOMPRESS 
NOCACHE
NOPARALLEL
NOMONITORING;


CREATE INDEX CLI_I1 ON CLIENTES
(ID_CLIENTE)
LOGGING
NOPARALLEL;


CREATE UNIQUE INDEX CL_I1 ON ENVIOS_LISTAS
(ID_ENVIO, ID_LISTA)
LOGGING
NOPARALLEL;


CREATE UNIQUE INDEX EN_I1 ON ENVIADOS
(ID_EMAIL, ID_ENVIO)
LOGGING
NOPARALLEL;


CREATE INDEX EN_I2 ON ENVIADOS
(ID_ENVIO)
LOGGING
NOPARALLEL;


CREATE UNIQUE INDEX ENV_I1 ON ENVIOS
(ID_ENVIO)
LOGGING
NOPARALLEL;


CREATE UNIQUE INDEX LIS_I1 ON LISTAS
(ID_LISTA)
LOGGING
NOPARALLEL;


CREATE INDEX TRA_I1 ON TRACKING
(ID_EMAIL)
LOGGING
NOPARALLEL;


CREATE INDEX TRA_I2 ON TRACKING
(ID_ENVIO)
LOGGING
NOPARALLEL;


CREATE UNIQUE INDEX USU_I1 ON USUARIOS
(ID_EMAIL, ID_LISTA)
LOGGING
NOPARALLEL;


CREATE INDEX USU_I2 ON USUARIOS
(EMAIL)
LOGGING
NOPARALLEL;


CREATE OR REPLACE procedure borrar_lista(p_id_lista number, p_id_cliente varchar2) 
	   is  
  v_id_cliente varchar2(40);
  insertados pls_integer := 0;
  borrados pls_integer := 0; 
  n pls_integer := 0;


begin
  if p_id_cliente is null then raise_application_error(-20015,'El cliente no puede ser null') ; end if;
  
  begin
    select 1 into n from listas 
	where id_lista = p_id_lista 
	and id_cliente=p_id_cliente;
  exception when 
     NO_DATA_FOUND 
  then 
     raise_application_error(-20016,'La lista no pertenece al usuario o la lista no existe');
  end;
 
  insert into usuarios_borrados (
	 id_email, id_lista, email, nombre, apellidos, tratamiento, fecha_creacion, fec_ult_actividad, t1, t2, t3, 
	 fecha_baja, motivo_baja) 
	 select id_email, id_lista, email, nombre, apellidos, tratamiento, fecha_creacion, 
   	 fec_ult_actividad, t1, t2, t3, sysdate, 'M'
   	 from usuarios where id_lista = p_id_lista;
  insertados := SQL%ROWCOUNT;
  dbms_output.put_line('Insertados '||insertados||' en usuarios_borrados');	  
  delete from usuarios where id_lista = p_id_lista;
  borrados := SQL%ROWCOUNT;
  dbms_output.put_line('Borrados '||borrados||' de la tabla usuarios');
   
  if(borrados <> insertados) then
	 rollback;
	 raise_application_error(-20017,'Se ha producido un fallo en el borrado; rollback');
  end if;
  
  update listas set estado='B', fecha_baja=sysdate where id_lista=p_id_lista;			
  commit;
exception when others then
  rollback;
  raise_application_error(-20118, 'Error indefinido; rollback');
end;
/

SHOW ERRORS;


CREATE OR REPLACE procedure borrar_usuario_listas(
  p_id_email number, p_id_lista number, p_motivo varchar2, p_ip_baja varchar2 , p_id_cliente varchar2
  ) is

   v_id_cliente varchar2(40);
   n pls_integer := 0;
   l pls_integer := 0;
begin
  begin
  -- Comprobamos si la lista pertenece al cliente
    select id_cliente into v_id_cliente from listas where id_lista = p_id_lista and id_cliente=p_id_cliente;
  end;

    -- Comprobamos si esta en la lista
   begin
	 select 1 into l from usuarios where id_lista = p_id_lista and id_email = p_id_email;
   end;
	-- Si está insertamos en borrados y la eliminamos

   insert into usuarios_borrados (
	id_email, id_lista, email, nombre, apellidos, tratamiento, fecha_creacion,
	fec_ult_actividad, t1, t2, t3, fecha_baja, motivo_baja, ip_baja)
	   select id_email, id_lista, email, nombre, apellidos, tratamiento, fecha_creacion,
	   fec_ult_actividad, t1, t2, t3, sysdate, p_motivo, p_ip_baja
	   from usuarios where id_email = p_id_email and id_lista = p_id_lista;

   delete from usuarios where id_email = p_id_email and id_lista = p_id_lista;

   update listas set num_registros = num_registros-1 where id_lista = p_id_lista;
   commit;
end;
/

SHOW ERRORS;


CREATE OR REPLACE procedure preparar_envio(p_id_cliente varchar2, p_id_envio number) is
  cursor c_usuarios is
    select u.id_email, u.id_lista from usuarios u, envios_listas el, envios e
	  where el.id_envio = p_id_envio
	    and u.id_lista = el.id_lista
		and e.id_envio = el.id_envio
		and e.id_cliente = p_id_cliente
		and estado = 'X';
begin

	update envios set estado = 'X'
	 where id_envio = p_id_envio
	   and id_cliente = p_id_cliente
	   and estado = 'P';

  if sql%rowcount != 1 then
  	 raise_application_error(-20010,'Para preparar el envío debe tener estado P');
  end if;
  
  for c in c_usuarios loop
    begin
	  insert into enviados (id_envio, id_email, id_lista, estado)
	    values (p_id_envio, c.id_email, c.id_lista, 'P');
	exception when dup_val_on_index then null;
	end;
	commit;
  end loop;
  
  update envios set estado = 'L'
	where  id_envio = p_id_envio
	and id_cliente = p_id_cliente
	and estado = 'X';
end;
/

SHOW ERRORS;


CREATE OR REPLACE function borrar_usuario(
  p_id_email number, p_id_lista number, p_motivo varchar2, p_ip_baja varchar2 
  ) return number is
  
  v_id_cliente varchar2(40);
  cursor ccc is select id_lista from listas where id_cliente = v_id_cliente;
  n pls_integer := 0;
  l pls_integer := 0; 
begin
  begin
    select id_cliente into v_id_cliente from listas where id_lista = p_id_lista;
  exception when others then return 0;
  end;
  if v_id_cliente is null then return 0; end if;
  
  -- Bucle de listas
  for c in ccc loop
    -- Comprobamos si esta en la lista
    begin
	  select 1 into l from usuarios where id_lista = c.id_lista and id_email = p_id_email;
	exception when others then l := 0;
    end;
	-- Si está insertamos en borrados y la eliminamos
	if l = 1 then 
	  insert into usuarios_borrados (
		id_email, id_lista, email, nombre, apellidos, tratamiento, fecha_creacion, 
		fec_ult_actividad, t1, t2, t3, fecha_baja, motivo_baja, ip_baja) 
	    select id_email, id_lista, email, nombre, apellidos, tratamiento, fecha_creacion, 
		  fec_ult_actividad, t1, t2, t3, sysdate, p_motivo, p_ip_baja
		  from usuarios where id_email = p_id_email and id_lista = c.id_lista;
	  
	  delete from usuarios where id_email = p_id_email and id_lista = c.id_lista;
	  commit;
	  n := n + 1;
	end if;
  end loop;
  
  return n;
end;
/

SHOW ERRORS;


CREATE OR REPLACE function calcula_id_email(p_email varchar2) return number as
/***
   Calcula un hash a partir de email que se utiliza como identificador de esa dirección.
   El mismo algoritmo está implementado en java en la aplicación, de forma que no se puede 
   modificar en un solo sitio.
   
   El algoritmo consiste en calcular el MD5 del email, coger los 5 primeros bytes y convertirlos a entero.

 ***/
  v_md5 varchar2(32);
  v_md5r dbms_obfuscation_toolkit.raw_checksum;
  v_str varchar2(10);
begin
  v_md5r := dbms_obfuscation_toolkit.md5(input => utl_raw.cast_to_raw(p_email));
  v_md5 := lower(rawtohex(v_md5r));
  v_str := substr(v_md5, 1, 10);
  return to_number(v_str, 'XXXXXXXXXX');
end;
/

SHOW ERRORS;


CREATE OR REPLACE TRIGGER TRACKING_AIR
after insert on tracking for each row
WHEN (
new.tipo = 'B'
      )
declare
  v_id_cliente varchar2(40);
begin
  /** Borrar Usuario
  Cuando se inserta tracking tipo B - Borrado, este trigger
  se encarga de efectuar el borrado del usuario.
  **/
  
  -- Localizamos el id_cliente
  select id_cliente into v_id_cliente from envios where id_envio = :new.id_envio;
  
  insert into usuarios_borrados (
	id_email, id_lista, email, nombre, apellidos, tratamiento, fecha_creacion,
	fec_ult_actividad, t1, t2, t3, fecha_baja, motivo_baja, ip_baja)
	   select id_email, id_lista, email, nombre, apellidos, tratamiento, fecha_creacion,
	   fec_ult_actividad, t1, t2, t3, sysdate, 'U', :new.ip
	   from usuarios where id_email = :new.id_email and id_lista = :new.id_lista;   
  delete from usuarios where id_email = :new.id_email and id_lista = :new.id_lista;  
  update listas set num_registros = num_registros-1 where id_lista = :new.id_lista;
  
exception 
  when others then null;
end;
/
SHOW ERRORS;