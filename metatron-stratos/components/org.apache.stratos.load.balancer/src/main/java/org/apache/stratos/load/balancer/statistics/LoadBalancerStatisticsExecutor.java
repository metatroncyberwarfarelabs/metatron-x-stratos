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
package org.apache.stratos.load.balancer.statistics;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.stratos.common.threading.StratosThreadPool;
import org.apache.stratos.load.balancer.util.LoadBalancerConstants;

import java.util.concurrent.ExecutorService;

/**
 * An executor service to asynchronously execute statistics update calls without blocking the
 * mediation flow.
 */
public class LoadBalancerStatisticsExecutor {

    private static final Log log = LogFactory.getLog(LoadBalancerStatisticsExecutor.class);

    private static volatile LoadBalancerStatisticsExecutor instance;

    private ExecutorService executorService;

    private LoadBalancerStatisticsExecutor() {
        executorService = StratosThreadPool.getExecutorService(LoadBalancerConstants.LOAD_BALANCER_THREAD_POOL_ID,
                LoadBalancerConstants.LOAD_BALANCER_DEFAULT_THREAD_POOL_SIZE);
    }

    public static LoadBalancerStatisticsExecutor getInstance() {
        if (instance == null) {
            synchronized (LoadBalancerStatisticsExecutor.class) {
                if (instance == null) {
                    instance = new LoadBalancerStatisticsExecutor();
                }
            }
        }
        return instance;
    }

    public ExecutorService getService() {
        return executorService;
    }
}
