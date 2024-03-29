DROP INDEX ASP.EN_I2;

CREATE INDEX ASP.EN_I2 ON ASP.ENVIADOS
(ID_ENVIO)
LOGGING
TABLESPACE ASPIDX
PCTFREE    10
INITRANS   2
MAXTRANS   255
STORAGE    (
            INITIAL          16M
            MINEXTENTS       1
            MAXEXTENTS       UNLIMITED
            PCTINCREASE      0
            FREELISTS        1
            FREELIST GROUPS  1
            BUFFER_POOL      DEFAULT
           )
NOPARALLEL;