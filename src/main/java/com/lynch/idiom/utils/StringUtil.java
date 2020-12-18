package com.lynch.idiom.utils;

import java.util.Stack;

/**
 * 字符串工具类
 *
 * @author Lynch
 * @date 2020-12-04
 */
public final class StringUtil {

    private StringUtil() {
    }

    /**
     * Check if the String is null or has only whitespaces.
     * Modified from {org.apache.commons.lang.StringUtils#isBlank(String)}.
     *
     * @param string String to check
     * @return {@code true} if the String is null or has only whitespaces
     */
    public static boolean isBlank(String string) {
        if (isEmpty(string)) {
            return true;
        }
        for (int i = 0; i < string.length(); i++) {
            if (!Character.isWhitespace(string.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    /**
     * Check if the String has any non-whitespace character.
     *
     * @param string String to check
     * @return {@code true} if the String has any non-whitespace character
     */
    public static boolean isNotBlank(String string) {
        return !isBlank(string);
    }

    /**
     * Check if the String is null or empty.
     *
     * @param string String to check
     * @return {@code true} if the String is null or empty
     */
    public static boolean isEmpty(String string) {
        return string == null || string.isEmpty();
    }

    /**
     * Check if the String has any character.
     *
     * @param string String to check
     * @return {@code true} if the String has any character
     * @since 1.1.0
     */
    public static boolean isNotEmpty(String string) {
        return !isEmpty(string);
    }

    /**
     * 用栈解决括号匹配问题，实现数据格式化
     *
     * @param str
     * @return
     */
    public static String getFormatData(String str) {
        Stack<Character> st = new Stack<>();
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < str.length(); i++) {
            if (str.charAt(i) == '{' || str.charAt(i) == '[' || str.charAt(i) == '(') {
                st.push(str.charAt(i));
                sb.append(str.charAt(i));
                sb.append('\n');
                for (int j = 0; j < st.size(); j++) {
                    sb.append('\t');
                }
            } else if (str.charAt(i) == '}' || str.charAt(i) == ']' || str.charAt(i) == ')') {
                st.pop();
                sb.append('\n');
                for (int j = 0; j < st.size(); j++) {
                    sb.append('\t');
                }
                sb.append(str.charAt(i));
            } else if (str.charAt(i) == ',') {
                sb.append(str.charAt(i)).append("\n");
                for (int j = 0; j < st.size(); j++) {
                    sb.append('\t');
                }
            } else if (str.charAt(i) != ' ') {
                sb.append(str.charAt(i));
            }
        }
        return sb.toString();
    }

}