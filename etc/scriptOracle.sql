/* Expresamos las cantidades en bytes */CREATE LOGFILE GROUP REDOLOGADD UNDOFILE 'redolog.dat'INITIAL_SIZE = 32768 UNDO_BUFFER_SIZE = 22528ENGINE NDBCREATE LOGFILE GROUP IDXADD UNDOFILE 'idx.dat'INITIAL_SIZE 1240UNDO_BUFFER_SIZE = 1024ENGINE NDBCREATE TABLESPACE ASPDATOSADD DATAFILE 'aspdatos.dbf'USE LOGFILE GROUP redologENGINE NDBCREATE TABLESPACE ASPIDXADD DATAFILE 'aspidx.dbf'USE LOGFILE GROUP IDXENGINE NDBCREATE TABLE CLIENTES(  ID_CLIENTE      VARCHAR2(12) NOT NULL,  NOMBRE          VARCHAR2(40),  PASSWORD        VARCHAR2(16),  FECHA_CREACION  DATE,  PIE_HTML        CLOB,  PIE_TEXTO       CLOB,  REMITENTE       VARCHAR2(60),  URL_BAJA        VARCHAR2(150))TABLESPACE ASPDATOSPCTUSED    40PCTFREE    10INITRANS   1MAXTRANS   255STORAGE    (            INITIAL          64K            MINEXTENTS       1            MAXEXTENTS       2147483645            PCTINCREASE      0            FREELISTS        1            FREELIST GROUPS  1            BUFFER_POOL      DEFAULT           )LOGGING  LOB (PIE_HTML) STORE AS      ( TABLESPACE  ASPDATOS        ENABLE      STORAGE IN ROW        CHUNK       8192        PCTVERSION  10        NOCACHE        STORAGE    (                    INITIAL          64K                    MINEXTENTS       1                    MAXEXTENTS       2147483645                    PCTINCREASE      0                    FREELISTS        1                    FREELIST GROUPS  1                    BUFFER_POOL      DEFAULT                   )      )  LOB (PIE_TEXTO) STORE AS      ( TABLESPACE  ASPDATOS        ENABLE      STORAGE IN ROW        CHUNK       8192        PCTVERSION  10        NOCACHE        STORAGE    (                    INITIAL          64K                    MINEXTENTS       1                    MAXEXTENTS       2147483645                    PCTINCREASE      0                    FREELISTS        1                    FREELIST GROUPS  1                    BUFFER_POOL      DEFAULT                   )      )NOCACHENOPARALLEL;CREATE INDEX CLI_I1 ON CLIENTES(ID_CLIENTE)LOGGINGTABLESPACE ASPIDXPCTFREE    10INITRANS   2MAXTRANS   255STORAGE    (            INITIAL          512K            MINEXTENTS       1            MAXEXTENTS       2147483645            PCTINCREASE      0            FREELISTS        1            FREELIST GROUPS  1            BUFFER_POOL      DEFAULT           )NOPARALLEL;CREATE TABLE ENVIOS(  ID_CLIENTE    VARCHAR2(12)                    NOT NULL,  ID_ENVIO      NUMBER(6)                       NOT NULL,  ETIQUETA      VARCHAR2(20),  DESCRIPCION   VARCHAR2(80),  ESTADO        VARCHAR2(1)                     NOT NULL,  FECHA_ENVIO   DATE,  HTML          CLOB,  TEXTO         CLOB,  REMITENTE     VARCHAR2(60),  ASUNTO        VARCHAR2(100),  REPLYTO       VARCHAR2(60),  INICIO_ENVIO  DATE,  MAQUINA       VARCHAR2(20))TABLESPACE ASPDATOSPCTUSED    40PCTFREE    10INITRANS   1MAXTRANS   255STORAGE    (            INITIAL          64K            MINEXTENTS       1            MAXEXTENTS       2147483645            PCTINCREASE      0            FREELISTS        1            FREELIST GROUPS  1            BUFFER_POOL      DEFAULT           )LOGGING  LOB (HTML) STORE AS      ( TABLESPACE  ASPDATOS        ENABLE      STORAGE IN ROW        CHUNK       8192        PCTVERSION  10        NOCACHE        STORAGE    (                    INITIAL          64K                    MINEXTENTS       1                    MAXEXTENTS       2147483645                    PCTINCREASE      0                    FREELISTS        1                    FREELIST GROUPS  1                    BUFFER_POOL      DEFAULT                   )      )  LOB (TEXTO) STORE AS      ( TABLESPACE  ASPDATOS        ENABLE      STORAGE IN ROW        CHUNK       8192        PCTVERSION  10        NOCACHE        STORAGE    (                    INITIAL          64K                    MINEXTENTS       1                    MAXEXTENTS       2147483645                    PCTINCREASE      0                    FREELISTS        1                    FREELIST GROUPS  1                    BUFFER_POOL      DEFAULT                   )      )NOCACHENOPARALLEL;CREATE UNIQUE INDEX ENV_I1 ON ENVIOS(ID_ENVIO)LOGGINGTABLESPACE ASPIDXPCTFREE    10INITRANS   2MAXTRANS   255STORAGE    (            INITIAL          64K            MINEXTENTS       1            MAXEXTENTS       2147483645            PCTINCREASE      0            FREELISTS        1            FREELIST GROUPS  1            BUFFER_POOL      DEFAULT           )NOPARALLEL; CREATE TABLE LISTAS(  ID_CLIENTE      VARCHAR2(12)                  NOT NULL,  ID_LISTA        NUMBER(6)                     NOT NULL,  NOMBRE_LISTA    VARCHAR2(40)                  NOT NULL,  NUM_REGISTROS   NUMBER(8),  ESTADO          VARCHAR2(1),  FECHA_CREACION  DATE,  FECHA_BAJA      DATE,  ETIQUETA_T1     VARCHAR2(12),  ETIQUETA_T2     VARCHAR2(12),  ETIQUETA_T3     VARCHAR2(12))TABLESPACE ASPDATOSPCTUSED    40PCTFREE    10INITRANS   1MAXTRANS   255STORAGE    (            INITIAL          512K            MINEXTENTS       1            MAXEXTENTS       2147483645            PCTINCREASE      0            FREELISTS        1            FREELIST GROUPS  1            BUFFER_POOL      DEFAULT           )LOGGINGNOCACHENOPARALLEL;CREATE UNIQUE INDEX LIS_I1 ON LISTAS(ID_LISTA)LOGGINGTABLESPACE ASPIDXPCTFREE    10INITRANS   2MAXTRANS   255STORAGE    (            INITIAL          512K            MINEXTENTS       1            MAXEXTENTS       2147483645            PCTINCREASE      0            FREELISTS        1            FREELIST GROUPS  1            BUFFER_POOL      DEFAULT           )NOPARALLEL;CREATE TABLE ENVIOS_LISTAS(  ID_ENVIO  NUMBER(6)                           NOT NULL,  ID_LISTA  NUMBER(6)                           NOT NULL)TABLESPACE ASPDATOSPCTUSED    40PCTFREE    10INITRANS   1MAXTRANS   255STORAGE    (            INITIAL          512K            MINEXTENTS       1            MAXEXTENTS       2147483645            PCTINCREASE      0            FREELISTS        1            FREELIST GROUPS  1            BUFFER_POOL      DEFAULT           )LOGGINGNOCACHENOPARALLEL;CREATE UNIQUE INDEX CL_I1 ON ENVIOS_LISTAS(ID_ENVIO, ID_LISTA)LOGGINGTABLESPACE ASPIDXPCTFREE    10INITRANS   2MAXTRANS   255STORAGE    (            INITIAL          512K            MINEXTENTS       1            MAXEXTENTS       2147483645            PCTINCREASE      0            FREELISTS        1            FREELIST GROUPS  1            BUFFER_POOL      DEFAULT           )NOPARALLEL; CREATE TABLE USUARIOS(  ID_EMAIL           NUMBER(13)                 NOT NULL,  ID_LISTA           NUMBER(6)                  NOT NULL,  EMAIL              VARCHAR2(80)               NOT NULL,  NOMBRE             VARCHAR2(30),  APELLIDOS          VARCHAR2(40),  TRATAMIENTO        VARCHAR2(6),  FECHA_CREACION     DATE,  FEC_ULT_ACTIVIDAD  DATE,  T1                 VARCHAR2(20),  T2                 VARCHAR2(20),  T3                 VARCHAR2(20))TABLESPACE ASPDATOSPCTUSED    40PCTFREE    10INITRANS   1MAXTRANS   255STORAGE    (            INITIAL          16M            MINEXTENTS       1            MAXEXTENTS       2147483645            PCTINCREASE      0            FREELISTS        1            FREELIST GROUPS  1            BUFFER_POOL      DEFAULT           )LOGGINGNOCACHENOPARALLEL;CREATE UNIQUE INDEX USU_I1 ON USUARIOS(ID_EMAIL, ID_LISTA)LOGGINGTABLESPACE ASPIDXPCTFREE    10INITRANS   2MAXTRANS   255STORAGE    (            INITIAL          16M            MINEXTENTS       1            MAXEXTENTS       2147483645            PCTINCREASE      0            FREELISTS        1            FREELIST GROUPS  1            BUFFER_POOL      DEFAULT           )NOPARALLEL;CREATE INDEX USU_I2 ON USUARIOS(EMAIL)LOGGINGTABLESPACE ASPIDXPCTFREE    10INITRANS   2MAXTRANS   255STORAGE    (            INITIAL          512K            MINEXTENTS       1            MAXEXTENTS       2147483645            PCTINCREASE      0            FREELISTS        1            FREELIST GROUPS  1            BUFFER_POOL      DEFAULT           )NOPARALLEL;CREATE TABLE USUARIOS_BORRADOS(  ID_EMAIL           NUMBER(13)                 NOT NULL,  ID_LISTA           NUMBER(6)                  NOT NULL,  EMAIL              VARCHAR2(80)               NOT NULL,  NOMBRE             VARCHAR2(30),  APELLIDOS          VARCHAR2(40),  TRATAMIENTO        VARCHAR2(6),  FECHA_CREACION     DATE,  FEC_ULT_ACTIVIDAD  DATE,  T1                 VARCHAR2(20),  T2                 VARCHAR2(20),  T3                 VARCHAR2(20),  FECHA_BAJA         DATE,  MOTIVO_BAJA        VARCHAR2(1),  IP_BAJA            VARCHAR2(31))TABLESPACE ASPDATOSPCTUSED    40PCTFREE    10INITRANS   1MAXTRANS   255STORAGE    (            INITIAL          16M            MINEXTENTS       1            MAXEXTENTS       2147483645            PCTINCREASE      0            FREELISTS        1            FREELIST GROUPS  1            BUFFER_POOL      DEFAULT           )LOGGINGNOCACHENOPARALLEL;CREATE TABLE ENVIADOS(  ID_ENVIO   NUMBER(6)                          NOT NULL,  ID_LISTA   NUMBER(6)                          NOT NULL,  ID_EMAIL   NUMBER(13)                         NOT NULL,  FEC_ENVIO  DATE,  LECTURAS   NUMBER(3),  CLICKS     NUMBER(3),  ESTADO     VARCHAR2(1))TABLESPACE ASPDATOSPCTUSED    40PCTFREE    10INITRANS   1MAXTRANS   255STORAGE    (            INITIAL          16M            MINEXTENTS       1            MAXEXTENTS       2147483645            PCTINCREASE      0            FREELISTS        1            FREELIST GROUPS  1            BUFFER_POOL      DEFAULT           )LOGGINGNOCACHENOPARALLEL;CREATE UNIQUE INDEX EN_I1 ON ENVIADOS(ID_EMAIL, ID_ENVIO)LOGGINGTABLESPACE ASPIDXPCTFREE    10INITRANS   2MAXTRANS   255STORAGE    (            INITIAL          16M            MINEXTENTS       1            MAXEXTENTS       2147483645            PCTINCREASE      0            FREELISTS        1            FREELIST GROUPS  1            BUFFER_POOL      DEFAULT           )NOPARALLEL;CREATE INDEX EN_I2 ON ENVIADOS(ID_ENVIO)LOGGINGTABLESPACE ASPIDXPCTFREE    10INITRANS   2MAXTRANS   255STORAGE    (            INITIAL          16M            MINEXTENTS       1            MAXEXTENTS       2147483645            PCTINCREASE      0            FREELISTS        1            FREELIST GROUPS  1            BUFFER_POOL      DEFAULT           )NOPARALLEL;CREATE TABLE ENVIOS_LISTAS(  ID_ENVIO  NUMBER(6)                           NOT NULL,  ID_LISTA  NUMBER(6)                           NOT NULL)TABLESPACE ASPDATOSPCTUSED    40PCTFREE    10INITRANS   1MAXTRANS   255STORAGE    (            INITIAL          512K            MINEXTENTS       1            MAXEXTENTS       2147483645            PCTINCREASE      0            FREELISTS        1            FREELIST GROUPS  1            BUFFER_POOL      DEFAULT           )LOGGINGNOCACHENOPARALLEL;CREATE UNIQUE INDEX CL_I1 ON ENVIOS_LISTAS(ID_ENVIO, ID_LISTA)LOGGINGTABLESPACE ASPIDXPCTFREE    10INITRANS   2MAXTRANS   255STORAGE    (            INITIAL          512K            MINEXTENTS       1            MAXEXTENTS       2147483645            PCTINCREASE      0            FREELISTS        1            FREELIST GROUPS  1            BUFFER_POOL      DEFAULT           )NOPARALLEL;CREATE TABLE TRACKING(  ID_ENVIO  NUMBER(6)                           NOT NULL,  ID_LISTA  NUMBER(6)                           NOT NULL,  ID_EMAIL  NUMBER(13)                          NOT NULL,  FECHA     DATE,  TIPO      VARCHAR2(1),  IP        VARCHAR2(31),  URL       VARCHAR2(100),  NUM       NUMBER(2))TABLESPACE ASPDATOSPCTUSED    40PCTFREE    10INITRANS   1MAXTRANS   255STORAGE    (            INITIAL          16M            MINEXTENTS       1            MAXEXTENTS       2147483645            PCTINCREASE      0            FREELISTS        1            FREELIST GROUPS  1            BUFFER_POOL      DEFAULT           )LOGGINGNOCACHENOPARALLEL;CREATE INDEX TRA_I1 ON TRACKING(ID_EMAIL)LOGGINGTABLESPACE ASPIDXPCTFREE    10INITRANS   2MAXTRANS   255STORAGE    (            INITIAL          16M            MINEXTENTS       1            MAXEXTENTS       2147483645            PCTINCREASE      0            FREELISTS        1            FREELIST GROUPS  1            BUFFER_POOL      DEFAULT           )NOPARALLEL;CREATE INDEX TRA_I2 ON TRACKING(ID_ENVIO)LOGGINGTABLESPACE ASPIDXPCTFREE    10INITRANS   2MAXTRANS   255STORAGE    (            INITIAL          16M            MINEXTENTS       1            MAXEXTENTS       2147483645            PCTINCREASE      0            FREELISTS        1            FREELIST GROUPS  1            BUFFER_POOL      DEFAULT           )NOPARALLEL;CREATE OR REPLACE TRIGGER TRACKING_AIRAFTER INSERT ON TRACKING FOR EACH ROWWHEN (new.tipo = 'B'      )declare  v_id_cliente varchar2(40);begin  /** Borrar Usuario  Cuando se inserta tracking tipo B - Borrado, este trigger  se encarga de efectuar el borrado del usuario.  **/  -- Localizamos el id_cliente  select id_cliente into v_id_cliente from envios where id_envio = :new.id_envio;  insert into usuarios_borrados (	id_email, id_lista, email, nombre, apellidos, tratamiento, fecha_creacion,	fec_ult_actividad, t1, t2, t3, fecha_baja, motivo_baja, ip_baja)	   select id_email, id_lista, email, nombre, apellidos, tratamiento, fecha_creacion,	   fec_ult_actividad, t1, t2, t3, sysdate, 'U', :new.ip	   from usuarios where id_email = :new.id_email and id_lista = :new.id_lista;  delete from usuarios where id_email = :new.id_email and id_lista = :new.id_lista;  update listas set num_registros = num_registros-1 where id_lista = :new.id_lista;exception  when others then null;end;/SHOW ERRORS;CREATE TABLE REBOTES(  ID_ENVIO  NUMBER(6)                           NOT NULL,  ID_LISTA  NUMBER(6)                           NOT NULL,  ID_EMAIL  NUMBER(13)                          NOT NULL,  FECHA     DATE)TABLESPACE ASPDATOSPCTUSED    40PCTFREE    10INITRANS   1MAXTRANS   255STORAGE    (            INITIAL          512K            MINEXTENTS       1            MAXEXTENTS       2147483645            PCTINCREASE      0            FREELISTS        1            FREELIST GROUPS  1            BUFFER_POOL      DEFAULT           )LOGGINGNOCACHENOPARALLEL;CREATE TABLE FBL(  ID_ENVIO  NUMBER(6) NOT NULL,  ID_LISTA  NUMBER(6) NOT NULL,  ID_EMAIL  NUMBER(13) NOT NULL,  FECHA     DATE,  TIPO      VARCHAR2(1))TABLESPACE ASPDATOSPCTUSED    40PCTFREE    10INITRANS   1MAXTRANS   255STORAGE    (            INITIAL          512K            MINEXTENTS       1            MAXEXTENTS       2147483645            PCTINCREASE      0            FREELISTS        1            FREELIST GROUPS  1            BUFFER_POOL      DEFAULT           )LOGGINGNOCACHENOPARALLEL;