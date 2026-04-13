package com.github.sbabcoc.nowintest.components;

import java.util.List;
import java.util.Optional;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import com.github.sbabcoc.nowintest.page.InterestsPage;
import com.github.sbabcoc.nowintest.page.MainPage;
import com.github.sbabcoc.nowintest.page.SavedPage;
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
        return isTabShown(Using.TAB_FOR_YOU).isPresent();
    }
    
    public boolean isForYouTabSelected() {
        return isTabSelected(Using.TAB_FOR_YOU);
    }
    
    public MainPage openMainPage() {
        if (getParentPage() instanceof MainPage) {
            throw new UnsupportedOperationException("The main page is already open");
        }
        isTabShown(Using.TAB_FOR_YOU).orElseThrow(() -> new RuntimeException("'For you' tab is absent or hidden")).click();
        return new MainPage(driver);
    }
    
    public SavedPage openSavedPage() {
        if (getParentPage() instanceof SavedPage) {
            throw new UnsupportedOperationException("The 'Saved' page is already open");
        }
        isTabShown(Using.TAB_SAVED).orElseThrow(() -> new RuntimeException("'Saved' tab is absent or hidden")).click();
        return new SavedPage(driver);
    }
    
    public InterestsPage openInterestsPage() {
        if (getParentPage() instanceof InterestsPage) {
            throw new UnsupportedOperationException("The 'Interests' page is already open");
        }
        isTabShown(Using.TAB_INTERESTS).orElseThrow(() -> new RuntimeException("'Interests' tab is absent or hidden")).click();
        return new InterestsPage(driver);
    }
    
    public boolean isSavedTabShown() {
        return isTabShown(Using.TAB_SAVED).isPresent();
    }
    
    public boolean isSavedTabSelected() {
        return isTabSelected(Using.TAB_SAVED);
    }
    
    public boolean isInterestsTabShown() {
        return isTabShown(Using.TAB_INTERESTS).isPresent();
    }
    
    public boolean isInterestsTabSelected() {
        return isTabSelected(Using.TAB_INTERESTS);
    }
    
    private Optional<WebElement> maybeTab(final ByEnum locator) {
        List<WebElement> tabList = findElements(locator);
        return (tabList.isEmpty()) ? Optional.empty() : Optional.of(tabList.get(0));
    }
    
    private Optional<WebElement> isTabShown(final ByEnum locator) {
        return maybeTab(locator).filter(WebElement::isDisplayed);
    }
    
    private boolean isTabSelected(final ByEnum locator) {
        return isTabShown(locator).map(WebElement::isSelected).orElse(false);
    }
}
