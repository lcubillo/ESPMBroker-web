DROP INDEX ASP.ENV_I1;

CREATE UNIQUE INDEX ASP.ENV_I1 ON ASP.ENVIOS
(ID_ENVIO)
LOGGING
TABLESPACE ASPIDX
PCTFREE    10
INITRANS   2
MAXTRANS   255
STORAGE    (
            INITIAL          64K
            MINEXTENTS       1
            MAXEXTENTS       UNLIMITED
            PCTINCREASE      0
            FREELISTS        1
            FREELIST GROUPS  1
            BUFFER_POOL      DEFAULT
           )
NOPARALLEL;