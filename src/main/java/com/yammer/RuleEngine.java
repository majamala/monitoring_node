package com.yammer;

import org.kie.api.KieServices;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class RuleEngine {

    private static final Logger LOG = LoggerFactory.getLogger(RuleEngine.class);


    private KieSession ksession;

    public RuleEngine() {
        KieServices ks = KieServices.Factory.get();
        KieContainer kc = ks.getKieClasspathContainer();
        this.ksession = kc.newKieSession("TestSession");
    }

    public void ruleTest(SensorReading sensorReading) {
        this.ksession.insert(sensorReading);

        LOG.info("Inserted sensorReading into ksession.");
        ksession.fireAllRules();

        LOG.info("Fired all rules.");

    }
}
