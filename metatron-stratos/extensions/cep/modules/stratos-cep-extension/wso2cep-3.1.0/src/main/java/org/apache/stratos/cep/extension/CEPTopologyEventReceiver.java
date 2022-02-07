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

package org.apache.stratos.cep.extension;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.stratos.messaging.event.Event;
import org.apache.stratos.messaging.event.topology.CompleteTopologyEvent;
import org.apache.stratos.messaging.event.topology.MemberActivatedEvent;
import org.apache.stratos.messaging.event.topology.MemberTerminatedEvent;
import org.apache.stratos.messaging.listener.topology.CompleteTopologyEventListener;
import org.apache.stratos.messaging.listener.topology.MemberActivatedEventListener;
import org.apache.stratos.messaging.listener.topology.MemberTerminatedEventListener;
import org.apache.stratos.messaging.message.receiver.topology.TopologyEventReceiver;
import org.apache.stratos.messaging.message.receiver.topology.TopologyManager;

/**
 * CEP Topology Receiver for Fault Handling Window Processor.
 */
public class CEPTopologyEventReceiver {
    private static final Log log = LogFactory.getLog(CEPTopologyEventReceiver.class);
    private FaultHandlingWindowProcessor faultHandler;
    private TopologyEventReceiver topologyEventReceiver;

    CEPTopologyEventReceiver(FaultHandlingWindowProcessor faultHandler) {
        this.faultHandler = faultHandler;
        this.topologyEventReceiver = TopologyEventReceiver.getInstance();
        addEventListeners();
    }

    private void addEventListeners() {
        // Load member time stamp map from the topology as a one time task
        topologyEventReceiver.addEventListener(new CompleteTopologyEventListener() {
            private boolean initialized;

            @Override
            protected void onEvent(Event event) {
                if (!initialized) {
                    try {
                        TopologyManager.acquireReadLock();
                        log.info("Complete topology event received to fault handling window processor.");
                        CompleteTopologyEvent completeTopologyEvent = (CompleteTopologyEvent) event;
                        initialized = faultHandler.loadTimeStampMapFromTopology(completeTopologyEvent.getTopology());
                    } catch (Exception e) {
                        log.error("Error loading member time stamp map from complete topology event.", e);
                    } finally {
                        TopologyManager.releaseReadLock();
                    }
                }
            }
        });

        // Remove member from the time stamp map when MemberTerminated event is received.
        topologyEventReceiver.addEventListener(new MemberTerminatedEventListener() {
            @Override
            protected void onEvent(Event event) {
                MemberTerminatedEvent memberTerminatedEvent = (MemberTerminatedEvent) event;
                faultHandler.getMemberTimeStampMap().remove(memberTerminatedEvent.getMemberId());
                if (log.isDebugEnabled()) {
                    log.debug("Member was removed from the timestamp map: [member] " + memberTerminatedEvent
                            .getMemberId());

                }
            }
        });

        // Add member to time stamp map whenever member is activated
        topologyEventReceiver.addEventListener(new MemberActivatedEventListener() {
            @Override
            protected void onEvent(Event event) {
                MemberActivatedEvent memberActivatedEvent = (MemberActivatedEvent) event;

                // do not put this member if we have already received a health event
                faultHandler.getMemberTimeStampMap().putIfAbsent(memberActivatedEvent.getMemberId(),
                        System.currentTimeMillis());
                if (log.isDebugEnabled()) {
                    log.debug("Member was added to the timestamp map: [member] " + memberActivatedEvent.getMemberId());
                }
            }
        });
    }

    void destroy() {
        topologyEventReceiver.terminate();
    }
}
