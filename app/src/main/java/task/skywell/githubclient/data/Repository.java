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

import io.reactivex.Single;
import task.skywell.githubclient.data.model.SearchResult;
import timber.log.Timber;

/**
 * Created on 14.07.2017.
 * @author Dimowner
 */
public class Repository implements IRepository {

	private LocalRepository localRepository;
	private RemoteRepository remoteRepository;

	public Repository(@NonNull LocalRepository localRepository, @NonNull RemoteRepository remoteRepository) {
		this.localRepository = localRepository;
		this.remoteRepository = remoteRepository;
	}

	@Override
	public Single<SearchResult> searchRepositories(@NonNull String search) {
		Timber.v("searchRepository str = " + search);
		if (isCached(search)) {
			return localRepository.searchRepositories(search);
		} else {
			return remoteRepository.searchRepositories(search);
		}
	}

	//TODO: implement this method
	private boolean isCached(String str) {
		return false;
	}
}
