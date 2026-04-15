package com.github.sbabcoc.nowintest.components;

import java.util.Arrays;

import org.openqa.selenium.By;
import org.openqa.selenium.SearchContext;
import org.openqa.selenium.WebElement;

import com.nordstrom.automation.selenium.model.ComponentContainer;
import com.nordstrom.automation.selenium.model.PageComponent;
import com.nordstrom.automation.selenium.model.RobustWebElement;

import io.appium.java_client.AppiumBy;

/**
 * This class is the topic selection page component.
 */
public class TopicSelection extends PageComponent {
    
    private Class<?>[] argumentTypes;
    private Object[] arguments;
    
    private static final Class<?>[] ARG_TYPES = {By.class, RobustWebElement.class, ComponentContainer.class};
    
    public TopicSelection(By scrollingLocator, RobustWebElement contextElement, ComponentContainer parent) {
        super(contextElement, parent);
        
        this.argumentTypes = ARG_TYPES;
        this.arguments = new Object[] {scrollingLocator, contextElement, parent};
    }

    /**
     * This enumeration defines element locator constants.
     */
    protected enum Using implements ByEnum {
        /** locator for topic selection title */
        TOPIC_TITLE(AppiumBy.className("android.widget.TextView")),
        /**  */
        CHECKBOX(AppiumBy.className("android.widget.CheckBox")),
        /**  */
        CHECK_STATE(AppiumBy.xpath(".//android.widget.CheckBox/..")),
        /** locator for topic selection "busy" spinner */
        BUSY_SPINNER(AppiumBy.className("android.widget.ProgressBar"));
        
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
    public SearchContext refreshContext(long expiration) {
        // if this context is past the expiration
        if (expiration >= acquiredAt()) {
            // reveal this topic selection
            reveal();
            // continue with standard refresh
            super.refreshContext(expiration);
        }
        return this;
    }
    
    @Override
    public Class<?>[] getArgumentTypes() {
        return Arrays.copyOf(argumentTypes, argumentTypes.length);
    }
    
    @Override
    public Object[] getArguments() {
        return Arrays.copyOf(arguments, arguments.length);
    }
    
    /**
     * Reveal the topic selection element for this component.
     * 
     * @return component context element
     */
    public WebElement reveal() {
        getParentPage().findElement((By) arguments[0]);
        return getWrappedElement();
    }

    /**
     * Get the title of this topic selection element.
     * 
     * @return topic selection title
     */
    public String getTitle() {
        return findElement(Using.TOPIC_TITLE).getText();
    }
    
    /**
     * Set the checked on this topic selection element.
     * <p>
     * <b>NOTE</b>: If the element is already checked, no action is performed.
     * 
     * @return {@code true} if the element state changed; otherwise {@code false}
     */
    public boolean select() {
        if (isChecked()) return false;
        toggle();
        return true;
    }
    
    /**
     * Clear the checked on this topic selection element.
     * <p>
     * <b>NOTE</b>: If the element isn't checked, no action is performed.
     * 
     * @return {@code true} if the element state changed; otherwise {@code false}
     */
    public boolean reject() {
        if (!isChecked()) return false;
        toggle();
        return true;
    }
    
    /**
     * Toggle the checked state of this topic selection element.
     * 
     * @return updated  state
     */
    public boolean toggle() {
        boolean initial = isChecked();
        findElement(Using.CHECKBOX).click();
        getWait().until(context -> ((TopicSelection) context).isChecked() != initial);
        return !initial;
    }
    
    /**
     * Determine the current checked state of this topic selection element.
     * 
     * @return {@code true} if the element is checked; otherwise {@code false}
     */
    public boolean isChecked() {
        return Boolean.parseBoolean(findElement(Using.CHECK_STATE).getAttribute("checked"));
    }
    
    /**
     * Determine if this topic selection element is busy.
     * 
     * @return {@code true} is element is busy; otherwise {@code false}
     */
    public boolean isBusy() {
        return !findElements(Using.BUSY_SPINNER).isEmpty();
    }
    
}
