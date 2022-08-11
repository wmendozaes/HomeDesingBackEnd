/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bo.com.micrium.modulobase.service;

import bo.com.micrium.modulobase.model.Parametro;
import bo.com.micrium.modulobase.repository.IParametroRepository;
import bo.com.micrium.modulobase.util.ParametroID;
import bo.com.micrium.modulobase.util.ParametroTipo;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Optional;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Log4j2
@Component
@Scope("singleton")
public class ParametroService {

    private static final long serialVersionUID = 1L;

    @Autowired
    private IParametroRepository repository;

    private HashMap<Integer, Parametro> listParametro;

    private SimpleDateFormat sdf = new SimpleDateFormat(ParametroTipo.FORMATO_FECHA);

    public synchronized Parametro getParametro(Integer idParametro) {
        return cargarParametros().get(idParametro);
    }

    public synchronized Object getParamVal(Integer idParametro) {
        Parametro p = cargarParametros().get(idParametro);

        switch (p.getTipo()) {
            case ParametroTipo.TIPO_CADENA:
                return p.getValor();

            case ParametroTipo.TIPO_COLOR:
                return p.getValor();

            case ParametroTipo.TIPO_FECHA:
                try {
                    return sdf.parse(p.getValor());
                } catch (Exception e) {
                    log.error("Erro de parse fecha, " + e.getMessage(), e);
                    return null;
                }
            case ParametroTipo.TIPO_NUMERICO:
                return new BigDecimal(p.getValor());
            case ParametroTipo.TIPO_BOOLEANO:
            	return Boolean.parseBoolean(p.getValor());
            case ParametroTipo.TIPO_LISTADO_VALORES_NUMERICOS:
                return p.getValor();
            case ParametroTipo.TIPO_LISTADO_VALORES_TEXTO:
                return p.getValor();
        }

        return null;
    }

    /**
     * Este metodo debe ser invocado cuando se haga alguna modificacion a un
     * Parametro para que el cambio se manifieste en el resto del sistema. Si en
     * el transcurso del Desarrollo se crean terceras clases que son de tipo
     * singleton estas clases deberan proverer mecanismos para reinicar sus
     * atributos propios para que desde aqui sean invocados y asi el cambio del
     * Parametro sean aplicables en todo contexto.
     *
     * *
     */
    public synchronized void restartParameter() {
        log.info("****** Reiniciarparametros..");
        listParametro = null;

        try {
            Parametro reaload = repository.findById(ParametroID.RELOAD_PARAMETER_ID).get();
            reaload.setValor("true");
            repository.save(reaload);
        } catch (Exception e) {
            log.error("Error al cargar ó guardar el parametro RELOAD con ID=" + ParametroID.RELOAD_PARAMETER_ID, e);
        }
    }

    private synchronized HashMap<Integer, Parametro> cargarParametros() {
        boolean reload = false;
        if (listParametro == null) {
            reload = true;
        } else {
            reload = verifRealoadParameters();
        }

        if (reload) {
            log.info("\n\n ***Recargando los parametros***");
            listParametro = new HashMap<Integer, Parametro>();
            for (Parametro item : repository.findAll()) {
                listParametro.put(item.getId(), item);
                log.info(item);
            }

            log.info("*** Final de cargar parametros ***\n\n ");
            updateParameterReload();

        }
        return listParametro;
    }

    private boolean verifRealoadParameters() {
        Optional<Parametro> p = repository.findById(ParametroID.RELOAD_PARAMETER_ID);

        if (p.isPresent()) {
            return p.get().getValor().equals("1");
        }

        return false;
    }

    private boolean updateParameterReload() {
        Optional<Parametro> p = repository.findById(ParametroID.RELOAD_PARAMETER_ID);

        if (p.isPresent()) {
            p.get().setValor("0");
            repository.save(p.get());

            log.info("Valor del parametro reaload  actualizado a false");

            return true;
        }

        return false;
    }

}
