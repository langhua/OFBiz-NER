/*******************************************************************************
 * Licensed to YYWorks Inc. under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 *******************************************************************************/
package cn.yyworks.nlp.report;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.ofbiz.htmlreport.AbstractHtmlReport;
import org.apache.ofbiz.htmlreport.InterfaceReportThread;
import org.apache.ofbiz.htmlreport.util.ReportStringUtil;

/**
 * Provides a html report for ner training.<p> 
 * 
 */
public class NerHtmlReport extends AbstractHtmlReport {
	
	public static final String NER_REPORT_CLASS = "NER_HTML_REPORT";
	
    /**
     * Public constructor with report variables.<p>
     * 
     * @param req the HttpServletRequest request
     * @param res the HttpServletResponse response
     */
    public NerHtmlReport(HttpServletRequest request, HttpServletResponse response) {

        super(request, response);
    }
    
    public static NerHtmlReport getReport(HttpServletRequest request, HttpServletResponse response) {
    	
    	NerHtmlReport wp = (NerHtmlReport) request.getAttribute(NER_REPORT_CLASS);
    	if (wp == null) {
    		wp = new NerHtmlReport(request, response);
    		request.setAttribute(NER_REPORT_CLASS, wp);
    	}
    	return wp;
    }
    
    public InterfaceReportThread initializeThread(HttpServletRequest request, HttpServletResponse response, String name) {

		if (name == null) {
			name = "";
		}
        ThreadGroup threadGroup = Thread.currentThread().getThreadGroup();
        int i = threadGroup.activeCount();
        Thread[] threads = new Thread[i];
        threadGroup.enumerate(threads, true);
        InterfaceReportThread thread = null;
        for (int j=0; j<threads.length; j++) {
        	Thread threadInstance = threads[j];
        	if (threadInstance instanceof NerHtmlThread) {
        		thread = (InterfaceReportThread) threadInstance;
        		break;
        	}
        }
        if (thread == null) {
            thread = new NerHtmlThread(request, response, name.toLowerCase());
        }

        return thread;
    }

    public static String checkButton(HttpServletRequest request, HttpServletResponse response) {
    	String action = request.getParameter("action");
    	if (ReportStringUtil.isNotEmpty(action)) {
    		if (action.equalsIgnoreCase("ok")) {
    			request.removeAttribute(NER_REPORT_CLASS);
    			request.removeAttribute(DIALOG_URI);
    			return "ok";
    		} else if (action.equalsIgnoreCase("cancel")) {
    			request.removeAttribute(NER_REPORT_CLASS);
    			request.removeAttribute(DIALOG_URI);
    			return "cancel";
    		}
    	}
    	action = request.getParameter("ok");
    	if (ReportStringUtil.isNotEmpty(action)) {
    		if (action.equalsIgnoreCase("ok")) {
    			request.removeAttribute(NER_REPORT_CLASS);
    			request.removeAttribute(DIALOG_URI);
    			return "ok";
    		}
    	}
        action = request.getParameter("cancel");
        if (ReportStringUtil.isNotEmpty(action)) {
        	if (action.equalsIgnoreCase("cancel")) {
    			request.removeAttribute(NER_REPORT_CLASS);
    			request.removeAttribute(DIALOG_URI);
        		return "cancel";
        	}
        }
        
    	return "success";
    }
}
