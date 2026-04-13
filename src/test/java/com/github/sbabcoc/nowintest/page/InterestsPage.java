package com.github.sbabcoc.nowintest.page;

import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebElement;

import com.google.common.collect.ImmutableMap;
import com.nordstrom.automation.selenium.model.RobustWebElement;

import io.appium.java_client.AppiumBy;

/**
 * This class is the model for the <b>Interests</b> view of the <b>Now in Android</b> app.
 */
public class InterestsPage extends PageTemplate {
    
    private Set<String> topics;

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
        /**  */
        VIEW_TITLE(By.xpath("//android.widget.TextView[normalize-space(@text)='Interests']")),
        INTERESTS_TOPIC(AppiumBy.androidUIAutomator("new UiSelector().resourceId(\"interests:topic\")")),
        TOPIC_CHECKBOX(By.xpath(".//*[@content-desc]")),
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

    private void scrollForward() {
        getParentPage().findElement(Using.SCROLL_FORWARD.locator);
    }
    
    private static By locatorFor(final String topic) {
        return AppiumBy.androidUIAutomator(
                "new UiScrollable(new UiSelector().scrollable(true)).setAsVerticalList()" +
                ".scrollIntoView(new UiSelector().fromParent(new UiSelector().description(\"" + topic + "\")))");
    }
}
