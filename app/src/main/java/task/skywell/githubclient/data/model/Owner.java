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
public class Owner {
	private final int id;
	private final String login;
	private final String avatar_url;

	public Owner(int id, String login, String avatar_url) {
		this.id = id;
		this.login = login;
		this.avatar_url = avatar_url;
	}

	public String getLogin() {
		return login;
	}

	public int getId() {
		return id;
	}

	public String getAvatar_url() {
		return avatar_url;
	}

	@Override
	public String toString() {
		return "Owner{" +
				"id=" + id +
				", login='" + login + '\'' +
				", avatar_url='" + avatar_url + '\'' +
				'}';
	}
}
