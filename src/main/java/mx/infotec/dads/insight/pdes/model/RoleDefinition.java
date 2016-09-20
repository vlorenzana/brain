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
    private String id;
    private String title;
    private List<String> actions=new ArrayList<>();
    private List<String> responsabilities=new ArrayList<>();

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<String> getActions() {
        return actions;
    }

    public void setActions(List<String> actions) {
        this.actions = actions;
    }

    

    public List<String> getResponsabilities() {
        return responsabilities;
    }

    public void setResponsabilities(List<String> responsabilities) {
        this.responsabilities = responsabilities;
    }

    public boolean isUsed() {
        return used;
    }

    public void setUsed(boolean used) {
        this.used = used;
    }
    public boolean used=false;
}
