package com.github.sbabcoc.nowintest.page;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import com.github.sbabcoc.nowintest.components.TabBarComponent;
import com.github.sbabcoc.nowintest.components.TopAppBarComponent;
import com.nordstrom.automation.selenium.annotations.PageUrl;
import com.nordstrom.automation.selenium.interfaces.DetectsLoadCompletion;
import com.nordstrom.automation.selenium.model.Page;
import io.appium.java_client.AppiumBy;

/**
 * This class is the model for the abstract view template of the <b>Now in Android</b> app.
 */
@PageUrl(appPackage="com.google.samples.apps.nowinandroid.demo.debug", value="com.google.samples.apps.nowinandroid.MainActivity")
public abstract class PageTemplate extends Page implements DetectsLoadCompletion<PageTemplate> {

    protected TopAppBarComponent topAppBar;
    protected TabBarComponent tabBar;

    /**
     * Constructor for main view context.
     * 
     * @param driver driver object
     */
    public PageTemplate(WebDriver driver) {
        super(driver);
    }
    
    /**
     * This enumeration defines element locator constants.
     */
    protected enum Using implements ByEnum {
        /**  */
        COMPOSE_ROOT(AppiumBy.androidUIAutomator("new UiSelector().resourceId(\"android:id/content\")")),
		TOP_APP_BAR(AppiumBy.androidUIAutomator("new UiSelector().resourceId(\"niaTopAppBar\")")),
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
    
    public abstract By usingViewTitle();

    @Override
    public boolean isLoadComplete() {
        if (findElements(usingViewTitle()).isEmpty()) return false;
        return findElements(Using.BUSY_SPINNER).isEmpty();
    }
    
    public TopAppBarComponent getTopAppBar() {
    	if (topAppBar == null) {
    		topAppBar = new TopAppBarComponent(Using.TOP_APP_BAR.locator, this);
    	}
    	return topAppBar;
    }
    
    public TabBarComponent getTabBar() {
        if (tabBar == null) {
            tabBar = new TabBarComponent(Using.COMPOSE_ROOT.locator, this);
        }
        return tabBar;
    }
}
