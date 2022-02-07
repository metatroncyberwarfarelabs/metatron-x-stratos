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

package org.apache.stratos.common.services;

import org.apache.axis2.AxisFault;
import org.apache.stratos.common.Component;

/**
 * Component startup synchronizer service interface.
 */
public interface ComponentStartUpSynchronizer {

    boolean isEnabled();

    void setComponentStatus(Component component, boolean active);

    boolean isComponentActive(Component component);

    void waitForComponentActivation(Component owner, Component component);

    void waitForAxisServiceActivation(Component owner, String serviceName) throws AxisFault;

    void addEventListener(ComponentStartUpEventListener eventListener);
}
