package com.bettercloud.vault.rest;

import com.bettercloud.vault.VaultException;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.security.KeyStore;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * <p>A simple client for issuing HTTP requests.  Supports the HTTP verbs:</p>
 * <ul>
 *     <li>GET</li>
 *     <li>POST</li>
 *     <li>PUT</li>
 *     <li>DELETE</li>
 * </ul>
 *
 * <p><code>Rest</code> uses the Builder pattern to provide a basic DSL for usage.  Methods for configuring an HTTP
 * request (i.e. <code>url()</code>, <code>parameter()</code>, and <code>header()</code>) are designed to be chained
 * together, while methods corresponding to the HTTP verbs are terminating operations:</p>
 *
 * <blockquote>
 * <pre>{@code
 * final RestResponse getResponse = new Rest()
 *                              .url("https://httpbin.org/get")
 *                              .header("header-1", "foo")
 *                              .header("header-2", "bar")
 *                              .parameter("param-1", "up")
 *                              .parameter("param-2", "down")
 *                              .get();
 *
 * final RestResponse postResponse = new Rest()
 *                              .url("https://httpbin.org/post")
 *                              .header("header-1", "foo")
 *                              .header("header-2", "bar")
 *                              .body( jsonString.getBytes("UTF-8") )
 *                              .post();
 * }</pre>
 * </blockquote>
 *
 * <p>Header and parameter names and values are url-encoded by the Rest client prior to sending the request.  The URL
 * string should be url-encoded by you (if necessary) prior to passing it.</p>
 */
public final class Rest {

    private String urlString;
    private byte[] body;
    private final Map<String, String> parameters = new TreeMap<String, String>();
    private final Map<String, String> headers = new TreeMap<String, String>();

    private Integer connectTimeoutSeconds;
    private Integer readTimeoutSeconds;
    private Boolean sslVerification;
    private String sslPemUTF8;

    /**
     * <p>Sets the base URL to which the HTTP request will be sent.  The URL may or may not include query parameters
     * (e.g. <code>http://httpbin.org/get?param-1=foo</code>).</p>
     *
     * <p>Depending on which HTTP verb is ultimately used, than any additional parameters set via the
     * <code>parameters()</code> method may be appending to this URL.</p>
     *
     * <p>Either way, the responsibility for any url-encoding of this base URL string belongs to the caller.</p>
     *
     * @param urlString A URL string, with any necessary url-encoding already applied @return The <code>Rest</code> instance itself
     */
    public Rest url(final String urlString) {
        this.urlString = urlString;
        return this;
    }

    /**
     * <p>Sets a binary payload that will be sent as the request body for POST or PUT requests.  Any value set here
     * will be ignored for GET requests.  Conversely, if a value IS set here... then any additional parameter values
     * set by <code>parameter()</code> will be ignored for POST or PUT requests.</p>
     *
     * @param body The payload to send with a POST or PUT request (e.g. a JSON string)
     * @return The <code>Rest</code> instance itself
     */
    public Rest body(final byte[] body) {
        this.body = body;
        return this;
    }

    /**
     * <p>Adds a parameter to be sent with the HTTP request.  Depending on which HTTP verb is ultimately used, this
     * parameter may either be appended to the URL or else posted with the request body.  Either way, both the
     * parameter name and value will be automatically url-encoded by the Rest client.</p>
     *
     * <p>For POST and PUT requests, these parameters will only be sent in the request body if that body is otherwise
     * unset.  In other words, if the <code>body()</code> method is invoked, then <code>parameter()</code> invocations
     * will be ignored for a POST or PUT.</p>
     *
     * <p>This method may be chained together repeatedly, to pass multiple parameters with a request.  When the
     * request is ultimately sent, the parameters will be sorted by their names.</p>
     *
     * @param name The raw parameter name (not url-encoded)
     * @param value The raw parameter value (not url-encoded)
     * @return The <code>Rest</code> instance itself
     * @throws RestException
     */
    public Rest parameter(final String name, final String value) throws RestException {
        try {
            this.parameters.put(URLEncoder.encode(name, "UTF-8"), URLEncoder.encode(value, "UTF-8"));
        } catch (UnsupportedEncodingException e) {
            throw new RestException(e);
        }
        return this;
    }

    /**
     * <p>Adds a header to be send with the HTTP request.</p>
     *
     * <p>Both the header name and value will be automatically url-encoded by the Rest client.</p>
     *
     * <p>This method may be chained together repeatedly, to pass multiple headers with a request.  When the request
     * is ultimately sent, the headers will be sorted by their names.</p>
     *
     * @param name The raw header name (not url-encoded)
     * @param value The raw header value (not url-encoded)
     * @return The <code>Rest</code> instance itself
     * @throws RestException
     */
    public Rest header(final String name, final String value) throws RestException {
        try {
            this.headers.put(URLEncoder.encode(name, "UTF-8"), URLEncoder.encode(value, "UTF-8"));
        } catch (UnsupportedEncodingException e) {
            throw new RestException(e);
        }
        return this;
    }

    // TODO: Add testing for timeouts

    /**
     * TODO: Document
     *
     * @param connectTimeoutSeconds
     * @return
     */
    public Rest connectTimeoutSeconds(final Integer connectTimeoutSeconds) {
        this.connectTimeoutSeconds = connectTimeoutSeconds;
        return this;
    }

    /**
     * TODO: Document
     *
     * @param readTimeoutSeconds
     * @return
     */
    public Rest readTimeoutSeconds(final Integer readTimeoutSeconds) {
        this.readTimeoutSeconds = readTimeoutSeconds;
        return this;
    }

    /**
     * <p>Whether or not HTTPS connections should verify that the server has a valid SSL certificate.
     * Unless this is set to <code>false</code>, the default behavior is to always verify SSL certificates.</p>
     *
     * <p>SSL CERTIFICATE VERIFICATION SHOULD NOT BE DISABLED IN PRODUCTION!  This feature is made available to
     * facilitate development or testing environments, where you might be using a self-signed cert that will not
     * pass verification.  However, even if you are using a self-signed cert on your server, you can still leave
     * SSL verification enabled and have your application supply the cert using <code>sslPemFile()</code>,
     * <code>sslPemResource()</code>, or <code>sslPemUTF8()</code>.</p>
     *
     * @param sslVerification Whether or not to verify the SSL certificate used by the server with HTTPS connections.  Default is <code>true</code>.
     * @return
     */
    public Rest sslVerification(final Boolean sslVerification) {
        this.sslVerification = sslVerification;
        return this;
    }

    /**
     * <p>An X.509 certificate, to use when communicating with Vault over HTTPS.  This method accepts a string
     * containing the certificate data.  This string should meet the following requirements:</p>
     *
     * <ul>
     *     <li>Contain an unencrypted X.509 certificate, in PEM format.</li>
     *     <li>Use UTF-8 encoding.</li>
     *     <li>
     *          Contain a line-break between the certificate header (e.g. "-----BEGIN CERTIFICATE-----") and the
     *          rest of the certificate content.  It doesn't matter whether or not there are additional line
     *          breaks within the certificate content, or whether there is a line break before the certificate
     *          footer (e.g. "-----END CERTIFICATE-----").  But the Java standard library will fail to properly
     *          process the certificate without a break following the header
     *          (see http://www.doublecloud.org/2014/03/reading-x-509-certificate-in-java-how-to-handle-format-issue/).
     *      </li>
     * </ul>
     *
     * @param pemFileContents An X.509 certificate, in unencrypted PEM format with UTF-8 encoding.
     * @return
     */
    public Rest sslPemUTF8(final String pemFileContents) {
        this.sslPemUTF8 = pemFileContents;
        return this;
    }

    /**
     * <p>Executes an HTTP GET request with the settings already configured.  Parameters and headers are optional, but
     * a <code>RestException</code> will be thrown if the caller has not first set a base URL with the
     * <code>url()</code> method.</p>
     *
     * <p>If a body payload has been set through the <code>body()</code> method, then it will be ignored when sending
     * a GET request.</p>
     *
     * @return The result of the HTTP operation
     * @throws RestException
     */
    public RestResponse get() throws RestException {
        if (urlString == null) {
            throw new RestException("No URL is set");
        }
        try {
            if (!parameters.isEmpty()) {
                // Append parameters to existing query string, or create one
                if (urlString.indexOf('?') == -1) {
                    urlString = urlString + "?" + parametersToQueryString();
                } else {
                    urlString = urlString + "&" + parametersToQueryString();
                }
            }
            // TODO:  Do this for "post" likewise
            // Initialize HTTP(S) connection, and set any header values
            final URLConnection connection = initURLConnection(urlString, "GET");
            for (final Map.Entry<String, String> header : headers.entrySet()) {
                connection.setRequestProperty(header.getKey(), header.getValue());
            }

            // Set request method and get resulting status code
            int statusCode;
            if (connection instanceof HttpsURLConnection) {
                final HttpsURLConnection httpsURLConnection = (HttpsURLConnection) connection;
                statusCode = httpsURLConnection.getResponseCode();
            } else if (connection instanceof HttpURLConnection) {
                final HttpURLConnection httpURLConnection = (HttpURLConnection) connection;
                statusCode = httpURLConnection.getResponseCode();
            } else {
                final String className = connection != null ? connection.getClass().getName() : "null";
                throw new RestException("Expecting a URLConnection of type "
                        + HttpURLConnection.class.getName()
                        + " or "
                        + HttpsURLConnection.class.getName()
                        + ", found "
                        + className);
            }

            // Download and parse response
            final String mimeType = connection.getContentType();
            final byte[] body = responseBodyBytes(connection);
            return new RestResponse(statusCode, mimeType, body);
        } catch (Exception e) {
            throw new RestException(e);
        }
    }

    /**
     * Executes an HTTP POST request with the settings already configured.  Parameters and headers are optional, but a
     * <code>RestException</code> will be thrown if the caller has not first set a base URL with the
     * <code>url()</code> method.
     *
     * <p>CGI parameters can always be passed via a query string on the URL.  Also, parameter values set via the
     * <code>parameter()</code> method will be sent with the POST request as form data.  However, if a body payload
     * is provided via the <code>body()</code> method, then that takes precedence over any parameters set via
     * <code>parameter()</code>, and those values will be discarded.</p>
     *
     * @return The result of the HTTP operation
     * @throws RestException
     */
    public RestResponse post() throws RestException {
        return postOrPutImpl(true);
    }

    /**
     * Executes an HTTP PUT request with the settings already configured.  Parameters and headers are optional, but a
     * <code>RestException</code> will be thrown if the caller has not first set a base URL with the
     * <code>url()</code> method.
     *
     * <p>CGI parameters can always be passed via a query string on the URL.  Also, parameter values set via the
     * <code>parameter()</code> method will be sent with the PUT request as form data.  However, if a body payload
     * is provided via the <code>body()</code> method, then that takes precedence over any parameters set via
     * <code>parameter()</code>, and those values will be discarded.</p>
     *
     * @return The result of the HTTP operation
     * @throws RestException
     */
    public RestResponse put() throws RestException {
        return postOrPutImpl(false);
    }

    /**
     * Executes an HTTP DELETE request with the settings already configured.  Parameters and headers are optional,
     * but a <code>RestException</code> will be thrown if the caller has not first set a base URL with the
     * <code>url()</code> method.
     *
     * @return The result of the HTTP operation
     * @throws RestException
     */
    public RestResponse delete() throws RestException {
        // TODO: Implement
        throw new UnsupportedOperationException();
    }


    /**
     * Since the implementations of a POST request and PUT request differ by only one line of code, they are refactored
     * into this private method which is turned wrapped by <code>post()</code> and <code>put()</code>.
     *
     * @param doPost If <code>true</code>, then a POST operation will be performed.  If false, then a PUT.
     * @return The result of the HTTP operation
     * @throws RestException
     */
    private RestResponse postOrPutImpl(final boolean doPost) throws RestException {
        if (urlString == null) {
            throw new RestException("No URL is set");
        }
        try {
            // Initialize HTTP connection, and set any header values
            final URL url = new URL(urlString);
            final HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            if (doPost) {
                connection.setRequestMethod("POST");
            } else {
                connection.setRequestMethod("PUT");
            }
            for (final Map.Entry<String, String> header : headers.entrySet()) {
                connection.setRequestProperty(header.getKey(), header.getValue());
            }

            connection.setDoOutput(true);
            connection.setRequestProperty("Accept-Charset", "UTF-8");

            // If a body payload has been provided, then it takes precedence.  Otherwise, look for any additional
            // parameters to send as form field values.  Parameters sent via the base URL query string are left
            // as-is regardless.
            if (body != null) {
                final OutputStream outputStream = connection.getOutputStream();
                outputStream.write(body);
                outputStream.close();
            } else if (!parameters.isEmpty()) {
                connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded;charset=UTF-8");
                final OutputStream outputStream = connection.getOutputStream();
                outputStream.write(parametersToQueryString().getBytes("UTF-8"));
                outputStream.close();
            }

            // Download and parse response
            final int statusCode = connection.getResponseCode();
            final String mimeType = connection.getContentType();
            final byte[] body = responseBodyBytes(connection);
            return new RestResponse(statusCode, mimeType, body);
        } catch (IOException e) {
            throw new RestException(e);
        }
    }

    /**
     * TODO: Document
     *
     * @param urlString
     * @param method
     * @return
     * @throws VaultException
     */
    private URLConnection initURLConnection(final String urlString, final String method) throws VaultException {
        try {
            final URL url = new URL(urlString);
            final URLConnection connection = url.openConnection();

            // Timeout settings, if applicable
            if (connectTimeoutSeconds != null) {
                connection.setConnectTimeout(connectTimeoutSeconds * 1000);
            }
            if (readTimeoutSeconds != null) {
                connection.setReadTimeout(readTimeoutSeconds * 1000);
            }

            // SSL settings, if applicable
            if (connection instanceof HttpsURLConnection) {
                final HttpsURLConnection httpsURLConnection = (HttpsURLConnection) connection;
                // Cert file supplied
                if (sslPemUTF8 != null) {
                    final SSLContext sslContext = initSSLContext();
                    httpsURLConnection.setSSLSocketFactory(sslContext.getSocketFactory());
                }
                // SSL verification disabled
                if (sslVerification != null && !sslVerification.booleanValue()) {
                    final SSLContext sslContext = SSLContext.getInstance("TLS");
                    sslContext.init(null, new TrustManager[]{new X509TrustManager() {
                        @Override
                        public void checkClientTrusted(final X509Certificate[] x509Certificates, final String s) throws CertificateException {
                        }

                        @Override
                        public void checkServerTrusted(final X509Certificate[] x509Certificates, final String s) throws CertificateException {
                        }

                        @Override
                        public X509Certificate[] getAcceptedIssuers() {
                            return new X509Certificate[0];
                        }
                    }}, new java.security.SecureRandom());
                    httpsURLConnection.setSSLSocketFactory(sslContext.getSocketFactory());
                    httpsURLConnection.setHostnameVerifier(new HostnameVerifier() {
                        @Override
                        public boolean verify(final String s, final SSLSession sslSession) {
                            return true;
                        }
                    });
                }
                httpsURLConnection.setRequestMethod(method);
            } else if (connection instanceof HttpURLConnection) {
                final HttpURLConnection httpURLConnection = (HttpURLConnection) connection;
                httpURLConnection.setRequestMethod(method);
            } else {
                final String message = "URL string " + (urlString != null ? urlString : "null") + " cannot be parsed as an instance of HttpURLConnection or HttpsURLConnection";
                throw new VaultException(message);
            }
            return connection;
        } catch (Exception e) {
            throw new VaultException(e);
        }
    }

    /**
     * TODO: Document
     *
     * @return
     * @throws VaultException
     */
    private SSLContext initSSLContext() throws VaultException {
        try {
            final CertificateFactory certificateFactory = CertificateFactory.getInstance("X.509");

            final ByteArrayInputStream pem = new ByteArrayInputStream(sslPemUTF8.getBytes("UTF-8"));
            final X509Certificate certificate = (X509Certificate) certificateFactory.generateCertificate(pem);
            pem.close();

            final TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
            final KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
            keyStore.load(null);
            keyStore.setCertificateEntry("caCert", certificate);

            trustManagerFactory.init(keyStore);

            final SSLContext sslContext = SSLContext.getInstance("TLS");
            sslContext.init(null, trustManagerFactory.getTrustManagers(), null);
            return sslContext;
        } catch (Exception e) {
            throw new VaultException(e);
        }
    }

    /**
     * <p>This helper method constructs a query string (e.g. <code>param-1=foo&param-2=bar</code>) from any parameters
     * that have been set via the <code>param()</code> method.  Parameters will be sorted by name.</p>
     *
     * @return A url-encoded URL query string
     */
    private String parametersToQueryString() {
        final StringBuilder queryString = new StringBuilder();
        final List<Map.Entry<String, String>> params = new ArrayList<Map.Entry<String, String>>(parameters.entrySet());
        for (int index = 0; index < params.size(); index++) {
            if (index > 0) {
                queryString.append('&');
            }
            final String name = params.get(index).getKey();
            final String value = params.get(index).getValue();
            queryString.append(name).append('=').append(value);
        }
        return queryString.toString();
    }

    /**
     * <p>This helper method downloads the body of an HTTP response (e.g. a clob of JSON text) as binary data.</p>
     *
     * @param connection An active HTTP connection
     * @return The body payload, downloaded from the HTTP connection response
     */
    private byte[] responseBodyBytes(final URLConnection connection) {
        try {
            final InputStream inputStream = connection.getInputStream();
            final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            int bytesRead;
            final byte[] bytes = new byte[16384];
            while ((bytesRead = inputStream.read(bytes, 0, bytes.length)) != -1) {
                byteArrayOutputStream.write(bytes, 0, bytesRead);
            }
            byteArrayOutputStream.flush();
            return byteArrayOutputStream.toByteArray();
        } catch (IOException e) {
            return new byte[0];
        }
    }

}

