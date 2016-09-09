/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.infotec.dads.insight.pdes.model;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import mx.infotec.dads.insight.util.DateUtils;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 *
 * @author victor.lorenzana
 */
public class TestFilter {
    
    static class TaskInfo
    {
        String id;
        String productName;
        String date;
        String hito;
    }
    static class PhaseTime
    {
        public String name;
        public String plannedTime;
        public String actualTime;
        public String percentActual;
    }
    public TestFilter() {
    }
    
    

    // TODO add test methods here.
    // The methods must be annotated with annotation @Test. For example:
    //
    // @Test
    // public void hello() {}
    
  
  public static void addFilter(String filter)
  {
      String url="http://localhost:2468/Proyecto/Coaches+2016//team/setup/selectLabelFilter";
      Connection con=Jsoup.connect(url);
      Map<String,String> data=new HashMap<String,String>();
      data.put("apply","Apply Filter");
      data.put("destUri","/Proyecto/Coaches+2016//cms/TSP/indiv_plan_summary?frame=content&section=50");
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
  private static String convertTime(String time)
  {
      DecimalFormat df=new DecimalFormat("00");
      if(!time.isEmpty() && !time.equals("0"))
      {
          Double timeMinutes=Double.parseDouble(time);
          int hours=timeMinutes.intValue()/60;
          double min=((timeMinutes/60)-hours)*60;
          time=hours+":"+df.format(min);
      }
      return time;
  }
  public static List<PhaseTime> getTimeTableFinished()
  {
      List<PhaseTime> getTimeTableFinished=new ArrayList<>();
      addFilter("terminado");      
      String url="http://localhost:2468/Proyecto/Coaches+2016//reports/table.class?chart=pie&title=Actual+Time&d1=Time&d2=Estimated+Time&for=%5BPhase_List%5D&h0=Phase&where=intersects%28%5B%2FProyecto%2FCoaches+2016%2FPhase_Display_Filter_List%5D%2C+%5B%5E%5D%29&colorScheme=byPhase";
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
            phase.actualTime=convertTime(timeActual);
            phase.plannedTime=convertTime(timePlanned);
            phase.percentActual=timePlanned.isEmpty() || timePlanned.equals("0")? "0%":((Double.parseDouble(timeActual)-Double.parseDouble(timePlanned))/Double.parseDouble(timeActual))+"%";
            getTimeTableFinished.add(phase);
        }
        
      }
      catch(IOException ioe)
      {
          ioe.printStackTrace();
      }
      for(PhaseTime phase : getTimeTableFinished)
      {
          System.out.println("phase: "+phase.name+" planned: "+phase.plannedTime+" actual: "+phase.actualTime+" % "+phase.percentActual);
          
      }
      return getTimeTableFinished;
  }
  public static void removeFilter(String filter)
  {
      String url="http://localhost:2468/Proyecto/Coaches+2016//team/setup/selectLabelFilter";
      Connection con=Jsoup.connect(url);
      Map<String,String> data=new HashMap<String,String>();
      data.put("remove","Remove Filter");
      data.put("destUri","/Proyecto/Coaches+2016//cms/TSP/indiv_plan_summary?frame=content&section=50");
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
  public static Set<String> getListaTareasProblems()
  {
      Set<String> tasks=new HashSet<>();
      String url="http://localhost:2468/Proyecto/Coaches+2016//reports/week.class?tl=auto&labelFilterAuto=t&pathFilterAuto=t";
      Connection con=Jsoup.connect(url);
      try
      {
        Document document=con.get();
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
                            tasks.add(taskName);

                        }
                    }
                    else if(left.startsWith("-"))
                    {
                        tasks.add(taskName);

                    }

                }
            }
        }
      }
      catch(Exception ioe)
      {
          
      }
      for(String task : tasks)
      {
          System.out.println("task: "+ task);
      }
      return tasks;
  }
  public static Map<String,TaskInfo> getListaProductos(Date init,Date end)
  {
      Map<String,TaskInfo> products=new HashMap<>();
      String url="http://localhost:2468/Proyecto/Coaches+2016//cms/TSP/indiv_plan_summary?frame=content&section=50";
      Connection con=Jsoup.connect(url);
      try
      {
        Document document=con.get();
        Elements renglones=document.select("body table[name=TASK] tr");
        DateUtils.setEn(false);
        
        
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
      }
      catch(Exception ioe)
      {
          
      }
      for(TaskInfo task : products.values())
      {
          System.out.println("product: "+task.productName+" hito: "+task.hito);
      }
      return products;
     
      
  }
  public static void main(String[] args) {
    Calendar init=Calendar.getInstance();
    Calendar end=Calendar.getInstance();
    init.add(Calendar.DATE, -20);
    //getListaTareas(init.getTime(),end.getTime());
    //getListaTareasProblems();
    getTimeTableFinished();
	}
}
