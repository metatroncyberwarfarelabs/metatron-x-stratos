/*
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
 */

package org.apache.stratos.kubernetes.client;

/**
 * Kubernetes constants.
 */
public class KubernetesConstants {

    public static final String POD_STATUS_RUNNING = "Running";
    public static final String POLICY_PULL_IF_NOT_PRESENT = "IfNotPresent";
    public static final String POLICY_PULL_ALWAYS = "Always";
    public static final String POLICY_PULL_NEVER = "Never";
    public static final String SESSION_AFFINITY_CLIENT_IP = "ClientIP";
    public static final String KIND_POD = "Pod";
    public static final String KIND_SERVICE = "Service";
    public static final String SERVICE_SELECTOR_LABEL = "serviceSelector";
    public static final String RESOURCE_CPU = "cpu";
    public static final String RESOURCE_MEMORY = "memory";
    public static final String NODE_PORT = "NodePort";
    public static final String CLUSTER_IP = "ClusterIP";
    public static final int MAX_LABEL_LENGTH = 63;
    public static final String SECRET_TYPE_DOCKERCFG = "kubernetes.io/dockercfg";
}
