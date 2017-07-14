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

package task.skywell.githubclient.data.model;

/**
 * Created on 14.07.2017.
 * @author Dimowner
 */
public class GitHubRepository {
	//		TODO: fix this class
	private final String name;
	private final String fullName;
	private final String url;
	private final Owner owner;

	public GitHubRepository(String name, String full_name, String url, Owner owner) {
		this.name = name;
		this.fullName = full_name;
		this.url = url;
		this.owner = owner;
	}

	public String getName() {
		return name;
	}

	public Owner getOwner() {
		return owner;
	}
}
