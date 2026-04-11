package com.github.sbabcoc.nowintest.page;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import com.nordstrom.automation.selenium.annotations.PageUrl;
import com.nordstrom.automation.selenium.model.Page;

/**
 * This class is the model for the main view of the <b>Now in Android</b> app.
 */
@PageUrl(appPackage="com.google.samples.apps.nowinandroid.demo.debug", value="com.google.samples.apps.nowinandroid.MainActivity")
public class MainPage extends Page {

    private static final String PKG = "com.google.samples.apps.nowinandroid.demo.debug";

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
        /** search query "prefill" field */
        TOP_APP_BAR(By.id("niaTopAppBar"));
        
        private final By locator;
        
        Using(By locator) {
            this.locator = locator;
        }

        @Override
        public By locator() {
            return locator;
        }
    }
    
    public void foo() {
        findElement(Using.TOP_APP_BAR);
    }
}
