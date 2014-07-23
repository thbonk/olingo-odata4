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
package org.apache.olingo.fit.proxy.v3.staticservice.microsoft.test.odata.services.astoriadefaultservice.types;

//CHECKSTYLE:OFF (Maven checkstyle)


@org.apache.olingo.ext.proxy.api.annotations.Namespace("Microsoft.Test.OData.Services.AstoriaDefaultService")
@org.apache.olingo.ext.proxy.api.annotations.ComplexType(name = "ComplexToCategory")
public interface ComplexToCategory 
    extends org.apache.olingo.ext.proxy.api.ComplexType,org.apache.olingo.ext.proxy.api.SingleQuery<ComplexToCategory> {




    @org.apache.olingo.ext.proxy.api.annotations.Property(
                name = "Term", 
                type = "Edm.String", 
                nullable = true)
    java.lang.String getTerm();

    void setTerm(java.lang.String _term);

    


    @org.apache.olingo.ext.proxy.api.annotations.Property(
                name = "Scheme", 
                type = "Edm.String", 
                nullable = true)
    java.lang.String getScheme();

    void setScheme(java.lang.String _scheme);

    


    @org.apache.olingo.ext.proxy.api.annotations.Property(
                name = "Label", 
                type = "Edm.String", 
                nullable = true)
    java.lang.String getLabel();

    void setLabel(java.lang.String _label);

    
}
