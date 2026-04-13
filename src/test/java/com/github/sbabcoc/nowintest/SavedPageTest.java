package com.github.sbabcoc.nowintest;

import static org.testng.Assert.assertTrue;

import org.testng.annotations.Test;

import com.github.sbabcoc.nowintest.page.MainPage;
import com.github.sbabcoc.nowintest.page.SavedPage;
import com.nordstrom.automation.selenium.annotations.InitialPage;
import com.nordstrom.automation.selenium.model.ContainerMethodInterceptor;
import com.nordstrom.automation.selenium.support.TestNgBase;

@InitialPage(MainPage.class)
public class SavedPageTest extends TestNgBase {

    @Test
    public void testLoadComplete() {
        MainPage mainPage = getInitialPage();
        ContainerMethodInterceptor.waitForLoadCompletion(mainPage);
        SavedPage savedPage = mainPage.getTabBar().openSavedPage();
        boolean isLoaded = savedPage.isLoadComplete();
        assertTrue(isLoaded, "Page load incomplete");
    }
    
    @Test
    public void testNavigationTabsAreShown() {
        MainPage mainPage = getInitialPage();
        ContainerMethodInterceptor.waitForLoadCompletion(mainPage);
        SavedPage savedPage = mainPage.getTabBar().openSavedPage();
        assertTrue(savedPage.getTabBar().isForYouTabShown(), "'For you' tab is not displayed");
        assertTrue(savedPage.getTabBar().isSavedTabShown(), "'Saved' tab is not displayed");
        assertTrue(savedPage.getTabBar().isInterestsTabShown(), "'Interests' tab is not displayed");
    }
    
}
