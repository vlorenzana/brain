/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.infotec.dads.insight.pdes.model;

import static java.util.Arrays.asList;
import java.util.List;
import mx.infotec.dads.insight.util.PQI;

/**
 *
 * @author victor.lorenzana
 */
public class PQITest {
    
    public static void main(String args[])
    {
        /*TestPQINormalPlanned();
        TestPQINormalActual();*/
        TestPQINormalPlanned2();
    }
    public static void TestPQINormalPlanned()
    {
        List<String> values=asList("1.75","0.5","0","1.65","0.29");
        PQI pqi=PQI.getPQIObjectFromStringList(values);
        assert new Double(1).equals(pqi.getDesignCodeIndex());
        assert new Double(0.58).equals(pqi.getRevDesignIndex());
        assert new Double(1).equals(pqi.getRevCodignIndex());
        assert new Double(1).equals(pqi.getCompIndex());
        assert new Double(1).equals(pqi.getTestIndex());
        
        Double resp=PQI.getPQIFromStringList(values);
        assert new Double(0.58).equals(resp);
        System.out.println("resp");
    }
    
    
    public static void TestPQINormalPlanned2()
    {
        List<String> values=asList("1.75","0.1","30","20","0.29");
        PQI pqi=PQI.getPQIObjectFromStringList(values);
        assert new Double(1).equals(pqi.getDesignCodeIndex());
        assert new Double(0.58).equals(pqi.getRevDesignIndex());
        assert new Double(0.2).equals(pqi.getRevCodignIndex());
        assert new Double(.5).equals(pqi.getCompIndex());
        assert new Double(.4).equals(pqi.getTestIndex());
        
        Double resp=PQI.getPQIFromStringList(values);
        assert new Double(0.02).equals(resp);
        System.out.println("resp");
    }
    
    public static void TestPQINormalActual()
    {
        List<String> values=asList("0.6","0.27","0","0","0.1");
        Double resp=PQI.getPQIFromStringList(values);
        assert new Double(0.06).equals(resp);
        System.out.println("resp");
    }
    
}
