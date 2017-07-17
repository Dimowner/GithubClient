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

	private final long id;
	private final String name;
	private final String description;
	private final int stargazers_count;
	private final Owner owner;

	public GitHubRepository(long id, String name, String description, int stargazers_count, Owner owner) {
		this.id = id;
		this.name = name;
		this.description = description;
		this.stargazers_count = stargazers_count;
		this.owner = owner;
	}


	public long getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public String getDescription() {
		return description;
	}

	public int getStargazers_count() {
		return stargazers_count;
	}

	public Owner getOwner() {
		return owner;
	}

	@Override
	public String toString() {
		return "GitHubRepository{" +
				"id=" + id +
				", name='" + name + '\'' +
				", description='" + description + '\'' +
				", stargazers_count=" + stargazers_count +
				", owner=" + owner +
				'}';
	}
}
