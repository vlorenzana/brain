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
public class InfoPQI {
 
    private String product="";
    private Double pqiActual;
    private String accion="";
    
    public String getProduct() {
        return product;
    }

    public void setProduct(String product) {
        this.product = product;
    }

    public String getAccion() {
        return accion;
    }

    public void setAccion(String accion) {
        this.accion = accion;
    }
    
    
    

    public Double getPqiActual() {
        return pqiActual;
    }

    public void setPqiActual(Double pqiActual) {
        this.pqiActual = pqiActual;
    }
    
}
