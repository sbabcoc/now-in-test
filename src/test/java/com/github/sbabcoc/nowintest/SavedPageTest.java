package com.github.sbabcoc.nowintest;

import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

import org.testng.annotations.Test;

import com.github.sbabcoc.nowintest.components.ForYouFeedComponent;
import com.github.sbabcoc.nowintest.components.NewsResourceCard;
import com.github.sbabcoc.nowintest.page.MainPage;
import com.github.sbabcoc.nowintest.page.SavedPage;
import com.nordstrom.automation.selenium.annotations.InitialPage;
import com.nordstrom.automation.selenium.support.TestNgBase;

/**
 * This is the test class for the "Saved" page of <b>Now in Android</b>.
 */
@InitialPage(MainPage.class)
public class SavedPageTest extends TestNgBase {

    /**
     * Ensure application launches, the main page opens, and "Saved" tab navigation opens the expected page.
     */
    @Test
    public void testLoadComplete() {
        MainPage mainPage = getInitialPage();
        SavedPage savedPage = mainPage.getTabBar().openSavedPage();
        boolean isLoaded = savedPage.isLoadComplete();
        assertTrue(isLoaded, "Page load incomplete");
    }
    
    /**
     * Ensure that the navigation tabs are shown.
     */
    @Test
    public void testNavigationTabsAreShown() {
        MainPage mainPage = getInitialPage();
        SavedPage savedPage = mainPage.getTabBar().openSavedPage();
        assertTrue(savedPage.getTabBar().isForYouTabShown(), "'For you' tab is not displayed");
        assertTrue(savedPage.getTabBar().isSavedTabShown(), "'Saved' tab is not displayed");
        assertTrue(savedPage.getTabBar().isInterestsTabShown(), "'Interests' tab is not displayed");
    }
    
    /**
     * Ensure that the "Saved" page shows the "No saved updates" placeholder by default.
     */
    @Test
    public void testEmptyBookmarks() {
        MainPage mainPage = getInitialPage();
        SavedPage savedPage = mainPage.getTabBar().openSavedPage();
        assertTrue(savedPage.isBookmarksPlaceholderShown(), "Bookmarks placeholder is not shown");
    }
    
    /**
     * Verify basic bookmark behavior: <ol>
     *     <li>Select the "Headlines" topic</li>
     *     <li>Get the first news resource card</li>
     *     <li>Scroll the resource card into view</li>
     *     <li>Bookmark the resource card</li>
     *     <li>Verify that the resource card is bookmarked</li>
     *     <li>Navigate to the "Saved" view</li>
     *     <li>Ensure that the "No saved updates" placeholder is not shown</li>
     *     <li>Get the first news resource card</li>
     *     <li>Verify that the resource card is bookmarked</li>
     * </ol>
     */
    @Test
    public void testBookmarkHeadlinesTopic() {
        MainPage mainPage = getInitialPage();
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
