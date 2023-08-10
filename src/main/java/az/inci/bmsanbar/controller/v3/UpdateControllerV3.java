package az.inci.bmsanbar.controller.v3;

import az.inci.bmsanbar.model.v2.Response;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@RequestMapping("/v3")
@RestController
@Slf4j
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
            byte[] result = Files.readAllBytes(path);
            return ResponseEntity.ok(Response.getResultResponse(result));
        }
        catch(IOException e)
        {
            log.error(e.toString());
            return ResponseEntity.ok(Response.getServerErrorResponse(e.toString()));
        }
    }
}
