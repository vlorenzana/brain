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
        return formatter.format(value);
    }
}
