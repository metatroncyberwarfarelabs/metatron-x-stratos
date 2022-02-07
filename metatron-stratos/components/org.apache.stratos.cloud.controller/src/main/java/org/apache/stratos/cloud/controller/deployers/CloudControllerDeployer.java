/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements. See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership. The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.apache.stratos.cloud.controller.deployers;

import org.apache.axiom.om.OMElement;
import org.apache.axis2.context.ConfigurationContext;
import org.apache.axis2.deployment.AbstractDeployer;
import org.apache.axis2.deployment.DeploymentException;
import org.apache.axis2.deployment.repository.util.DeploymentFileData;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.stratos.cloud.controller.config.CloudControllerConfig;
import org.apache.stratos.cloud.controller.config.parser.CloudControllerConfigParser;
import org.apache.stratos.cloud.controller.domain.IaasProvider;
import org.apache.stratos.common.util.AxiomXpathParserUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * All the {@link IaasProvider}s will get deployed / undeployed / updated via this class.
 */
public class CloudControllerDeployer extends AbstractDeployer {

    private static final Log log = LogFactory.getLog(CloudControllerDeployer.class);
    private static final String FILE_NAME = "cloud-controller";
    private Map<String, List<IaasProvider>> fileToIaasProviderListMap;

    @Override
    public void init(ConfigurationContext arg0) {
        fileToIaasProviderListMap = new ConcurrentHashMap<String, List<IaasProvider>>();
    }

    @Override
    public void setDirectory(String arg0) {
        // component xml handles this

    }

    @Override
    public void setExtension(String arg0) {
        // component xml handles this
    }

    public void deploy(DeploymentFileData deploymentFileData) throws DeploymentException {

        log.debug("Started to deploy the deployment artifact: " + deploymentFileData.getFile());

        // since cloud-controller.xml resides in repository/conf
        if (deploymentFileData.getName().contains(FILE_NAME)) {

            OMElement docElt = AxiomXpathParserUtil.parse(deploymentFileData.getFile());

            CloudControllerConfigParser.parse(docElt);

            // update map
            fileToIaasProviderListMap.put(deploymentFileData.getAbsolutePath(),
                    new ArrayList<IaasProvider>(
                            CloudControllerConfig.getInstance()
                                    .getIaasProviders()));

            log.info("Successfully deployed the cloud-controller XML file located at " +
                    deploymentFileData.getAbsolutePath());
        }

    }

    public void undeploy(String file) throws DeploymentException {
        if (file.contains(FILE_NAME)) {
            // grab the entry from Map
            if (fileToIaasProviderListMap.containsKey(file)) {
                // remove 'em
                CloudControllerConfig.getInstance().getIaasProviders().removeAll(fileToIaasProviderListMap.get(file));
                log.info("Successfully un-deployed the cloud-controller XML file specified at " +
                        file);
            }

            CloudControllerConfig.getInstance().setDataPubConfig(null);
            CloudControllerConfig.getInstance().setTopologyConfig(null);
        }
    }

}
