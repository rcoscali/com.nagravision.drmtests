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

import android.content.Context;
import android.drm.DrmErrorEvent;
import android.drm.DrmEvent;
import android.drm.DrmInfoEvent;
import android.drm.DrmManagerClient;
import android.util.Log;

public class GetDrmEnginesTest extends TestsContent.DrmTestItem
{
	private DrmManagerClient mDrmMgrClt = null;
	private final String TAG = "drmtests";

	public GetDrmEnginesTest(Context ctxt, String id, String content)
	{
		super(id, content);
	}

	@Override
	protected String run()
	{
		appendLog("Instantiate DrmManagerClient\n");
		mDrmMgrClt = new DrmManagerClient(getContext());

		appendLog("Add DRM error listener\n");
		mDrmMgrClt.setOnErrorListener(new DrmManagerClient.OnErrorListener() {

			@Override
			public void onError(DrmManagerClient client, DrmErrorEvent event) {
				String logstr = "DRM Error: " + event.getMessage();
				Log.v(TAG, logstr);
				appendLog(logstr);
			}
		});
		appendLog("Add DRM event listener\n");
		mDrmMgrClt.setOnEventListener(new DrmManagerClient.OnEventListener() {

			@Override
			public void onEvent(DrmManagerClient client, DrmEvent event) {
				String logstr = "DRM Event: " + event.getMessage();
				Log.v(TAG, logstr);
				appendLog(logstr);
			}
		});
		appendLog("Add DRM info listener\n");
		mDrmMgrClt.setOnInfoListener(new DrmManagerClient.OnInfoListener() {

			@Override
			public void onInfo(DrmManagerClient client, DrmInfoEvent event) {
				String logstr = "DRM Info: " + event.getMessage();
				Log.v(TAG, logstr);
				appendLog(logstr);
			}
		});

		appendLog("Get available DrmEngines\n");
		String[] drmEngines = mDrmMgrClt.getAvailableDrmEngines();
		appendLog("Got " + drmEngines.length + " DRM engines:\n");

		boolean status = false;
		for (int i = 0; i < drmEngines.length; i++)
		{
			appendLog("DRM #" + i + ": " + drmEngines[i] + "\n");
			status = status | ("NagraVision DRM plug-in".equals(drmEngines[i]));
		}

		setStatus(status);
		return getResult().toString();
	}


}
