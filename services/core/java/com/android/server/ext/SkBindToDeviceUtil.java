package com.android.server.ext;

import android.app.ActivityManagerInternal;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.GosPackageStateFlag;
import android.content.pm.PackageManagerInternal;
import android.ext.LogViewerApp;
import android.ext.SettingsIntents;
import android.os.Process;
import android.os.UserHandle;
import android.util.Log;
import android.util.Slog;

import com.android.internal.R;
import com.android.server.LocalServices;
import com.android.server.pm.pkg.AndroidPackage;
import com.android.server.pm.pkg.PackageState;

import java.util.List;

public class SkBindToDeviceUtil {

    private static final String TAG = SkBindToDeviceUtil.class.getSimpleName();


    /**
     * @param uid The uid of the app that had SO_BINDTODEVICE blocked.
     * @param pid The pid of the app process that had SO_BINDTODEVICE blocked.
     */
    public static void showSkBindToDeviceNotification(Context ctx, int uid, int pid) {
        // TODO: Should we trust that uid and pid are as expected?
        // TODO: Verify TombstoneHandler doesn't do any additional checks that we have missed
        //if (!Process.isApplicationUid(uid)) {
        //    throw new IllegalArgumentException("uid must belong to an application");
       // }

        Log.w("SocketBtdDebug", "SO_BINDTODEVICE used by uid: " + uid + ", pid: " + pid);

        if (true) {
            return;
        }


        String firstPackageName = null;

        var ami = LocalServices.getService(ActivityManagerInternal.class);
        ActivityManagerInternal.ProcessRecordSnapshot prs = ami.getProcessRecordByPid(pid);
        if (prs != null) {
            firstPackageName = prs.appInfo.packageName;
        } else {
            int appId = UserHandle.getAppId(uid);
            var pm = LocalServices.getService(PackageManagerInternal.class);
            List<AndroidPackage> appIdPkgs = pm.getPackagesForAppId(appId);
            if (appIdPkgs.size() == 1) {
                var pkg = appIdPkgs.get(0);
                firstPackageName = pkg.getPackageName();
            }
        }

        if (firstPackageName == null) {
            Slog.d(TAG, "firstPackageName is null for uid " + uid);
            return;
        }

        var n = AppSwitchNotification.maybeCreate(ctx, firstPackageName, uid,
                SettingsIntents.APP_SK_BIND_TO_DEVICE);
        if (n == null) {
            return;
        }

        //n.titleRes = R.string.notif_memtag_crash_title;
        //n.gosPsFlagSuppressNotif = GosPackageStateFlag.FORCE_MEMTAG_SUPPRESS_NOTIF;
        //Intent i = LogViewerApp.createBaseErrorReportIntent(errorReport);
        //i.putExtra(LogViewerApp.EXTRA_SOURCE_APP_INFO, n.appInfo);
        //if (textTombstoneFileSpec != null) {
       //     i.putExtra(LogViewerApp.EXTRA_TEXT_TOMBSTONE_FILE_PATH, textTombstoneFileSpec.first);
        //    i.putExtra(LogViewerApp.EXTRA_TEXT_TOMBSTONE_LAST_MODIFIED_TIME, textTombstoneFileSpec.second.longValue());
       // }
      //  n.moreInfoIntent = i;
        n.maybeShow();
    }

}
