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
package org.apache.stratos.autoscaler.applications.topic;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.stratos.autoscaler.applications.ApplicationHolder;
import org.apache.stratos.messaging.broker.publish.EventPublisher;
import org.apache.stratos.messaging.broker.publish.EventPublisherPool;
import org.apache.stratos.messaging.domain.application.Application;
import org.apache.stratos.messaging.domain.application.Applications;
import org.apache.stratos.messaging.domain.application.ClusterDataHolder;
import org.apache.stratos.messaging.domain.instance.ApplicationInstance;
import org.apache.stratos.messaging.domain.instance.GroupInstance;
import org.apache.stratos.messaging.event.Event;
import org.apache.stratos.messaging.event.application.*;
import org.apache.stratos.messaging.util.MessagingUtil;

import java.util.Set;

/**
 * This will publish application related events to application status topic.
 */
public class ApplicationsEventPublisher {
    private static final Log log = LogFactory.getLog(ApplicationsEventPublisher.class);

    public static void sendCompleteApplicationsEvent(Applications completeApplications) {
        ApplicationHolder.acquireReadLock();
        try{
            if (log.isDebugEnabled()) {
                log.debug("Publishing complete applications event...");
            }
            publishEvent(new CompleteApplicationsEvent(completeApplications));
        }finally {
            ApplicationHolder.releaseReadLock();
        }
    }

    public static void sendApplicationCreatedEvent(Application application) {
        if(log.isInfoEnabled()) {
            log.info("Sending application created event for [application] " +
                    application.getUniqueIdentifier());
        }
        publishEvent(new ApplicationCreatedEvent(application));
    }

    public static void sendApplicationUpdated(Application application) {
        if(log.isInfoEnabled()) {
            log.info("Sending application updated event for [application] " +
                    application.getUniqueIdentifier());
        }
        publishEvent(new ApplicationUpdatedEvent(application));
    }

    public static void sendApplicationDeletedEvent(String appId, Set<ClusterDataHolder> clusterData) {
        if(log.isInfoEnabled()) {
            log.info("Sending application deleted event for [application] " +
                    appId);
        }
        publishEvent(new ApplicationDeletedEvent(appId, clusterData));
    }

    public static void sendApplicationInstanceCreatedEvent(String appId,
                                                           ApplicationInstance applicationInstance) {
        if(log.isInfoEnabled()) {
            log.info("Sending application instnace created event for [application] " +
                    appId + " [instance] " + applicationInstance.getInstanceId());
        }
        publishEvent(new ApplicationInstanceCreatedEvent(appId, applicationInstance));
    }

    public static void sendGroupInstanceCreatedEvent(String appId, String groupId,
                                                     GroupInstance groupInstance) {
        if (log.isInfoEnabled()) {
            log.info("Publishing group instance created event: [application] " + appId +
                    " [group] " + groupId + " [instance] " + groupInstance.getInstanceId());
        }
        GroupInstanceCreatedEvent groupCreatedEvent =
                new GroupInstanceCreatedEvent(appId, groupId, groupInstance);
        publishEvent(groupCreatedEvent);
    }

    public static void sendGroupInstanceActivatedEvent(String appId, String groupId,
                                                       String instanceId) {
        if (log.isInfoEnabled()) {
            log.info("Publishing group instance activated event: [application] " + appId +
                    " [group] " + groupId + " [instance] " + instanceId);
        }
        GroupInstanceActivatedEvent groupActivatedEvent =
                new GroupInstanceActivatedEvent(appId, groupId, instanceId);
        publishEvent(groupActivatedEvent);
    }

    public static void sendGroupInstanceInactivateEvent(String appId, String groupId,
                                                        String instanceId) {
        if (log.isInfoEnabled()) {
            log.info("Publishing group instance inactivate event: [application] " + appId +
                    " [group] " + groupId + " [instance] " + instanceId);
        }
        GroupInstanceInactivatedEvent groupInactivateEvent =
                new GroupInstanceInactivatedEvent(appId, groupId, instanceId);
        publishEvent(groupInactivateEvent);
    }

    public static void sendGroupInstanceTerminatingEvent(String appId, String groupId,
                                                         String instanceId) {
        if (log.isInfoEnabled()) {
            log.info("Publishing group instance terminating event: [application] " + appId +
                    " [group] " + groupId + " [instance] " + instanceId);
        }
        GroupInstanceTerminatingEvent groupInTerminatingEvent =
                new GroupInstanceTerminatingEvent(appId, groupId, instanceId);
        publishEvent(groupInTerminatingEvent);
    }

    public static void sendGroupInstanceTerminatedEvent(String appId, String groupId,
                                                        String instanceId) {

        if (log.isInfoEnabled()) {
            log.info("Publishing group instance terminated event: [application] " + appId +
                    " [group] " + groupId + " [instance] " + instanceId);
        }
        GroupInstanceTerminatedEvent groupInTerminatedEvent =
                new GroupInstanceTerminatedEvent(appId, groupId, instanceId);
        publishEvent(groupInTerminatedEvent);
    }

    public static void sendApplicationInstanceActivatedEvent(String appId, String instanceId) {
        if (log.isInfoEnabled()) {
            log.info("Publishing application instance active event: [application] " + appId
                    + " [instance] " + instanceId);
        }
        ApplicationInstanceActivatedEvent applicationActivatedEvent =
                new ApplicationInstanceActivatedEvent(appId, instanceId);

        publishEvent(applicationActivatedEvent);
    }

    public static void sendApplicationInstanceInactivatedEvent(String appId, String instanceId) {
        if (log.isInfoEnabled()) {
            log.info("Publishing application instance in-activated event: [application] " + appId +
                    " [instance] " + instanceId);
        }
        ApplicationInstanceInactivatedEvent applicationInactivatedEvent =
                new ApplicationInstanceInactivatedEvent(appId, instanceId);
        publishEvent(applicationInactivatedEvent);

    }

    public static void sendApplicationInstanceTerminatingEvent(String appId, String instanceId) {
        if (log.isInfoEnabled()) {
            log.info("Publishing application instance terminating event: [application] " + appId +
                    " [instance] " + instanceId);
        }
        ApplicationInstanceTerminatingEvent applicationTerminatingEvent =
                new ApplicationInstanceTerminatingEvent(appId, instanceId);
        publishEvent(applicationTerminatingEvent);
    }

    public static void sendApplicationInstanceTerminatedEvent(String appId, String instanceId) {
        if (log.isInfoEnabled()) {
            log.info("Publishing application instance terminated event: [application] " + appId +
                    " [instance] " + instanceId);
        }
        ApplicationInstanceTerminatedEvent applicationTerminatedEvent =
                new ApplicationInstanceTerminatedEvent(appId, instanceId);
        publishEvent(applicationTerminatedEvent);
    }

    public static void publishEvent(Event event) {
        //publishing events to application status topic
        String applicationTopic = MessagingUtil.getMessageTopicName(event);
        EventPublisher eventPublisher = EventPublisherPool.getPublisher(applicationTopic);
        eventPublisher.publish(event);
    }
}
