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

package task.skywell.githubclient.ui;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.v4.view.AbsSavedState;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableStringBuilder;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import task.skywell.githubclient.R;
import task.skywell.githubclient.AndroidUtils;

/**
 * Created on 14.07.2017.
 * @author Dimowner
 */
class RepositoriesRecyclerAdapter extends RecyclerView.Adapter<RepositoriesRecyclerAdapter.ItemViewHolder> {

	private List<ListItem> mShowingData;

	private ItemClickListener itemClickListener;

	class ItemViewHolder extends RecyclerView.ViewHolder {
		View view;

		ItemViewHolder(View itemView) {
			super(itemView);
			this.view = itemView;
		}
	}

	RepositoriesRecyclerAdapter() {
		this.mShowingData = Collections.emptyList();
	}

	@Override
	public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

		//Create list item programmatically. It is just TextView
		Context ctx = parent.getContext();
		TextView view = new TextView(ctx);
		int standardPad = (int) ctx.getResources().getDimension(R.dimen.padding_standard);
		int tinyPad = (int) ctx.getResources().getDimension(R.dimen.padding_tiny);
		view.setPadding(standardPad, tinyPad, standardPad, tinyPad);
		FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
				FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT);
		view.setLayoutParams(params);
		view.setLineSpacing(tinyPad, view.getLineSpacingMultiplier());
		return new ItemViewHolder(view);
	}

	@Override
	public void onBindViewHolder(final ItemViewHolder holder, final int position) {
		holder.view.setOnClickListener(v -> {
			if (itemClickListener != null) {
				itemClickListener.onItemClick(v, holder.getAdapterPosition());
			}
		});

//		TODO: should be added check that one line fit on screen without move to next line.
		//Build styled 2 lines string
		SpannableStringBuilder builder = new SpannableStringBuilder()
				.append(AndroidUtils.formatString(holder.view.getContext(),
						mShowingData.get(position).getName(),
						R.style.ListItemMainTextAppearance))
				.append("\n")
				.append(AndroidUtils.formatString(holder.view.getContext(),
						mShowingData.get(position).getName(),
						R.style.ListItemSecondaryTextAppearance));

		((TextView)holder.view).setText(builder.subSequence(0, builder.length()));
	}

	@Override
	public int getItemCount() {
		return mShowingData.size();
	}

	ListItem getItem(int pos) {
		return mShowingData.get(pos);
	}

	public void setData(List<ListItem> data) {
		if (data != null) {
			this.mShowingData = data;
		} else {
			this.mShowingData = Collections.emptyList();
		}
		notifyDataSetChanged();
	}

	void setItemClickListener(ItemClickListener itemClickListener) {
		this.itemClickListener = itemClickListener;
	}

	/**
	 * Save adapters state
	 * @return adapter state.
	 */
	Parcelable onSaveInstanceState() {
		SavedState ss = new SavedState(AbsSavedState.EMPTY_STATE);
		ss.items = mShowingData.toArray(new ListItem[0]);
		return ss;
	}

	/**
	 * Restore adapters state
	 * @param state Adapter state.
	 */
	void onRestoreInstanceState(Parcelable state) {
		SavedState ss = (SavedState) state;
		mShowingData = new ArrayList<>();
		Collections.addAll(mShowingData, ss.items);
		notifyDataSetChanged();
	}


	/**
	 * Object state
	 */
	private static class SavedState extends View.BaseSavedState {
		SavedState(Parcelable superState) {
			super(superState);
		}

		private SavedState(Parcel in) {
			super(in);
			items = (ListItem[]) in.readParcelableArray(getClass().getClassLoader());
		}

		@Override
		public void writeToParcel(Parcel out, int flags) {
			super.writeToParcel(out, flags);
			out.writeParcelableArray(items, flags);
		}

		ListItem[] items;

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

	interface ItemClickListener{
		void onItemClick(View view, int position);
	}
}

