package com.danimaniarqsoft.brain.pdes.insight.rules;

import com.danimaniarqsoft.brain.pdes.insight.Message;
import com.danimaniarqsoft.brain.pdes.insight.MessageType;
import com.danimaniarqsoft.brain.pdes.model.PerformanceReportTable;

public class RulePerformance extends RuleBase {

	private PerformanceReportTable data;

	public RulePerformance(PerformanceReportTable data) {
		this.data = data;
	}

	@Override
	public boolean evaluate() {
		return false;
	}

	@Override
	public Message getMessage() {
		return Message.getBuilder().addMessageType(MessageType.ADVICE)
				.addText("Es posible que no se esté agregando el detalle suficiente en tus actividades").addnewLine()
				.addText("Trata de registrar más tiempo").build();
	}
}
