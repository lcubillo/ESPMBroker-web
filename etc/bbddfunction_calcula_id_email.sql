CREATE OR REPLACE function ASP.calcula_id_email(p_email varchar2) return number as
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