package com.mx.ymate.security.satoken;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @Author: mengxiang.
 * @Date 2025/04/24.
 * @Time: 11:00.
 * @Description:
 */
public class PathAnalyzer {

    private static final Map<String, PathAnalyzer> CACHED = new LinkedHashMap<>();
    private final Pattern pattern;

    public static PathAnalyzer get(String expr) {
        PathAnalyzer pa = CACHED.get(expr);
        if (pa == null) {
            synchronized (expr.intern()) {
                pa = CACHED.get(expr);
                if (pa == null) {
                    pa = new PathAnalyzer(expr);
                    CACHED.put(expr, pa);
                }
            }
        }

        return pa;
    }

    private PathAnalyzer(String expr) {
        this.pattern = Pattern.compile(exprCompile(expr), 2);
    }

    public Matcher matcher(String uri) {
        return this.pattern.matcher(uri);
    }

    public boolean matches(String uri) {
        return this.pattern.matcher(uri).find();
    }

    private static String exprCompile(String expr) {
        String p = expr.replace(".", "\\.");
        p = p.replace("$", "\\$");
        p = p.replace("**", ".[]");
        p = p.replace("*", "[^/]*");
        if (p.contains("{")) {
            if (p.indexOf("_}") > 0) {
                p = p.replaceAll("\\{[^}]+?_}", "(.+?)");
            }

            p = p.replaceAll("\\{[^}]+?}", "([^/]+?)");
        }

        if (!p.startsWith("/")) {
            p = "/" + p;
        }

        p = p.replace(".[]", ".*");
        return "^" + p + "$";
    }
}
