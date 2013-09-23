package br.com.socialfut.util;

import java.util.List;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningTaskInfo;
import android.content.Context;

public class ActivityStackUtils
{
    public static boolean isMyApplicationTaskOnTop(Context context)
    {
        String packageName = context.getPackageName();
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<RunningTaskInfo> recentTasks = activityManager.getRunningTasks(Integer.MAX_VALUE);
        if (recentTasks != null && recentTasks.size() > 0)
        {
            RunningTaskInfo t = recentTasks.get(0);
            // com.android.launcher
            String pack = t.baseActivity.getPackageName();
            if (pack.equals(packageName))
            {
                return true;
            }
        }
        return false;
    }
}
