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
package org.apache.stratos.common.threading;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.Map;
import java.util.concurrent.*;

/**
 * Utility class for Stratos thread pool
 */
public class StratosThreadPool {

    private static final Log log = LogFactory.getLog(StratosThreadPool.class);

    private static volatile Map<String, ExecutorService> executorServiceMap = new ConcurrentHashMap<>();
    private static volatile Map<String, ScheduledExecutorService> scheduledServiceMap = new ConcurrentHashMap<>();
    private static Object executorServiceMapLock = new Object();
    private static Object scheduledServiceMapLock = new Object();

    /**
     * Return the executor service based on the identifier and thread pool size
     *
     * @param identifier     Thread pool identifier name
     * @param threadPoolSize Thread pool size
     * @return ExecutorService
     */
    public static ExecutorService getExecutorService(String identifier, int threadPoolSize) {
        ExecutorService executorService = executorServiceMap.get(identifier);
        if (executorService == null) {
            synchronized (executorServiceMapLock) {
                if (executorService == null) {
                    executorService = Executors.newFixedThreadPool(threadPoolSize);
                    executorServiceMap.put(identifier, executorService);
                    log.info(String.format("Thread pool created: [type] Executor Service [id] %s [size] %d", identifier, threadPoolSize));
                }
            }
        }
        return executorService;
    }

    /**
     * Returns a scheduled executor for given thread pool size.
     *
     * @param identifier     Thread pool identifier name
     * @param threadPoolSize Thread pool size
     * @return
     */
    public static ScheduledExecutorService getScheduledExecutorService(String identifier, int threadPoolSize) {
        ScheduledExecutorService scheduledExecutorService = scheduledServiceMap.get(identifier);
        if (scheduledExecutorService == null) {
            synchronized (scheduledServiceMapLock) {
                if (scheduledExecutorService == null) {
                    scheduledExecutorService = Executors.newScheduledThreadPool(threadPoolSize);
                    scheduledServiceMap.put(identifier, scheduledExecutorService);
                    log.info(String.format("Thread pool created: [type] Scheduled Executor Service [id] %s [size] %d",
                            identifier, threadPoolSize));
                }
            }

        }
        return scheduledExecutorService;
    }

    public static void shutdown (String identifier) {

        ExecutorService executorService = executorServiceMap.get(identifier);
        if (executorService == null) {
            log.warn("No executor service found for id " + identifier + ", unable to shut down");
            return;
        }

        // try to shut down gracefully
        executorService.shutdown();
        // wait 10 secs till terminated
        try {
            if (!executorService.awaitTermination(10, TimeUnit.SECONDS)) {
                log.info("Thread Pool [id] " + identifier + " did not finish all tasks before " +
                        "timeout, forcefully shutting down");
                executorService.shutdownNow();
            }
        } catch (InterruptedException e) {
            // interrupted, shutdown now
            executorService.shutdownNow();
        }

        // remove from the map
        executorServiceMap.remove(identifier);

        log.info("Successfully shutdown thread pool associated with id: " + identifier);
    }
}
