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

package com.arcbees.analytics.shared.options;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import com.arcbees.analytics.shared.HitCallback;

public class NoopOptionsCallback extends OptionsCallback<Map<String, String>> {
    public static final Logger LOGGER = Logger
            .getLogger(NoopOptionsCallback.class.getName());

    private final Map<String, String> options;
    private final boolean logCalls;

    public NoopOptionsCallback(boolean shouldLogCalls) {
        this.logCalls = shouldLogCalls;
        this.options = new HashMap<>();
    }

    @Override
    public void addHitCallback(HitCallback hitCallback) {
    }

    @Override
    public Map<String, String> getOptions() {
        return this.options;
    }

    @Override
    public void onCallback(Map<String, String> options) {
        if (logCalls) {
            LOGGER.info("Would send: " + options);
        }
    }

    @Override
    public void putBoolean(String fieldName, boolean value) {
        this.options.put(fieldName, String.valueOf(value));
    }

    @Override
    public void putNumber(String fieldName, double value) {
        this.options.put(fieldName, String.valueOf(value));
    }

    @Override
    public void putText(String fieldName, String value) {
        this.options.put(fieldName, String.valueOf(value));
    }
}
