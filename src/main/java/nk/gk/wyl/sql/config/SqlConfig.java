package nk.gk.wyl.sql.config;

import nk.gk.wyl.sql.util.sql.SelectSql;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SqlConfig {
    @Value("${is_logic}")
    private boolean is_logic;



    @Bean
    public int run(){
        SelectSql.setIs_logic(is_logic);
        return 0;
    }
}
