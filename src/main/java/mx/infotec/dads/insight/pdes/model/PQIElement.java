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
public class PQIElement {
    public Double pqi_planned;
    public Double pqi_actual;
    public String imagePQIPlanned;
    

    

    public Double getPqi_planned() {
        return pqi_planned;
    }

    public void setPqi_planned(Double pqi_planned) {
        this.pqi_planned = pqi_planned;
    }

    public Double getPqi_actual() {
        return pqi_actual;
    }

    public void setPqi_actual(Double pqi_actual) {
        this.pqi_actual = pqi_actual;
    }

    public String getImagePQIPlanned() {
        return imagePQIPlanned;
    }

    public void setImagePQIPlanned(String imagePQIPlanned) {
        this.imagePQIPlanned = imagePQIPlanned;
    }

    public String getImagePQIActual() {
        return imagePQIActual;
    }

    public void setImagePQIActual(String imagePQIActual) {
        this.imagePQIActual = imagePQIActual;
    }

    public String getPathToProduct() {
        return pathToProduct;
    }

    public void setPathToProduct(String pathToProduct) {
        this.pathToProduct = pathToProduct;
    }
    public String imagePQIActual;
    public String pathToProduct;
}
