/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.apache.olingo.odata4.client.core.edm.v4.annotation;

import org.apache.olingo.odata4.client.api.edm.v4.annotation.DynExprConstruct;

public class DynExprSingleParamOp extends DynExprConstructImpl {

  private static final long serialVersionUID = -7974475975925167731L;

  public static enum Type {

    Not,
    Eq,
    Ne,
    Gt,
    Ge,
    Lt,
    Le;

    public static Type fromString(final String value) {
      Type result = null;
      for (Type type : values()) {
        if (value.equals(type.name())) {
          result = type;
        }
      }
      return result;
    }

  }

  private Type type;

  private DynExprConstruct expression;

  public Type getType() {
    return type;
  }

  public void setType(final Type type) {
    this.type = type;
  }

  public DynExprConstruct getExpression() {
    return expression;
  }

  public void setExpression(final DynExprConstruct expression) {
    this.expression = expression;
  }

}