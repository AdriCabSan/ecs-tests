import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class MTax implements Constant {

    public MTax(){

    }

    public static List<String> validateXTaxList(List<XTax> xTaxList) {

        List<String> errorList = new ArrayList<>();
        List<String> validIds = new ArrayList<>();
        if (isXTaxListNotValid(xTaxList, errorList)) return errorList;
        checkXTaxesValidity(xTaxList, errorList, validIds);
        checkValidIds(xTaxList, errorList, validIds);
        return errorList;
    }

    private static void checkXTaxesValidity(List<XTax> xTaxList, List<String> errorList, List<String> validIds) {
        boolean hasJustLocalTaxes = areXTaxesValid(xTaxList, errorList, validIds);
        if(hasJustLocalTaxes)
            errorList.add("Debe de incluir al menos una tasa no local");
    }

    protected static boolean areXTaxesValid(List<XTax> xTaxList, List<String> errorList, List<String> validIds) {
        boolean hasJustLocalTaxes = false;
        for (XTax tax : xTaxList) {
            if(tax.getId() != null)
                validIds.add(tax.getId().toString());
            if(isXTaxNotValid(errorList, tax))
                errorList.add("El impuesto no es un dato valido");
            hasJustLocalTaxes = areLocalTaxesPresent(errorList, tax);
        }
        return hasJustLocalTaxes;
    }

    protected static boolean isXTaxListNotValid(List<XTax> xTaxList, List<String> errorList) {
        boolean isXTaxListValid = xTaxList != null && xTaxList.size() > 0;
        if(!isXTaxListValid) {
            errorList.add("El documento no tiene tasas");
            return true;
        }
        return false;
    }

    protected static boolean isXTaxNotValid(List<String> errorList, XTax tax) {
        List<String> taxCategoryList = MInfoTaxCategory.getTaxCategoryStringList();
        return !taxCategoryList.contains(tax.getTax()) && isXTaxEntered(errorList,tax);
    }

    protected static boolean isXTaxEntered(List<String> errorList, XTax tax) {
        boolean isTaxPresent=false;
        if(tax.getAmount() == null)
            errorList.add("El importe es obligatorio");
        if(tax.getTax() == null)
            errorList.add("El impuesto es obligatorio");
        else isTaxPresent=true;
        return isTaxPresent;
    }

    protected static boolean areLocalTaxesPresent(List<String> errorList, XTax tax) {
        boolean hasJustLocalTaxes = true;
        boolean isTaxAmountPresent = tax.isTrasladado() && tax.getTaxAmount() == null;
        if(!tax.isLocal()){
            hasJustLocalTaxes=false;
            if(tax.getTaxAmount() == null )
                errorList.add("El importe es obligatorio");
        }
        else if(isTaxAmountPresent)
            errorList.add("El importe es obligatorio");

        return hasJustLocalTaxes;
    }

    private static void checkValidIds(List<XTax> xTaxList, List<String> errorList, List<String> validIds) {
        if(validIds.size() > 0){
            List<XTax> xt = TaxsByListId(validIds, false);
            if(xt.size() != validIds.size())
                errorList.add("Existen datos no guardados previamente");
            else
                setXTaxesCreationDates(xTaxList, xt);
        }
    }

    private static void setXTaxesCreationDates(List<XTax> xTaxList, List<XTax> xt) {
        HashMap<String, XTax> map_taxes = getXTaxHashMap(xt);
        boolean IsXTaxIdNull;
        for(int i = 0; i < xTaxList.size(); i++) {
            IsXTaxIdNull = xTaxList.get(i).getId() != null;
            if (IsXTaxIdNull)
                setXTaxCreationDate(xTaxList, map_taxes, i);
        }
    }

    protected static HashMap<String, XTax> getXTaxHashMap(List<XTax> xt) {
        HashMap<String, XTax> map_taxes = new HashMap<String, XTax>();
        for(XTax tax: xt)
            map_taxes.put(tax.getId().toString(), tax);

        return map_taxes;
    }
    private static void setXTaxCreationDate(List<XTax> xTaxList, HashMap<String, XTax> map_taxes, int i) {
        xTaxList.get(i)
                .setCreated(
                        getValidXTaxCreationDate(xTaxList, map_taxes, i));
    }

    protected static Date getValidXTaxCreationDate(List<XTax> xTaxList, HashMap<String, XTax> map_taxes, int i) {
         return map_taxes.get(
                xTaxList.get(i)
                        .getId()
                        .toString()
        ).getCreated();
    }

}
