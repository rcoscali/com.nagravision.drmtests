/*
 * Copyright (C) 2014 NagraVision
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.nagravision.drmtests.tests;

import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import android.content.Context;
import android.util.Base64;
import android.util.Log;

public class HtmlTestReport {
	private Document doc = null;
	private Element id = null;
	private Element synopsis = null;
	private Element log = null;
	private Element result = null;
	private Element status = null;

	private static final String ID_ID = "id-id";
	private static final String ID_SYNOPSIS = "id-synopsis";
	private static final String ID_LOG = "id-log";
	private static final String ID_RESULT = "id-result";
	private static final String ID_STATUS = "id-status";

	private static final String COLOR_GREEN = "#33ff33";
	private static final String COLOR_RED = "#ff3333";

	private static final String STATUS_OK = "OK";
	private static final String STATUS_KO = "KO";

	public HtmlTestReport(Context ctxt) {
		InputStream ims = null;
		try {
			ims = ctxt.getAssets().open("TemplateTest.html");
		} catch (IOException e1) {
			Log.e("drmTests", e1.toString());
			e1.printStackTrace();
		}
		try {
			doc = Jsoup.parse(ims, "UTF-8", "");
		} catch (Exception e2) {
			Log.e("drmTests", e2.toString());
		}
		setId(doc.getElementById(ID_ID));
		setSynopsis(doc.getElementById(ID_SYNOPSIS));
		setLog(doc.getElementById(ID_LOG));
		setResult(doc.getElementById(ID_RESULT));
		setStatus(doc.getElementById(ID_STATUS));
	}


	/**
	 * @param log
	 *            the log to set
	 */
	public void appendLog(String log) {
		this.log.append("<br/>\n" + log);
	}

	public String getDocumentAsDataUri()
	{
		StringBuffer ret = new StringBuffer("");
		ret.append(Base64.encodeToString(doc.outerHtml().getBytes(),
				Base64.DEFAULT | Base64.NO_WRAP));
		return ret.toString();
	}

	public String getDocumentAsHtml() {
		return doc.outerHtml();
	}

	/**
	 * @return the id
	 */
	public Element getId() {
		return id;
	}

	/**
	 * @return the log
	 */
	public Element getLog() {
		return log;
	}

	/**
	 * @return the result
	 */
	public Element getResult() {
		return result;
	}

	/**
	 * @return the statusKo
	 */
	public Element getStatus() {
		return status;
	}

	/**
	 * @return the synopsis
	 */
	public Element getSynopsis() {
		return synopsis;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(Element id) {
		this.id = id;
	}

	/**
	 * @param id
	 *            the id to set
	 */
	public void setId(String id) {
		this.id.text(id);
	}

	/**
	 * @param log
	 *            the log to set
	 */
	public void setLog(Element log) {
		this.log = log;
	}

	/**
	 * @param log
	 *            the log to set
	 */
	public void setLog(String log) {
		this.log.text(log);
	}

	/**
	 * @param result the result to set
	 */
	public void setResult(Element result) {
		this.result = result;
	}

	/**
	 * @param result
	 *            the result to set
	 */
	public void setResult(String result) {
		this.result.text(result);
	}

	public void setStatus(boolean status) {
		this.status.text(status ? STATUS_OK : STATUS_KO);
		Attributes attrs = this.status.attributes();
		Iterator<Attribute> it = attrs.iterator();
		while (it.hasNext()) {
			Attribute attr = it.next();
			if (attr.getKey().equals("color")) {
				attr.setValue(status ? COLOR_GREEN : COLOR_RED);
				break;
			}
		}
	}

	/**
	 * @param statusKo
	 *            the statusKo to set
	 */
	public void setStatus(Element status) {
		this.status = status;
	}

	/**
	 * @param synopsis the synopsis to set
	 */
	public void setSynopsis(Element synopsis) {
		this.synopsis = synopsis;
	}

	/**
	 * @param synopsis
	 *            the synopsis to set
	 */
	public void setSynopsis(String synopsis) {
		this.synopsis.text(synopsis);
	}
}
