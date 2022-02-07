/*
 *
 *  * Licensed to the Apache Software Foundation (ASF) under one
 *  * or more contributor license agreements. See the NOTICE file
 *  * distributed with this work for additional information
 *  * regarding copyright ownership. The ASF licenses this file
 *  * to you under the Apache License, Version 2.0 (the
 *  * "License"); you may not use this file except in compliance
 *  * with the License. You may obtain a copy of the License at
 *  *
 *  * http://www.apache.org/licenses/LICENSE-2.0
 *  *
 *  * Unless required by applicable law or agreed to in writing,
 *  * software distributed under the License is distributed on an
 *  * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 *  * KIND, either express or implied. See the License for the
 *  * specific language governing permissions and limitations
 *  * under the License.
 *
 */
package org.apache.stratos.common.util;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.XMLConfiguration;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.carbon.utils.CarbonUtils;

import java.io.File;

/**
 * This class contains utility methods for read Autoscaler configuration file.
 */
public class ConfUtil {

    private static final String CONFIG_FILE_NAME = "stratos-config";
    private static Log log = LogFactory.getLog(ConfUtil.class);

    private XMLConfiguration config;

    private static ConfUtil instance = null;

    private ConfUtil(String configFilePath) {
        log.info("Loading configuration.....");
        try {

            File confFile;
            if (configFilePath != null && !configFilePath.isEmpty()) {
                confFile = new File(configFilePath);

            } else {
                confFile = new File(CarbonUtils.getCarbonConfigDirPath(), CONFIG_FILE_NAME);
            }

            config = new XMLConfiguration(confFile);
        } catch (ConfigurationException e) {
            log.error("Unable to load autoscaler configuration file", e);
            config = new XMLConfiguration();  // continue with default values
        }
    }

    public static ConfUtil getInstance(String configFilePath) {
        if (instance == null) {
            instance = new ConfUtil(configFilePath);
        }
        return instance;
    }

    public XMLConfiguration getConfiguration() {
        return config;
    }

}
