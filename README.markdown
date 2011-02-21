# Tasker

## Introduction
Tasker is a really simple task management application for Android. It is intended as a foray into Android development, so it will probably not have the best practices as it starts out. Also it probably won't ever make it to the Android Marketplace. The application takes it's initial basis from the example Notes application provided as a tutorial from http://developer.android.com.
There will be an accompanying series of blog posts at http://beyondtc.wordpress.com to document my experiences with Android.

## Author
Marcus Ramsden

## Building Instructions
To build the project you need to run;

    mvn clean install

This will build and install the application, including resolving the required dependencies for the project. The project makes use of the maven-android-plugin and you can read more about the plugin by running;

    mvn help:describe -Dplugin=android

This command will give you more details about the maven-android-plugin, the plugin web page can be found at http://code.google.com/p/maven-android-plugin.

To run an emulator you will need to use the following command;

    mvn android:emulator-start

This will try to start an avd called 'Android-2-3-3' as defined in the properties section of the pom.xml file. If you want to start your own avd then run the following command;

    mvn android:emulator-start -Demulator.avd=my-avd

Once you have an emulator started or a device connected then you can run;

    mvn deploy

Thiw will install the application on the emulator or connected device. 
