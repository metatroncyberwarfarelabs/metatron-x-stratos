package org.apache.stratos.autoscaler.util;
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


import com.hazelcast.core.HazelcastInstance;
import org.apache.axis2.engine.AxisConfiguration;
import org.apache.stratos.autoscaler.status.processor.cluster.ClusterStatusProcessorChain;
import org.apache.stratos.autoscaler.status.processor.group.GroupStatusProcessorChain;
import org.apache.stratos.common.services.ComponentStartUpSynchronizer;
import org.apache.stratos.common.services.DistributedObjectProvider;
import org.wso2.carbon.ntask.core.service.TaskService;
import org.wso2.carbon.registry.core.Registry;
import org.wso2.carbon.registry.core.session.UserRegistry;

import java.util.concurrent.ExecutorService;

public class ServiceReferenceHolder {

    private static ServiceReferenceHolder instance;
    private Registry registry;
    private TaskService taskService;
    private ClusterStatusProcessorChain clusterStatusProcessorChain;
    private GroupStatusProcessorChain groupStatusProcessorChain;
    private AxisConfiguration axisConfiguration;
    private DistributedObjectProvider distributedObjectProvider;
    private HazelcastInstance hazelcastInstance;
    private ExecutorService executorService;
    private ComponentStartUpSynchronizer componentStartUpSynchronizer;

    private ServiceReferenceHolder() {
    }

    public static ServiceReferenceHolder getInstance() {
        if (instance == null) {
            synchronized (ServiceReferenceHolder.class) {
                if (instance == null) {
                    instance = new ServiceReferenceHolder();
                }
            }
        }
        return instance;
    }

    public AxisConfiguration getAxisConfiguration() {
        return axisConfiguration;
    }

    public void setAxisConfiguration(AxisConfiguration axisConfiguration) {
        this.axisConfiguration = axisConfiguration;
    }

    public DistributedObjectProvider getDistributedObjectProvider() {
        return distributedObjectProvider;
    }

    public void setDistributedObjectProvider(DistributedObjectProvider distributedObjectProvider) {
        this.distributedObjectProvider = distributedObjectProvider;
    }

    public HazelcastInstance getHazelcastInstance() {
        return hazelcastInstance;
    }

    public void setHazelcastInstance(HazelcastInstance hazelcastInstance) {
        this.hazelcastInstance = hazelcastInstance;
    }

    public Registry getRegistry() {
        return registry;
    }

    public void setRegistry(UserRegistry governanceSystemRegistry) {
        registry = governanceSystemRegistry;
    }

    public TaskService getTaskService() {
        return taskService;
    }

    public void setTaskService(TaskService taskService) {
        this.taskService = taskService;
    }

    public ClusterStatusProcessorChain getClusterStatusProcessorChain() {
        return clusterStatusProcessorChain;
    }

    public void setClusterStatusProcessorChain(ClusterStatusProcessorChain clusterStatusProcessorChain) {
        this.clusterStatusProcessorChain = clusterStatusProcessorChain;
    }

    public GroupStatusProcessorChain getGroupStatusProcessorChain() {
        return groupStatusProcessorChain;
    }

    public void setGroupStatusProcessorChain(GroupStatusProcessorChain groupStatusProcessorChain) {
        this.groupStatusProcessorChain = groupStatusProcessorChain;
    }

    public ExecutorService getExecutorService() {
        return executorService;
    }

    public void setExecutorService(ExecutorService executorService) {
        this.executorService = executorService;
    }

    public ComponentStartUpSynchronizer getComponentStartUpSynchronizer() {
        return componentStartUpSynchronizer;
    }

    public void setComponentStartUpSynchronizer(ComponentStartUpSynchronizer componentStartUpSynchronizer) {
        this.componentStartUpSynchronizer = componentStartUpSynchronizer;
    }
}
