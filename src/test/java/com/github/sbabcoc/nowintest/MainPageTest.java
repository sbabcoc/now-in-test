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

@InitialPage(MainPage.class)
public class MainPageTest extends TestNgBase {

    @Test
    public void testLoadComplete() {
        MainPage mainPage = getInitialPage();
        boolean isLoaded = ContainerMethodInterceptor.waitForLoadCompletion(mainPage);
        assertTrue(isLoaded, "Page load incomplete");
    }
    
    @Test
    public void testNavigationTabsAreShown() {
        MainPage mainPage = getInitialPage();
        ContainerMethodInterceptor.waitForLoadCompletion(mainPage);
        assertTrue(mainPage.getTabBar().isForYouTabShown(), "'For you' tab is not shown");
        assertTrue(mainPage.getTabBar().isSavedTabShown(), "'Saved' tab is not shown");
        assertTrue(mainPage.getTabBar().isInterestsTabShown(), "'Interests' tab is not shown");
    }
    
    @Test
    public void testForYouFeedIsShownAndPopulated() {
        MainPage mainPage = getInitialPage();
        ContainerMethodInterceptor.waitForLoadCompletion(mainPage);
        ForYouFeedComponent forYouFeed = mainPage.getForYouFeed();
        assertTrue(forYouFeed.isDisplayed(), "'For You' feed is not shown");
        Set<String> topicsMap = forYouFeed.getAllTopics();
        assertFalse(topicsMap.isEmpty(), "'For You' feed has no topics");
    }
    
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
