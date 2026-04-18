package com.github.sbabcoc.nowintest;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

import java.util.Set;

import org.testng.annotations.Test;

import com.github.sbabcoc.nowintest.components.ForYouFeedComponent;
import com.github.sbabcoc.nowintest.components.InterestsListComponent;
import com.github.sbabcoc.nowintest.components.TopicSelection;
import com.github.sbabcoc.nowintest.page.InterestsPage;
import com.github.sbabcoc.nowintest.page.MainPage;
import com.nordstrom.automation.selenium.annotations.InitialPage;
import com.nordstrom.automation.selenium.support.TestNgBase;

/**
 * This is the test class for the "Interests" page of <b>Now in Android</b>.
 */
@InitialPage(MainPage.class)
public class InterestsPageTest extends TestNgBase {

    /**
     * Ensure application launches, the main page opens, and "Interests" tab navigation opens the expected page.
     */
    @Test
    public void testLoadComplete() {
        MainPage mainPage = getInitialPage();
        InterestsPage interestsPage = mainPage.getTabBar().openInterestsPage();
        boolean isLoaded = interestsPage.isLoadComplete();
        assertTrue(isLoaded, "Page load incomplete");
    }
    
    /**
     * Ensure that the navigation tabs are shown.
     */
    @Test
    public void testNavigationTabsAreShown() {
        MainPage mainPage = getInitialPage();
        InterestsPage interestsPage = mainPage.getTabBar().openInterestsPage();
        assertTrue(interestsPage.getTabBar().isForYouTabShown(), "'For you' tab is not displayed");
        assertTrue(interestsPage.getTabBar().isSavedTabShown(), "'Saved' tab is not displayed");
        assertTrue(interestsPage.getTabBar().isInterestsTabShown(), "'Interests' tab is not displayed");
    }
    
    /**
     * Ensure that the interests list topic selectors are displayed.
     */
    @Test
    public void testInterestsListIsShownAndPopulated() {
        MainPage mainPage = getInitialPage();
        InterestsPage interestsPage = mainPage.getTabBar().openInterestsPage();
        InterestsListComponent interestsList = interestsPage.getInterestsList();
        assertTrue(interestsList.isDisplayed(), "Interest list is not shown");
        Set<String> topicsList = interestsList.getTopicsList();
        assertFalse(topicsList.isEmpty(), "Interests list has no topics");
    }
    
    /**
     * Verify topic selection behavior: <ol>
     *     <li>Get the list of topics</li>
     *     <li>Select the "Compose" topic</li>
     *     <li>Navigate to the "Interests" view</li>
     *     <li>Verify that the "Compose" topic is selected</li>
     *     <li>Verify that the topics list matches the list from the main page</li>
     * </ol>
     */
    @Test
    public void testSelectComposeTopic() {
        MainPage mainPage = getInitialPage();
        ForYouFeedComponent forYouFeed = mainPage.getForYouFeed();
        Set<String> topicsList = forYouFeed.getTopicsList();
        forYouFeed.getTopicSelection("Compose").select();
        InterestsPage interestsPage = mainPage.getTabBar().openInterestsPage();
        InterestsListComponent interestsList = interestsPage.getInterestsList();
        TopicSelection topicSelection = interestsList.getTopicSelection("Compose");
        assertTrue(topicSelection.isChecked());
        assertEquals(interestsList.getTopicsList(), topicsList, "Topics list on Interests page differs from main page list");
    }
    
}
