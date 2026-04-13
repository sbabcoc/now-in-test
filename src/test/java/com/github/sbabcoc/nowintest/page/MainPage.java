package com.github.sbabcoc.nowintest.page;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import com.github.sbabcoc.nowintest.components.ForYouFeedComponent;
import io.appium.java_client.AppiumBy;

/**
 * This class is the model for the main view of the <b>Now in Android</b> app.
 */
public class MainPage extends PageTemplate {

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
    public By usingViewTitle() {
        return Using.VIEW_TITLE.locator;
    }
    
    public ForYouFeedComponent getForYouFeed() {
    	if (forYouFeed == null) {
    		forYouFeed = new ForYouFeedComponent(Using.FOR_YOU_FEED.locator, this);
    	}
    	return forYouFeed;
    }
}
