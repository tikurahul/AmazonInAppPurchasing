Amazon In-App Purchasing SDK (Phonegap 2.0 Plugin)
==================================================

This project is a Phonegap 2.0 plugin, for the Amazon Appstore for Android's In-App Purchasing SDK.

To get started:

1. Go to developer.amazon.com, and download the In-App purchasing sdk, and the Amazon SDK Tester.
2. adb install -r AmazonSDKTester.apk (the Amazon Appstore SDK Tester)
3. adb push ./assets/amazon.sdktester.json /mnt/sdcard. (Copy the amazon.sdktester.json file in the assets folder to /mnt/sdcard)
4. git clone this project, and build / run it to play with the plugin.

Here is a screenshot representing the purchase flow.

![Purchase flow screenshot](https://github.com/tikurahul/AmazonInAppPurchasing/raw/master/assets/sdk_purchase.png "Screenshot for the purchase flow")