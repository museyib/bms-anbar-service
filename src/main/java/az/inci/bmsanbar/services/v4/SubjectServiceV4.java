package az.inci.bmsanbar.services.v4;

import az.inci.bmsanbar.model.v4.Customer;
import az.inci.bmsanbar.model.v4.ExpCenter;
import az.inci.bmsanbar.model.v4.Sbe;
import az.inci.bmsanbar.model.v4.Whs;
import az.inci.bmsanbar.services.AbstractService;
import jakarta.persistence.Query;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings({"SqlResolve", "SqlNoDataSourceInspection"})
@Service
public class SubjectServiceV4 extends AbstractService {
    public List<Sbe> getSbeList() {
        List<Sbe> sbeList = new ArrayList<>();
        Query q = em.createNativeQuery("SELECT SBE_CODE, SBE_NAME FROM SBE_MASTER WHERE SBE_TYPE IN ('S','B')");

        List<Object[]> resultList = q.getResultList();

        resultList.stream().map((result) ->
        {
            Sbe sbe = new Sbe();
            sbe.setSbeCode(String.valueOf(result[0]));
            sbe.setSbeName(String.valueOf(result[1]));
            return sbe;
        }).forEachOrdered(sbeList::add);

        em.close();

        return sbeList;
    }

    public List<Customer> getCustomerList(String sbeCode) {
        List<Customer> customerList = new ArrayList<>();
        Query q = em.createNativeQuery("""
                SELECT BM.BP_CODE,
                    BM.BP_NAME,
                    SM.SBE_CODE,
                    SM.SBE_NAME
                FROM SBE_BP SB
                JOIN BP_MASTER BM ON SB.BP_CODE=BM.BP_CODE
                JOIN SBE_MASTER SM ON SB.SBE_CODE=SM.SBE_CODE
                WHERE SM.SBE_CODE = :SBE_CODE""");
        q.setParameter("SBE_CODE", sbeCode);

        List<Object[]> resultList = q.getResultList();

        resultList.stream().map((result) ->
        {
            Customer customer = new Customer();
            customer.setBpCode(String.valueOf(result[0]));
            customer.setBpName(String.valueOf(result[1]));
            customer.setSbeCode(String.valueOf(result[2]));
            customer.setSbeName(String.valueOf(result[3]));
            return customer;
        }).forEachOrdered(customerList::add);

        em.close();

        return customerList;
    }

    public List<Whs> getWhsList() {
        List<Whs> whsList = new ArrayList<>();
        Query q = em.createNativeQuery("SELECT WHS_CODE, WHS_NAME FROM WHS_MASTER WHERE INACTIVE_FLAG = 0");

        List<Object[]> resultList = q.getResultList();

        resultList.stream().map((result) ->
        {
            Whs whs = new Whs();
            whs.setWhsCode(String.valueOf(result[0]));
            whs.setWhsName(String.valueOf(result[1]));
            return whs;
        }).forEachOrdered(whsList::add);

        em.close();

        return whsList;
    }

    public List<Whs> getTrgWhsList(String userId) {
        List<Whs> whsList = new ArrayList<>();
        Query q = em.createNativeQuery("""
                SELECT WM.WHS_CODE,WM.WHS_NAME
                FROM BMS_USER_WHS BUW
                JOIN WHS_MASTER WM ON BUW.WHS_CODE = WM.WHS_CODE
                WHERE BUW.USER_ID = :USER_ID""");
        q.setParameter("USER_ID", userId);

        List<Object[]> resultList = q.getResultList();

        resultList.stream().map((result) ->
        {
            Whs whs = new Whs();
            whs.setWhsCode(String.valueOf(result[0]));
            whs.setWhsName(String.valueOf(result[1]));
            return whs;
        }).forEachOrdered(whsList::add);

        em.close();

        return whsList;
    }

    public List<ExpCenter> getExpCenterList() {
        List<ExpCenter> expCenterList = new ArrayList<>();
        Query q = em.createNativeQuery(
                "SELECT EXP_CENTER_CODE, EXP_CENTER_DESC FROM dbo.FN_GET_EXP_CENTER_HIERARCHY('U04')");

        List<Object[]> resultList = q.getResultList();

        resultList.stream().map((result) ->
        {
            ExpCenter expCenter = new ExpCenter();
            expCenter.setExpCenterCode(String.valueOf(result[0]));
            expCenter.setExpCenterName(String.valueOf(result[1]));
            return expCenter;
        }).forEachOrdered(expCenterList::add);

        em.close();

        return expCenterList;
    }
}
