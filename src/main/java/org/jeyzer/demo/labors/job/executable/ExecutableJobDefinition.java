package org.jeyzer.demo.labors.job.executable;

/*-
 * ---------------------------LICENSE_START---------------------------
 * Jeyzer Demo
 * --
 * Copyright (C) 2020 Jeyzer SAS
 * --
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 * ----------------------------LICENSE_END----------------------------
 */



public enum ExecutableJobDefinition {
	
	DUMMY(1, 1),
	
	CPU_CONSUMING_TASK(1, 5),
	
	FUNCTION_GLOBAL_PERCENTAGE(100, 10, 10, true),
	OPERATION_GLOBAL_PERCENTAGE(100, 10, 10, true),
	CONTENTION_TYPE_GLOBAL_PERCENTAGE(100, 10, 10, true),
	
	FUNCTION_PRESENCE(1, 2, true),
	OPERATION_PRESENCE(1, 2, true),
	CONTENTION_TYPE_PATTERN(1, 15),
	
	APPLICATIVE_TASK_ONESHOT_EVENT(1, 1),
	APPLICATIVE_TASK_LONG_EVENT(1, 5),
	APPLICATIVE_SESSION_ONESHOT_EVENT(1, 1),
	APPLICATIVE_SESSION_LONG_EVENT(1, 5),
	APPLICATIVE_SYSTEM_EVENT(1, 1),
	
	JEYZER_PUBLISHER_EVENT(1, 1),
	
	OPERATION_IN_PRINCIPAL_PERCENTAGE(5,10, true),
	FUNCTION_IN_PRINCIPAL_PERCENTAGE(5,10, true),
	CONTENTION_TYPE_IN_PRINCIPAL_PERCENTAGE(1,15, true),
	
	MEMORY_CONSUMING_PROCESS(1,6),
	
	EXCESSIVE_GC_TIME(1,2),
	EXCESSIVE_OLD_GC_EXECUTION(1,10),
	
	NAMED_THREAD_LEAK(1,35),
	GLOBAL_THREAD_LEAK(1,10),
	
	STACK_OVERFLOW(1,6),
	TASK_EXECUTION_PATTERN(1,2),
	
	OPERATION_PATTERN(1,3),
	FUNCTION_PATTERN(1,3),
	OPERATION_PATTERN_WITH_PERCENTAGE(1,30),
	FUNCTION_PATTERN_WITH_PERCENTAGE(1,10),
	
	JEYZER_MX_CONTEXT_PARAMETER_NUMBER(1,2),
	JEYZER_MX_CONTEXT_PARAMETER_PATTERN(1,2),
	TASK_JEYZER_MX_CONTEXT_PARAMETER_NUMBER(1,2),
	TASK_JEYZER_MX_CONTEXT_PARAMETER_PATTERN(1,4),
	
	ACTIVE_THREAD_LIMIT(1,3),
	GLOBAL_THREAD_LIMIT(1,5),
	
	LOCKER_TASK(4,6),
	LOCKS_CONTENTION(15,5),
	
	FUNCTION_PARALLEL_CONTENTION(5,2),
	OPERATION_PARALLEL_CONTENTION(5,5),
	CONTENTION_TYPE_PARALLEL_CONTENTION(12,5),
	
	CPU_RUNNABLE_VS_CPU_CAPACITY(1,6),
	FUNCTION_AND_OPERATION_PARALLEL_CONTENTION(10,5),
	
	LONG_RUNNING_TASK_WITH_FUNCTION(1,8),
	LONG_RUNNING_TASK(1,50),
	
	MEMORY_CONSUMING_TASK(1,3),
	
	DEADLOCK(1,5),
	ABSENT_THREADS(1,6),
	ACTIVE_NAMED_THREAD_LIMIT(1,5),
	ACTIVE_UNIQUE_THREAD(1,4),
	NAMED_THREAD_LIMIT(1,5),
	UNIQUE_THREAD(1,5),
	
	GC_FAILING_TO_RELEASE_MEMORY(1,20),

	SYSTEM_CPU_OVERLOAD(1,5),
	CPU_CONSUMING_PROCESS(1,4),
	
	SESSION_EXECUTION_PATTERN(1,2),
	
	MX_BEAN_PARAMETER_NUMBER_HIGHER(1,3),
	MX_BEAN_PARAMETER_NUMBER_LOWER(1,3),
	MX_BEAN_PARAMETER_PATTERN(1,3),
	
	FROZEN_STACKS(1,40),
	FROZEN_STACKS_WITH_FUNCTION(1,5),
	
	MEMORY_CONSUMING_SYSTEM(1,5),
	
	MULTI_FUNCTION_CONTENTION(1,5),
	
	// jar path collection happens in between (period 15s)
	PROCESS_JAR_VERSION_SNAPSHOT(1,4),
	PROCESS_JAR_MULTIPLE_VERSIONS(1,4),
	PROCESS_JAR_VERSION_ABSENCE(1,4),
	PROCESS_JAR_MANIFEST_VERSION_MISMATCH(1,4),
	
	// module collection happens in between (period 15s)
	PROCESS_MODULE_VERSION_SNAPSHOT(1,4),
	PROCESS_MODULE_VERSION_ABSENCE(1,4),
	
	JVM_FLAG(1,4);
	
	private ExecutableJobDefinition(int threadCount, int ticCount) {
		this(threadCount, ticCount, -1, false);
	}
	
	private ExecutableJobDefinition(int threadCount, int ticCount, boolean systemEventBased) {
		this(threadCount, ticCount, -1, systemEventBased);
	}
	
	private ExecutableJobDefinition(int threadCount, int ticCount, int percentage, boolean systemEventBased) {
		this.threadCount = threadCount;
		this.startLifeTics = ticCount;
		this.percentage = percentage;
		this.systemEventBased = systemEventBased;
	}
	
	private final int threadCount;
	private final int startLifeTics;
	private final int percentage;
	private boolean systemEventBased;
	
	public boolean isPercentageBased() {
		return this.percentage != -1;
	}

	public int getThreadCount() {
		return threadCount;
	}

	public int getStartLifeTics() {
		return startLifeTics;
	}

	public int getPercentage() {
		return percentage;
	}
	
	public String getEventName() {
		return this.name().toLowerCase().replace('_', ' ');
	}
	
	public String getStickerName() {
		return this.name().toLowerCase(); 
	}
	
	public String getThreadName() {
		return this.name().toLowerCase(); 
	}
	
	public String getClassName() {
		return this.getClass().getPackage().getName() + ".impl." + getClassNameFromEnumValue();
	}
	
	public boolean isSystemEventBased() {
		return systemEventBased;
	}
	
	private String getClassNameFromEnumValue() {
		String name = this.name().toLowerCase();
		String result = "";
		int start = 0;
		while (true) {
			int end = name.indexOf('_', start);
			result += name.substring(start, start + 1).toUpperCase() 
					+ (end != -1 ? name.substring(start + 1, end) : name.substring(start + 1));
			if (end == -1)
				break;
			start = end + 1;
		}
		return result + "Job";
	}
	
	@Override
	public String toString() {
		return this.name() + " thread count : " + this.getThreadCount() + " ticks : " + this.getStartLifeTics();
	}
}
