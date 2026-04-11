package com.github.sbabcoc.nowintest;

import org.testng.annotations.Test;

import com.github.sbabcoc.nowintest.page.MainPage;
import com.nordstrom.automation.selenium.annotations.InitialPage;
import com.nordstrom.automation.selenium.support.TestNgBase;

@InitialPage(MainPage.class)
public class MainPageTest extends TestNgBase {

    @Test
    public void testFoo() {
        MainPage page = getInitialPage();
        page.foo();
    }
    
}
