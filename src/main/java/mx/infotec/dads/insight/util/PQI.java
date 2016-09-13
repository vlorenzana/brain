/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.infotec.dads.insight.util;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author victor.lorenzana
 */
public class PQI {
    
    private Double designCode;
    private Double revDesign;

    
    public PQI(Double designCode, Double revDesign, Double revCodign, Double testDefects, Double compDefects) 
    {
        this.designCode = designCode;
        this.revDesign = revDesign;
        this.revCodign = revCodign;
        this.testDefects = testDefects;
        this.compDefects = compDefects;
    }
    private Double revCodign;
    private Double testDefects;

    
    public Double getDesignCode() {
        return designCode;
    }

    public void setDesignCode(Double designCode) {
        this.designCode=designCode;   
    }
    public Double getDesignCodeIndex()
    {
        return designCode>1?1.0:designCode;    
    }

    public Double getRevDesign() {
        return revDesign;
    }
    public Double getPQIIndex()
    {
        return getDesignCodeIndex()*getRevDesignIndex()*getRevCodignIndex()*getCompIndex()*getTestIndex();
    }
    public Double getTestIndex()
    {
        
        if(this.testDefects==Double.NaN)
        {
            return Double.NaN;
        }
        else
        {
            Double def_test_index=10/(5+this.testDefects);
            def_test_index=def_test_index>1 ? 1 : def_test_index;
            return def_test_index;
        }
        
    }
    public Double getCompIndex()
    {        
        if(compDefects==Double.NaN)
        {
            return Double.NaN;
        }
        else
        {
            Double def_comp_index=20/(10+compDefects);
            def_comp_index=def_comp_index>1 ? 1 : def_comp_index;
            return def_comp_index;
        }
        
    }

    public void setRevDesign(Double revDesign) {
        this.revDesign=revDesign;
    }

    public Double getRevCodignIndex()
    {
        return revCodign*2>1?1.0:revCodign*2;        
    }
    public Double getRevDesignIndex()
    {
        return revDesign*2>1?1.0:revDesign*2;        
    }
    public Double getRevCodign() {
        return revCodign;
    }

    public void setRevCodign(Double revCodign) {      
      this.revCodign=revCodign;
    }

    public Double getTestDefects() {
        return testDefects;
    }

    public void setTestDefects(Double testDefects) {
        this.testDefects = testDefects;
    }

    public Double getCompDefects() {
        return compDefects;
    }

    public void setCompDefects(Double cmpDefects) {
        this.compDefects = cmpDefects;
    }
    private Double compDefects;
    
    public static PQI getPQIObjectFromStringList(List<String> data)
    {
        List<Double> values=convertDoubleFromString(data);
        return getPQIObjectFromDoubleList(values);
    }
    private static Double getCodeDesign(List<Double> data)
    {
        return data.get(0);
    }
    private static Double getRevCoding(List<Double> data)
    {
        return data.get(1);
    }
    private static Double getDefComp(List<Double> data)
    {
        return data.get(2);
    }
    private static Double getDefTest(List<Double> data)
    {
        return data.get(3);
    }
    private static Double getRevDesign(List<Double> data)
    {
        return data.get(4);
    }
    public static PQI getPQIObjectFromDoubleList(List<Double> values)
    {        
        return new PQI(getCodeDesign(values),getRevDesign(values),getRevCoding(values),getDefTest(values),getDefComp(values));
    }
    
    private static List<Double> convertDoubleFromString(List<String> data)
    {
        List<Double> values=new ArrayList<>();
        if(data.isEmpty() || data.size()!=5)
        {
            return null;
        }
        for(String value : data)
        {
            try
                {
                    Double doubleData=Double.parseDouble(value);
                    values.add(doubleData);
                }
                catch(NumberFormatException nfe)
                {                    
                    values.add(Double.NaN);
                }
        }
        return values;
    }
    public static Double getPQIFromStringList(List<String> data)
    {
        return getPQIFromDoubleList(convertDoubleFromString(data));
    }
    public static Double getPQIFromDoubleList(List<Double> values)
    {
        if(hasPQI(values))
        {
            PQI pqi=getPQIObjectFromDoubleList(values);                    
            return pqi.getPQIIndex();            
        }
        else
        {
            return null;
        }        
    }
    private static boolean hasPQI(List<Double> data)
    {
        if(data.size()!=5)
        {
            return false;
        }        
        for(Double d : data)
        {
            if(Double.isNaN(d))
            {
                return false;
            }
        }
        return true;
    }
}
