/*
 * Copyright (C) 2018 The Dagger Authors.
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

package dagger.internal.codegen;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.base.Preconditions.checkState;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.CodeBlock;
import dagger.internal.codegen.ModifiableBindingMethods.ModifiableBindingMethod;
import java.util.Optional;

/** A binding expression that wraps another in a nullary method on the component. */
abstract class MethodBindingExpression extends BindingExpression {

  private final BindingMethodImplementation methodImplementation;
  private final GeneratedComponentModel generatedComponentModel;
  private final Optional<ModifiableBindingMethod> matchingModifiableBindingMethod;

  protected MethodBindingExpression(
      BindingMethodImplementation methodImplementation,
      GeneratedComponentModel generatedComponentModel,
      Optional<ModifiableBindingMethod> matchingModifiableBindingMethod) {
    this.methodImplementation = checkNotNull(methodImplementation);
    this.generatedComponentModel = checkNotNull(generatedComponentModel);
    this.matchingModifiableBindingMethod = checkNotNull(matchingModifiableBindingMethod);
  }

  @Override
  Expression getDependencyExpression(ClassName requestingClass) {
    addMethod();
    return Expression.create(
        methodImplementation.returnType(),
        requestingClass.equals(generatedComponentModel.name())
            ? CodeBlock.of("$N()", methodName())
            : CodeBlock.of("$T.this.$N()", generatedComponentModel.name(), methodName()));
  }

  @Override
  final CodeBlock getModifiableBindingMethodImplementation(
      ModifiableBindingMethod modifiableBindingMethod, GeneratedComponentModel component) {
    // A matching modifiable binding method means that we have previously created the binding method
    // and we are now implementing it. If there is no matching method we need to first create the
    // method. We create the method by deferring to getDependencyExpression (defined above) via a
    // call to super.getModifiableBindingMethodImplementation().
    if (matchingModifiableBindingMethod.isPresent()) {
      checkState(
          matchingModifiableBindingMethod.get().fulfillsSameRequestAs(modifiableBindingMethod));
      return methodImplementation.body();
    }
    return super.getModifiableBindingMethodImplementation(modifiableBindingMethod, component);
  }

  /** Adds the method to the component (if necessary) the first time it's called. */
  protected abstract void addMethod();

  /** Returns the name of the method to call. */
  protected abstract String methodName();
}
