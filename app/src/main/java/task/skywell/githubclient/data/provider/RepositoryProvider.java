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

package task.skywell.githubclient.data.provider;

import task.skywell.githubclient.data.Repository;

/**
 * Created on 14.07.2017.
 * @author Dimowner
 */
public class RepositoryProvider {

	//Prevent instantiation
	private RepositoryProvider() {}

	private static volatile Repository repository;

	public static Repository getInstance() {
		synchronized (RepositoryProvider.class) {
			if (repository == null) {
				repository = new Repository(
						LocalRepositoryProvider.getInstance(),
						RemoteRepositoryProvider.getInstance()
				);
			}
			return repository;
		}
	}
}
