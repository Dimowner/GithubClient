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

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import task.skywell.githubclient.R;

/**
 * Created on 13.07.2017.
 * @author Dimowner
 */
public class DetailsFragment extends Fragment {

	public static final String EXTRAS_KEY_REPO_NAME = "repo_name";
	public static final String EXTRAS_KEY_REPO_DESCRIPTION = "repo_description";
	public static final String EXTRAS_KEY_OWNER = "owner";
	public static final String EXTRAS_KEY_OWNER_FACE_URL = "owner_face_url";

	public static DetailsFragment newInstance(String name, String decr, String owner, String faceUrl) {
		DetailsFragment fragment = new DetailsFragment();
		Bundle data = new Bundle();
		data.putString(EXTRAS_KEY_REPO_NAME, name);
		data.putString(EXTRAS_KEY_REPO_DESCRIPTION, decr);
		data.putString(EXTRAS_KEY_OWNER, owner);
		data.putString(EXTRAS_KEY_OWNER_FACE_URL, faceUrl);
		fragment.setArguments(data);
		return fragment;
	}

	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fragment_details, container, false);
	}

	@Override
	public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);

		TextView txtRepoName = (TextView) view.findViewById(R.id.details_repo_name);
		TextView txtRepoDescription= (TextView) view.findViewById(R.id.details_repo_description);
		TextView txtOwner= (TextView) view.findViewById(R.id.details_owner);
		ImageView ivFace = (ImageView) view.findViewById(R.id.details_owner_face);

		Bundle args = getArguments();

		if (args.containsKey(EXTRAS_KEY_REPO_NAME)) {
			txtRepoName.setText(args.getString(EXTRAS_KEY_REPO_NAME));
		}
		if (args.containsKey(EXTRAS_KEY_REPO_DESCRIPTION)) {
			txtRepoDescription.setText(args.getString(EXTRAS_KEY_REPO_DESCRIPTION));
		}
		if (args.containsKey(EXTRAS_KEY_OWNER)) {
			txtOwner.setText(args.getString(EXTRAS_KEY_OWNER));
		}

		if (args.containsKey(EXTRAS_KEY_OWNER_FACE_URL)) {
			ivFace.setScaleType(ImageView.ScaleType.CENTER_CROP);
			Glide.with(getContext())
					.load(args.getString(EXTRAS_KEY_OWNER_FACE_URL))
					.placeholder(R.drawable.account)
					.error(R.drawable.close)
					.into(ivFace);
		}
	}
}
