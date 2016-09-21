/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.infotec.dads.insight.util;

import java.text.DecimalFormat;
import java.text.NumberFormat;

/**
 *
 * @author victor.lorenzana
 */
public class DoubleUtils {
    private static NumberFormat formatter = new DecimalFormat("#0.00");   
    
    public static String formatToDigits(double value)
    {
        if(Double.isNaN(value))
        {
            return Constants.NO_SE_PUEDE_CALCULAR;
        }
        return formatter.format(value);
    }
    public static String computePercent(double planned,double actual)
    {
        double percent=0;
        if(planned!=0)
        {
            percent=actual/planned;
        }  
        return DoubleUtils.formatToDigits(percent)+"%";
    }
}
