/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.infotec.dads.insight.pdes.model;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.Version;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import mx.infotec.dads.insight.controller.WizardController;
import mx.infotec.dads.insight.pdes.service.HtmlTemplateService;
import static org.apache.commons.lang3.StringEscapeUtils.unescapeHtml4;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import static mx.infotec.dads.insight.util.Constants.PAGE_PLANNING;
import static mx.infotec.dads.insight.util.Constants.PAGE_QUALITY;
import static mx.infotec.dads.insight.util.Constants.PAGE_ROLES;
import static mx.infotec.dads.insight.util.Constants.PAGE_TASK_PRODUCTS;
import org.apache.commons.io.FileUtils;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.type.TypeFactory;
import org.jsoup.nodes.Entities.EscapeMode;

/**
 *
 * @author victor.lorenzana
 */
public class ReportInformation {
    
    private static final Configuration CFG;
    static {
	CFG = new Configuration(new Version("2.3.0"));
	CFG.setClassForTemplateLoading(HtmlTemplateService.class, "/");
    }
    
    public String intPlan="",intComp_Ext="",intHitos="",int_Size="",int_Time="",int_Quality="";
    final private String path;
    public List<String> actions;
    public List<String> actionsTaks;
    public List<InfoPQI> pqi=new ArrayList<>();
    public List<URLProduct> url_products=new ArrayList<>();
    public Collection<RoleDefinition> definitions;
    public ReportInformation(String path)
    {
        this.path=path;
    }
    
    public void load()
    {
        loadIndex();
        loadTaskAndProducts();
        loadQuality();
        loadRoles();
    }
    private void loadRoles()
    {
        try
        {
            loadRoleDefinition();
            loadRolesFromReport();
        }
        catch(IOException e)
        {
            
        }
    }
    private void saveRoles()
    {
        String file=path+"/"+PAGE_ROLES;
        try
        {
            Document content=readFile(file);
            Elements rows=content.select("div[class=\"row\"]");
            if(rows.size()>0)
            {       
                rows.get(0).children().remove();                
                for(RoleDefinition def : definitions)
                {     
                    if(def.isUsed())
                    {
                        saveRole(rows.get(0),def);
                    }
                }
                saveDocument(new File(file), content); 
            }
        }
        catch(IOException e)
        {
            e.printStackTrace();
        }
    }
    
    private void loadRolesFromReport()
    {
        String file=path+"/"+PAGE_ROLES;
        try
        {
            Document content=readFile(file);
            for(RoleDefinition def : definitions)
            {
                String id=def.id;
                loadRole(id,content,def);
            }
        }
        catch(IOException e)
        {
            e.printStackTrace();
        }
    }
    public  void loadRoleDefinition() throws IOException
    {
        ObjectMapper mapper = new ObjectMapper();
        InputStream in=WizardController.class.getResourceAsStream("/roles/role_definition.json");        
        definitions=mapper.readValue(new InputStreamReader(in,Charset.defaultCharset()), TypeFactory.defaultInstance().constructParametricType(Collection.class, RoleDefinition.class));        
        for(RoleDefinition def : definitions)
        {
            if(def.actions.isEmpty() && !def.responsabilities.isEmpty())
            {
                for(String resp : def.responsabilities)
                {
                    def.actions.add("");
                }
            }
        }
       
    }
    private static Document readFile(String file) throws IOException {
        return Jsoup.parse(new File(file),"UTF-8");
        
    }
    
    private String extractParagraph(String id,Document document)
    {
        Elements elements=document.select("p[id="+ id +"]");
        if(elements.size()>0)
        {
            return elements.get(0).text();
        }        
        return "";
    }
    public List<String> extractList(String id,Document content)
    {
        List<String> extractList=new ArrayList<>();
        Elements elements=content.select("ul[id="+  id +"] li");
        for(int i=0;i<elements.size();i++)
        {
            Element li=elements.get(i);
            extractList.add(unescapeHtml4(li.text()));
        }        
        return extractList;
    }
    private List<URLProduct> loadTableProductsURL(String id,Document content)
    {
        List<URLProduct> loadTable=new ArrayList<>();
        Elements table=content.select("table[id="+ id +"] tr");
        for(int i=1;i<table.size();i++)
        {
            Element tr=table.get(i);
            String status=tr.attr("status");
            if("finishedToDate".equalsIgnoreCase(status))
            {
                String product=tr.child(0).text();            
                String url=tr.child(3).text();
                URLProduct productURL=new URLProduct();
                productURL.product=unescapeHtml4(product);
                productURL.url=url;
                productURL.index=i;
                loadTable.add(productURL);
            }
        }
        return loadTable;
    }
    private void saveURLProducs(String id,Document document)
    {
        Elements table=document.select("table[id="+ id +"] tr");
        for(int i=1;i<table.size();i++)
        {
            Element tr=table.get(i);
            for(int j=0;j<this.url_products.size();j++)
            {
                URLProduct product=this.url_products.get(j);
                if(product.index==i)
                {
                    tr.child(3).text(product.url);
                }
            }
        }
    }
    private List<InfoPQI> loadTable(String id,Document content)
    {
        List<InfoPQI> loadTable=new ArrayList<>();
        Elements table=content.select("table[id="+ id +"] tr");
        for(int i=1;i<table.size();i++)
        {
            Element tr=table.get(i);
            String product=tr.child(0).text();
            String action=tr.child(3).text();
            InfoPQI info=new InfoPQI();
            info.accion=unescapeHtml4(action);
            info.product=unescapeHtml4(product);
            info.pqiActual=Double.parseDouble(tr.child(2).child(0).text());
            loadTable.add(info);
        }
        return loadTable;
    }
    private void saveTable(String id,Document content)
    {
        
        Elements table=content.select("table[id="+ id +"] tr");
        for(int i=1;i<table.size();i++)
        {
            Element tr=table.get(i);            
            tr.child(3).text(this.pqi.get(i-1).accion);            
        }
        
    }
    private void loadQuality()
    {
        String file=path+"/"+PAGE_QUALITY;
        try
        {
            Document content=readFile(file);
            int_Quality=unescapeHtml4(extractParagraph("int_Quality",content));
            pqi=loadTable("int_pqi",content);
            
        }
        catch(IOException e)
        {
            e.printStackTrace();
        }
    }
    private void saveQuality()
    {
        String file=path+"/"+PAGE_QUALITY;
        try
        {
            Document content=readFile(file);
            updateParagraph("int_Quality",int_Quality,content);
            saveTable("int_pqi",content);
            saveDocument(new File(file), content); 
            
        }
        catch(IOException e)
        {
            e.printStackTrace();
        }
    }
    private void loadTaskAndProducts()
    {
        String file=path+"/"+PAGE_TASK_PRODUCTS;
        try
        {
            Document content=readFile(file);
            actionsTaks=extractList("int_Taks",content);
            int_Size=unescapeHtml4(extractParagraph("int_Size",content));
            int_Time=unescapeHtml4(extractParagraph("int_Time",content));
            url_products=loadTableProductsURL("table_products",content);
        }
        catch(IOException e)
        {
            e.printStackTrace();
        }
    }
    
    private void saveTaks()
    {
        String file=path+"/"+PAGE_TASK_PRODUCTS;        
        try
        {
            Document content=readFile(file);
            updateList("int_Taks",this.actionsTaks,content);
            updateParagraph("int_Size",this.int_Size,content);
            updateParagraph("int_Time",this.int_Time,content);
            saveURLProducs("table_products",content);            
            saveDocument(new File(file), content); 
            
        }
        catch(IOException e)
        {
            e.printStackTrace();
        }
        
    }
    private void updateParagraph(String id,String text,Document document)
    {
        Elements elements=document.select("p[id="+ id + "]");
        if(elements.size()>0)
        {
            elements.get(0).text(text);
        }
    }
    private void saveDocument(File file,Document doc)
    {
        try
        {            
            doc.outputSettings().escapeMode(EscapeMode.xhtml);
            FileUtils.writeStringToFile(file, doc.html() , "UTF-8");                    
        }
        catch(IOException e)
        {
            e.printStackTrace();
        }
    }
    
    private void loadIndex()
    {
        String file=path+"/"+PAGE_PLANNING;
        try
        {
            Document content=readFile(file);
            intPlan=unescapeHtml4(extractParagraph("intPlan",content));
            intComp_Ext=unescapeHtml4(extractParagraph("intComp_Ext",content));
            intHitos=unescapeHtml4(extractParagraph("intHitos",content));
            actions=extractList("acciones",content);
        }
        catch(IOException e)
        {
            e.printStackTrace();
        }
    }   
    private void saveIndex()
    {
        String file=path+"/"+PAGE_PLANNING;
        intPlan= intPlan==null ? "" : intPlan;
        try
        {
            Document content=readFile(file);
            updateParagraph("intPlan",this.intPlan,content);
            updateParagraph("intComp_Ext",this.intComp_Ext,content);
            updateParagraph("intHitos",this.intHitos,content);
            updateList("acciones",this.actions,content);
            saveDocument(new File(file), content); 
            
        }
        catch(IOException e)
        {
            e.printStackTrace();
        }
        
    }

    private void saveRole(Element row, RoleDefinition def) 
    {
        try
        {
            Template mainTemplate = CFG.getTemplate("templates/templateRole.html");
            ByteArrayOutputStream out=new ByteArrayOutputStream();
            OutputStreamWriter writer=new OutputStreamWriter(out);
            Map<String,Object> data=new HashMap<>();
            data.put("title", def.title);
            data.put("id", def.id);
            List<RoleInformation> inf=new ArrayList<>();
            for(int i=0;i<def.responsabilities.size();i++)
            {
                inf.add(new RoleInformation(def.responsabilities.get(i), def.actions.get(i)));
            }
            data.put("rolTable",inf);            
            mainTemplate.process(data, writer);
            row.append(out.toString());
            
            
        }
        catch(Exception te)
        {
            te.printStackTrace();
        }
        
        
         
        
    }
    private void loadRole(String id, Document content, RoleDefinition def) {
        Elements rows=content.select("table[id="+id+"] tr");
        for(int i=1;i<rows.size();i++)
        {
            Element tr=rows.get(i);
            String resp=tr.child(0).text().trim();
            String action=tr.child(1).text().trim();
            int index=-1;
            for(String resp_cat : def.responsabilities)
            {
                index++;
                if(resp_cat.equalsIgnoreCase(resp))
                {
                    def.actions.add(index, action);
                    def.used=true;
                }
            }
        }
    }
    public void save()
    {
        saveIndex();
        saveTaks();
        saveQuality();
        saveRoles();
    }

    private void updateList(String id, List<String> actions, Document document) {
        Elements uls=document.select("ul[id="+ id +"]");
        if(uls.size()>0)
        {
            Element ul=uls.get(0);            
            ul.children().remove();
            for(String action : actions)
            {
                Element li=ul.appendElement("li");                
                li.text(action);                
            }
        }
    }
}
