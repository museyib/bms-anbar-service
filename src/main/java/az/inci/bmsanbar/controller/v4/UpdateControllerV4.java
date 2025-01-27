package az.inci.bmsanbar.controller.v4;

import az.inci.bmsanbar.model.v4.Response;
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

@RequestMapping("/v4")
@RestController
public class UpdateControllerV4
{
    @GetMapping("/download")
    public ResponseEntity<Response<byte[]>> download(@RequestParam("file-name") String fileName) throws IOException {
        File file = new File(fileName.concat(".apk"));
        Path path = Paths.get(file.getPath());
        byte[] result = Files.readAllBytes(path);
        return ResponseEntity.ok(Response.getResultResponse(result));
    }
}
