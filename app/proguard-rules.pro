-dontwarn org.bouncycastle.jsse.BCSSLParameters
-dontwarn org.bouncycastle.jsse.BCSSLSocket
-dontwarn org.bouncycastle.jsse.provider.BouncyCastleJsseProvider
-dontwarn org.conscrypt.Conscrypt$Version
-dontwarn org.conscrypt.Conscrypt
-dontwarn org.openjsse.javax.net.ssl.SSLParameters
-dontwarn org.openjsse.javax.net.ssl.SSLSocket
-dontwarn org.openjsse.net.ssl.OpenJSSE

-keep class java.awt.** { *; }
-keep class javax.imageio.** { *; }
-keep class org.bouncycastle.** { *; }
-keep class org.conscrypt.** { *; }
-keep class org.openjsse.** { *; }
-keep class org.slf4j.** { *; }

#============== NEW ADDED ==============
# Keep all classes and their members in the main packages
-keep class com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.hslf.record.** { *; }
-keep class com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.ddf.** { *; }
-keep class com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.openxml4j.** { *; }
-keep class com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.hslf.** { *; }
-keep class com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.** { *; }

# Keep constructors for all classes in these packages
-keepclassmembers class com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.hslf.record.** {
    public <init>(...);
}
-keepclassmembers class com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.ddf.** {
    public <init>(...);
}
-keepclassmembers class com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.openxml4j.** {
    public <init>(...);
}
-keepclassmembers class com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.hslf.** {
    public <init>(...);
}
-keepclassmembers class com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.** {
    public <init>(...);
}

# Keep all inner classes and their members in these packages
-keep class com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.hslf.record.**$* { *; }
-keep class com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.ddf.**$* { *; }
-keep class com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.openxml4j.**$* { *; }
-keep class com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.hslf.**$* { *; }
-keep class com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.**$* { *; }

# Keep all enums and annotations in these packages
-keepclassmembers enum com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.** { *; }
-keepclassmembers @interface com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.** { *; }

-dontwarn com.facebook.infer.annotation.Nullsafe$Mode
-dontwarn com.facebook.infer.annotation.Nullsafe
-keep class com.artifex.mupdfdemo.** {*;}

-dontwarn com.gemalto.jp2.JP2Decoder
-dontwarn com.gemalto.jp2.JP2Encoder
-dontwarn javax.naming.Binding
-dontwarn javax.naming.NamingEnumeration
-dontwarn javax.naming.NamingException
-dontwarn javax.naming.directory.Attribute
-dontwarn javax.naming.directory.Attributes
-dontwarn javax.naming.directory.DirContext
-dontwarn javax.naming.directory.InitialDirContext
-dontwarn javax.naming.directory.SearchControls
-dontwarn javax.naming.directory.SearchResult

-keep class com.google.gson.stream.** { *; }
# Prevent R8 from leaving Data object members always null
-keepclassmembers,allowobfuscation class * {
  @com.google.gson.annotations.SerializedName <fields>;
}
# Retain fields annotated with @SerializedName
-keepclassmembers class ** {
    @com.google.gson.annotations.SerializedName <fields>;
}
# Retain all annotations for Gson
-keepattributes RuntimeVisibleAnnotations
# Gson specific classes
-dontwarn sun.misc.**
-keep class com.google.gson.** { *; }
# Retain generic signatures of TypeToken and its subclasses
-keep,allowobfuscation,allowshrinking class com.google.gson.reflect.TypeToken
-keep,allowobfuscation,allowshrinking class * extends com.google.gson.reflect.TypeToken

# Parcelable
# Do not obfuscate methods in classes annotated with @Parcelize
-keepclassmembers class * implements android.os.Parcelable {
    public static final android.os.Parcelable$Creator CREATOR;
}
# Retain @Parcelize classes
-keepnames class **$$Parcelable { *; }
-keep @kotlinx.parcelize.Parcelize public class *

-keep class * extends com.google.gson.TypeAdapter
-keep class * implements com.google.gson.TypeAdapterFactory
-keep class * implements com.google.gson.JsonSerializer
-keep class * implements com.google.gson.JsonDeserializer
-keepclasseswithmembers class * {
    <init>(...);
    @com.google.gson.annotations.SerializedName <fields>;
}
-keep,allowobfuscation,allowshrinking class com.google.gson.reflect.TypeToken
-keep,allowobfuscation,allowshrinking class * extends com.google.gson.reflect.TypeToken


-dontwarn okhttp3.**
-keep class okhttp3.** { *; }

# Retrofit
-keepclassmembers,allowshrinking,allowobfuscation interface * {
    @retrofit2.http.* <methods>;
}
-dontwarn org.codehaus.mojo.animal_sniffer.IgnoreJRERequirement
-dontwarn javax.annotation.**
-dontwarn kotlin.Unit
-dontwarn retrofit2.KotlinExtensions
-dontwarn retrofit2.KotlinExtensions$*
-if interface * { @retrofit2.http.* <methods>; }
-keep,allowobfuscation interface <1>
-if interface * { @retrofit2.http.* <methods>; }
-keep,allowobfuscation interface * extends <1>
-keep,allowobfuscation,allowshrinking class kotlin.coroutines.Continuation
-if interface * { @retrofit2.http.* public *** *(...); }
-keep,allowoptimization,allowshrinking,allowobfuscation class <3>
-keep,allowobfuscation,allowshrinking class retrofit2.Response
-dontwarn okhttp3.internal.platform.**
-dontwarn org.conscrypt.**
-dontwarn org.bouncycastle.**
-dontwarn org.openjsse.**
-dontwarn retrofit.**
-keep class retrofit.** { *; }
-keepattributes Signature
-keepattributes Exceptions
-keepclasseswithmembers class * {
    @retrofit.http.* <methods>;
}
-keep class retrofit.** { *; }
-dontwarn org.apache.tools.zip.ZipEntry
-dontwarn org.apache.tools.zip.ZipFile

-dontwarn org.spongycastle.cert.X509CertificateHolder
-dontwarn org.spongycastle.cms.CMSEnvelopedData
-dontwarn org.spongycastle.cms.Recipient
-dontwarn org.spongycastle.cms.RecipientId
-dontwarn org.spongycastle.cms.RecipientInformation
-dontwarn org.spongycastle.cms.RecipientInformationStore
-dontwarn org.spongycastle.cms.jcajce.JceKeyTransEnvelopedRecipient
-dontwarn org.spongycastle.cms.jcajce.JceKeyTransRecipient