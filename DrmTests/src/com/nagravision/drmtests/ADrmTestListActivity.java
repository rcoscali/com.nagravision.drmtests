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

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;

import com.nagravision.drmtests.tests.CanHandlePathTest;
import com.nagravision.drmtests.tests.CanHandleUriTest;
import com.nagravision.drmtests.tests.GetDrmEnginesTest;
import com.nagravision.drmtests.tests.TestsContent;

/**
 * An activity representing a list of TheDrmTests. This activity
 * has different presentations for handset and tablet-size devices. On
 * handsets, the activity presents a list of items, which when touched,
 * lead to a {@link ADrmTestDetailActivity} representing
 * item details. On tablets, the activity presents the list of items and
 * item details side-by-side using two vertical panes.
 * <p>
 * The activity makes heavy use of fragments. The list of items is a
 * {@link ADrmTestListFragment} and the item details
 * (if present) is a {@link ADrmTestDetailFragment}.
 * <p>
 * This activity also implements the required
 * {@link ADrmTestListFragment.Callbacks} interface
 * to listen for item selections.
 */
public class ADrmTestListActivity extends FragmentActivity
implements ADrmTestListFragment.Callbacks {

	/**
	 * Whether or not the activity is in two-pane mode, i.e. running on a tablet
	 * device.
	 */
	private boolean mTwoPane;
	private TestsContent mTestsContent = null;

	public ADrmTestListActivity() {
		Log.v("drmtests", "Adding tests items / context = " + this);
		// Add all tests
		// TODO: use PM & reflexion API to add tests classes
		mTestsContent = new TestsContent(this);
		mTestsContent.addItem(new GetDrmEnginesTest(this, "1",
				"Get DRM engines"));
		mTestsContent.addItem(new CanHandleUriTest(this, "2",
				"Can Handle(URI, mime)"));
		mTestsContent.addItem(new CanHandlePathTest(this, "3",
				"Can Handle(Path, mime)"));
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (savedInstanceState == null)
		{
			Log.v("drmtests", "getPackageCodePath:"
					+ getBaseContext().getPackageCodePath());
			Log.v("drmtests", "getPackageResourcePath:"
					+ getBaseContext().getPackageResourcePath());
			Log.v("drmtests", "getCacheDir:"
					+ getBaseContext().getCacheDir().getAbsolutePath());
			// Log.v("drmtests", "getExternalCacheDir:"
			// + getBaseContext().getExternalCacheDir().getAbsolutePath());
			Log.v("drmtests",
					"getDataDirectory:" + Environment.getDataDirectory());
			Log.v("drmtests",
					"getDownloadCacheDirectory:"
							+ Environment.getDownloadCacheDirectory());
			Log.v("drmtests",
					"getExternalStorageDirectory:"
							+ Environment.getExternalStorageDirectory());
			Log.v("drmtests",
					"getExternalStoragePublicDirectory:"
							+ Environment
							.getExternalStoragePublicDirectory(STORAGE_SERVICE));
			Log.v("drmtests",
					"getRootDirectory:" + Environment.getRootDirectory());
		}
		setContentView(R.layout.activity_adrmtest_list);

		if (findViewById(R.id.adrmtest_detail_container) != null) {
			// The detail container view will be present only in the
			// large-screen layouts (res/values-large and
			// res/values-sw600dp). If this view is present, then the
			// activity should be in two-pane mode.
			mTwoPane = true;

			// In two-pane mode, list items should be given the
			// 'activated' state when touched.
			((ADrmTestListFragment) getSupportFragmentManager()
					.findFragmentById(R.id.adrmtest_list))
					.setActivateOnItemClick(true);
		}
	}

	/**
	 * Callback method from {@link ADrmTestListFragment.Callbacks}
	 * indicating that the item with the given ID was selected.
	 */
	@Override
	public void onItemSelected(String id) {
		if (mTwoPane) {
			// In two-pane mode, show the detail view in this activity by
			// adding or replacing the detail fragment using a
			// fragment transaction.
			Bundle arguments = new Bundle();
			arguments.putString(ADrmTestDetailFragment.ARG_ITEM_ID, id);
			ADrmTestDetailFragment fragment = new ADrmTestDetailFragment();
			fragment.setArguments(arguments);
			getSupportFragmentManager().beginTransaction()
			.replace(R.id.adrmtest_detail_container, fragment)
			.commit();

		} else {
			// In single-pane mode, simply start the detail activity
			// for the selected item ID.
			Intent detailIntent = new Intent(this, ADrmTestDetailActivity.class);
			detailIntent.putExtra(ADrmTestDetailFragment.ARG_ITEM_ID, id);
			startActivity(detailIntent);
		}
	}
}
