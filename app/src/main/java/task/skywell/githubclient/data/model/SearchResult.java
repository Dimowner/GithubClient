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

import java.util.Arrays;

/**
 * Created on 14.07.2017.
 * @author Dimowner
 */
public  class SearchResult {
	private int total_count;
	private GitHubRepository[] items;

	public SearchResult(int total_count, GitHubRepository[] items) {
		this.total_count = total_count;
		this.items = items;
	}

	public int getTotalCount() {
		return total_count;
	}

	public GitHubRepository[] getItems() {
		return items;
	}

	@Override
	public String toString() {
		return "SearchResult{" +
				"total_count=" + total_count +
				", items=" + Arrays.toString(items) +
				'}';
	}
}
