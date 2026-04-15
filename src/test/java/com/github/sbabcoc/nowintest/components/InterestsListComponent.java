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

/**
 * This class is the interests list page component.
 */
public class InterestsListComponent extends PageComponent {

    private Set<String> topics;
    private Map<String, TopicSelection> topicMap = new HashMap<>();

    public InterestsListComponent(By locator, ComponentContainer parent) {
        super(locator, parent);
    }

    /**
     * This enumeration defines element locator constants.
     */
    protected enum Using implements ByEnum {
        /** common topic textview locator (each textview declares a unique label) */
        TOPIC_TEXTVIEW(AppiumBy.className("android.widget.TextView")),
        /** locator used to scroll the interests collection forward */
        SCROLL_FORWARD(AppiumBy.androidUIAutomator(
            "new UiScrollable(new UiSelector().scrollable(true)).setAsVerticalList().scrollForward()"));
        
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
     * Get the titles of every topic selection element.
     *  
     * @return set of topic titles
     */
    public Set<String> getTopicsList() {
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

    /**
     * Get the topic selection object with the specified title.
     * 
     * @param topic topic selection title
     * @return {@link TopicSelection} object
     */
    public TopicSelection getTopicSelection(final String topic) {
        TopicSelection topicSelection = null;
        if (!getTopicsList().contains(topic)) {
            throw new IllegalArgumentException("Unrecognized topic: " + topic);
        }
        if (topicMap.containsKey(topic)) {
            topicSelection = topicMap.get(topic);
            topicSelection.reveal();
        } else {
            By scrollingLocator = scrollingLocatorWithTitle(topic);
            By contextLocator = contextLocatorWithTitle(topic);
            findElement(scrollingLocator);
            
            try {
                Thread.sleep(300);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                throw new NoSuchElementException("Interrupted during settle time interval");
            }
            
            RobustWebElement contextElement = (RobustWebElement) findElement(contextLocator);
            topicSelection = new TopicSelection(scrollingLocator, contextElement, this);
            topicMap.put(topic, topicSelection);
        }
        return topicSelection;
    }

    /**
     * Collect the set of currently visible topic title.
     */
    private void collectVisible() {
        findElements(Using.TOPIC_TEXTVIEW).stream()
                .map(e -> e.getAttribute("text"))
                .filter(s -> s != null)
                .forEach(topics::add);
    }

    /**
     * Reset the topic selection collection, returning to the topmost position.
     */
    public void reset() {
        int maxTries = 10;
        while (true) {
            boolean isFound = findElements(AppiumBy.androidUIAutomator("new UiSelector().text(\"Accessibility\")"))
                    .stream()
                    .anyMatch(WebElement::isDisplayed);

            if (isFound || --maxTries <= 0) {
                break;
            }

            ((JavascriptExecutor) driver).executeScript("mobile: swipeGesture", ImmutableMap.of(
                "elementId", ((RemoteWebElement) getWrappedElement()).getId(),
                "direction", "down",
                "percent", 1.0,
                "speed", 1500
            ));
        }
    }

    /**
     * Scroll the topic selection collection forward.
     */
    private void scrollForward() {
        getParentPage().findElement(Using.SCROLL_FORWARD);
    }
    
    /**
     * Get the scrolling locator that will reveal the titled topic selection element.
     * 
     * @param title desired topic selection title
     * @return scrolling locator to reveal the titled topic selection element
     */
    private static By scrollingLocatorWithTitle(final String title) {
        return AppiumBy.androidUIAutomator(
                "new UiScrollable(new UiSelector().scrollable(true)).scrollIntoView(new UiSelector().text(\""
                        + title + "\"))");
    }
    
    /**
     * Get the context locator for the titled topic selection element.
     * 
     * @param title desired topic selection title
     * @return context locator for the titled topic selection element
     */
    private static By contextLocatorWithTitle(final String title) {
        return AppiumBy.xpath("//*[@text='" + title + "']/parent::*");
    }
}
