package com.github.sbabcoc.nowintest.components;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import com.nordstrom.automation.selenium.interfaces.DetectsLoadCompletion;
import com.nordstrom.automation.selenium.model.ComponentContainer;
import com.nordstrom.automation.selenium.model.PageComponent;
import io.appium.java_client.AppiumBy;

public class TabBarComponent extends PageComponent implements DetectsLoadCompletion<TabBarComponent> {

    public TabBarComponent(By locator, ComponentContainer parent) {
        super(locator, parent);
    }

    /**
     * This enumeration defines element locator constants.
     */
    protected enum Using implements ByEnum {
        /**  */
        TAB_FOR_YOU(AppiumBy.androidUIAutomator("new UiSelector().text(\"For you\")")),
        TAB_SAVED(AppiumBy.androidUIAutomator("new UiSelector().text(\"Saved\")")),
        TAB_INTERESTS(AppiumBy.androidUIAutomator("new UiSelector().text(\"Interests\")"));
        
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
    public boolean isLoadComplete() {
        if (!isForYouTabShown()) return false;
        if (!isSavedTabShown()) return false;
        if (!isInterestsTabShown()) return false;
        return true;
    }
    
    public boolean isForYouTabShown() {
        return isTabShown(Using.TAB_FOR_YOU);
    }
    
    public boolean isSavedTabShown() {
        return isTabShown(Using.TAB_SAVED);
    }
    
    public boolean isInterestsTabShown() {
        return isTabShown(Using.TAB_INTERESTS);
    }
    
    private boolean isTabShown(final ByEnum locator) {
        List<WebElement> tabList = findElements(locator);
        return (tabList.isEmpty()) ? false : tabList.get(0).isDisplayed();
    }
}
