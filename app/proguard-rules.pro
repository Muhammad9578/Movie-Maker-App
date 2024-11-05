# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in E:\Android SDK\sdk/tools/proguard/proguard-android.txt
# You can edit the include path and order by changing the proguardFiles
# directive in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# Add any project specific keep options here:
-keep public class com.sandrios.** { *; }

-keep public class android.support.v7.widget.** { *; }
-keep public class android.support.v7.internal.widget.** { *; }
-keep public class android.support.v7.internal.view.menu.** { *; }

# need this for Facebook SDK
-keepattributes Signature

-dontwarn com.facebook.**
-dontwarn com.parse.**
-dontwarn com.google.firebase.**

-keep class com.facebook.** { *; }
-keep class com.parse.** { *; }

-keepnames class com.firebase.** { *; }
-keepnames class com.shaded.fasterxml.jackson.** { *; }
-keepnames class org.shaded.apache.** { *; }
-keepnames class javax.servlet.** { *; }
-dontwarn org.w3c.dom.**
-dontwarn org.joda.time.**
-dontwarn org.shaded.apache.commons.logging.impl.**

-dontwarn okio.**

-keep public class * extends android.support.v4.view.ActionProvider {
    public <init>(android.content.Context);
}

-dontwarn android.support.**

-keep public class * implements com.bumptech.glide.module.GlideModule
-keep public enum com.bumptech.glide.load.resource.bitmap.ImageHeaderParser$** {
  **[] $VALUES;
  public *;
}
-keepattributes *Annotation*
-keepclassmembers class ** {
    @com.squareup.otto.Subscribe public *;
    @com.squareup.otto.Produce public *;}

    -keep class org.apache.http.** { *;}
    -keep class com.squareup.picasso.OkHttpDownloader
    -dontwarn org.apache.http.**
    -dontwarn com.squareup.picasso.OkHttpDownloader

    -keep class com.startapp.** {
    *;
    }
    -keepattributes Exceptions, InnerClasses, Signature, Deprecated, SourceFile, LineNumberTable, *Annotation*, EnclosingMethod
    -dontwarn android.webkit.JavascriptInterface
    -dontwarn com.startapp.**

-keepclassmembers enum * {
public static **[] values();
public static ** valueOf(java.lang.String);

}

-dontwarn com.sothree.**
-keep class com.sothree.**
-keep interface com.sothree.**
# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}
