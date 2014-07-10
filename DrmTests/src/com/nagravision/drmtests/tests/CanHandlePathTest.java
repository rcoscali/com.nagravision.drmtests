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

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.Vector;

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

		boolean status = true;

		Vector<String> mpdFiles = new Vector<String>();
		mpdFiles.addElement("CENC_SD_time_MPD.mpd");
		mpdFiles.addElement("CENC_HD_time_MPD.mpd");
		mpdFiles.addElement("manifest1.mpd");
		mpdFiles.addElement("manifest2.mpd");
		mpdFiles.addElement("1.mpd");
		mpdFiles.addElement("2.mpd");
		
		Vector<String> directories = new Vector<String>();
		directories.addElement(getContext().getPackageCodePath());
		directories.addElement(getContext().getPackageResourcePath());
		directories.addElement(getContext().getCacheDir().getAbsolutePath());
		directories.addElement(Environment.getDataDirectory().getAbsolutePath());
		directories.addElement(Environment.getDownloadCacheDirectory().getAbsolutePath());
		directories.addElement(Environment.getExternalStorageDirectory().getAbsolutePath());
		directories.addElement(Environment.getExternalStoragePublicDirectory(Context.STORAGE_SERVICE).getAbsolutePath());
		directories.addElement(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MOVIES).getAbsolutePath());
		directories.addElement(Environment.getRootDirectory().getAbsolutePath());
		directories.addElement(Environment.getRootDirectory().getAbsolutePath()+"/tmp");
		
		boolean canhdl = false;
		
		for (Enumeration<String> e = mpdFiles.elements();
			 e.hasMoreElements();)			 
		{
			boolean found = false;
			String filename = (String)e.nextElement();
			for (Enumeration<String> f = directories.elements();
				 f.hasMoreElements();)
			{			
				String dirname = (String)f.nextElement();
				File mpd = new File(dirname, filename);
				if (mpd.canRead())
				{
					found = true;
					appendLog("Querying " + mpd.getAbsolutePath());
					canhdl = mDrmMgrClt.canHandle(mpd.getAbsolutePath(), null);
					appendLog("DRM : " + canhdl + " "
							+ (canhdl ? "<PASS>" : "<FAIL>") + "\n");
					status = status & (canhdl == true);
				}
			}				
			if (!found)
			{
				// Didn't found the MPD file: create one with data from Assets
				File mpd = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MOVIES), filename);
				try {
					if (mpd.createNewFile())
					{
						FileOutputStream fos = null;
						InputStream ims = null;
						try {
							fos = new FileOutputStream(mpd);
							ims = getContext().getAssets().open(filename);
							byte[] buf = new byte[4096];
							while (ims.available() > 0)
							{
								int rdsz = ims.read(buf, 0, Math.max(1024, ims.available()));
								fos.write(buf, 0, rdsz);
								fos.flush();
							}
							fos.close();
							ims.close();
						} catch (IOException e1) {
							Log.e("drmTests", e1.toString());
							e1.printStackTrace();
						}
						if (mpd.canRead())
						{
							found = true;
							appendLog("Querying " + mpd.getAbsolutePath());
							canhdl = mDrmMgrClt.canHandle(mpd.getAbsolutePath(), null);
							appendLog("DRM : " + canhdl + " "
									+ (canhdl ? "<PASS>" : "<FAIL>") + "\n");
							status = status & (canhdl == true);
						}
					}
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		}
		
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
