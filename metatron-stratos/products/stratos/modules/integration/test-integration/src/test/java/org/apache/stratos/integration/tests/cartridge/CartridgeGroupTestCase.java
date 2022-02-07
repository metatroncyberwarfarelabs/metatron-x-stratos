/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package org.apache.stratos.integration.tests.cartridge;

import com.google.gson.reflect.TypeToken;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.stratos.common.beans.cartridge.CartridgeGroupBean;
import org.apache.stratos.integration.common.RestConstants;
import org.apache.stratos.integration.tests.StratosIntegrationTest;
import org.testng.annotations.Test;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;
import static org.testng.AssertJUnit.*;

/**
 * Test to handle Cartridge group CRUD operations
 */
@Test(groups = { "cartridge" })
public class CartridgeGroupTestCase extends StratosIntegrationTest {
    private static final Log log = LogFactory.getLog(CartridgeGroupTestCase.class);
    private static final String RESOURCES_PATH = "/cartridge-group-test";
    private long startTime;

    @Test(timeOut = DEFAULT_TEST_TIMEOUT,
          priority = 1)
    public void testCartridgeGroup() throws Exception {
        log.info("Running CartridgeGroupTestCase.testCartridgeGroup test method...");
        startTime = System.currentTimeMillis();

        boolean addedC1 = restClient.addEntity(RESOURCES_PATH + RestConstants.CARTRIDGES_PATH +
                "/" + "c4-cartridge-group-test.json", RestConstants.CARTRIDGES, RestConstants.CARTRIDGES_NAME);
        assertTrue(String.format("Cartridge did not added: [cartridge-name] %s", "c4-cartridge-group-test"), addedC1);

        boolean addedC2 = restClient.addEntity(RESOURCES_PATH + RestConstants.CARTRIDGES_PATH +
                "/" + "c5-cartridge-group-test.json", RestConstants.CARTRIDGES, RestConstants.CARTRIDGES_NAME);
        assertTrue(String.format("Cartridge did not added: [cartridge-name] %s", "c5-cartridge-group-test"), addedC2);

        boolean addedC3 = restClient.addEntity(RESOURCES_PATH + RestConstants.CARTRIDGES_PATH +
                "/" + "c6-cartridge-group-test.json", RestConstants.CARTRIDGES, RestConstants.CARTRIDGES_NAME);
        assertTrue(String.format("Cartridge did not added: [cartridge-name] %s", "c6-cartridge-group-test"), addedC3);

        boolean added = restClient.addEntity(RESOURCES_PATH + RestConstants.CARTRIDGE_GROUPS_PATH +
                        "/" + "g4-g5-g6-cartridge-group-test.json", RestConstants.CARTRIDGE_GROUPS,
                RestConstants.CARTRIDGE_GROUPS_NAME);
        assertTrue(String.format("Cartridge Group did not added: [cartridge-group-name] %s",
                "g4-g5-g6-cartridge-group-test"), added);

        CartridgeGroupBean bean = (CartridgeGroupBean) restClient.
                getEntity(RestConstants.CARTRIDGE_GROUPS, "G4-cartridge-group-test", CartridgeGroupBean.class,
                        RestConstants.CARTRIDGE_GROUPS_NAME);
        assertEquals(bean.getName(), "G4-cartridge-group-test",
                String.format("Cartridge Group name did not match: [cartridge-group-name] %s",
                        "g4-g5-g6-cartridge-group-test.json"));

        boolean updated = restClient.updateEntity(RESOURCES_PATH + RestConstants.CARTRIDGE_GROUPS_PATH +
                        "/" + "g4-g5-g6-cartridge-group-test-v1.json", RestConstants.CARTRIDGE_GROUPS,
                RestConstants.CARTRIDGE_GROUPS_NAME);
        assertTrue(String.format("Cartridge Group did not updated: [cartridge-group-name] %s",
                "g4-g5-g6-cartridge-group-test"), updated);

        CartridgeGroupBean updatedBean = (CartridgeGroupBean) restClient.
                getEntity(RestConstants.CARTRIDGE_GROUPS, "G4-cartridge-group-test", CartridgeGroupBean.class,
                        RestConstants.CARTRIDGE_GROUPS_NAME);
        assertEquals(updatedBean.getName(), "G4-cartridge-group-test",
                String.format("Updated Cartridge Group didn't match: [cartridge-group-name] %s",
                        "g4-g5-g6-cartridge-group-test"));

        boolean removedC1 = restClient
                .removeEntity(RestConstants.CARTRIDGES, "c4-cartridge-group-test", RestConstants.CARTRIDGE_GROUPS_NAME);
        assertFalse(
                String.format("Cartridge can be removed while it is used in " + "cartridge group: [cartridge-name] %s",
                        "c4-cartridge-group-test"), removedC1);

        boolean removedC2 = restClient
                .removeEntity(RestConstants.CARTRIDGES, "c5-cartridge-group-test", RestConstants.CARTRIDGE_GROUPS_NAME);
        assertFalse(
                String.format("Cartridge can be removed while it is used in " + "cartridge group: [cartridge-name] %s",
                        "c5-cartridge-group-test"), removedC2);

        boolean removedC3 = restClient
                .removeEntity(RestConstants.CARTRIDGES, "c6-cartridge-group-test", RestConstants.CARTRIDGE_GROUPS_NAME);
        assertFalse(
                String.format("Cartridge can be removed while it is used in " + "cartridge group: [cartridge-name] %s",
                        "c6-cartridge-group-test"), removedC3);

        boolean removed = restClient.removeEntity(RestConstants.CARTRIDGE_GROUPS, "G4-cartridge-group-test",
                RestConstants.CARTRIDGE_GROUPS_NAME);
        assertTrue(String.format("Cartridge Group did not removed: [cartridge-group-name] %s",
                "g4-g5-g6-cartridge-group-test"), removed);

        CartridgeGroupBean beanRemoved = (CartridgeGroupBean) restClient.
                getEntity(RestConstants.CARTRIDGE_GROUPS, "G4-cartridge-group-test", CartridgeGroupBean.class,
                        RestConstants.CARTRIDGE_GROUPS_NAME);
        assertEquals(beanRemoved, null,
                String.format("Cartridge Group did not removed completely: [cartridge-group-name] %s",
                        "g4-g5-g6-cartridge-group-test"));

        removedC1 = restClient
                .removeEntity(RestConstants.CARTRIDGES, "c4-cartridge-group-test", RestConstants.CARTRIDGE_GROUPS_NAME);
        assertTrue(String.format("Cartridge can not be removed : [cartridge-name] %s", "c4-cartridge-group-test"),
                removedC1);

        removedC2 = restClient
                .removeEntity(RestConstants.CARTRIDGES, "c5-cartridge-group-test", RestConstants.CARTRIDGE_GROUPS_NAME);
        assertTrue(String.format("Cartridge can not be removed : [cartridge-name] %s", "c5-cartridge-group-test"),
                removedC2);

        removedC3 = restClient
                .removeEntity(RestConstants.CARTRIDGES, "c6-cartridge-group-test", RestConstants.CARTRIDGE_GROUPS_NAME);
        assertTrue(String.format("Cartridge can not be removed : [cartridge-name] %s", "c6-cartridge-group-test"),
                removedC3);
    }

    @Test(timeOut = DEFAULT_TEST_TIMEOUT,
          priority = 2)
    public void testCartridgeGroupList() throws Exception {
        log.info("Running CartridgeGroupTestCase.testCartridgeGroupList test method...");
        boolean addedC1 = restClient.addEntity(RESOURCES_PATH + RestConstants.CARTRIDGES_PATH +
                "/" + "c4-cartridge-group-test.json", RestConstants.CARTRIDGES, RestConstants.CARTRIDGES_NAME);
        assertTrue(String.format("Cartridge did not added: [cartridge-name] %s", "c4-cartridge-group-test"), addedC1);

        boolean addedC2 = restClient.addEntity(RESOURCES_PATH + RestConstants.CARTRIDGES_PATH +
                "/" + "c5-cartridge-group-test.json", RestConstants.CARTRIDGES, RestConstants.CARTRIDGES_NAME);
        assertTrue(String.format("Cartridge did not added: [cartridge-name] %s", "c5-cartridge-group-test"), addedC2);

        boolean addedC3 = restClient.addEntity(RESOURCES_PATH + RestConstants.CARTRIDGES_PATH +
                "/" + "c6-cartridge-group-test.json", RestConstants.CARTRIDGES, RestConstants.CARTRIDGES_NAME);
        assertTrue(String.format("Cartridge did not added: [cartridge-name] %s", "c6-cartridge-group-test"), addedC3);

        String group1 = "group-1-cartridge-group-test";
        String group2 = "group-2-cartridge-group-test";

        boolean added = restClient.addEntity(RESOURCES_PATH + RestConstants.CARTRIDGE_GROUPS_PATH +
                "/" + group1 + ".json", RestConstants.CARTRIDGE_GROUPS, RestConstants.CARTRIDGE_GROUPS_NAME);
        assertTrue(String.format("Cartridge Group did not added: [cartridge-group-name] %s", group1), added);

        added = restClient.addEntity(RESOURCES_PATH + RestConstants.CARTRIDGE_GROUPS_PATH +
                "/" + group2 + ".json", RestConstants.CARTRIDGE_GROUPS, RestConstants.CARTRIDGE_GROUPS_NAME);
        assertTrue(String.format("Cartridge Group did not added: [cartridge-group-name] %s", group1), added);

        Type listType = new TypeToken<ArrayList<CartridgeGroupBean>>() {
        }.getType();

        List<CartridgeGroupBean> cartridgeGroupList = (List<CartridgeGroupBean>) restClient.
                listEntity(RestConstants.CARTRIDGE_GROUPS, listType, RestConstants.CARTRIDGE_GROUPS_NAME);
        assertTrue(cartridgeGroupList.size() >= 2);

        CartridgeGroupBean bean1 = null;
        for (CartridgeGroupBean cartridgeGroupBean : cartridgeGroupList) {
            if (cartridgeGroupBean.getName().equals(group1)) {
                bean1 = cartridgeGroupBean;
            }
        }
        assertNotNull(bean1);

        CartridgeGroupBean bean2 = null;
        for (CartridgeGroupBean cartridgeGroupBean : cartridgeGroupList) {
            if (cartridgeGroupBean.getName().equals(group2)) {
                bean2 = cartridgeGroupBean;
            }
        }
        assertNotNull(bean2);

        boolean removed = restClient
                .removeEntity(RestConstants.CARTRIDGE_GROUPS, group1, RestConstants.CARTRIDGE_GROUPS_NAME);
        assertTrue(String.format("Cartridge Group did not removed: [cartridge-group-name] %s", group1), removed);

        CartridgeGroupBean beanRemoved = (CartridgeGroupBean) restClient.
                getEntity(RestConstants.CARTRIDGE_GROUPS, group1, CartridgeGroupBean.class,
                        RestConstants.CARTRIDGE_GROUPS_NAME);
        assertNull(String.format("Cartridge Group did not removed completely: " + "[cartridge-group-name] %s", group1),
                beanRemoved);

        boolean removedC1 = restClient
                .removeEntity(RestConstants.CARTRIDGES, "c4-cartridge-group-test", RestConstants.CARTRIDGE_GROUPS_NAME);
        assertFalse(
                String.format("Cartridge can be removed while it is used in " + "cartridge group: [cartridge-name] %s",
                        "c4-cartridge-group-test"), removedC1);

        boolean removedC2 = restClient
                .removeEntity(RestConstants.CARTRIDGES, "c5-cartridge-group-test", RestConstants.CARTRIDGE_GROUPS_NAME);
        assertFalse(
                String.format("Cartridge can be removed while it is used in " + "cartridge group: [cartridge-name] %s",
                        "c5-cartridge-group-test"), removedC2);

        boolean removedC3 = restClient
                .removeEntity(RestConstants.CARTRIDGES, "c6-cartridge-group-test", RestConstants.CARTRIDGE_GROUPS_NAME);
        assertFalse(
                String.format("Cartridge can be removed while it is used in " + "cartridge group: [cartridge-name] %s",
                        "c6-cartridge-group-test"), removedC3);

        removed = restClient.removeEntity(RestConstants.CARTRIDGE_GROUPS, group2, RestConstants.CARTRIDGE_GROUPS_NAME);
        assertTrue(String.format("Cartridge Group did not removed: [cartridge-group-name] %s", group2), removed);

        beanRemoved = (CartridgeGroupBean) restClient.
                getEntity(RestConstants.CARTRIDGE_GROUPS, group2, CartridgeGroupBean.class,
                        RestConstants.CARTRIDGE_GROUPS_NAME);
        assertNull(String.format("Cartridge Group did not removed completely: " + "[cartridge-group-name] %s", group2),
                beanRemoved);

        removedC1 = restClient
                .removeEntity(RestConstants.CARTRIDGES, "c4-cartridge-group-test", RestConstants.CARTRIDGE_GROUPS_NAME);
        assertTrue(String.format("Cartridge can not be removed : [cartridge-name] %s", "c4-cartridge-group-test"),
                removedC1);

        removedC2 = restClient
                .removeEntity(RestConstants.CARTRIDGES, "c5-cartridge-group-test", RestConstants.CARTRIDGE_GROUPS_NAME);
        assertTrue(String.format("Cartridge can not be removed : [cartridge-name] %s", "c5-cartridge-group-test"),
                removedC2);

        removedC3 = restClient
                .removeEntity(RestConstants.CARTRIDGES, "c6-cartridge-group-test", RestConstants.CARTRIDGE_GROUPS_NAME);
        assertTrue(String.format("Cartridge can not be removed : [cartridge-name] %s", "c6-cartridge-group-test"),
                removedC3);
        long duration = System.currentTimeMillis() - startTime;
        log.info(String.format("ApplicationBurstingTestCase completed in [duration] %s ms", duration));
    }
}
