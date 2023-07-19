package az.inci.bmsanbar.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@RestController
public class UpdateController
{
    @RequestMapping("/download")
    @ResponseBody
    public ResponseEntity<byte[]> download(@RequestParam("file-name") String fileName)
    {
        File file = new File(String.format("D:\\BMS Anbar\\%s.apk", fileName));
        Path path = Paths.get(file.getPath());

        byte[] bytes = new byte[0];

        try
        {
            bytes = Files.readAllBytes(path);
        }
        catch(IOException e)
        {
            e.printStackTrace();
            return new ResponseEntity<>(bytes, HttpStatus.OK);
        }

        return new ResponseEntity<>(bytes, HttpStatus.OK);
    }
}
