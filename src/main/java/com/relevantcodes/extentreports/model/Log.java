/*
* Copyright (c) 2015, Anshoo Arora (Relevant Codes).  All rights reserved.
* 
* Copyrights licensed under the New BSD License.
* 
* See the accompanying LICENSE file for terms.
*/

package com.relevantcodes.extentreports.model;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;

import com.relevantcodes.extentreports.LogStatus;

public class Log implements Serializable {
    /**
     * 
     */
    private static final long serialVersionUID = 889252425952501333L;
    private Date timestamp;
    private LogStatus logStatus;
    private String stepName;
    private String tcid = "";
    private String action = "";
    private String object = "";
    private String data = "";
    private String details = "";

    public String getTcid() {
	return this.tcid;
    }

    public String getAction() {
	return this.action;
    }

    public String getObject() {
	return this.object;
    }

    public String getData() {
	return this.data;
    }

    public Date getTimestamp() {
	return timestamp;
    }

    public void setTimestamp(Date timestamp) {
	this.timestamp = timestamp;
    }

    public void setLogStatus(LogStatus logStatus) {
	this.logStatus = logStatus;
    }

    public LogStatus getLogStatus() {
	return logStatus;
    }

    public void setStepName(String stepName) {
	this.stepName = stepName;
    }

    public String getStepName() {
	return stepName;
    }

    public void setDetails(String details) {
	String arr[] = details.split("###");
	this.tcid = arr[0];
	this.action = arr[1];
	this.object = arr[2];
	this.data = arr[3];
	this.details = arr[4] == null ? "" : arr[4];
    }

    public String getDetails() {
	return details;
    }

    public Log() {
	timestamp = Calendar.getInstance().getTime();
    }
}
