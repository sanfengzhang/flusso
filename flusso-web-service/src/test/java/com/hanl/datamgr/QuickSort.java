package com.hanl.datamgr;

import org.apache.commons.lang.ArrayUtils;

/**
 * @author: Hanl
 * @date :2020/1/3
 * @desc:
 */
public class QuickSort {

    public static void main(String[] args) {
        int[] aa = new int[]{3, 1, 2, 5, 4, 6, 9, 7, 10, 8};
        quickSort(0, aa.length - 1, aa);
        System.out.println(ArrayUtils.toString(aa));
    }

    public static void quickSort(int left, int right, int[] arr) {
        if (left < right) {
            int pivot = sortPartition(left, right, arr);//先计算基准点
            quickSort(left, pivot - 1, arr);//然后计算左子序列
            quickSort(pivot + 1, right, arr);//然后计算右子序列
        }
    }

    public static int sortPartition(int left, int right, int[] arr) {
        int tmp = arr[left];
        while (left < right) {
            while (left < right && tmp <= arr[right]) {
                right--;
            }
            arr[left] = arr[right];
            while (left < right && tmp >= arr[left]) {
                left++;
            }
            arr[right] = arr[left];
        }
        arr[left] = tmp;
        return left;
    }
}
