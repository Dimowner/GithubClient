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
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.ArrayList;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;
import task.skywell.githubclient.R;
import task.skywell.githubclient.data.model.GitHubRepository;
import task.skywell.githubclient.data.model.SearchResult;
import task.skywell.githubclient.data.provider.RepositoryProvider;
import task.skywell.githubclient.widget.SearchViewPanel;
import timber.log.Timber;

/**
 * Created on 13.07.2017.
 * @author Dimowner
 */
public class RepositoriesFragment extends Fragment {

	private final String EXTRAS_KEY_ADAPTER_DATA = "adapter_data";

	private ConstraintLayout constraintLayout;

	private RecyclerView mRecyclerView;

	private RepositoriesRecyclerAdapter mAdapter;

	private SearchViewPanel mSearchPanel;

	private CompositeDisposable compositeDisposable = new CompositeDisposable();

	@Override
	public void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setRetainInstance(true);
	}

	@Override
	public void onDestroy() {
		compositeDisposable.dispose();
		super.onDestroy();
	}

	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fragment_repositories, container, false);
	}

	@Override
	public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		constraintLayout = (ConstraintLayout) view.findViewById(R.id.coordinator_layout);

		Timber.v("hasInternet = " + isConnectedToNetwork(getContext()));
		mSearchPanel = (SearchViewPanel) view.findViewById(R.id.search_panel);
		mSearchPanel.setHint(getString(R.string.repository_name));
		mSearchPanel.setOnSearchListener(new SearchViewPanel.OnSearchListener() {
			@Override
			public void onStartSearch(String str) {
				if (isConnectedToNetwork(getContext())) {
					searchRepositories(str);
				} else {
					mSearchPanel.cancelSearch();
					Toast.makeText(getContext(), R.string.no_connection, Toast.LENGTH_LONG).show();
				}
			}

			@Override
			public void onCancelSearch() {
				if (compositeDisposable.size() > 0) {
					compositeDisposable.clear();
				}
			}

			@Override
			public void onFinishSearch() {
			}
		});

		mRecyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
		mRecyclerView.setHasFixedSize(true);

		mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

		DividerItemDecoration divider = new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL);
		divider.setDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.recycler_divider));
		mRecyclerView.addItemDecoration(divider);

		mAdapter = new RepositoriesRecyclerAdapter();
		mAdapter.setItemClickListener((view1, position) -> {
			AppCompatActivity activity = (AppCompatActivity) getActivity();
			activity.getSupportFragmentManager()
					.beginTransaction()
					.add(R.id.fragment, new DetailsFragment())
					.addToBackStack("details")
					.commit();

			if (activity.getSupportActionBar() != null) {
				activity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
			}
		});

		mRecyclerView.setAdapter(mAdapter);
	}

	private void searchRepositories(String str) {
		if (str != null) {
			compositeDisposable.add(
					RepositoryProvider.getInstance()
							.searchRepositories(str)
							.map(this::convertModel)
							.subscribeOn(Schedulers.io())
							.observeOn(AndroidSchedulers.mainThread())
							.subscribe(this::displayData, this::handleError));
		}
	}

	/**
	 * Convert {@link task.skywell.githubclient.data.model.GitHubRepository} models int {@link ListItem} models
	 * @param data {@link task.skywell.githubclient.data.model.GitHubRepository} list
	 * @return {@link ListItem} list
	 */
	private List<ListItem> convertModel(SearchResult data) {
		List<ListItem> listData = new ArrayList<>();

		GitHubRepository[] items = data.getItems();

		for (GitHubRepository e : items) {
			ListItem item = new ListItem(e.getName(), e.getOwner().getLogin());
			listData.add(item);
		}
		return listData;
	}

	private void displayData(List<ListItem> data) {
		mAdapter.setData(data);
		mSearchPanel.finishSearch();
	}

	private void handleError(Throwable throwable) {
		Timber.e(throwable);
		mSearchPanel.cancelSearch();

		Snackbar
				.make(constraintLayout, R.string.error_on_query, Snackbar.LENGTH_LONG)
				.setAction(R.string.retry, view -> mSearchPanel.startSearch())
				.show();
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		if (mAdapter != null) {
			outState.putParcelable(EXTRAS_KEY_ADAPTER_DATA, mAdapter.onSaveInstanceState());
		}
	}

	@Override
	public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
		super.onViewStateRestored(savedInstanceState);
		if (savedInstanceState != null && savedInstanceState.containsKey(EXTRAS_KEY_ADAPTER_DATA)) {
			if (mAdapter == null) {
				mAdapter = new RepositoriesRecyclerAdapter();
				mRecyclerView.setAdapter(mAdapter);
			}
			mAdapter.onRestoreInstanceState(savedInstanceState.getParcelable(EXTRAS_KEY_ADAPTER_DATA));
		}
	}

	/**
	 * Check connectivity to network
	 * @param context app context
	 * @return true if connected otherwise false
	 */
	public boolean isConnectedToNetwork(Context context) {
		NetworkInfo networkInfo = ((ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo();
		return (networkInfo != null && networkInfo.isConnected());
	}
}
