/*
 * Copyright 2005-2015 WSO2, Inc. (http://wso2.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.stratos.integration.common.rest;

import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;

/**
 * Error response.
 */
@XmlRootElement(name = "errorResponse")
public class ErrorResponse implements Serializable {

    private int errorCode;
    private String errorMessage;

    public ErrorResponse() {
    }

    public ErrorResponse(int errorCode, String errorMessage) {
        this.setErrorCode(errorCode);
        this.setErrorMessage(errorMessage);
    }

    public int getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
}
