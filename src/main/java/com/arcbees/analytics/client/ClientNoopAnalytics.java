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

import com.arcbees.analytics.shared.GaAccount;
import com.google.gwt.core.client.JavaScriptObject;
import com.google.inject.Inject;

public class ClientNoopAnalytics extends ClientAnalytics {

    @Inject
    public ClientNoopAnalytics(
            @GaAccount String userAccount,
            @AutoCreate boolean autoCreate,
            @AutoInject boolean autoInject,
            @TrackInitialPageView boolean trackInitialPageView,
            @FallbackPath String fallbackPath) {
        super(userAccount, autoCreate, autoInject, trackInitialPageView, fallbackPath);
    }

    @Override
    protected native void nativeCall(final JavaScriptObject params) /*-{
        if($wnd.console){
            $wnd.console.log("Analytics would have called: " + JSON.stringify(params));
        }
    }-*/;
}
