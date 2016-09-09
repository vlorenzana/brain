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
public class PhaseTime {
    
    public String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPlannedTime() {
        return plannedTime;
    }

    public void setPlannedTime(String plannedTime) {
        this.plannedTime = plannedTime;
    }

    public String getActualTime() {
        return actualTime;
    }

    public void setActualTime(String actualTime) {
        this.actualTime = actualTime;
    }

    public String getPercentActual() {
        return percentActual;
    }

    public void setPercentActual(String percentActual) {
        this.percentActual = percentActual;
    }
    public String plannedTime;
    public String actualTime;
    public String percentActual;
}
