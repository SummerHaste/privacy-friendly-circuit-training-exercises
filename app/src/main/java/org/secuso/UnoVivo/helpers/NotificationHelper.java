/**
 * This file is part of Privacy Friendly Circuit Trainer.
 * Privacy Friendly Circuit Trainer is free software:
 * you can redistribute it and/or modify it under the terms of the
 * GNU General Public License as published by the Free Software Foundation,
 * either version 3 of the License, or any later version.
 * Privacy Friendly Circuit Trainer is distributed in the hope
 * that it will be useful, but WITHOUT ANY WARRANTY; without even
 * the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License
 * along with Privacy Friendly Interval Timer. If not, see <http://www.gnu.org/licenses/>.
 */

package org.secuso.UnoVivo.helpers;

import static org.secuso.UnoVivo.activities.MotivationAlertTextsActivity.LOG_CLASS;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

import org.secuso.UnoVivo.receivers.MotivationAlertReceiver;
import org.secuso.UnoVivo.tutorial.PrefManager;

import java.util.Calendar;

/**
 * Sets the motivation alert event to notify the user about a workout.
 *
 * @author Tobias Neidig
 * @author Alexander Karakuz
 * @version 20170603
 */
public class NotificationHelper {

    /**
     * Schedules (or updates) the motivation alert notification alarm
     *
     * @param context The application context
     */
    public static void setMotivationAlert(Context context) {
        Log.i(LOG_CLASS, "Setting motivation alert alarm");

        Intent motivationAlertIntent = new Intent(context, MotivationAlertReceiver.class);
        PendingIntent motivationAlertPendingIntent = PendingIntent.getBroadcast(context, 1, motivationAlertIntent, PendingIntent.FLAG_IMMUTABLE);
        AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        long timestamp = PrefManager.getNotificationMotivationAlertTime(context);


        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(timestamp);
        calendar.set(Calendar.YEAR, Calendar.getInstance().get(Calendar.YEAR));
        calendar.set(Calendar.MONTH, Calendar.getInstance().get(Calendar.MONTH));
        calendar.set(Calendar.DAY_OF_MONTH, Calendar.getInstance().get(Calendar.DAY_OF_MONTH));
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        if (calendar.before(Calendar.getInstance())) {
            calendar.add(Calendar.DAY_OF_MONTH, 1);
        }

        // Set alarm
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                if (!am.canScheduleExactAlarms()) {
                    Log.i(LOG_CLASS, "Motivation alert cannot be scheduled because of missing permission.");
                } else {
                    am.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), motivationAlertPendingIntent);
                }
            } else {
                am.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), motivationAlertPendingIntent);
            }
        } else {
            am.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), motivationAlertPendingIntent);
        }
        Log.i(LOG_CLASS, "Scheduled motivation alert at start time " + calendar.toString());
    }

    /**
     * Checks if the motivation alert is enabled in the settings
     *
     * @param context The application context
     */
    public static boolean isMotivationAlertEnabled(Context context) {
        return PrefManager.getNotificationMotivationAlertEnabled(context);
    }

    /**
     * Cancels the motivation alert
     *
     * @param context The application context
     */
    public static void cancelMotivationAlert(Context context) {
        Log.i(LOG_CLASS, "Canceling motivation alert alarm");
        Intent motivationAlertIntent = new Intent(context, MotivationAlertReceiver.class);
        PendingIntent motivationAlertPendingIntent = PendingIntent.getBroadcast(context, 1, motivationAlertIntent, PendingIntent.FLAG_IMMUTABLE);
        AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        am.cancel(motivationAlertPendingIntent);
    }
}
