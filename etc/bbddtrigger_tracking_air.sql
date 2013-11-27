DROP TRIGGER ASP.TRACKING_AIR;

CREATE OR REPLACE TRIGGER ASP.TRACKING_AIR
after insert ON ASP.TRACKING for each row
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