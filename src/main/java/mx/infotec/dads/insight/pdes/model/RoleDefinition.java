/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.infotec.dads.insight.pdes.model;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author victor.lorenzana
 */
public class RoleDefinition {
    public String id;
    public String title;
    public List<String> acctions=new ArrayList<>();
    public List<String> responsabilities=new ArrayList<>();
    public boolean used=false;
}
