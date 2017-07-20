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
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Single;
import io.reactivex.observers.TestObserver;
import io.reactivex.subscribers.TestSubscriber;
import task.skywell.githubclient.data.room.RepositoryItemModel;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

/**
 * Created on 17.07.2017.
 * @author Dimowner
 */
public class RepositoryTest {

	private static final String QUERY = "query";

	@Mock
	Context mockContext;

	@Mock
	private LocalRepository localRepository;

	@Mock
	private RemoteRepository remoteRepository;

	private Repository repository;


	@Before
	public void beforeEachTest() {
		MockitoAnnotations.initMocks(this);
		repository = new Repository(localRepository, remoteRepository);
	}

	@Test
	public void searchRepositories_fromRemoteRepo() {
		when(localRepository.isCached(QUERY)).thenReturn(false);
		List<RepositoryItemModel> testList = new ArrayList<>(1);
		testList.add(new RepositoryItemModel(1, "name", "owner", "description", "http://test.com"));
		when(remoteRepository.searchRepositories(QUERY)).thenReturn(Single.fromCallable(() -> testList));

		TestObserver<List<RepositoryItemModel>> observer = new TestObserver<>();
		repository.searchRepositories(QUERY).subscribe(observer);

		observer.assertComplete();
		observer.assertNoErrors();
		observer.assertValueCount(1);
		observer.assertValues(testList);
	}

	@Test
	public void searchRepositories_fromRemoteRepo_error() {
		Exception exception = new RuntimeException("exception");

		when(localRepository.isCached(QUERY)).thenReturn(false);
		when(remoteRepository.searchRepositories(QUERY)).thenReturn(Single.error(exception));

		TestObserver<List<RepositoryItemModel>> observer = new TestObserver<>();
		repository.searchRepositories(QUERY).subscribe(observer);

		observer.assertError(exception);
		observer.assertNotComplete();
		observer.assertErrorMessage("exception");
	}

	@Test
	public void searchRepositories_fromLocalRepo() {

		when(localRepository.isCached(QUERY)).thenReturn(true);
		List<RepositoryItemModel> testList = new ArrayList<>(1);
		testList.add(new RepositoryItemModel(1, "name", "owner", "description", "http://test.com"));
		when(localRepository.searchRepositories(QUERY)).thenReturn(Single.fromCallable(() -> testList));

		TestObserver<List<RepositoryItemModel>> observer = new TestObserver<>();
		repository.searchRepositories(QUERY).subscribe(observer);

		observer.assertComplete();
		observer.assertNoErrors();
		observer.assertValueCount(1);
		observer.assertValues(testList);
	}

	@Test
	public void searchRepositories_fromLocalRepo_error() {
		Exception exception = new RuntimeException("exception");

		when(localRepository.isCached(QUERY)).thenReturn(true);
		when(localRepository.searchRepositories(QUERY)).thenReturn(Single.error(exception));

		TestObserver<List<RepositoryItemModel>> observer = new TestObserver<>();
		repository.searchRepositories(QUERY).subscribe(observer);

		observer.assertError(exception);
		observer.assertNotComplete();
		observer.assertErrorMessage("exception");
	}
}
