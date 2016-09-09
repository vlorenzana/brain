/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.infotec.dads.insight.pdes.model;

import org.apache.commons.lang3.StringEscapeUtils;

/**
 *
 * @author victor.lorenzana
 */
public class TaskWithProblem {
    public String name;
    public String hito;
    public String date;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
    public TaskWithProblem(String name,String hito,String date)
    {
       setName(name);
       setHito(hito);       
       setDate(date);       
    }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = StringEscapeUtils.escapeHtml4(name);;
    }

    public String getHito() {
        return hito;
    }

    public void setHito(String hito) {
        this.hito = StringEscapeUtils.escapeHtml4(hito);
    }
}
