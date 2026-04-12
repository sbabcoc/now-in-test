package com.github.sbabcoc.nowintest.components;

import org.openqa.selenium.By;

import com.nordstrom.automation.selenium.model.ComponentContainer;
import com.nordstrom.automation.selenium.model.PageComponent;

public class ForYouTopicSelection extends PageComponent {
    
    public ForYouTopicSelection(By locator, ComponentContainer parent) {
        super(locator, parent);
    }
    
    /**
     * This enumeration defines element locator constants.
     */
    protected enum Using implements ByEnum {
        /**  */
        TOPIC_TITLE(By.className("android.widget.TextView")),
        BUSY_SPINNER(By.className("android.widget.ProgressBar"));
        
        private final By locator;
        
        Using(By locator) {
            this.locator = locator;
        }

        @Override
        public By locator() {
            return locator;
        }
    }
    
    public String getTitle() {
        return findElement(Using.TOPIC_TITLE).getText();
    }
    
    public boolean select() {
        if (isChecked()) return false;
        toggle();
        return true;
    }
    
    public boolean reject() {
        if (!isChecked()) return false;
        toggle();
        return true;
    }
    
    public void toggle() {
        getWrappedElement().click();
    }
    
    public boolean isChecked() {
        return Boolean.parseBoolean(getWrappedElement().getAttribute("checked"));
    }
    
    public boolean isBusy() {
        return !findElements(Using.BUSY_SPINNER).isEmpty();
    }
    
}
