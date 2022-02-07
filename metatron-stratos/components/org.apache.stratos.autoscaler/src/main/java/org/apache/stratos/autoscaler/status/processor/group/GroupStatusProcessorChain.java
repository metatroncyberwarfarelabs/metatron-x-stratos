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
import org.apache.stratos.autoscaler.status.processor.StatusProcessorChain;

/**
 * Cluster status tracking processor chain
 */
public class GroupStatusProcessorChain extends StatusProcessorChain {
    private static final Log log = LogFactory.getLog(GroupStatusActiveProcessor.class);

    @Override
    public void initialize() {
        GroupStatusActiveProcessor groupStatusActiveProcessor =
                new GroupStatusActiveProcessor();
        add(groupStatusActiveProcessor);

        GroupStatusTerminatedProcessor groupStatusTerminatedProcessor =
                new GroupStatusTerminatedProcessor();
        add(groupStatusTerminatedProcessor);

        /*GroupStatusTerminatingProcessor groupStatusTerminatingProcessor =
                new GroupStatusTerminatingProcessor();
        add(groupStatusTerminatingProcessor);*/

        GroupStatusInactiveProcessor groupStatusInactiveProcessor =
                new GroupStatusInactiveProcessor();
        add(groupStatusInactiveProcessor);

    }

    public void process(final String idOfComponent, final String appId,
                        final String instanceId) {


        GroupStatusProcessor root = (GroupStatusProcessor) list.getFirst();
        if (root == null) {
            throw new RuntimeException("Message processor chain is not initialized");
        }
        if (log.isDebugEnabled()) {
            log.debug("GroupProcessor chain calculating the status for the group " +
                    "[ " + idOfComponent + " ]");
        }
        root.process(idOfComponent, appId, instanceId);
    }
}
