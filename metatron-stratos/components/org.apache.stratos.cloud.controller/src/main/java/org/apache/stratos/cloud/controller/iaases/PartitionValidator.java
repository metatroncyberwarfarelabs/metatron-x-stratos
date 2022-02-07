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
package org.apache.stratos.cloud.controller.iaases;

import org.apache.stratos.cloud.controller.domain.IaasProvider;
import org.apache.stratos.cloud.controller.domain.Partition;
import org.apache.stratos.cloud.controller.exception.InvalidPartitionException;

import java.util.Properties;

/**
 * All the IaaSes needs to write a partition validator which implements this interface.
 */
public interface PartitionValidator {

    /**
     * set the IaasProvider reference.
     *
     * @param iaasProvider {@link IaasProvider}
     */
    public abstract void setIaasProvider(IaasProvider iaasProvider);

    /**
     * Validate the given properties for its existent in this partition.
     *
     * @param partition  partition.
     * @param properties set of properties to be validated.
     * @return cloned and modified {@link IaasProvider} which maps to the given partition.
     * @throws InvalidPartitionException if at least one property is evaluated to be invalid.
     */
    public abstract IaasProvider validate(Partition partition, Properties properties) throws InvalidPartitionException;
}
