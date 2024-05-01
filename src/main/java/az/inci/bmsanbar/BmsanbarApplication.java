package az.inci.bmsanbar;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
public class BmsanbarApplication
{


    public static void main(String[] args)
    {
        SpringApplication.run(BmsanbarApplication.class, args);
    }

    @GetMapping(value = "/")
    public String home()
    {
        return "BMSAnbar Spring service";
    }
}
