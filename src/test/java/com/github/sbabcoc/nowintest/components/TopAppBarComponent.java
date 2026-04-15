package com.github.sbabcoc.nowintest.components;

import org.openqa.selenium.By;

import com.nordstrom.automation.selenium.interfaces.DetectsLoadCompletion;
import com.nordstrom.automation.selenium.model.ComponentContainer;
import com.nordstrom.automation.selenium.model.PageComponent;

import io.appium.java_client.AppiumBy;

/**
 * This class is the top app bar page component.
 */
public class TopAppBarComponent extends PageComponent implements DetectsLoadCompletion<TopAppBarComponent> {

    public TopAppBarComponent(By locator, ComponentContainer parent) {
        super(locator, parent);
    }

    /**
     * This enumeration defines element locator constants.
     */
    protected enum Using implements ByEnum {
        /**  */
        BUTTON_SEARCH(AppiumBy.xpath(".//*[@content-desc='Search']/following-sibling::android.widget.Button")),
        BUTTON_SETTINGS(AppiumBy.xpath(".//*[@content-desc='Settings']/following-sibling::android.widget.Button"));
        
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
        return true;
    }
    
}
