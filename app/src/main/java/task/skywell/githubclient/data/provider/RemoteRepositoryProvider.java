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

import task.skywell.githubclient.data.RemoteRepository;

/**
 * Created on 14.07.2017.
 *
 * @author Dimowner
 */
public class RemoteRepositoryProvider {

	//Prevent instantiation
	private RemoteRepositoryProvider() {}

	private static volatile RemoteRepository remoteRepository;

	public static RemoteRepository getInstance() {
		synchronized (RemoteRepository.class) {
			if (remoteRepository == null) {
				remoteRepository = new RemoteRepository();
			}
			return remoteRepository;
		}
	}
}
