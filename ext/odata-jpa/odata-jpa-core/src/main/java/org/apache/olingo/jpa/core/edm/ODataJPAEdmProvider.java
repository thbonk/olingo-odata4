/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements. See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership. The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.apache.olingo.jpa.core.edm;

import java.util.ArrayList;
import java.util.List;

import org.apache.olingo.commons.api.ODataException;
import org.apache.olingo.commons.api.edm.provider.CsdlSchema;
import org.apache.olingo.jpa.api.ODataJPAAbstractEdmProvider;
import org.apache.olingo.jpa.api.ODataJPAContext;
import org.apache.olingo.jpa.api.model.JPACsdlBuilder;
import org.apache.olingo.jpa.api.model.JPACsdlMetaModelAccessor;
import org.apache.olingo.jpa.api.model.JPACsdlSchemaAccessor;
import org.apache.olingo.jpa.core.model.JPACsdlMetaModel;

public class ODataJPAEdmProvider extends ODataJPAAbstractEdmProvider {

  private List<CsdlSchema> csdlSchemas = new ArrayList<CsdlSchema>();

  public ODataJPAEdmProvider(ODataJPAContext context) {
    super(context);
  }

  @Override
  public List<CsdlSchema> getSchemas() throws ODataException {
    // Build CSDL Schema if not yet built
    if (csdlSchemas.isEmpty()) {
      JPACsdlBuilder jpaCsdlMetaModelBuilder = JPACsdlMetaModel.newJPACsdlMetaModelBuilder(this.getODataJPAContext());
      JPACsdlMetaModelAccessor metaModelAccessor = jpaCsdlMetaModelBuilder.build();
      csdlSchemas.add(((JPACsdlSchemaAccessor) metaModelAccessor).getCsdlSchema());
    }
    return csdlSchemas;
  }
}
