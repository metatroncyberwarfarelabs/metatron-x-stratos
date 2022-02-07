/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package org.apache.stratos.python.cartridge.agent.integration.tests;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.stratos.messaging.domain.topology.ServiceType;
import org.apache.stratos.messaging.domain.topology.Topology;
import org.apache.stratos.messaging.event.instance.notifier.ArtifactUpdatedEvent;
import org.apache.stratos.messaging.event.topology.CompleteTopologyEvent;
import org.apache.stratos.messaging.event.topology.MemberInitializedEvent;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Test to verify backward compatibility of the Python Cartridge Agent against
 * older configuration options.
 */
public class AgentConfBackwardCompatibilityTestCase extends PythonAgentIntegrationTest {
    public AgentConfBackwardCompatibilityTestCase() throws IOException {
    }

    private static final Log log = LogFactory.getLog(AgentConfBackwardCompatibilityTestCase.class);
    private static final int TIMEOUT = 5 * 60000;
    private static final String CLUSTER_ID = "php.php.domain";
    private static final String APPLICATION_PATH = "/tmp/AgentConfBackwardCompatibilityTestCase";
    private static final String DEPLOYMENT_POLICY_NAME = "deployment-policy-1";
    private static final String AUTOSCALING_POLICY_NAME = "autoscaling-policy-1";
    private static final String APP_ID = "application-1";
    private static final String MEMBER_ID = "php.member-1";
    private static final String INSTANCE_ID = "instance-1";
    private static final String CLUSTER_INSTANCE_ID = "cluster-1-instance-1";
    private static final String NETWORK_PARTITION_ID = "network-partition-1";
    private static final String PARTITION_ID = "partition-1";
    private static final String TENANT_ID = "-1234";
    private static final String SERVICE_NAME = "php";

    @Override
    protected String getClassName() {
        return this.getClass().getSimpleName();
    }

    @BeforeMethod(alwaysRun = true)
    public void setupCompatibilityTest() throws Exception {
        System.setProperty("jndi.properties.dir", getCommonResourcesPath());

        // start Python agent with configurations provided in resource path
        super.setup(TIMEOUT);

        // Simulate server socket
        startServerSocket(8080);
    }

    @AfterMethod(alwaysRun = true)
    public void tearDownCompatibilityTest(){
        tearDown(APPLICATION_PATH);
    }

    @Test(timeOut = TIMEOUT, groups = { "smoke" })
    public void testConfigCompatibility(){
        startCommunicatorThread();
        assertAgentActivation();
    }

    private void assertAgentActivation() {
        Thread startupTestThread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (!eventReceiverInitialized) {
                    sleep(1000);
                }
                List<String> outputLines = new ArrayList<>();
                while (!outputStream.isClosed()) {
                    List<String> newLines = getNewLines(outputLines, outputStream.toString());
                    if (newLines.size() > 0) {
                        for (String line : newLines) {
                            if (line.contains("Subscribed to 'topology/#'")) {
                                sleep(2000);
                                // Send complete topology event
                                log.info("Publishing complete topology event...");
                                Topology topology = PythonAgentIntegrationTest.createTestTopology(
                                        SERVICE_NAME,
                                        CLUSTER_ID,
                                        DEPLOYMENT_POLICY_NAME,
                                        AUTOSCALING_POLICY_NAME,
                                        APP_ID,
                                        MEMBER_ID,
                                        CLUSTER_INSTANCE_ID,
                                        NETWORK_PARTITION_ID,
                                        PARTITION_ID,
                                        ServiceType.SingleTenant);
                                CompleteTopologyEvent completeTopologyEvent = new CompleteTopologyEvent(topology);
                                publishEvent(completeTopologyEvent);
                                log.info("Complete topology event published");

                                // Publish member initialized event
                                log.info("Publishing member initialized event...");
                                MemberInitializedEvent memberInitializedEvent = new MemberInitializedEvent(SERVICE_NAME,
                                        CLUSTER_ID, CLUSTER_INSTANCE_ID, MEMBER_ID, NETWORK_PARTITION_ID, PARTITION_ID,
                                        INSTANCE_ID);
                                publishEvent(memberInitializedEvent);
                                log.info("Member initialized event published");
                            }
                        }
                    }
                    sleep(1000);
                }
            }
        });
        startupTestThread.start();

        while (!instanceStarted || !instanceActivated) {
            // wait until the instance activated event is received.
            // this will assert whether instance got activated within timeout period; no need for explicit assertions
            sleep(2000);
        }
    }
}
