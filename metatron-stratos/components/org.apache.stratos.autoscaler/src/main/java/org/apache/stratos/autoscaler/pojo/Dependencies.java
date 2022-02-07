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
package org.apache.stratos.autoscaler.pojo;

import java.io.Serializable;

public class Dependencies implements Serializable {

    private static final long serialVersionUID = 4187267350546153680L;

    private String[] startupOrders;

    private String[] scalingDependants;

    private String terminationBehaviour;

    public String getTerminationBehaviour() {
        return terminationBehaviour;
    }

    public void setTerminationBehaviour(String terminationBehaviour) {
        this.terminationBehaviour = terminationBehaviour;
    }

    public String[] getStartupOrders() {
        return startupOrders;
    }

    public void setStartupOrders(String[] startupOrders) {
        this.startupOrders = startupOrders;
    }

    public String[] getScalingDependants() {
        return scalingDependants;
    }

    public void setScalingDependants(String[] scalingDependants) {
        this.scalingDependants = scalingDependants;
    }

}
