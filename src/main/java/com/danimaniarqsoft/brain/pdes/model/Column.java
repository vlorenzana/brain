package com.danimaniarqsoft.brain.pdes.model;


public enum Column {
  HORAS_PLAN(3), HORAS_ACTUAL(4), HORAS_ACTUAL_PLAN(5), EV_PLAN(7), EV_ACTUAL(8), EV_ACTUAL_PLAN(9);
  private int index;

  private Column(int index) {
    this.index = index;
  }

  public int getIndex() {
    return index;
  }
}
