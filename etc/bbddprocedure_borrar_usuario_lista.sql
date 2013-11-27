CREATE OR REPLACE procedure ASP.borrar_usuario_listas(
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
