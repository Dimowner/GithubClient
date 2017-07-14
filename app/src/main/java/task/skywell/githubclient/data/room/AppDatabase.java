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

package task.skywell.githubclient.data.room;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

/**
 * Created on 14.07.2017.
 * @author Dimowner
 */
@Database(entities = {RepositoryItemModel.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
	public abstract RepositoriesDao repositoriesDao();

	private static volatile AppDatabase appDatabase;

	public static AppDatabase getInstance(Context context) {
		if (appDatabase == null) {
			synchronized (AppDatabase.class) {
				if (appDatabase == null) {
					appDatabase = Room.databaseBuilder(context.getApplicationContext(),
												AppDatabase.class, "repositories_db").build();
				}
			}
		}
		return appDatabase;
	}
}
