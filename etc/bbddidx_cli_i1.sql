DROP INDEX ASP.CLI_I1;

CREATE INDEX ASP.CLI_I1 ON ASP.CLIENTES
(ID_CLIENTE)
LOGGING
TABLESPACE ASPIDX
PCTFREE    10
INITRANS   2
MAXTRANS   255
STORAGE    (
            INITIAL          512K
            MINEXTENTS       1
            MAXEXTENTS       UNLIMITED
            PCTINCREASE      0
            FREELISTS        1
            FREELIST GROUPS  1
            BUFFER_POOL      DEFAULT
           )
NOPARALLEL;