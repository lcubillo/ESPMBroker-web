CREATE OR REPLACE procedure ASP.preparar_envio(p_id_cliente varchar2, p_id_envio number) is
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
