# now-in-test


* Ensure that required development environment is installed and configured
  *  **Node.js and npm**: Download and install the latest LTS version from the official Node.js website. Verify by running `node -v` and `npm -v` in your terminal.
  * **Java Development Kit (JDK)**: Install JDK 17 or higher and set the `JAVA_HOME` environment variable to your JDK installation path.
  * **Android Studio (for Android)**: Download from the [Android Studio page](https://developer.android.com/studio). Use the **SDK Manager** to install "Android SDK Platform-Tools" and "Build-Tools".
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
* Clone the **now-in-test** repository
  ```bash
  git clone https://github.com/sbabcoc/now-in-test.git
  ```
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
 * 

To decide that application launched successfully:
