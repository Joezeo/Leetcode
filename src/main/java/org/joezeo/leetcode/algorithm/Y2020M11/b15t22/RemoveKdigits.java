package org.joezeo.leetcode.algorithm.Y2020M11.b15t22;

import java.util.*;

/**
 * 给定一个以字符串表示的非负整数 num，移除这个数中的 k 位数字，使得剩下的数字最小。
 * 注意:
 * num 的长度小于 10002 且 ≥ k。
 * num 不会包含任何前导零。
 *
 * 示例 1 :
 * 输入: num = "1432219", k = 3
 * 输出: "1219"
 * 解释: 移除掉三个数字 4, 3, 和 2 形成一个新的最小的数字 1219。
 *
 * 示例 2 :
 * 输入: num = "10200", k = 1
 * 输出: "200"
 * 解释: 移掉首位的 1 剩下的数字为 200. 注意输出不能有任何前导零。
 *
 * 示例 3 :
 * 输入: num = "10", k = 2
 * 输出: "0"
 * 解释: 从原数字移除所有的数字，剩余为空就是0。
 *
 * 来源：力扣（LeetCode）
 * 链接：https://leetcode-cn.com/problems/remove-k-digits
 * 著作权归领扣网络所有。商业转载请联系官方授权，非商业转载请注明出处。
 *
 * @author Joezeo
 * @date 2020/11/15 22:28
 */
public class RemoveKdigits {
    // 首先来暴力求解贪心算法
    // 这样做的时间复杂度达到了O(n*k)，这样明显是可以优化的
    // 并且这样怎么做每次的内层循环要使用删除后的数列来进行循环，我的做法看起来空间复杂度就会很高
    // 最后对结果的转换也比较麻烦，不能使用int或者long来辅助转换
    public String removeKdigits_violent(String num, int k) {
        if (num.length() == 1 && k > 0 || num.length() == k) {
            return "0";
        }
        char[] numsChar = num.toCharArray();
        List<Integer> numList = new ArrayList<>();
        for (int j = 0; j < numsChar.length; j++) {
            numList.add(numsChar[j] - 48);
        }
        List<Integer> listCopy = new ArrayList(numList);
        for(int idx = 0; idx < k; idx++) {
            for (int j = 0; j < listCopy.size() - 1; j++) {
                if (listCopy.get(j) > listCopy.get(j+1)) { // 如果当前数字比下一个数字大，那么就删除当前数字
                    numList.remove(j);
                    listCopy = new ArrayList<>(numList);
                    break;
                }
                if(j == listCopy.size() - 2) { // 没有找到符合条件的数字，就删除数列的最后一个数字
                    numList.remove(listCopy.size() - 1);
                    listCopy = new ArrayList<>(numList);
                }
            }
        }

        StringBuilder sb = new StringBuilder();
        Iterator<Integer> iter = numList.iterator();
        while(iter.hasNext()) { // 记得要删除开头的0
            int cur = iter.next();
            if (cur != 0){
                break;
            }
            if (cur == 0){
                iter.remove();
            }
        }
        for (int i = 0; i < numList.size(); i++) {
            sb.append(numList.get(i));
        }
        Deque<Integer> deque = new LinkedList<>();
        return sb.toString().length() == 0 ? "0" : sb.toString();
    }

    // 做一个上一个方法的改进：使用单调栈来维护当前数列
    // 如果栈为空，那么直接入栈
    // 如果入栈的数字比栈顶的数字小，那么删除栈顶的数字，入栈新数字，计数器+1(注意：这里应该是一个循环判断)
    // 如果一直到最后一个数字都不符合条件，那么依次出栈
    // 为了避免反转，使用双端队列来代替栈
    public String removeKdigits(String num, int k) {
        if (num.length() == 1 && k > 0 || num.length() == k) {
            return "0";
        }
        char[] numsChar = num.toCharArray();
        List<Integer> numList = new ArrayList<>();
        for (int j = 0; j < numsChar.length; j++) {
            numList.add(numsChar[j] - 48);
        }
        int counter = 0;
        Deque<Integer> stack = new LinkedList<>();
        for (int cur : numList) {
            while (!stack.isEmpty() && stack.peekLast() > cur && counter < k) {
                stack.pollLast();
                counter++;
            }
            stack.offerLast(cur);
        }
        while (counter < k) {
            stack.pollLast();
            counter++;
        }
        StringBuilder sb = new StringBuilder();
        boolean enterNum = false;
        while (!stack.isEmpty()) {
            int cur = stack.pollFirst();
            if (cur != 0) {
                enterNum = true;
            }
            if (enterNum) {
                sb.append(cur);
            }
        }
        return sb.toString().length() == 0 ? "0" : sb.toString();
    }
}
