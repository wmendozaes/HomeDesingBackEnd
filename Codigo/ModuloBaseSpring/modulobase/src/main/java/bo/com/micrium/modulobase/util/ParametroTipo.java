/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bo.com.micrium.modulobase.util;


public class ParametroTipo {
    
    public static final String FORMATO_FECHA = "dd/MM/yyyy HH:mm:ss";

    public static final int TIPO_CADENA = 1;
    public static final int TIPO_FECHA = 2;
    public static final int TIPO_NUMERICO = 3;
    public static final int TIPO_BOOLEANO = 4;
    public static final int TIPO_COLOR = 5;
    public static final int TIPO_LISTADO_VALORES_TEXTO = 6;
    public static final int TIPO_LISTADO_VALORES_NUMERICOS = 7;

    public static boolean esValido(int valor) {
        return (valor == TIPO_CADENA || valor == TIPO_FECHA || valor == TIPO_NUMERICO || valor == TIPO_BOOLEANO || valor == TIPO_COLOR || valor == TIPO_LISTADO_VALORES_TEXTO || valor == TIPO_LISTADO_VALORES_NUMERICOS);
    }
}
