package com.danimaniarqsoft.brain.pdes.model;

import java.io.Serializable;

/**
 * 
 * @author Daniel Cortes Pichardo
 *
 */
public class TableWrapper implements Serializable {
  private static final long serialVersionUID = 4533821364231259733L;
  private double[][]        doubleValues;
  private String[][]        stringValues;

  public TableWrapper(double[][] doubleValues, String[][] stringValues) {
    this.doubleValues = doubleValues;
    this.stringValues = stringValues;
  }

  public String[][] getStringValues() {
    return stringValues;
  }

  public double[][] getDoubleValues() {
    return doubleValues;
  }
}
