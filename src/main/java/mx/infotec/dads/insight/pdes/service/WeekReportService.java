package mx.infotec.dads.insight.pdes.service;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.imageio.ImageIO;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import mx.infotec.dads.insight.dao.OverallMetricsDAO;
import mx.infotec.dads.insight.pdes.exceptions.ReportException;
import mx.infotec.dads.insight.pdes.model.InfoReportTable;
import mx.infotec.dads.insight.pdes.model.PQIElement;
import mx.infotec.dads.insight.pdes.model.PerformanceReportTable;
import mx.infotec.dads.insight.pdes.model.PhaseTime;
import mx.infotec.dads.insight.pdes.model.Product;
import mx.infotec.dads.insight.pdes.model.TaskWithProblem;
import mx.infotec.dads.insight.pdes.model.Report;
import mx.infotec.dads.insight.pdes.model.SizeReportTable;
import mx.infotec.dads.insight.pdes.model.TaskInfo;
import mx.infotec.dads.insight.pdes.model.WeekReportTable;
import mx.infotec.dads.insight.util.Constants;
import static mx.infotec.dads.insight.util.Constants.FILTER_FINISHED;
import mx.infotec.dads.insight.util.ContextUtil;
import mx.infotec.dads.insight.util.DateUtils;
import static mx.infotec.dads.insight.util.DateUtils.betweenWeek;
import static mx.infotec.dads.insight.util.DateUtils.getDateFromHTML;
import mx.infotec.dads.insight.util.DoubleUtils;
import mx.infotec.dads.insight.util.PQI;
import mx.infotec.dads.insight.util.ProductComparator;
import mx.infotec.dads.insight.util.UrlPd;
import static org.apache.commons.lang3.StringEscapeUtils.escapeHtml4;
import org.jsoup.Connection;
import mx.infotec.dads.insight.util.TaskWithProblemComparator;
import static mx.infotec.dads.insight.util.ConnectionUtil.getConnection;
import static mx.infotec.dads.insight.util.Constants.NO_SE_PUEDE_CALCULAR;
import static mx.infotec.dads.insight.util.DoubleUtils.computePercent;
import org.apache.commons.lang3.math.NumberUtils;
/**
 * WeekReportService
 * 
 * @author Daniel Cortes Pichardo
 *
 */
public class WeekReportService {
    
    
    private WeekReportService() {
        
    }

    
    private static final String EXTENSION = ".png";
    private static final String PATH_TASK = "/";
    /**
     * 
     * @param output
     * @param urlPd
     * @return
     * @throws ReportException 
     */
    public static Report createReport(final File output,final UrlPd urlPd) throws ReportException {
	try 
        {            
            removeFilter(FILTER_FINISHED, urlPd);
            removeFilterByPath(urlPd);            
	    Document weekReportInHTML = getConnection(urlPd.getWeekReportUrl()).get();
	    WeekReportTable table = new WeekReportTable(weekReportInHTML);
	    Element cellElement = weekReportInHTML.select("body table tbody tr td.left").get(1);
	    String parse = cellElement.text();
	    String reportDate = DateUtils.extractDate(parse);
	    String toDateReportString = DateUtils.convertPdesDate(reportDate);
	    Date toDateReportDate = DateUtils.convertStringToDate(toDateReportString);
	    Date fromDateReportDate = DateUtils.getDateInit(toDateReportDate);
	    Document mainData = getConnection(urlPd.getGeneralReportUrl()).get();
	    InfoReportTable gTable = new InfoReportTable(mainData);
	    gTable.setReportedPeriod("Del " + DateUtils.convertDateToString(fromDateReportDate) + " al "
		    + DateUtils.convertDateToString(toDateReportDate));
	    OverallMetricsDAO omDAO = new OverallMetricsDAO(urlPd);	    
            SizeReportTable sizeTable = findSizeTable(urlPd,omDAO);
	    List<String> tasksInProgress = findTasksInProgress(urlPd);
            List<String> tasksCompleted = findTasksCompleted(urlPd);
            List<String> tasksNextWeek = findTasksNext(urlPd);
            
	    PerformanceReportTable pTable = computeData(table);
	    gTable.setStatus(ContextUtil.computeStatus(pTable.getVgDiff()));
            List<TaskWithProblem> taskProblems=findTaksWithProblems(urlPd);
            List<Product> productsFinished=findProductsToDate(urlPd,toDateReportDate);
            List<PhaseTime> timeTableFinished=getTimeTableFinished(urlPd);
            List<PQIElement> tablePQI=createTablePQI(urlPd,output,productsFinished,toDateReportDate);
            
	    return new Report(gTable, table, pTable, sizeTable, tasksInProgress,tasksCompleted,tasksNextWeek,taskProblems,productsFinished,timeTableFinished,tablePQI);
	} catch (ReportException | URISyntaxException | IOException e) {
	    throw new ReportException("createReport", e);
	}

    }
    public static SizeReportTable findSizeTable(final UrlPd urlPd,final OverallMetricsDAO omDAO) throws ReportException
    {   
        try
        {
            removeFilter(FILTER_FINISHED, urlPd);
            addFilter(FILTER_FINISHED, urlPd);        
            String xpathQuery="body div form table";            
            Elements overallMetricsElements = omDAO.getOverallMetrics().select(xpathQuery);
            Element sizeTable = overallMetricsElements.get(1);
            sizeTable.addClass("table table-bordered table-striped table-responsive");
            return new SizeReportTable(sizeTable.toString());
        }
        catch(ReportException re)
        {
            throw re;
        }
        finally
        {
            removeFilter(FILTER_FINISHED, urlPd);
        }
    }
    public static List<PhaseTime> getTimeTableFinished(final UrlPd urlPd) throws ReportException
    {
        List<PhaseTime> getTimeTableFinished=new ArrayList<>();
        
        removeFilter(FILTER_FINISHED, urlPd);
        addFilter(FILTER_FINISHED,urlPd);      
        String url=urlPd.getPhaseTime().toString();
        Connection connection=getConnection(url);
        try
        {         
          Document document=connection.get();
          Connection connectionToPlanSummary=getConnection(urlPd.getOveralMetricsUrl());
          Document docPlanSummary=connectionToPlanSummary.get();
          Element table=document.select("table").get(0);        
          Elements rows=table.select("tr");
          double totalPlanned=0,totalActual=0;
          for(int i=1 ; i<rows.size();i++)
          {
              Element row=rows.get(i);
              String phaseName=row.child(0).ownText();
              String attName=phaseName+"/Estimated Time";
              String xpathQuery="input[name="+attName+"]";            
              Elements inputs=docPlanSummary.select(xpathQuery);
              if(inputs.size()>0 
                      && inputs.get(0)!=null && inputs.get(0).parent()!=null 
                      && inputs.get(0).parent().previousElementSibling()!=null 
                      && inputs.get(0).parent().previousElementSibling().text()!=null
                      )
              {
                  phaseName=inputs.get(0).parent().previousElementSibling().text();
                  
              }
              
              String timeActual=row.child(1).ownText();
              String timePlanned=row.child(2).ownText();
              if(NumberUtils.isNumber(timeActual))
              {
                  totalActual+=Double.parseDouble(timeActual);
              }
              if(NumberUtils.isNumber(timePlanned))
              {
                  totalPlanned+=Double.parseDouble(timePlanned);
              }
              PhaseTime phase=new PhaseTime();
              phase.setName(phaseName);
              phase.setActualTime(DateUtils.convertDecimalToTime(timeActual));
              phase.setPlannedTime(DateUtils.convertDecimalToTime(timePlanned));
              double dPlanned=Double.parseDouble(timePlanned);
              double dActual=Double.parseDouble(timeActual);
              phase.setPercentActual(computePercent(dPlanned, dActual));              
              getTimeTableFinished.add(phase);
          }
            PhaseTime phase=new PhaseTime();
            phase.setName("Total");
            phase.setActualTime(DateUtils.convertDecimalToTime(String.valueOf(totalActual)));
            phase.setPlannedTime(DateUtils.convertDecimalToTime(String.valueOf(totalPlanned)));            
            phase.setPercentActual(computePercent(totalPlanned, totalActual));
            getTimeTableFinished.add(phase);

        }
        catch(IOException ioe)
        {
            throw new ReportException(ioe);
        }
        finally
        {
            removeFilter(FILTER_FINISHED, urlPd);            
        }
        

        return getTimeTableFinished;
    }
    public static void addFilter(final String filter,final UrlPd urlPd) throws ReportException
    {        
        Connection connection=getConnection(urlPd.getFilter().toString());
        Map<String,String> data=new HashMap<>();
        data.put("apply","Apply Filter");
        data.put("destUri",urlPd.getDestFilterURI());
        data.put("filter",filter);
        connection.data(data);
        try
        {
          connection.post();
        }
        catch(IOException ioe)
        {
            throw new ReportException(ioe);
        }

    }
    public static void addFilterByPath(final String path,final UrlPd urlPd) throws ReportException
    {        
        Connection connection=getConnection(urlPd.getFilterByWBS().toString());
        Map<String,String> data=new HashMap<>();        
        data.put("destUri",urlPd.getDestFilterBYWBSURI());
        data.put("relPath",path);
        connection.data(data);
        try
        {
          connection.post();
        }
        catch(IOException ioe)
        {
            throw new ReportException(ioe);            
        }

    }
    public static void removeFilter(final String filter,final UrlPd urlPd) throws ReportException
    {
        
        Connection connection=getConnection(urlPd.getFilter());
        
        Map<String,String> data=new HashMap<>();
        data.put("remove","Remove Filter");
        data.put("destUri",urlPd.getDestFilterRemoveURI());
        data.put("filter",filter);
        connection.data(data);
        try
        {
          connection.post();
        }
        catch(IOException ioe)
        {
            throw new ReportException(ioe);
        }

    }
    public static void removeFilterByPath(final UrlPd urlPd) throws ReportException
    {
        
        Connection connection=getConnection(urlPd.getFilterByWBS());
        Map<String,String> data=new HashMap<>();        
        data.put("destUri",urlPd.getDestFilterBYWBSURI());
        data.put("relPath","");
        connection.data(data);
        try
        {
          connection.post();
        }
        catch(IOException ioe)
        {
            throw new ReportException(ioe);
        }

    }

    public static PerformanceReportTable computeData(final WeekReportTable table) {
	String earnedValue = computeVg(table);
	String evDiff = computeVgDiff(table);
	String evLeft = computeVgLeft(table);
	String horasTarea = computeTaskHours(table);
	String tareaCerradas = computeClosedTask(table);
	double hsTasksTerm = computeHoursTaskNotFinished(table);
	String semHrTarNoTerm = computeWeekhrsTaskNotFinished(table, hsTasksTerm);
	double evhxH = computeVgXh(table);
	String evNoPerformed = computeEvNotPerformed(evhxH, hsTasksTerm);
	String recoveryWeeks = computeRecoveryWeeks(table);
        String recoveryPercent=computeRecoveryPercent(table);
        String recoveryHr=computeRecoveryHr(table,recoveryWeeks);
        
	return PerformanceReportTable.getBuilder().withVg(earnedValue).withVgDiff(evDiff).withVgFalta(evLeft)
		.withTaskHours(horasTarea).withTaskClosed(tareaCerradas)
		.withHoursNotFinished(DoubleUtils.formatToDigits(hsTasksTerm)).withWeekHrsNotFinished(semHrTarNoTerm)
		.withVgPerHour(DoubleUtils.formatToDigits(evhxH)).withVgNotPerformed(evNoPerformed).withRecovery(recoveryWeeks).withRecoveryPercent(recoveryPercent)
                .withRecoveryHr(recoveryHr)
                .build();
        
    }
    public static Date getStartReport(final UrlPd urlPd) throws IOException, URISyntaxException,ReportException
    {
        Document doc = getConnection(urlPd.getWeekReportUrl()).get();
        Elements elements=doc.select("h2");
        if(elements.size()>0)
        {
            Element h2=elements.get(0);
            String text=h2.textNodes().get(0).text().trim();
            return getDateFromHTML(text);           
        }
        return new Date();
    }
    private static String getPath(final String id,final Document doc)
    {
        String getPath="";        
        int pos=id.lastIndexOf("-");
        if(pos!=-1)
        {
            String idParent=id.substring(0,pos);
            Elements elements=doc.select("tr[id="+ id +"]");
            if(elements.size()>0)
            {
                Element tr=elements.get(0);                
                String productName=tr.child(0).child(0).ownText();
                return getPath(idParent,doc)+PATH_TASK+productName;
            }
        }
        return getPath;
    }
    
    private static List<Product> findProductsToDate(final UrlPd urlPd,final Date endWeek) throws IOException, URISyntaxException,ReportException
    {   
        List<Product> findProductsFinished=new ArrayList<>();        
        Map<String,TaskInfo> products=new HashMap<>();
        Document doc = getConnection(urlPd.getSummary()).get();
        Elements renglones=doc.select("body table[id=snip15_task] tr");
        for(int index=2;index<renglones.size();index++)
        {
            Element tr=renglones.get(index);
            if(tr.children().size()>8)
            {
                String id=tr.attr("id");
                String parentId=id.substring(0,id.lastIndexOf("-"));
                String productName=tr.child(0).child(0).ownText();
                String type=tr.child(1).ownText();
                String planned=tr.child(5).ownText(); // plannedDate
                String finished=tr.child(7).ownText().trim(); // endDate
                String hito=tr.child(8).ownText();
                if(type.isEmpty() && !planned.isEmpty())
                {
                    products.remove(parentId);
                    Date plannedDate = DateUtils.convertStringToDate(DateUtils.extractDate(planned));                    
                    if(plannedDate.before(endWeek) || plannedDate.equals(endWeek))
                    {                        
                        TaskInfo task=new TaskInfo();
                        task.setId(id);
                        task.setProductName(escapeHtml4(productName));
                        task.setHito(hito);                        
                        task.setEndDate(finished);
                        task.setPlannedDate(planned);
                        task.setStatus("before");
                        products.put(id, task);
                    }
                    if(!finished.isEmpty())
                    {
                        Date finishedDate = DateUtils.convertStringToDate(DateUtils.extractDate(finished));                    
                        if(finishedDate.before(endWeek) || finishedDate.equals(endWeek))
                        {
                            TaskInfo task=new TaskInfo();
                            task.setId(id);
                            task.setProductName(escapeHtml4(productName));
                            task.setHito(hito);
                            task.setEndDate(finished);
                            task.setPlannedDate(planned);
                            if(betweenWeek(finishedDate, endWeek))
                            {
                                task.setStatus("finishedToDate");
                            }
                            else
                            {
                                task.setStatus("finished");
                            }                            
                            products.put(id, task);
                        }
                    }
                }
            }
        }
        for(String key : products.keySet())
        {
            TaskInfo info=products.get(key);
            Product product=new Product();
            product.setName(info.getProductName());
            product.setFinishDate(info.getEndDate());
            product.setPlannedDate(info.getPlannedDate());
            String path=getPath(info.getId(), doc);
            if(path.startsWith(PATH_TASK))
            {
                path=path.substring(1);
            }
            product.setPath(path);
            product.setStatus(info.getStatus());
            findProductsFinished.add(product);
        }
        Collections.sort(findProductsFinished, new ProductComparator());
        return findProductsFinished;
    }
    private static BufferedImage getImage(final UrlPd urlPd,final int index) throws IOException, URISyntaxException,ReportException 
    {
        Document doc = getConnection(urlPd.getSummaryToPQI()).get();
        Elements images=doc.select("body div form img");
        int currentImage=-1;
        for(int i=0;i<images.size();i++)
        {
            Element imageElement=images.get(i);
            if(imageElement.nextElementSibling()!=null && "map".equals(imageElement.nextElementSibling().tagName()))
            {
                String innerHTML=imageElement.nextElementSibling().html();
                if(innerHTML.contains("chart=radar"))
                {
                    currentImage++;
                    if(currentImage==index)
                    {
                        String src=imageElement.attr("src");
                        BufferedImage image=ImageIO.read(urlPd.getImageFromCacheUrl(src).toURL());
                        return image;
                    }
                }
            }
        }
        return null;
    }
    private static Double getPQIProductPlanned(final UrlPd urlPd) throws ReportException,URISyntaxException,IOException
    {  
        return getPQIProductActual(urlPd,"quality%2FestProfile.rpt");
    }
    private static Double getPQIProductActual(final UrlPd urlPd) throws ReportException,URISyntaxException,IOException
    {       
        return getPQIProductActual(urlPd,"quality%2FactProfile.rpt");
    }
    private static Double getPQIProductActual(final UrlPd urlPd,final String param) throws ReportException,URISyntaxException,IOException
    {
        Document doc = getConnection(urlPd.getURLPQITable().toString()+param).get();
        Elements rows=doc.select("table tr");
        
        for(int index=1;index<rows.size();index++)
        {
            List<Double> values=new ArrayList<>();
            Element tr=rows.get(index);
            for(int j=1;j<tr.children().size();j++)
            {
                Element node=tr.child(j);
                String data=node.text();
                try
                {
                    Double doubleData=Double.parseDouble(data);
                    values.add(doubleData);
                }
                catch(NumberFormatException nfe)
                {                    
                    values.add(Double.NaN);
                }
            }            
            if(hasPQI(values))
            {
                return PQI.getPQIFromDoubleList(values);                
            }
            else
            {
                return null;
            }
        }
        return null;        
    }
    private static boolean hasPQI(final List<Double> data)
    {
        if(data.size()!=5)
        {
            return false;
        }        
        for(Double d : data)
        {
            if(Double.isNaN(d))
            {
                return false;
            }
        }
        return true;
    }
    private static List<PQIElement> createTablePQI(final UrlPd urlPd,final File output,final List<Product> products,final Date endWeek) throws ReportException,URISyntaxException
    {
        List<PQIElement> createTablePQI=new ArrayList<>();
        try
        {        
            int index=0;
            for(Product product : products)
            {
                if(!product.getFinishDate().isEmpty())
                {
                    Date finished=getDateFromHTML(product.getFinishDate());                    
                    if(betweenWeek(finished,endWeek))                        
                    {
                        index++;
                        addFilterByPath(product.getPath(), urlPd);                        
                        Double pqi_planned=getPQIProductPlanned(urlPd);
                        Double pqi_actual=getPQIProductActual(urlPd);
                        if(pqi_actual!=null && pqi_planned!=null)                        
                        {
                            PQIElement element=new PQIElement();
                            element.setPathToProduct(escapeHtml4(product.getPath()));
                            createTablePQI.add(element);
                            element.setPqi_actual(pqi_actual);
                            element.setPqi_planned(pqi_planned);
                            element.setImagePQIActual("");
                            element.setImagePQIPlanned("");                           
                            BufferedImage pqi_planned_image=getImage(urlPd,0); 
                            if(pqi_planned_image!=null)
                            {                    
                                String name="pqi_planned_"+index;
                                element.setImagePQIPlanned(Constants.REPORT_IMG_FOLDER+File.separator+name+EXTENSION);
                                ContextUtil.saveImageToDisk(pqi_planned_image, output, name);                
                            }
                            BufferedImage pqi_actual_image=getImage(urlPd,1);
                            if(pqi_actual_image!=null)
                            {                    
                                String name="pqi_actual_"+index;
                                element.setImagePQIActual(Constants.REPORT_IMG_FOLDER+File.separator+name+EXTENSION);
                                ContextUtil.saveImageToDisk(pqi_actual_image, output, name);                
                            }
                        }
                    }
                }
            }        
        }
        catch(IOException e)
        {
            throw new ReportException(e);
        }
        finally
        {
            removeFilterByPath(urlPd);            
        }
        return createTablePQI;
    }
    
    
    private static List<String> findTasksInProgress(final UrlPd urlPd) throws IOException, URISyntaxException {
	Document doc = getConnection(urlPd.getWeekReportUrl().toString()).get();	
        Elements tasksList = doc.select("table[id=$$$_progress]");
	if (!tasksList.isEmpty()) {
	    Elements task = tasksList.get(0).select("td.left");
	    List<String> tasksInProgress = new ArrayList<>();
	    for (Element element : task) {
		tasksInProgress.add(element.text());
	    }
	    return tasksInProgress;
	} else {
	    return new ArrayList<>();
	}

    }
    private static List<String> findTasksCompleted(final UrlPd urlPd) throws IOException, URISyntaxException {
	Document doc = getConnection(urlPd.getWeekReportUrl()).get();
	Elements tasksList = doc.select("[id=$$$_comp] tr");
	if (!tasksList.isEmpty()) {
	    Elements task = tasksList.select("td.left");
	    List<String> taskCompleted = new ArrayList<>();
	    for (Element element : task) {
		taskCompleted.add(element.text());
	    }
	    return taskCompleted;
	} else {
	    return new ArrayList<>();
	}

    }
    private static List<TaskWithProblem> findTaksWithProblems(final UrlPd urlPd) throws IOException, URISyntaxException
    {
        List<TaskWithProblem> findProductsWithProblems=new ArrayList<>();
        Document document = getConnection(urlPd.getWeekReportUrl().toString()).get();        
        Elements rows=document.select("table[id=$$$_progress] tr");
        for(int index=1;index<rows.size();index++)
        {
            Element tr=rows.get(index);
            if(tr.childNodes().size()>10)
            {
                String taskName=tr.child(0).ownText();
                String used=tr.child(5).ownText().replace('%', ' ').trim();
                String left=tr.child(10).ownText();
                String date=tr.child(6).ownText();
                if(!taskName.isEmpty())
                {
                    if(!used.isEmpty())
                    {
                        int iUsed=Integer.parseInt(used);
                        if(iUsed>100)
                        {                            
                            TaskWithProblem task=new TaskWithProblem(taskName,tr.child(7).text(),date);
                            findProductsWithProblems.add(task);
                        }
                        else if(left!=null && left.startsWith("-"))
                        {
                            TaskWithProblem task=new TaskWithProblem(taskName,tr.child(7).text(),date);
                            findProductsWithProblems.add(task);
                        }
                    }
                    else if(left!=null && left.startsWith("-"))
                    {
                        TaskWithProblem task=new TaskWithProblem(taskName,tr.child(7).text(),date);
                        findProductsWithProblems.add(task);

                    }

                }
            }
        } 
        Collections.sort(findProductsWithProblems, new TaskWithProblemComparator());
        return findProductsWithProblems;
    }
    private static List<String> findTasksNext(final UrlPd urlPd) throws IOException, URISyntaxException {
	Document doc = getConnection(urlPd.getWeekReportUrl().toString()).get();	
        Elements tasksList = doc.select("table[id=$$$_due] tr");
	if (!tasksList.isEmpty()) {
            List<String> taskNextWeek = new ArrayList<>();
            Elements task = tasksList.select("td.left");                
            for (Element element : task) {
                taskNextWeek.add(element.text());
            }
	    return taskNextWeek;
	} else {
	    return new ArrayList<>();
	}

    }

    public static String computeVg(final WeekReportTable table) {
        if(Double.isNaN(table.getDoubleProperty(1, 4)))
        {
            return NO_SE_PUEDE_CALCULAR;
        }
	return DoubleUtils.formatToDigits(table.getDoubleProperty(1, 4));
    }

    public static String computeVgDiff(final WeekReportTable table) {
        if(Double.isNaN(table.getDoubleProperty(1, 4)) || Double.isNaN(table.getDoubleProperty(1, 3)))
        {
            return NO_SE_PUEDE_CALCULAR;
        }
	return DoubleUtils.formatToDigits(table.getDoubleProperty(1, 4) - table.getDoubleProperty(1, 3));
    }

    public static String computeVgLeft(final WeekReportTable table) {
	return DoubleUtils.formatToDigits((1 - table.getDoubleProperty(1, 5)) * 100);
    }

    public static String computeTaskHours(final WeekReportTable table) {
	return DoubleUtils.formatToDigits(table.getDoubleProperty(1, 1));
    }

    public static String computeClosedTask(final WeekReportTable table) {
	return DoubleUtils.formatToDigits((table.getDoubleProperty(3, 2) * 100) - 100);
    }

    public static double computeHoursTaskNotFinished(final WeekReportTable table) {
	return table.getDoubleProperty(1, 1) - table.getDoubleProperty(3, 1);
    }

    public static String computeWeekhrsTaskNotFinished(final WeekReportTable table, final double hsTareasTerm) {
	return DoubleUtils.formatToDigits(hsTareasTerm / table.getDoubleProperty(2, 1));
    }

    public static double computeVgXh(final WeekReportTable table) {
        if(table.getDoubleProperty(3, 1)==0)
        {
            return Double.NaN;
        }
	return table.getDoubleProperty(1, 4) / table.getDoubleProperty(3, 1);
    }

    public static String computeEvNotPerformed(final double vhxH, final double hsTareasTerm) {
        if(Double.isNaN(vhxH) || Double.isNaN(hsTareasTerm))
        {
            return NO_SE_PUEDE_CALCULAR;
        }
	return DoubleUtils.formatToDigits(vhxH * hsTareasTerm);
    }

    public static String computeRecoveryWeeks(final WeekReportTable table) {
        if(table.getDoubleProperty(2, 4)==0 || Double.isNaN(table.getDoubleProperty(1, 3)) || Double.isNaN(table.getDoubleProperty(1, 4)) || Double.isNaN(table.getDoubleProperty(2, 4)))
        {
            return "No se puede calcular";
        }
        double weeks=(table.getDoubleProperty(1, 3) - table.getDoubleProperty(1, 4)) / table.getDoubleProperty(2, 4);
        weeks*=-1;        
	return weeks>0 ? "+"+DoubleUtils.formatToDigits(weeks): DoubleUtils.formatToDigits(weeks);
    }

    public static String computeRecoveryPercent(final WeekReportTable table) {
        double actualVsPlan=table.getDoubleProperty(1, 5);
        return actualVsPlan>1 ? "+"+DoubleUtils.formatToDigits((Math.abs(1-actualVsPlan))*100) : "-"+DoubleUtils.formatToDigits((1-actualVsPlan)*100);
    }
    public static String computeRecoveryHr(final WeekReportTable table,final String weeks) {
        if(!NumberUtils.isNumber(weeks))
        {
            return NO_SE_PUEDE_CALCULAR;
        }
        double averagePerWeekToDate=table.getDoubleProperty(2, 1);
        double dWeeks=Double.parseDouble(weeks);
        double hr=averagePerWeekToDate * dWeeks;
        return hr>0 ? "+"+DoubleUtils.formatToDigits(hr) : DoubleUtils.formatToDigits(hr);
    }
    
}
