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
package org.apache.stratos.autoscaler.pojo.policy.deployment;

import org.apache.stratos.common.Properties;

import java.io.Serializable;

public class ApplicationPolicy implements Serializable {

    private static final long serialVersionUID = -2851334419121310395L;
    private String id;
    private String algorithm;
    private String[] networkPartitions;
    private Properties properties;
    // if networkPartitionGroups property is set, we are populating following variable.
    private String[] networkPartitionGroups;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAlgorithm() {
        return algorithm;
    }

    public void setAlgorithm(String algorithm) {
        this.algorithm = algorithm;
    }

    public String[] getNetworkPartitions() {
        return networkPartitions;
    }

    public void setNetworkPartitions(String[] networkPartitions) {
        this.networkPartitions = networkPartitions;
    }

    public Properties getProperties() {
        return properties;
    }

    public void setProperties(Properties properties) {
        this.properties = properties;
    }

    public String[] getNetworkPartitionGroups() {
        return networkPartitionGroups;
    }

    public void setNetworkPartitionGroups(String[] networkPartitionGroups) {
        this.networkPartitionGroups = networkPartitionGroups;
    }
}
