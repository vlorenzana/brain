package com.danimaniarqsoft.brain.pdes.model;

public enum Row {
  SEMANA_PASADA(2), A_LA_FECHA(3), PROMEDIO_SEMANA(4), TAREAS_COMPLETADAS(5);
  private int index;

  private Row(int index) {
    this.index = index;
  }

  public int getIndex() {
    return index;
  }
}
