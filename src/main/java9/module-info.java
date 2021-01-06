/**
 * Jeyzer demo application
 */
module org.jeyzer.demo {

	exports org.jeyzer.demo.features.mx.generic.feature;
	exports org.jeyzer.demo.features.mx.generic.fighter;
	exports org.jeyzer.demo.labors.job.executable.impl;
	
	requires org.jeyzer.demo.shared;
	requires org.jeyzer.publish;
	requires org.jeyzer.demo.dup1;
	requires org.jeyzer.demo.dup2;
	requires org.jeyzer.demo.dup3;
	requires java.management;
	requires org.slf4j;
	
	requires static org.jeyzer.annotations;

}