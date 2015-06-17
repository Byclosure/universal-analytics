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

package com.arcbees.analytics.client;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import com.arcbees.analytics.shared.AnalyticsImpl;
import com.arcbees.analytics.shared.AnalyticsPlugin;
import com.arcbees.analytics.shared.GaAccount;
import com.arcbees.analytics.shared.HitType;
import com.arcbees.analytics.shared.options.AnalyticsOptions;
import com.arcbees.analytics.shared.options.CreateOptions;
import com.arcbees.analytics.shared.options.GeneralOptions;
import com.arcbees.analytics.shared.options.NoopOptionsCallback;
import com.arcbees.analytics.shared.options.TimingOptions;
import com.google.gwt.core.client.Duration;
import com.google.inject.Inject;

public class ClientNoopAnalytics extends AnalyticsImpl {
    private static final Logger LOGGER = Logger
            .getLogger(ClientNoopAnalytics.class.getName());

    private final Map<String, Double> timingEvents = new HashMap<>();

    private final boolean shouldLogCalls;

    @Inject
    public ClientNoopAnalytics(@GaAccount final String userAccount,
            @AutoCreate final boolean autoCreate) {
        this(userAccount, autoCreate, true);
    }

    @Inject
    public ClientNoopAnalytics(@GaAccount final String userAccount,
            @AutoCreate final boolean autoCreate, boolean shouldLogCalls) {
        super(userAccount);
        this.shouldLogCalls = shouldLogCalls;
        if (autoCreate) {
            create().go();
        }
    }

    static void call(final String... params) {
        final Map<String, String> data = new HashMap<>();
        for (final String p : params) {
            data.put(String.valueOf(data.size()), p);
        }
        LOGGER.fine("Not calling analytics: " + data.toString());
    }

    @Override
    public CreateOptions create(final String userAccount) {
        return new AnalyticsOptions(new NoopOptionsCallback(shouldLogCalls) {
            @Override
            public void onCallback(Map<String, String> options) {
                call("create", userAccount, options.toString());
                setGlobalSettings().forceSsl(true).go();
            }
        }).createOptions();
    }

    @Override
    public void enablePlugin(final AnalyticsPlugin plugin) {
        if (plugin.getJsName() != null) {
            call("require", plugin.getFieldName(), plugin.getJsName());
        } else {
            call("require", plugin.getFieldName());
        }
    }

    @Override
    public TimingOptions endTimingEvent(final String trackerName,
            final String timingCategory, final String timingVariableName) {
        final String key = getTimingKey(timingCategory, timingVariableName);
        if (this.timingEvents.containsKey(key)) {
            return sendTiming(trackerName, timingCategory, timingVariableName,
                    (int) (Duration.currentTimeMillis() - this.timingEvents
                            .remove(key)));
        }
        return new AnalyticsOptions(new NoopOptionsCallback(shouldLogCalls) {
            @Override
            public void onCallback(Map<String, String> options) {
                // Do nothing a timing event was ended before it was started.
                // This is here just to stop a crash.
            }
        }).timingOptions(timingCategory, timingVariableName, 0);
    }

    @Override
    public AnalyticsOptions send(final String trackerName, final HitType hitType) {
        return new AnalyticsOptions(new NoopOptionsCallback(shouldLogCalls) {
            @Override
            public void onCallback(Map<String, String> options) {
                call(trackerName == null ? "send" : trackerName + ".send",
                        hitType.getFieldName(), options.toString());
            }
        });
    }

    @Override
    public GeneralOptions setGlobalSettings() {
        return new AnalyticsOptions(new NoopOptionsCallback(shouldLogCalls) {
            @Override
            public void onCallback(Map<String, String> options) {
                call("set", options.toString());
            }
        }).generalOptions();
    }

    @Override
    public void startTimingEvent(final String timingCategory,
            final String timingVariableName) {
        this.timingEvents.put(getTimingKey(timingCategory, timingVariableName),
                Duration.currentTimeMillis());
    }
}
