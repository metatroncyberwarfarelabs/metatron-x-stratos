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

package org.apache.stratos.cloud.controller.iaases.docker;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.stratos.cloud.controller.domain.IaasProvider;
import org.apache.stratos.cloud.controller.domain.Partition;
import org.apache.stratos.cloud.controller.exception.InvalidPartitionException;
import org.apache.stratos.cloud.controller.iaases.Iaas;
import org.apache.stratos.cloud.controller.iaases.PartitionValidator;

import java.util.Properties;

/**
 * Docker partition validator definition.
 */
public class DockerPartitionValidator implements PartitionValidator {

    private static final Log log = LogFactory.getLog(DockerPartitionValidator.class);

    private IaasProvider iaasProvider;

    @Override
    public void setIaasProvider(IaasProvider iaasProvider) {
        this.iaasProvider = iaasProvider;
    }

    @Override
    public IaasProvider validate(Partition partition, Properties properties) throws InvalidPartitionException {
        try {
            // in Docker case currently we only update the custom properties passed via Partitions, and
            // no validation done as of now.
            IaasProvider updatedIaasProvider = new IaasProvider(iaasProvider);
            updateOtherProperties(updatedIaasProvider, properties);
            Iaas updatedIaas = iaasProvider.buildIaas();
            updatedIaas.setIaasProvider(updatedIaasProvider);
            return updatedIaasProvider;
        } catch (Exception e) {
            String msg = String.format("Invalid partition detected [partition-id] %s", partition.getId());
            log.error(msg, e);
            throw new InvalidPartitionException(msg, e);
        }
    }

    private void updateOtherProperties(IaasProvider updatedIaasProvider, Properties properties) {
        for (Object property : properties.keySet()) {
            if (property instanceof String) {
                String key = (String) property;
                updatedIaasProvider.setProperty(key, properties.getProperty(key));
                if (log.isDebugEnabled()) {
                    log.debug("Added property " + key + " to the IaasProvider.");
                }
            }
        }
    }
}
