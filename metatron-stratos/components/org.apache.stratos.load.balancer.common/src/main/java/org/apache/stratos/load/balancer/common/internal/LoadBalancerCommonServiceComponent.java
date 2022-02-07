/**
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 * <p/>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p/>
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.apache.stratos.load.balancer.common.internal;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.osgi.service.component.ComponentContext;
import org.wso2.carbon.utils.ConfigurationContextService;

/**
 * @scr.component name="org.apache.stratos.load.balancer.common.internal.LoadBalancerCommonServiceComponent" immediate="true"
 * @scr.reference name="config.context.service"
 * interface="org.wso2.carbon.utils.ConfigurationContextService" cardinality="1..1"
 * policy="dynamic" bind="setConfigurationContextService"
 * unbind="unsetConfigurationContextService"
 */
public class LoadBalancerCommonServiceComponent {

    private static final Log log = LogFactory.getLog(LoadBalancerCommonServiceComponent.class);

    protected void activate(ComponentContext context) {
        if (log.isDebugEnabled()) {
            log.debug("Activating LoadBalancerCommonServiceComponent...");
        }
        try {
            log.debug("Load Balancer Common Service bundle activated");
        }
        catch (Exception e) {
            log.error("Could not activate Load Balancer Common Service bundle", e);
        }
    }

    protected void setConfigurationContextService(ConfigurationContextService contextService) {
    }

    protected void unsetConfigurationContextService(ConfigurationContextService contextService) {
    }
}
