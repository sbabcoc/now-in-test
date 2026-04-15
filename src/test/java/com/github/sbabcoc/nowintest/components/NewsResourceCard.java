package com.github.sbabcoc.nowintest.components;

import java.util.Arrays;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.SearchContext;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebElement;

import com.github.sbabcoc.nowintest.page.PageTemplate;
import com.github.sbabcoc.nowintest.page.ResourcePage;
import com.google.common.collect.ImmutableMap;
import com.nordstrom.automation.selenium.model.ComponentContainer;
import com.nordstrom.automation.selenium.model.PageComponent;
import com.nordstrom.automation.selenium.model.RobustWebElement;

import io.appium.java_client.AppiumBy;

/**
 * This class is the news resource card page component.
 */
public class NewsResourceCard extends PageComponent {
    
    private Class<?>[] argumentTypes;
    private Object[] arguments;
    
    private static final Class<?>[] ARG_TYPES = {By.class, RobustWebElement.class, ComponentContainer.class};
    
    public NewsResourceCard(By scrollingLocator, RobustWebElement contextElement, ComponentContainer parent) {
        super(contextElement, parent);
        
        this.argumentTypes = ARG_TYPES;
        this.arguments = new Object[] {scrollingLocator, contextElement, parent};
    }

    /**
     * This enumeration defines element locator constants.
     */
    protected enum Using implements ByEnum {
        /** generic locator for the bookmark element (matches both "Bookmark" and "Unbookmark" */
        BOOKMARK(AppiumBy.androidUIAutomator("new UiSelector().descriptionContains(\"ookmark\")")),
        /** FIXME: locator for the resource card summary (indexed reference) */
        SUMMARY(AppiumBy.androidUIAutomator("new UiSelector().className(\"android.widget.TextView\").index(4)")),
        /** generic locator for the resource card topic tags */
        TOPIC_TAG(AppiumBy.androidUIAutomator("new UiSelector().resourceIdMatches(\"^topicTag:.*\")"));
        
        private final By locator;
        
        Using(By locator) {
            this.locator = locator;
        }

        @Override
        public By locator() {
            return locator;
        }
    }
    
    /**
     * Refresh the underlying search context for this object.
     * <p>
     * <b>NOTE</b>: This override reveals the associate news resource card to enable freshening the context element.
     * 
     * @param expiration expiration time of context chain
     * @return refreshed container search context
     */
    @Override
    public SearchContext refreshContext(long expiration) {
        // if this context is past the expiration
        if (expiration >= acquiredAt()) {
            // scroll to the top of the view
            ((PageTemplate) getParentPage()).scrollToTop();
            // reveal this topic selection
            reveal();
            // continue with standard refresh
            super.refreshContext(expiration);
        }
        return this;
    }
    
    @Override
    public Class<?>[] getArgumentTypes() {
        return Arrays.copyOf(argumentTypes, argumentTypes.length);
    }
    
    @Override
    public Object[] getArguments() {
        return Arrays.copyOf(arguments, arguments.length);
    }
    
    /**
     * Reveal the news resource card associated with this page component.
     * 
     * @return page component content element
     */
    public WebElement reveal() {
        getParentPage().findElement((By) arguments[0]);
        return getWrappedElement();
    }
    
    /**
     * Open the resource page associated with this news resource card.
     * 
     * @return {@link ResourcePage} object
     */
    public ResourcePage openResourcePage() {
        scrollToSummary();
        
        try {
            Thread.sleep(300);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new NoSuchElementException("Interrupted during settle time interval");
        }
        
        findElement(Using.SUMMARY.locator).click();
        ResourcePage resourcePage = new ResourcePage(driver);
        resourcePage.setSpawningPage(getParentPage());
        return resourcePage;
    }
    
    /**
     * Set the bookmark on this news resource card.
     * <p>
     * <b>NOTE</b>: If the card is already bookmarked, no action is performed.
     * 
     * @return {@code true} if the card state changed; otherwise {@code false}
     */
    public boolean select() {
        if (isChecked()) return false;
        toggle();
        return true;
    }
    
    /**
     * Clear the bookmark on this news resource card.
     * <p>
     * <b>NOTE</b>: If the card isn't bookmarked, no action is performed.
     * 
     * @return {@code true} if the card state changed; otherwise {@code false}
     */
    public boolean reject() {
        if (!isChecked()) return false;
        toggle();
        return true;
    }
    
    /**
     * Toggle the bookmark state of this news resource card.
     * 
     * @return updated bookmark state
     */
    public boolean toggle() {
        boolean initial = isChecked();
        findElement(Using.BOOKMARK).click();
        getWait().until(context -> ((NewsResourceCard) context).isChecked() != initial);
        return !initial;
    }
    
    /**
     * Determine the current bookmark state of this news resource card.
     * 
     * @return {@code true} if the card is bookmarked; otherwise {@code false}
     */
    public boolean isChecked() {
        return "Unbookmark".equals(findElement(Using.BOOKMARK).getAttribute("content-desc"));
    }
    
    /**
     * Scroll to the summary element of this news resource card.
     */
    public void scrollToSummary() {
        int maxTries = 10;
        WebElement container = getParentPage().findElement(AppiumBy.androidUIAutomator("new UiSelector().scrollable(true)"));
        while (true) {
            boolean isFound = container.findElements(Using.SUMMARY.locator)
                    .stream()
                    .anyMatch(WebElement::isDisplayed);

            if (isFound || --maxTries <= 0) {
                break;
            }

            ((JavascriptExecutor) driver).executeScript("mobile: swipeGesture", ImmutableMap.of(
                "elementId", ((RemoteWebElement) container).getId(),
                "direction", "up",
                "percent", 0.5,
                "speed", 1500
            ));
        }
    }
}
