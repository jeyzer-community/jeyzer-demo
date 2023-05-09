package org.jeyzer.demo.labors.job.executable;

/*-
 * ---------------------------LICENSE_START---------------------------
 * Jeyzer Demo
 * --
 * Copyright (C) 2020 - 2023 Jeyzer
 * --
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 * ----------------------------LICENSE_END----------------------------
 */


import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class ExecutableJobAdjuster {
	
	private static final Logger logger = LoggerFactory.getLogger(ExecutableJobAdjuster.class);

	public void adjustJobs(List<ExecutableJob> jobs) {
		List<ExecutableJob> candidates = getAdjustCandidates(jobs);
		int safeguard = 0;
		while(!candidates.isEmpty()) {
			adjustCandidates(candidates);
			candidates = getAdjustCandidates(jobs);
			if (safeguard++ == 1000) {
				logger.error("Cannot achieve proper job adjustment.");
				logger.error("Problematic jobs are : " + candidates);
				System.exit(-1);
			}
		}
	}

	private void adjustCandidates(List<ExecutableJob> candidates) {
		for (ExecutableJob candidate : candidates) {
			// increase by 20% the life tics. Multiplied by the number of threads, can be high
			int newLifeTics = (int)(candidate.getLifeTics()*1.2);
			if (newLifeTics == candidate.getLifeTics())
				newLifeTics += 1; // minimum
			candidate.setLifeTics(newLifeTics);
		}
	}

	private List<ExecutableJob> getAdjustCandidates(List<ExecutableJob> jobs) {
		List<ExecutableJob> candidates = new ArrayList<>();
		int total = getTotalWeight(jobs);
		if (total == 0)
			return candidates;
		
		for (ExecutableJob job : jobs) {
			ExecutableJobDefinition def = job.getJobDefinition();
			if (def.isPercentageBased() && (double)100*job.getWeight()/total < def.getPercentage())
				candidates.add(job);
		}
		return candidates;
	}

	private int getTotalWeight(List<ExecutableJob> jobs) {
		int total = 0;
		for (ExecutableJob job : jobs) {
			total += job.getWeight();
		}
		return total;
	}
}
