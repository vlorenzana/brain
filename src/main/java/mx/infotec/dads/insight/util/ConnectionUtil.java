/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.infotec.dads.insight.util;

import java.net.URI;
import org.jsoup.Connection;
import org.jsoup.Jsoup;

/**
 *
 * @author victor.lorenzana
 */
public class ConnectionUtil {
    
    public static Connection getConnection(URI url)
    {
        return Jsoup.connect(url.toString()).timeout(Constants.TIMEOUT);
    }
    public static Connection getConnection(String url)
    {
        return Jsoup.connect(url).timeout(Constants.TIMEOUT);
    }
}
