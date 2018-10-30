/*
 * Copyright (C) 2017 The Dagger Authors.
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

package dagger.model;

/** Represents the different kinds of {@link Binding}s that can exist in a binding graph. */
public enum BindingKind {
  /** A binding for an {@link javax.inject.Inject}-annotated constructor. */
  INJECTION,

  /** A binding for a {@link dagger.Provides}-annotated method. */
  PROVISION,

  /**
   * An implicit binding for a {@link dagger.Component}- or {@link
   * dagger.producers.ProductionComponent}-annotated type.
   */
  COMPONENT,

  /**
   * A binding for a provision method on a component's {@linkplain dagger.Component#dependencies()
   * dependency}.
   */
  COMPONENT_PROVISION,

  /**
   * A binding for an instance of a component's {@linkplain dagger.Component#dependencies()
   * dependency}.
   */
  COMPONENT_DEPENDENCY,

  /** A binding for a {@link dagger.MembersInjector} of a type. */
  MEMBERS_INJECTOR,

  /** A binding for a subcomponent builder. */
  SUBCOMPONENT_BUILDER,

  /** A binding for a {@link dagger.BindsInstance}-annotated builder method. */
  BOUND_INSTANCE,

  /** A binding for a {@link dagger.producers.Produces}-annotated method. */
  PRODUCTION,

  /**
   * A binding for a production method on a production component's {@linkplain
   * dagger.producers.ProductionComponent#dependencies()} dependency} that returns a {@link
   * com.google.common.util.concurrent.ListenableFuture} or {@link
   * com.google.common.util.concurrent.FluentFuture}. Methods on production component dependencies
   * that don't return a future are considered {@linkplain #COMPONENT_PROVISION component provision
   * bindings}.
   */
  COMPONENT_PRODUCTION,

  /**
   * A synthetic binding for a multibound set that depends on individual multibinding {@link
   * #PROVISION} or {@link #PRODUCTION} contributions.
   */
  MULTIBOUND_SET,

  /**
   * A synthetic binding for a multibound map that depends on the individual multibinding {@link
   * #PROVISION} or {@link #PRODUCTION} contributions.
   */
  MULTIBOUND_MAP,

  /**
   * A synthetic binding for {@code Optional} of a type or a {@link javax.inject.Provider}, {@link
   * dagger.Lazy}, or {@code Provider} of {@code Lazy} of a type. Generated by a {@link
   * dagger.BindsOptionalOf} declaration.
   */
  OPTIONAL,

  /**
   * A binding for {@link dagger.Binds}-annotated method that that delegates from requests for one
   * key to another.
   */
  // TODO(dpb,ronshapiro): This name is confusing and could use work. Not all usages of @Binds
  // bindings are simple delegations and we should have a name that better reflects that
  DELEGATE,

  /** A binding for a members injection method on a component. */
  MEMBERS_INJECTION,
  ;

  /**
   * Returns {@code true} if this is a kind of multibinding (not a contribution to a multibinding,
   * but the multibinding itself).
   */
  public boolean isMultibinding() {
    switch (this) {
      case MULTIBOUND_MAP:
      case MULTIBOUND_SET:
        return true;

      default:
        return false;
    }
  }
}
