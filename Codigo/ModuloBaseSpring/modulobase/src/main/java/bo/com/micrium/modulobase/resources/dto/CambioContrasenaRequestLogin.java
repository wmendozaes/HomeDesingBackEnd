package bo.com.micrium.modulobase.resources.dto;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CambioContrasenaRequestLogin implements Serializable{
	
	private String userName;
    private String contrasenaAntigua;
    private String contrasenaNueva;
}
