package az.inci.bmsanbar.services.v2;


import az.inci.bmsanbar.model.InvAttribute;
import az.inci.bmsanbar.model.InvBarcode;
import az.inci.bmsanbar.model.Inventory;
import az.inci.bmsanbar.model.v2.InvInfo;
import az.inci.bmsanbar.services.AbstractService;
import jakarta.persistence.Query;
import jakarta.persistence.StoredProcedureQuery;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static jakarta.persistence.ParameterMode.IN;

@SuppressWarnings({"SqlResolve", "SqlNoDataSourceInspection", "unchecked"})
@Service
public class InventoryServiceV2 extends AbstractService
{

    public InvInfo getInfoByBarcode(String barcode, String userId)
    {
        InvInfo invInfo = new InvInfo();

        Query q = em.createNativeQuery("EXEC DBO.SP_INV_INFO_BY_BARCODE ?,?");
        q.setParameter(1, barcode);
        q.setParameter(2, userId);
        List<Object[]> result = q.getResultList();
        if(result.size() > 0)
        {
            invInfo.setInvCode(String.valueOf(result.get(0)[0]));
            invInfo.setInvName(String.valueOf(result.get(0)[1]));
            invInfo.setInfo(String.valueOf(result.get(0)[2]));
            invInfo.setWhsQty(Double.parseDouble(String.valueOf(result.get(0)[3])));
            invInfo.setDefaultUomCode(String.valueOf(result.get(0)[4]));
        }

        em.close();

        return invInfo;
    }

    public InvInfo getInfoByInvCode(String invCode, String userId)
    {
        InvInfo invInfo = new InvInfo();

        Query q = em.createNativeQuery("EXEC DBO.SP_INV_INFO_BY_INV_CODE ?,?");
        q.setParameter(1, invCode);
        q.setParameter(2, userId);
        List<Object[]> result = q.getResultList();
        if(result.size() > 0)
        {
            invInfo.setInvCode(String.valueOf(result.get(0)[0]));
            invInfo.setInvName(String.valueOf(result.get(0)[1]));
            invInfo.setInfo(String.valueOf(result.get(0)[2]));
            invInfo.setWhsQty(Double.parseDouble(String.valueOf(result.get(0)[3])));
            invInfo.setDefaultUomCode(String.valueOf(result.get(0)[4]));
        }

        em.close();

        return invInfo;
    }

    public List<Inventory> getSearchResult(String keyword, String field)
    {
        List<Inventory> inventoryList = new ArrayList<>();

        String wildCardKeyword = "%".concat(keyword.trim().replaceAll(" ", "%")).concat("%");

        Query q = em.createNativeQuery(
                "SELECT INV_CODE, INV_NAME, INV_BRAND_CODE, UNIT_CODE FROM INV_MASTER WHERE INV_NAME LIKE ?");
        if(field != null)
        {
            switch (field)
            {
                case "Kod" -> q = em.createNativeQuery(
                        "SELECT INV_CODE, INV_NAME, INV_BRAND_CODE, UNIT_CODE FROM INV_MASTER WHERE INV_CODE LIKE ?");
                case "Ad" -> q = em.createNativeQuery(
                        "SELECT INV_CODE, INV_NAME, INV_BRAND_CODE, UNIT_CODE FROM INV_MASTER WHERE INV_NAME LIKE ?");
            }
        }

        q.setParameter(1, wildCardKeyword);

        List<Object[]> resultList = q.getResultList();
        resultList.stream().map((result)->
                                {
                                    Inventory inventory = new Inventory();
                                    inventory.setInvCode(String.valueOf(result[0]));
                                    inventory.setInvName(String.valueOf(result[1]));
                                    inventory.setInvBrand(String.valueOf(result[2]));
                                    inventory.setDefaultUomCode(String.valueOf(result[3]));
                                    return inventory;
                                }).forEachOrdered(inventoryList::add);

        em.close();

        return inventoryList;
    }

    public Inventory getInvByBarcode(String barcode)
    {
        Query q = em.createNativeQuery("SELECT IM.INV_CODE, " +
                                       "IM.INV_NAME, " +
                                       "IM.INV_BRAND_CODE, " +
                                       "PL.PRICE " +
                                       "FROM INV_MASTER IM " +
                                       "LEFT JOIN INV_BARCODE IB ON IM.INV_CODE = IB.INV_CODE " +
                                       "LEFT JOIN PRICE_LIST PL ON IM.INV_CODE = PL.INV_CODE " +
                                       "AND PL.PRICE_CODE = 'P01' " +
                                       "WHERE IB.BAR_CODE = ?");
        q.setParameter(1, barcode);
        List<Object[]> resultList = q.getResultList();
        Inventory inventory = new Inventory();
        resultList.stream().peek((result)->inventory.setInvCode(String.valueOf(result[0])))
                  .peek((result)->inventory.setInvName(String.valueOf(result[1])))
                  .peek((result)->inventory.setInvBrand(String.valueOf(result[2])))
                  .forEachOrdered((result)->inventory.setPrice(Double.parseDouble(String.valueOf(result[3]))));

        em.close();

        return inventory;
    }

    public BigDecimal getQty(String whsCode, String invCode)
    {
        Query q = em.createNativeQuery("SELECT WHS_QTY FROM WHS_SUM WHERE WHS_CODE=? AND INV_CODE=?");
        q.setParameter(1, whsCode);
        q.setParameter(2, invCode);
        List<BigDecimal> resultList = q.getResultList();
        BigDecimal qty = new BigDecimal("0");
        if(resultList.size() > 0)
            qty = resultList.get(0);

        em.close();

        return qty;
    }

    public Integer getPickReport(String startDate, String endDate, String pickUser)
    {
        Query q = em.createNativeQuery("SELECT count(*) FROM INV_PICK_TRX IPT JOIN PICK_DOC PD " +
                                       "ON IPT.TRX_NO=PD.TRX_NO AND PD.REC_STATUS=6 " +
                                       "WHERE IPT.TRX_DATE BETWEEN ? AND ? AND IPT.PICK_USER_ID=? " +
                                       "AND IPT.PICK_STATUS IN ('P', 'Q')");
        q.setParameter(1, startDate);
        q.setParameter(2, endDate);
        q.setParameter(3, pickUser);
        List<Integer> resultList = q.getResultList();
        Integer qty = 0;
        if(resultList.size() > 0)
            qty = resultList.get(0);

        em.close();

        return qty;
    }

    public Integer getPackReport(String startDate, String endDate, String approveUser)
    {
        Query q = em.createNativeQuery("SELECT count(*) FROM INV_PICK_TRX IPT JOIN PICK_DOC PD " +
                                       "ON IPT.TRX_NO=PD.TRX_NO WHERE IPT.TRX_DATE BETWEEN ? AND ? AND IPT.APPROVE_USER_ID=? " +
                                       "AND IPT.PICK_STATUS IN ('P', 'Q')");
        q.setParameter(1, startDate);
        q.setParameter(2, endDate);
        q.setParameter(3, approveUser);
        List<Integer> resultList = q.getResultList();
        Integer qty = 0;
        if(resultList.size() > 0)
            qty = resultList.get(0);

        em.close();

        return qty;
    }

    public List<InvAttribute> getAttributeList(String invCode)
    {
        List<InvAttribute> attributeList = new ArrayList<>();

        Query q = em.createNativeQuery("SELECT IMA.INV_CODE, " +
                                       "IMA.INV_ATTRIB_ID, " +
                                       "IMA.INV_ATTRIB_NAME, " +
                                       "IMA.DATA_TYPE," +
                                       "ISNULL(IA.ATTRIB_VALUE, '') AS ATTRIB_VALUE," +
                                       "IA.WHS_CODE AS WHS_CODE," +
                                       "IIF(IA.ATTRIB_VALUE IS NULL, 0, 1) ATTRIB_STATUS " +
                                       "FROM (SELECT IM.INV_CODE, " +
                                       "           IAD.INV_ATTRIB_ID," +
                                       "           IAD.INV_ATTRIB_NAME," +
                                       "           IAD.DATA_TYPE" +
                                       "    FROM INV_MASTER IM," +
                                       "         INV_ATTRIB_DEF IAD) IMA " +
                                       "LEFT JOIN INV_ATTRIB IA ON IA.INV_CODE = IMA.INV_CODE " +
                                       "AND IA.INV_ATTRIB_ID = IMA.INV_ATTRIB_ID " +
                                       "WHERE IMA.INV_CODE=?");
        q.setParameter(1, invCode);
        List<Object[]> resultList = q.getResultList();
        resultList.stream().map((result)->
                                {
                                    InvAttribute attribute = new InvAttribute();
                                    attribute.setInvCode(String.valueOf(result[0]));
                                    attribute.setAttributeId(String.valueOf(result[1]));
                                    attribute.setAttributeName(String.valueOf(result[2]));
                                    attribute.setAttributeType(String.valueOf(result[3]));
                                    attribute.setAttributeValue(String.valueOf(result[4]));
                                    attribute.setWhsCode(String.valueOf(result[5]));
                                    attribute.setDefined(Integer.parseInt(String.valueOf(result[6])) == 1);
                                    return attribute;
                                }).forEachOrdered(attributeList::add);

        em.close();

        return attributeList;
    }

    public List<InvAttribute> getAttributeList(String invCode, String userId)
    {
        List<InvAttribute> attributeList = new ArrayList<>();

        Query q = em.createNativeQuery("SELECT IMA.INV_CODE, " +
                                       "IMA.INV_ATTRIB_ID, " +
                                       "IMA.INV_ATTRIB_NAME, " +
                                       "IMA.DATA_TYPE," +
                                       "ISNULL(IA.ATTRIB_VALUE, '') AS ATTRIB_VALUE," +
                                       "IIF(IA.WHS_CODE IS NULL OR IA.WHS_CODE='', (SELECT TOP 1 WHS_CODE FROM BMS_USER_WHS WHERE USER_ID=?), IA.WHS_CODE) AS WHS_CODE," +
                                       "IIF(IA.ATTRIB_VALUE IS NULL, 0, 1) ATTRIB_STATUS " +
                                       "FROM (SELECT IM.INV_CODE, " +
                                       "           IAD.INV_ATTRIB_ID," +
                                       "           IAD.INV_ATTRIB_NAME," +
                                       "           IAD.DATA_TYPE" +
                                       "    FROM INV_MASTER IM," +
                                       "         INV_ATTRIB_DEF IAD) IMA " +
                                       "LEFT JOIN INV_ATTRIB IA ON IA.INV_CODE = IMA.INV_CODE " +
                                       "AND IA.INV_ATTRIB_ID = IMA.INV_ATTRIB_ID " +
                                       "AND (IA.WHS_CODE=(SELECT TOP 1 WHS_CODE FROM BMS_USER_WHS WHERE USER_ID=?) " +
                                       "OR IA.INV_ATTRIB_ID NOT IN ('AT010','AT011')) " +
                                       "WHERE IMA.INV_CODE=?");
        q.setParameter(1, userId);
        q.setParameter(2, userId);
        q.setParameter(3, invCode);
        List<Object[]> resultList = q.getResultList();
        resultList.stream().map((result)->
                                {
                                    InvAttribute attribute = new InvAttribute();
                                    attribute.setInvCode(String.valueOf(result[0]));
                                    attribute.setAttributeId(String.valueOf(result[1]));
                                    attribute.setAttributeName(String.valueOf(result[2]));
                                    attribute.setAttributeType(String.valueOf(result[3]));
                                    attribute.setAttributeValue(String.valueOf(result[4]));
                                    attribute.setWhsCode(String.valueOf(result[5]));
                                    attribute.setDefined(Integer.parseInt(String.valueOf(result[6])) == 1);
                                    return attribute;
                                }).forEachOrdered(attributeList::add);

        em.close();

        return attributeList;
    }

    @Transactional
    public void updateInvAttributes(List<InvAttribute> attributeList)
    {
        for(InvAttribute attribute : attributeList)
        {
            if(attribute.isDefined())
            {
                Query q;
                if(attribute.getAttributeValue().isEmpty() ||
                   (attribute.getAttributeValue().equals("0")
                    && attribute.getAttributeType().equals("BIT")))
                {
                    q = em.createNativeQuery(
                            "DELETE FROM INV_ATTRIB WHERE INV_CODE=? AND INV_ATTRIB_ID=? " +
                            "AND (WHS_CODE=? OR INV_ATTRIB_ID NOT IN ('AT010','AT011'))");
                    q.setParameter(1, attribute.getInvCode());
                    q.setParameter(2, attribute.getAttributeId());
                    q.setParameter(3, attribute.getWhsCode());
                }
                else
                {
                    q = em.createNativeQuery(
                            "UPDATE INV_ATTRIB SET ATTRIB_VALUE= ? " +
                            "WHERE INV_CODE=? AND INV_ATTRIB_ID=? " +
                            "AND (WHS_CODE=? OR INV_ATTRIB_ID NOT IN ('AT010','AT011'))");
                    q.setParameter(1, attribute.getAttributeValue());
                    q.setParameter(2, attribute.getInvCode());
                    q.setParameter(3, attribute.getAttributeId());
                    q.setParameter(4, attribute.getWhsCode());
                }
                q.executeUpdate();
            }
            else if(!attribute.getAttributeValue().isEmpty())
            {
                Query q = em.createNativeQuery(
                        "INSERT INTO INV_ATTRIB(INV_CODE,INV_ATTRIB_ID,ATTRIB_VALUE,WHS_CODE) VALUES (?,?,?,?)");
                q.setParameter(1, attribute.getInvCode());
                q.setParameter(2, attribute.getAttributeId());
                q.setParameter(3, attribute.getAttributeValue());
                q.setParameter(4, attribute.getWhsCode());
                q.executeUpdate();
            }
        }
        em.close();
    }

    @Transactional
    public boolean updateShelfBarcode(String whsCode,
                                      String shelfBarcode,
                                      String invBarcode)
    {
        shelfBarcode = shelfBarcode.replace("%23", "#");
        Query selectQuery = em.createNativeQuery("SELECT INV_CODE FROM INV_ATTRIB" +
                                                 " WHERE INV_CODE=(SELECT INV_CODE FROM INV_BARCODE WHERE BAR_CODE=?)" +
                                                 " AND INV_ATTRIB_ID='AT010' AND WHS_CODE=?");
        selectQuery.setParameter(1, invBarcode);
        selectQuery.setParameter(2, whsCode);
        String invCode = "";
        List<Object> resultList = selectQuery.getResultList();
        if(resultList.size() > 0)
            invCode = String.valueOf(resultList.get(0));

        if(invCode != null && !invCode.isEmpty())
        {
            Query updateQuery = em.createNativeQuery("UPDATE INV_ATTRIB SET ATTRIB_VALUE= ?" +
                                                     " WHERE INV_CODE=? AND INV_ATTRIB_ID='AT010' AND WHS_CODE=?");
            updateQuery.setParameter(1, shelfBarcode);
            updateQuery.setParameter(2, invCode);
            updateQuery.setParameter(3, whsCode);
            updateQuery.executeUpdate();
        }
        else
        {
            selectQuery = em.createNativeQuery("SELECT INV_CODE from INV_BARCODE WHERE BAR_CODE=?");
            selectQuery.setParameter(1, invBarcode);
            resultList = selectQuery.getResultList();
            if(resultList.size() > 0)
            {
                invCode = String.valueOf(resultList.get(0));
                Query insertQuery = em.createNativeQuery(
                        "INSERT INTO INV_ATTRIB(INV_CODE,INV_ATTRIB_ID,ATTRIB_VALUE,WHS_CODE)" +
                        " VALUES (?,?,?,?)");
                insertQuery.setParameter(1, invCode);
                insertQuery.setParameter(2, "AT010");
                insertQuery.setParameter(3, shelfBarcode);
                insertQuery.setParameter(4, whsCode);
                insertQuery.executeUpdate();
            }
            else
            {
                return false;
            }
        }

        em.close();

        return true;
    }

    public List<InvBarcode> getBarcodeList(String invCode)
    {
        List<InvBarcode> barcodeList = new ArrayList<>();

        Query q = em.createNativeQuery(
                "SELECT IM.INV_CODE,"
                + "       IB.BAR_CODE,"
                + "       IB.UOM_FACTOR,"
                + "       IB.UOM"
                + " FROM INV_MASTER IM"
                + "     JOIN INV_BARCODE IB ON IM.INV_CODE = IB.INV_CODE"
                + " WHERE IM.INV_CODE = ?");
        q.setParameter(1, invCode);
        List<Object[]> resultList = q.getResultList();
        resultList.stream().map((result)->
                                {
                                    InvBarcode barcode = new InvBarcode();
                                    barcode.setInvCode(String.valueOf(result[0]));
                                    barcode.setBarcode(String.valueOf(result[1]));
                                    barcode.setUomFactor(Double.parseDouble(String.valueOf(result[2])));
                                    barcode.setUom(String.valueOf(result[3]));
                                    barcode.setDefined(true);
                                    return barcode;
                                }).forEachOrdered(barcodeList::add);

        em.close();

        return barcodeList;
    }

    @Transactional
    public void updateInvBarcodes(List<InvBarcode> barcodeList)
    {
        for(InvBarcode barcode : barcodeList)
        {
            Query q;
            if(barcode.isDefined())
            {
                if(barcode.getUomFactor() == 0)
                {
                    q = em.createStoredProcedureQuery("SP_DELETE_BARCODE");
                    ((StoredProcedureQuery)q).registerStoredProcedureParameter("INV_CODE", String.class, IN);
                    ((StoredProcedureQuery)q).registerStoredProcedureParameter("BAR_CODE", String.class, IN);
                }
                else
                {
                    q = em.createNativeQuery("""
                                     UPDATE INV_BARCODE
                                     SET UOM_FACTOR = :UOM_FACTOR,
                                         UOM = :UOM
                                     WHERE INV_CODE = :INV_CODE AND BAR_CODE = :BAR_CODE""");
                    q.setParameter("UOM_FACTOR", barcode.getUomFactor());
                    q.setParameter("UOM", barcode.getUom());
                }
                q.setParameter("INV_CODE", barcode.getInvCode());
                q.setParameter("BAR_CODE", barcode.getBarcode());
            }
            else
            {
                q = em.createStoredProcedureQuery("SP_INSERT_BARCODE");
                ((StoredProcedureQuery)q).registerStoredProcedureParameter("INV_CODE", String.class, IN);
                ((StoredProcedureQuery)q).registerStoredProcedureParameter("BAR_CODE", String.class, IN);
                ((StoredProcedureQuery)q).registerStoredProcedureParameter("UOM", String.class, IN);
                ((StoredProcedureQuery)q).registerStoredProcedureParameter("UOM_FACTOR", Double.class, IN);
                q.setParameter("INV_CODE", barcode.getInvCode());
                q.setParameter("BAR_CODE", barcode.getBarcode());
                q.setParameter("UOM_FACTOR", barcode.getUomFactor());
                q.setParameter("UOM", barcode.getUom());
            }
            q.executeUpdate();
        }
        em.close();
    }

    public List<Inventory> getInvList()
    {
        List<Inventory> inventoryList = new ArrayList<>();

        Query q = em.createNativeQuery(
                "SELECT IM.INV_CODE, IM.INV_NAME,IM.INV_BRAND_CODE,IB.BAR_CODE," +
                " PL.PRICE,ISNULL(IA.ATTRIB_VALUE, '') FROM INV_MASTER IM" +
                " JOIN INV_BARCODE IB ON IM.INV_CODE=IB.INV_CODE" +
                " JOIN INV_ATTRIB IA ON IM.INV_CODE = IA.INV_CODE AND IA.INV_ATTRIB_ID='AT019'" +
                " JOIN PRICE_LIST PL ON IM.INV_CODE = PL.INV_CODE AND PL.PRICE_CODE = 'P01' " +
                " ORDER BY IM.INV_CODE");
        List<Object[]> resultList = q.getResultList();
        resultList.stream().map((result)->
                                {
                                    Inventory inventory = new Inventory();
                                    inventory.setInvCode(String.valueOf(result[0]));
                                    inventory.setInvName(String.valueOf(result[1]));
                                    inventory.setInvBrand(String.valueOf(result[2]));
                                    inventory.setBarcode(String.valueOf(result[3]));
                                    inventory.setPrice(Double.parseDouble(String.valueOf(result[4])));
                                    inventory.setInternalCount(String.valueOf(result[5]));
                                    return inventory;
                                }).forEachOrdered(inventoryList::add);

        em.close();

        return inventoryList;
    }

    public List<Inventory> getInvListByUser(String userId)
    {
        List<Inventory> inventoryList = new ArrayList<>();

        Query q = em.createNativeQuery(
                "EXEC DBO.SP_GET_INV_LIST_BY_USER_PRODUCER ?");
        q.setParameter(1, userId);
        List<Object[]> resultList = q.getResultList();
        for(Object[] result : resultList)
        {
            Inventory inventory = new Inventory();
            inventory.setInvCode(String.valueOf(result[0]));
            inventory.setInvName(String.valueOf(result[1]));
            inventory.setInvBrand(String.valueOf(result[2]));
            inventory.setBarcode(String.valueOf(result[3]));
            inventory.setInternalCount(String.valueOf(result[4]));
            inventoryList.add(inventory);
        }

        em.close();

        return inventoryList;
    }

    public List<Inventory> getWhsSumByUser(String userId, String whsCode)
    {
        List<Inventory> inventoryList = new ArrayList<>();

        Query q = em.createNativeQuery(
                "EXEC DBO.SP_GET_WHS_SUM_FOR_TERMINAL_INT_USE ?, ?");
        q.setParameter(1, userId);
        q.setParameter(2, whsCode);
        List<Object[]> resultList = q.getResultList();
        for(Object[] result : resultList)
        {
            Inventory inventory = new Inventory();
            inventory.setInvCode(String.valueOf(result[0]));
            inventory.setInvName(String.valueOf(result[1]));
            inventory.setInvBrand(String.valueOf(result[2]));
            inventory.setBarcode(String.valueOf(result[3]));
            inventory.setInternalCount(String.valueOf(result[4]));
            inventory.setWhsQty(Double.parseDouble(String.valueOf(result[5])));
            inventory.setPrice(Double.parseDouble(String.valueOf(result[6])));
            inventoryList.add(inventory);
        }

        em.close();

        return inventoryList;
    }

    public InvBarcode getInvBarcode(String barcode)
    {
        InvBarcode invBarcode = new InvBarcode();

        Query q = em.createNativeQuery(
                "SELECT IB.INV_CODE,"
                + "        IB.BAR_CODE,"
                + "        IB.UOM_FACTOR"
                + " FROM INV_BARCODE IB"
                + " WHERE BAR_CODE = ?");
        q.setParameter(1, barcode);
        List<Object[]> resultList = q.getResultList();
        if(resultList.size() > 0)
        {
            invBarcode.setInvCode(String.valueOf(resultList.get(0)[0]));
            invBarcode.setBarcode(String.valueOf(resultList.get(0)[1]));
            invBarcode.setUomFactor(Double.parseDouble(String.valueOf(resultList.get(0)[2])));
            invBarcode.setDefined(true);
        }

        em.close();

        return invBarcode;
    }
}
