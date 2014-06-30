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
import android.os.Environment;
import android.util.Log;
//import android.os.Environment;

public class CanHandlePathTest extends TestsContent.DrmTestItem
{
	private DrmManagerClient mDrmMgrClt = null;
	private final String TAG = "drmtests";

	public CanHandlePathTest(Context ctxt, String id, String content)
	{
		super(ctxt, id, content);
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

		String sdcardPath = Environment.getExternalStoragePublicDirectory(
				Context.STORAGE_SERVICE).getPath();
		// sdcardPath = "/system/tmp";
		boolean status = true;

		appendLog("Querying " + sdcardPath + "/CENC_SD_time_MPD.mpd\n");
		boolean canhdl = mDrmMgrClt.canHandle(sdcardPath + "/CENC_SD_time_MPD.mpd", null);
		appendLog("DRM fwk say: " + canhdl + " "
				+ (canhdl ? "<PASSED>" : "<FAILED>") + "\n");
		status = status & (canhdl == true);

		appendLog("Querying " + sdcardPath + "/CENC_HD_time_MPD.mpd");
		canhdl = mDrmMgrClt.canHandle(sdcardPath + "/CENC_HD_time_MPD.mpd", null);
		appendLog("DRM fwk say: " + canhdl + " "
				+ (canhdl ? "<PASSED>" : "<FAILED>") + "\n");
		status = status & (canhdl == true);

		appendLog("Querying " + sdcardPath + "/manifest1.mpd");
		canhdl = mDrmMgrClt.canHandle(sdcardPath + "/manifest1.mpd", null);
		appendLog("DRM fwk say: " + canhdl + " "
				+ (canhdl ? "<PASSED>" : "<FAILED>") + "\n");
		status = status & (canhdl == true);

		appendLog("Querying " + sdcardPath + "/manifest2.mpd");
		canhdl = mDrmMgrClt.canHandle(sdcardPath + "/manifest2.mpd", null);
		appendLog("DRM fwk say: " + canhdl + " "
				+ (canhdl ? "<PASSED>" : "<FAILED>") + "\n");
		status = status & (canhdl == true);

		appendLog("Querying " + sdcardPath + "/1.mpd");
		canhdl = mDrmMgrClt.canHandle(sdcardPath + "/manifest1.mpd", null);
		appendLog("DRM fwk say: " + canhdl + " "
				+ (canhdl ? "<FAILED>" : "<PASSED>") + "\n");
		status = status & (canhdl != true);

		appendLog("Querying " + sdcardPath + "/2.mpd");
		canhdl = mDrmMgrClt.canHandle(sdcardPath + "/manifest2.mpd", null);
		appendLog("DRM fwk say: " + canhdl + " "
				+ (canhdl ? "<FAILED>" : "<PASSED>") + "\n");
		status = status & (canhdl != true);

		appendLog("Querying video/mp4 mimeType");
		canhdl = mDrmMgrClt.canHandle("", "video/mp4");
		appendLog("DRM fwk say: " + canhdl + " "
				+ (canhdl ? "<FAILED>" : "<PASSED>") + "\n");
		status = status & (canhdl != true);

		appendLog("Querying audio/mp4 mimeType");
		canhdl = mDrmMgrClt.canHandle("", "audio/mp4");
		appendLog("DRM fwk say: " + canhdl + " "
				+ (canhdl ? "<FAILED>" : "<PASSED>") + "\n");
		status = status & (canhdl != true);

		appendLog("Querying video/m4v mimeType");
		canhdl = mDrmMgrClt.canHandle("", "video/m4v");
		appendLog("DRM fwk say: " + canhdl + " "
				+ (canhdl ? "<FAILED>" : "<PASSED>") + "\n");
		status = status & (canhdl != true);

		appendLog("Querying audio/m4a mimeType");
		canhdl = mDrmMgrClt.canHandle("", "audio/m4a");
		appendLog("DRM fwk say: " + canhdl + " "
				+ (canhdl ? "<FAILED>" : "<PASSED>") + "\n");
		status = status & (canhdl != true);

		setStatus(status);
		return getResult().toString();
	}
}
