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

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Flowable;
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

	private OnLoadListener onLoadListener;

	interface GitHub {
		@GET("/search/repositories")
		Flowable<SearchResult> searchRepositories(
				@Query("q") String search,
				@Query("sort") String order,
				@Query("page") Integer page);
	}

	@Override
	public Flowable<List<RepositoryItemModel>> searchRepositories(@NonNull String search) {

		HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
		interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
		OkHttpClient client = new OkHttpClient.Builder().addInterceptor(interceptor).build();

		// Create a very simple REST adapter which points the GitHub API.
		Retrofit retrofit = new Retrofit.Builder()
				.baseUrl(API_URL)
				.client(client)
				.addConverterFactory(GsonConverterFactory.create())
				.addCallAdapterFactory(RxJava2CallAdapterFactory.create())
				.build();

		// Create an instance of our GitHub API interface.
		GitHub github = retrofit.create(GitHub.class);

//		TODO: do two parallel queries
		return github.searchRepositories(search, "stars", 0)
				.map(this::convertModel)
				.doOnNext(data -> {
						if (onLoadListener != null) {
							onLoadListener.onRepositoriesLoad(search, data);
						}
					});
	}

	/**
	 * Convert {@link task.skywell.githubclient.data.model.GitHubRepository} models int {@link RepositoryItemModel} models
	 * @param data {@link task.skywell.githubclient.data.model.GitHubRepository} list
	 * @return {@link RepositoryItemModel} list
	 */
	private List<RepositoryItemModel> convertModel(SearchResult data) {
		List<RepositoryItemModel> listData = new ArrayList<>();

		GitHubRepository[] items = data.getItems();

		for (GitHubRepository e : items) {
			RepositoryItemModel item = new RepositoryItemModel(e.getId(), e.getName(),
							e.getOwner().getLogin(), e.getDescription(), e.getOwner().getAvatar_url());
			listData.add(item);
		}
		return listData;
	}
//
//	private SearchResult convert(SearchResult r1, SearchResult r2) {
//		int totalCount = r1.getTotalCount() + r2.getTotalCount();
//
//		GitHubRepository[] array1 = r1.getItems();
//		GitHubRepository[] array2 = r2.getItems();
//		GitHubRepository[] items = new GitHubRepository[totalCount];
//		Timber.v("a1 = " + array1.length + " a2 = " + array2.length);
//		for (int i = 0; i < array1.length; i++) {
//			items[i] = array1[i];
//		}
//		int lastIndex = array1.length-1;
//		for (int i = 0; i < array2.length; i++) {
//			items[lastIndex + i] = array2[i];
//		}
//		return new SearchResult(totalCount, items);
//	}

	public void setOnLoadListener(OnLoadListener onLoadListener) {
		this.onLoadListener = onLoadListener;
	}

	public interface OnLoadListener {
		void onRepositoriesLoad(String query, List<RepositoryItemModel> list);
	}
}
