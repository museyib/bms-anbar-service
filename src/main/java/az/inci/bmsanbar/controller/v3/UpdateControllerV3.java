package az.inci.bmsanbar.controller.v3;

import az.inci.bmsanbar.model.v2.Response;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@RequestMapping("/v3")
@RestController
public class UpdateControllerV3
{
    @GetMapping("/download")
    @ResponseBody
    public ResponseEntity<Response> download(@RequestParam("file-name") String fileName)
    {
        try
        {
            File file = new File(fileName.concat(".apk"));
            Path path = Paths.get(file.getPath());
            byte[] bytes = Files.readAllBytes(path);
            return ResponseEntity.ok(Response.builder()
                                             .statusCode(0)
                                             .data(bytes)
                                             .build());
        }
        catch(IOException e)
        {
            return ResponseEntity.ok(Response.builder()
                                             .statusCode(1)
                                             .developerMessage("Server xətası")
                                             .systemMessage(e.toString())
                                             .build());
        }
    }
}
