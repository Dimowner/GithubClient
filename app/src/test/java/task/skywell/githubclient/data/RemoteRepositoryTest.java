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

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;

import task.skywell.githubclient.data.model.GitHubRepository;
import task.skywell.githubclient.data.model.Owner;
import task.skywell.githubclient.data.model.SearchResult;
import task.skywell.githubclient.data.room.RepositoryItemModel;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * Created on 20.07.2017.
 * @author Dimowner
 */
public class RemoteRepositoryTest {

	@Mock
	private RemoteRepository.GitHub gitHub;

	private RemoteRepository remoteRepository;

	@Before
	public void onBeforeEachTest() {
		MockitoAnnotations.initMocks(this);
		remoteRepository = new RemoteRepository();
	}

	@Test
	public void searchRepositories() {
//		TODO: fix this test

	}

	@Test
	public void convertModelTest() {
		Owner owner = new Owner(1, "owner", "http://url.com");
		GitHubRepository gitRepo = new GitHubRepository(0, "name", "description", 12, owner);
		SearchResult searchResult = new SearchResult(1, new GitHubRepository[] {gitRepo});

		List<RepositoryItemModel> list = remoteRepository.convertModel(searchResult);

		assertThat(list).isNotNull();
		assertThat(list.size()).isEqualTo(1);

		RepositoryItemModel item = list.get(0);

		assertThat(item.getId()).isEqualTo(0);
		assertThat(item.getName()).isEqualTo("name");
		assertThat(item.getOwner()).isEqualTo("owner");
		assertThat(item.getDescription()).isEqualTo("description");
		assertThat(item.getAvatar_url()).isEqualTo("http://url.com");
	}

	@Test
	public void mergeResultsTest() {
		List<RepositoryItemModel> testList1 = new ArrayList<>(1);
		testList1.add(new RepositoryItemModel(1, "name1", "owner1", "description1", "http://test.com1"));

		List<RepositoryItemModel> testList2 = new ArrayList<>(2);
		testList2.add(new RepositoryItemModel(2, "name2", "owner2", "description2", "http://test.com2"));
		testList2.add(new RepositoryItemModel(3, "name3", "owner3", "description3", "http://test.com3"));

		List<RepositoryItemModel> result = remoteRepository.mergeResults(testList1, testList2);

		assertThat(result).isNotNull();
		assertThat(result.size()).isEqualTo(3);

		RepositoryItemModel item1 = result.get(0);
		assertThat(item1.getId()).isEqualTo(1);
		assertThat(item1.getName()).isEqualTo("name1");
		assertThat(item1.getOwner()).isEqualTo("owner1");
		assertThat(item1.getDescription()).isEqualTo("description1");
		assertThat(item1.getAvatar_url()).isEqualTo("http://test.com1");

		RepositoryItemModel item3 = result.get(2);
		assertThat(item3.getId()).isEqualTo(3);
		assertThat(item3.getName()).isEqualTo("name3");
		assertThat(item3.getOwner()).isEqualTo("owner3");
		assertThat(item3.getDescription()).isEqualTo("description3");
		assertThat(item3.getAvatar_url()).isEqualTo("http://test.com3");
	}
}
