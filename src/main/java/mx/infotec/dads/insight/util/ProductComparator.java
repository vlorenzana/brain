/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.infotec.dads.insight.util;

import java.util.Comparator;
import java.util.Date;
import mx.infotec.dads.insight.pdes.model.Product;

/**
 *
 * @author victor.lorenzana
 */
public class ProductComparator implements Comparator<Product>{

    @Override
    public int compare(Product o1, Product o2) {
        if(!o1.finishDate.isEmpty() && !o2.finishDate.isEmpty())
        {
            Date date1=DateUtils.convertStringToDate(DateUtils.extractDate(o1.finishDate));
            Date date2=DateUtils.convertStringToDate(DateUtils.extractDate(o2.finishDate));
            return date1.compareTo(date2);
        }
        if(o1.finishDate.isEmpty() && !o2.finishDate.isEmpty())
        {            
            Date date1=new Date();
            Date date2=DateUtils.convertStringToDate(DateUtils.extractDate(o2.finishDate));
            return date1.compareTo(date2);
        }
        if(!o1.finishDate.isEmpty() && o2.finishDate.isEmpty())
        {
            Date date1=DateUtils.convertStringToDate(DateUtils.extractDate(o1.finishDate));
            Date date2=new Date();
            return date1.compareTo(date2);
            
        }
        return 0;
    }
    
}
