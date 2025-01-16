package az.inci.bmsanbar.controller.v4;

import az.inci.bmsanbar.model.v2.Response;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static az.inci.bmsanbar.Utilities.getMessage;

@RequestMapping("/v4")
@RestController
@Slf4j
public class UpdateControllerV4
{
    @GetMapping("/download")
    public ResponseEntity<Response> download(@RequestParam("file-name") String fileName)
    {
        try
        {
            File file = new File(fileName.concat(".apk"));
            Path path = Paths.get(file.getPath());
            byte[] result = Files.readAllBytes(path);
            return ResponseEntity.ok(Response.getResultResponse(result));
        }
        catch(IOException e)
        {
            String message = getMessage(e);
            log.error(message);
            return ResponseEntity.ok(Response.getServerErrorResponse(message));
        }
    }
}
