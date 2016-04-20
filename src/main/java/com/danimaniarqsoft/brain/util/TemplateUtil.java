package com.danimaniarqsoft.brain.util;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;

import com.danimaniarqsoft.brain.pdes.exceptions.ReportException;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;

public class TemplateUtil {

  private TemplateUtil() {

  }

  public static String readAsString(Template main) throws TemplateException, IOException {
    return readAsString(main, new HashMap<String, Object>());
  }

  public static String readAsString(Template main, Map<String, Object> data)
      throws TemplateException, IOException {
    StringWriter out = new StringWriter();
    main.process(data, out);
    out.flush();
    out.close();
    return out.toString();
  }

  public static void mergeIntoWeekReportFile(Template template, Map<String, Object> data,
      String fileName, File outPutFile) throws IOException, TemplateException {
    Writer file = new FileWriter(new File(outPutFile, fileName));
    template.process(data, file);
    file.flush();
    file.close();
  }

  public static void saveTemplate(Template mainTemplate, Configuration cfg,
      Map<String, Object> subData, String templateName, File outPutFile) throws ReportException {
    try {
      Template performanceTemplate = cfg.getTemplate("templates/" + templateName);
      String resString = TemplateUtil.readAsString(performanceTemplate, subData);
      Map<String, Object> mainData = new HashMap<String, Object>();
      mainData.put("content", resString);
      TemplateUtil.mergeIntoWeekReportFile(mainTemplate, mainData, templateName, outPutFile);
    } catch (Exception e) {
      throw new ReportException("createPerformanceFile", e);
    }
  }
}
