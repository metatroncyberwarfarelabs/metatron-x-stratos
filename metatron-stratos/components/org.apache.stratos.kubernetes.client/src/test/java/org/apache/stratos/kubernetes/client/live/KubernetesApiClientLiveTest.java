/*
 *
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 *
 */
package org.apache.stratos.kubernetes.client.live;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.stratos.kubernetes.client.exceptions.KubernetesClientException;
import org.junit.Test;
import org.junit.experimental.categories.Category;

import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

/**
 * Notes:
 * Please ssh into the kubernetes custer and pull the docker image before running
 * the live tests. Otherwise tests would fail when running for the first time on a fresh
 * kubernetes cluster.
 * <p/>
 * Caution!
 * At the end of the tests it will remove all the replication controllers, pods and services
 * available in the given kubernetes environment.
 */
@Category(org.apache.stratos.kubernetes.client.LiveTests.class)
public class KubernetesApiClientLiveTest extends AbstractLiveTest {

    private static final Log log = LogFactory.getLog(KubernetesApiClientLiveTest.class);

    @Test
    public void testPodCreation() throws Exception {
        log.info("Testing pod creation...");
        Map<String, String> podLabels1 = new HashMap<>();
        podLabels1.put("applicationId", "my-application-1");
        Map<String, String> podAnnocations1 = new HashMap<>();
        podAnnocations1.put("test", "test");
        createPod("stratos-test-pod-1", "stratos-test-pod", podLabels1, podAnnocations1, "http-1", "1", "512Mi", null, null);

        Map<String, String> podLabels2 = new HashMap<>();
        podLabels2.put("applicationId", "my-application-2");
        Map<String, String> podAnnocations2 = new HashMap<>();
        podAnnocations2.put("test", "test");
        createPod("stratos-test-pod-2", "stratos-test-pod", podLabels2, podAnnocations2, "http-1", "2", "4Gi", null, null);

        deletePod("stratos-test-pod-1");
        deletePod("stratos-test-pod-2");
    }

    @Test
    public void testDeletingAnNonExistingPod() {
        try {
            client.deletePod("-1234");
        } catch (Exception e) {
            assertEquals(true, e instanceof KubernetesClientException);
        }
    }

    @Test
    public void testServiceCreation() throws Exception {
        log.info("Testing service creation...");

        String serviceId = "tomcat-domain-1";
        String serviceName = "stratos-test-pod";
        String containerPortName = "http-1";
        String serviceType = "NodePort";

        Map<String, String> serviceLabels1 = new HashMap<>();
        serviceLabels1.put("applicationId", "my-application-1");

        Map<String, String> annotationMap = new HashMap<>();
        annotationMap.put("test", "test");

        createService(serviceId, serviceName, serviceLabels1, annotationMap, SERVICE_PORT, serviceType,
                containerPortName, containerPort, minionPublicIPs);

        Map<String, String> podLabels3 = new HashMap<>();
        podLabels3.put("applicationId", "my-application-3");
        Map<String, String> podAnnocations3 = new HashMap<>();
        podAnnocations3.put("test", "test");
        createPod("stratos-test-pod-3", serviceName, podLabels3, podAnnocations3, containerPortName, "1", "512", null, null);

        Map<String, String> podLabels4 = new HashMap<>();
        podLabels4.put("applicationId", "my-application-4");
        Map<String, String> podAnnocations4 = new HashMap<>();
        podAnnocations4.put("test", "test");
        createPod("stratos-test-pod-4", serviceName, podLabels4, podAnnocations4, containerPortName, "2", "512", null, null);

        if (testServiceSocket) {
            // test service accessibility
            log.info(String.format("Connecting to service: [portal] %s:%d", minionPublicIPs.get(0), SERVICE_PORT));
            sleep(4000);
            Socket socket = new Socket(minionPublicIPs.get(0), SERVICE_PORT);
            assertTrue(socket.isConnected());
            log.info(String.format("Connecting to service successful: [portal] %s:%d", minionPublicIPs.get(0),
                    SERVICE_PORT));
        }

        deleteService(serviceId);

        deletePod("stratos-test-pod-3");
        deletePod("stratos-test-pod-4");
    }
}
