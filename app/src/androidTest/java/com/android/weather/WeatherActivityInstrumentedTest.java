package com.android.weather;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.android.weather.ui.weather.WeatherActivity;
import com.android.weather.utils.Constant;

import android.content.Context;

import androidx.test.InstrumentationRegistry;
import androidx.test.espresso.Espresso;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import androidx.test.rule.ActivityTestRule;
import androidx.test.runner.AndroidJUnit4;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.assertEquals;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class WeatherActivityInstrumentedTest
{


    @Rule
    public ActivityTestRule<WeatherActivity> activityRule = new ActivityTestRule<>(WeatherActivity.class);

    @Test
    public void testErrorScreen()
    {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getTargetContext();

        assertEquals("com.android.weather", appContext.getPackageName());
        Espresso.onView(withId(R.id.errorMsg)).check(matches(not(isDisplayed())));
        Espresso.onView(withId(R.id.errorMsg)).check(matches(withText(R.string.error)));
    }

    @Test
    public void testInternetErrorTexts() throws Throwable
    {
        // When.
        activityRule.runOnUiThread(new Runnable()
        {

            @Override
            public void run()
            {
                activityRule.getActivity().showError(Constant.Error.INTERNET_NOT_AVAILABLE);
            }
        });

        Espresso.onView(withId(R.id.errorMsg)).check(matches(withText(R.string.internet_not_available)));

    }

    @Test
    public void testProviderErrorTexts() throws Throwable
    {
        // When.
        activityRule.runOnUiThread(new Runnable()
        {

            @Override
            public void run()
            {
                activityRule.getActivity().showError(Constant.Error.LOCATION_PROVIDER_DISABLED);
            }
        });

        Espresso.onView(withId(R.id.errorMsg)).check(matches(withText(R.string.enablegps)));

    }

    @Test
    public void testVisibility() throws Throwable
    {
        // When.
        activityRule.runOnUiThread(new Runnable()
        {

            @Override
            public void run()
            {
                activityRule.getActivity().showProgress();
            }
        });
        //check progress bar is visible
        Espresso.onView(withId(R.id.progressBar)).check(matches((isDisplayed())));
        //check error screen is not visible
        Espresso.onView(withId(R.id.errorscreen)).check(matches(not(isDisplayed())));
        //chech weather screen is not visible
        Espresso.onView(withId(R.id.weatherscreen)).check(matches(not(isDisplayed())));

    }
}
