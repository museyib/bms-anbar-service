package az.inci.wms;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
public class WmsApplication {


    public static void main(String[] args) {
        SpringApplication.run(WmsApplication.class, args);
    }

    @GetMapping(value = "/")
    public String home() {
        return "BMSAnbar Spring service";
    }
}
