package com.danimaniarqsoft.brain.pdes.service;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.danimaniarqsoft.brain.pdes.exceptions.ReportException;
import com.danimaniarqsoft.brain.pdes.model.PerformanceTable;
import com.danimaniarqsoft.brain.pdes.model.SizeTable;
import com.danimaniarqsoft.brain.pdes.service.context.ReportContext;
import com.danimaniarqsoft.brain.util.TemplateUtil;
import com.danimaniarqsoft.brain.util.ZipUtils;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.Version;

public class HtmlTemplateService extends AbstractHtmlTemplate {
  private static Configuration       cfg      = null;
  private static Template            mainTemplate;
  private static HtmlTemplateService instance = null;

  static {
    cfg = new Configuration(new Version("2.3.0"));
    cfg.setClassForTemplateLoading(HtmlTemplateService.class, "/");
  }


  private HtmlTemplateService() {

  }

  public static HtmlTemplateService getInstance() {
    if (instance == null) {
      synchronized (HtmlTemplateService.class) {
        if (instance == null) {
          instance = new HtmlTemplateService();
        }
      }
    }
    return instance;
  }

  @Override
  protected void createIndexFile(ReportContext context) throws ReportException {
    try {
      mainTemplate = cfg.getTemplate("templates/mainLayout.html");
      HashMap<String, Object> data = new HashMap<String, Object>();
      data.put("gn", context.getReport().getGeneralTable());
      TemplateUtil.saveTemplate(mainTemplate, cfg, data, "index.html", context.getOutputFile());
    } catch (IOException e) {
      throw new ReportException("createIndexFile", e);
    }
  }

  @Override
  protected void createSizeFile(ReportContext context) throws ReportException {
    SizeTable sizeTable = context.getReport().getSizeTable();
    Map<String, Object> templateData = new HashMap<String, Object>();
    templateData.put("sizeTable", sizeTable.getData().toString());
    TemplateUtil.saveTemplate(mainTemplate, cfg, templateData, "size.html",
        context.getOutputFile());

  }

  @Override
  protected void createDefectFile(ReportContext context) throws ReportException {
    TemplateUtil.saveTemplate(mainTemplate, cfg, new HashMap<String, Object>(), "defectChart.html",
        context.getOutputFile());
  }

  @Override
  protected void createExternalCommitmentsFile(ReportContext context) throws ReportException {
    TemplateUtil.saveTemplate(mainTemplate, cfg, new HashMap<String, Object>(),
        "extCommitmentsChart.html", context.getOutputFile());
  }

  @Override
  protected void createHoursFile(ReportContext context) throws ReportException {
    TemplateUtil.saveTemplate(mainTemplate, cfg, new HashMap<String, Object>(), "hrsChart.html",
        context.getOutputFile());
  }

  @Override
  protected void createTaskProgressFile(ReportContext context) throws ReportException {
    HashMap<String, Object> data = new HashMap<String, Object>();
    data.put("tasks", context.getReport().getTasksInProgress());
    TemplateUtil.saveTemplate(mainTemplate, cfg, data, "taskProgressChart.html",
        context.getOutputFile());
  }

  @Override
  protected void createVgFile(ReportContext context) throws ReportException {
    TemplateUtil.saveTemplate(mainTemplate, cfg, new HashMap<String, Object>(), "vgChart.html",
        context.getOutputFile());

  }

  @Override
  protected void createWeekResumeFile(ReportContext context) throws ReportException {
    Map<String, Object> templateData = new HashMap<String, Object>();
    for (int i = 0; i < context.getReport().getWeekTable().getNumRows(); i++) {
      for (int j = 0; j < context.getReport().getWeekTable().getNumCols(); j++) {
        templateData.put("data" + i + "" + j,
            context.getReport().getWeekTable().getStringProperty(i, j));
      }
    }
    TemplateUtil.saveTemplate(mainTemplate, cfg, templateData, "weekResume.html",
        context.getOutputFile());

  }

  @Override
  protected void createSupportFile(ReportContext context) throws ReportException {
    TemplateUtil.saveTemplate(mainTemplate, cfg, new HashMap<String, Object>(), "support.html",
        context.getOutputFile());

  }

  @Override
  protected void createPerformanceFile(ReportContext context) throws ReportException {
    PerformanceTable pt = context.getReport().getPerformanceTable();
    Map<String, Object> templateData = new HashMap<String, Object>();
    templateData.put("pTable", pt);
    TemplateUtil.saveTemplate(mainTemplate, cfg, templateData, "performance.html",
        context.getOutputFile());
  }

  @Override
  protected void createMilestonesFile(ReportContext context) throws ReportException {
    TemplateUtil.saveTemplate(mainTemplate, cfg, new HashMap<String, Object>(),
        "milestonesChart.html", context.getOutputFile());
  }

  @Override
  protected void createWebSite(ReportContext context) throws ReportException {
    ZipUtils.extract("/site/site.zip", context.getOutputFile());
  }
}
