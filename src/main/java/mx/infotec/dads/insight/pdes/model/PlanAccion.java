/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.infotec.dads.insight.pdes.model;

import javafx.beans.property.SimpleStringProperty;

/**
 *
 * @author victor.lorenzana
 */
public class PlanAccion {
    private String accion;

    public String getAccion() {
        return accion;
    }

    public void setAccion(String accion) {
        this.accion=accion;
    }
    public PlanAccion(String accion)
    {
        this.accion=accion;
    }
}
