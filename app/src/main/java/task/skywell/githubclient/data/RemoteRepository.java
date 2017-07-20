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

package task.skywell.githubclient.data;

import android.support.annotation.NonNull;
import android.support.annotation.VisibleForTesting;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Single;
import io.reactivex.schedulers.Schedulers;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Query;
import task.skywell.githubclient.data.model.GitHubRepository;
import task.skywell.githubclient.data.model.SearchResult;
import task.skywell.githubclient.data.room.RepositoryItemModel;

/**
 * Created on 14.07.2017.
 * @author Dimowner
 */
public class RemoteRepository implements IRepository {

	private static final String API_URL = "https://api.github.com";

	private Retrofit retrofit;

	@VisibleForTesting
	GitHub github;

	private OnLoadListener onLoadListener;

	interface GitHub {
		@GET("/search/repositories")
		Single<SearchResult> searchRepositories(
				@Query("q") String search,
				@Query("sort") String order,
				@Query("page") Integer page);
	}

	@Override
	public Single<List<RepositoryItemModel>> searchRepositories(@NonNull String search) {

		if (retrofit == null) {
			HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
			interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
			OkHttpClient client = new OkHttpClient.Builder().addInterceptor(interceptor).build();

			// Create a very simple REST adapter which points the GitHub API.
			retrofit = new Retrofit.Builder()
					.baseUrl(API_URL)
					.client(client)
					.addConverterFactory(GsonConverterFactory.create())
					.addCallAdapterFactory(RxJava2CallAdapterFactory.create())
					.build();
		}

		if (github == null) {
			// Create an instance of our GitHub API interface.
			github = retrofit.create(GitHub.class);
		}

		return Single.zip(
				github.searchRepositories(search, "stars", 1)
						.map(this::convertModel)
						.subscribeOn(Schedulers.io()),
				github.searchRepositories(search, "stars", 2)
						.map(this::convertModel)
						.subscribeOn(Schedulers.io()),
				this::mergeResults)
				.doOnSuccess(data -> {
							if (onLoadListener != null) {
								onLoadListener.onRepositoriesLoad(search, data);
							}
						});
	}

	/**
	 * Convert {@link task.skywell.githubclient.data.model.SearchResult} models into list of {@link RepositoryItemModel} models
	 * @param data {@link task.skywell.githubclient.data.model.SearchResult}
	 * @return Coverted {@link RepositoryItemModel} list
	 */
	@VisibleForTesting
	List<RepositoryItemModel> convertModel(SearchResult data) {
		List<RepositoryItemModel> listData = new ArrayList<>();

		GitHubRepository[] items = data.getItems();

		for (GitHubRepository e : items) {
			RepositoryItemModel item = new RepositoryItemModel(e.getId(), e.getName(),
							e.getOwner().getLogin(), e.getDescription(), e.getOwner().getAvatar_url());
			listData.add(item);
		}
		return listData;
	}

	/**
	 * Merge two lists into one
	 * @param r1 List of {@link RepositoryItemModel} items from first query
	 * @param r2 List of {@link RepositoryItemModel} items from second query
	 * @return Combined result
	 */
	@VisibleForTesting
	List<RepositoryItemModel> mergeResults(List<RepositoryItemModel> r1, List<RepositoryItemModel> r2) {
		List<RepositoryItemModel> list = new ArrayList<>(r1.size() + r2.size());
		list.addAll(r1);
		list.addAll(r2);

		return list;
	}

	public void setOnLoadListener(OnLoadListener onLoadListener) {
		this.onLoadListener = onLoadListener;
	}

	/**
	 * Interface for transfer query results into {@link LocalRepository} for caching.
	 */
	public interface OnLoadListener {
		void onRepositoriesLoad(String query, List<RepositoryItemModel> list);
	}
}
