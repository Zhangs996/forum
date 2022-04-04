package com.zhang.forum;

import org.junit.Test;

import java.util.*;

/**
 * @BelongsProject: forum
 * @BelongsPackage: com.zhang.forum
 * @Author: zhangshuo
 * @CreateTime: 2022-03-10 12:54
 * @Description: leetcode
 */
public class TestLeecode {
    @Test
    public void test1(){
        ArrayDeque<Character> deque = new ArrayDeque<>();
        deque.offer('a');
        deque.offer('b');
        deque.push('c');
        System.out.println(deque.peek());
        System.out.println(deque.getFirst());
    }

    @Test
    public void test2(){
        List<List<Integer>> list = new ArrayList<>();
        ArrayList<Integer> childList = new ArrayList<>();
        childList.add(0);
        childList.add(1);
        list.add(childList);
        childList.clear();
        childList.add(2);
        childList.add(3);
        list.add(childList);
        System.out.println(list);
    }

    public static void main(String[] args) {
        System.out.println(addStrings("1", "9"));
    }

    public static String addStrings(String num1, String num2) {
        StringBuffer res = new StringBuffer("");
        int i = num1.length() - 1;
        int j = num2.length() - 1;
        int add = 0;
        int times = i > j ? i : j;
        while(times >= 0){
            int x = i >= 0 ? num1.charAt(i) - '0' : 0;
            int y = j >= 0 ? num2.charAt(j) - '0' : 0;
            res.append((x + y + add) % 10);
            add = (x + y + add) / 10;
            i--;j--;times--;
        }
        return res.reverse().toString();
    }

    @Test
    public void test3(){
        int[] a = new int[10];
    }

    @Test
    public void testSave2() {

        char[] s={'A','G','C','T'};
        String st=String.valueOf(s);
        System.out.println(st);
        int a[] = {4, 3, 6, 5, 1, 2};
        char[] f = {'a','b','v'};
        int b[] = Arrays.copyOf(a, 4);
        int c[] = Arrays.copyOfRange(a, 2, 4);//包括2的索引，不包括4的索引

        String str = String.valueOf(f);
        System.out.println(str);
        for (int i = 0; i < b.length; i++)
            System.out.print(b[i] + " ");
        System.out.println();

        for (int i = 0; i < c.length; i++)
            System.out.print(c[i] + " ");
        System.out.println();

    }
}


