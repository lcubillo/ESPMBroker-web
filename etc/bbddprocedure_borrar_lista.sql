CREATE OR REPLACE procedure ASP.borrar_lista(p_id_lista number, p_id_cliente varchar2) 
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
