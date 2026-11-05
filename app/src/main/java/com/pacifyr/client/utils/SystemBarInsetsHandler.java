package com.pacifyr.client.utils;

import android.app.Activity;
import android.app.Application;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowInsets;
import android.view.WindowManager;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import com.pacifyr.client.activity.SplashScreen;
import com.pacifyr.client.home.MainVideoPlayerActivity;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * Android 15+ (targetSdk 35+) forces edge-to-edge, and Android 16 ignores the opt-out flag,
 * so every screen draws under the status / navigation bars. The app's layouts were built for
 * the old behaviour, so this restores it globally:
 * <ul>
 *     <li>pads each activity's content by the system bar / display cutout insets
 *     (plus the keyboard for adjustResize screens)</li>
 *     <li>paints a scrim behind the status bar with the theme's status bar color</li>
 * </ul>
 * Register once from Application.onCreate().
 */
public final class SystemBarInsetsHandler implements Application.ActivityLifecycleCallbacks {

    private static final String APP_PACKAGE_PREFIX = "com.pacifyr.";
    private static final String SCRIM_TAG = "pacifyr_status_bar_scrim";

    /** Screens that intentionally draw full screen. */
    private static final Set<String> EXCLUDED = new HashSet<>(Arrays.asList(
            SplashScreen.class.getName(),
            MainVideoPlayerActivity.class.getName()
    ));

    private SystemBarInsetsHandler() {
    }

    public static void register(Application application) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.VANILLA_ICE_CREAM) {
            application.registerActivityLifecycleCallbacks(new SystemBarInsetsHandler());
        }
    }

    @Override
    public void onActivityPostCreated(@NonNull Activity activity, @Nullable Bundle savedInstanceState) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.VANILLA_ICE_CREAM) return;
        String name = activity.getClass().getName();
        if (!name.startsWith(APP_PACKAGE_PREFIX) || EXCLUDED.contains(name)) return;
        apply(activity);
    }

    @RequiresApi(api = Build.VERSION_CODES.VANILLA_ICE_CREAM)
    private static void apply(Activity activity) {
        if (activity.getWindow() == null) return;
        // Floating (dialog-style) windows are not forced edge-to-edge.
        if (activity.getWindow().isFloating()) return;

        View decor = activity.getWindow().getDecorView();
        final View content = decor.findViewById(android.R.id.content);
        if (!(decor instanceof ViewGroup) || content == null) return;

        final View scrim = getOrCreateScrim(activity, (ViewGroup) decor);
        final int softInputAdjust = activity.getWindow().getAttributes().softInputMode
                & WindowManager.LayoutParams.SOFT_INPUT_MASK_ADJUST;
        final boolean resizeForIme = softInputAdjust == WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE
                || softInputAdjust == WindowManager.LayoutParams.SOFT_INPUT_ADJUST_UNSPECIFIED;

        content.setOnApplyWindowInsetsListener((v, insets) -> {
            android.graphics.Insets bars = insets.getInsets(
                    WindowInsets.Type.systemBars() | WindowInsets.Type.displayCutout());
            int bottom = bars.bottom;
            if (resizeForIme) {
                bottom = Math.max(bottom, insets.getInsets(WindowInsets.Type.ime()).bottom);
            }
            v.setPadding(bars.left, bars.top, bars.right, bottom);

            ViewGroup.LayoutParams lp = scrim.getLayoutParams();
            if (lp.height != bars.top) {
                lp.height = bars.top;
                scrim.setLayoutParams(lp);
            }
            // Consumed: children must not apply the same insets again (fitsSystemWindows layouts).
            return WindowInsets.CONSUMED;
        });
        content.requestApplyInsets();
    }

    private static View getOrCreateScrim(Activity activity, ViewGroup decor) {
        View existing = decor.findViewWithTag(SCRIM_TAG);
        if (existing != null) return existing;

        View scrim = new View(activity);
        scrim.setTag(SCRIM_TAG);
        scrim.setBackgroundColor(resolveStatusBarColor(activity));
        decor.addView(scrim, new FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, 0, Gravity.TOP));
        return scrim;
    }

    /** The color the theme would have given the status bar before Android 15. */
    private static int resolveStatusBarColor(Activity activity) {
        int[] attrs = {android.R.attr.statusBarColor, androidx.appcompat.R.attr.colorPrimaryDark};
        TypedArray a = activity.getTheme().obtainStyledAttributes(attrs);
        try {
            int color = a.getColor(0, Color.TRANSPARENT);
            if (Color.alpha(color) == 0) {
                color = a.getColor(1, Color.BLACK);
            }
            return color;
        } finally {
            a.recycle();
        }
    }

    // Unused lifecycle callbacks
    @Override public void onActivityCreated(@NonNull Activity activity, @Nullable Bundle savedInstanceState) { }
    @Override public void onActivityStarted(@NonNull Activity activity) { }
    @Override public void onActivityResumed(@NonNull Activity activity) { }
    @Override public void onActivityPaused(@NonNull Activity activity) { }
    @Override public void onActivityStopped(@NonNull Activity activity) { }
    @Override public void onActivitySaveInstanceState(@NonNull Activity activity, @NonNull Bundle outState) { }
    @Override public void onActivityDestroyed(@NonNull Activity activity) { }
}
