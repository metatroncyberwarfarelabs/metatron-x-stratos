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

package org.apache.stratos.common.statistics.publisher.wso2.cep;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.stratos.common.statistics.publisher.HealthStatisticsPublisher;
import org.wso2.carbon.databridge.commons.Attribute;
import org.wso2.carbon.databridge.commons.AttributeType;
import org.wso2.carbon.databridge.commons.StreamDefinition;

import java.util.ArrayList;
import java.util.List;

/**
 * Health statistics publisher for publishing statistics to WSO2 CEP.
 */
public class WSO2CEPHealthStatisticsPublisher extends HealthStatisticsPublisher {

    private static final Log log = LogFactory.getLog(WSO2CEPHealthStatisticsPublisher.class);
    private static volatile WSO2CEPHealthStatisticsPublisher wso2CEPHealthStatisticsPublisher;
    private static final String DATA_STREAM_NAME = "cartridge_agent_health_stats";
    private static final String VERSION = "1.0.0";
    private static final String CEP_THRIFT_CLIENT_NAME = "cep";

    private WSO2CEPHealthStatisticsPublisher() {
        super(createStreamDefinition(), CEP_THRIFT_CLIENT_NAME);
    }

    public static WSO2CEPHealthStatisticsPublisher getInstance() {
        if (wso2CEPHealthStatisticsPublisher == null) {
            synchronized (WSO2CEPHealthStatisticsPublisher.class) {
                if (wso2CEPHealthStatisticsPublisher == null) {
                    wso2CEPHealthStatisticsPublisher = new WSO2CEPHealthStatisticsPublisher();
                }
            }
        }
        return wso2CEPHealthStatisticsPublisher;
    }

    private static StreamDefinition createStreamDefinition() {
        try {
            // Create stream definition
            StreamDefinition streamDefinition = new StreamDefinition(DATA_STREAM_NAME, VERSION);
            streamDefinition.setNickName("agent health stats");
            streamDefinition.setDescription("agent health stats");

            // Set payload definition
            List<Attribute> payloadData = new ArrayList<Attribute>();
            payloadData.add(new Attribute("cluster_id", AttributeType.STRING));
            payloadData.add(new Attribute("cluster_instance_id", AttributeType.STRING));
            payloadData.add(new Attribute("network_partition_id", AttributeType.STRING));
            payloadData.add(new Attribute("member_id", AttributeType.STRING));
            payloadData.add(new Attribute("partition_id", AttributeType.STRING));
            payloadData.add(new Attribute("health_description", AttributeType.STRING));
            payloadData.add(new Attribute("value", AttributeType.DOUBLE));

            streamDefinition.setPayloadData(payloadData);
            return streamDefinition;
        } catch (Exception e) {
            throw new RuntimeException("Could not create stream definition", e);
        }
    }

    /**
     * Publish health statistics to cep.
     *
     * @param clusterId          Cluster id of the member
     * @param clusterInstanceId  Cluster instance id of the member
     * @param networkPartitionId Network partition id of the member
     * @param memberId           Member id
     * @param partitionId        Partition id of the member
     * @param health             Health type: memory_consumption | load_average
     * @param value              Health type value
     */
    @Override
    public void publish(String clusterId, String clusterInstanceId, String networkPartitionId,
                        String memberId, String partitionId, String health, double value) {
        if (log.isDebugEnabled()) {
            log.debug(String.format("Publishing health statistics: [cluster] %s [cluster-instance] %s " +
                            "[network-partition] %s [partition] %s [member] %s [health] %s [value] %f",
                    clusterId, clusterInstanceId, networkPartitionId, partitionId, memberId, health, value));
        }
        // Set payload values
        List<Object> payload = new ArrayList<Object>();
        payload.add(clusterId);
        payload.add(clusterInstanceId);
        payload.add(networkPartitionId);
        payload.add(memberId);
        payload.add(partitionId);
        payload.add(health);
        payload.add(value);

        publish(payload.toArray());
    }
}
