Android-Rate
============

Android-Rate is a library to help you promote your android app by prompting users to rate the app after using it for a few days.

![screen shot](http://i.gyazo.com/286342ba215a515f2f443a7ce996cc92.gif)

## Getting Started

![Maven Badges](https://maven-badges.herokuapp.com/maven-central/com.github.miroslavcupalka/android-material-rate/badge.svg)

You can download from.

```groovy

repositories {
    maven {
        url "https://dl.bintray.com/miroslavcupalka/maven"
    }
}

dependencies {
     compile(group: 'com.miroslavcupalka', name: 'Android-Material-Rate', version: '0.5.6', ext: 'aar')
}

```

## Sample

Please try to move the [sample](https://github.com/miroslavcupalka/Android-Material-Rate/tree/master/sample).

## How to use

[![javadoc.io](https://javadocio-badges.herokuapp.com/com.github.hotchemi/android-rate/badge.svg)](http://www.javadoc.io/doc/com.github.miroslavcupalka/android-material-rate/)

## Support

Android-Rate supports over API level 9.

### Configuration

Android-Rate provides methods to configure its behavior.

```java
@Override
protected void onCreate(Bundle savedInstanceState) {
  super.onCreate(savedInstanceState);
  setContentView(R.layout.activity_main);

  AppRate.with(this)
      .setInstallDays(0) // default 10, 0 means install day.
      .setLaunchTimes(3) // default 10
      .setRemindInterval(2) // default 1
      .setShowLaterButton(true) // default true
      .setDebug(false) // default false
      .setEnforcingMaterialDialog(true) // default true
      .setOnClickButtonListener(new OnClickButtonListener() { // callback listener.
          @Override
          public void onClickButton(int which) {
              Log.d(MainActivity.class.getName(), Integer.toString(which));
          }
      })
      .monitor();

  // Show a dialog if meets conditions
  AppRate.showRateDialogIfMeetsConditions(this);
}
```

The default conditions to show rate dialog is as below:

1. App is launched more than 10 days later than installation. Change via `AppRate#setInstallDays(int)`.
2. App is launched more than 10 times. Change via `AppRate#setLaunchTimes(int)`.
3. App is launched more than 2 days after neutral button clicked. Change via `AppRate#setRemindInterval(int)`.
4. App shows neutral dialog(Remind me later) by default. Change via `setShowNeutralButton(boolean)`.
5. To specify the callback when the button is pressed. The same value as the second argument of `DialogInterface.OnClickListener#onClick` will be passed in the argument of `onClickButton`.
6. Setting `AppRate#setDebug(boolean)` will ensure that the rating request is shown each time the app is launched. **This feature is only development!**.

### Event Tracking

When you want to track significant events, write code as below.

```java

@Override
protected void onCreate(Bundle savedInstanceState) {
  super.onCreate(savedInstanceState);
  setContentView(R.layout.activity_main);
  AppRate.with(this).setEventTimes(2).monitor();
}

@Override
public void onClick() {
  AppRate.passSignificantEvent(this); // when user pass this line for the third time, dialog appears.
}
```

### Clear show dialog flag

When you want to show the dialog again, call `AppRate#clearAgreeShowDialog()`.

```java
AppRate.with(this).clearAgreeShowDialog();
```

### When the button presses on

call `AppRate#showDialog(Activity)`.

```java
AppRate.with(this).showDialog(this);
```

### Set custom view

call `AppRate#setView(View)`.

```java
LayoutInflater inflater = (LayoutInflater)this.getSystemService(LAYOUT_INFLATER_SERVICE);
View view = inflater.inflate(R.layout.custom_dialog, (ViewGroup)findViewById(R.id.layout_root));
AppRate.with(this).setView(view).monitor();
```

### Custom dialog

If you want to use your own dialog labels, override string xml resources on your application.

```xml
<resources>
    <string name="rate_dialog_title">Rate this app</string>
    <string name="rate_dialog_message">If you enjoy playing this app, would you mind taking a moment to rate it? It won\'t take more than a minute. Thanks for your support!</string>
    <string name="rate_dialog_ok">Rate It Now</string>
    <string name="rate_dialog_cancel">Remind Me Later</string>
    <string name="rate_dialog_no">No, Thanks</string>
</resources>
```

## Localization

Android-Rate currently supports the following languages:

- English
- Spanish
- French
- Chinese
- Korean
- Japanese
- Vietnamese
- Polish
- Czech
- Russian
- Ukrainian
- Hebrew
- Portuguese
- Turkish

## Contribute

1. Fork it
2. Create your feature branch (`git checkout -b my-new-feature`)
3. Commit your changes (`git commit -am 'Added some feature'`)
4. Push to the branch (`git push origin my-new-feature`)
5. Create new Pull Request
