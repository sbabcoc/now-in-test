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

/**
 * This class is the tab bar page component.
 */
public class TabBarComponent extends PageComponent implements DetectsLoadCompletion<TabBarComponent> {

    public TabBarComponent(By locator, ComponentContainer parent) {
        super(locator, parent);
    }

    /**
     * This enumeration defines element locator constants.
     */
    protected enum Using implements ByEnum {
        /** locator for the "For you" tab element */
        TAB_FOR_YOU(AppiumBy.androidUIAutomator("new UiSelector().text(\"For you\")")),
        /** locator for the "Saved" tab element */
        TAB_SAVED(AppiumBy.androidUIAutomator("new UiSelector().text(\"Saved\")")),
        /** locator for the "Interests" tab element */
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
    
    /**
     * Determine if the "For you" tab element is shown.
     * 
     * @return {@code true} if tab is shown; otherwise {@code false}
     */
    public boolean isForYouTabShown() {
        return isTabShown(Using.TAB_FOR_YOU).isPresent();
    }
    
    /**
     * Determine if the "For you" tab element is selected.
     * 
     * @return {@code true} if tab is selected; otherwise {@code false}
     */
    public boolean isForYouTabSelected() {
        return isTabSelected(Using.TAB_FOR_YOU);
    }
    
    /**
     * Click the "For you" tab to open the main page.
     * 
     * @return {@link MainPage} object
     * @throws UnsupportedOperationException if the main page is already active
     */
    public MainPage openMainPage() {
        if (getParentPage() instanceof MainPage) {
            throw new UnsupportedOperationException("The main page is already open");
        }
        isTabShown(Using.TAB_FOR_YOU).orElseThrow(() -> new RuntimeException("'For you' tab is absent or hidden")).click();
        return new MainPage(driver);
    }
    
    /**
     * Determine if the "Saved" tab element is shown.
     * 
     * @return {@code true} if tab is shown; otherwise {@code false}
     */
    public boolean isSavedTabShown() {
        return isTabShown(Using.TAB_SAVED).isPresent();
    }
    
    /**
     * Determine if the "Saved" tab element is selected.
     * 
     * @return {@code true} if tab is selected; otherwise {@code false}
     */
    public boolean isSavedTabSelected() {
        return isTabSelected(Using.TAB_SAVED);
    }
    
    /**
     * Click the "Saved" tab to open the <b>Saved</b> page.
     * 
     * @return {@link SavedPage} object
     * @throws UnsupportedOperationException if the <b>Saved</b> page is already active
     */
    public SavedPage openSavedPage() {
        if (getParentPage() instanceof SavedPage) {
            throw new UnsupportedOperationException("The 'Saved' page is already open");
        }
        isTabShown(Using.TAB_SAVED).orElseThrow(() -> new RuntimeException("'Saved' tab is absent or hidden")).click();
        return new SavedPage(driver);
    }
    
    /**
     * Determine if the "Interests" tab element is shown.
     * 
     * @return {@code true} if tab is shown; otherwise {@code false}
     */
    public boolean isInterestsTabShown() {
        return isTabShown(Using.TAB_INTERESTS).isPresent();
    }
    
    /**
     * Determine if the "Interests" tab element is selected.
     * 
     * @return {@code true} if tab is selected; otherwise {@code false}
     */
    public boolean isInterestsTabSelected() {
        return isTabSelected(Using.TAB_INTERESTS);
    }
    
    /**
     * Click the "Interests" tab to open the <b>Interests</b> page.
     * 
     * @return {@link InterestsPage} object
     * @throws UnsupportedOperationException if the <b>Interests</b> page is already active
     */
    public InterestsPage openInterestsPage() {
        if (getParentPage() instanceof InterestsPage) {
            throw new UnsupportedOperationException("The 'Interests' page is already open");
        }
        isTabShown(Using.TAB_INTERESTS).orElseThrow(() -> new RuntimeException("'Interests' tab is absent or hidden")).click();
        return new InterestsPage(driver);
    }
    
    /**
     * Determine if the specified tab is shown.
     * 
     * @param locator locator for desired tab
     * @return optional tab element if shown; otherwise empty optional
     */
    private Optional<WebElement> isTabShown(final ByEnum locator) {
        return maybeTab(locator).filter(WebElement::isDisplayed);
    }
    
    /**
     * Determine if the specified tab is shown and selected.
     * 
     * @param locator locator for desired tab
     * @return optional tab element if shown and selected; otherwise empty optional
     */
    private boolean isTabSelected(final ByEnum locator) {
        return isTabShown(locator).map(WebElement::isSelected).orElse(false);
    }
    
    /**
     * Get an optional element reference for the specified tab.
     * 
     * @param locator locator for desired tab
     * @return optional tab element if found; otherwise empty optional
     */
    private Optional<WebElement> maybeTab(final ByEnum locator) {
        List<WebElement> tabList = findElements(locator);
        return (tabList.isEmpty()) ? Optional.empty() : Optional.of(tabList.get(0));
    }
}
