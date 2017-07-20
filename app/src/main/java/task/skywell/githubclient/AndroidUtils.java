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

package task.skywell.githubclient;

import android.content.Context;
import android.text.SpannableString;
import android.text.style.TextAppearanceSpan;
import android.util.TypedValue;

/**
 * Created on 20.07.2017.
 * @author Dimowner
 */
public class AndroidUtils {

	private AndroidUtils() {}

	public static int dp(Context ctx, int val) {
		return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, val, ctx.getResources().getDisplayMetrics());
	}

	public static SpannableString formatString(Context context, String text, int styleId) {
		SpannableString spannableString = new SpannableString(text);
		spannableString.setSpan(new TextAppearanceSpan(context, styleId), 0, text.length(), 0);
		return spannableString;
	}

	public static SpannableString formatString(Context context, int textId, int styleId) {
		String text = context.getString(textId);
		SpannableString spannableString = new SpannableString(text);
		spannableString.setSpan(new TextAppearanceSpan(context, styleId), 0, text.length(), 0);
		return spannableString;
	}
}
