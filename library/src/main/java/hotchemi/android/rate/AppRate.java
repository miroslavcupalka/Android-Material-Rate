package hotchemi.android.rate;

import android.app.Activity;
import android.content.Context;
import android.view.View;

import java.util.Date;

import static hotchemi.android.rate.DialogManager.createDialog;
import static hotchemi.android.rate.PreferenceHelper.getEventTimes;
import static hotchemi.android.rate.PreferenceHelper.getInstallDate;
import static hotchemi.android.rate.PreferenceHelper.getIsAgreeShowDialog;
import static hotchemi.android.rate.PreferenceHelper.getLaunchTimes;
import static hotchemi.android.rate.PreferenceHelper.getRemindInterval;
import static hotchemi.android.rate.PreferenceHelper.isFirstLaunch;
import static hotchemi.android.rate.PreferenceHelper.setInstallDate;

public class AppRate {

    private static AppRate singleton;

    private final Context context;

    private final DialogOptions options = new DialogOptions();

    private int installDate = 10;

    private int launchTimes = 10;

    private int remindInterval = 1;

    private int eventsTimes = -1;

    private boolean isDebug = false;

    private AppRate(Context context) {
        this.context = context.getApplicationContext();
    }

    public static AppRate with(Context context) {
        if (singleton == null) {
            synchronized (AppRate.class) {
                if (singleton == null) {
                    singleton = new AppRate(context);
                }
            }
        }
        return singleton;
    }

    public AppRate setLaunchTimes(int launchTimes) {
        this.launchTimes = launchTimes;
        return this;
    }

    public AppRate setInstallDays(int installDate) {
        this.installDate = installDate;
        return this;
    }

    public AppRate setRemindInterval(int remindInterval) {
        this.remindInterval = remindInterval;
        return this;
    }

    public AppRate setShowLaterButton(boolean isShowNeutralButton) {
        options.setShowNeutralButton(isShowNeutralButton);
        return this;
    }

    public AppRate setEventsTimes(int eventsTimes) {
        this.eventsTimes = eventsTimes;
        return this;
    }

    public AppRate setShowTitle(boolean isShowTitle) {
        options.setShowTitle(isShowTitle);
        return this;
    }

    public AppRate clearAgreeShowDialog() {
        PreferenceHelper.setAgreeShowDialog(context, true);
        return this;
    }

    public AppRate setAgreeShowDialog(boolean clear) {
        PreferenceHelper.setAgreeShowDialog(context, clear);
        return this;
    }

    public AppRate setDebug(boolean isDebug) {
        this.isDebug = isDebug;
        return this;
    }

    public AppRate setView(View view) {
        options.setView(view);
        return this;
    }

    public AppRate setOnClickButtonListener(OnClickButtonListener listener) {
        options.setListener(listener);
        return this;
    }

    public AppRate setTitle(int resourceId) {
        options.setTitleResId(resourceId);
        return this;
    }

    public AppRate setMessage(int resourceId) {
        options.setMessageResId(resourceId);
        return this;
    }

    public AppRate setTextRateNow(int resourceId) {
        options.setTextPositiveResId(resourceId);
        return this;
    }

    public AppRate setTextLater(int resourceId) {
        options.setTextNeutralResId(resourceId);
        return this;
    }

    public AppRate setTextNever(int resourceId) {
        options.setTextNegativeResId(resourceId);
        return this;
    }

    public AppRate setCancelable(boolean cancelable) {
        options.setCancelable(cancelable);
        return this;
    }

    public AppRate setEnforcingMaterialDialog(boolean enforcingMaterialDialog){
        options.setEnforcingMaterialDialog(enforcingMaterialDialog);
        return this;
    }

    public void monitor() {
        if (isFirstLaunch(context)) {
            setInstallDate(context);
        }
        PreferenceHelper.setLaunchTimes(context, getLaunchTimes(context) + 1);
    }

    public static boolean showRateDialogIfMeetsConditions(Activity activity) {
        boolean isMeetsConditions = singleton.isDebug || singleton.shouldShowRateDialog();
        if (isMeetsConditions) {
            singleton.showRateDialog(activity);
        }
        return isMeetsConditions;
    }

    public static boolean passSignificantEvent(Activity activity) {
        return passSignificantEvent(activity, true);
    }

    public static boolean passSignificantEventAndConditions(Activity activity) {
        return passSignificantEvent(activity, singleton.shouldShowRateDialog());
    }

    private static boolean passSignificantEvent(Activity activity, boolean shouldShow) {
        int eventTimes = getEventTimes(activity);
        PreferenceHelper.setEventTimes(activity, ++eventTimes);
        boolean isMeetsConditions = singleton.isDebug || (singleton.isOverEventPass() && shouldShow);
        if (isMeetsConditions) {
            singleton.showRateDialog(activity);
        }
        return isMeetsConditions;
    }

    public void showRateDialog(Activity activity) {
        if (!activity.isFinishing()) {
            createDialog(activity, options).show();
        }
    }

    public boolean isOverEventPass() {
        return eventsTimes != -1 && getEventTimes(context) > eventsTimes;
    }

    public boolean shouldShowRateDialog() {
        return getIsAgreeShowDialog(context) &&
                isOverLaunchTimes() &&
                isOverInstallDate() &&
                isOverRemindDate();
    }

    private boolean isOverLaunchTimes() {
        return getLaunchTimes(context) >= launchTimes;
    }

    private boolean isOverInstallDate() {
        return isOverDate(getInstallDate(context), installDate);
    }

    private boolean isOverRemindDate() {
        return isOverDate(getRemindInterval(context), remindInterval);
    }

    private static boolean isOverDate(long targetDate, int threshold) {
        return new Date().getTime() - targetDate >= threshold * 24 * 60 * 60 * 1000;
    }

    public boolean isDebug() {
        return isDebug;
    }

}
