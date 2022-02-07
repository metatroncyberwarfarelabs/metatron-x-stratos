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

package org.apache.stratos.common.statistics.publisher.wso2.cep;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.stratos.common.statistics.publisher.InFlightRequestPublisher;
import org.wso2.carbon.databridge.commons.Attribute;
import org.wso2.carbon.databridge.commons.AttributeType;
import org.wso2.carbon.databridge.commons.StreamDefinition;

import java.util.ArrayList;
import java.util.List;

/**
 * WSO2 CEP in flight request count publisher.
 * <p/>
 * In-flight request count:
 * Number of requests being served at a given moment could be identified as in-flight request count.
 */
public class WSO2CEPInFlightRequestPublisher extends InFlightRequestPublisher {
    private static final Log log = LogFactory.getLog(WSO2CEPInFlightRequestPublisher.class);
    private static volatile WSO2CEPInFlightRequestPublisher wso2CEPInFlightRequestPublisher;
    private static final String DATA_STREAM_NAME = "in_flight_requests";
    private static final String VERSION = "1.0.0";
    private static final String CEP_THRIFT_CLIENT_NAME = "cep";

    private WSO2CEPInFlightRequestPublisher() {
        super(createStreamDefinition(), CEP_THRIFT_CLIENT_NAME);
    }

    public static WSO2CEPInFlightRequestPublisher getInstance() {
        if (wso2CEPInFlightRequestPublisher == null) {
            synchronized (WSO2CEPInFlightRequestPublisher.class) {
                if (wso2CEPInFlightRequestPublisher == null) {
                    wso2CEPInFlightRequestPublisher = new WSO2CEPInFlightRequestPublisher();
                }
            }
        }
        return wso2CEPInFlightRequestPublisher;
    }

    private static StreamDefinition createStreamDefinition() {
        try {
            // Create stream definition
            StreamDefinition streamDefinition = new StreamDefinition(DATA_STREAM_NAME, VERSION);
            streamDefinition.setNickName("lb stats");
            streamDefinition.setDescription("lb stats");
            List<Attribute> payloadData = new ArrayList<Attribute>();

            // Set payload definition
            payloadData.add(new Attribute("cluster_id", AttributeType.STRING));
            payloadData.add(new Attribute("cluster_instance_id", AttributeType.STRING));
            payloadData.add(new Attribute("network_partition_id", AttributeType.STRING));
            payloadData.add(new Attribute("in_flight_request_count", AttributeType.DOUBLE));
            streamDefinition.setPayloadData(payloadData);
            return streamDefinition;
        } catch (Exception e) {
            throw new RuntimeException("Could not create stream definition", e);
        }
    }

    /**
     * Publish in-flight request count of a cluster.
     *
     * @param clusterId             Cluster id
     * @param clusterInstanceId     Cluster instance id
     * @param networkPartitionId    Cluster's network partition id
     * @param inFlightRequestCount  Cluster's in-flight-request count
     */
    @Override
    public void publish(String clusterId, String clusterInstanceId, String networkPartitionId,
                        int inFlightRequestCount) {
        // Set payload values
        List<Object> payload = new ArrayList<Object>();

        if (log.isDebugEnabled()) {
            log.debug(String.format("Publishing health statistics: [cluster] %s " +
                            "[cluster-instance] %s [network-partition] %s [in-flight-request-count] %d",
                    clusterId, clusterInstanceId, networkPartitionId, inFlightRequestCount));
        }
        payload.add(clusterId);
        payload.add(clusterInstanceId);
        payload.add(networkPartitionId);
        payload.add((double) inFlightRequestCount);

        publish(payload.toArray());
    }
}
