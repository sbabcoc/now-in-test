package com.github.sbabcoc.nowintest.page;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import com.github.sbabcoc.nowintest.components.InterestsListComponent;
import io.appium.java_client.AppiumBy;

/**
 * This class is the model for the <b>Interests</b> view of the <b>Now in Android</b> app.
 * <p>
 * <b>NOTE</b>: This class extends {@link PageTemplate}, which provides common features.
 */
public class InterestsPage extends PageTemplate {
    
    private InterestsListComponent interestsList;

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
        /** view title locator */
        VIEW_TITLE(AppiumBy.xpath("//android.widget.TextView[normalize-space(@text)='Interests']")),
        /** locator for the interest topics component container */
        INTEREST_TOPICS(AppiumBy.androidUIAutomator("new UiSelector().resourceId(\"interests:topics\")"));
        
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
    
    /**
     * Get the interests list page component.
     * 
     * @return {@link InterestsListComponent} object
     */
    public InterestsListComponent getInterestsList() {
        if (interestsList == null) {
            interestsList = new InterestsListComponent(Using.INTEREST_TOPICS.locator, this);
        }
        return interestsList;
    }
}
