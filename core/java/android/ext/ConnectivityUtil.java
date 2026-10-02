package android.ext;

import android.annotation.NonNull;
import android.annotation.SuppressLint;
import android.annotation.SystemApi;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.os.UserHandle;
import android.provider.Settings;
import android.util.Log;

/** @hide */
@SystemApi(client = SystemApi.Client.MODULE_LIBRARIES)
public class ConnectivityUtil {

    @SuppressLint("Enum")
    public enum NetworkType {
        PHYSICAL,
        VPN
    }

    private ConnectivityUtil() {}
    /**
     * Return true if this uid is a core system component, or if the uid is known to PackageManager
     * and at least one of the packages that use it is a system app.
     */
    public static boolean isSystem(@NonNull Context context, int uid) {
        if (UserHandle.isCore(uid)) {
            return true;
        }

        PackageManager pm = context.getPackageManager();
        String[] packageNames = pm.getPackagesForUid(uid);
        if (packageNames == null) {
            return false;
        }
        int userId = UserHandle.getUserId(uid);
        for (String packageName : packageNames) {
            try {
                ApplicationInfo appInfo = pm.getApplicationInfoAsUser(packageName, 0, userId);
                if (appInfo.isSystemApp()) {
                    return true;
                }
            } catch (PackageManager.NameNotFoundException ignored) { }
        }
        return false;
    }

    public static boolean isRegularAppWithLockdownVpnEnabled(@NonNull Context context, int uid) {
        final boolean lockdownVpnEnabled = isLockdownVpnEnabled(context, UserHandle.getUserId(uid));
        return lockdownVpnEnabled && !isSystem(context, uid);
    }

    public static boolean isLockdownVpnEnabled(@NonNull Context context, int userId) {
        return Settings.Secure.getIntForUser(context.getContentResolver(),
                Settings.Secure.ALWAYS_ON_VPN_LOCKDOWN, 0, userId) == 1;
    }
}
