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
    private List<TaskWithProblem> taskProblems;
    private List<ProductsFinished> productsFinished;
    private List<PhaseTime> timeTableFinished;
    private List<PQIElement> tablePQI;

    
    

    public Report(InfoReportTable gTable, WeekReportTable weekTable, PerformanceReportTable performanceTable,
	    SizeReportTable sizeTable, List<String> tasksInProgress,List<String> tasksCompleted,List<String> tasksNextWeek,List<TaskWithProblem> taskProblems,List<ProductsFinished> productsFinished,
            List<PhaseTime> timeTableFinished,List<PQIElement> tablePQI) {
	this.infoReportTable = gTable;
	this.weekReportTable = weekTable;
	this.performanceReportTable = performanceTable;
	this.sizeReportTable = sizeTable;
	this.tasksInProgress = tasksInProgress;
        this.tasksCompleted = tasksCompleted;
        this.tasksNextWeek = tasksNextWeek;
        this.taskProblems=taskProblems;
        this.productsFinished=productsFinished;
        this.timeTableFinished=timeTableFinished;
        this.tablePQI=tablePQI;
    }

    public List<PQIElement> getTablePQI() {
        return tablePQI;
    }

    public void setTablePQI(List<PQIElement> tablePQI) {
        this.tablePQI = tablePQI;
    }
    public List<PhaseTime> getTimeTableFinished() {
        return timeTableFinished;
    }

    public void setTimeTableFinished(List<PhaseTime> timeTableFinished) {
        this.timeTableFinished = timeTableFinished;
    }
    
    public List<ProductsFinished> getProductsFinished() {
        return productsFinished;
    }

    public void setProductsFinished(List<ProductsFinished> productsFinished) {
        this.productsFinished = productsFinished;
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

    public List<TaskWithProblem> getTaskProblems() {
        return taskProblems;
    }

    public void setTaskProblems(List<TaskWithProblem> taskProblems) {
        this.taskProblems = taskProblems;
    }
    
}
