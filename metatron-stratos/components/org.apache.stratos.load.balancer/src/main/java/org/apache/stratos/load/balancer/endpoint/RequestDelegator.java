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

package org.apache.stratos.load.balancer.endpoint;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.stratos.load.balancer.algorithm.LoadBalanceAlgorithm;
import org.apache.stratos.load.balancer.common.domain.Cluster;
import org.apache.stratos.load.balancer.common.domain.Member;
import org.apache.stratos.load.balancer.common.topology.TopologyProvider;
import org.apache.stratos.load.balancer.conf.LoadBalancerConfiguration;
import org.apache.stratos.load.balancer.context.AlgorithmContext;
import org.apache.stratos.load.balancer.context.ClusterContext;
import org.apache.stratos.load.balancer.context.LoadBalancerContext;

import java.util.ArrayList;

/**
 * Implements core load balancing logic for identifying the next member
 * according to the incoming request information.
 */
public class RequestDelegator {

    private static final Log log = LogFactory.getLog(RequestDelegator.class);

    private LoadBalanceAlgorithm algorithm;

    public RequestDelegator(LoadBalanceAlgorithm algorithm) {
        this.algorithm = algorithm;
    }

    /**
     * Find the next member in a cluster by applying a load balancing algorithm by the given host name.
     *
     * @param hostName  host name of the cluster
     * @param messageId synapse message id to be included in debugging logs
     * @return
     */
    public Member findNextMemberFromHostName(String hostName, String messageId) {
        if (hostName == null)
            return null;

        long startTime = System.currentTimeMillis();

        TopologyProvider topologyProvider = LoadBalancerConfiguration.getInstance().getTopologyProvider();
        Cluster cluster = topologyProvider.getClusterByHostName(hostName);
        if (cluster != null) {
            if (log.isDebugEnabled()) {
                log.debug(String.format("Cluster %s identified for request %s", cluster.getClusterId(), messageId));
            }
            Member member = findNextMemberInCluster(cluster);
            if (member != null) {
                if (log.isDebugEnabled()) {
                    long endTime = System.currentTimeMillis();
                    log.debug(String.format("Next member identified in %dms: [service] %s [cluster] %s [member] %s [message-id] %s",
                            (endTime - startTime), member.getServiceName(), member.getClusterId(), member.getMemberId(), messageId));
                }
            }
            return member;
        } else {
            if (log.isWarnEnabled()) {
                log.warn(String.format("Could not find a cluster for hostname %s", hostName));
            }
        }
        return null;
    }

    /**
     * Find the next member in a cluster by applying a load balancing algorithm by the given host name and tenant id.
     *
     * @param hostName host name of the cluster
     * @param tenantId tenant id of the incoming request
     * @return
     */
    public Member findNextMemberFromTenantId(String hostName, int tenantId) {
        long startTime = System.currentTimeMillis();

        // Find cluster from host name and tenant id
        TopologyProvider topologyProvider = LoadBalancerConfiguration.getInstance().getTopologyProvider();
        Cluster cluster = topologyProvider.getClusterByHostName(hostName, tenantId);
        if (cluster != null) {
            Member member = findNextMemberInCluster(cluster);
            if (member != null) {
                if (log.isDebugEnabled()) {
                    long endTime = System.currentTimeMillis();
                    log.debug(String.format("Next member identified in %dms: [service] %s [cluster] %s [tenant-id] %d [member] %s",
                            (endTime - startTime), member.getServiceName(), member.getClusterId(), tenantId, member.getMemberId()));
                }
            }
            return member;
        } else {
            if (log.isWarnEnabled()) {
                log.warn(String.format("Could not find a cluster for hostname %s and tenant-id %d", hostName, tenantId));
            }
        }
        return null;
    }

    /**
     * Find next member in the cluster by applying a load balancing algorithm.
     * <p/>
     * This operation should be synchronized in order to find a member
     * correctly. This has no performance impact as per the load tests
     * carried out.
     */
    private synchronized Member findNextMemberInCluster(Cluster cluster) {
        // Find algorithm context of the cluster
        ClusterContext clusterContext = LoadBalancerContext.getInstance().getClusterContext(cluster.getClusterId());
        if (clusterContext == null) {
            clusterContext = new ClusterContext(cluster.getServiceName(), cluster.getClusterId());
            LoadBalancerContext.getInstance().addClusterContext(clusterContext);
        }

        AlgorithmContext algorithmContext = clusterContext.getAlgorithmContext();
        if (algorithmContext == null) {
            algorithmContext = new AlgorithmContext(cluster.getServiceName(), cluster.getClusterId());
            clusterContext.setAlgorithmContext(algorithmContext);
        }
        algorithm.setMembers(new ArrayList<Member>(cluster.getMembers()));
        Member member = algorithm.getNextMember(algorithmContext);
        if (member == null) {
            if (log.isWarnEnabled()) {
                log.warn(String.format("Could not find a member in cluster: [service] %s [cluster] %s",
                        cluster.getServiceName(), cluster.getClusterId()));
            }
        }
        return member;
    }

    public boolean isTargetHostValid(String hostName) {
        if (hostName == null)
            return false;

        TopologyProvider topologyProvider = LoadBalancerConfiguration.getInstance().getTopologyProvider();
        boolean valid = topologyProvider.clusterExistsByHostName(hostName);
        return valid;
    }

    public Cluster getCluster(String hostName) {
        TopologyProvider topologyProvider = LoadBalancerConfiguration.getInstance().getTopologyProvider();
        return topologyProvider.getClusterByHostName(hostName);
    }
}
