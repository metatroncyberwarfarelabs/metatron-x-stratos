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

package org.apache.stratos.autoscaler.statistics.publisher;

import org.apache.stratos.common.statistics.publisher.ThriftStatisticsPublisher;
import org.wso2.carbon.databridge.commons.StreamDefinition;

/**
 * Scaling Decision Publisher interface.
 */
public abstract class ScalingDecisionPublisher extends ThriftStatisticsPublisher {

    public ScalingDecisionPublisher(StreamDefinition streamDefinition, String thriftClientName) {
        super(streamDefinition, thriftClientName);
    }

    /**
     * Publishing scaling decision to DAS.
     *
     * @param timestamp               Scaling Time
     * @param scalingDecisionId       Scaling Decision Id
     * @param clusterId               Cluster Id
     * @param minInstanceCount        Minimum Instance Count
     * @param maxInstanceCount        Maximum Instance Count
     * @param rifPredicted            RIF Predicted
     * @param rifThreshold            RIF Threshold
     * @param rifRequiredInstances    RIF Required Instances
     * @param mcPredicted             MC Predicted
     * @param mcThreshold             MC Threshold
     * @param mcRequiredInstances     MC Required Instances
     * @param laPredicted             LA Predicted
     * @param laThreshold             LA Threshold
     * @param laRequiredInstance      LA Required Instance
     * @param requiredInstanceCount   Required Instance Count
     * @param activeInstanceCount     Active Instance Count
     * @param additionalInstanceCount Additional Instance Needed
     * @param scalingReason           Scaling Reason
     */
    public abstract void publish(Long timestamp, String scalingDecisionId, String clusterId,
                                 int minInstanceCount, int maxInstanceCount,
                                 int rifPredicted, int rifThreshold, int rifRequiredInstances,
                                 int mcPredicted, int mcThreshold, int mcRequiredInstances,
                                 int laPredicted, int laThreshold, int laRequiredInstance,
                                 int requiredInstanceCount, int activeInstanceCount, int additionalInstanceCount,
                                 String scalingReason);
}
