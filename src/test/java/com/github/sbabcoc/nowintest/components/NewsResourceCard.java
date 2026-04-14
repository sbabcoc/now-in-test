package com.github.sbabcoc.nowintest.components;

import java.util.Arrays;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.SearchContext;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebElement;

import com.google.common.collect.ImmutableMap;
import com.nordstrom.automation.selenium.model.ComponentContainer;
import com.nordstrom.automation.selenium.model.Page;
import com.nordstrom.automation.selenium.model.PageComponent;
import com.nordstrom.automation.selenium.model.RobustWebElement;

import io.appium.java_client.AppiumBy;

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
        /**  */
        TITLE(AppiumBy.androidUIAutomator("new UiSelector().className(\"android.widget.TextView\").index(1)")),
        BOOKMARK(AppiumBy.androidUIAutomator("new UiSelector().className(\"android.view.View\").index(2)")),
        DATE(AppiumBy.androidUIAutomator("new UiSelector().className(\"android.widget.TextView\").index(3)")),
        SUMMARY(AppiumBy.androidUIAutomator("new UiSelector().className(\"android.widget.TextView\").index(4)")),
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
    
    @Override
    public SearchContext refreshContext(long expiration) {
        // if this context is past the expiration
        if (expiration >= acquiredAt()) {
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
    
    public WebElement reveal() {
        getParentPage().findElement((By) arguments[0]);
        return getWrappedElement();
    }
    
    public Page openResource() {
        scrollToSummary();
        ((JavascriptExecutor) driver).executeScript("mobile: clickGesture",
                ImmutableMap.of("elementId", ((RemoteWebElement) getWrappedElement()).getId()));
        return new Page(driver);
    }
    
    public boolean select() {
        if (isChecked()) return false;
        toggle();
        return true;
    }
    
    public boolean reject() {
        if (!isChecked()) return false;
        toggle();
        return true;
    }
    
    public void toggle() {
        findElement(Using.BOOKMARK).click();
    }
    
    public boolean isChecked() {
        return Boolean.parseBoolean(findElement(Using.BOOKMARK).getAttribute("checked"));
    }

    public void scrollToSummary() {
        int maxTries = 10;
        WebElement container = driver.findElement(AppiumBy.androidUIAutomator("new UiSelector().scrollable(true)"));
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
