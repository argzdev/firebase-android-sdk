// Copyright 2019 Google LLC
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//      http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package com.google.firebase.datatransport;

import android.content.Context;
import androidx.annotation.Keep;
import androidx.annotation.NonNull;
import com.google.android.datatransport.TransportFactory;
import com.google.android.datatransport.cct.CCTDestination;
import com.google.android.datatransport.runtime.TransportRuntime;
import com.google.firebase.components.Component;
import com.google.firebase.components.ComponentRegistrar;
import com.google.firebase.components.Dependency;
import com.google.firebase.components.Qualified;
import com.google.firebase.platforminfo.LibraryVersionComponent;
import java.util.Arrays;
import java.util.List;

@Keep
public class TransportRegistrar implements ComponentRegistrar {
  private static final String LIBRARY_NAME = "fire-transport";

  @Override
  @NonNull
  public List<Component<?>> getComponents() {
    return Arrays.asList(
        Component.builder(TransportFactory.class)
            .name(LIBRARY_NAME)
            .add(Dependency.required(Context.class))
            .factory(
                c -> {
                  TransportRuntime.initialize(c.get(Context.class));
                  return TransportRuntime.getInstance().newFactory(CCTDestination.LEGACY_INSTANCE);
                })
            .build(),
        Component.builder(Qualified.qualified(LegacyTransportBackend.class, TransportFactory.class))
            .add(Dependency.required(Context.class))
            .factory(
                c -> {
                  TransportRuntime.initialize(c.get(Context.class));
                  return TransportRuntime.getInstance().newFactory(CCTDestination.LEGACY_INSTANCE);
                })
            .build(),
        Component.builder(Qualified.qualified(TransportBackend.class, TransportFactory.class))
            .add(Dependency.required(Context.class))
            .factory(
                c -> {
                  TransportRuntime.initialize(c.get(Context.class));
                  return TransportRuntime.getInstance().newFactory(CCTDestination.INSTANCE);
                })
            .build(),
        LibraryVersionComponent.create(LIBRARY_NAME, BuildConfig.VERSION_NAME));
  }
}
