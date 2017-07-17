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

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;

import java.lang.ref.WeakReference;
import java.util.List;

import io.reactivex.Flowable;
import io.reactivex.Single;
import io.reactivex.schedulers.Schedulers;
import task.skywell.githubclient.data.room.AppDatabase;
import task.skywell.githubclient.data.room.RepositoriesDao;
import task.skywell.githubclient.data.room.RepositoryItemModel;
import timber.log.Timber;

/**
 * Created on 14.07.2017.
 * @author Dimowner
 */
public class LocalRepository implements IRepository {

	private static final String PREF_KEY_QUERY = "pref_key_query";

	private WeakReference<Context> weakContext;

	public LocalRepository(Context context) {
		this.weakContext = new WeakReference<>(context);
	}

	/**
	 * Check weak reference to {@link Context} still live.
	 * @return true if live, otherwise - false
	 */
	public boolean isContextLive() {
		return weakContext.get() != null;
	}

	public void setContext(Context weakContext) {
		this.weakContext = new WeakReference<>(weakContext);
	}

	@Override
	public Flowable<List<RepositoryItemModel>> searchRepositories(@NonNull String search) {
		return getRepositoriesDao().getAll()
				.subscribeOn(Schedulers.io());
	}

	/**
	 * Rewrite local cached repositories
	 * @param items new repositories to save.
	 */
	public void rewriteRepositories(List<RepositoryItemModel> items) {
		Single.just(items).map(data -> {
			getRepositoriesDao().deleteAll();
			getRepositoriesDao().insertAll(data.toArray(new RepositoryItemModel[data.size()]));
			return null;
		}).subscribeOn(Schedulers.io()).subscribe((o, throwable) -> Timber.e(throwable));
	}

	private RepositoriesDao getRepositoriesDao() {
		return AppDatabase.getInstance(weakContext.get()).repositoriesDao();
	}

	/**
	 * Save into preferences query string.
	 * @param str query string
	 */
	public void saveQueryString(String str) {
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(weakContext.get());
		SharedPreferences.Editor editor = prefs.edit();
		editor.putString(PREF_KEY_QUERY, str);
		editor.apply();
	}

	/**
	 * Check that repositories for query already cached
	 * @param queryStr query string to check
	 * @return true if repositories for specified query already cached, otherwise - false.
	 */
	boolean isCached(String queryStr) {
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(weakContext.get());
		String saveStr = prefs.getString(PREF_KEY_QUERY, "");
		if (saveStr.isEmpty()) {
			return false;
		} else if (saveStr.toLowerCase().equals(queryStr.toLowerCase())) {
			return true;
		}
		return false;
	}
}
