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
package org.apache.stratos.autoscaler.util;

import org.wso2.carbon.utils.CarbonUtils;

import java.io.File;

public final class AutoscalerConstants {

    /**
     * Constant values for Autoscaler
     */
    public static final String ID_ELEMENT = "id";
    public static final String PARTITION_ELEMENT = "partition";
    public static final String PARTITIONS_ELEMENT = "partitions";
    public static final String PROPERTY_ELEMENT = "property";
    public static final String PROPERTY_NAME_ATTR = "name";
    public static final String PROPERTY_VALUE_ATTR = "value";
    public static final String AUTOSCALER_THREAD_POOL_ID = "autoscaler.thread.pool";
    public static final String THREAD_POOL_SIZE_KEY = "autoscaler.thread.pool.size";
    public static final String AUTOSCALER_SCHEDULER_ID = "autoscaler.scheduler.thread.pool";
    public static final String SCHEDULER_THREAD_POOL_SIZE_KEY = "autoscaler.scheduler.thread.pool.size";
    public static final int AUTOSCALER_SCHEDULER_THREAD_POOL_SIZE = 5;
    public static final int AUTOSCALER_THREAD_POOL_SIZE = 50;
    public static final String COMPONENTS_CONFIG = CarbonUtils.getCarbonConfigDirPath() +
            File.separator + "stratos-config.xml";

    /**
     * Persistence
     */
    public static final String AUTOSCALER_RESOURCE = "/autoscaler";
    public static final String AS_POLICY_RESOURCE = "/policies/autoscalingPolicies";
    public static final String APPLICATIONS_RESOURCE = "/applications";
    public static final String APPLICATION_CONTEXTS_RESOURCE = "/applicationContexts";
    public static final String APPLICATION_POLICY_RESOURCE = "/policies/applicationPolicies";
    public static final String NETWORK_PARTITION_ALGO_CTX_RESOURCE = "/algorithms/networkPartitionAlgorithmContext";
    public static final String DEPLOYMENT_POLICY_RESOURCE = "/policies/deploymentPolicies";

    /**
     * Cluster monitoring  interval
     */
    public static final String Cluster_MONITOR_INTERVAL = "autoscaler.cluster.monitorInterval";

    public static final String SERVICE_GROUP = "/groups";

    /**
     * PortRange min max
     */
    public static final int PORT_RANGE_MAX = 65535;
    public static final int PORT_RANGE_MIN = 1;

    /**
     * Payload values
     */
    public static final String PAYLOAD_DEPLOYMENT = "default";

    public static final String MONITOR_THREAD_POOL_ID = "monitor.thread.pool";
    public static final String STATS_PUBLISHER_THREAD_POOL_ID = "autoscaler.stats.publisher.thread.pool";
    public static final String MONITOR_THREAD_POOL_SIZE = "monitor.thread.pool.size";
    public static final String CLUSTER_MONITOR_SCHEDULER_ID = "cluster.monitor.scheduler";
    public static final String MEMBER_FAULT_EVENT_NAME = "member_fault";
    //scheduler
    public static final int SCHEDULE_DEFAULT_INITIAL_DELAY = 30;
    public static final int SCHEDULE_DEFAULT_PERIOD = 15;
    public static final String APPLICATION_SYNC_CRON = "1 * * * * ? *";
    public static final String APPLICATION_SYNC_TASK_NAME = "APPLICATION_SYNC_TASK";
    public static final String APPLICATION_SYNC_TASK_TYPE = "APPLICATION_SYNC_TASK_TYPE";
    public static final String AUTOSCALER_CONFIG_FILE_NAME = "autoscaler.xml";
    public static final String CLOUD_CONTROLLER_SERVICE_SFX = "services/CloudControllerService";
    public static final int CLOUD_CONTROLLER_DEFAULT_PORT = 9444;
    public static final String STRATOS_MANAGER_SERVICE_SFX = "services/InstanceCleanupNotificationService";
    public static final int STRATOS_MANAGER_DEFAULT_PORT = 9445;
    public static final String STRATOS_MANAGER_HOSTNAME_ELEMENT = "autoscaler.stratosManager.hostname";
    public static final String STRATOS_MANAGER_DEFAULT_PORT_ELEMENT = "autoscaler.stratosManager.port";
    public static final String STRATOS_MANAGER_CLIENT_TIMEOUT_ELEMENT = "autoscaler.stratosManager.clientTimeout";
    // partition properties
    public static final String REGION_PROPERTY = "region";
    public static final String MEMBER_AVERAGE_LOAD_AVERAGE = "member_average_load_average";
    public static final String MEMBER_AVERAGE_MEMORY_CONSUMPTION = "member_average_memory_consumption";
    public static final String AVERAGE_REQUESTS_IN_FLIGHT = "average_in_flight_requests";
    public static final String MEMBER_GRADIENT_LOAD_AVERAGE = "member_gradient_load_average";
    public static final String MEMBER_GRADIENT_MEMORY_CONSUMPTION = "member_gradient_memory_consumption";
    public static final String GRADIENT_OF_REQUESTS_IN_FLIGHT = "gradient_in_flight_requests";
    public static final String MEMBER_SECOND_DERIVATIVE_OF_MEMORY_CONSUMPTION = "member_second_derivative_memory_consumption";
    public static final String MEMBER_SECOND_DERIVATIVE_OF_LOAD_AVERAGE = "member_second_derivative_load_average";
    public static final String SECOND_DERIVATIVE_OF_REQUESTS_IN_FLIGHT = "second_derivative_in_flight_requests";
    public static final String AVERAGE_LOAD_AVERAGE = "average_load_average";
    public static final String AVERAGE_MEMORY_CONSUMPTION = "average_memory_consumption";
    public static final String GRADIENT_LOAD_AVERAGE = "gradient_load_average";
    public static final String GRADIENT_MEMORY_CONSUMPTION = "gradient_memory_consumption";
    public static final String SECOND_DERIVATIVE_OF_MEMORY_CONSUMPTION = "second_derivative_memory_consumption";
    public static final String SECOND_DERIVATIVE_OF_LOAD_AVERAGE = "second_derivative_load_average";
    //member expiry interval
    public static final String MEMBER_EXPIRY_INTERVAL = "member.expiry.interval";
    //Grouping
    public static final String TERMINATE_NONE = "terminate-none";
    public static final String TERMINATE_ALL = "terminate-all";
    public static final String GROUP = "group";
    public static final String CARTRIDGE = "cartridge";
    public static final int IS_DEFAULT_PORT = 9443;
    public static final String OAUTH_SERVICE_SFX = "services/OAuthAdminService";
    public static final String IDENTITY_APPLICATION_SERVICE_SFX = "services/IdentityApplicationManagementService";
    public static final String TOKEN_ENDPOINT_SFX = "oauth2/token";
    public static final String TERMINATE_DEPENDENTS = "terminate-dependents";
    //scaling decision payload values
    public static final String TIMESTAMP = "timestamp";
    public static final String SCALING_DECISION_ID = "scaling_decision_id";
    public static final String CLUSTER_ID = "cluster_id";
    public static final String MIN_INSTANCE_COUNT = "min_instance_count";
    public static final String MAX_INSTANCE_COUNT = "max_instance_count";
    public static final String RIF_PREDICTED = "rif_predicted";
    public static final String RIF_THRESHOLD = "rif_threshold";
    public static final String RIF_REQUIRED_INSTANCES = "rif_required_instances";
    public static final String MC_PREDICTED = "mc_predicted";
    public static final String MC_THRESHOLD = "mc_threshold";
    public static final String MC_REQUIRED_INSTANCES = "mc_required_instances";
    public static final String LA_PREDICTED = "la_predicted";
    public static final String LA_THRESHOLD = "la_threshold";
    public static final String LA_REQUIRED_INSTANCES = "la_required_instances";
    public static final String REQUIRED_INSTANCE_COUNT = "required_instance_count";
    public static final String ACTIVE_INSTANCE_COUNT = "active_instance_count";
    public static final String ADDITIONAL_INSTANCE_COUNT = "additional_instance_count";
    public static final String SCALING_REASON = "scaling_reason";
    public static final String NETWORK_PARTITION_ID_LIST = "NETWORK_PARTITION_ID_LIST";
}
