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
        int res=0;
        if(!o1.plannedDate.isEmpty() && !o2.plannedDate.isEmpty())
        {
            Date date1=DateUtils.convertStringToDate(DateUtils.extractDate(o1.plannedDate));
            Date date2=DateUtils.convertStringToDate(DateUtils.extractDate(o2.plannedDate));
            res=date1.compareTo(date2);
        }
        if(o1.plannedDate.isEmpty() && !o2.plannedDate.isEmpty())
        {            
            Date date1=new Date();
            Date date2=DateUtils.convertStringToDate(DateUtils.extractDate(o2.plannedDate));
            res=date1.compareTo(date2);
        }
        if(!o1.plannedDate.isEmpty() && o2.plannedDate.isEmpty())
        {
            Date date1=DateUtils.convertStringToDate(DateUtils.extractDate(o1.plannedDate));
            Date date2=new Date();
            res=date1.compareTo(date2);            
        }
        if(res==0)
        {
            if(!o1.finishDate.isEmpty() && !o2.finishDate.isEmpty())
            {
                Date date1=DateUtils.convertStringToDate(DateUtils.extractDate(o1.finishDate));
                Date date2=DateUtils.convertStringToDate(DateUtils.extractDate(o2.finishDate));
                res=date1.compareTo(date2);
            }
            if(o1.finishDate.isEmpty() && !o2.finishDate.isEmpty())
            {            
                Date date1=new Date();
                Date date2=DateUtils.convertStringToDate(DateUtils.extractDate(o2.finishDate));
                res=date1.compareTo(date2);
            }
            if(!o1.finishDate.isEmpty() && o2.finishDate.isEmpty())
            {
                Date date1=DateUtils.convertStringToDate(DateUtils.extractDate(o1.finishDate));
                Date date2=new Date();
                res=date1.compareTo(date2);            
            }   
        }
        if(res==0)
        {
            String name1=o1.name;
            String name2=o2.name;
            res=name1.compareToIgnoreCase(name2);
        }
        return res;
    }
    
}
