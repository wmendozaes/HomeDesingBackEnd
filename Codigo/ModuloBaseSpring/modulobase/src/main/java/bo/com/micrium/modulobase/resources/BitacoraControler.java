/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bo.com.micrium.modulobase.resources;

import bo.com.micrium.modulobase.resources.dto.BitacoraResponse;
import bo.com.micrium.modulobase.security.jwt.JwtTokenUtil;
import bo.com.micrium.modulobase.util.ConvercionUtil;
import java.util.Calendar;
import java.util.Date;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@Log4j2
@RestController
@CrossOrigin
@RequestMapping(value = "/bitacoras", produces = {MediaType.APPLICATION_JSON_VALUE})
public class BitacoraControler extends GenericControler {

    @GetMapping
    Page<BitacoraResponse> list(
            @RequestHeader(value = JwtTokenUtil.KEY_TOKEN) String token,
            @RequestHeader(value = JwtTokenUtil.IP_CLIENT) String ipClient,
            @RequestHeader(value = JwtTokenUtil.FORM) String form,
            @Param(value = "fechaIniStr") String fechaIniStr,
            @Param(value = "fechaFinStr") String fechaFinStr,
            @Param(value = "fecha") String fecha,
            @Param(value = "accion") String accion,
            @Param(value = "direccionIp") String direccionIp,
            @Param(value = "formulario") String formulario,
            @Param(value = "usuario") String usuario,
            Pageable pageRequest) {
        try {
            if (ipClient == null || ipClient.isEmpty() || ipClient.equals("127.0.0.1") || ipClient.equals("localhost")) {
                ipClient = httpServletRequest.getRemoteAddr();
            }

            StringBuilder sb = new StringBuilder();
            sb.append("-------------------REQUEST----------------------\n").
                    append("token ").append(token).append(", \n").
                    append("form ").append(form).append(", \n").
                    append("ipClient ").append(ipClient).append(", \n").
                    append("url ").append(httpServletRequest.getRequestURL()).append(", \n").
                    append("fechaIniStr ").append(fechaIniStr).append(", \n").
                    append("fechaFinStr ").append(fechaFinStr).append(", \n").
                    append("fecha ").append(fecha).append(", \n").
                    append("accion ").append(accion).append(", \n").
                    append("direccionIp ").append(direccionIp).append(", \n").
                    append("formulario ").append(formulario).append(", \n").
                    append("usuario ").append(usuario).append(", \n").
                    append("fecha ").append(fecha).append(", \n").
                    append("fecha ").append(fecha).append(", \n").
                    append("------------------------------------------------\n");

            log.info(sb.toString());

            int fechaIniBool = (fechaIniStr == null || fechaIniStr.trim().isEmpty()) ? -1 : 0;
            Date fechaIni = (fechaIniBool == -1) ? new Date() : ConvercionUtil.FORMAT_FECHA.parse(fechaIniStr);
            {
                Calendar cal = Calendar.getInstance();
                cal.setTime(fechaIni);
                cal.set(Calendar.HOUR, 0);
                cal.set(Calendar.MINUTE, 0);
                cal.set(Calendar.SECOND, 0);
                fechaIni = cal.getTime();
            }
            int fechaFinBool = (fechaFinStr == null || fechaFinStr.trim().isEmpty()) ? -1 : 0;
            Date fechaFin = (fechaFinBool == -1) ? new Date() : ConvercionUtil.FORMAT_FECHA.parse(fechaFinStr);
            {
                Calendar cal = Calendar.getInstance();
                cal.setTime(fechaFin);
                cal.set(Calendar.HOUR_OF_DAY, 23);
                cal.set(Calendar.MINUTE, 59);
                cal.set(Calendar.SECOND, 59);
                fechaFin = cal.getTime();
            }
            int fechaBool = (fecha == null || fecha.trim().isEmpty()) ? -1 : 0;
            int accionBool = (accion == null || accion.trim().isEmpty()) ? -1 : 0;
            int direccionIpBool = (direccionIp == null || direccionIp.trim().isEmpty()) ? -1 : 0;
            int formularioBool = (formulario == null || formulario.trim().isEmpty()) ? -1 : 0;
            int usuarioBool = (usuario == null || usuario.trim().isEmpty()) ? -1 : 0;

            Page<BitacoraResponse> out = bitacoraRespository.filter(fechaIniBool, fechaIni, fechaFinBool, fechaFin,
                    fechaBool, (fechaBool == -1) ? "" : ("%" + fecha + "%").toUpperCase(),
                    accionBool, (accionBool == -1) ? "" : ("%" + accion + "%").toUpperCase(),
                    direccionIpBool, (direccionIpBool == -1) ? "" : ("%" + direccionIp + "%").toUpperCase(),
                    formularioBool, (formularioBool == -1) ? "" : ("%" + formulario + "%").toUpperCase(),
                    usuarioBool, (usuarioBool == -1) ? "" : ("%" + usuario + "%").toUpperCase(),
                    pageRequest).map(b -> ConvercionUtil.convertir(b));

            sb = new StringBuilder();
            sb.append("-------------------RESPONSE----------------------\n").
                    append("token ").append(token).append(", \n").
                    append("DATA ").append(out).append(", \n").
                    append("CONTENT ").append(out.getContent()).append(", \n").
                    append("------------------------------------------------\n");

            log.info(sb.toString());
            
            return out;
        } catch (Exception ex) {
            log.error("error al paginar bitacoras, " + ex.getMessage(), ex);
            Page<BitacoraResponse> out = bitacoraRespository.findByIdAndAccion(-1, "-1", pageRequest).map(b -> ConvercionUtil.convertir(b));
            StringBuilder sb = new StringBuilder();
            sb.append("-------------------RESPONSE----------------------\n").
                    append("token ").append(token).append(", \n").
                    append("DATA ").append(out).append(", \n").
                    append("CONTENT ").append(out.getContent()).append(", \n").
                    append("------------------------------------------------\n");

            log.info(sb.toString());
            
            return out;
        }
    }

}
