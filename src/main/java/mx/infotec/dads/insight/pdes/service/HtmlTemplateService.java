package mx.infotec.dads.insight.pdes.service;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import mx.infotec.dads.insight.pdes.exceptions.ReportException;
import mx.infotec.dads.insight.pdes.model.PerformanceReportTable;
import mx.infotec.dads.insight.pdes.model.SizeReportTable;
import mx.infotec.dads.insight.pdes.service.context.ReportContext;
import mx.infotec.dads.insight.util.TemplateUtil;
import mx.infotec.dads.insight.util.ZipUtils;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.Version;
import static mx.infotec.dads.insight.util.Constants.PAGE_PLANNING;
import static mx.infotec.dads.insight.util.Constants.PAGE_QUALITY;
import static mx.infotec.dads.insight.util.Constants.PAGE_ROLES;
import static mx.infotec.dads.insight.util.Constants.PAGE_TASK_PRODUCTS;

/**
 * Servicio para la generación de templates
 * 
 * @author Daniel Cortes Pichardo
 *
 */
public class HtmlTemplateService extends AbstractHtmlTemplate {
    private static final Configuration CFG;
    private Template mainTemplate;
    private static HtmlTemplateService instance = null;

    static {
	CFG = new Configuration(new Version("2.3.0"));
	CFG.setClassForTemplateLoading(HtmlTemplateService.class, "/");
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
	    mainTemplate = CFG.getTemplate("templates/mainLayout.html");
	    HashMap<String, Object> data = new HashMap<>();
	    data.put("gn", context.getReport().getInfoReportTable());
            Map<String, Object> templateData = new HashMap<>();
            for (int i = 0; i < context.getReport().getWeekReportTable().getNumRows(); i++) {
                for (int j = 0; j < context.getReport().getWeekReportTable().getNumCols(); j++) {
                    data.put("data" + i + "" + j, context.getReport().getWeekReportTable().getStringProperty(i, j));
                }
            }
            PerformanceReportTable pt = context.getReport().getPerformanceReportTable();	
            data.put("pTable", pt);
	    TemplateUtil.saveTemplate(mainTemplate, CFG, data, PAGE_PLANNING, context.getOutputFile());
	} catch (IOException e) {
	    throw new ReportException("createIndexFile", e);
	}
    }

    @Override
    protected void createQualityFile(ReportContext context) throws ReportException {
	SizeReportTable sizeTable = context.getReport().getSizeReportTable();
	Map<String, Object> templateData = new HashMap<>();
	templateData.put("sizeTable", sizeTable.getData());
        templateData.put("tablePQI", context.getReport().getTablePQI());
	TemplateUtil.saveTemplate(mainTemplate, CFG, templateData, PAGE_QUALITY, context.getOutputFile());

    }

    @Override
    protected void createRoleFile(ReportContext context) throws ReportException {
	TemplateUtil.saveTemplate(mainTemplate, CFG, new HashMap<String, Object>(), PAGE_ROLES,
		context.getOutputFile());
    }

    @Override
    protected void createExternalCommitmentsFile(ReportContext context) throws ReportException {
	/*TemplateUtil.saveTemplate(mainTemplate, CFG, new HashMap<String, Object>(), "extCommitmentsChart.html",
		context.getOutputFile());*/
    }

    @Override
    protected void createHoursFile(ReportContext context) throws ReportException {
	/*TemplateUtil.saveTemplate(mainTemplate, CFG, new HashMap<String, Object>(), "hrsChart.html",
		context.getOutputFile());*/
    }

    @Override
    protected void createTaskProgressFile(ReportContext context) throws ReportException {
	HashMap<String, Object> data = new HashMap<>();
	data.put("tasks", context.getReport().getTasksInProgressToHTml());
        data.put("taskcompleted", context.getReport().getTasksCompletedToHTML());
        data.put("tasknext", context.getReport().getTasksNextWeekToHTML());
        data.put("task_problems", context.getReport().getTaskProblems());
        data.put("products", context.getReport().getProducts());
        data.put("timetable", context.getReport().getTimeTableFinished());
        data.put("sizeTable", context.getReport().getSizeReportTable().getData());
	TemplateUtil.saveTemplate(mainTemplate, CFG, data, PAGE_TASK_PRODUCTS, context.getOutputFile());
    }

    @Override
    protected void createVgFile(ReportContext context) throws ReportException {
	/*TemplateUtil.saveTemplate(mainTemplate, CFG, new HashMap<String, Object>(), "vgChart.html",
		context.getOutputFile());*/

    }

    @Override
    protected void createWeekResumeFile(ReportContext context) throws ReportException {
	Map<String, Object> templateData = new HashMap<>();
	for (int i = 0; i < context.getReport().getWeekReportTable().getNumRows(); i++) {
	    for (int j = 0; j < context.getReport().getWeekReportTable().getNumCols(); j++) {
		templateData.put("data" + i + "" + j, context.getReport().getWeekReportTable().getStringProperty(i, j));
	    }
	}
	TemplateUtil.saveTemplate(mainTemplate, CFG, templateData, "weekResume.html", context.getOutputFile());

    }

    @Override
    protected void createSupportFile(ReportContext context) throws ReportException {
	TemplateUtil.saveTemplate(mainTemplate, CFG, new HashMap<String, Object>(), "support.html",
		context.getOutputFile());

    }

    @Override
    protected void createPerformanceFile(ReportContext context) throws ReportException {
	/*PerformanceReportTable pt = context.getReport().getPerformanceReportTable();
	Map<String, Object> templateData = new HashMap<>();
	templateData.put("pTable", pt);
	TemplateUtil.saveTemplate(mainTemplate, CFG, templateData, "performance.html", context.getOutputFile());*/
    }

    @Override
    protected void createMilestonesFile(ReportContext context) throws ReportException {
	/*TemplateUtil.saveTemplate(mainTemplate, CFG, new HashMap<String, Object>(), "milestonesChart.html",
		context.getOutputFile());*/
    }

    @Override
    protected void createWebSite(ReportContext context) throws ReportException {
	ZipUtils.extract("/site/site.zip", context.getOutputFile());
    }
}
