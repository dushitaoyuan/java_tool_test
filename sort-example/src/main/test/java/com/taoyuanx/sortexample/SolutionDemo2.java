package com.taoyuanx.sortexample;

/**
 * 链表相加
 */
class SolutionDemo2 {
    public ListNode addTwoNumbers(ListNode l1, ListNode l2) {
        ListNode newList = new ListNode(0);
        ListNode head = newList;
        int add = 0;
        while (l1 != null || l2 != null) {
            int v1 = 0, v2 = 0;
            if (l1 != null) {
                v1 = l1.val;
                l1 = l1.next;
            }
            if (l2 != null) {
                v2 = l2.val;
                l2 = l2.next;
            }
            int sum = v1 + v2 + add;
            newList.val = sum % 10;
            if (sum >= 10) {
                add = 1;
            } else {
                add = 0;
            }
            if (l1 != null || l2 != null) {
                ListNode next = new ListNode(0);
                newList.next = next;
                newList = next;
            }
        }
        if (add > 0) {
            newList.next = new ListNode(add);
        }

        return head;
    }

    public void printList(ListNode list) {
        StringBuilder builder = new StringBuilder();
        while (list != null) {
            builder.append(list.val);
            if (list.next != null) {
                builder.append("->");
            }
            list = list.next;
        }
        System.out.println(builder.toString());
    }

    public static void main(String[] args) {
        demo();
        demo2();
        demo3();
    }

    public static void demo() {
        SolutionDemo2 solution = new SolutionDemo2();
        ListNode l1 = new ListNode(2);
        l1.next = new ListNode(4);
        l1.next.next = new ListNode(3);


        ListNode l2 = new ListNode(5);
        l2.next = new ListNode(6);
        l2.next.next = new ListNode(4);
        solution.printList(solution.addTwoNumbers(l1, l2));
    }

    public static void demo2() {
        SolutionDemo2 solution = new SolutionDemo2();
        ListNode l1 = new ListNode(0);


        ListNode l2 = new ListNode(0);
        solution.printList(solution.addTwoNumbers(l1, l2));
    }

    public static void demo3() {
        SolutionDemo2 solution = new SolutionDemo2();
        ListNode l1 = new ListNode(9);
        l1.next = new ListNode(9);
        l1.next.next = new ListNode(9);


        ListNode l2 = new ListNode(9);
        l2.next = new ListNode(9);
        l2.next.next = new ListNode(9);
        l2.next.next.next = new ListNode(9);

        l1.next.next.next = l2;

        solution.printList(solution.addTwoNumbers(l1, l2));
    }

    public static class ListNode {
        int val;
        ListNode next;

        ListNode() {
        }

        ListNode(int val) {
            this.val = val;
        }

        ListNode(int val, ListNode next) {
            this.val = val;
            this.next = next;
        }


    }
}