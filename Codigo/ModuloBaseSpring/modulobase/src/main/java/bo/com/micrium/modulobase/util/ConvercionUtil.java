/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bo.com.micrium.modulobase.util;

import bo.com.micrium.modulobase.model.*;
import bo.com.micrium.modulobase.resources.dto.*;

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

    public  static  PaisResponse convertir(Pais model) {
        return  new PaisResponse(model.getId(), model.getNombre(), model.getDescripcion());
    }

    public static DepartamentoResponse convertir(Departamento model) {
        return new DepartamentoResponse(model.getId(), model.getNombre(), model.getDescripcion(), model.getPaisID().getId(), model.getPaisID().getNombre());
    }

    public static ProvinciaResponse convertir(Provincia model) {
        return new ProvinciaResponse(model.getId(), model.getNombre(), model.getDescripcion(), model.getDepartamentoID().getId(), model.getDepartamentoID().getNombre());
    }

    public static LocalidadResponse convertir(Localidad model) {
        return  new LocalidadResponse(model.getId(), model.getNombre(), model.getDescripcion(), model.getProvinciaId().getId(), model.getProvinciaId().getNombre());
    }

    public static TrabajoTipoResponse convertir(TrabajoTipo model) {
        return new TrabajoTipoResponse(model.getId(), model.getNombre(), model.getDescripcion());
    }

    public static ProyectoTipoResponse convertir(ProyectoTipo model) {
        return new ProyectoTipoResponse(model.getId(), model.getNombre(), model.getDescripcion());
    }

    public static ContratoTipoResponse convertir(ContratoTipo model) {
        return  new ContratoTipoResponse(model.getId(), model.getNombre(), model.getDescripcion());
    }

    public  static DocumentoTipoResponse convertir(DocumentoTipo model) {
        return  new DocumentoTipoResponse(model.getId(), model.getNombre(), model.getDescripcion());
    }

    public static VehiculoTipoResponse convertir(VehiculoTipo model) {
        return new VehiculoTipoResponse(model.getId(), model.getNombre(), model.getDescripcion());
    }

    public static HerramientaTipoResponse convertir(HerramientaTipo model) {
        return new HerramientaTipoResponse(model.getId(), model.getNombre(), model.getDescripcion());
    }

    public static MaterialTipoResponse convertir(MaterialTipo model) {
        return new MaterialTipoResponse(model.getId(), model.getNombre(), model.getDescripcion());
    }

}
