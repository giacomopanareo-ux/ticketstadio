package it.unife.ticketstadio.config;
import com.fasterxml.jackson.databind.Module;
import com.fasterxml.jackson.datatype.hibernate6.Hibernate6Module;
import org.springframework.context.annotation.*;
@Configuration
public class JacksonConfig {
    @Bean
    public Module hibernate6Module(){
        Hibernate6Module m=new Hibernate6Module();
        m.configure(Hibernate6Module.Feature.FORCE_LAZY_LOADING,true);
        return m;
    }
}
