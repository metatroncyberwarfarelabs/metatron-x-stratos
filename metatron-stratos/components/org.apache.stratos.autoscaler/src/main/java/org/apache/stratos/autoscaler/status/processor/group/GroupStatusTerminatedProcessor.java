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
package org.apache.stratos.autoscaler.status.processor.group;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.stratos.autoscaler.applications.ApplicationHolder;
import org.apache.stratos.autoscaler.applications.topic.ApplicationBuilder;
import org.apache.stratos.autoscaler.status.processor.StatusProcessor;
import org.apache.stratos.messaging.domain.application.*;
import org.apache.stratos.messaging.domain.topology.ClusterStatus;

import java.util.Map;

/**
 * Cluster Terminated status processor
 */
@SuppressWarnings("ALL")
public class GroupStatusTerminatedProcessor extends GroupStatusProcessor {
    private static final Log log = LogFactory.getLog(GroupStatusTerminatedProcessor.class);
    private GroupStatusProcessor nextProcessor;

    @Override
    public void setNext(StatusProcessor nextProcessor) {
        this.nextProcessor = (GroupStatusProcessor) nextProcessor;
    }

    @Override
    public boolean process(String idOfComponent, String appId, String instanceId) {
        boolean statusChanged;
        statusChanged = doProcess(idOfComponent, appId, instanceId);
        if (statusChanged) {
            return true;
        }

        if (nextProcessor != null) {
            // ask the next processor to take care of the message.
            return nextProcessor.process(idOfComponent, appId, instanceId);
        } else {

            log.warn(String.format("No possible state change found for [component] %s [instance] %s", idOfComponent,
                    instanceId));
        }
        return false;
    }

    @SuppressWarnings("unchecked")
    private boolean doProcess(String idOfComponent, String appId, String instanceId) {
        ParentComponent component;
        Map<String, Group> groups;
        Map<String, ClusterDataHolder> clusterData;

        if (log.isDebugEnabled()) {
            log.debug(String.format(
                    "GroupStatusTerminatedProcessor is calculating the terminated status for [group-id] %s "
                            + "[instance-id] %s", idOfComponent, instanceId));
        }
        ApplicationHolder.acquireWriteLock();
        try {
            Application application = ApplicationHolder.getApplications().
                    getApplication(appId);
            component = application;
            if (!idOfComponent.equals(appId)) {
                //it is an application
                component = application.getGroupRecursively(idOfComponent);
            }

            //finding all the children of the application/group
            //noinspection unchecked
            groups = component.getAliasToGroupMap();
            clusterData = component.getClusterDataMap();

            if (groups.isEmpty() && getAllClusterInSameState(clusterData, ClusterStatus.Terminated, instanceId) ||
                    clusterData.isEmpty() && getAllGroupInSameState(groups, GroupStatus.Terminated, instanceId) ||
                    getAllClusterInSameState(clusterData, ClusterStatus.Terminated, instanceId)
                            && getAllGroupInSameState(groups, GroupStatus.Terminated, instanceId)) {
                //send the terminated event
                if (component instanceof Application) {
                    log.info(String.format(
                            "Sending application instance terminated for [application-id] %s, [instance-id] %s", appId,
                            instanceId));
                    ApplicationBuilder.handleApplicationInstanceTerminatedEvent(appId, instanceId);
                    return true;
                } else {
                    log.info(String.format("Sending group instance terminated for [group-id] %s, [instance-id] %s",
                            component.getUniqueIdentifier(), instanceId));
                    ApplicationBuilder
                            .handleGroupInstanceTerminatedEvent(appId, component.getUniqueIdentifier(), instanceId);
                    return true;
                }
            }
        } finally {
            ApplicationHolder.releaseWriteLock();
        }
        return false;
    }
}
