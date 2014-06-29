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

package com.nagravision.drmtests;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;

import com.nagravision.drmtests.tests.TestsContent;

/**
 * A fragment representing a single ADrmTest detail screen.
 * This fragment is either contained in a {@link ADrmTestListActivity}
 * in two-pane mode (on tablets) or a {@link ADrmTestDetailActivity}
 * on handsets.
 */
public class ADrmTestDetailFragment extends Fragment {
	/**
	 * The fragment argument representing the item ID that this fragment
	 * represents.
	 */
	public static final String ARG_ITEM_ID = "item_id";

	/**
	 * The dummy content this fragment is presenting.
	 */
	private TestsContent.DrmTestItem mItem;

	private View mRootView = null;

	/**
	 * Mandatory empty constructor for the fragment manager to instantiate the
	 * fragment (e.g. upon screen orientation changes).
	 */
	public ADrmTestDetailFragment() {
		Log.v("drmtests", "ADrmTestDetailFragment constructor");
	}

	/**
	 * @return the mRootView
	 */
	public View getRootView() {
		return mRootView;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		Log.v("drmtests", "ADrmTestDetailFragment creation");

		if (getArguments().containsKey(ARG_ITEM_ID)) {
			// Load the dummy content specified by the fragment
			// arguments. In a real-world scenario, use a Loader
			// to load content from a content provider.
			mItem = TestsContent.ITEM_MAP.get(getArguments().getString(ARG_ITEM_ID));
			Log.v("drmtests", "ADrmTestDetailFragment item = " + getArguments().getString(ARG_ITEM_ID));
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		Log.v("drmtests", "ADrmTestDetailFragment view creation");

		setRootView(inflater.inflate(R.layout.fragment_adrmtest_detail, container, false));

		// Show the test content as text in a TextView.
		if (mItem != null) {
			mItem.init();
			Log.v("drmtests", "ADrmTestDetailFragment start item");
			mItem.setView(((WebView) getRootView().findViewById(R.id.adrmtest_detail)));
			WebView wv = (WebView) getRootView().findViewById(R.id.adrmtest_detail);
			wv.getSettings().setDefaultFontSize(10);
			wv.getSettings().setDefaultFixedFontSize(10);
			wv.loadData(mItem.getHtmlData(), "text/html", "base64");
			mItem.execute();
		}

		return getRootView();
	}

	/**
	 * @param mRootView the mRootView to set
	 */
	public void setRootView(View mRootView) {
		this.mRootView = mRootView;
	}
}
