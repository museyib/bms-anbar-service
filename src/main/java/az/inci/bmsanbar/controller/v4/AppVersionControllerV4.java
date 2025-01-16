package az.inci.bmsanbar.controller.v4;

import az.inci.bmsanbar.model.v2.Response;
import az.inci.bmsanbar.services.v4.AppVersionServiceV4;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/v4/app-version")
@RestController
public class AppVersionControllerV4 {

    private AppVersionServiceV4 appVersionService;

    @Autowired
    public void setAppVersionService(AppVersionServiceV4 appVersionService) {
        this.appVersionService = appVersionService;
    }

    @GetMapping("/check")
    public ResponseEntity<Response> checkForNewVersion(@RequestParam("app-name") String appName,
                                                       @RequestParam("current-version") int currentVersion) {
        return ResponseEntity.ok(Response.getResultResponse(appVersionService.checkForNewVersion(appName,currentVersion)));
    }
}
