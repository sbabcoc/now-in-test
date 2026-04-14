package com.github.sbabcoc.nowintest;

import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

import org.testng.annotations.Test;

import com.github.sbabcoc.nowintest.components.ForYouFeedComponent;
import com.github.sbabcoc.nowintest.components.NewsResourceCard;
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
    
    @Test
    public void testEmptyBookmarks() {
        MainPage mainPage = getInitialPage();
        ContainerMethodInterceptor.waitForLoadCompletion(mainPage);
        SavedPage savedPage = mainPage.getTabBar().openSavedPage();
        assertTrue(savedPage.isBookmarksPlaceholderShown(), "Bookmarks placeholder is not shown");
    }
    
    @Test
    public void testBookmarkHeadlinesTopic() {
        MainPage mainPage = getInitialPage();
        ContainerMethodInterceptor.waitForLoadCompletion(mainPage);
        ForYouFeedComponent forYouFeed = mainPage.getForYouFeed();
        forYouFeed.getTopicSelection("Headlines").select();
        NewsResourceCard resourceCard = forYouFeed.getFirstNewsResourceCard();
        resourceCard.scrollToSummary();
        resourceCard.select();
        assertTrue(resourceCard.isChecked());
        SavedPage savedPage = mainPage.getTabBar().openSavedPage();
        assertFalse(savedPage.isBookmarksPlaceholderShown(), "Bookmarks placeholder is shown");
        resourceCard = savedPage.getFirstNewsResourceCard();
        assertTrue(resourceCard.isChecked());
    }
    

    
}
