package com.github.hinsteny.test.commons.core.algorithm.structure;

import com.github.hinsteny.commons.core.algorithm.structure.BinaryHeap;
import java.util.List;
import org.testng.annotations.Test;

/**
 * @author Hinsteny
 * @version BinaryHeapTest: BinaryHeapTest 2019-06-13 10:48 All rights reserved.$
 */
public class BinaryHeapTest {

    @Test(testName = "测试使用二叉堆示例")
    public void testAesUtilEncryptAndDecryptToStr() {
        // 构建一个二叉堆, 内部存放int数据
        Integer[] data = {3, 5, 9, 1, 10, 8, 2, 12, 7, 4};
        System.out.println("============== 最小堆示例 ==============");
        BinaryHeap<Integer> heap = new BinaryHeap();
        for (int i = 0; i < data.length; i++) {
            heap.add(data[i]);
        }
        System.out.println(heap);
        System.out.println("============== 最小堆构建好了, 现在依次弹出堆顶元素, 再看堆中数据排布 ==============");
        while (heap.size() > 0) {
            heap.popTop();
            System.out.println(heap);
        }
        System.out.println("============== 最大堆示例 ==============");
        heap = new BinaryHeap(true);
        for (int i = 0; i < data.length; i++) {
            heap.add(data[i]);
        }
        System.out.println(heap);
        System.out.println("============== 最大堆构建好了, 现在依次弹出堆顶元素, 再看堆中数据排布 ==============");
        while (heap.size() > 0) {
            heap.popTop();
            System.out.println(heap);
        }
        System.out.println("============== 堆排序示例 ==============");
        heap = new BinaryHeap();
        for (int i = 0; i < data.length; i++) {
            heap.add(data[i]);
        }
        List<Integer> sort = heap.sort();
        System.out.println(sort);
    }

}
