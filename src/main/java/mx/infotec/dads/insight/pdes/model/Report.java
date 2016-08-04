package mx.infotec.dads.insight.pdes.model;

import java.util.ArrayList;
import java.util.List;
import mx.infotec.dads.insight.util.HTMLUtils;
/**
 * Clase que encapsulta todo el reporte
 * 
 * @author Daniel Cortes Pichardo
 *
 */
public class Report {

    private WeekReportTable weekReportTable;
    private PerformanceReportTable performanceReportTable;
    private InfoReportTable infoReportTable;
    private SizeReportTable sizeReportTable;
    private List<String> tasksInProgress;
    private List<String> tasksCompleted;
    private List<String> tasksNextWeek;

    public Report(InfoReportTable gTable, WeekReportTable weekTable, PerformanceReportTable performanceTable,
	    SizeReportTable sizeTable, List<String> tasksInProgress,List<String> tasksCompleted,List<String> tasksNextWeek) {
	this.infoReportTable = gTable;
	this.weekReportTable = weekTable;
	this.performanceReportTable = performanceTable;
	this.sizeReportTable = sizeTable;
	this.tasksInProgress = tasksInProgress;
        this.tasksCompleted = tasksCompleted;
        this.tasksNextWeek = tasksNextWeek;
    }

    public WeekReportTable getWeekReportTable() {
	return weekReportTable;
    }

    public void setWeekReportTable(WeekReportTable weekReportTable) {
	this.weekReportTable = weekReportTable;
    }

    public PerformanceReportTable getPerformanceReportTable() {
	return performanceReportTable;
    }

    public void setPerformanceReportTable(PerformanceReportTable performanceReportTable) {
	this.performanceReportTable = performanceReportTable;
    }

    public InfoReportTable getInfoReportTable() {
	return infoReportTable;
    }

    public void setInfoReportTable(InfoReportTable infoReportTable) {
	this.infoReportTable = infoReportTable;
    }

    public SizeReportTable getSizeReportTable() {
	return sizeReportTable;
    }

    public void setSizeReportTable(SizeReportTable sizeReportTable) {
	this.sizeReportTable = sizeReportTable;
    }

    public List<String> getTasksInProgress() {        
	return tasksInProgress;
    }
    public List<String> getTasksInProgressToHTml()
    {
        List<String> tasks=new ArrayList<String>();
        for(String task : tasksInProgress)
        {
            tasks.add(HTMLUtils.escapeHTML(task));
        }
        return tasks;
    }
    public List<String> getTasksCompleted() {        
	return tasksCompleted;
    }
    public List<String> getTasksCompletedToHTML()
    {
        List<String> tasks=new ArrayList<String>();
        for(String task : tasksCompleted)
        {
            tasks.add(HTMLUtils.escapeHTML(task));
        }
        return tasks;
    }
    public List<String> getTasksNextWeek() {        
	return tasksNextWeek;
    }
    public List<String> getTasksNextWeekToHTML()
    {
        List<String> tasks=new ArrayList<String>();
        for(String task : tasksNextWeek)
        {
            tasks.add(HTMLUtils.escapeHTML(task));
        }
        return tasks;
    }

    public void setTasksInProgress(List<String> tasksInProgress) {        
	this.tasksInProgress = tasksInProgress;
    }
}
