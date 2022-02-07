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

package org.apache.stratos.cartridge.agent.data.publisher.log;

public class Constants {

    public static String LOG_PUBLISHER_STREAM_PREFIX = "logs.";
    public static String LOG_PUBLISHER_STREAM_VERSION = "1.0.0";
    public static String TAIL_COMMAND = "tail -n 100 -F ";
    public static String MEMBER_ID = "memberId";
    public static String LOG_EVENT = "logEvent";

    public static String DATE_FORMATTER = "yyyy-MM-dd";
    public static String DATE_TIME_FORMATTER = "yyyy-MM-dd HH:mm:ss,SSS";
    public static String TENANT_ID = "tenantID";
    public static String SERVER_NAME = "serverName";
    public static String APP_NAME = "appName";
    public static String LOG_TIME = "logTime";
    public static String PRIORITY = "priority";
    public static String MESSAGE = "message";
    public static String LOGGER = "logger";
    public static String IP = "ip";
    public static String INSTANCE = "instance";
    public static String STACKTRACE = "stacktrace";
}
