/*
 * Catroid: An on-device visual programming system for Android devices
 * Copyright (C) 2010-2018 The Catrobat Team
 * (<http://developer.catrobat.org/credits>)
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * An additional term exception under section 7 of the GNU Affero
 * General Public License, version 3, is available at
 * http://developer.catrobat.org/license_additional_term
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package org.catrobat.catroid.uiespresso.content.brick.app;

import android.support.test.runner.AndroidJUnit4;

import org.catrobat.catroid.R;
import org.catrobat.catroid.content.Script;
import org.catrobat.catroid.content.bricks.IfLogicElseBrick;
import org.catrobat.catroid.content.bricks.IfLogicEndBrick;
import org.catrobat.catroid.content.bricks.PhiroIfLogicBeginBrick;
import org.catrobat.catroid.ui.SpriteActivity;
import org.catrobat.catroid.uiespresso.content.brick.utils.BrickTestUtils;
import org.catrobat.catroid.uiespresso.testsuites.Cat;
import org.catrobat.catroid.uiespresso.testsuites.Level;
import org.catrobat.catroid.uiespresso.util.rules.BaseActivityInstrumentationRule;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;

import java.util.Arrays;
import java.util.List;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.doesNotExist;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

import static org.catrobat.catroid.uiespresso.content.brick.utils.BrickDataInteractionWrapper.onBrickAtPosition;

@RunWith(AndroidJUnit4.class)
public class PhiroIfBrickTest {
	private PhiroIfLogicBeginBrick ifBrick;

	@Rule
	public BaseActivityInstrumentationRule<SpriteActivity> baseActivityTestRule = new
			BaseActivityInstrumentationRule<>(SpriteActivity.class, SpriteActivity.EXTRA_FRAGMENT_POSITION, SpriteActivity.FRAGMENT_SCRIPTS);

	@Before
	public void setUp() throws Exception {
		createProject();
		baseActivityTestRule.launchActivity();
	}

	@Category({Cat.AppUi.class, Level.Smoke.class, Cat.Gadgets.class})
	@Test
	public void testPhiroIfBrick() {
		onBrickAtPosition(0).checkShowsText(R.string.brick_when_started);
		onBrickAtPosition(1).checkShowsText(R.string.brick_phiro_sensor_begin);
		onBrickAtPosition(2).checkShowsText(R.string.brick_if_else);
		onBrickAtPosition(3).checkShowsText(R.string.brick_if_end);

		List<Integer> spinnerValuesResourceIds = Arrays.asList(
				R.string.phiro_sensor_front_left,
				R.string.phiro_sensor_front_right,
				R.string.phiro_sensor_side_left,
				R.string.phiro_sensor_side_right,
				R.string.phiro_sensor_bottom_left,
				R.string.phiro_sensor_bottom_right);

		onBrickAtPosition(1).onSpinner(R.id.brick_phiro_sensor_action_spinner)
				.checkStringIdValuesAvailable(spinnerValuesResourceIds);
	}

	@Category({Cat.AppUi.class, Level.Smoke.class, Cat.Gadgets.class})
	@Test
	public void testPhiroIfBrickDelete() {
		onBrickAtPosition(1).onChildView(withText(R.string.brick_phiro_sensor_begin))
				.perform(click());
		onView(withText(R.string.brick_context_dialog_delete_brick))
				.perform(click());
		onView(withText(R.string.yes))
				.perform(click());

		onView(withText(R.string.brick_phiro_sensor_begin))
				.check(doesNotExist());
		onView(withText(R.string.brick_if_else))
				.check(doesNotExist());
		onView(withText(R.string.brick_if_end))
				.check(doesNotExist());
	}

	private void createProject() {
		ifBrick = new PhiroIfLogicBeginBrick();
		IfLogicElseBrick ifLogicElseBrick = new IfLogicElseBrick(ifBrick);
		IfLogicEndBrick ifLogicEndBrick = new IfLogicEndBrick(ifBrick, ifLogicElseBrick);
		ifBrick.setIfElseBrick(ifLogicElseBrick);
		ifBrick.setIfEndBrick(ifLogicEndBrick);
		Script script = BrickTestUtils.createProjectAndGetStartScript("PhiroIfBrickTest");
		script.addBrick(ifBrick);
		script.addBrick(ifLogicElseBrick);
		script.addBrick(ifLogicEndBrick);
	}
}
