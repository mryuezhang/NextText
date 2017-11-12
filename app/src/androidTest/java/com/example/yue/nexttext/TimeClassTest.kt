package com.example.yue.nexttext

import android.support.test.InstrumentationRegistry
import android.support.test.runner.AndroidJUnit4
import com.example.yue.nexttext.Core.DataType.Time
import com.example.yue.nexttext.UI.Utilities
import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4::class)
class TimeClassTest {

    val time1 = Time("Sun, November, 12, 2017", "10:16")
    val time2 = Time("Fri, December, 1, 2017", "21:30")

    @Test
    fun isGettingCalendarCorrect() {
        val appContext = InstrumentationRegistry.getTargetContext()

        val cal1 = time1.getCalendar()
        val cal2 = time2.getCalendar()

        Assert.assertEquals(12, Utilities.reverseDateFormat_DAY(Utilities.formatDate(cal1)))
        Assert.assertEquals(1, Utilities.reverseDateFormat_DAY(Utilities.formatDate(cal2)))

        Assert.assertEquals(2017, Utilities.reverseDateFormat_YEAR(Utilities.formatDate(cal1)))
        Assert.assertEquals(2017, Utilities.reverseDateFormat_YEAR(Utilities.formatDate(cal2)))

        Assert.assertEquals(10, Utilities.reverseDateFormat_MONTH(Utilities.formatDate(cal1)))
        Assert.assertEquals(11, Utilities.reverseDateFormat_MONTH(Utilities.formatDate(cal2)))

        Assert.assertEquals(10, Utilities.reverseTimeFormat_HOUR(Utilities.formatTime(cal1, appContext)))
        Assert.assertEquals(9, Utilities.reverseTimeFormat_HOUR(Utilities.formatTime(cal2, appContext)))
    }

}