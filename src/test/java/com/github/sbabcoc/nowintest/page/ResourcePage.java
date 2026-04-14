package com.github.sbabcoc.nowintest.page;

import org.openqa.selenium.WebDriver;

import com.nordstrom.automation.selenium.model.Page;

import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.nativekey.AndroidKey;
import io.appium.java_client.android.nativekey.KeyEvent;

public class ResourcePage extends Page {

    public ResourcePage(WebDriver driver) {
        super(driver);
    }
    
    @SuppressWarnings("unchecked")
    public <T extends Page> T backToNIA() {
        ((AndroidDriver) driver).pressKey(new KeyEvent(AndroidKey.BACK));
        return Page.newPage((Class<T>) getContainerClass(getSpawningPage()), driver);
    }

}
