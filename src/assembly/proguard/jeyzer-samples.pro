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

-keep public enum org.jeyzer.demo.labors.job.executable.ExecutableJobDefinition {
    **[] $VALUES;
	public *;
}

-keep public enum org.jeyzer.demo.labors.job.system.SystemJobDefinition {
    **[] $VALUES;
	public *;
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

-keep public class org.jeyzer.demo.features.mx.generic.fighter.MilitaryFlightMissionMXBean {
	public java.lang.String getFormation();
	public java.lang.String getModel();
	public java.lang.String getAction();
}

-keep public class org.jeyzer.demo.features.mx.generic.fighter.MilitaryFlightMissionMXBeanImpl {
	public java.lang.String getFormation();
	public java.lang.String getModel();
	public java.lang.String getAction();
}