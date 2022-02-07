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

package org.apache.stratos.autoscaler.applications.payload;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.stratos.autoscaler.exception.application.ApplicationDefinitionException;


public class PayloadFactory {

    public static final String DATA_IDENTIFIER = "data";
    private static final String LB_IDENTIFIER = "lb";
    private static Log log = LogFactory.getLog(PayloadFactory.class);

    /**
     * Creates and returns a PayloadData instance
     *
     * @param cartridgeProvider Cartridge provider
     * @param cartridgeType     Cartridge type
     * @param basicPayloadData  BasicPayloadData instance
     * @return Payload subscription
     */
    public static PayloadData getPayloadDataInstance(String cartridgeProvider, String cartridgeType,
                                                     BasicPayloadData basicPayloadData)
            throws ApplicationDefinitionException {

        PayloadData payloadData = null;

        //TODO: fix after adding the property Category to Cartridge Definition
        if (cartridgeProvider.equals(DATA_IDENTIFIER)) {
            payloadData = new DataCartridgePayloadData(basicPayloadData);
        } else if (cartridgeProvider.equals(LB_IDENTIFIER)) {
            payloadData = new LoadBalancerCartridgePayloadData(basicPayloadData);
        } else {
            payloadData = new FrameworkCartridgePayloadData(basicPayloadData);
        }

        if (payloadData == null) {
            throw new ApplicationDefinitionException("Unable to find matching payload for cartridge type " + cartridgeType +
                    ", provider " + cartridgeProvider);
        }

        return payloadData;
    }
}
