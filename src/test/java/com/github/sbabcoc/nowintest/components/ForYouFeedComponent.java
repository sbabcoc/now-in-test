package com.github.sbabcoc.nowintest.components;

import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebElement;

import com.google.common.collect.ImmutableMap;
import com.nordstrom.automation.selenium.model.ComponentContainer;
import com.nordstrom.automation.selenium.model.PageComponent;
import com.nordstrom.automation.selenium.model.RobustWebElement;

import io.appium.java_client.AppiumBy;

public class ForYouFeedComponent extends PageComponent {

    private Set<String> topics;

    public ForYouFeedComponent(By locator, ComponentContainer parent) {
        super(locator, parent);
    }

    /**
     * This enumeration defines element locator constants.
     */
    protected enum Using implements ByEnum {
        /**  */
        TOPIC_CHECKBOX(By.xpath(".//*[@content-desc]")),
        SCROLL_FORWARD(AppiumBy.androidUIAutomator(
            "new UiScrollable(new UiSelector().scrollable(true)).setAsHorizontalList().scrollForward()"));
        
        private final By locator;
        
        Using(By locator) {
            this.locator = locator;
        }

        @Override
        public By locator() {
            return locator;
        }
    }
    
    public Set<String> getTopics() {
        if (topics == null) {
            reset();
            String lastSnapshot = "";
            topics = new LinkedHashSet<>();
            while (true) {
                collectVisible();
                String snapshot = topics.toString();
                if (snapshot.equals(lastSnapshot)) break;
                lastSnapshot = snapshot;
                scrollForward();
            }
            reset();
        }
        return Collections.unmodifiableSet(topics);
    }

    public RobustWebElement scrollToItem(String title) {
        return (RobustWebElement) findElement(locatorFor(title));
    }

    private void collectVisible() {
        findElements(Using.TOPIC_CHECKBOX).stream()
                .map(e -> e.getAttribute("content-desc"))
                .filter(s -> s != null)
                .forEach(topics::add);
    }

    public void reset() {
        int maxTries = 10;
        WebElement container = driver.findElement(AppiumBy.androidUIAutomator("new UiSelector().scrollable(true)"));
        while (true) {
            boolean isFound = container.findElements(AppiumBy.accessibilityId("Headlines"))
                    .stream()
                    .anyMatch(WebElement::isDisplayed);

            if (isFound || --maxTries <= 0) {
                break;
            }

            ((JavascriptExecutor) driver).executeScript("mobile: swipeGesture", ImmutableMap.of(
                "elementId", ((RemoteWebElement) container).getId(),
                "direction", "right",
                "percent", 1.0,
                "speed", 1500
            ));
        }
    }

    private void scrollForward() {
        getParentPage().findElement(Using.SCROLL_FORWARD);
    }
    
    private static By locatorFor(final String topic) {
        return AppiumBy.androidUIAutomator(
                "new UiScrollable(new UiSelector().scrollable(true)).setAsHorizontalList()" +
                ".scrollIntoView(new UiSelector().fromParent(new UiSelector().description(\"" + topic + "\")))");
    }
}
