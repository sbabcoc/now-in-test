package com.github.sbabcoc.nowintest.page;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import com.github.sbabcoc.nowintest.components.NewsResourceCard;
import com.nordstrom.automation.selenium.model.RobustWebElement;

import io.appium.java_client.AppiumBy;

/**
 * This class is the model for the <b>Saved</b> view of the <b>Now in Android</b> app.
 */
public class SavedPage extends PageTemplate {
    
    /**
     * Constructor for main view context.
     * 
     * @param driver driver object
     */
    public SavedPage(WebDriver driver) {
        super(driver);
    }
    
    /**
     * This enumeration defines element locator constants.
     */
    protected enum Using implements ByEnum {
        /** view title locator */
        VIEW_TITLE(AppiumBy.xpath("//android.widget.TextView[normalize-space(@text)='Saved']")),
        /** locator for bookmarks feed container element */
        BOOKMARKS_FEED(AppiumBy.androidUIAutomator("new UiSelector().resourceId(\"bookmarks:feed\")")),
        /** locator for "No saved updates" placeholder */
        BOOKMARKS_EMPTY(AppiumBy.androidUIAutomator("new UiSelector().resourceId(\"bookmarks:empty\")")),
        /** common news resource card container (each container declares a unique resource ID) */
        NEWS_RESOURCE_CARD(AppiumBy.androidUIAutomator("new UiSelector().resourceIdMatches(\"^newsResourceCard:.*\")"));
        
        private final By locator;
        
        Using(By locator) {
            this.locator = locator;
        }

        @Override
        public By locator() {
            return locator;
        }
    }

    @Override
    public By usingViewTitle() {
        return Using.VIEW_TITLE.locator;
    }
    
    /**
     * Determine if the "No saved updates" placeholder is shown.
     * 
     * @return {@code true} if placeholder is shown; otherwise {@code false}
     */
    public boolean isBookmarksPlaceholderShown() {
        return !findElements(Using.BOOKMARKS_EMPTY).isEmpty();
    }
    
    /**
     * Get the first news resource card in the collection.
     * 
     * @return {@link NewsResourceCard} object
     */
    public NewsResourceCard getFirstNewsResourceCard() {
        WebElement card = findElement(Using.NEWS_RESOURCE_CARD);
        String resourceId = card.getAttribute("resource-id");
        By scrollingLocator = scrollingLocatorWithResourceId(resourceId);
        By contextLocator = contextLocatorWithResourceId(resourceId);
        getParentPage().findElement(scrollingLocator);
        
        try {
            Thread.sleep(300);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new NoSuchElementException("Interrupted during settle time interval");
        }
        
        RobustWebElement contextElement = (RobustWebElement) getParentPage().findElement(contextLocator);
        return new NewsResourceCard(scrollingLocator, contextElement, this);
    }
    
    /**
     * Get the scrolling locator that will reveal the identified topic selection element.
     * 
     * @param resourceId desired topic selection identifier
     * @return scrolling locator to reveal the identified topic selection element
     */
    private static By scrollingLocatorWithResourceId(final String resourceId) {
        return AppiumBy.androidUIAutomator(
                "new UiScrollable(new UiSelector().scrollable(true)).scrollIntoView(new UiSelector().resourceId(\""
                        + resourceId + "\"))");
    }
    
    /**
     * Get the context locator for the identified topic selection element.
     * 
     * @param resourceId desired topic selection identifier
     * @return context locator for the identified topic selection element
     */
    private static By contextLocatorWithResourceId(final String resourceId) {
        return AppiumBy.xpath("//*[@resource-id='" + resourceId + "']/parent::*");
    }
}
