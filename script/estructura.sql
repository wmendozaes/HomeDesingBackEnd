
-- ----------------------------
-- Table structure for MU_ETIQUETA
-- ---------------------------- 
CREATE TABLE MU_ETIQUETA (
ID NUMBER(10) NOT NULL ,
LLAVE VARCHAR2(255) NOT NULL ,
VALOR VARCHAR2(255) NOT NULL ,
GRUPO VARCHAR2(255) NOT NULL,
ESTADO NUMBER(1) NOT NULL 
)
LOGGING
NOCOMPRESS
NOCACHE

;

-- ----------------------------
-- Table structure for MU_ACCION
-- ---------------------------- 
CREATE TABLE MU_ACCION (
ID NUMBER(10) NOT NULL ,
FORMULARIO_ID NUMBER(10) NOT NULL ,
NOMBRE VARCHAR2(100) NOT NULL, 
URL VARCHAR2(255) NULL, 
METODO VARCHAR2(100) NULL
)
LOGGING
NOCOMPRESS
NOCACHE

;

-- ----------------------------
-- Table structure for MU_BITACORA
-- ----------------------------
CREATE TABLE MU_BITACORA (
ID NUMBER(19) NOT NULL ,
ACCION VARCHAR2(4000) NOT NULL  ,
DIRECCION_IP VARCHAR2(50) NOT NULL  ,
FECHA DATE NOT NULL  ,
FORMULARIO VARCHAR2(255) NOT NULL  ,
USUARIO VARCHAR2(100) NULL 
)
LOGGING
NOCOMPRESS
NOCACHE

;

-- ----------------------------
-- Table structure for MU_FORMULARIO
-- ----------------------------
CREATE TABLE MU_FORMULARIO (
ID NUMBER(10) NOT NULL ,
NOMBRE VARCHAR2(255) NOT NULL  ,
ORDEN NUMBER(10) NOT NULL ,
MODULO_ID NUMBER(10) NULL ,
URL VARCHAR2(255) NULL  ,
ICONO VARCHAR2(255) NULL  
)
LOGGING
NOCOMPRESS
NOCACHE

;

-- ----------------------------
-- Table structure for MU_GRUPO_AD
-- ----------------------------
CREATE TABLE MU_GRUPO_AD (
ID NUMBER(10) NOT NULL ,
NOMBRE VARCHAR2(255) NOT NULL  ,
DESCRIPCION VARCHAR2(255) NULL ,
ESTADO NUMBER(1) NOT NULL ,
ROL_ID NUMBER(10) NOT NULL  
)
LOGGING
NOCOMPRESS
NOCACHE

;

-- ----------------------------
-- Table structure for MU_PARAMETRO
-- ----------------------------
CREATE TABLE MU_PARAMETRO (
ID NUMBER(10) NOT NULL ,
NOMBRE VARCHAR2(255) NOT NULL  ,
TIPO NUMBER(10) NOT NULL ,
VALOR VARCHAR2(4000) NULL ,
DESCRIPCION VARCHAR2(500) NULL ,
TIPO_PARAMETRO_ID NUMBER(10) NOT NULL 
)
LOGGING
NOCOMPRESS
NOCACHE

;

-- ----------------------------
-- Table structure for MU_ROL
-- ----------------------------
CREATE TABLE MU_ROL (
ID NUMBER(10) NOT NULL ,
NOMBRE VARCHAR2(50) NOT NULL ,
DESCRIPCION VARCHAR2(200) NULL ,
ESTADO NUMBER(1) NOT NULL 
)
LOGGING
NOCOMPRESS
NOCACHE

;

-- ----------------------------
-- Table structure for MU_ROL_ACCION
-- ----------------------------
CREATE TABLE MU_ROL_ACCION (
ID NUMBER NOT NULL ,
ROL_ID NUMBER NOT NULL ,
ACCION_ID NUMBER NOT NULL 
)
LOGGING
NOCOMPRESS
NOCACHE

;
 
-- ----------------------------
-- Table structure for MU_ROL_TIPO_PARAMETRO_PERMISO
-- ----------------------------
CREATE TABLE MU_ROL_TIPO_PARAMETRO_PERMISO (
ID NUMBER(10) NOT NULL ,
ROL_ID NUMBER(10) NOT NULL ,
TIPO_PARAMETRO_ID NUMBER(10) NOT NULL ,
TIPO_PERMISO NUMBER(10) NOT NULL 
)
LOGGING
NOCOMPRESS
NOCACHE

;
COMMENT ON COLUMN MU_ROL_TIPO_PARAMETRO_PERMISO.TIPO_PARAMETRO_ID IS 'EXISTEN 3 TIPOS DE PERMISOS: VISUALIZAR 1, EDITAR 2, NINGUNA 3';


-- ----------------------------
-- Table structure for MU_TIPO_PARAMETRO
-- ----------------------------
CREATE TABLE MU_TIPO_PARAMETRO (
ID NUMBER(10) NOT NULL ,
NOMBRE VARCHAR2(50) NOT NULL ,
DESCRIPCION VARCHAR2(150) NULL 
)
LOGGING
NOCOMPRESS
NOCACHE

;

-- ----------------------------
-- Table structure for MU_USUARIO
-- ----------------------------
CREATE TABLE MU_USUARIO (
ID NUMBER(10) NOT NULL ,
NOMBRE_USUARIO VARCHAR2(50) NOT NULL  ,
NOMBRE_COMPLETO VARCHAR2(100) NOT NULL  ,
CONTRASENA VARCHAR2(100) NOT NULL  ,
TIPO NUMBER(10) NOT NULL  , 
NUMERO_INTENTOS NUMBER(10) NOT NULL  , 
ULTIMO_INTENTO DATE NULL  , 
ROL_ID NUMBER(10) NOT NULL  ,
ESTADO NUMBER(10) NOT NULL,
VALIDAR_FECHA_LIMITE NUMBER(1) NOT NULL, 
FECHA_LIMITE DATE,
FECHA_ACTUALIZACION DATE
)
LOGGING
NOCOMPRESS
NOCACHE

;

-- ----------------------------
-- Sequence structure for SEQ_MU_BITACORA
-- ----------------------------
CREATE SEQUENCE SEQ_MU_BITACORA
 INCREMENT BY 1
 MINVALUE 1
 MAXVALUE 999999999999999999999999999
 START WITH 188
 NOCACHE ;

  CREATE SEQUENCE SEQ_MU_ETIQUETAS
 INCREMENT BY 1
 MINVALUE 1
 MAXVALUE 999999999999999999999999999
 START WITH 1
 NOCACHE ;

-- ----------------------------
-- Sequence structure for SEQ_MU_GRUPO_AD
-- ----------------------------
CREATE SEQUENCE SEQ_MU_GRUPO_AD
 INCREMENT BY 1
 MINVALUE 1
 MAXVALUE 999999999999999999999999999
 START WITH 100
 NOCACHE ;

-- ----------------------------
-- Sequence structure for SEQ_MU_ROL
-- ----------------------------
CREATE SEQUENCE SEQ_MU_ROL
 INCREMENT BY 1
 MINVALUE 1
 MAXVALUE 999999999999999999999999999
 START WITH 100
 NOCACHE ;

-- ----------------------------
-- Sequence structure for SEQ_MU_ROL_TIPO_PARAMETRO
-- ----------------------------
CREATE SEQUENCE SEQ_MU_ROL_TIPO_PAR
 INCREMENT BY 1
 MINVALUE 1
 MAXVALUE 9999999999999999999999999999
 START WITH 100
 NOCACHE ;

-- ----------------------------
-- Sequence structure for SEQ_MU_USUARIO
-- ----------------------------
CREATE SEQUENCE SEQ_MU_USUARIO
 INCREMENT BY 1
 MINVALUE 1
 MAXVALUE 999999999999999999999999999
 START WITH 100
 NOCACHE ;
 
-- ----------------------------
-- Sequence structure for SEQ_MU_ROL_ACCION
-- ----------------------------
CREATE SEQUENCE SEQ_MU_ROL_ACCION
 INCREMENT BY 1
 MINVALUE 1
 MAXVALUE 999999999999999999999999999
 START WITH 10000
 NOCACHE ;
 
-- ----------------------------
-- Sequence structure for SEQ_MU_FORMULARIO
-- ----------------------------
CREATE SEQUENCE SEQ_MU_FORMULARIO
 INCREMENT BY 1
 MINVALUE 1
 MAXVALUE 999999999999999999999999999
 START WITH 10000
 NOCACHE ;
 
-- ----------------------------
-- Sequence structure for 

-- ----------------------------
CREATE SEQUENCE SEQ_MU_ACCION
 INCREMENT BY 1
 MINVALUE 1
 MAXVALUE 999999999999999999999999999
 START WITH 10000
 NOCACHE ;
 

ALTER TABLE MU_ETIQUETA ADD PRIMARY KEY (ID);

ALTER TABLE MU_ACCION ADD PRIMARY KEY (ID);

ALTER TABLE MU_BITACORA ADD PRIMARY KEY (ID);

ALTER TABLE MU_FORMULARIO ADD PRIMARY KEY (ID);

ALTER TABLE MU_GRUPO_AD ADD PRIMARY KEY (ID);

ALTER TABLE MU_PARAMETRO ADD PRIMARY KEY (ID);

ALTER TABLE MU_ROL ADD PRIMARY KEY (ID);

ALTER TABLE MU_ROL_TIPO_PARAMETRO_PERMISO ADD PRIMARY KEY (ID);

ALTER TABLE MU_TIPO_PARAMETRO ADD PRIMARY KEY (ID);

ALTER TABLE MU_USUARIO ADD PRIMARY KEY (ID);

ALTER TABLE MU_ROL_ACCION ADD PRIMARY KEY (ID);

ALTER TABLE MU_ACCION ADD FOREIGN KEY (FORMULARIO_ID) REFERENCES MU_FORMULARIO (ID);

ALTER TABLE MU_GRUPO_AD ADD FOREIGN KEY (ROL_ID) REFERENCES MU_ROL (ID);

ALTER TABLE MU_PARAMETRO ADD FOREIGN KEY (TIPO_PARAMETRO_ID) REFERENCES MU_TIPO_PARAMETRO (ID) ON DELETE CASCADE;

ALTER TABLE MU_ROL_TIPO_PARAMETRO_PERMISO ADD FOREIGN KEY (ROL_ID) REFERENCES MU_ROL (ID) ON DELETE CASCADE;

ALTER TABLE MU_ROL_TIPO_PARAMETRO_PERMISO ADD FOREIGN KEY (TIPO_PARAMETRO_ID) REFERENCES MU_TIPO_PARAMETRO (ID) ON DELETE CASCADE;

ALTER TABLE MU_USUARIO ADD FOREIGN KEY (ROL_ID) REFERENCES MU_ROL (ID);

ALTER TABLE MU_ROL_ACCION ADD FOREIGN KEY (ACCION_ID) REFERENCES MU_ACCION (ID) ON DELETE CASCADE;

ALTER TABLE MU_ROL_ACCION ADD FOREIGN KEY (ROL_ID) REFERENCES MU_ROL (ID) ON DELETE CASCADE;