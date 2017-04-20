package com.nothio.bucher.util;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;

import com.nothio.bucher.MyApp;

import java.util.ArrayList;

@TargetApi(Build.VERSION_CODES.M)
public class PermissionUtil {
    /**
     * method that will return whether the permission is accepted. By default it is true if the user is using a device below
     * version 23
     *
     * @param permission
     * @return
     */
    @TargetApi(11)
    private static boolean hasPermission(Context cnt, String permission) {
        if (HaveToCheckPermission()) {
            return (cnt.checkSelfPermission(permission) == PackageManager.PERMISSION_GRANTED);
        }
        return true;
    }

    @TargetApi(11)
    private static boolean hasNoPermission(Context cnt, String permission) {
        if (HaveToCheckPermission()) {
            return (cnt.checkSelfPermission(permission) == PackageManager.PERMISSION_DENIED);
        }
        return false;
    }

    public static ArrayList<String> permissionToAsk(Context cnt, ArrayList<String> wanted) {
        ArrayList<String> result = new ArrayList<String>();

        for (String perm : wanted) {
            if (!hasPermission(cnt, perm)) {
                result.add(perm);
            }
        }

        return result;
    }

    public static boolean anyPermissionDenied(Context cnt, ArrayList<String> wanted) {

        for (String perm : wanted) {
            if (hasNoPermission(cnt, perm)) {
                return true;
            }
        }

        return false;
    }

    /**
     * method to determine whether we have asked
     * for this permission before.. if we have, we do not want to ask again.
     * They either rejected us or later removed the permission.
     *
     * @param permission
     * @return
     */
    private boolean shouldWeAsk(String permission, MyApp myapp) {
        return myapp.getPrefBoolean(permission);
    }

    /**
     * we will save that we have already asked the user
     *
     * @param permission
     */
    private void markAsAsked(String permission, MyApp myapp) {
        myapp.setPref(permission, false);
    }

    /**
     * We may want to ask the user again at their request.. Let's clear the
     * marked as seen preference for that permission.
     *
     * @param permission
     */
    private void clearMarkAsAsked(String permission, MyApp myapp) {
        myapp.setPref(permission, true);
    }


    /**
     * This method is used to determine the permissions we do not have accepted yet and ones that we have not already
     * bugged the user about.  This comes in handle when you are asking for multiple permissions at once.
     *
     * @param wanted
     * @return
     */
    private ArrayList<String> findUnAskedPermissions(Context cnt, MyApp myapp, ArrayList<String> wanted) {
        ArrayList<String> result = new ArrayList<String>();

        for (String perm : wanted) {
            if (!hasPermission(cnt, perm) && shouldWeAsk(perm, myapp)) {
                result.add(perm);
            }
        }

        return result;
    }

    /**
     * this will return us all the permissions we have previously asked for but
     * currently do not have permission to use. This may be because they declined us
     * or later revoked our permission. This becomes useful when you want to tell the user
     * what permissions they declined and why they cannot use a feature.
     *
     * @param wanted
     * @return
     */
    private ArrayList<String> findRejectedPermissions(Context cnt, MyApp myapp, ArrayList<String> wanted) {
        ArrayList<String> result = new ArrayList<String>();

        for (String perm : wanted) {
            if (!hasPermission(cnt, perm) && !shouldWeAsk(perm, myapp)) {
                result.add(perm);
            }
        }

        return result;
    }

    /**
     * Just a check to see if we have marshmallows (version 23)
     *
     * @return
     */
    private static boolean HaveToCheckPermission() {
        return (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP_MR1);
    }
}
