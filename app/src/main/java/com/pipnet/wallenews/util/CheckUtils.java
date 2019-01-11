package com.pipnet.wallenews.util;


import android.text.TextUtils;


/**
 * User: lts (549860123@qq.com)
 * Date: 2015-09-17
 * Time: 11:17
 * FIXME
 */
public class CheckUtils {
    /**
     * 验证手机格式
     */
    public static boolean isMobileNO(String mobiles) {

        String telRegex = "[1][3578]\\d{9}";// "[1]"代表第1位为数字1，"[358]"代表第二位可以为3、5、8中的一个，"\\d{9}"代表后面是可以是0～9的数字，有9位。
        if (TextUtils.isEmpty(mobiles))
            return false;
        else
            return mobiles.matches(telRegex);
    }

    /**
     * 测试用手机账号的判断，前三位必须是601开头,后四位必须是1111
     */
    public static boolean isTestNO(String testNumber) {
        String regex = "[6][0][1][1][1][1][1]\\d{4}";
        if (TextUtils.isEmpty(testNumber)) {
            return false;
        }else {
            return testNumber.matches(regex);
        }
    }
}
