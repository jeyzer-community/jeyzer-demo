-dontoptimize
-dontshrink

-renamesourcefileattribute SourceFile
-keepattributes SourceFile,LineNumberTable

-keep public class org.jeyzer.demo.features.FeatureDemo {
    public static void main(java.lang.String[]);
}

-keep public class org.jeyzer.demo.philosopher2.PhilosophersDemo {
    public static void main(java.lang.String[]);
}

-keep public class org.jeyzer.demo.tollbooth.TollDemo {
    public static void main(java.lang.String[]);
}

-keep public class org.jeyzer.demo.labors.LaborsDemo {
    public static void main(java.lang.String[]);
}

-keep public class org.jeyzer.demo.labors.job.executable.impl.*

-keep public class org.jeyzer.demo.labors.job.executable.impl.PlaneMXBean {
	public java.lang.String getState();
	public long getAltitude();
	public java.lang.String getModel();
	public long getAge();
}

-keep public class org.jeyzer.demo.features.mx.generic.feature.DemoFeaturesMXBean {
	public java.lang.String getDemoName();
	public java.lang.String getVersion();
	public java.lang.String getPlayedFeatures();
}

-keep public class org.jeyzer.demo.features.mx.generic.feature.FeaturesPublisher {
	public java.lang.String getDemoName();
	public java.lang.String getVersion();
	public java.lang.String getPlayedFeatures();
}

-keep public class org.jeyzer.demo.features.mx.generic.volley.VolleyBallMXBean {
	public java.lang.String getFormationType();
	public java.lang.String getGameVariant();
	public java.lang.String getAction();
}

-keep public class org.jeyzer.demo.features.mx.generic.volley.VolleyBallMXBeanImpl {
	public java.lang.String getFormationType();
	public java.lang.String getGameVariant();
	public java.lang.String getAction();
}