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
import android.content.res.Resources;
import android.graphics.Point;
import android.os.Build;
import android.text.SpannableString;
import android.text.style.TextAppearanceSpan;
import android.view.Display;
import android.view.WindowManager;

/**
 * Created on 20.07.2017.
 * @author Dimowner
 */
public class AndroidUtils {

	private static int screenWidth = 0;
	private static int screenHeight = 0;

	private AndroidUtils() {}

	public static int dpToPx(int dp) {
		return (int) (dp * Resources.getSystem().getDisplayMetrics().density);
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

	public static int getScreenHeight(Context c) {
		if (screenHeight == 0) {
			WindowManager wm = (WindowManager) c.getSystemService(Context.WINDOW_SERVICE);
			Display display = wm.getDefaultDisplay();
			Point size = new Point();
			display.getSize(size);
			screenHeight = size.y;
		}

		return screenHeight;
	}

	public static int getScreenWidth(Context c) {
		if (screenWidth == 0) {
			WindowManager wm = (WindowManager) c.getSystemService(Context.WINDOW_SERVICE);
			Display display = wm.getDefaultDisplay();
			Point size = new Point();
			display.getSize(size);
			screenWidth = size.x;
		}

		return screenWidth;
	}

	public static boolean isAndroid5() {
		return Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP;
	}
}
