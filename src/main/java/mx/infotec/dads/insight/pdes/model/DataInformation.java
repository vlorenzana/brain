/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.infotec.dads.insight.pdes.model;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import mx.infotec.dads.insight.controller.WizardController;
import static org.apache.commons.lang3.StringEscapeUtils.unescapeHtml4;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import static mx.infotec.dads.insight.util.Constants.PAGE_PLANNING;
import static mx.infotec.dads.insight.util.Constants.PAGE_QUALITY;
import static mx.infotec.dads.insight.util.Constants.PAGE_ROLES;
import static mx.infotec.dads.insight.util.Constants.PAGE_TASK_PRODUCTS;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.type.TypeFactory;

/**
 *
 * @author victor.lorenzana
 */
public class DataInformation {
    
    public String intPlan="",intComp_Ext="",intHitos="",int_Taks="",int_Size="",int_Time="",int_Quality="";
    final private String path;
    public List<String> actions;
    public List<InfoPQI> pqi=new ArrayList<>();
    public List<URLProduct> url_products=new ArrayList<>();
    public Collection<RoleDefinition> definitions;
    public DataInformation(String path)
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
    private void loadRoleDefinition() throws IOException
    {
        ObjectMapper mapper = new ObjectMapper();
        InputStream in=WizardController.class.getResourceAsStream("/roles/role_definition.json");        
        definitions=mapper.readValue(new InputStreamReader(in,Charset.defaultCharset()), TypeFactory.defaultInstance().constructParametricType(Collection.class, RoleDefinition.class));        
        for(RoleDefinition def : definitions)
        {
            if(def.acctions.isEmpty() && !def.responsabilities.isEmpty())
            {
                for(String resp : def.responsabilities)
                {
                    def.acctions.add("");
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
                loadTable.add(productURL);
            }
        }
        return loadTable;
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
    private void loadTaskAndProducts()
    {
        String file=path+"/"+PAGE_TASK_PRODUCTS;
        try
        {
            Document content=readFile(file);
            int_Taks=unescapeHtml4(extractParagraph("int_Taks",content));
            int_Size=unescapeHtml4(extractParagraph("int_Size",content));
            int_Time=unescapeHtml4(extractParagraph("int_Time",content));
            url_products=loadTableProductsURL("table_products",content);
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
                    def.acctions.set(index, action);
                    def.used=true;
                }
            }
        }
    }
}
