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

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import android.content.res.AssetManager;
import android.util.Base64;
import android.util.Log;

import com.nagravision.drmtests.ADrmTestListActivity;

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

	public HtmlTestReport() {
		InputStream ims = null;
		try {
			ims = getAssets().open("TemplateTest.html");
		} catch (IOException e1) {
			Log.e("drmTests", e1.toString());
			e1.printStackTrace();
		}
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		try {
			DocumentBuilder db = dbf.newDocumentBuilder();
			doc = db.parse(new InputSource(ims));
		} catch (Exception e) {
			Log.e("drmTests", e.toString());
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
		NodeList nl = this.log.getChildNodes();
		org.w3c.dom.Text log_text = null;
		for (int i = 0; i < nl.getLength(); i++) {
			if (nl.item(i) instanceof org.w3c.dom.Text)
				log_text = (org.w3c.dom.Text) nl.item(i);
		}
		if (log_text != null) {
			log_text.appendData("<br/>\n" + log);
		}
	}


	private AssetManager getAssets() {
		return ADrmTestListActivity.getContext().getAssets();
	}

	public String getDocumentAsDataUri()
	{
		StringBuffer ret = new StringBuffer("");
		ret.append("data:text/html;base64,");
		ret.append(Base64.encodeToString(doc.getTextContent().getBytes(),
				Base64.URL_SAFE));
		return ret.toString();
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
		NodeList nl = this.id.getChildNodes();
		org.w3c.dom.Text id_text = null;
		for (int i = 0; i < nl.getLength(); i++) {
			if (nl.item(i) instanceof org.w3c.dom.Text)
				id_text = (org.w3c.dom.Text) nl.item(i);
		}
		if (id_text != null) {
			id_text.setData(id);
		}
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
		NodeList nl = this.log.getChildNodes();
		org.w3c.dom.Text log_text = null;
		for (int i = 0; i < nl.getLength(); i++) {
			if (nl.item(i) instanceof org.w3c.dom.Text)
				log_text = (org.w3c.dom.Text) nl.item(i);
		}
		if (log_text != null) {
			log_text.setData(log);
		}
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
		NodeList nl = this.result.getChildNodes();
		org.w3c.dom.Text result_text = null;
		for (int i = 0; i < nl.getLength(); i++) {
			if (nl.item(i) instanceof org.w3c.dom.Text)
				result_text = (org.w3c.dom.Text) nl.item(i);
		}
		if (result_text != null) {
			result_text.setData(result);
		}
	}

	public void setStatus(boolean status) {
		Attr fontcolor = this.status.getAttributeNode("color");
		fontcolor.setValue(status ? "#00cc00" : "#cc0000");
		NodeList nl = this.status.getChildNodes();
		org.w3c.dom.Text status_text = null;
		for (int i = 0; i < nl.getLength(); i++) {
			if (nl.item(i) instanceof org.w3c.dom.Text)
				status_text = (org.w3c.dom.Text) nl.item(i);
		}
		if (status_text !=  null)
		{
			status_text.setData(status ? "OK" : "KO");
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
		NodeList nl = this.synopsis.getChildNodes();
		org.w3c.dom.Text synopsis_text = null;
		for (int i = 0; i < nl.getLength(); i++) {
			if (nl.item(i) instanceof org.w3c.dom.Text)
				synopsis_text = (org.w3c.dom.Text) nl.item(i);
		}
		if (synopsis_text != null) {
			synopsis_text.setData(synopsis);
		}
	}
}
