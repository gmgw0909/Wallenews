package com.pipnet.wallenews.base;

import android.app.Activity;

import java.util.ArrayList;
import java.util.List;

/**
 * @author litieshan
 * @desc 活动控制器
 * @date 2017-06-28
 * @time 09：30
 */
public class ActivityController {
    public static final List<Activity> ACTIVITIES = new ArrayList<Activity>();


    public static void addActivity(Activity activity) {
        ACTIVITIES.add(activity);
    }

    public static void removeActivity(Activity activity) {
        ACTIVITIES.remove(activity);
    }

    public static void finishAll() {
        for (Activity activity : ACTIVITIES) {
            if (!activity.isFinishing()) {
                activity.finish();
            }
        }
    }

//    public static void finishGroup() {
//        for (Activity activity : ACTIVITIES) {
//            if (activity instanceof MyGroupActivity) {
//                activity.finish();
//            }
//            if (activity instanceof GroupChatActivity) {
//                activity.finish();
//            }
//            if (activity instanceof GroupDetailsActivity) {
//                activity.finish();
//            }
//        }
//    }
}
