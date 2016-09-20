/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.infotec.dads.insight.pdes.model;

/**
 *
 * @author victor.lorenzana
 */
public class RoleStatus {

    private String description;    
    private String status;
    private String responsability;
    private String id;
    
    public String getDescription() {
        return description;
    }

    public void setDescription(String desc) {
        this.description = desc;
    }
    
    

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getResponsability() {
        return responsability;
    }

    public void setResponsability(String resp) {
        this.responsability = resp;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
    
}
