package com.encureit.samtadoot.utils;

import androidx.annotation.Nullable;

/**
 * Created by AbhishekR on
 */
public class StringUtil {

    /**
     * Returns true if the string is null or 0-length.
     *
     * @param str the string to be examined
     * @return true if str is null or zero length
     */
    public static boolean isEmpty(@Nullable CharSequence str) {
        return str == null || str.toString().trim().length() == 0;
    }

}
