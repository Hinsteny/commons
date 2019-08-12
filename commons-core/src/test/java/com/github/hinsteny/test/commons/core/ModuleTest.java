package com.github.hinsteny.test.commons.core;

import com.github.hinsteny.commons.core.algorithm.structure.BinaryHeap;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * @author Hinsteny
 * @version ModuleTest: ModuleTest 2019-06-28 10:08 All rights reserved.$
 */
public class ModuleTest {

    @Test
    public void obtainingModuleInfoCase() {
        Module myClassModule = BinaryHeap.class.getModule();
        boolean named = myClassModule.isNamed();
        Assert.assertTrue(!named, "");
        boolean open = myClassModule.isOpen("");
        Assert.assertTrue(open, "");
    }
}
