package com.github.sbabcoc.nowintest.page;

import org.openqa.selenium.WebDriver;

import com.nordstrom.automation.selenium.interfaces.DetectsLoadCompletion;
import com.nordstrom.automation.selenium.model.Page;

import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.nativekey.AndroidKey;
import io.appium.java_client.android.nativekey.KeyEvent;
import io.appium.java_client.appmanagement.ApplicationState;

/**
 * This is a generic model for a news resource page.
 */
public class ResourcePage extends Page implements DetectsLoadCompletion<ResourcePage> {

    @Override
    public boolean isLoadComplete() {
        ApplicationState state = ((AndroidDriver) driver).queryAppState("com.google.samples.apps.nowinandroid.demo.debug");
        return state == ApplicationState.RUNNING_IN_BACKGROUND;
    }

    public ResourcePage(WebDriver driver) {
        super(driver);
    }
    
    /**
     * Tap the Android BACK key to navigate back to the NIA app.
     * 
     * @param <T> landing page type
     * @return "back" navigation landing page object
     */
    @SuppressWarnings("unchecked")
    public <T extends Page> T backToNIA() {
        ((AndroidDriver) driver).pressKey(new KeyEvent(AndroidKey.BACK));
        return Page.newPage((Class<T>) getContainerClass(getSpawningPage()), driver);
    }

}
