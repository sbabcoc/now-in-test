package com.github.sbabcoc.nowintest.page;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

/**
 * This class is the model for the <b>Interests</b> view of the <b>Now in Android</b> app.
 */
public class InterestsPage extends PageTemplate {
    
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
        VIEW_TITLE(By.xpath("//android.widget.TextView[normalize-space(@text)='Interests']"));
        
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
}
