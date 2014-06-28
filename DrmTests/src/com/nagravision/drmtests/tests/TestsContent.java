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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.webkit.WebView;

/**
 * Helper class for providing tests content for user interfaces created by
 * Android template wizards.
 * <p>
 */
public class TestsContent {
	/**
	 * A drm test item representing a piece of content.
	 */
	public static abstract class DrmTestItem extends AsyncTask<Void, String, String> {
		public String id;
		public String synopsis;

		private HtmlTestReport report = null;
		private final Boolean terminated = false;

		private Context mCtxt = null;

		private WebView mView = null;

		public DrmTestItem(String id, String synopsis) {
			this.id = id;
			this.synopsis = synopsis;
			this.report = new HtmlTestReport();
			this.report.setId(id);
			this.report.setSynopsis(synopsis);
			this.report.setResult("running...");
			this.report.setStatus(false);
		}

		/**
		 * Append a message to the test result string
		 * @param aResult result string to add for the test result
		 * @return the test result string
		 */
		public StringBuffer appendLog(String aResult) {
			this.report.appendLog(aResult);
			publishProgress(this.report.getDocumentAsDataUri());
			return new StringBuffer(report.getDocumentAsDataUri());
		}

		@Override
		protected String doInBackground(Void... params)
		{
			Log.v("drmtests", "doInBackground");
			return run();
		}

		/**
		 * @return the mCtxt
		 */
		public Context getContext() {
			return mCtxt;
		}

		public String getHtmlData() {
			return getResult().toString();
		}

		/**
		 * Test result getter
		 * @return the result of the test
		 */
		public StringBuffer getResult() {
			return new StringBuffer(this.report.getDocumentAsDataUri());
		}

		@Override
		protected void onPostExecute(String value)
		{
			Log.v("drmtests", "onPostExecute: " + value);
			appendLog("Test execution terminated.\n");
			mView.loadData(getResult().toString(), "text/html", "base64");
		}

		@Override
		protected void onPreExecute()
		{
			Log.v("drmtests", "onPreExecute");
			mView.loadData(getResult().toString(), "text/html", "base64");
		}

		protected void onProgressUpdate(String value)
		{
			Log.v("drmtests", "onProgressUpdate= " + value);
			mView.loadData(getResult().toString(), "text/html", "base64");
		}

		protected abstract String run();

		/**
		 * @param mCtxt the mCtxt to set
		 */
		public void setContext(Context mCtxt) {
			this.mCtxt = mCtxt;
		}

		/**
		 * Test result setter
		 * @param aResult the test result to set
		 */
		public void setResult(String aResult) {
			this.report.setResult(aResult);
			publishProgress(getResult().toString());
		}

		public void setStatus(boolean status)
		{
			this.report.setStatus(status);
		}

		public void setView(WebView view)
		{
			Log.v("drmtests", "Set WebView on DrmTestItem");
			this.mView = view;
		}

		/**
		 * @return the status of the test
		 */
		public Boolean terminated()
		{
			return terminated;
		}

		@Override
		public String toString() {
			return synopsis;
		}
	}

	/**
	 * An array of drm test items.
	 */
	public static List<DrmTestItem> ITEMS = new ArrayList<DrmTestItem>();

	/**
	 * A map of drm test items, by ID.
	 */
	public static Map<String, DrmTestItem> ITEM_MAP = new HashMap<String, DrmTestItem>();

	private Context mCtxt = null;

	public TestsContent(Context ctxt)
	{
		setContext(ctxt);
	}

	public void addItem(DrmTestItem item) {
		ITEMS.add(item);
		ITEM_MAP.put(item.id, item);
	}

	/**
	 * @return the mCtxt
	 */
	public Context getContext() {
		return mCtxt;
	}

	/**
	 * @param mCtxt the mCtxt to set
	 */
	public void setContext(Context mCtxt) {
		this.mCtxt = mCtxt;
	}
}
