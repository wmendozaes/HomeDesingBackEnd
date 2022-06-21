package bo.com.micrium.modulobase;

import java.util.TimeZone;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ModulobaseApplication {

	public static void main(String[] args) {
		TimeZone.setDefault(TimeZone.getTimeZone("America/La_Paz"));
		SpringApplication.run(ModulobaseApplication.class, args);
	}

}
