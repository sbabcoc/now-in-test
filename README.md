# now-in-test

This repository implements a set of user interface tests that target the [**Now in Android**](https://github.com/android/nowinandroid) sample application from **Google**. 

## Prerequisites
* Ensure that required development environment is installed and configured
  *  **Node.js and npm**: Download and install the latest LTS version from the official Node.js website. Verify by running `node -v` and `npm -v` in your terminal.
  * **Java Development Kit (JDK)**: Install JDK 17 or higher and set the `JAVA_HOME` environment variable to your JDK installation path.
  * **Android Studio (for Android)**: Download from the [Android Studio page](https://developer.android.com/studio).
    * Use the **SDK Manager** to install "Android SDK Platform-Tools" and "Build-Tools".
    * Use the **Device Manager** to create and launch a **Pixel 3a** emulator with **API 34**
* Install Appium Server
  Appium is now installed globally via npm. Open your terminal or command prompt and run:**Appium**
  ```bash
  npm install -g appium
  ```
  Verify the installation by checking the version: `appium --version`
* Install the Android **UiAutomator2** driver
  ```bash
  appium driver install uiautomator2
  ```
* Configure environment variables
  * **ANDROID_HOME**: Point this to your Android SDK folder (e.g., `C:\Users\Name\AppData\Local\Android\Sdk` on Windows or `~/Library/Android/sdk` on Mac).
  * **PATH Additions**: Add `%ANDROID_HOME%\platform-tools`, `%ANDROID_HOME%\emulator`, and `%JAVA_HOME%\bin` to your system's **PATH** variable
  ## Install this project
* Clone the **now-in-test** repository
  ```bash
  git clone https://github.com/sbabcoc/now-in-test.git
  ```
## Install the target application
* Clone the **Now In Android** repository and build the project
  ```bash
  git clone --depth=1 https://github.com/android/nowinandroid.git
  cd nowinandroid
  git checkout d6abd6d3 # check out expected project snapshot
  ./gradlew assembleDebug
  ```
* Navigate to the **now-in-test** project and add the **Now in Android** APK
  ```bash
  cd ../now-in-test
  mkdir -p src/test/resources/apps
  cp ../nowinandroid/app/build/outputs/apk/demo/debug/app-demo-debug.apk src/test/resources/apps/app-demo-debug.apk
  ```
## Execute the tests
 * Build and run the test suite
   ```bash
   ./gradlew clean build
   ```

# Project Details

The **now-in-test** project is build on the [Selenium Foundation](https://github.com/sbabcoc/Selenium-Foundation) framework, which implements the **Page Object Model** strategy for modeling and interacting with target applications. Using this framework promotes the implementation of well-structured automation models. It also provides automatic recovery from stale element reference failures (which are typically just "noise"), as well as automatic capture of screenshots and page source when tests fail.

**Selenium Foundation** includes hooks for automatic page-load synchronization. Page models that implement the **DetectsLoadCompletion** interface can provide scenario-specific checks to ensure that pages are completely loaded prior to proceeding with page-specific actions. The framework also supplies each page and page component with its own class-specific logger, which provides a direct association between log messages and the classes that produced them.

## Technical Rationale for Test Case 6

* **Wait/Sync**: Every action is followed by explicit validation that the expected outcome of the action is observed. This strategy improves stability and efficiency. It also ensures that tests are terminated immediately at the point where the target application exhibits unexpected behavior.
* **Locators**: I used locators based on resource identifiers or descriptions where possible. I also avoided multi-node specification unless there was no other choice, as these sorts of structural dependencies are prone to breakage.
* **Break Risk**: The universe of changed that can trigger automation failures is vast:
  * Changes in page layout or element hierarchy;
  * Alterations to element attributes;
  * Addition/removal/refactoring of features;
  * Revisions in navigation behavior
  
  Any of these can disrupt the parity between the target application and the automation models used by the tests to interact with it.
* **Failure Investigation**: The starting point for failure investigation is always the diagnostic artifacts produced by test execution - console and server logs, screenshots, and page source. The gold standard for failure messages is that they include sufficient information to diagnose the failure without needing to run/debug the test or crack open the code.  
Execution history can also be enlightening. If a test has been running reliably, then suddenly starts to fail consistently, the point at which the test began to fail is likely to be associated with a product defect or a change that disrupted application/automation model parity. A test that fails intermittently may lack state transition synchronization.
* **Out of Scope**: This test doesn't validate the content of the external resource that opens when a news resource card is tapped. This is primarily due to the fact that the **Now in Android** application doesn't provide a reliable way to determine which external application will supply the content of each resource. Consequently, there's no reasonable way to define the expected landing page for this action.

> Written with [StackEdit](https://stackedit.io/).
