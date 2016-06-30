package com.danimaniarqsoft.brain.tasks;

import java.io.File;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.danimaniarqsoft.brain.pdes.service.PersonalReportService;
import com.danimaniarqsoft.brain.pdes.service.context.ReportContext;
import com.danimaniarqsoft.brain.util.Constants;
import com.danimaniarqsoft.brain.util.ContextUtil;
import com.danimaniarqsoft.brain.util.DateUtils;
import com.danimaniarqsoft.brain.util.UrlPd;

import javafx.concurrent.Task;

public final class GenerateReportTask extends Task<Boolean> {
	private static final Logger LOGGER = LoggerFactory.getLogger(GenerateReportTask.class);

	private final int selectedIndex;
	private final UrlPd urlPd;

	public GenerateReportTask(int selectedIndex, UrlPd urlPd) {
		this.selectedIndex = selectedIndex;
		this.urlPd = urlPd;
	}

	@Override
	protected final Boolean call() throws Exception {
		try {
			if (selectedIndex == Constants.FIST_SELECTION) {
				DateUtils.setEn(false);
			} else {
				DateUtils.setEn(true);
			}
			ReportContext context = new ReportContext();
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
