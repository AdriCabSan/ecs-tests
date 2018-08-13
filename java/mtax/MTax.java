import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MTax implements Constant {

    public MTax(){

    }

    public static List<String> validate(List<XTax> xTaxList) {

        List<String> errorList = new ArrayList<>();
        List<String> validIds = new ArrayList<>();
        boolean isXTaxListValid = xTaxList != null && xTaxList.size() > 0;
        if(!isXTaxListValid)
            errorList.add("El documento no tiene tasas");
        List<String> taxCategoryList = MInfoTaxCategory.getTaxCategoryStringList();
        boolean hasJustLocalTaxes;
            for (XTax tax : xTaxList) {
                if(tax.getId() != null)
                    validIds.add(tax.getId().toString());
                if(isXTaxNotValid(errorList, taxCategoryList, tax))
                    errorList.add("El impuesto no es un dato valido");
                hasJustLocalTaxes = areLocalTaxesPresent(errorList, tax);
            }
            if(hasJustLocalTaxes)
                errorList.add("Debe de incluir al menos una tasa no local");

        checkValidIds(xTaxList, errorList, validIds);
        return errorList;
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

    private static boolean isXTaxNotValid(List<String> errorList, List<String> taxCategoryList, XTax tax) {
        return !taxCategoryList.contains(tax.getTax()) && isXTaxNotEntered(errorList,tax);
    }

    private static boolean isXTaxNotEntered(List<String> errorList, XTax tax) {
        boolean isTaxPresent=false;
        if(tax.getAmount() == null)
            errorList.add("El importe es obligatorio");
        if(tax.getTax() == null)
            errorList.add("El impuesto es obligatorio");
        else isTaxPresent=true;
        return isTaxPresent;
    }

    private static void setXTaxesCreationDates(List<XTax> xTaxList, List<XTax> xt) {
        HashMap<String, XTax> map_taxs = getXTaxHashMap(xt);
        for(int i = 0; i < xTaxList.size(); i++)
            boolean IsXTaxIdNull = xTaxList.get(i).getId() != null;
            if(IsXTaxIdNull)
                setXTaxCreationDate(xTaxList, map_taxs, i);
    }

    private static void setXTaxCreationDate(List<XTax> xTaxList, HashMap<String, XTax> map_taxes, int i) {
        xTaxList.get(i)
                .setCreated(
                        getValidXTaxCreationDate(xTaxList, map_taxes, i));
    }

    private static boolean areLocalTaxesPresent(List<String> errorList, X_Tax tax) {
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

    private static HashMap<String, XTax> getXTaxHashMap(List<XTax> xt) {
        HashMap<String, XTax> map_taxs = new HashMap<String, XTax>();
        for(XTax tax: xt){
            map_taxs.put(tax.getId().toString(), tax);
        }
        return map_taxs;
    }

    private static Date getValidXTaxCreationDate(List<XTax> xTaxList, HashMap<String, XTax> map_taxs, int i) {
        return return map_taxes.get(
                xTaxList.get(i)
                        .getId()
                        .toString()
        ).getCreated();
    }

}
