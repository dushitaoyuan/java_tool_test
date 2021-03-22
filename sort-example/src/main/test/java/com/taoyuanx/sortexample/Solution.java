package com.taoyuanx.sortexample;

import java.util.*;

/**
 * 有效括号
 */
class Solution {
    public List<String> generateParenthesis(int n) {
        List<String> res = new ArrayList<>();
        if (n <= 0) return res;
        dfs(n, "", res, 0, 0);
        return res;
    }

    private void dfs(int n, String path, List<String> res, int open, int close) {

        if (path.length() == 2 * n) {
            res.add(path);
            return;
        }
        if (open < n) {
            dfs(n, path + "(", res, open + 1, close);

        }
        if (close < open) {
            dfs(n, path + ")", res, open, close + 1);
        }

    }

    public int reverse(int x) {
        boolean flag = x > 0;
        x = Math.abs(x);
        int reverse = 0, temp = x, mod = 0;

        while (x > 0) {
            temp = reverse;
            mod = x % 10;
            x /= 10;
            reverse *= 10;
            reverse += mod;
            if (temp != (reverse - mod) / 10) {
                return 0;
            }
        }
        return flag ? reverse : -reverse;
    }

    public static void main(String[] args) {
        Solution solution = new Solution();
        System.out.println(solution.reverse(123));
    }


}