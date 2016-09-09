package mx.infotec.dads.insight.pdes.service;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.imageio.ImageIO;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import mx.infotec.dads.insight.dao.OverallMetricsDAO;
import mx.infotec.dads.insight.pdes.exceptions.ReportException;
import mx.infotec.dads.insight.pdes.model.InfoReportTable;
import mx.infotec.dads.insight.pdes.model.PQIElement;
import mx.infotec.dads.insight.pdes.model.PerformanceReportTable;
import mx.infotec.dads.insight.pdes.model.PhaseTime;
import mx.infotec.dads.insight.pdes.model.ProductsFinished;
import mx.infotec.dads.insight.pdes.model.TaskWithProblem;
import mx.infotec.dads.insight.pdes.model.Report;
import mx.infotec.dads.insight.pdes.model.SizeReportTable;
import mx.infotec.dads.insight.pdes.model.TaskInfo;
import mx.infotec.dads.insight.pdes.model.WeekReportTable;
import mx.infotec.dads.insight.util.Constants;
import static mx.infotec.dads.insight.util.Constants.FILTER_FINISHED;
import mx.infotec.dads.insight.util.ContextUtil;
import mx.infotec.dads.insight.util.DateUtils;
import mx.infotec.dads.insight.util.DoubleUtils;
import mx.infotec.dads.insight.util.UrlPd;
import static org.apache.commons.lang3.StringEscapeUtils.escapeHtml4;
import org.jsoup.Connection;

/**
 * WeekReportService
 * 
 * @author Daniel Cortes Pichardo
 *
 */
public class WeekReportService {
    
    
    private WeekReportService() {

    }

    /**
     * 
     * @param urlPd
     * @return
     * @throws IOException
     * @throws URISyntaxException
     */
    public static Report createReport(File output,final UrlPd urlPd) throws ReportException {
	try {
            removeFilter(FILTER_FINISHED, urlPd);
            removeFilterByPath(urlPd);            
	    Document doc = Jsoup.connect(urlPd.getWeekReportUrl().toString()).get();
	    WeekReportTable table = new WeekReportTable(doc);
	    Element element = doc.select("body table tbody tr td.left").get(1);
	    String parse = element.text();
	    String reportDate = DateUtils.extractDate(parse);
	    String toDateReportString = DateUtils.convertPdesDate(reportDate);
	    Date toDateReportDate = DateUtils.convertStringToDate(toDateReportString);
	    Date fromDateReportDate = DateUtils.moveDays(toDateReportDate, -6);
	    Document mainData = Jsoup.connect(urlPd.getGeneralReportUrl().toString()).get();
	    InfoReportTable gTable = new InfoReportTable(mainData);
	    gTable.setReportedPeriod("Del " + DateUtils.convertDateToString(fromDateReportDate) + " al "
		    + DateUtils.convertDateToString(toDateReportDate));
	    //OverallMetricsDAO omDAO = new OverallMetricsDAO(urlPd);
	    //SizeReportTable sizeTable = omDAO.findSizeTable("body div form table");
            SizeReportTable sizeTable = findSizeTable(urlPd);
	    List<String> tasksInProgress = findTasksInProgress(urlPd);
            List<String> tasksCompleted = findTasksCompleted(urlPd);
            List<String> tasksNextWeek = findTasksNext(urlPd);
            
	    PerformanceReportTable pTable = computeData(table);
	    gTable.setStatus(ContextUtil.computeStatus(pTable.getVgDiff()));
            List<TaskWithProblem> taskProblems=findTaksWithProblems(urlPd);
            List<ProductsFinished> productsFinished=findProductsFinished(urlPd);
            List<PhaseTime> timeTableFinished=getTimeTableFinished(urlPd);
            List<PQIElement> tablePQI=createTablePQI(urlPd,output,productsFinished);
            
	    return new Report(gTable, table, pTable, sizeTable, tasksInProgress,tasksCompleted,tasksNextWeek,taskProblems,productsFinished,timeTableFinished,tablePQI);
	} catch (Exception e) {
	    throw new ReportException("createReport", e);
	}

    }
    public static SizeReportTable findSizeTable(UrlPd urlPd) throws ReportException
    {   
        try
        {
            removeFilter(FILTER_FINISHED, urlPd);
            addFilter(FILTER_FINISHED, urlPd);        
            String xpathQuery="body div form table";
            OverallMetricsDAO omDAO = new OverallMetricsDAO(urlPd);
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
    public static List<PhaseTime> getTimeTableFinished(UrlPd urlPd) throws ReportException
    {
        List<PhaseTime> getTimeTableFinished=new ArrayList<>();
        
        removeFilter(FILTER_FINISHED, urlPd);
        addFilter(FILTER_FINISHED,urlPd);      
        String url=urlPd.getPhaseTime().toString();
        Connection con=Jsoup.connect(url);
        try
        {         
          Document document=con.post();
          Element table=document.select("table").get(0);        
          Elements rows=table.select("tr");
          for(int i=1 ; i<rows.size();i++)
          {
              Element row=rows.get(i);
              String phaseName=row.child(0).ownText();
              String timeActual=row.child(1).ownText();
              String timePlanned=row.child(2).ownText();
              PhaseTime phase=new PhaseTime();
              phase.name=phaseName;
              phase.actualTime=DateUtils.convertDecimalToTime(timeActual);
              phase.plannedTime=DateUtils.convertDecimalToTime(timePlanned);
              phase.percentActual=timePlanned.isEmpty() || timePlanned.equals("0")? "0%":((Double.parseDouble(timeActual)-Double.parseDouble(timePlanned))/Double.parseDouble(timeActual))+"%";
              getTimeTableFinished.add(phase);
          }

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
    public static void addFilter(String filter,UrlPd urlPd) throws ReportException
    {        
        Connection con=Jsoup.connect(urlPd.getFilter().toString());
        Map<String,String> data=new HashMap<>();
        data.put("apply","Apply Filter");
        data.put("destUri",urlPd.getDestURI());
        data.put("filter",filter);
        con.data(data);
        try
        {
          Document document=con.post();
        }
        catch(IOException ioe)
        {
            ioe.printStackTrace();
        }

    }
    public static void addFilterByPath(String path,UrlPd urlPd) throws ReportException
    {        
        Connection con=Jsoup.connect(urlPd.getFilterByWBS().toString());
        Map<String,String> data=new HashMap<>();        
        data.put("destUri",urlPd.getDestURI());
        data.put("relPath",path);
        con.data(data);
        try
        {
          Document document=con.post();
        }
        catch(IOException ioe)
        {
            ioe.printStackTrace();
        }

    }
    public static void removeFilter(String filter,UrlPd urlPd) throws ReportException
    {
        
        Connection con=Jsoup.connect(urlPd.getFilter().toString());
        Map<String,String> data=new HashMap<>();
        data.put("remove","Remove Filter");
        data.put("destUri",urlPd.getDestURI());
        data.put("filter",filter);
        con.data(data);
        try
        {
          Document document=con.post();
        }
        catch(IOException ioe)
        {
            ioe.printStackTrace();
        }

    }
    public static void removeFilterByPath(UrlPd urlPd) throws ReportException
    {
        
        Connection con=Jsoup.connect(urlPd.getFilterByWBS().toString());
        Map<String,String> data=new HashMap<>();
        
        data.put("destUri",urlPd.getDestURI());
        data.put("relPath","");
        con.data(data);
        try
        {
          Document document=con.post();
        }
        catch(IOException ioe)
        {
            ioe.printStackTrace();
        }

    }

    public static PerformanceReportTable computeData(WeekReportTable table) {
	String vg = computeVg(table);
	String vgDiff = computeVgDiff(table);
	String vgFalta = computeVgFalta(table);
	String horasTarea = computeTaskHours(table);
	String tareaCerradas = computeClosedTask(table);
	double hsTareasTerm = computeHoursTaskNotFinished(table);
	String semHrTarNoTerm = computeWeekhrsTaskNotFinished(table, hsTareasTerm);
	double vhxH = computeVgXh(table);
	String vgNoRe = computeVgNotPerformed(vhxH, hsTareasTerm);
	String recup = computeRecoveryWeeks(table);
        String recovery_percent=computeRecoveryPercent(table);
        String recovery_hr=computeRecoveryHr(table,recup);
        
	return PerformanceReportTable.getBuilder().withVg(vg).withVgDiff(vgDiff).withVgFalta(vgFalta)
		.withTaskHours(horasTarea).withTaskClosed(tareaCerradas)
		.withHoursNotFinished(DoubleUtils.formatToDigits(hsTareasTerm)).withWeekHrsNotFinished(semHrTarNoTerm)
		.withVgPerHour(DoubleUtils.formatToDigits(vhxH)).withVgNotPerformed(vgNoRe).withRecovery(recup).withRecoveryPercent(recovery_percent)
                .withRecoveryHr(recovery_hr)
                .build();
        
    }
    private static Date getStartReport(UrlPd urlPd) throws IOException, URISyntaxException,ReportException
    {
        Document doc = Jsoup.connect(urlPd.getWeekReportUrl().toString()).get();
        Elements elements=doc.select("h2");
        if(elements.size()>0)
        {
            Element h2=elements.get(0);
            String text=h2.textNodes().get(0).text().trim();
            int pos=text.lastIndexOf(" ");
            if(pos>0)
            {
                char c=(char)160;
                String dateString=text.substring(pos).trim();  
                StringBuilder sb=new StringBuilder(dateString);
                int pos2=dateString.indexOf(c);
                sb.replace(pos2, dateString.length()-1, "");
                return DateUtils.convertStringToDate(DateUtils.extractDate(sb.toString()));
            }
        }
        return new Date();
    }
    private static String getPath(String id,Document doc)
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
                return getPath(idParent,doc)+"/"+productName;
            }
        }
        return getPath;
    }
    private static List<ProductsFinished> findProductsFinished(UrlPd urlPd) throws IOException, URISyntaxException,ReportException
    {        
        Date end=getStartReport(urlPd);
        Calendar endCal=Calendar.getInstance();
        endCal.setTime(end);
        endCal.add(Calendar.DATE, -7);
        Date init=endCal.getTime();
        List<ProductsFinished> findProductsFinished=new ArrayList<>();        
        Map<String,TaskInfo> products=new HashMap<>();
        Document doc = Jsoup.connect(urlPd.getSummary().toString()).get();
        Elements renglones=doc.select("body table[name=TASK] tr");
        for(int i=2;i<renglones.size();i++)
        {
            Element tr=renglones.get(i);
            if(tr.children().size()>8)
            {
                String id=tr.attr("id");
                String parentId=id.substring(0,id.lastIndexOf("-"));
                String productName=tr.child(0).child(0).ownText();
                String type=tr.child(1).ownText();
                String date=tr.child(7).ownText();
                String hito=tr.child(8).ownText();

                //System.out.println("date: "+date);

                if(type.isEmpty() && !date.isEmpty())
                {
                    products.remove(parentId);
                    Date finished = DateUtils.convertStringToDate(DateUtils.extractDate(date));
                    if((finished.after(init) || finished.equals(init)) && (finished.before(end) || finished.equals(end)))
                    {                        
                        TaskInfo task=new TaskInfo();
                        task.id=id;
                        task.productName=productName;
                        task.hito=hito;                        
                        products.put(id, task);
                    }
                }
            }
        }
        for(String key : products.keySet())
        {
            TaskInfo info=products.get(key);
            ProductsFinished product=new ProductsFinished();
            product.name=info.productName;
            String path=getPath(info.id, doc);
            if(path.startsWith("/"))
            {
                path=path.substring(1);
            }
            product.path=path;
            findProductsFinished.add(product);
        }
        return findProductsFinished;
    }
    private static BufferedImage getImage(UrlPd urlPd,int index) throws IOException, URISyntaxException,ReportException 
    {
        Document doc = Jsoup.connect(urlPd.getSummaryToPQI().toString()).get();
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
    
    private static List<PQIElement> createTablePQI(UrlPd urlPd,File output,List<ProductsFinished> products) throws ReportException,URISyntaxException
    {
        List<PQIElement> createTablePQI=new ArrayList<>();
        try
        {        
            int index=0;
            for(ProductsFinished product : products)
            {
                addFilterByPath(product.path, urlPd);
                
                               
                PQIElement element=new PQIElement();
                element.pathToProduct=escapeHtml4(product.path);
                createTablePQI.add(element);
                element.pqi_actual=0.38;
                element.pqi_planned=0.80;
                element.imagePQIActual="";
                element.imagePQIPlanned="";
                if(element.pqi_actual<=0.4)
                {
                    element.style="color:red;";
                }
                BufferedImage pqi_planned=getImage(urlPd,0); 
                if(pqi_planned!=null)
                {                    
                    String name="pqi_planned_"+index;
                    element.imagePQIPlanned=Constants.REPORT_IMG_FOLDER+"/"+name+".png";
                    ContextUtil.saveImageToDisk(pqi_planned, output, name);                
                }
                BufferedImage pqi_actual=getImage(urlPd,1);
                if(pqi_actual!=null)
                {                    
                    String name="pqi_actual_"+index;
                    element.imagePQIActual=Constants.REPORT_IMG_FOLDER+"/"+name+".png";
                    ContextUtil.saveImageToDisk(pqi_actual, output, name);                
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
    
    private static List<String> findTasksInProgress(UrlPd urlPd) throws IOException, URISyntaxException {
	Document doc = Jsoup.connect(urlPd.getWeekReportUrl().toString()).get();
	Elements tasksList = doc.select("[name=dueTask]");
	if (!tasksList.isEmpty()) {
	    Elements task = doc.select("[name=dueTask]").get(0).select("td.left");
	    List<String> tasksInProgress = new ArrayList<>();
	    for (Element element : task) {
		tasksInProgress.add(element.text());
	    }
	    return tasksInProgress;
	} else {
	    return new ArrayList<>();
	}

    }
    private static List<String> findTasksCompleted(UrlPd urlPd) throws IOException, URISyntaxException {
	Document doc = Jsoup.connect(urlPd.getWeekReportUrl().toString()).get();
	Elements tasksList = doc.select("[name=compTask]");
	if (!tasksList.isEmpty()) {
	    Elements task = doc.select("[name=compTask]").get(0).select("td.left");
	    List<String> taskCompleted = new ArrayList<>();
	    for (Element element : task) {
		taskCompleted.add(element.text());
	    }
	    return taskCompleted;
	} else {
	    return new ArrayList<>();
	}

    }
    private static List<TaskWithProblem> findTaksWithProblems(UrlPd urlPd) throws IOException, URISyntaxException
    {
        List<TaskWithProblem> findProductsWithProblems=new ArrayList<>();
        Document document = Jsoup.connect(urlPd.getWeekReportUrl().toString()).get();        
        Elements renglones=document.select("table[id=$$$_progress] tr");
        for(int i=1;i<renglones.size();i++)
        {
            Element tr=renglones.get(i);
            if(tr.childNodes().size()>10)
            {
                String taskName=tr.child(0).ownText();
                String used=tr.child(5).ownText().replace('%', ' ').trim();
                String left=tr.child(10).ownText();
                if(!taskName.isEmpty())
                {
                    if(!used.isEmpty())
                    {
                        int iUsed=Integer.parseInt(used);
                        if(iUsed>100)
                        {
                            //tasks.add(taskName);
                            TaskWithProblem task=new TaskWithProblem(taskName,tr.child(7).text());
                            findProductsWithProblems.add(task);

                        }
                    }
                    else if(left.startsWith("-"))
                    {
                        TaskWithProblem task=new TaskWithProblem(taskName,tr.child(7).text());
                        findProductsWithProblems.add(task);

                    }

                }
            }
        }      
        return findProductsWithProblems;
    }
    private static List<String> findTasksNext(UrlPd urlPd) throws IOException, URISyntaxException {
	Document doc = Jsoup.connect(urlPd.getWeekReportUrl().toString()).get();
	Elements tasksList = doc.select("[name=dueTask]");
	if (!tasksList.isEmpty()) {
            List<String> taskNextWeek = new ArrayList<>();
            if(doc.select("[name=dueTask]").size()>1)
            {
                Elements task = doc.select("[name=dueTask]").get(1).select("td.left");                
                for (Element element : task) {
                    taskNextWeek.add(element.text());
                }
            }
	    return taskNextWeek;
	} else {
	    return new ArrayList<>();
	}

    }

    public static String computeVg(WeekReportTable table) {
	return DoubleUtils.formatToDigits(table.getDoubleProperty(1, 4));
    }

    public static String computeVgDiff(WeekReportTable table) {
	return DoubleUtils.formatToDigits(table.getDoubleProperty(1, 4) - table.getDoubleProperty(1, 3));
    }

    public static String computeVgFalta(WeekReportTable table) {
	return DoubleUtils.formatToDigits((1 - table.getDoubleProperty(1, 5)) * 100);
    }

    public static String computeTaskHours(WeekReportTable table) {
	return DoubleUtils.formatToDigits(table.getDoubleProperty(1, 1));
    }

    public static String computeClosedTask(WeekReportTable table) {
	return DoubleUtils.formatToDigits((table.getDoubleProperty(3, 2) * 100) - 100);
    }

    public static double computeHoursTaskNotFinished(WeekReportTable table) {
	return table.getDoubleProperty(1, 1) - table.getDoubleProperty(3, 1);
    }

    public static String computeWeekhrsTaskNotFinished(WeekReportTable table, double hsTareasTerm) {
	return DoubleUtils.formatToDigits(hsTareasTerm / table.getDoubleProperty(2, 1));
    }

    public static double computeVgXh(WeekReportTable table) {
	return table.getDoubleProperty(1, 4) / table.getDoubleProperty(3, 1);
    }

    public static String computeVgNotPerformed(double vhxH, double hsTareasTerm) {
	return DoubleUtils.formatToDigits(vhxH * hsTareasTerm);
    }

    public static String computeRecoveryWeeks(WeekReportTable table) {
        double weeks=(table.getDoubleProperty(1, 3) - table.getDoubleProperty(1, 4)) / table.getDoubleProperty(2, 4);
        weeks*=-1;        
	return weeks>0 ? "+"+DoubleUtils.formatToDigits(weeks): DoubleUtils.formatToDigits(weeks);
    }

    public static String computeRecoveryPercent(WeekReportTable table) {
        double actualVsPlan=table.getDoubleProperty(1, 5);
        return actualVsPlan>1 ? "+"+DoubleUtils.formatToDigits((Math.abs(1-actualVsPlan))*100) : "-"+DoubleUtils.formatToDigits((1-actualVsPlan)*100);
    }
    public static String computeRecoveryHr(WeekReportTable table,String weeks) {
        double averagePerWeekToDate=table.getDoubleProperty(2, 1);
        double dWeeks=Double.parseDouble(weeks);
        double hr=averagePerWeekToDate * dWeeks;
        return hr>0 ? "+"+DoubleUtils.formatToDigits(hr) : DoubleUtils.formatToDigits(hr);
    }
}
