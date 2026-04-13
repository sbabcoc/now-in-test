package com.github.sbabcoc.nowintest.components;

import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebElement;

import com.google.common.collect.ImmutableMap;
import com.nordstrom.automation.selenium.model.ComponentContainer;
import com.nordstrom.automation.selenium.model.PageComponent;
import com.nordstrom.automation.selenium.model.RobustWebElement;

import io.appium.java_client.AppiumBy;

public class ForYouFeedComponent extends PageComponent {

    private Set<String> topics;
    private Map<String, TopicSelection> topicMap = new HashMap<>();

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
            "new UiScrollable(new UiSelector().scrollable(true)).setAsHorizontalList().scrollForward()")),
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
    
    public NewsResourceCard getFirstNewsResourceCard() {
        WebElement card = findElement(Using.NEWS_RESOURCE_CARD);
        String resourceId = card.getAttribute("resource-id");
        By locator = scrollingLocatorWithResourceId(resourceId);
        RobustWebElement element = (RobustWebElement) getParentPage().findElement(locator);
        return new NewsResourceCard(element, this);
    }
    
    public Set<String> getAllTopics() {
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
    
    public TopicSelection getTopicSelection(final String topic) {
        TopicSelection topicSelection = null;
        if (!getAllTopics().contains(topic)) {
            throw new IllegalArgumentException("Unrecognized topic: " + topic);
        }
        if (topicMap.containsKey(topic)) {
            topicSelection = topicMap.get(topic);
            topicSelection.reveal();
        } else {
            By scrollingLocator = scrollingLocatorWithDescription(topic);
            By contextLocator = contextLocatorWithDescription(topic);
            getParentPage().findElement(scrollingLocator);
            
            try {
                Thread.sleep(300);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                throw new NoSuchElementException("Interrupted during settle time pause");
            }
            
            RobustWebElement contextElement = (RobustWebElement) getParentPage().findElement(contextLocator);
            topicSelection = new TopicSelection(scrollingLocator, contextElement, this);
            topicMap.put(topic, topicSelection);
        }
        return topicSelection;
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
    
    private static By scrollingLocatorWithDescription(final String description) {
        return AppiumBy.androidUIAutomator(
                "new UiScrollable(new UiSelector().scrollable(true)).scrollIntoView(new UiSelector().description(\""
                        + description + "\"))");
    }
    
    private static By contextLocatorWithDescription(final String description) {
        return AppiumBy.xpath("//*[@content-desc='" + description + "']/parent::*");
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
