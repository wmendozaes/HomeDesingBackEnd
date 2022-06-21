/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bo.com.micrium.modulobase.security.jwt;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Enumeration;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import bo.com.micrium.modulobase.model.Accion;
import bo.com.micrium.modulobase.model.Rol;
import bo.com.micrium.modulobase.model.RolAccion;
import bo.com.micrium.modulobase.repository.IAccionRepository;
import bo.com.micrium.modulobase.repository.IRolAccionRepository;
import bo.com.micrium.modulobase.repository.IRolRepository;
import bo.com.micrium.modulobase.resources.EtiquetaControler;
import bo.com.micrium.modulobase.resources.PerfilControler;
import bo.com.micrium.modulobase.service.ParametroService;
import bo.com.micrium.modulobase.util.ParametroID;
import lombok.extern.log4j.Log4j2;


@Component
@Log4j2
public class JwtRequestFilter extends OncePerRequestFilter implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    @Autowired
    private IRolRepository rolRepository;
    
    @Autowired
    private IRolAccionRepository rolAccionRepository;
    
    @Autowired
    private IAccionRepository accionRepository;
    
    @Autowired
    private JwtUserDetailsService jwtUserDetailsService;
    
    @Autowired
    ParametroService parametroService;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;
    
    private void peticionesOptionsCors(HttpServletRequest request, HttpServletResponse response) {
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Methods", "POST, GET, PUT, OPTIONS, DELETE");
        response.setHeader("Access-Control-Max-Age", "3600");
        Enumeration<String> headersEnum = ((HttpServletRequest) request).getHeaders("Access-Control-Request-Headers");
        StringBuilder headers = new StringBuilder();
        String delim = "";
        while (headersEnum.hasMoreElements()) {
            headers.append(delim).append(headersEnum.nextElement());
            delim = ", ";
        }
        response.setHeader("Access-Control-Allow-Headers", headers.toString());
    }
    
    private void tokenInvalido(HttpServletRequest request, HttpServletResponse response) throws IOException {
        HttpServletResponse httpResp = (HttpServletResponse) response;
        httpResp.setHeader("Access-Control-Allow-Origin", "*");
        httpResp.setHeader("Access-Control-Allow-Methods", "POST, GET, PUT, OPTIONS, DELETE");
        httpResp.setHeader("Access-Control-Max-Age", "3600");
        Enumeration<String> headersEnum = ((HttpServletRequest) request).getHeaders("Access-Control-Request-Headers");
        StringBuilder headers = new StringBuilder();
        String delim = "";
        while (headersEnum.hasMoreElements()) {
            headers.append(delim).append(headersEnum.nextElement());
            delim = ", ";
        }
        httpResp.setHeader("Access-Control-Allow-Headers", headers.toString());
        
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        PrintWriter res = response.getWriter();
        res.append("Unauthorized");
        res.close();
    }
    
    private void sinPermiso(HttpServletRequest request, HttpServletResponse response) throws IOException {
        HttpServletResponse httpResp = (HttpServletResponse) response;
        httpResp.setHeader("Access-Control-Allow-Origin", "*");
        httpResp.setHeader("Access-Control-Allow-Methods", "POST, GET, PUT, OPTIONS, DELETE");
        httpResp.setHeader("Access-Control-Max-Age", "3600");
        Enumeration<String> headersEnum = ((HttpServletRequest) request).getHeaders("Access-Control-Request-Headers");
        StringBuilder headers = new StringBuilder();
        String delim = "";
        while (headersEnum.hasMoreElements()) {
            headers.append(delim).append(headersEnum.nextElement());
            delim = ", ";
        }
        httpResp.setHeader("Access-Control-Allow-Headers", headers.toString());
        
        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        PrintWriter res = response.getWriter();
        res.append("Forbidden");
        res.close();
    }
    
    
    @Override
    public void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        if (request.getMethod().equals("OPTIONS")) {
            peticionesOptionsCors(request, response);
            return;
        }
        
        if (!request.getRequestURI().equals(request.getContextPath() + JwtAuthenticationController.METODO_AUTENTICACION)
                && !request.getRequestURI().equals(request.getContextPath() + JwtAuthenticationController.METODO_VERSION)
                && !request.getRequestURI().equals(request.getContextPath() + PerfilControler.RESOURCE_CAMBIOLOGIN)
                && !request.getRequestURI().equals(request.getContextPath() + EtiquetaControler.RESOURCE_BY_LLAVE)
                && !request.getRequestURI().equals(request.getContextPath() + EtiquetaControler.RESOURCE_BY_GRUPO)) {
            final String requestTokenHeader = request.getHeader(JwtTokenUtil.KEY_TOKEN);
            
            String rolNombre = null;
            
            if (requestTokenHeader == null || !requestTokenHeader.startsWith("Bearer ")) {
                log.info("Token invalido " + requestTokenHeader);
                tokenInvalido(request, response);
                return;
            }
            
            try {
                rolNombre = jwtTokenUtil.getRolNombreFromToken(requestTokenHeader);
                
                if (jwtTokenUtil.isTokenExpired(requestTokenHeader)) {
                    log.info("Token expirado ");
                    tokenInvalido(request, response);
                    return;
                }
            } catch (Exception e) {
                log.error("Token invalido, " + e.getMessage(), e);
                tokenInvalido(request, response);
                return;
            }
            
            Rol rol = rolRepository.findByNombreAndEstadoTrue(rolNombre);
            
            if (rol == null) {
                tokenInvalido(request, response);
                return;
            }
            
            boolean flag = true;

            int validacionActiveDirectory = ((BigDecimal)  parametroService.getParamVal(ParametroID.VALIDACION_ACTIVE_DIRECTORY)).intValue() ;

            exito:
            for (RolAccion rolAccion : rolAccionRepository.findAllByRolId(rol.getId())) {
                Accion accion = accionRepository.findById(rolAccion.getAccionId()).get();
                //if (!(validacionActiveDirectory==ParametroID.LOCAL && accion.getFormularioId()==ParametroID.FORMULARIO_GRUPO)){
                    String[] urls = accion.getUrl().split(",");
                    String[] metodos = accion.getMetodo().split(",");
                    int size = urls.length;

                    for (int i = 0; i < size; i++) {
                        String uri = request.getContextPath() + urls[i];
                        if (request.getRequestURI().startsWith(uri) && metodos[i].equals(request.getMethod().toUpperCase())) {
                            flag = false;
                            break exito;
                        }

                    }
               // }
                

            }
            
            if (flag) {
                log.info("No tiene permiso al recurso, " + request.getRequestURL());
                sinPermiso(request, response);
                return;
            }
            
            UserDetails userDetails = this.jwtUserDetailsService.loadUserByUsername(rolNombre);
            
            UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
                    userDetails, null, userDetails.getAuthorities());
            usernamePasswordAuthenticationToken
                    .setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
        }
        
        chain.doFilter(request, response);
    }
}
