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
        /**  */
        VIEW_TITLE(By.xpath("//android.widget.TextView[normalize-space(@text)='Saved']")),
        BOOKMARKS_FEED(AppiumBy.androidUIAutomator("new UiSelector().resourceId(\"bookmarks:feed\")")),
        BOOKMARKS_EMPTY(AppiumBy.androidUIAutomator("new UiSelector().resourceId(\"bookmarks:empty\")")),
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
    
    public boolean isBookmarksPlaceholderShown() {
        return !findElements(Using.BOOKMARKS_EMPTY).isEmpty();
    }
    
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
    
    private static By scrollingLocatorWithResourceId(final String resourceId) {
        return AppiumBy.androidUIAutomator(
                "new UiScrollable(new UiSelector().scrollable(true)).scrollIntoView(new UiSelector().resourceId(\""
                        + resourceId + "\"))");
    }
    
    private static By contextLocatorWithResourceId(final String resourceId) {
        return AppiumBy.xpath("//*[@resource-id='" + resourceId + "']/parent::*");
    }
}
