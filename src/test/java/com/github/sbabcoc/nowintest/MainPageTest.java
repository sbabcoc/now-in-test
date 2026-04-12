package com.github.sbabcoc.nowintest;

import static org.testng.Assert.assertTrue;

import org.testng.annotations.Test;

import com.github.sbabcoc.nowintest.page.MainPage;
import com.nordstrom.automation.selenium.annotations.InitialPage;
import com.nordstrom.automation.selenium.model.ContainerMethodInterceptor;
import com.nordstrom.automation.selenium.support.TestNgBase;

@InitialPage(MainPage.class)
public class MainPageTest extends TestNgBase {

    @Test
    public void testLoadComplete() {
        MainPage page = getInitialPage();
        boolean isLoaded = ContainerMethodInterceptor.waitForLoadCompletion(page);
        assertTrue(isLoaded, "Page load incomplete");
    }
    
    @Test
    public void testNavigationTabsAreShown() {
        MainPage page = getInitialPage();
        ContainerMethodInterceptor.waitForLoadCompletion(page);
        assertTrue(page.isForYouTabShown(), "'For you' tab is not displayed");
        assertTrue(page.isSavedTabShown(), "'Saved' tab is not displayed");
        assertTrue(page.isInterestsTabShown(), "'Interests' tab is not displayed");
    }
    
}
