package com.taoyuanx.sortexample;

import java.util.Stack;

/**
 * 括号匹配
 */
class SolutionDemo {
    public boolean isValid(String s) {
        Stack<Character> stack = new Stack<>();
        for (char temp : s.toCharArray()) {
            if (temp == '{') {
                stack.push('}');
            } else if (temp == '(') {
                stack.push(')');
            } else if (temp == '[') {
                stack.push(']');
            } else if (stack.isEmpty() || stack.pop() != temp) {
                return false;
            }

        }
        return stack.isEmpty();
    }

    public static void main(String[] args) {
        SolutionDemo solution = new SolutionDemo();
        System.out.println(solution.isValid("()"));
        System.out.println(solution.isValid("()["));
    }
}