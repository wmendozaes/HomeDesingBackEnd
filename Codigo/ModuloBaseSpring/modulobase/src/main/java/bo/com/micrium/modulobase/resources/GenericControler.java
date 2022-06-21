/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bo.com.micrium.modulobase.resources;

import bo.com.micrium.modulobase.model.Bitacora;
import bo.com.micrium.modulobase.repository.IBitacoraRespository;
import bo.com.micrium.modulobase.security.jwt.JwtTokenUtil;
import bo.com.micrium.modulobase.validator.ApiException;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Calendar;
import javax.servlet.http.HttpServletRequest;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

/**
 *
 * @author alepaco.maton
 */
@Log4j2
public class GenericControler implements Serializable {

    @Autowired
    protected IBitacoraRespository bitacoraRespository;

    @Autowired
    protected JwtTokenUtil jwtTokenUtil;

    @Autowired
    protected HttpServletRequest httpServletRequest;

    protected void guardarBitacora(String token, String direccionIp, String form, String accion) {
        bitacoraRespository.save(new Bitacora(null, accion, direccionIp,
                new Timestamp(Calendar.getInstance().getTimeInMillis()), form, jwtTokenUtil.getUsernameFromToken(token)));
    }
    
    protected void guardarBitacoraSinToken(String userName, String direccionIp, String form, String accion) {
        bitacoraRespository.save(new Bitacora(null, accion, direccionIp,
                new Timestamp(Calendar.getInstance().getTimeInMillis()), form, userName));
    }

    @PostMapping("/bitacoras/eventual")
    private ResponseEntity<?> guardarBitacoraEventual(@RequestHeader(value = JwtTokenUtil.KEY_TOKEN) String token,
            @RequestHeader(value = JwtTokenUtil.IP_CLIENT) String ipClient,
            @RequestHeader(value = JwtTokenUtil.FORM) String form, @RequestBody String request) throws ApiException {
        guardarBitacora(token, ipClient, form, request);
        return ResponseEntity.ok().build();
    }

    protected String obtenerToken() {
        return jwtTokenUtil.getRolNombreFromToken(this.httpServletRequest.getHeader(JwtTokenUtil.KEY_TOKEN));
    }

}
