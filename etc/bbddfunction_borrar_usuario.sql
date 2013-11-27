CREATE OR REPLACE function ASP.borrar_usuario(
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

