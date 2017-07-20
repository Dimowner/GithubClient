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

import java.util.List;

import io.reactivex.Single;
import task.skywell.githubclient.data.room.RepositoryItemModel;

/**
 * Contract for application repositories
 * Created on 14.07.2017.
 * @author Dimowner
 */
interface IRepository {

	/**
	 * Do search repositories by specified query string.
	 * @param search Query string for search
	 * @return Search result contained in List of {@link RepositoryItemModel} items
	 */
	Single<List<RepositoryItemModel>> searchRepositories(@NonNull String search);

}
