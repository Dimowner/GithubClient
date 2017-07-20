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

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Single;
import task.skywell.githubclient.data.room.RepositoryItemModel;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Created on 17.07.2017.
 * @author Dimowner
 */
public class RepositoryTest {

	private LocalRepository localRepository;
	private RemoteRepository remoteRepository;
	private Repository repository;

	@Mock
	Context mockContext;

	@Before
	public void beforeEachTest() {
		localRepository = mock(LocalRepository.class);
		remoteRepository = mock(RemoteRepository.class);
		repository = new Repository(localRepository, remoteRepository);
	}

	@Test
	public void searchRepositories_fromRemoteRepo() {
		when(localRepository.isCached("query")).thenReturn(false);
		List<RepositoryItemModel> testList = new ArrayList<>(1);
		testList.add(new RepositoryItemModel(5, "Repo", "master", "description", "http://test.com"));
		when(remoteRepository.searchRepositories("query")).thenReturn(Single.fromCallable(() -> testList));

		final RepositoryItemModel[] item = new RepositoryItemModel[1];

		repository.searchRepositories("query").subscribe(data -> item[0] = data.get(0));
		assertThat(item[0].getId()).isEqualTo(5);
		assertThat(item[0].getName()).isEqualTo("Repo");
		assertThat(item[0].getOwner()).isEqualTo("master");
		assertThat(item[0].getDescription()).isEqualTo("description");
		assertThat(item[0].getAvatar_url()).isEqualTo("http://test.com");
	}

	@Test
	public void searchRepositories_fromLocalRepo() {
		when(localRepository.isCached("query")).thenReturn(true);
		List<RepositoryItemModel> testList = new ArrayList<>(1);
		testList.add(new RepositoryItemModel(5, "Repo", "master", "description", "http://test.com"));
		when(localRepository.searchRepositories("query")).thenReturn(Single.fromCallable(() -> testList));

		final RepositoryItemModel[] item = new RepositoryItemModel[1];

		repository.searchRepositories("query").subscribe(data -> item[0] = data.get(0));

		assertThat(item[0].getId()).isEqualTo(5);
		assertThat(item[0].getName()).isEqualTo("Repo");
		assertThat(item[0].getOwner()).isEqualTo("master");
		assertThat(item[0].getDescription()).isEqualTo("description");
		assertThat(item[0].getAvatar_url()).isEqualTo("http://test.com");
	}

	@Test
	public void searchRepositories_fromRemoteRepo_error() {
		when(localRepository.isCached("query")).thenReturn(false);
		when(remoteRepository.searchRepositories("query")).thenReturn(Single.error(new NullPointerException("Error")));

		final String[] errorMessage = {""};
		repository.searchRepositories("query").subscribe(data -> {},
				throwable -> errorMessage[0] = throwable.getMessage());
		assertThat(errorMessage[0]).isEqualTo("Error");

	}

	@Test
	public void searchRepositories_fromRemoteRepo_emptyList() {
		when(localRepository.isCached("query")).thenReturn(false);
		List<RepositoryItemModel> testList = new ArrayList<>();
		when(remoteRepository.searchRepositories("query")).thenReturn(Single.fromCallable(() -> testList));

		final int[] size = {0};
		repository.searchRepositories("query").subscribe(data -> size[0] = data.size());
		assertThat(size[0]).isEqualTo(0);
	}
}
