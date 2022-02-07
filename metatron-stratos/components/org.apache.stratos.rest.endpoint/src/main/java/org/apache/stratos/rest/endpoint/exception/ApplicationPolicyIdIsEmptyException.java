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

package org.apache.stratos.rest.endpoint.exception;

import javax.ws.rs.core.Response;

public class ApplicationPolicyIdIsEmptyException extends RestAPIException {

    private static final long serialVersionUID = 1L;

    private String message;
    private Response.Status httpStatusCode;

    public ApplicationPolicyIdIsEmptyException() {
        super();
    }

    public ApplicationPolicyIdIsEmptyException(String message, Throwable cause) {
        super(message, cause);
        this.message = message;
    }

    public ApplicationPolicyIdIsEmptyException(Response.Status httpStatusCode, String message, Throwable cause) {
        super(message, cause);
        this.message = message;
        this.httpStatusCode = httpStatusCode;
    }

    public ApplicationPolicyIdIsEmptyException(String message) {
        super(message);
        this.message = message;
    }

    public ApplicationPolicyIdIsEmptyException(Response.Status httpStatusCode, String message) {
        super(message);
        this.message = message;
        this.httpStatusCode = httpStatusCode;
    }

    public ApplicationPolicyIdIsEmptyException(Throwable cause) {
        super(cause);
    }

    public String getMessage() {
        return message;
    }

    public Response.Status getHTTPStatusCode() {
        return httpStatusCode;
    }


}
