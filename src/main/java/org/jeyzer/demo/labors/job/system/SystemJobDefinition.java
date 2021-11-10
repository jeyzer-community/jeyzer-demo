package org.jeyzer.demo.labors.job.system;

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


public enum SystemJobDefinition {
	
	DISK_FREE_SPACE(true),
	DISK_USED_SPACE(true),
	DISK_SPACE_TOTAL(true),
	DISK_WRITE_TIME(true),
	DISK_WRITE_SPEED(true),
	THREAD_DUMP_CAPTURE_TIME(true),
	PROCESS_CARD_PROPERTY_PATTERN(false),
	PROCESS_CARD_PROPERTY_NUMBER(false),
	PROCESS_CARD_PROPERTY_ABSENCE(false),
	PROCESS_UP_TIME(true),
	PROCESS_COMMAND_LINE_MAX_HEAP(false),
	PROCESS_COMMAND_LINE_PARAMETER_ABSENCE(false),
	PROCESS_COMMAND_LINE_PROPERTY_NUMBER(false),
	PROCESS_COMMAND_LINE_PROPERTY_PATTERN(false),
	PROCESS_COMMAND_LINE_PARAMETER_PATTERN(false),
	DISK_SPACE_FREE_PERCENT(true),
	DISK_SPACE_USED_PERCENT(true),
	MX_BEAN_PARAMETER_PATTERN_SYSTEM(false),
	MX_BEAN_PARAMETER_NUMBER_SYSTEM(false),
	STICKER_MATCH(true),
	PROCESS_JAR_VERSION(true),
	PROCESS_JAR_NAME(true),
	PROCESS_JAR_NAME_ABSENCE(true),
	PROCESS_MODULE_VERSION(true),
	PROCESS_MODULE_NAME(true),
	PROCESS_MODULE_NAME_ABSENCE(true),
	RECORDING_SIZE(true),
	GARBAGE_COLLECTOR_NAME(true);
	
	private boolean fullSessionScope;
	
	private SystemJobDefinition(boolean exist) {
		this.fullSessionScope = exist;
	}
	
	public String getEventName() {
		return this.name().toLowerCase().replace('_', ' ');
	}
	
	public String getStickerName() {
		return this.name().toLowerCase();
	}
	
	public String getClassName() {
		return this.getClass().getPackage().getName() + '.' + getClassNameFromEnumValue();
	}
	
	public boolean isFullSessionScope() {
		return fullSessionScope;
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
}
