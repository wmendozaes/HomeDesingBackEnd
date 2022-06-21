/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bo.com.micrium.modulobase.security.jwt;

import bo.com.micrium.modulobase.ldap.ActiveDirectory;
import bo.com.micrium.modulobase.model.Grupo;
import bo.com.micrium.modulobase.model.Rol;
import bo.com.micrium.modulobase.model.Usuario;
import bo.com.micrium.modulobase.repository.IGrupoRepository;
import bo.com.micrium.modulobase.repository.IRolRepository;
import bo.com.micrium.modulobase.repository.IUsuarioRepository;
import bo.com.micrium.modulobase.service.ParametroService;
import bo.com.micrium.modulobase.util.ParametroID;
import bo.com.micrium.modulobase.util.UsuarioTipo;
import bo.com.micrium.modulobase.util.abm.UsuarioEstado;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

@Log4j2
@Component
public class AuthenticationLdapManager implements AuthenticationManager, Serializable {

    private static final long serialVersionUID = 1L;

    @Autowired
    IUsuarioRepository usuarioRepository;

    @Autowired
    IRolRepository rolRepository;

    @Autowired
    IGrupoRepository grupoRepository;

    @Autowired
    ParametroService parametroService;

    @Autowired
    BCryptPasswordEncoder passwordEncoder;

    private Rol validarUsuarioLocal(Usuario usuario, String nombreUsuario, String contrasena) {
        if (usuario == null) {
            throw new BadCredentialsException("El usuario o contraseña ingresados son incorrectos") {
            };
        }

        if (usuario.getEstado() == UsuarioEstado.BLOQUEADO) {
            int timeOut = ((BigDecimal) parametroService.getParamVal(ParametroID.TIEMPO_BLOQUEO_AUTENTICACION)).intValue();

            Calendar cal = Calendar.getInstance();
            cal.setTime(usuario.getUltimoIntento());
            cal.add(Calendar.MINUTE, timeOut);

            if (cal.getTime().getTime() < (new Date()).getTime()) {
                usuario.setNumeroIntentos(0);
                usuario.setEstado(UsuarioEstado.HABILITADO);
            } else {
                throw new DisabledException("Usuario: " + '"' + nombreUsuario + '"' + " ha sido bloqueado por varios reintentos. vuelva a intentar en " + timeOut + " minutos.") {
                };
            }
        }

        if (!passwordEncoder.matches(contrasena, usuario.getContrasena())) {
            int nroOption = ((BigDecimal) parametroService.getParamVal(ParametroID.NUMERO_INTENTOS_AUTENTICACION)).intValue();

            if (usuario.getUltimoIntento() != null) {
                int timeOut = ((BigDecimal) parametroService.getParamVal(ParametroID.TIEMPO_BLOQUEO_AUTENTICACION)).intValue();

                Calendar cal = Calendar.getInstance();
                cal.setTime(usuario.getUltimoIntento());
                cal.add(Calendar.MINUTE, timeOut);

                if (cal.getTime().getTime() < (new Date()).getTime()) {
                    usuario.setNumeroIntentos(0);
                }
            }

            usuario.setUltimoIntento(new Date());

            if (usuario.getNumeroIntentos() >= nroOption) {
                usuario.setEstado(UsuarioEstado.BLOQUEADO);

                long timeOut = ((BigDecimal) parametroService.getParamVal(ParametroID.TIEMPO_BLOQUEO_AUTENTICACION)).longValue();
                throw new DisabledException("El usuario " + '"' + nombreUsuario + '"' + " ha sido inhabilitado. vuelva a intentar en " + timeOut + " minutos.") {
                };
            }

            usuario.setNumeroIntentos(usuario.getNumeroIntentos() + 1);

            usuarioRepository.save(usuario);

            throw new BadCredentialsException("El usuario o contraseña ingresados son incorrectos") {
            };
        }

        usuario.setNumeroIntentos(0);
        usuario.setUltimoIntento(null);

        usuarioRepository.save(usuario);

        return usuario.getRolId();
    }

    public Rol validarLdap(String nombreUsuario, String contrasena) throws AuthenticationException {
    	int validacionActiveDirectory = ((BigDecimal)  parametroService.getParamVal(ParametroID.VALIDACION_ACTIVE_DIRECTORY)).intValue() ;

        Usuario usuario = usuarioRepository.findByNombreUsuarioAndEstadoIn(nombreUsuario, Arrays.asList(UsuarioEstado.HABILITADO, UsuarioEstado.BLOQUEADO));
        
        if (usuario.getValidarFechaLimite()){
        	if ( usuario.getFechaLimite().compareTo(new Date() )<=0){
        		throw new BadCredentialsException("Usuario expiro su tiempo en el sistema, contacte con el administrador") {
                };
        	}
        }
       
        if (usuario.getTipo()==ParametroID.LOCAL){
        	if ( (new Date().compareTo(usuario.getFechaActualizacion()))>0){
         		throw new BadCredentialsException("Usuario debe cambiar su password") {
         		};		  
        	}
        }
        
        
        //antes validar el parametro si corresponde a un valor valido
        //1 LDAP, 2 Local, 3 Hibrido cualquiero otro valor no corresponde
        
        if (validacionActiveDirectory==ParametroID.HIBRIDO && validacionActiveDirectory==ParametroID.LOCAL){ 	
        	if (usuario.getTipo()==ParametroID.LOCAL){
        		return validarUsuarioLocal(usuario, nombreUsuario, contrasena);
        	}
        	throw new BadCredentialsException("Usuario no valido en el sistema") {
            };
        }
        
        /*if (!validacionActiveDirectory) {
            return validarUsuarioLocal(usuario, nombreUsuario, contrasena);
        }*/

        try {
        	//esto para logica super usuario, no se modifica
            if (usuario != null && usuario.getTipo() == UsuarioTipo.SUPER_USUARIO) {
                return validarUsuarioLocal(usuario, nombreUsuario, contrasena);
            }

            //if (!new ActiveDirectory(parametroService).validarUsuario(nombreUsuario, contrasena)) {
            if (validacionActiveDirectory==ParametroID.LDAP && validacionActiveDirectory==ParametroID.HIBRIDO
            		&&  !new ActiveDirectory(parametroService).validarUsuario(nombreUsuario, contrasena)){
                if (usuario == null) {
                    throw new BadCredentialsException("El usuario o contraseña ingresados son incorrectos") {
                    };
                }

                int nroOption = ((BigDecimal) parametroService.getParamVal(ParametroID.NUMERO_INTENTOS_AUTENTICACION)).intValue();

                if (usuario.getUltimoIntento() != null) {
                    int timeOut = ((BigDecimal) parametroService.getParamVal(ParametroID.TIEMPO_BLOQUEO_AUTENTICACION)).intValue();

                    Calendar cal = Calendar.getInstance();
                    cal.setTime(usuario.getUltimoIntento());
                    cal.add(Calendar.MINUTE, timeOut);

                    if (cal.getTime().getTime() >= (new Date()).getTime()) {
                        usuario.setNumeroIntentos(0);
                    }
                }

                usuario.setUltimoIntento(new Date());

                if (usuario.getNumeroIntentos() >= nroOption) {
                    usuario.setEstado(UsuarioEstado.BLOQUEADO);

                    long timeOut = ((BigDecimal) parametroService.getParamVal(ParametroID.TIEMPO_BLOQUEO_AUTENTICACION)).longValue();
                    throw new DisabledException("El usuario " + '"' + nombreUsuario + '"' + " ha sido inhabilitado. vuelva a intentar en " + timeOut + " minutos.") {
                    };
                }

                usuario.setNumeroIntentos(usuario.getNumeroIntentos() + 1);

                usuarioRepository.save(usuario);

                throw new BadCredentialsException("El usuario o contraseña ingresados son incorrectos") {
                };
            }

            if (usuario != null) {
            	return usuario.getRolId();
            }
            Boolean validacionGrupoLDAP=(Boolean) parametroService.getParamVal(ParametroID.ACTIVE_DIRECTORY_GRUPOLDAP);
            
            //Si el usuario no existe, empieza a intentar loguear por Logica de GrupoLDAP, primero verifica parametro
            if (!(validacionActiveDirectory==ParametroID.LDAP || validacionActiveDirectory==ParametroID.HIBRIDO) && !validacionGrupoLDAP){
            	 throw new InsufficientAuthenticationException("Usuario no valido en el sistema") {
                 };
            }
            
            List<Grupo> gruposAD = new ArrayList<>();

            for (String grupoAD : new ActiveDirectory(parametroService).getListaGrupos(nombreUsuario)) {
                Grupo grupo = grupoRepository.findByNombreAndEstadoTrue(grupoAD);
                if (grupo != null) {
                    gruposAD.add(grupo);
                }
            }

            /**
             * Si el usuario no tiene ningun grupo en active directory entonces
             * no puede loggearse al sistema en el caso que tenga habilitada la
             * validacion de grupos de active directory
             */
            if (gruposAD.isEmpty()) {
                throw new InsufficientAuthenticationException("Usted no tiene grupo de trabajo. Solicite a su administrador que lo incluya a un grupo de trabajo") {
                };
            }

            /**
             * en el caso que el rol asignado al usuario no tengan ya no se
             * encuentre relacionado con mis grupos de active directory, pero
             * tenga un grupo de active directory que esta registrado en el
             * sistema, entonces retorno el rol de ese grupo de active directory
             * que tengo relacionado y que se encuentra en el sistema
             */
            return gruposAD.stream().findFirst().get().getRolId();
        } catch (Exception e) {
            log.error("Error al iniciar sesion con " + nombreUsuario + ", " + e.getMessage(), e);
            throw new BadCredentialsException("El usuario no pudo iniciar sesion. Problemas con LDAP " + e.getMessage()) {
            };
        }

    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        log.info("authentication " + authentication);
        log.info("authentication " + authentication.getPrincipal());
        log.info("authentication " + authentication.getCredentials());
        log.info("authentication " + authentication.getName());
        log.info("authentication " + authentication.getDetails());
        return authentication;
    }

}
