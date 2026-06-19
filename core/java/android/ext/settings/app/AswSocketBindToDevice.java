package android.ext.settings.app;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.GosPackageState;
import android.content.pm.GosPackageStateFlag;

import com.android.server.os.nano.AppCompatProtos;

import dalvik.system.VMRuntime;

/** @hide */
public class AswSocketBindToDevice extends AppSwitch {
    public static final AswSocketBindToDevice I = new AswSocketBindToDevice();

    private AswSocketBindToDevice() {
        gosPsFlag = GosPackageStateFlag.ALLOW_SOCKET_BIND_TO_DEVICE;
        gosPsFlagNonDefault = GosPackageStateFlag.ALLOW_SOCKET_BIND_TO_DEVICE_NON_DEFAULT;
        // TODO: ?
        //compatChangeToDisableHardening = AppCompatProtos.DISABLE_HARDENED_MALLOC;
    }

    @Override
    public Boolean getImmutableValue(Context ctx, int userId, ApplicationInfo appInfo,
                                     GosPackageState ps, StateInfo si) {
        if (appInfo.isSystemApp()) {
            si.immutabilityReason = IR_IS_SYSTEM_APP;
            return false;
        }

        return null;
    }

    @Override
    protected boolean getDefaultValueInner(Context ctx, int userId, ApplicationInfo appInfo,
                                           GosPackageState ps, StateInfo si) {
        return true;
    }
}
