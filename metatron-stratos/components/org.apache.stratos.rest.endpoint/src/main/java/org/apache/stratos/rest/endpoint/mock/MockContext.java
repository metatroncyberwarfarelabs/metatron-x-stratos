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
package org.apache.stratos.rest.endpoint.mock;

import org.apache.stratos.common.beans.ResponseMessageBean;
import org.apache.stratos.common.beans.TenantInfoBean;
import org.apache.stratos.common.beans.UserInfoBean;
import org.apache.stratos.common.beans.cartridge.CartridgeBean;
import org.apache.stratos.common.beans.partition.NetworkPartitionBean;
import org.apache.stratos.common.beans.partition.NetworkPartitionReferenceBean;
import org.apache.stratos.common.beans.partition.PartitionBean;
import org.apache.stratos.common.beans.partition.PartitionReferenceBean;
import org.apache.stratos.common.beans.policy.autoscale.AutoscalePolicyBean;
import org.apache.stratos.common.beans.policy.deployment.DeploymentPolicyBean;
import org.apache.stratos.common.beans.topology.ClusterBean;
import org.apache.stratos.rest.endpoint.exception.RestAPIException;
import org.wso2.carbon.context.CarbonContext;

import javax.ws.rs.core.Response.Status;
import java.util.*;

public class MockContext {
    private static MockContext mockContext = new MockContext(); // singleton

    private Map<Integer, List<String>> tenantIdToAliasesMap = new HashMap<Integer, List<String>>();
    private Map<Integer, List<CartridgeBean>> cartridgeDefinitionBeanList = new HashMap<Integer, List<CartridgeBean>>();
    private Map<Integer, Map<String, CartridgeBean>> availableSingleTenantCartridges = new HashMap<Integer, Map<String, CartridgeBean>>();
    private Map<Integer, Map<String, CartridgeBean>> availableMultiTenantCartridges = new HashMap<Integer, Map<String, CartridgeBean>>();
    private Map<Integer, Map<String, CartridgeBean>> subscribedCartridges = new HashMap<Integer, Map<String, CartridgeBean>>();
    private Map<String, TenantInfoBean> tenantMap = new HashMap<String, TenantInfoBean>();
    private Map<Integer, Map<String, UserInfoBean>> tenantUserMap = new HashMap<Integer, Map<String, UserInfoBean>>();
    private Map<String, Integer> tenantIdMap = new HashMap<String, Integer>();
    private Map<Integer, Map<String, PartitionBean>> partitionMap = new HashMap<Integer, Map<String, PartitionBean>>();
    private Map<Integer, Map<String, AutoscalePolicyBean>> autoscalePolicyMap = new HashMap<Integer, Map<String, AutoscalePolicyBean>>();
    private Map<Integer, Map<String, DeploymentPolicyBean>> deploymentPolicyMap = new HashMap<Integer, Map<String, DeploymentPolicyBean>>();
    private Map<String, ClusterBean> clusterMap = new HashMap<String, ClusterBean>();

    private int tenantIdCount = 1;
    public static final int PUBLIC_DEFINITION = 0;

    private MockContext() {
    } // do not allow to initialize

    public static MockContext getInstance() {
        return mockContext;
    }

    public ResponseMessageBean addCartirdgeDefinition(CartridgeBean cartridgeDefinitionBean) {
        int tenantId = getTenantId();
        List<CartridgeBean> tenantCartridges;

        if (this.cartridgeDefinitionBeanList.containsKey(PUBLIC_DEFINITION)) {
            tenantCartridges = this.cartridgeDefinitionBeanList.get(PUBLIC_DEFINITION);
        } else {
            tenantCartridges = new LinkedList<CartridgeBean>();
            this.cartridgeDefinitionBeanList.put(PUBLIC_DEFINITION, tenantCartridges);
        }

        tenantCartridges.add(cartridgeDefinitionBean);

        CartridgeBean cartridge = new CartridgeBean();
        cartridge.setType(cartridgeDefinitionBean.getType());
        cartridge.setDescription(cartridgeDefinitionBean.getDescription());
        cartridge.setDisplayName(cartridgeDefinitionBean.getDisplayName());
        cartridge.setMultiTenant(cartridgeDefinitionBean.isMultiTenant());
        cartridge.setProvider(cartridgeDefinitionBean.getProvider());
        cartridge.setVersion(cartridgeDefinitionBean.getVersion());

        Map<String, CartridgeBean> cartridges;
        if (cartridge.isMultiTenant()) {
            if (this.availableMultiTenantCartridges.containsKey(tenantId)) {
                cartridges = availableMultiTenantCartridges.get(tenantId);
            } else {
                cartridges = new HashMap<String, CartridgeBean>();
                this.availableMultiTenantCartridges.put(tenantId, cartridges);
            }

            cartridges.put(cartridge.getType(), cartridge);
            System.out.println(cartridges.size());
        } else {
            if (this.availableSingleTenantCartridges.containsKey(tenantId)) {
                cartridges = availableSingleTenantCartridges.get(tenantId);
            } else {
                cartridges = new HashMap<String, CartridgeBean>();
                this.availableSingleTenantCartridges.put(tenantId, cartridges);
            }
            cartridges.put(cartridge.getType(), cartridge);
            System.out.println(cartridges.size());
        }

        ResponseMessageBean stratosApiResponse = new ResponseMessageBean();
        stratosApiResponse.setMessage("Successfully deployed cartridge definition with type ");
        return stratosApiResponse;
    }

    public CartridgeBean[] getAvailableMultiTenantCartridges() throws RestAPIException {
        int tenantId = getTenantId();
        if (!availableMultiTenantCartridges.containsKey(tenantId) && !availableMultiTenantCartridges.containsKey(PUBLIC_DEFINITION)) {
            Collection<CartridgeBean> cartridges = new HashMap<String, CartridgeBean>().values();
            return cartridges.toArray(new CartridgeBean[cartridges.size()]);
        }

        List<CartridgeBean> cartridges = new ArrayList<CartridgeBean>();

        if (availableMultiTenantCartridges.get(tenantId) != null)
            cartridges.addAll(availableMultiTenantCartridges.get(tenantId).values());

        if (availableMultiTenantCartridges.get(PUBLIC_DEFINITION) != null)
            cartridges.addAll(availableMultiTenantCartridges.get(PUBLIC_DEFINITION).values());

        return cartridges.toArray(new CartridgeBean[cartridges.size()]);
    }


    public CartridgeBean[] getAvailableSingleTenantCartridges() throws RestAPIException {
        int tenantId = getTenantId();
        if (!availableSingleTenantCartridges.containsKey(tenantId) && !availableSingleTenantCartridges.containsKey(PUBLIC_DEFINITION)) {
            Collection<CartridgeBean> cartridges = new HashMap<String, CartridgeBean>().values();
            return cartridges.toArray(new CartridgeBean[cartridges.size()]);
        }

        List<CartridgeBean> cartridges = new ArrayList<CartridgeBean>();

        if (availableSingleTenantCartridges.get(tenantId) != null)
            cartridges.addAll(availableSingleTenantCartridges.get(tenantId).values());

        if (availableSingleTenantCartridges.get(PUBLIC_DEFINITION) != null)
            cartridges.addAll(availableSingleTenantCartridges.get(PUBLIC_DEFINITION).values());

        return cartridges.toArray(new CartridgeBean[cartridges.size()]);
    }

    public CartridgeBean[] getAvailableLbCartridges() throws RestAPIException {
        return getAvailableSingleTenantCartridges();
    }

    public CartridgeBean[] getAvailableCartridges() throws RestAPIException {
        return getAvailableSingleTenantCartridges();
    }


    public CartridgeBean[] getSubscribedCartridges() throws RestAPIException {
        int tenantId = getTenantId();
        if (!subscribedCartridges.containsKey(tenantId) && !subscribedCartridges.containsKey(PUBLIC_DEFINITION)) {
            Collection<CartridgeBean> cartridges = new HashMap<String, CartridgeBean>().values();
            return cartridges.toArray(new CartridgeBean[cartridges.size()]);
        }
        List<CartridgeBean> cartridges = new ArrayList<CartridgeBean>();

        if (subscribedCartridges.get(tenantId) != null)
            cartridges.addAll(subscribedCartridges.get(tenantId).values());

        if (subscribedCartridges.get(PUBLIC_DEFINITION) != null)
            cartridges.addAll(subscribedCartridges.get(PUBLIC_DEFINITION).values());

        return cartridges.toArray(new CartridgeBean[cartridges.size()]);
    }

    public ResponseMessageBean unsubscribe(String alias) throws RestAPIException {
        int tenantId = getTenantId();
        if (subscribedCartridges.containsKey(tenantId)) {
            if ((subscribedCartridges.get(tenantId)).containsKey(alias)) {
                (subscribedCartridges.get(tenantId)).remove(alias);
            }
        } else {
            throw new RestAPIException(Status.NO_CONTENT, "Unable to un-subscribe");
        }
        ResponseMessageBean stratosApiResponse = new ResponseMessageBean();
        stratosApiResponse.setMessage("Successfully un-subscribed");
        return stratosApiResponse;
    }

    public CartridgeBean getCartridgeInfo(String alias) throws RestAPIException {
        int tenantId = getTenantId();
        if (!subscribedCartridges.containsKey(tenantId))
            throw new RestAPIException(Status.NO_CONTENT, "No cartridges subscribed for current tenant.");

        if (!(subscribedCartridges.get(tenantId)).containsKey(alias))
            throw new RestAPIException(Status.NO_CONTENT, "Cartridge information is not available.");

        return (subscribedCartridges.get(tenantId)).get(alias);
    }

    public CartridgeBean getAvailableSingleTenantCartridgeInfo(String cartridgeType) throws RestAPIException {
        int tenantId = getTenantId();
        if (!availableSingleTenantCartridges.containsKey(tenantId)) {
            if (!availableSingleTenantCartridges.containsKey(PUBLIC_DEFINITION)) {
                throw new RestAPIException(Status.NO_CONTENT, "No cartridges defined for current tenant");
            }
            if (!(availableSingleTenantCartridges.get(PUBLIC_DEFINITION)).containsKey(cartridgeType))
                throw new RestAPIException(Status.NO_CONTENT, "Cartridge is not available.");

            return (availableSingleTenantCartridges.get(PUBLIC_DEFINITION)).get(cartridgeType);
        }
        if (!(availableSingleTenantCartridges.get(tenantId)).containsKey(cartridgeType))
            throw new RestAPIException(Status.NO_CONTENT, "Cartridge is not available.");

        return (availableSingleTenantCartridges.get(tenantId)).get(cartridgeType);
    }

    public CartridgeBean getAvailableMultiTenantCartridgeInfo(String cartridgeType) throws RestAPIException {
        int tenantId = getTenantId();
        if (!availableMultiTenantCartridges.containsKey(tenantId)) {
            if (!availableMultiTenantCartridges.containsKey(PUBLIC_DEFINITION)) {
                throw new RestAPIException(Status.NO_CONTENT, "No cartridges defined for current tenant");
            }
            if (!(availableMultiTenantCartridges.get(PUBLIC_DEFINITION)).containsKey(cartridgeType))
                throw new RestAPIException(Status.NO_CONTENT, "Cartridge is not available.");

            return (availableMultiTenantCartridges.get(PUBLIC_DEFINITION)).get(cartridgeType);
        }
        if (!(availableMultiTenantCartridges.get(tenantId)).containsKey(cartridgeType))
            throw new RestAPIException(Status.NO_CONTENT, "Cartridge is not available.");

        return (availableMultiTenantCartridges.get(tenantId)).get(cartridgeType);
    }

    public ResponseMessageBean deleteCartridgeDefinition(String cartridgeType) throws RestAPIException {
        if (!deleteFromAvailableSingleTenantCartridgeDefinitions(cartridgeType) && !deleteFromAvailableMultiTenantCartridgeDefinitions(cartridgeType)) {
            throw new RestAPIException(Status.NO_CONTENT, "No cartridges defined for tenant");
        }
        ResponseMessageBean stratosApiResponse = new ResponseMessageBean();
        stratosApiResponse.setMessage("Successfully delete cartridge definition");
        return stratosApiResponse;
    }

    private boolean deleteFromAvailableSingleTenantCartridgeDefinitions(String cartridgeType) {
        int tenantId = getTenantId();
        if (!availableSingleTenantCartridges.containsKey(tenantId)) {
            if (!availableSingleTenantCartridges.containsKey(PUBLIC_DEFINITION)) {
                return false;
            }
            if (!(availableSingleTenantCartridges.get(PUBLIC_DEFINITION)).containsKey(cartridgeType))
                return false;

            (availableSingleTenantCartridges.get(PUBLIC_DEFINITION)).remove(cartridgeType);
            return true;
        }
        if (!(availableSingleTenantCartridges.get(tenantId)).containsKey(cartridgeType))
            return false;

        (availableSingleTenantCartridges.get(tenantId)).remove(cartridgeType);
        return true;
    }

    private boolean deleteFromAvailableMultiTenantCartridgeDefinitions(String cartridgeType) {
        int tenantId = getTenantId();
        if (!availableMultiTenantCartridges.containsKey(tenantId)) {
            if (!availableMultiTenantCartridges.containsKey(PUBLIC_DEFINITION)) {
                return false;
            }
            if (!(availableMultiTenantCartridges.get(PUBLIC_DEFINITION)).containsKey(cartridgeType))
                return false;

            (availableMultiTenantCartridges.get(PUBLIC_DEFINITION)).remove(cartridgeType);
            return true;
        }
        if (!(availableMultiTenantCartridges.get(tenantId)).containsKey(cartridgeType))
            return false;

        (availableMultiTenantCartridges.get(tenantId)).remove(cartridgeType);
        return true;
    }

    public ResponseMessageBean addTenant(TenantInfoBean tenantInfoBean) throws RestAPIException {
        try {
            tenantMap.put(tenantInfoBean.getTenantDomain(), tenantInfoBean);
            tenantInfoBean.setTenantId(tenantIdCount);
            tenantIdMap.put(tenantInfoBean.getAdmin(), tenantIdCount++);
        } catch (Exception e) {
            throw new RestAPIException(Status.INTERNAL_SERVER_ERROR, e.getMessage());
        }
        ResponseMessageBean stratosApiResponse = new ResponseMessageBean();
        stratosApiResponse.setMessage("Successfully added new Tenant");
        return stratosApiResponse;
    }

    public TenantInfoBean getTenant(String tenantDomain) throws RestAPIException {
        if (!tenantMap.containsKey(tenantDomain)) {
            throw new RestAPIException(Status.NO_CONTENT, "Information for tenant: " + tenantDomain + " is not available");
        }
        return tenantMap.get(tenantDomain);
    }

    public ResponseMessageBean deleteTenant(String tenantDomain) {
        if (tenantMap.containsKey(tenantDomain)) {
            TenantInfoBean tenant = tenantMap.get(tenantDomain);
            tenantMap.remove(tenantDomain);
            tenantIdMap.remove(tenant.getTenantId());
        }

        ResponseMessageBean stratosApiResponse = new ResponseMessageBean();
        stratosApiResponse.setMessage("Successfully deleted tenant");
        return stratosApiResponse;
    }

    public TenantInfoBean[] getTenants() throws RestAPIException {
        Collection<TenantInfoBean> tenants = tenantMap.values();
        return tenants.toArray(new TenantInfoBean[tenants.size()]);
    }

    public TenantInfoBean[] retrievePartialSearchTenants(String searchDomain) throws RestAPIException {
        List<TenantInfoBean> searchResult = new LinkedList<TenantInfoBean>();
        for (String tenantDomain : tenantMap.keySet()) {
            if (tenantDomain.contains(searchDomain)) {
                searchResult.add(new TenantInfoBean(tenantMap.get(tenantDomain)));
            }
        }
        return searchResult.toArray(new TenantInfoBean[searchResult.size()]);
    }

    public ResponseMessageBean activateTenant(String tenantDomain) throws RestAPIException {
        if (tenantMap.containsKey(tenantDomain)) {
            tenantMap.get(tenantDomain).setActive(true);
        } else {
            throw new RestAPIException(Status.BAD_REQUEST, "Invalid tenant domain");
        }
        ResponseMessageBean stratosApiResponse = new ResponseMessageBean();
        stratosApiResponse.setMessage("Successfully activated Tenant");
        return stratosApiResponse;
    }

    public ResponseMessageBean deactivateTenant(String tenantDomain) throws RestAPIException {
        if (tenantMap.containsKey(tenantDomain)) {
            tenantMap.get(tenantDomain).setActive(false);
        } else {
            throw new RestAPIException(Status.BAD_REQUEST, "Invalid tenant domain");
        }
        ResponseMessageBean stratosApiResponse = new ResponseMessageBean();
        stratosApiResponse.setMessage("Successfully deactivated Tenant");
        return stratosApiResponse;
    }

    public ResponseMessageBean addPartition(PartitionBean partition) {
        int tenantId = getTenantId();
        Map<String, PartitionBean> partitions;

        if (partitionMap.containsKey(tenantId)) {
            partitions = partitionMap.get(tenantId);
        } else {
            partitions = new HashMap<String, PartitionBean>();
            partitionMap.put(tenantId, partitions);
        }

        partitions.put(partition.getId(), partition);
        ResponseMessageBean stratosApiResponse = new ResponseMessageBean();
        stratosApiResponse.setMessage("Successfully deployed partition");
        return stratosApiResponse;
    }

    public ResponseMessageBean addAutoScalingPolicyDefinition(AutoscalePolicyBean autoscalePolicy) {
        int tenantId = getTenantId();
        Map<String, AutoscalePolicyBean> policies;

        if (autoscalePolicyMap.containsKey(tenantId)) {
            policies = autoscalePolicyMap.get(tenantId);
        } else {
            policies = new HashMap<String, AutoscalePolicyBean>();
            autoscalePolicyMap.put(tenantId, policies);
        }

        policies.put(autoscalePolicy.getId(), autoscalePolicy);
        ResponseMessageBean stratosApiResponse = new ResponseMessageBean();
        stratosApiResponse.setMessage("Successfully deployed auto scaling policy definition");
        return stratosApiResponse;
    }

    public ResponseMessageBean addDeploymentPolicyDefinition(String applicationId, DeploymentPolicyBean deploymentPolicy) {
        int tenantId = getTenantId();
        Map<String, DeploymentPolicyBean> policies;


        if (deploymentPolicyMap.containsKey(tenantId)) {
            policies = deploymentPolicyMap.get(tenantId);
        } else {
            policies = new HashMap<String, DeploymentPolicyBean>();
            deploymentPolicyMap.put(tenantId, policies);
        }


        policies.put(applicationId + UUID.randomUUID().getLeastSignificantBits(), deploymentPolicy);
        ResponseMessageBean stratosApiResponse = new ResponseMessageBean();
        stratosApiResponse.setMessage("Successfully deployed deployment policy definition");
        return stratosApiResponse;
    }

    public PartitionBean[] getPartitions() throws RestAPIException {
        int tenantId = getTenantId();
        if (!partitionMap.containsKey(tenantId) && !partitionMap.containsKey(PUBLIC_DEFINITION)) {
            Collection<PartitionBean> partitions = new HashMap<String, PartitionBean>().values();
            return partitions.toArray(new PartitionBean[partitions.size()]);
        }

        List<PartitionBean> partitions = new ArrayList<PartitionBean>();

        if (partitionMap.get(tenantId) != null)
            partitions.addAll(partitionMap.get(tenantId).values());

        if (partitionMap.get(PUBLIC_DEFINITION) != null)
            partitions.addAll(partitionMap.get(PUBLIC_DEFINITION).values());

        return partitions.toArray(new PartitionBean[partitions.size()]);
    }

    public PartitionBean getPartition(String partitionId) throws RestAPIException {
        int tenantId = getTenantId();
        if (!partitionMap.containsKey(tenantId)) {
            if (!partitionMap.containsKey(PUBLIC_DEFINITION)) {
                throw new RestAPIException(Status.NO_CONTENT, "No partitions have been defined for the tenant");
            }
            if (!(partitionMap.get(PUBLIC_DEFINITION)).containsKey(partitionId)) {
                throw new RestAPIException("There is no partition with the id: " + partitionId);
            }
            return (partitionMap.get(PUBLIC_DEFINITION)).get(partitionId);
        } else {
            if (!(partitionMap.get(tenantId)).containsKey(partitionId)) {
                throw new RestAPIException("There is no partition with the id: " + partitionId);
            }
            return (partitionMap.get(tenantId)).get(partitionId);
        }
    }

    public PartitionBean[] getPartitionsOfPolicy(String deploymentPolicyId) throws RestAPIException {
        int tenantId = getTenantId();
        if (!deploymentPolicyMap.containsKey(tenantId)) {
            if (!deploymentPolicyMap.containsKey(PUBLIC_DEFINITION)) {
                throw new RestAPIException(Status.NO_CONTENT, "No deployment policies have been defined for tenant");
            } else {
                if (!(deploymentPolicyMap.get(PUBLIC_DEFINITION)).containsKey(deploymentPolicyId)) {
                    throw new RestAPIException(Status.NO_CONTENT, "There is no deployment policy with id: " + deploymentPolicyId);
                }
                List<PartitionReferenceBean> partitions = (deploymentPolicyMap.get(PUBLIC_DEFINITION)).get(deploymentPolicyId).getNetworkPartitions().get(0).getPartitions();
                return partitions.toArray(new PartitionBean[partitions.size()]);
            }
        }

        if (!(deploymentPolicyMap.get(tenantId)).containsKey(deploymentPolicyId)) {
            throw new RestAPIException(Status.NO_CONTENT, "There is no deployment policy with id: " + deploymentPolicyId);
        }
        //FIXME to parse thr all the NW partitions
        List<PartitionReferenceBean> partitions = (deploymentPolicyMap.get(tenantId)).
                get(deploymentPolicyId).getNetworkPartitions().get(0).getPartitions();
        return partitions.toArray(new PartitionBean[partitions.size()]);
    }

    public NetworkPartitionBean[] getPartitionGroups(String deploymentPolicyId) throws RestAPIException {
        int tenantId = getTenantId();
        if (!deploymentPolicyMap.containsKey(tenantId)) {
            if (!deploymentPolicyMap.containsKey(PUBLIC_DEFINITION)) {
                throw new RestAPIException(Status.NO_CONTENT, "No deployment policies have been defined for tenant");
            } else {
                if (!(deploymentPolicyMap.get(PUBLIC_DEFINITION)).containsKey(deploymentPolicyId)) {
                    throw new RestAPIException(Status.NO_CONTENT, "There is no deployment policy with id: " + deploymentPolicyId);
                }
                List<NetworkPartitionReferenceBean> networkPartitionsList = (deploymentPolicyMap.get(PUBLIC_DEFINITION)).get(deploymentPolicyId).getNetworkPartitions();
                return networkPartitionsList.toArray(new NetworkPartitionBean[networkPartitionsList.size()]);
            }
        }

        if (!(deploymentPolicyMap.get(tenantId)).containsKey(deploymentPolicyId)) {
            throw new RestAPIException(Status.NO_CONTENT, "There is no deployment policy with id: " + deploymentPolicyId);
        }
        List<NetworkPartitionReferenceBean> networkPartitionsList = (deploymentPolicyMap.get(tenantId)).get(deploymentPolicyId).getNetworkPartitions();
        return networkPartitionsList.toArray(new NetworkPartitionBean[networkPartitionsList.size()]);
    }

    public AutoscalePolicyBean[] getAutoscalePolicies() throws RestAPIException {
        int tenantId = getTenantId();
        if (!autoscalePolicyMap.containsKey(tenantId) && !autoscalePolicyMap.containsKey(PUBLIC_DEFINITION)) {
            Collection<AutoscalePolicyBean> autoscalePolicies = new HashMap<String, AutoscalePolicyBean>().values();
            return autoscalePolicies.toArray(new AutoscalePolicyBean[autoscalePolicies.size()]);
        }

        List<AutoscalePolicyBean> autoscalePolicies = new ArrayList<AutoscalePolicyBean>();

        if (autoscalePolicyMap.get(tenantId) != null)
            autoscalePolicies.addAll(autoscalePolicyMap.get(tenantId).values());

        if (autoscalePolicyMap.get(PUBLIC_DEFINITION) != null)
            autoscalePolicies.addAll(autoscalePolicyMap.get(PUBLIC_DEFINITION).values());

        return autoscalePolicies.toArray(new AutoscalePolicyBean[autoscalePolicies.size()]);
    }

    public AutoscalePolicyBean getAutoscalePolicies(String autoscalePolicyId) throws RestAPIException {
        int tenantId = getTenantId();
        if (!autoscalePolicyMap.containsKey(tenantId)) {
            if (!autoscalePolicyMap.containsKey(PUBLIC_DEFINITION)) {
                throw new RestAPIException(Status.NO_CONTENT, "No autoscaling policies have been defined for tenant");
            }
            if (!(autoscalePolicyMap.get(PUBLIC_DEFINITION)).containsKey(autoscalePolicyId)) {
                throw new RestAPIException("There is no auto scale policy with id: " + autoscalePolicyId);
            }
            return (autoscalePolicyMap.get(PUBLIC_DEFINITION)).get(autoscalePolicyId);
        } else {
            if (!(autoscalePolicyMap.get(tenantId)).containsKey(autoscalePolicyId)) {
                throw new RestAPIException("There is no auto scale policy with id: " + autoscalePolicyId);
            }
            return (autoscalePolicyMap.get(tenantId)).get(autoscalePolicyId);
        }
    }

    public DeploymentPolicyBean[] getDeploymentPolicies() throws RestAPIException {
        int tenantId = getTenantId();
        if (!deploymentPolicyMap.containsKey(tenantId) && !deploymentPolicyMap.containsKey(PUBLIC_DEFINITION)) {
            Collection<DeploymentPolicyBean> depPolicies = new HashMap<String, DeploymentPolicyBean>().values();
            return depPolicies.toArray(new DeploymentPolicyBean[depPolicies.size()]);
        }

        List<DeploymentPolicyBean> depPolicies = new ArrayList<DeploymentPolicyBean>();

        if (deploymentPolicyMap.get(tenantId) != null)
            depPolicies.addAll(deploymentPolicyMap.get(tenantId).values());

        if (deploymentPolicyMap.get(PUBLIC_DEFINITION) != null)
            depPolicies.addAll(deploymentPolicyMap.get(PUBLIC_DEFINITION).values());

        return depPolicies.toArray(new DeploymentPolicyBean[depPolicies.size()]);
    }

    public DeploymentPolicyBean getDeploymentPolicies(String deploymentPolicyId) throws RestAPIException {
        int tenantId = getTenantId();
        if (!deploymentPolicyMap.containsKey(tenantId)) {
            if (!deploymentPolicyMap.containsKey(PUBLIC_DEFINITION)) {
                throw new RestAPIException("No deployment policies have been defined for tenant");
            } else {
                if (!(deploymentPolicyMap.get(PUBLIC_DEFINITION)).containsKey(deploymentPolicyId)) {
                    throw new RestAPIException("There is no deployment policy with id: " + deploymentPolicyId);
                }
                return (deploymentPolicyMap.get(PUBLIC_DEFINITION)).get(deploymentPolicyId);
            }
        } else {
            if (!(deploymentPolicyMap.get(tenantId)).containsKey(deploymentPolicyId)) {
                throw new RestAPIException("There is no deployment policy with id: " + deploymentPolicyId);
            }
            return (deploymentPolicyMap.get(tenantId)).get(deploymentPolicyId);
        }
    }

    public ResponseMessageBean deployService(Object serviceDefinitionBean) {
//    	int tenantId = getTenantId();
//    	Map<String,ServiceDefinitionBean> serviceDefinitions;
//
//    	if(!serviceDefinitionBean.getIsPublic()){
//        	if(!serviceDefinitionMap.containsKey(tenantId)){
//        		serviceDefinitions = new HashMap<String,ServiceDefinitionBean>();
//        		serviceDefinitionMap.put(tenantId, serviceDefinitions);
//        	}
//        	else{
//        		serviceDefinitions = serviceDefinitionMap.get(tenantId);
//        	}
//    	}
//    	else{
//    		if(!serviceDefinitionMap.containsKey(PUBLIC_DEFINITION)){
//        		serviceDefinitions = new HashMap<String,ServiceDefinitionBean>();
//        		serviceDefinitionMap.put(PUBLIC_DEFINITION, serviceDefinitions);
//        	}
//        	else{
//        		serviceDefinitions = serviceDefinitionMap.get(PUBLIC_DEFINITION);
//        	}
//    	}
//
//    	serviceDefinitions.put(serviceDefinitionBean.getCartridgeType(),serviceDefinitionBean);
        ResponseMessageBean stratosApiResponse = new ResponseMessageBean();
        stratosApiResponse.setMessage("Successfully deployed service");
        return stratosApiResponse;

    }

    public PartitionBean[] getPartitions(String deploymentPolicyId, String partitionGroupId) throws RestAPIException {
        int tenantId = getTenantId();
        DeploymentPolicyBean deploymentPolicy;

        if (!deploymentPolicyMap.containsKey(tenantId)) {
            if (!deploymentPolicyMap.containsKey(PUBLIC_DEFINITION)) {
                throw new RestAPIException(Status.NO_CONTENT, "No deployment policies have been defined for tenant");
            } else {
                if (!(deploymentPolicyMap.get(PUBLIC_DEFINITION)).containsKey(deploymentPolicyId)) {
                    throw new RestAPIException(Status.NO_CONTENT, "There is no deployment policy with id: " + deploymentPolicyId);
                } else {
                    deploymentPolicy = (deploymentPolicyMap.get(PUBLIC_DEFINITION)).get(deploymentPolicyId);
                }
            }
        } else {
            if (!(deploymentPolicyMap.get(tenantId)).containsKey(deploymentPolicyId)) {
                throw new RestAPIException(Status.NO_CONTENT, "There is no deployment policy with id: " + deploymentPolicyId);
            } else {
                deploymentPolicy = (deploymentPolicyMap.get(tenantId)).get(deploymentPolicyId);
            }
        }

        PartitionBean[] partitionsArray = null;
        for (NetworkPartitionReferenceBean networkPartition : deploymentPolicy.getNetworkPartitions()) {
            if (networkPartition.getId().equals(partitionGroupId)) {
                List<PartitionReferenceBean> partitions = networkPartition.getPartitions();
                partitionsArray = partitions.toArray(new PartitionBean[partitions.size()]);
            }
        }
        if (partitionsArray == null) {
            throw new RestAPIException(Status.NO_CONTENT, "Partition not found");
        }
        return partitionsArray;
    }

    public ClusterBean[] getClusters() throws RestAPIException {
        Collection<ClusterBean> clusters = clusterMap.values();
        return clusters.toArray(new ClusterBean[clusters.size()]);
    }

    public DeploymentPolicyBean[] getDeploymentPoliciesForCartridgeType(String cartridgeType) throws RestAPIException {
        int tenantId = getTenantId();
        if (!deploymentPolicyMap.containsKey(tenantId)) {
            if (!deploymentPolicyMap.containsKey(PUBLIC_DEFINITION)) {
                Collection<DeploymentPolicyBean> depPolicies = new HashMap<String, DeploymentPolicyBean>().values();
                return depPolicies.toArray(new DeploymentPolicyBean[depPolicies.size()]);
            } else {
                Collection<DeploymentPolicyBean> depPolicies = (deploymentPolicyMap.get(PUBLIC_DEFINITION)).values();
                return depPolicies.toArray(new DeploymentPolicyBean[depPolicies.size()]);
            }
        } else {
            Collection<DeploymentPolicyBean> depPolicies = (deploymentPolicyMap.get(tenantId)).values();
            return depPolicies.toArray(new DeploymentPolicyBean[depPolicies.size()]);
        }
    }

    public void addUser(UserInfoBean user) {
        int tenantId = getTenantId();
        Map<String, UserInfoBean> users;

        if (tenantUserMap.containsKey(tenantId)) {
            users = tenantUserMap.get(tenantId);
        } else {
            users = new HashMap<String, UserInfoBean>();
            tenantUserMap.put(tenantId, users);
        }

        users.put(user.getUserName(), user);
    }

    public void deleteUser(String userName) {
        int tenantId = getTenantId();
        Map<String, UserInfoBean> users;

        if (!tenantUserMap.containsKey(tenantId)) {
            return;
        }

        users = tenantUserMap.get(tenantId);
        users.remove(userName);
    }

    public void updateUser(UserInfoBean user) {
        int tenantId = getTenantId();
        Map<String, UserInfoBean> users;

        if (!tenantUserMap.containsKey(tenantId)) {
            return;
        }

        users = tenantUserMap.get(tenantId);
        if (users.containsKey(user.getUserName())) {
            users.put(user.getUserName(), user);
        }
    }

    public List<UserInfoBean> getAllUsers() {
        int tenantId = getTenantId();
        List<UserInfoBean> userList = new ArrayList<UserInfoBean>();

        if (tenantUserMap.containsKey(tenantId)) {
            userList.addAll(tenantUserMap.get(tenantId).values());
        }
        return userList;
    }

    private int getTenantId() {
        String userName = CarbonContext.getThreadLocalCarbonContext().getUsername();
        if (tenantIdMap.containsKey(userName)) {
            return tenantIdMap.get(userName);
        } else {
            return -1;
        }
    }
}
