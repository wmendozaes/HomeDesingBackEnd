package bo.com.micrium.modulobase.util;

import java.io.Serializable;

/**
 * Esta Clase tiene como finalidad principal identificar los distintos
 * parametros definidos en nuestro proyecto, los cuales son identificados por el
 * ID correspondiente al ID de la BD de la tabla Parametro.
 * 
 * @author: Cesar Augusto Choque S
 * @version: 20-04-2015
 * 
 * **/
public class ParametroID  implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * Para nuevos Parametros es necesario agregar su respectivo atributo que lo
	 * identifique en el sistema, y que es representado por esta clase, el valor
	 * del paramtro debe ser el ID que lo identifica en la tabla Parametro.
	 * */
	public static final int VALIDACION_ACTIVE_DIRECTORY = 1;
	public static final int INITIAL_CONTEXT_FACTORY = 2;
	public static final int PROVIDER_URL = 3;
	public static final int SECURITY_AUTHENTICACION = 4;
	public static final int SECURITY_PRINCIPAL = 5;
	public static final int SECURITY_CREDENTIALS = 6;
	public static final int SECURITY_USER = 7;
	public static final int DOMINIO = 8;
        
	public static final int CONTRASENA_POR_DEFECTO = 10;
        
    public static final int NUMERO_INTENTOS_AUTENTICACION = 11;
	public static final int TIEMPO_BLOQUEO_AUTENTICACION = 12;
	public static final int EXPRESION_REGULAR_CONTRASENA = 13;
	public static final int EXPRESION_REGULAR_NOMBRE_USUARIO = 14;
	public static final int MENSAJE_VALIDACION_NOMBRE_USUARIO = 15;
	public static final int MENSAJE_VALIDACION_CONTRASENA = 16;
	public static final int EXPRESION_REGULAR_GENERAL = 17;
	public static final int MENSAJE_VALIDACION_GENERAL = 18;
	public static final int ACTIVE_DIRECTORY_GRUPOLDAP=19;
	public static final int BLOQUEO_USUARIOS_DIAS=20;
	

	public static final int RELOAD_PARAMETER_ID = 51;
	
	//otros valores staticos 
	public static final int LDAP =1;
	public static final int LOCAL=2;
	public static final int HIBRIDO=3;
	
	public static final int FORMULARIO_GRUPO=102;
	public static final int TIPOPARAMETRO_LDAP=1;
		
}
