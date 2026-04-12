package com.github.sbabcoc.nowintest.components;

import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.openqa.selenium.By;
import com.nordstrom.automation.selenium.model.ComponentContainer;
import com.nordstrom.automation.selenium.model.PageComponent;
import com.nordstrom.automation.selenium.model.RobustWebElement;

import io.appium.java_client.AppiumBy;

public class ForYouFeedComponent extends PageComponent {

    private Set<String> interestTitles;
    private Map<Object, ForYouTopicSelection> selectionsMap; 

    public ForYouFeedComponent(By locator, ComponentContainer parent) {
        super(locator, parent);
    }

    /**
     * This enumeration defines element locator constants.
     */
    protected enum Using implements ByEnum {
        /**  */
        //TOPIC_SELECTION_LIST(AppiumBy.androidUIAutomator("new UiSelector().resourceId(\"forYou:topicSelection\")")),
        TOPIC_CHECKBOX(By.xpath(".//*[@content-desc]")),
        SCROLL_BACKWARD(AppiumBy.androidUIAutomator(
            "new UiScrollable(new UiSelector().scrollable(true)).setAsHorizontalList().scrollBackward()")),
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
    
    public Map<Object, ForYouTopicSelection> getSelectionsMap() {
        if (selectionsMap == null) {
            selectionsMap = getInterestTitles().stream()
                    .collect(Collectors.toMap(s -> s, s -> new ForYouTopicSelection(locatorFor(s), this)));
        }
        return selectionsMap;
    }
    
    public Set<String> getInterestTitles() {
        if (interestTitles == null) {
            reset();
            String lastSnapshot = "";
            interestTitles = new LinkedHashSet<>();
            while (true) {
                collectVisible();
                String snapshot = interestTitles.toString();
                if (snapshot.equals(lastSnapshot)) break;
                lastSnapshot = snapshot;
                scrollForward();
            }
            reset();
        }
        return Collections.unmodifiableSet(interestTitles);
    }

    public RobustWebElement scrollToItem(String title) {
        return (RobustWebElement) findElement(locatorFor(title));
    }

    private void collectVisible() {
        findElements(Using.TOPIC_CHECKBOX).stream()
                .map(e -> e.getAttribute("content-desc"))
                .filter(s -> s != null)
                .forEach(interestTitles::add);
    }

    public void reset() {
        int maxAttempts = 3;
        By headlines = AppiumBy.xpath(".//*[@text='Headlines' or @content-desc='Headlines']");

        while (maxAttempts > 0) {
            // If Headlines is visible, we are done
            if (!getParentPage().findElements(headlines).isEmpty()) {
                break;
            }

            // Otherwise, send a strong backward scroll command
            getParentPage().findElement(Using.SCROLL_BACKWARD);
            maxAttempts--;
        }
    }

    private void scrollForward() {
        getParentPage().findElement(Using.SCROLL_FORWARD.locator);
    }
    
    private static By locatorFor(final String topic) {
        return AppiumBy.androidUIAutomator(
                "new UiScrollable(new UiSelector().scrollable(true)).setAsHorizontalList()" +
                ".scrollIntoView(new UiSelector().fromParent(new UiSelector().description(\"" + topic + "\")))");
    }
}
