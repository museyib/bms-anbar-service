package az.inci.wms.services.v4;

import az.inci.wms.services.AbstractService;
import jakarta.persistence.Query;
import org.springframework.stereotype.Service;

@Service
public class AppVersionServiceV4 extends AbstractService {

    public boolean checkForNewVersion(String appName, int currentVersion) {
        Query query = em.createNativeQuery("""
                SELECT * FROM VERSION_INFO
                WHERE APP_NAME = :APP_NAME AND APP_VERSION > :APP_VERSION""");
        query.setParameter("APP_NAME", appName);
        query.setParameter("APP_VERSION", currentVersion);
        return !query.getResultList().isEmpty();
    }
}
