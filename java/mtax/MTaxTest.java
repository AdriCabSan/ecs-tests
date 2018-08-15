import org.junit.Before;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class MTaxTest {
    List<String> errorList = new ArrayList<>();
    List<String> validIds = new ArrayList<>();
    List<XTax> xTaxList=new ArrayList<XTax>();
    List<String> taxCategoryList;
    List<XTax> xt;
    HashMap<String ,XTax> taxHashMap= new HashMap<String ,XTax>;
    MTax mTax= new MTax();

    @Before
    public void initVariables(){
        taxCategoryList = MInfoTaxCategory.getTaxCategoryStringList();
        errorList.add("");
        xTaxList.add(new XTax());
        validIds.add(xTaxList.get(0).toString());
        List<XTax> xt = TaxsByListId(validIds, false);
        taxHashMap.put("1",xTaxList.get(0));

    }

    @Test
    void validateXTaxListTest() {
        List<String> result= mTax.validateXTaxList(xTaxList);
        assertEquals(errorList,result);
    }

    @Test
    void areXTaxesValidTest() {
        boolean result= mTax.areXTaxesValid(xTaxList,errorList,validIds);
        assertEquals(false,result);
    }

    @Test
    void isXTaxListNotValidTest() {
        boolean result= mTax.isXTaxListNotValid(xTaxList,errorList);
        assertEquals(false,result);
    }

    @Test
    void isXTaxNotValidTest() {
        boolean result =mTax.isXTaxNotValid(errorList,new XTax());
        assertEquals(false,result);
    }

    @Test
    void isXTaxEnteredTest() {
        boolean result = mTax.isXTaxEntered(errorList,xTaxList.get(0));
        assertEquals(true,result);
    }

    @Test
    void isLocalTaxPresentTest() {
        boolean result = mTax.isLocalTaxPresent(errorList,xTaxList.get(0));
        assertEquals(false,result);
    }

    @Test
    void isLocalTaxAmountPresentTest() {
        boolean result = mTax.isLocalTaxAmountPresent(errorList,xTaxList.get(0));
        assertEquals(false,result);
    }

    @Test
    void getXTaxHashMapTest() {
        HashMap<String ,XTax> result = mTax.getXTaxHashMap(xt);
        assertEquals(taxHashMap,result);
    }

    @Test
    void getValidXTaxCreationDateTest() {
        Date expectedDate=new Date(2017,8,12);
        Date result= mTax.getValidXTaxCreationDate(xTaxList,mTax.getXTaxHashMap(xt),1);
        assertEquals(expectedDate,result);
    }
}