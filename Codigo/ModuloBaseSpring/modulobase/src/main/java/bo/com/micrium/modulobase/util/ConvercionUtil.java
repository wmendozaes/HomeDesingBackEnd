/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bo.com.micrium.modulobase.util;

import bo.com.micrium.modulobase.model.Bitacora;
import bo.com.micrium.modulobase.model.Etiqueta;
import bo.com.micrium.modulobase.model.Grupo;
import bo.com.micrium.modulobase.model.Parametro;
import bo.com.micrium.modulobase.model.Rol;
import bo.com.micrium.modulobase.model.TipoParametro;
import bo.com.micrium.modulobase.model.Usuario;
import bo.com.micrium.modulobase.resources.dto.BitacoraResponse;
import bo.com.micrium.modulobase.resources.dto.EtiquetaResponse;
import bo.com.micrium.modulobase.resources.dto.GrupoResponse;
import bo.com.micrium.modulobase.resources.dto.ParametroResponse;
import bo.com.micrium.modulobase.resources.dto.RolResponse;
import bo.com.micrium.modulobase.resources.dto.TipoParametroResponse;
import bo.com.micrium.modulobase.resources.dto.UsuarioResponse;
import java.io.Serializable;
import java.text.SimpleDateFormat;

/**
 *
 * @author alepaco.maton
 */
public class ConvercionUtil implements Serializable {

    public static SimpleDateFormat FORMAT_FECHA = new SimpleDateFormat(ParametroTipo.FORMATO_FECHA);

    public static UsuarioResponse convertir(Usuario model) {
        return new UsuarioResponse(model.getId(), model.getNombreUsuario(), model.getNombreCompleto(), model.getTipo(), model.getRolId().getId(), model.getRolId().getNombre(),
        		model.getEstado());
    }

    public static RolResponse convertir(Rol model) {
        return new RolResponse(model.getId(), model.getNombre(), model.getDescripcion());
    }

    public static GrupoResponse convertir(Grupo model) {
        return new GrupoResponse(model.getId(), model.getNombre(), model.getDescripcion(), model.getRolId().getId(), model.getRolId().getNombre());
    }

    public static BitacoraResponse convertir(Bitacora model) {
        return new BitacoraResponse(model.getId(), model.getAccion(), model.getDireccionIp(), FORMAT_FECHA.format(model.getFecha()),
                model.getFormulario(), model.getUsuario());
    }

    public static ParametroResponse convertir(Parametro model, TipoParametro tipoParametro, boolean editable) {
        return new ParametroResponse(model.getId(), model.getNombre(), model.getTipo(),
                model.getValor(), model.getDescripcion(), tipoParametro.getId(), tipoParametro.getNombre(), editable);
    }

    public static TipoParametroResponse convertir(TipoParametro model) {
        return new TipoParametroResponse(model.getId(), model.getNombre(), model.getDescripcion());
    }

    public static EtiquetaResponse convertir(Etiqueta model) {
        return new EtiquetaResponse(model.getId(), model.getLlave(), model.getValor(), model.getGrupo());
    }

}
