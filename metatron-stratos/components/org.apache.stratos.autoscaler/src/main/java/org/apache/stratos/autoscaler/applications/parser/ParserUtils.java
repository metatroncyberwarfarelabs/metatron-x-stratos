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

package org.apache.stratos.autoscaler.applications.parser;

import org.apache.stratos.autoscaler.applications.pojo.ApplicationContext;
import org.apache.stratos.autoscaler.applications.pojo.CartridgeContext;
import org.apache.stratos.autoscaler.applications.pojo.ComponentContext;
import org.apache.stratos.autoscaler.applications.pojo.GroupContext;
import org.apache.stratos.autoscaler.exception.application.ApplicationDefinitionException;
import org.apache.stratos.autoscaler.util.AutoscalerConstants;
import org.apache.stratos.messaging.domain.application.ScalingDependentList;
import org.apache.stratos.messaging.domain.application.StartupOrder;

import java.util.*;

public class ParserUtils {

    public static Set<StartupOrder> convertStartupOrder(String[] startupOrderArr) throws ApplicationDefinitionException {

        Set<StartupOrder> startupOrders = new LinkedHashSet<StartupOrder>();

        if (startupOrderArr == null) {
            return startupOrders;
        }

        for (String commaSeparatedStartupOrder : startupOrderArr) {
            startupOrders.add(getStartupOrder(commaSeparatedStartupOrder));
        }

        return startupOrders;
    }

    private static StartupOrder getStartupOrder(String commaSeparatedStartupOrder) throws ApplicationDefinitionException {

        List<String> startupOrders = new ArrayList<String>();
        for (String startupOrder : Arrays.asList(commaSeparatedStartupOrder.split(","))) {
            startupOrder = startupOrder.trim();
            if (!startupOrder.startsWith("cartridge.") && !startupOrder.startsWith("group.")) {
                throw new ApplicationDefinitionException("Incorrect startup order specified, " +
                        "should start with 'cartridge.' or 'group.'");
            }
            startupOrders.add(startupOrder);
        }
        return new StartupOrder(startupOrders);
    }

    public static Set<StartupOrder> convertStartupOrder(String[] startupOrderArr, GroupContext groupContext)
            throws ApplicationDefinitionException {

        Set<StartupOrder> startupOrders = new LinkedHashSet<StartupOrder>();

        if (startupOrderArr == null) {
            return startupOrders;
        }


        for (String commaSeparatedStartupOrder : startupOrderArr) {
            // convertStartupOrder all Startup Orders to aliases-based
            List<String> components = Arrays.asList(commaSeparatedStartupOrder.split(","));
            for (String component : components) {
                boolean aliasFound = false;
                if (component.startsWith(AutoscalerConstants.GROUP)) {
                    String groupAlias = component.substring(AutoscalerConstants.GROUP.length() + 1);
                    if (groupContext.getGroupContexts() != null) {
                        for (GroupContext context : groupContext.getGroupContexts()) {
                            if (context.getAlias().equals(groupAlias)) {
                                aliasFound = true;
                            }
                        }
                    }

                } else {
                    String cartridgeAlias = component.substring(
                            AutoscalerConstants.CARTRIDGE.length() + 1);
                    if (groupContext.getCartridgeContexts() != null) {
                        for (CartridgeContext context : groupContext.getCartridgeContexts()) {
                            if (context.getSubscribableInfoContext().getAlias().equals(cartridgeAlias)) {
                                aliasFound = true;
                            }
                        }
                    }
                }
                if (!aliasFound) {
                    String msg = "The startup-order defined in the [group] " + groupContext.getName()
                            + " is not correct. [startup-order-alias] " + component +
                            " is not there in the application.";
                    throw new ApplicationDefinitionException(msg);
                }
            }
            StartupOrder startupOrder = new StartupOrder(components);
            startupOrders.add(startupOrder);

        }

        return startupOrders;
    }

    
    public static void validateStartupOrderAlias(String[] startupOrderArr, ApplicationContext applicationContext)
            throws ApplicationDefinitionException {

        for (String commaSeparatedStartupOrder : startupOrderArr) {
            List<String> components = Arrays.asList(commaSeparatedStartupOrder.split(","));
            for (String component : components) {
                boolean aliasFound = false;
                if (component.startsWith(AutoscalerConstants.GROUP)) {
                    String groupAlias = component.substring(AutoscalerConstants.GROUP.length() + 1);
                    if (applicationContext.getComponents().getGroupContexts() != null) {
                        for (GroupContext context : applicationContext.getComponents().getGroupContexts()) {
                            if (context.getAlias().equals(groupAlias)) {
                                aliasFound = true;
                            }
                        }
                    }

                } else {
                    String cartridgeAlias = component.substring(
                            AutoscalerConstants.CARTRIDGE.length() + 1);
                    if (applicationContext.getComponents().getCartridgeContexts() != null) {
                        for (CartridgeContext context : applicationContext.getComponents().getCartridgeContexts()) {
                            if (context.getSubscribableInfoContext().getAlias().equals(cartridgeAlias)) {
                                aliasFound = true;
                            }
                        }
                    }
                }
                if (!aliasFound) {
                    String msg = "The startup-order defined in the [application] " + applicationContext.getApplicationId()
                            + " is not correct. [startup-order-alias] " + component +
                            " is not there in the application.";
                    throw new ApplicationDefinitionException(msg);
                }
            }
        }

    }
    
    
    private static StartupOrder getStartupOrder(List<String> components, GroupContext groupContext)
            throws ApplicationDefinitionException {

        List<String> aliasBasedComponents = new ArrayList<String>();

        for (String component : components) {
            component = component.trim();

            String aliasBasedComponent;
            if (component.startsWith("cartridge.")) {
                String cartridgeType = component.substring(10);
                aliasBasedComponent = getAliasForServiceType(cartridgeType, groupContext);
                if (aliasBasedComponent == null) {
                    throw new ApplicationDefinitionException("Unable convertStartupOrder Startup Order to alias-based; " +
                            "cannot find the matching alias for Service type " + cartridgeType);
                }

                aliasBasedComponent = "cartridge.".concat(aliasBasedComponent);

            } else if (component.startsWith("group.")) {
                String groupName = component.substring(6);
                aliasBasedComponent = getAliasForGroupName(groupName, groupContext);
                if (aliasBasedComponent == null) {
                    throw new ApplicationDefinitionException("Unable convertStartupOrder Startup Order to alias-based; " +
                            "cannot find the matching alias for Group name " + groupName);
                }

                aliasBasedComponent = "group.".concat(aliasBasedComponent);

            } else {
                throw new ApplicationDefinitionException("Incorrect Startup Order specified, " +
                        "should start with 'cartridge.' or 'group.'");
            }
            aliasBasedComponents.add(aliasBasedComponent);
        }

        return new StartupOrder(aliasBasedComponents);
    }


    public static Set<ScalingDependentList> convertScalingDependentList(String[] scalingDependentListArr)
            throws ApplicationDefinitionException {

        Set<ScalingDependentList> scalingDependentLists = new HashSet<ScalingDependentList>();
        if (scalingDependentListArr == null) {
            return scalingDependentLists;
        }
        for (String commaSeparatedScalingDependentList : scalingDependentListArr) {
            scalingDependentLists.add(getScalingDependentList(commaSeparatedScalingDependentList));
        }
        return scalingDependentLists;
    }

    private static ScalingDependentList getScalingDependentList(String commaSeparatedScalingDependentList) throws ApplicationDefinitionException {

        List<String> scalingDependentLists = new ArrayList<String>();
        for (String scalingDependentList : Arrays.asList(commaSeparatedScalingDependentList.split(","))) {
            scalingDependentList = scalingDependentList.trim();
            if (!scalingDependentList.startsWith("cartridge.") && !scalingDependentList.startsWith("group.")) {
                throw new ApplicationDefinitionException("Incorrect scaling dependent List specified, " +
                        "should start with 'cartridge.' or 'group.'");
            }
            scalingDependentLists.add(scalingDependentList);
        }
        return new ScalingDependentList(scalingDependentLists);
    }

    public static Set<ScalingDependentList> convertScalingDependentList(String[] scalingDependentListArr, GroupContext groupContext)
            throws ApplicationDefinitionException {

        Set<ScalingDependentList> scalingDependentLists = new HashSet<ScalingDependentList>();
        if (scalingDependentListArr == null) {
            return scalingDependentLists;
        }
        for (String commaSeparatedScalingDependentList : scalingDependentListArr) {
            // convertScalingDependentList all scaling dependents to aliases-based
            List<String> components = Arrays.asList(commaSeparatedScalingDependentList.split(","));
            ScalingDependentList dependentList = new ScalingDependentList(components);
            scalingDependentLists.add(dependentList);
        }
        return scalingDependentLists;
    }

    private static ScalingDependentList getScalingDependentList(List<String> components, GroupContext groupContext)
            throws ApplicationDefinitionException {

        List<String> aliasBasedComponents = new ArrayList<String>();

        for (String component : components) {
            component = component.trim();

            String aliasBasedComponent;
            if (component.startsWith("cartridge.")) {
                String cartridgeType = component.substring(10);
                aliasBasedComponent = getAliasForServiceType(cartridgeType, groupContext);
                if (aliasBasedComponent == null) {
                    throw new ApplicationDefinitionException("Unable convertScalingDependentList Scaling dependent list to alias-based; " +
                            "cannot find the matching alias for Service type " + cartridgeType);
                }

                aliasBasedComponent = "cartridge.".concat(aliasBasedComponent);

            } else if (component.startsWith("group.")) {
                String groupName = component.substring(6);
                aliasBasedComponent = getAliasForGroupName(groupName, groupContext);
                if (aliasBasedComponent == null) {
                    throw new ApplicationDefinitionException("Unable convertScalingDependentList Scaling dependent list to alias-based; " +
                            "cannot find the matching alias for Group name " + groupName);
                }

                aliasBasedComponent = "group.".concat(aliasBasedComponent);

            } else {
                throw new ApplicationDefinitionException("Incorrect Scaling dependent list specified, " +
                        "should start with 'cartridge.' or 'group.'");
            }
            aliasBasedComponents.add(aliasBasedComponent);
        }

        return new ScalingDependentList(aliasBasedComponents);
    }

    private static String getAliasForGroupName(String groupName, GroupContext groupContext) {

        for (GroupContext groupCtxt : groupContext.getGroupContexts()) {
            if (groupName.equals(groupCtxt.getName())) {
                return groupCtxt.getAlias();
            }
        }

        return null;
    }


    private static String getAliasForServiceType(String serviceType, GroupContext groupContext) {

        for (CartridgeContext subCtxt : groupContext.getCartridgeContexts()) {
            if (serviceType.equals(subCtxt.getType())) {
                return subCtxt.getSubscribableInfoContext().getAlias();
            }
        }

        return null;
    }
}
