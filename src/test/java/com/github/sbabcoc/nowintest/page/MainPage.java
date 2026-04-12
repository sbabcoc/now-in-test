package com.github.sbabcoc.nowintest.page;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import com.github.sbabcoc.nowintest.components.ForYouFeedComponent;
import com.github.sbabcoc.nowintest.components.TopAppBarComponent;
import com.nordstrom.automation.selenium.annotations.PageUrl;
import com.nordstrom.automation.selenium.interfaces.DetectsLoadCompletion;
import com.nordstrom.automation.selenium.model.Page;
import io.appium.java_client.AppiumBy;

/**
 * This class is the model for the main view of the <b>Now in Android</b> app.
 */
@PageUrl(appPackage="com.google.samples.apps.nowinandroid.demo.debug", value="com.google.samples.apps.nowinandroid.MainActivity")
public class MainPage extends Page implements DetectsLoadCompletion<MainPage> {

    private TopAppBarComponent topAppBar;
    private ForYouFeedComponent forYouFeed;

    /**
     * Constructor for main view context.
     * 
     * @param driver driver object
     */
    public MainPage(WebDriver driver) {
        super(driver);
    }
    
    /**
     * This enumeration defines element locator constants.
     */
    protected enum Using implements ByEnum {
        /**  */
        VIEW_TITLE(By.xpath("//android.widget.TextView[normalize-space(@text)='Now in Android']")),
		TOP_APP_BAR(AppiumBy.androidUIAutomator("new UiSelector().resourceId(\"niaTopAppBar\")")),
		FOR_YOU_FEED(AppiumBy.androidUIAutomator("new UiSelector().resourceId(\"forYou:feed\")")),
		TAB_FOR_YOU(AppiumBy.androidUIAutomator("new UiSelector().text(\"For you\")")),
		TAB_SAVED(AppiumBy.androidUIAutomator("new UiSelector().text(\"Saved\")")),
		TAB_INTERESTS(AppiumBy.androidUIAutomator("new UiSelector().text(\"Interests\")")),
		BUSY_SPINNER(By.xpath("//*[contains(@resource-id, 'loadingWheel') or contains(@content-desc, 'loadingWheel')]"));
        
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
        if (findElements(Using.VIEW_TITLE).isEmpty()) return false;
        return findElements(Using.BUSY_SPINNER).isEmpty();
    }
    
    public TopAppBarComponent getTopAppBar() {
    	if (topAppBar == null) {
    		topAppBar = new TopAppBarComponent(Using.TOP_APP_BAR.locator, this);
    	}
    	return topAppBar;
    }
    
    public ForYouFeedComponent getForYouFeed() {
    	if (forYouFeed == null) {
    		forYouFeed = new ForYouFeedComponent(Using.FOR_YOU_FEED.locator, this);
    	}
    	return forYouFeed;
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
