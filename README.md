# Compose Multiplatform Projects

![kotlin-version](https://img.shields.io/badge/kotlin-2.0.0-blue) ![jdk-version](https://img.shields.io/badge/JDK-11-red)

https://github.com/JetBrains/compose-multiplatform  

Compose Multiplatform is a declarative framework for sharing UIs across multiple 
platforms with Kotlin. It is based on Jetpack Compose and developed by JetBrains 
and open-source contributors.

You can choose the platforms across which to share your UIs using Compose Multiplatform:

- iOS (Beta)
- Android
- Desktop (Windows, MacOS, Linux)
- Web (Alpha)
- For example, you can share UIs between iOS and Android or Windows and MacOS.

https://www.jetbrains.com/fleet/  

https://developer.android.com/studio  

https://developer.apple.com/xcode/  


# Environment Setup

```shell
brew install kdoctor

[!] CocoaPods
  ! CocoaPods configuration is not required, but highly recommended for full-fledged development
  ✖ System ruby is currently used
    CocoaPods is not compatible with system ruby installation on Apple M1 computers.
    Please install ruby via Homebrew, rvm, rbenv or other tool and make it default
    Detailed information: https://stackoverflow.com/questions/64901180/how-to-run-cocoapods-on-apple-silicon-m1/66556339#66556339
```

```shell
brew install ruby

echo 'export PATH="/usr/local/opt/ruby/bin:$PATH"' >> ~/.zshrc

kdoctor

Environment diagnose (to see all details, use -v option):
[✓] Operation System
[✓] Java
[✓] Android Studio
  ! Android Studio (AI-221.6008.13.2211.9619390)
    Location: /Applications/Android Studio Electric Eel.app
    Bundled Java: openjdk 11.0.15 2022-04-19
    Kotlin Plugin: 221-1.8.0-release-for-android-studio-AS5591.52
    Kotlin Multiplatform Mobile Plugin: not installed
    Install Kotlin Multiplatform Mobile plugin - https://plugins.jetbrains.com/plugin/14936-kotlin-multiplatform-mobile
  ! Android Studio (AI-222.4459.24.2221.10121639)
    Location: /Applications/Android Studio Flamingo.app
    Bundled Java: openjdk 17.0.6 2023-01-17
    Kotlin Plugin: 222-1.8.20-release-AS3739.54
    Kotlin Multiplatform Mobile Plugin: not installed
    Install Kotlin Multiplatform Mobile plugin - https://plugins.jetbrains.com/plugin/14936-kotlin-multiplatform-mobile
[✓] Xcode
[✓] CocoaPods

Recommendations:
  ! Android Studio 2021.3 has the issue with running shared unit tests via run gutters.
    Use a newer version.
    More details: https://youtrack.jetbrains.com/issue/KTIJ-20362
Conclusion:
  ✓ Your operation system is ready for Kotlin Multiplatform Mobile Development!
```


# KMP Kotlin Multiplatform New Project Wizard

https://kmp.jetbrains.com/  


# Fleet IDE: Quick Tips

https://www.jetbrains.com/help/fleet/navigation.html

Goto popup --> `CMD + K` or click on the "Search" 🔍 button in the top-right corner.

Full-text search (Find) --> `CMD + Shift + F`

Find Fleet IDE action --> `CMD + Shift + K`


# Android Studio IDE: Project Setup

- Open Android Studio.
- Download and install the Kotlin Multiplatform plugin:
  https://plugins.jetbrains.com/plugin/14936-kotlin-multiplatform 
- Android Studio > Preferences > Plugins > Marketplace > Kotlin Multiplatform Mobile (by JetBrains)
- Install and Restart Android Studio.

| Kotlin Multiplatform Mobile Plugin |
| :--------------------------------: |
| ![Kotlin Multiplatform Mobile Plugin](Screenshots/kotlin-multiplatform-plugin.png) |


# Running the Project

## Android

https://developer.android.com/studio/run/emulator-commandline  

Be sure to add the `emulator` directory to your `~/.zprofile` or `~/.zshrc`:
```shell
export PATH=~/android-sdks/emulator:${PATH}
```

```shell
emulator -list-avds

emulator -avd Pixel_2_API_31 -netdelay none -netspeed full
```

Click on the "Run" ▶️ button. In the "RUN & DEBUG" popup window, click on:
`composeApp [Pixel 2 API 31]`.


## iOS

Was able to deploy the `CurrencyApp/composeApp` to an iPhone 15 Simulator. 
Still figuring out Fleet compatibility issues with Xcode and iOS Simulator.  
Refer to:  
https://youtrack.jetbrains.com/issue/FL-25004/Compose-multiplatform-cant-be-launched-in-iOS  

macOS <-> Xcode Compatibility List
https://developer.apple.com/support/xcode/  

At the time of this writing, the minimum supported iOS & Xcode version for Fleet 
with Compose Multiplatform is `iOS 17` and `Xcode 15`, which requires 
`macOS Ventura 13.5`.

Download Xcode 15:
https://developer.apple.com/download/all/?q=xcode%2015.2

```shell
# List available software updates for macOS
softwareupdate --list

# Update to Xcode 15 Command Line Tools
softwareupdate --install "Command Line Tools beta 7 for Xcode-15.0"
```

```shell
# List all available simulators
xcrun simctl list devices

# Example: Starts default emulator for iPhone 15 Pro Max, iOS 17.2
open -a Simulator.app

# Run a specific simulator
xcrun simctl boot "iPhone 15"
```


## Desktop

In the Fleet IDE, click on the "Run" ▶️ button in the top-right corner, which
will open the "RUN & DEBUG" popup window. Click on `composeApp [Desktop]`. The
project will build, and a Desktop app window will be opened in about a minute.


# Third-Party Currency API

https://currencyapi.com/  

Work in Progress - Add your API key to:
```shell
# local.properties
API_KEY=cur_live_your_api_key
```

For Kotlin / Compose Multiplatform projects, it requires a bit of work to share 
`local.properties` code between Android and iOS. It is recommended to use the 
BuildKonfig plugin: https://github.com/yshrsmz/BuildKonfig
