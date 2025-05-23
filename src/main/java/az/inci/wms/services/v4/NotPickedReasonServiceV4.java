package az.inci.wms.services.v4;

import az.inci.wms.model.v4.NotPickedReason;
import az.inci.wms.services.AbstractService;
import jakarta.persistence.Query;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class NotPickedReasonServiceV4 extends AbstractService {
    public List<NotPickedReason> getReasonList() {
        List<NotPickedReason> reasonList = new ArrayList<>();
        Query query = em.createNativeQuery("SELECT * FROM INV_NOT_PICKED_REASON");

        List<Object[]> resultList = query.getResultList();
        resultList.stream().map(result -> {
            NotPickedReason reason = new NotPickedReason();
            reason.setReasonId(String.valueOf(result[0]));
            reason.setReasonDescription(String.valueOf(result[1]));
            return reason;
        }).forEachOrdered(reasonList::add);

        em.close();

        return reasonList;
    }
}
