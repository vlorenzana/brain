package mx.infotec.dads.insight.tasks;

import java.io.File;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import mx.infotec.dads.insight.pdes.service.PersonalReportService;
import mx.infotec.dads.insight.pdes.service.context.ReportContext;
import mx.infotec.dads.insight.util.Constants;
import mx.infotec.dads.insight.util.ContextUtil;
import mx.infotec.dads.insight.util.DateUtils;
import mx.infotec.dads.insight.util.UrlPd;
import javafx.concurrent.Task;
import mx.infotec.dads.insight.pdes.service.WeekReportService;

/**
 * Tarea para generar el reporte
 * 
 * @author Daniel Cortes Pichardo
 *
 */
public class GenerateReportTask extends Task<Boolean> {
    private static final Logger LOGGER = LoggerFactory.getLogger(GenerateReportTask.class);

    private int selectedIndex;
    private UrlPd urlPd;    
    private String pathToReport;
    public GenerateReportTask(int selectedIndex, UrlPd urlPd) {
	this.selectedIndex = selectedIndex;
	this.urlPd = urlPd;        
    }
    
    public String getPathToReport()
    {
        return pathToReport;
    }

    @Override
    protected Boolean call() throws Exception {
	try {
	    if (selectedIndex == Constants.FIST_SELECTION) {
		DateUtils.setEn(false);
	    } else {
		DateUtils.setEn(true);
	    }
            Date endReport=WeekReportService.getStartReport(urlPd);
	    ReportContext context = new ReportContext(endReport);
            File file=context.getOutputFile();
            this.pathToReport=file.getAbsolutePath();
	    context.setUrlPd(urlPd);
	    PersonalReportService.getInstance().createReport(context);
	} catch (Exception e) {
	    ContextUtil.saveExceptionToDisk(e, Constants.FILE_ERROR_TXT, new File("./"));
	}
	LOGGER.info(
		"Thanks for using danimaniarqsoft solutions, visit my web page at www.danimanicp.com for further news");
	return true;
    }

}
