package com.github.sbabcoc.nowintest.page;

import java.util.HashMap;
import java.util.Map;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
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
        /** compose root container - used as the search context for {@link TabBarComponent} */
        COMPOSE_ROOT(AppiumBy.androidUIAutomator("new UiSelector().resourceId(\"android:id/content\")")),
        /** top app bar container */
		TOP_APP_BAR(AppiumBy.androidUIAutomator("new UiSelector().resourceId(\"niaTopAppBar\")")),
		/** locator for the "busy" spinner */
		BUSY_SPINNER(AppiumBy.xpath("//*[contains(@resource-id, 'loadingWheel') or contains(@content-desc, 'loadingWheel')]"));
        
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
     * Get the view title locator.
     * 
     * @return {@link By} object used to locate the view title
     */
    public abstract By usingViewTitle();

    /**
     * Determine if page load is complete: <ul>
     *     <li>The expected view title is found</li>
     *     <li>The "busy" spinner is not found</li>
     * </ul>
     */
    @Override
    public boolean isLoadComplete() {
        if (findElements(usingViewTitle()).isEmpty()) return false;
        return findElements(Using.BUSY_SPINNER).isEmpty();
    }
    
    /**
     * Get the top app bar page component.
     * 
     * @return {@link TopAppBarComponent} object
     */
    public TopAppBarComponent getTopAppBar() {
    	if (topAppBar == null) {
    		topAppBar = new TopAppBarComponent(Using.TOP_APP_BAR.locator, this);
    	}
    	return topAppBar;
    }
    
    /**
     * Get the tab bar page component.
     * 
     * @return {@link TabBarComponent} object
     */
    public TabBarComponent getTabBar() {
        if (tabBar == null) {
            tabBar = new TabBarComponent(Using.COMPOSE_ROOT.locator, this);
        }
        return tabBar;
    }
    
    /**
     * Scroll to the top of the current view.
     */
    public void scrollToTop() {
        Map<String, Object> params = new HashMap<>();
        params.put("direction", "up");
        params.put("percent", 1.0);

        boolean scrolled = true;
        while (scrolled) {
            Object result = ((JavascriptExecutor) driver).executeScript("mobile: scroll", params);
            scrolled = Boolean.TRUE.equals(result);
        }
    }
}
