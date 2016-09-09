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
    private SimpleStringProperty accion=new SimpleStringProperty();

        public String getAccion() {
            return accion.get();
        }

        public void setAccion(String accion) {
            this.accion.set(accion);
        }
        public PlanAccion(String accion)
        {
            this.accion=new SimpleStringProperty(accion);
        }
}
