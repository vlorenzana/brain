package com.danimaniarqsoft.brain.pdes.model;

import java.util.List;

/**
 * 
 * @author Daniel Cortes Pichardo
 *
 */
public class Report {

  private WeekTable        weekTable;
  private PerformanceTable performanceTable;
  private InfoReportTable  generalTable;
  private SizeTable        sizeTable;
  private List<String>     tasksInProgress;

  public Report(InfoReportTable gTable, WeekTable weekTable, PerformanceTable performanceTable,
      SizeTable sizeTable, List<String> tasksInProgress) {
    this.generalTable = gTable;
    this.weekTable = weekTable;
    this.performanceTable = performanceTable;
    this.sizeTable = sizeTable;
    this.tasksInProgress = tasksInProgress;
  }


  public PerformanceTable getPerformanceTable() {
    return performanceTable;
  }

  public void setPerformanceTable(PerformanceTable performanceTable) {
    this.performanceTable = performanceTable;
  }

  public WeekTable getWeekTable() {
    return weekTable;
  }

  public void setWeekTable(WeekTable weekTable) {
    this.weekTable = weekTable;
  }

  public InfoReportTable getGeneralTable() {
    return generalTable;
  }

  public void setGeneralTable(InfoReportTable generalTable) {
    this.generalTable = generalTable;
  }

  public SizeTable getSizeTable() {
    return sizeTable;
  }

  public void setSizeTable(SizeTable sizeTable) {
    this.sizeTable = sizeTable;
  }


  public List<String> getTasksInProgress() {
    return tasksInProgress;
  }


  public void setTasksInProgress(List<String> tasksInProgress) {
    this.tasksInProgress = tasksInProgress;
  }
}
