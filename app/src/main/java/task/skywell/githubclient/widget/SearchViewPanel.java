/*
 * Copyright 2017 Dmitriy Ponomarenko
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package task.skywell.githubclient.widget;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;
import task.skywell.githubclient.R;

/**
 * Created on 13.07.2017.
 * @author Dimowner
 */
public class SearchViewPanel extends LinearLayout {

	private EditText txtContent;
	private ImageButton btnSearch;
	private ProgressBar progress;

	private boolean inProgress = false;

	private OnSearchListener onSearchListener;

	public interface OnSearchListener {
		void onStartSearch(String str);
		void onCancelSearch();
		void onFinishSearch();
	}

	public SearchViewPanel(Context context) {
		this(context, null);
	}

	public SearchViewPanel(Context context, @Nullable AttributeSet attrs) {
		super(context, attrs);

		LayoutInflater inflater = (LayoutInflater) getContext().
				getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		inflater.inflate(R.layout.search_view_panel, this, true);

		txtContent = (EditText) findViewById(R.id.txt_content);
		btnSearch = (ImageButton) findViewById(R.id.btn_search);
		progress = (ProgressBar) findViewById(R.id.progress);

		txtContent.setSingleLine(true);
		txtContent.setOnEditorActionListener((v, actionId, event) -> {
			if ((event != null && (event.getKeyCode() == KeyEvent.KEYCODE_ENTER))
					|| (actionId == EditorInfo.IME_ACTION_DONE)) {
				handleClick();
			}
			return false;
		});
		btnSearch.setOnClickListener(v -> handleClick());
	}

	private void handleClick() {
		if (inProgress()) {
			cancelSearch();
		} else {
			startSearch();
		}
	}

	/**
	 * Method starts showing progress, and calls on {@link OnSearchListener}
	 * method {@link OnSearchListener#onStartSearch(String)}
	 */
	public void startSearch() {
		String searchStr = txtContent.getText().toString();
		if (!searchStr.isEmpty()) {
			inProgress = true;
			showSearchProgress();
			if (onSearchListener != null) {
				onSearchListener.onStartSearch(searchStr);
			}
		} else {
			Toast.makeText(getContext(), R.string.search_is_empty, Toast.LENGTH_LONG).show();
		}
	}

	/**
	 * Method cancels showing progress, and calls on {@link OnSearchListener}
	 * method {@link OnSearchListener#onCancelSearch()}
	 */
	public void cancelSearch() {
		inProgress = false;
		hideSearchProgress();
		if (onSearchListener != null) {
			onSearchListener.onCancelSearch();
		}
	}

	/**
	 * Method finishes showing progress, and calls on {@link OnSearchListener}
	 * method {@link OnSearchListener#onFinishSearch()}
	 */
	public void finishSearch() {
		inProgress = false;
		txtContent.setText(null);
		hideSearchProgress();
		if (onSearchListener != null) {
			onSearchListener.onFinishSearch();
		}
	}

	private void showSearchProgress() {
		progress.setVisibility(VISIBLE);
		btnSearch.setImageResource(R.drawable.close);
	}

	private void hideSearchProgress() {
		progress.setVisibility(GONE);
		btnSearch.setImageResource(R.drawable.magnify);
	}

	public boolean inProgress() {
		return inProgress;
	}

	public void setHint(CharSequence hint) {
		txtContent.setHint(hint);
	}

	public void setOnSearchListener(OnSearchListener onSearchListener) {
		this.onSearchListener = onSearchListener;
	}

	@Override
	public Parcelable onSaveInstanceState() {
		Parcelable superState = super.onSaveInstanceState();
		SavedState ss = new SavedState(superState);
		ss.inProgress = inProgress;
		ss.searchStr = txtContent.getText().toString();
		return ss;
	}

	@Override
	public void onRestoreInstanceState(Parcelable state) {
		SavedState ss = (SavedState) state;
		inProgress = ss.inProgress;
		txtContent.setText(ss.searchStr);
		super.onRestoreInstanceState(ss.getSuperState());

		if (inProgress) {
			showSearchProgress();
		} else {
			hideSearchProgress();
		}
	}

	/**
	 * View state
	 */
	private static class SavedState extends BaseSavedState {

		SavedState(Parcelable superState) {
			super(superState);
		}

		private SavedState(Parcel in) {
			super(in);
			boolean b[] = new boolean[1];
			in.readBooleanArray(b);
			inProgress = b[0];
			searchStr = in.readString();
		}

		@Override
		public void writeToParcel(Parcel out, int flags) {
			super.writeToParcel(out, flags);
			out.writeBooleanArray(new boolean[] {inProgress});
			out.writeString(searchStr);
		}

		boolean inProgress;

		String searchStr;

		public static final Parcelable.Creator<SavedState> CREATOR =
				new Parcelable.Creator<SavedState>() {
					@Override
					public SavedState createFromParcel(Parcel in) {
						return new SavedState(in);
					}

					@Override
					public SavedState[] newArray(int size) {
						return new SavedState[size];
					}
				};
	}
}
