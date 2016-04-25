package com.danimaniarqsoft.brain.pdes.insight;

import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;

import com.danimaniarqsoft.brain.pdes.insight.rules.Rule;

/**
 * 
 * InsightEngine is the core of the Expert System
 * 
 * @author Daniel Cortes Pichardo
 *
 */
public class InsightEngine {
	private PriorityQueue<Rule> rules = new PriorityQueue<Rule>(3);

	public InsightEngine() {

	}

	public List<Message> execute() {
		List<Message> messages = new ArrayList<>();
		for (Rule rule : rules) {
			if (rule.evaluate()) {
				messages.add(rule.getMessage());
			}
		}
		return messages;
	}

	public void addRule(Rule rule) {
		rules.add(rule);
	}
}
