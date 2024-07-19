# Compose Multiplatform Projects

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
