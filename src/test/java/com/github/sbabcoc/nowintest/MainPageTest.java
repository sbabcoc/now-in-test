package com.github.sbabcoc.nowintest;

import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

import java.util.Set;

import org.testng.annotations.Test;

import com.github.sbabcoc.nowintest.components.ForYouFeedComponent;
import com.github.sbabcoc.nowintest.components.NewsResourceCard;
import com.github.sbabcoc.nowintest.page.MainPage;
import com.github.sbabcoc.nowintest.page.ResourcePage;
import com.nordstrom.automation.selenium.annotations.InitialPage;
import com.nordstrom.automation.selenium.model.ContainerMethodInterceptor;
import com.nordstrom.automation.selenium.model.Enhanceable;
import com.nordstrom.automation.selenium.model.Page;
import com.nordstrom.automation.selenium.support.TestNgBase;

/**
 * This is the test class for the main <b>Now in Android</b> page.
 */
@InitialPage(MainPage.class)
public class MainPageTest extends TestNgBase {

    /**
     * #1: Ensure application launches and opens the main page.
     */
    @Test
    public void testLoadComplete() {
        MainPage mainPage = getInitialPage();
        boolean isLoaded = ContainerMethodInterceptor.waitForLoadCompletion(mainPage);
        assertTrue(isLoaded, "Page load incomplete");
    }
    
    /**
     * #2: Ensure that the navigation tabs are shown.
     */
    @Test
    public void testNavigationTabsAreShown() {
        MainPage mainPage = getInitialPage();
        ContainerMethodInterceptor.waitForLoadCompletion(mainPage);
        assertTrue(mainPage.getTabBar().isForYouTabShown(), "'For you' tab is not shown");
        assertTrue(mainPage.getTabBar().isSavedTabShown(), "'Saved' tab is not shown");
        assertTrue(mainPage.getTabBar().isInterestsTabShown(), "'Interests' tab is not shown");
    }
    
    /**
     * Ensure that the "For You" feed selectors are displayed.
     */
    @Test
    public void testForYouFeedIsShownAndPopulated() {
        MainPage mainPage = getInitialPage();
        ContainerMethodInterceptor.waitForLoadCompletion(mainPage);
        ForYouFeedComponent forYouFeed = mainPage.getForYouFeed();
        assertTrue(forYouFeed.isDisplayed(), "'For You' feed is not shown");
        Set<String> topicsMap = forYouFeed.getTopicsList();
        assertFalse(topicsMap.isEmpty(), "'For You' feed has no topics");
    }
    
    /**
     * #6: Verify feed navigation: <ol>
     *     <li>Select the "Headlines" topic</li>
     *     <li>Get the first news resource card</li>
     *     <li>Open the resource page (implicitly verified)</li>
     *     <li>Navigate back to the NIA app (implicitly verified)</li>
     *     <li>Verify that the expected page object type was returned</li>
     * </ol>
     */
    @Test
    public void testSelectHeadlinesTopic() {
        MainPage mainPage = getInitialPage();
        ContainerMethodInterceptor.waitForLoadCompletion(mainPage);
        ForYouFeedComponent forYouFeed = mainPage.getForYouFeed();
        forYouFeed.getTopicSelection("Headlines").select();
        NewsResourceCard resourceCard = forYouFeed.getFirstNewsResourceCard();
        ResourcePage resourcePage = resourceCard.openResourcePage();
        Page backPage = resourcePage.backToNIA();
        Class<?> backPageClass = Enhanceable.getContainerClass(backPage);
        assertTrue(backPageClass.isAssignableFrom(MainPage.class), "Incorrect 'back' page class: " + backPageClass);
    }
    
}
