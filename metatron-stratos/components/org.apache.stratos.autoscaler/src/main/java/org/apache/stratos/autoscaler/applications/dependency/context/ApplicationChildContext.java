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
package org.apache.stratos.autoscaler.applications.dependency.context;

import org.apache.stratos.messaging.domain.topology.ClusterStatus;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

/**
 * This is to keep track of the group/cluster status and their dependencies
 */
public abstract class ApplicationChildContext {

    protected boolean started;
    protected boolean hasStartupDependents;
    protected boolean isGroupScalingEnabled;
    private String id;
    private boolean terminated;
    private ClusterStatus status;
    private Stack<ClusterStatus> statusLifeCycle;
    private List<ApplicationChildContext> applicationChildContextList;
    private ApplicationChildContext parent;

    public ApplicationChildContext(String id, boolean killDependent) {
        applicationChildContextList = new ArrayList<ApplicationChildContext>();
        statusLifeCycle = new Stack<ClusterStatus>();
        this.setHasStartupDependents(killDependent);
        this.id = id;
    }

    public List<ApplicationChildContext> getApplicationChildContextList() {
        return applicationChildContextList;
    }

    public void setApplicationChildContextList(List<ApplicationChildContext> applicationChildContextList) {
        this.applicationChildContextList = applicationChildContextList;
    }

    public void addApplicationChildContext(ApplicationChildContext applicationContext) {
        applicationChildContextList.add(applicationContext);

    }

    public void addStatusToLIfeCycle(ClusterStatus status) {
        this.statusLifeCycle.push(status);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public ClusterStatus getCurrentStatus() {
        return status;
    }

    public void setCurrentStatus(ClusterStatus status) {
        this.status = status;
    }

    public List<ClusterStatus> getStatusLifeCycle() {
        return statusLifeCycle;
    }

    public boolean hasChild() {
        boolean hasChild;
        if (this.applicationChildContextList.isEmpty()) {
            hasChild = false;
        } else {
            hasChild = true;
        }
        return hasChild;
    }


    public boolean isTerminated() {
        return terminated;
    }

    public void setTerminated(boolean terminated) {
        this.terminated = terminated;
    }

    public boolean hasStartupDependents() {
        return hasStartupDependents;
    }

    public void setHasStartupDependents(boolean isDependent) {
        this.hasStartupDependents = isDependent;
    }

    public boolean isGroupScalingEnabled() {
        return isGroupScalingEnabled;
    }

    public void setGroupScalingEnabled(boolean isDependent) {
        this.isGroupScalingEnabled = isDependent;
    }

    public ApplicationChildContext getParent() {
        return parent;
    }

    public void setParent(ApplicationChildContext parent) {
        this.parent = parent;
    }
}
