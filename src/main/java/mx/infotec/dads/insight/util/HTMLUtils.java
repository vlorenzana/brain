/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.infotec.dads.insight.util;

import java.io.StringWriter;
import java.util.HashMap;
import org.apache.commons.lang3.StringEscapeUtils;

/**
 *
 * @author victor.lorenzana
 */
public class HTMLUtils {
    
    public static String escapeHTML(String s) {
    return StringEscapeUtils.escapeHtml4(s);
    /*StringBuilder out = new StringBuilder(Math.max(16, s.length()));
    for (int i = 0; i < s.length(); i++) {
        char c = s.charAt(i);
        if (c > 127 || c == '"' || c == '<' || c == '>' || c == '&') {
            out.append("&#");
            out.append((int) c);
            out.append(';');
        } else {
            out.append(c);
        }
    }
    return out.toString();*/
}
    
    

}
