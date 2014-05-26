/**
 * Copyright 2014 ArcBees Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */

package com.arcbees.analytics.server;

import java.io.IOException;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestFactory;
import com.google.api.client.http.HttpResponse;
import com.google.api.client.http.HttpStatusCodes;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;

public class MeasureProtocolRequest {
    private static Logger logger = Logger.getLogger(MeasureProtocolRequest.class.getName());

    public static class Builder {
        private final Multimap<String, String> parameterTuples = ArrayListMultimap.create();

        public Builder() {
        }

        public Builder protocolVersion(String protocolVersion) {
            parameterTuples.put(GaParameterConstants.PROTOCOL_VERSION, protocolVersion);

            return this;
        }

        public Builder clientId(String clientId) {
            parameterTuples.put(GaParameterConstants.CLIENT_ID, clientId);

            return this;
        }

        public Builder trackingCode(String trackId) {
            parameterTuples.put(GaParameterConstants.TRACKING_ID, trackId);

            return this;
        }

        public Builder hitType(String hitType) {
            parameterTuples.put(GaParameterConstants.HIT_TYPE, hitType);

            return this;
        }

        public Builder applicationName(String applicationName) {
            parameterTuples.put(GaParameterConstants.APPLICATION_NAME, applicationName);

            return this;
        }

        public Builder applicationVersion(String applicationVersion) {
            parameterTuples.put(GaParameterConstants.APPLICATION_VERSION, applicationVersion);

            return this;
        }

        public Builder eventCategory(String category) {
            parameterTuples.put(GaParameterConstants.EVENT_CATEGORY, category);

            return this;
        }

        public Builder eventAction(String eventAction) {
            parameterTuples.put(GaParameterConstants.EVENT_ACTION, eventAction);

            return this;
        }

        public Builder eventLabel(String eventLabel) {
            parameterTuples.put(GaParameterConstants.EVENT_LABEL, eventLabel);

            return this;
        }

        public Builder eventValue(String eventValue) {
            parameterTuples.put(GaParameterConstants.EVENT_VALUE, eventValue);

            return this;
        }

        public MeasureProtocolRequest build() {
            return new MeasureProtocolRequest(parameterTuples);
        }
    }

    private final Multimap methodParameterTuples;

    MeasureProtocolRequest(Multimap methodParameterTuples) {
        this.methodParameterTuples = methodParameterTuples;
    }

    public boolean executeRequest() {
        HttpTransport httpTransport = new NetHttpTransport();
        HttpRequestFactory requestFactory = httpTransport.createRequestFactory();

        GenericUrl genericUrl = new GenericUrl(GaParameterConstants.POST_URL);
        Iterator tupleIterator = methodParameterTuples.entries().iterator();

        while (tupleIterator.hasNext()) {
            String entryKey = tupleIterator.next().toString();
            List<String> entryValues = (List<String>) methodParameterTuples.get(entryKey);
            genericUrl.put(entryKey, entryValues);
        }

        try {
            HttpRequest request = requestFactory.buildGetRequest(genericUrl);
            HttpResponse response = request.execute();

            return response.getStatusCode() == HttpStatusCodes.STATUS_CODE_OK;
        } catch (IOException e) {
            logger.log(Level.SEVERE, e.getMessage());
        }

        return false;
    }
}
