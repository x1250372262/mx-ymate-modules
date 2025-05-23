package com.mx.ymate.dev.util;

/**
 * @Author: mengxiang.
 * @Date 2025/04/24.
 * @Time: 11:00.
 * @Description:
 */
public class PathMatchUtil {

    /**
     * @param s 待匹配字符串
     * @param p 通配符模板字符串
     * @return 匹配结果
     */
    public boolean isMatch(String s, String p) {
        // i 用来记录s串检测的索引的位置
        int i = 0;
        // j 用来记录p串检测的索引的位置
        int j = 0;
        // 记录 待测串i的回溯点
        int ii = -1;
        // 记录 通配符*的回溯点
        int jj = -1;

        // 以s字符串的长度为循环基数，用i来记录s串当前的位置
        while (i < s.length()) {
            // 用j来记录p串的当前位置，检测p串中j位置的值是不是通配符*
            if (j < p.length() && p.charAt(j) == '*') {
                // 如果在p串中碰到通配符*，复制两串的当前索引，记录当前的位置，并对p串+1，定位到非通配符位置
                ii = i;
                jj = j;
                j++;
                // 检测p串是否结束  检测两串当前位置的值是否相等 检测p串中j位置是否是单值通配符？
            } else if (j < p.length()
                    && (s.charAt(i) == p.charAt(j)
                    || p.charAt(j) == '?')) {
                // 如果此时p串还在有效位置上，那么两串当前位置相等或者p串中是单值通配符，表明此时匹配通过，两串均向前移动一步
                i++;
                j++;
            } else {
                // 如果在以上两种情况下均放行，表明此次匹配是失败的，那么此时就要明确一点，s串是否在被p串中的通配符*监听着，
                // 因为在首次判断中如果碰到通配符*，我们会将他当前索引的位置记录在jj的位置上，
                // 如果jj = -1 表明匹配失败，当前s串不在监听位置上
                if (jj == -1) {
                    return false;
                }
                // 如果此时在s串在通配符*的监听下， 让p串回到通配符*的位置上继续监听下一个字符
                j = jj;
                // 让i回到s串中与通配符对应的当前字符的下一个字符上，也就是此轮匹配只放行一个字符
                i = ii + 1;
            }
        }

        // 当s串中的每一个字符都与p串中的字符进行匹配之后，对p串的残余串进行检查，如果残余串是一个*那么继续检测，否则跳出
        while (j < p.length() && p.charAt(j) == '*') {
            j++;
        }

        // 此时查看p是否已经检测到最后，如果检测到最后表示匹配成功，否则匹配失败
        return j == p.length();
    }

}
