package com.example.yue.nexttext;

import android.util.Log;

import com.example.yue.nexttext.Core.DataType.Weather;
import com.example.yue.nexttext.Core.Utility.Constants;

import org.json.JSONException;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;

/**
 * Created by jamesmulvenna on 2017-11-16.
 */

public class JamesWeatherOutputTest {

    @Test
    public void testWeather() throws IOException, JSONException {
        Weather thisWeather = new Weather("Ottawa", "CA");
        int conditionCode = 0;

        try {
            conditionCode = thisWeather.getConditionCode();

        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Log.d("Condition code: ", Integer.toString(conditionCode));

        Assert.assertEquals(conditionCode, Constants.WEATHER_RAIN);
    }
}
