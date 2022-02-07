/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package org.apache.stratos.autoscaler.event.publisher;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.stratos.messaging.broker.publish.EventPublisher;
import org.apache.stratos.messaging.broker.publish.EventPublisherPool;
import org.apache.stratos.messaging.event.Event;
import org.apache.stratos.messaging.event.instance.notifier.InstanceCleanupClusterEvent;
import org.apache.stratos.messaging.event.instance.notifier.InstanceCleanupMemberEvent;
import org.apache.stratos.messaging.util.MessagingUtil;

public class InstanceNotificationPublisher {
    private static final Log log = LogFactory.getLog(InstanceNotificationPublisher.class);

    public static InstanceNotificationPublisher getInstance() {
        return InstanceHolder.INSTANCE;
    }

    private static void publish(Event event) {
        String topic = MessagingUtil.getMessageTopicName(event);

        EventPublisher instanceNotifyingEvent = EventPublisherPool.
                getPublisher(topic);
        instanceNotifyingEvent.publish(event);
    }

    public synchronized void sendInstanceCleanupEventForCluster(String clusterId, String instanceId) {
        log.info(String.format("Publishing Instance Cleanup Event: [cluster] %s " +
                "[cluster-instance] %s", clusterId, instanceId));
        publish(new InstanceCleanupClusterEvent(clusterId, instanceId));
    }

    /**
     * Publishing the instance termination notification to the instances
     *
     * @param memberId
     */
    public synchronized void sendInstanceCleanupEventForMember(String memberId) {
        log.info(String.format("Publishing Instance Cleanup Event: [member] %s", memberId));
        publish(new InstanceCleanupMemberEvent(memberId));
    }

    /* An instance of InstanceNotificationPublisher is created when the class is loaded.
     * Since the class is loaded only once, it is guaranteed that an object of
     * InstanceNotificationPublisher is created only once. Hence it is singleton.
     */
    private static class InstanceHolder {
        private static final InstanceNotificationPublisher INSTANCE = new InstanceNotificationPublisher();
    }
}
