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

package task.skywell.githubclient.ui;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

/**
 * Created on 14.07.2017.
 * @author Dimowner
 */
class ListItem implements Parcelable {

	private final long id;
	private final String name;
	private final String owner;
	private final String description;
	private final String avatar_url;

	ListItem(long id, @NonNull String name, @NonNull String owner, String description, String avatar_url) {
		this.id = id;
		this.name = name;
		this.owner = owner;
		this.description = description;
		this.avatar_url = avatar_url;
	}

	public long getId() {
		return id;
	}

	String getName() {
		return name;
	}

	String getOwner() {
		return owner;
	}

	String getDescription() {
		return description;
	}

	String getAvatar_url() {
		return avatar_url;
	}

	//----- START Parcelable implementation ----------
	private ListItem(Parcel in) {
		id = in.readLong();
		String[] data = new String[4];
		in.readStringArray(data);
		name = data[0];
		owner = data[1];
		description = data[2];
		avatar_url = data[3];
	}

	public int describeContents() {
		return 0;
	}

	public void writeToParcel(Parcel out, int flags) {
		out.writeLong(id);
		out.writeStringArray(new String[] {name, owner, description, avatar_url});
	}

	public static final Parcelable.Creator<ListItem> CREATOR
			= new Parcelable.Creator<ListItem>() {
		public ListItem createFromParcel(Parcel in) {
			return new ListItem(in);
		}

		public ListItem[] newArray(int size) {
			return new ListItem[size];
		}
	};
	//----- END Parcelable implementation ----------

	@Override
	public String toString() {
		return "ListItem{" +
				"id=" + id +
				", name='" + name + '\'' +
				", owner='" + owner + '\'' +
				", description='" + description + '\'' +
				", avatar_url='" + avatar_url + '\'' +
				'}';
	}
}
