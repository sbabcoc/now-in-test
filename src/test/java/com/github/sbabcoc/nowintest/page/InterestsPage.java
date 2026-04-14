package com.github.sbabcoc.nowintest.page;

import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebElement;

import com.github.sbabcoc.nowintest.components.TopicSelection;
import com.google.common.collect.ImmutableMap;
import com.nordstrom.automation.selenium.model.RobustWebElement;

import io.appium.java_client.AppiumBy;

/**
 * This class is the model for the <b>Interests</b> view of the <b>Now in Android</b> app.
 * <p>
 * <b>NOTE</b>: This class extends {@link PageTemplate}, which provides common features.
 */
public class InterestsPage extends PageTemplate {
    
    private Set<String> topics;
    private Map<String, TopicSelection> topicMap = new HashMap<>();

    /**
     * Constructor for main view context.
     * 
     * @param driver driver object
     */
    public InterestsPage(WebDriver driver) {
        super(driver);
    }
    
    /**
     * This enumeration defines element locator constants.
     */
    protected enum Using implements ByEnum {
        /** view title locator */
        VIEW_TITLE(By.xpath("//android.widget.TextView[normalize-space(@text)='Interests']")),
        /** common topic checkbox locator (each checkbox declares a unique description) */
        TOPIC_CHECKBOX(By.xpath(".//*[@content-desc]")),
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

    @Override
    public By usingViewTitle() {
        return Using.VIEW_TITLE.locator;
    }
    
    /**
     * Get the titles of every topic selection element.
     *  
     * @return set of topic titles
     */
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

    /**
     * Get the topic selection object with the specified title.
     * 
     * @param topic topic selection title
     * @return {@link TopicSelection} object
     */
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
        findElements(Using.TOPIC_CHECKBOX).stream()
                .map(e -> e.getAttribute("content-desc"))
                .filter(s -> s != null)
                .forEach(topics::add);
    }

    /**
     * Reset the topic selection collection, returning to the topmost position.
     */
    public void reset() {
        int maxTries = 10;
        WebElement container = findElement(AppiumBy.androidUIAutomator("new UiSelector().scrollable(true)"));
        while (true) {
            boolean isFound = container.findElements(AppiumBy.accessibilityId("Data Storage"))
                    .stream()
                    .anyMatch(WebElement::isDisplayed);

            if (isFound || --maxTries <= 0) {
                break;
            }

            ((JavascriptExecutor) driver).executeScript("mobile: swipeGesture", ImmutableMap.of(
                "elementId", ((RemoteWebElement) container).getId(),
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
        findElement(Using.SCROLL_FORWARD.locator);
    }
    
    /**
     * Get the scrolling locator that will reveal the described topic selection element.
     * 
     * @param description desired topic selection description
     * @return scrolling locator to reveal the described topic selection element
     */
    private static By scrollingLocatorWithDescription(final String description) {
        return AppiumBy.androidUIAutomator(
                "new UiScrollable(new UiSelector().scrollable(true)).scrollIntoView(new UiSelector().description(\""
                        + description + "\"))");
    }
    
    /**
     * Get the context locator for the described topic selection element.
     * 
     * @param description desired topic selection description
     * @return context locator for the described topic selection element
     */
    private static By contextLocatorWithDescription(final String description) {
        return AppiumBy.xpath("//*[@content-desc='" + description + "']/parent::*");
    }
    
}
