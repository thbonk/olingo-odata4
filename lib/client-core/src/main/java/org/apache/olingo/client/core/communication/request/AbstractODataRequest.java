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
package org.apache.olingo.client.core.communication.request;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Constructor;
import java.net.URI;
import java.util.Collection;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.DecompressingHttpClient;
import org.apache.olingo.client.api.CommonODataClient;
import org.apache.olingo.client.api.communication.header.HeaderName;
import org.apache.olingo.client.api.communication.header.ODataHeaders;
import org.apache.olingo.client.api.communication.request.ODataRequest;
import org.apache.olingo.client.api.communication.request.ODataStreamer;
import org.apache.olingo.client.api.communication.response.ODataResponse;
import org.apache.olingo.commons.api.format.Format;
import org.apache.olingo.client.api.http.HttpClientException;
import org.apache.olingo.client.api.http.HttpMethod;
import org.apache.olingo.client.core.communication.header.ODataHeadersImpl;
import org.apache.olingo.commons.api.format.ODataMediaFormat;
import org.apache.olingo.commons.api.format.ODataPubFormat;
import org.apache.olingo.commons.api.format.ODataValueFormat;

/**
 * Abstract representation of an OData request. Get instance by using factories.
 *
 * @param <T> Accepted content-type formats by the request in object.
 *
 * @see org.apache.olingo.client.api.communication.request.cud.v3.CUDRequestFactory
 * @see org.apache.olingo.client.api.communication.request.cud.v4.CUDRequestFactory
 * @see org.apache.olingo.client.api.communication.request.batch.v3.BatchRequestFactory
 * @see org.apache.olingo.client.api.communication.request.batch.v4.BatchRequestFactory
 * @see org.apache.olingo.client.api.communication.request.invoke.v3.InvokeRequestFactory
 * @see org.apache.olingo.client.api.communication.request.invoke.v4.InvokeRequestFactory
 * @see org.apache.olingo.client.api.communication.request.streamed.v3.StreamedRequestFactory
 * @see org.apache.olingo.client.api.communication.request.streamed.v4.StreamedRequestFactory
 */
public abstract class AbstractODataRequest<T extends Format> extends AbstractRequest implements ODataRequest {

  protected final CommonODataClient<?> odataClient;

  private final Class<T> formatRef;

  /**
   * OData request method.
   */
  protected final HttpMethod method;

  /**
   * OData request header.
   */
  protected final ODataHeadersImpl odataHeaders;

  /**
   * Target URI.
   */
  protected final URI uri;

  /**
   * HTTP client.
   */
  protected final HttpClient httpClient;

  /**
   * HTTP request.
   */
  protected final HttpUriRequest request;

  /**
   * Constructor.
   *
   * @param odataClient client instance getting this request
   * @param formatRef reference class for the format being used
   * @param method HTTP request method. If configured X-HTTP-METHOD header will be used.
   * @param uri OData request URI.
   */
  protected AbstractODataRequest(final CommonODataClient<?> odataClient,
          final Class<T> formatRef, final HttpMethod method, final URI uri) {

    this.odataClient = odataClient;

    this.formatRef = formatRef;
    this.method = method;

    // initialize default headers
    this.odataHeaders = (ODataHeadersImpl) odataClient.getVersionHeaders();

    // target uri
    this.uri = uri;

    HttpClient _httpClient = odataClient.getConfiguration().getHttpClientFactory().
            createHttpClient(this.method, this.uri);
    if (odataClient.getConfiguration().isGzipCompression()) {
      _httpClient = new DecompressingHttpClient(_httpClient);
    }
    this.httpClient = _httpClient;

    this.request = odataClient.getConfiguration().getHttpUriRequestFactory().
            createHttpUriRequest(this.method, this.uri);
  }

  /**
   * {@inheritDoc}
   */
  @SuppressWarnings("unchecked")
  public T getDefaultFormat() {
    return (T) (formatRef.equals(ODataPubFormat.class)
            ? odataClient.getConfiguration().getDefaultPubFormat()
            : (formatRef.equals(ODataValueFormat.class)
            ? odataClient.getConfiguration().getDefaultValueFormat()
            : (formatRef.equals(ODataMediaFormat.class)
            ? odataClient.getConfiguration().getDefaultMediaFormat()
            : odataClient.getConfiguration().getDefaultFormat())));
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public URI getURI() {
    return uri;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Collection<String> getHeaderNames() {
    return odataHeaders.getHeaderNames();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String getHeader(final String name) {
    return odataHeaders.getHeader(name);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public ODataRequest setAccept(final String value) {
    odataHeaders.setHeader(HeaderName.accept, value);
    return this;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public ODataRequest setIfMatch(final String value) {
    odataHeaders.setHeader(HeaderName.ifMatch, value);
    return this;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public ODataRequest setIfNoneMatch(final String value) {
    odataHeaders.setHeader(HeaderName.ifNoneMatch, value);
    return this;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public ODataRequest setPrefer(final String value) {
    odataHeaders.setHeader(HeaderName.prefer, value);
    return this;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public ODataRequest setXHTTPMethod(final String value) {
    odataHeaders.setHeader(HeaderName.xHttpMethod, value);
    return this;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public ODataRequest setContentType(final String value) {
    odataHeaders.setHeader(HeaderName.contentType, value);
    return this;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public ODataRequest setSlug(final String value) {
    odataHeaders.setHeader(HeaderName.slug, value);
    return this;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public ODataRequest addCustomHeader(final String name, final String value) {
    odataHeaders.setHeader(name, value);
    return this;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public ODataRequest addCustomHeader(final HeaderName name, final String value) {
    odataHeaders.setHeader(name, value);
    return this;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String getAccept() {
    final String acceptHead = odataHeaders.getHeader(HeaderName.accept);
    return StringUtils.isBlank(acceptHead) ? getDefaultFormat().toString(odataClient.getServiceVersion()) : acceptHead;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String getIfMatch() {
    return odataHeaders.getHeader(HeaderName.ifMatch);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String getIfNoneMatch() {
    return odataHeaders.getHeader(HeaderName.ifNoneMatch);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String getPrefer() {
    return odataHeaders.getHeader(HeaderName.prefer);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String getContentType() {
    final String contentTypeHead = odataHeaders.getHeader(HeaderName.contentType);
    return StringUtils.isBlank(contentTypeHead)
            ? getDefaultFormat().toString(odataClient.getServiceVersion()) : contentTypeHead;
  }

  /**
   * ${@inheritDoc }
   */
  @Override
  public HttpMethod getMethod() {
    return method;
  }

  /**
   * Gets request headers.
   *
   * @return request headers.
   */
  public ODataHeaders getHeader() {
    return odataHeaders;
  }

  /**
   * {@inheritDoc }
   */
  @Override
  public byte[] toByteArray() {
    final ByteArrayOutputStream baos = new ByteArrayOutputStream();
    try {
      final StringBuilder requestBuilder = new StringBuilder();
      requestBuilder.append(getMethod().toString()).append(' ').append(uri.toString()).append(' ').append("HTTP/1.1");

      baos.write(requestBuilder.toString().getBytes());

      baos.write(ODataStreamer.CRLF);

      // Set Content-Type and Accept headers with default values, if not yet set
      if (StringUtils.isBlank(odataHeaders.getHeader(HeaderName.contentType))) {
        setContentType(getContentType());
      }
      if (StringUtils.isBlank(odataHeaders.getHeader(HeaderName.accept))) {
        setAccept(getAccept());
      }

      for (String name : getHeaderNames()) {
        final String value = getHeader(name);

        if (StringUtils.isNotBlank(value)) {
          baos.write((name + ": " + value).getBytes());
          baos.write(ODataStreamer.CRLF);
        }
      }

      return baos.toByteArray();
    } catch (IOException e) {
      throw new IllegalStateException(e);
    } finally {
      IOUtils.closeQuietly(baos);
    }
  }

  /**
   * {@inheritDoc }
   */
  @Override
  public InputStream rawExecute() {
    try {
      final HttpEntity httpEntity = doExecute().getEntity();
      return httpEntity == null ? null : httpEntity.getContent();
    } catch (IOException e) {
      throw new HttpClientException(e);
    } catch (RuntimeException e) {
      this.request.abort();
      throw new HttpClientException(e);
    }
  }

  /**
   * Builds the request and execute it.
   *
   * @return HttpReponse object.
   */
  protected HttpResponse doExecute() {
    checkRequest(odataClient, request);

    // Set Content-Type and Accept headers with default values, if not yet set
    if (StringUtils.isBlank(odataHeaders.getHeader(HeaderName.contentType))) {
      setContentType(getContentType());
    }
    if (StringUtils.isBlank(odataHeaders.getHeader(HeaderName.accept))) {
      setAccept(getAccept());
    }

    // Add header for KeyAsSegment management
    if (odataClient.getConfiguration().isKeyAsSegment()) {
      addCustomHeader(
              HeaderName.dataServiceUrlConventions.toString(),
              odataClient.newPreferences().keyAsSegment());
    }

    // Add all available headers
    for (String key : getHeaderNames()) {
      this.request.addHeader(key, odataHeaders.getHeader(key));
    }

    if (LOG.isDebugEnabled()) {
      for (Header header : this.request.getAllHeaders()) {
        LOG.debug("HTTP header being sent: " + header);
      }
    }

    HttpResponse response;
    try {
      response = this.httpClient.execute(this.request);
    } catch (IOException e) {
      throw new HttpClientException(e);
    } catch (RuntimeException e) {
      this.request.abort();
      throw new HttpClientException(e);
    }

    checkResponse(odataClient, response, getAccept());

    return response;
  }

  /**
   * Gets an empty response that can be initialized by a stream.
   * <p>
   * This method has to be used to build response items about a batch request.
   *
   * @param <V> ODataResppnse type.
   * @return empty OData response instance.
   */
  @SuppressWarnings("unchecked")
  public <V extends ODataResponse> V getResponseTemplate() {

    for (Class<?> clazz : this.getClass().getDeclaredClasses()) {
      if (ODataResponse.class.isAssignableFrom(clazz)) {
        try {
          final Constructor<?> constructor = clazz.getDeclaredConstructor(this.getClass());
          constructor.setAccessible(true);
          return (V) constructor.newInstance(this);
        } catch (Exception e) {
          LOG.error("Error retrieving response class template instance", e);
        }
      }
    }

    throw new IllegalStateException("No response class template has been found");
  }
}