package com.yammer;

import org.kie.api.KieServices;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;

public class RuleEngine {

    private static final Logger LOG = LoggerFactory.getLogger(RuleEngine.class);


    private KieSession ksession;

    public RuleEngine() {
        KieServices ks = KieServices.Factory.get();

        // From the kie services, a container is created from the classpath
        KieContainer kc = ks.getKieClasspathContainer();

        this.ksession = kc.newKieSession("TestSession");
    }

    public void ruleTest(SensorReading sensorReading) {
        this.ksession.insert(sensorReading);

        LOG.info("Inserted sensorReading into ksession.");
        // and fire the org
        ksession.fireAllRules();

        LOG.info("Fired all rules.");
        // Remove comment if using logging
        // logger.close();


        // and then dispose the session
        //ksession.dispose();
        //LOG.info("Inserted message into ksession.");
    }
}
