/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bo.com.micrium.modulobase.security.jwt;

import com.zaxxer.hikari.HikariConfig;
import javax.sql.DataSource;
import lombok.extern.log4j.Log4j2;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Log4j2
@Configuration
public class JpaConfig extends HikariConfig {

    @Bean(name = "customDataSource")
    @ConfigurationProperties("spring.datasource")
    public DataSource customDataSource() {
        return DataSourceBuilder.create().build();
    }

}
