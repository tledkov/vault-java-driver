package io.github.jopenlibs.vault.api;

import io.github.jopenlibs.vault.Vault;
import io.github.jopenlibs.vault.VaultConfig;
import io.github.jopenlibs.vault.VaultException;
import io.github.jopenlibs.vault.json.Json;
import io.github.jopenlibs.vault.json.JsonObject;
import io.github.jopenlibs.vault.response.AuthResponse;
import io.github.jopenlibs.vault.response.LogicalResponse;
import io.github.jopenlibs.vault.response.LookupResponse;
import io.github.jopenlibs.vault.response.UnwrapResponse;
import io.github.jopenlibs.vault.response.WrapResponse;
import io.github.jopenlibs.vault.rest.Rest;
import io.github.jopenlibs.vault.rest.RestResponse;
import java.io.Serializable;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;


/**
 * <p>The implementing class for operations on Vault's <code>/v1/auth/*</code> REST endpoints.</p>
 *
 * <p>This class is not intended to be constructed directly.  Rather, it is meant to used by way of
 * <code>Vault</code>
 * in a DSL-style builder pattern.  See the Javadoc comments of each <code>public</code> method for
 * usage examples.</p>
 *
 * @see Vault#auth()
 */
public class Auth extends OperationsBase {

    /**
     * <p>A container for all of the options that can be passed to the createToken(TokenRequest)
     * method, to avoid that method having an excessive number of parameters (with <code>null</code>
     * typically passed to most of them).</p>
     *
     * <p>All properties here are optional.  Use of this class resembles a builder pattern (i.e.
     * call the mutator method for each property you wish to set), but this class lacks a final
     * <code>build()</code> method as no post-initialization logic is necessary.</p>
     */
    public static class TokenRequest implements Serializable {

        private UUID id;
        private List<String> polices;
        private Map<String, String> meta;
        private Boolean noParent;
        private Boolean noDefaultPolicy;
        private String ttl;
        private String displayName;
        private Long numUses;
        private String role;
        private Boolean renewable;
        private String type;
        private String explicitMaxTtl;
        private String period;
        private String entityAlias;

        /**
         * @param id (optional) The ID of the client token. Can only be specified by a root token.
         * Otherwise, the token ID is a randomly generated UUID.
         * @return This object, with its id field populated
         */
        public TokenRequest id(final UUID id) {
            this.id = id;
            return this;
        }

        /**
         * @param polices (optional) A list of policies for the token. This must be a subset of the
         * policies belonging to the token
         * @return This object, with its polices field populated
         */
        public TokenRequest polices(final List<String> polices) {
            this.polices = polices;
            return this;
        }

        /**
         * @param meta (optional) A map of string to string valued metadata. This is passed through
         * to the audit backends.
         * @return This object, with its meta field populated
         */
        public TokenRequest meta(final Map<String, String> meta) {
            this.meta = meta;
            return this;
        }

        /**
         * @param noParent (optional) If true and set by a root caller, the token will not have the
         * parent token of the caller. This creates a token with no parent.
         * @return This object, with its noParent field populated
         */
        public TokenRequest noParent(final Boolean noParent) {
            this.noParent = noParent;
            return this;
        }

        /**
         * @param noDefaultPolicy (optional) If <code>true</code> the default policy will not be a
         * part of this token's policy set.
         * @return This object, with its noDefaultPolicy field populated
         */
        public TokenRequest noDefaultPolicy(final Boolean noDefaultPolicy) {
            this.noDefaultPolicy = noDefaultPolicy;
            return this;
        }

        /**
         * @param ttl (optional) The TTL period of the token, provided as "1h", where hour is the
         * largest suffix. If not provided, the token is valid for the default lease TTL, or
         * indefinitely if the root policy is used.
         * @return This object, with its ttl field populated
         */
        public TokenRequest ttl(final String ttl) {
            this.ttl = ttl;
            return this;
        }

        /**
         * @param displayName (optional) The display name of the token. Defaults to "token".
         * @return This object, with its displayName field populated
         */
        public TokenRequest displayName(final String displayName) {
            this.displayName = displayName;
            return this;
        }

        /**
         * @param numUses (optional) The maximum uses for the given token. This can be used to
         * create a one-time-token or limited use token. Defaults to 0, which has no limit to the
         * number of uses.
         * @return This object, with its numUses field populated
         */
        public TokenRequest numUses(final Long numUses) {
            this.numUses = numUses;
            return this;
        }

        /**
         * @param role (optional) The role the token will be created with. Default is no role.
         * @return This object, with its role field populated
         */
        public TokenRequest role(final String role) {
            this.role = role;
            return this;
        }

        /**
         * @param renewable Set to false to disable the ability of the token to be renewed past its
         * initial TTL. Setting the value to true will allow the token to be renewable up to the
         * system/mount maximum TTL.
         * @return This object, with its renewable field populated
         */
        public TokenRequest renewable(final Boolean renewable) {
            this.renewable = renewable;
            return this;
        }

        /**
         * @param type The token type. Can be "batch" or "service".
         * @return This object, with its type field populated
         */
        public TokenRequest type(final String type) {
            this.type = type;
            return this;
        }

        /**
         * @param explicitMaxTtl If set, the token will have an explicit max TTL set upon it.
         * @return This object, with its explicitMaxTtl field populated
         */
        public TokenRequest explicitMaxTtl(final String explicitMaxTtl) {
            this.explicitMaxTtl = explicitMaxTtl;
            return this;
        }

        /**
         * @param period If specified, the token will be periodic
         * @return This object, with its period field populated
         */
        public TokenRequest period(final String period) {
            this.period = period;
            return this;
        }

        /**
         * @param entityAlias Name of the entity alias to associate with during token creation.
         * @return This object, with its period field populated
         */
        public TokenRequest entityAlias(final String entityAlias) {
            this.entityAlias = entityAlias;
            return this;
        }

        public UUID getId() {
            return id;
        }

        public List<String> getPolices() {
            return polices;
        }

        public Map<String, String> getMeta() {
            return meta;
        }

        public Boolean getNoParent() {
            return noParent;
        }

        public Boolean getNoDefaultPolicy() {
            return noDefaultPolicy;
        }

        public String getTtl() {
            return ttl;
        }

        public String getDisplayName() {
            return displayName;
        }

        public Long getNumUses() {
            return numUses;
        }

        public String getRole() {
            return role;
        }

        public Boolean getRenewable() {
            return renewable;
        }

        public String getType() {
            return type;
        }

        public String getExplicitMaxTtl() {
            return explicitMaxTtl;
        }

        public String getPeriod() {
            return period;
        }

        public String getEntityAlias() {
            return entityAlias;
        }
    }

    private String nameSpace;

    public Auth(final VaultConfig config) {
        super(config);

        if (this.config.getNameSpace() != null && !this.config.getNameSpace().isEmpty()) {
            this.nameSpace = this.config.getNameSpace();
        }
    }

    public Auth withNameSpace(final String nameSpace) {
        this.nameSpace = nameSpace;
        return this;
    }

    /**
     * <p>Operation to create an authentication token.  Relies on another token already being
     * present in the <code>VaultConfig</code> instance.  Example usage:</p>
     *
     * <blockquote>
     * <pre>{@code
     * final VaultConfig config = new VaultConfig().address(...).token(...).build();
     * final Vault vault = new Vault(config);
     * final AuthResponse response = vault.auth().createToken(new TokenRequest().withTtl("1h"));
     *
     * final String token = response.getAuthClientToken();
     * }</pre>
     * </blockquote>
     *
     * @param tokenRequest A container of optional configuration parameters
     * @return The auth token, with additional response metadata
     * @throws VaultException If any error occurs, or unexpected response received from Vault
     */
    public AuthResponse createToken(final TokenRequest tokenRequest) throws VaultException {
        return createToken(tokenRequest, "token");
    }

    /**
     * <p>Operation to create an authentication token.  Relies on another token already being
     * present in the <code>VaultConfig</code> instance.  Example usage:</p>
     *
     * <blockquote>
     * <pre>{@code
     * final VaultConfig config = new VaultConfig().address(...).token(...).build();
     * final Vault vault = new Vault(config);
     * final AuthResponse response = vault.auth().createToken(new TokenRequest().withTtl("1h"));
     *
     * final String token = response.getAuthClientToken();
     * }</pre>
     * </blockquote>
     *
     * @param tokenRequest A container of optional configuration parameters
     * @param tokenAuthMount The mount name of the token authentication back end.  If null, defaults
     * to "token"
     * @return The auth token, with additional response metadata
     * @throws VaultException If any error occurs, or unexpected response received from Vault
     */
    public AuthResponse createToken(final TokenRequest tokenRequest, final String tokenAuthMount)
            throws VaultException {
        final String mount = tokenAuthMount != null ? tokenAuthMount : "token";

        return retry((attempt) -> {
            // Parse parameters to JSON
            final JsonObject jsonObject = Json.object();

            if (tokenRequest.getId() != null) {
                jsonObject.add("id", tokenRequest.getId().toString());
            }
            if (tokenRequest.getPolices() != null && !tokenRequest.getPolices().isEmpty()) {
                jsonObject.add(
                        "policies",
                        Json.array(tokenRequest.getPolices().toArray(new String[0]))
                ); //NOPMD
            }

            if (tokenRequest.getMeta() != null && !tokenRequest.getMeta().isEmpty()) {
                final JsonObject metaMap = Json.object();
                for (final Map.Entry<String, String> entry : tokenRequest.getMeta().entrySet()) {
                    metaMap.add(entry.getKey(), entry.getValue());
                }
                jsonObject.add("meta", metaMap);
            }
            if (tokenRequest.getNoParent() != null) {
                jsonObject.add("no_parent", tokenRequest.getNoParent());
            }
            if (tokenRequest.getNoDefaultPolicy() != null) {
                jsonObject.add("no_default_policy", tokenRequest.getNoDefaultPolicy());
            }
            if (tokenRequest.getTtl() != null) {
                jsonObject.add("ttl", tokenRequest.getTtl());
            }
            if (tokenRequest.getDisplayName() != null) {
                jsonObject.add("display_name", tokenRequest.getDisplayName());
            }
            if (tokenRequest.getNumUses() != null) {
                jsonObject.add("num_uses", tokenRequest.getNumUses());
            }
            if (tokenRequest.getRenewable() != null) {
                jsonObject.add("renewable", tokenRequest.getRenewable());
            }
            if (tokenRequest.getType() != null) {
                jsonObject.add("type", tokenRequest.getType());
            }
            if (tokenRequest.getExplicitMaxTtl() != null) {
                jsonObject.add("explicit_max_ttl", tokenRequest.getExplicitMaxTtl());
            }
            if (tokenRequest.getPeriod() != null) {
                jsonObject.add("period", tokenRequest.getPeriod());
            }
            if (tokenRequest.getEntityAlias() != null) {
                jsonObject.add("entity_alias", tokenRequest.getEntityAlias());
            }

            final String requestJson = jsonObject.toString();

            final StringBuilder urlBuilder = new StringBuilder(config.getAddress())//NOPMD
                    .append("/v1/auth/")
                    .append(mount)
                    .append("/create");
            if (tokenRequest.getRole() != null) {
                urlBuilder.append("/").append(tokenRequest.getRole());
            }
            final String url = urlBuilder.toString();

            // HTTP request to Vault
            final RestResponse restResponse = new Rest()//NOPMD
                    .url(url)
                    .header("X-Vault-Token", config.getToken())
                    .header("X-Vault-Namespace", this.nameSpace)
                    .body(requestJson.getBytes(StandardCharsets.UTF_8))
                    .connectTimeoutSeconds(config.getOpenTimeout())
                    .readTimeoutSeconds(config.getReadTimeout())
                    .sslVerification(config.getSslConfig().isVerify())
                    .sslContext(config.getSslConfig().getSslContext())
                    .post();

            // Validate restResponse
            if (restResponse.getStatus() != 200) {
                throw new VaultException(
                        "Vault responded with HTTP status code: " + restResponse.getStatus()
                                + "\nResponse body: " + new String(restResponse.getBody(),
                                StandardCharsets.UTF_8),
                        restResponse.getStatus());
            }

            final String mimeType =
                    restResponse.getMimeType() == null ? "null" : restResponse.getMimeType();
            if (!mimeType.equals("application/json")) {
                throw new VaultException("Vault responded with MIME type: " + mimeType,
                        restResponse.getStatus());
            }

            return new AuthResponse(restResponse, attempt);
        });
    }

    /**
     * <p>Basic login operation to authenticate to an app-id backend.  Example usage:</p>
     *
     * <blockquote>
     * <pre>{@code
     * final AuthResponse response = vault.auth().loginByAppID("app-id/login", "app_id", "user_id");
     *
     * final String token = response.getAuthClientToken();
     * }</pre>
     * </blockquote>
     *
     * <strong>NOTE: </strong> As of Vault 0.6.1, Hashicorp has deprecated the App ID
     * authentication backend in favor of AppRole.  This method will be removed at some point after
     * this backend has been eliminated from Vault.
     *
     * @param path The path on which the authentication is performed (e.g.
     * <code>auth/app-id/login</code>)
     * @param appId The app-id used for authentication
     * @param userId The user-id used for authentication
     * @return The auth token, with additional response metadata
     * @throws VaultException If any error occurs, or unexpected response received from Vault
     */
    @Deprecated
    public AuthResponse loginByAppID(final String path, final String appId, final String userId)
            throws VaultException {
        return retry((attempt) -> {
            // HTTP request to Vault
            final String requestJson = Json.object().add("app_id", appId).add("user_id", userId)
                    .toString();
            final RestResponse restResponse = new Rest()//NOPMD
                    .url(config.getAddress() + "/v1/auth/" + path)
                    .optionalHeader("X-Vault-Namespace", this.nameSpace)
                    .body(requestJson.getBytes(StandardCharsets.UTF_8))
                    .connectTimeoutSeconds(config.getOpenTimeout())
                    .readTimeoutSeconds(config.getReadTimeout())
                    .sslVerification(config.getSslConfig().isVerify())
                    .sslContext(config.getSslConfig().getSslContext())
                    .post();

            // Validate restResponse
            if (restResponse.getStatus() != 200) {
                throw new VaultException(
                        "Vault responded with HTTP status code: " + restResponse.getStatus()
                                + "\nResponse body: " + new String(restResponse.getBody(),
                                StandardCharsets.UTF_8),
                        restResponse.getStatus());
            }

            final String mimeType =
                    restResponse.getMimeType() == null ? "null" : restResponse.getMimeType();
            if (!mimeType.equals("application/json")) {
                throw new VaultException("Vault responded with MIME type: " + mimeType,
                        restResponse.getStatus());
            }

            return new AuthResponse(restResponse, attempt);
        });
    }

    /**
     * <p>Basic login operation to authenticate to an app-role backend.  This version of the
     * overloaded method assumes that the auth backend is mounted on the default path (i.e.
     * "/v1/auth/approle").  Example usage:</p>
     *
     * <blockquote>
     * <pre>{@code
     * final AuthResponse response = vault.auth().loginByAppRole(9e1aede8-dcc6-a293-8223-f0d824a467ed", "9ff4b26e-6460-834c-b925-a940eddb6880");
     *
     * final String token = response.getAuthClientToken();
     * }</pre>
     * </blockquote>
     *
     * @param roleId The role-id used for authentication
     * @param secretId The secret-id used for authentication
     * @return The auth token, with additional response metadata
     * @throws VaultException If any error occurs, or unexpected response received from Vault
     */
    public AuthResponse loginByAppRole(final String roleId, final String secretId)
            throws VaultException {
        return loginByAppRole("approle", roleId, secretId);
    }

    /**
     * <p>Basic login operation to authenticate to an app-role backend.  This version of the
     * overloaded method requires you to explicitly specify the path on which the auth backend is
     * mounted, following the "/v1/auth/" prefix.  Example usage:</p>
     *
     * <blockquote>
     * <pre>{@code
     * final AuthResponse response = vault.auth().loginByAppRole("approle", "9e1aede8-dcc6-a293-8223-f0d824a467ed", "9ff4b26e-6460-834c-b925-a940eddb6880");
     *
     * final String token = response.getAuthClientToken();
     * }</pre>
     * </blockquote>
     * <p>
     * NOTE:  I hate that this method takes the custom mount path as its first parameter, while all
     * of the other methods in this class take it as the last parameter (a better practice).  I just
     * didn't think about it during code review.  Now it's difficult to deprecate this, since a
     * version of the method with path as the final parameter would have the same method signature.
     * <p>
     * I may or may not change this in some future breaking-change major release, especially if we
     * keep adding similar overloaded methods elsewhere and need the global consistency.  At any
     * rate, going forward no new methods should take a custom path as the first parameter.
     *
     * @param path The path on which the authentication is performed, following the "/v1/auth/"
     * prefix (e.g. "approle")
     * @param roleId The role-id used for authentication
     * @param secretId The secret-id used for authentication
     * @return The auth token, with additional response metadata
     * @throws VaultException If any error occurs, or unexpected response received from Vault
     */
    public AuthResponse loginByAppRole(final String path, final String roleId,
            final String secretId) throws VaultException {
        return retry(attempt -> {
            // HTTP request to Vault
            final String requestJson = Json.object().add("role_id", roleId)
                    .add("secret_id", secretId).toString();
            final RestResponse restResponse = new Rest()//NOPMD
                    .url(config.getAddress() + "/v1/auth/" + path + "/login")
                    .header("X-Vault-Namespace", this.nameSpace)
                    .body(requestJson.getBytes(StandardCharsets.UTF_8))
                    .connectTimeoutSeconds(config.getOpenTimeout())
                    .readTimeoutSeconds(config.getReadTimeout())
                    .sslVerification(config.getSslConfig().isVerify())
                    .sslContext(config.getSslConfig().getSslContext())
                    .post();

            // Validate restResponse
            if (restResponse.getStatus() != 200) {
                throw new VaultException(
                        "Vault responded with HTTP status code: " + restResponse.getStatus()
                                + "\nResponse body: " + new String(restResponse.getBody(),
                                StandardCharsets.UTF_8),
                        restResponse.getStatus());
            }
            final String mimeType =
                    restResponse.getMimeType() == null ? "null" : restResponse.getMimeType();
            if (!mimeType.equals("application/json")) {
                throw new VaultException("Vault responded with MIME type: " + mimeType,
                        restResponse.getStatus());
            }
            return new AuthResponse(restResponse, attempt);
        });
    }

    /**
     * <p>Basic login operation to authenticate to a Username &amp; Password backend.  Example
     * usage:</p>
     *
     * <blockquote>
     * <pre>{@code
     * final AuthResponse response = vault.auth().loginByUserPass("test", "password");
     *
     * final String token = response.getAuthClientToken();
     * }</pre>
     * </blockquote>
     *
     * @param username The username used for authentication
     * @param password The password used for authentication
     * @return The auth token, with additional response metadata
     * @throws VaultException If any error occurs, or unexpected response received from Vault
     */
    public AuthResponse loginByUserPass(final String username, final String password)
            throws VaultException {
        return loginByUserPass(username, password, "userpass");
    }

    /**
     * <p>Basic login operation to authenticate to a Username &amp; Password backend.  Example
     * usage:</p>
     *
     * <blockquote>
     * <pre>{@code
     * final AuthResponse response = vault.auth().loginByUserPass("test", "password");
     *
     * final String token = response.getAuthClientToken();
     * }</pre>
     * </blockquote>
     *
     * @param username The username used for authentication
     * @param password The password used for authentication
     * @param userpassAuthMount The mount name of the userpass authentication back end.  If null,
     * defaults to "userpass"
     * @return The auth token, with additional response metadata
     * @throws VaultException If any error occurs, or unexpected response received from Vault
     */
    public AuthResponse loginByUserPass(final String username, final String password,
            final String userpassAuthMount) throws VaultException {
        final String mount = userpassAuthMount != null ? userpassAuthMount : "userpass";

        return retry(attempt -> {
            // HTTP request to Vault
            final String requestJson = Json.object().add("password", password).toString();
            final RestResponse restResponse = new Rest()//NOPMD
                    .url(config.getAddress() + "/v1/auth/" + mount + "/login/" + username)
                    .header("X-Vault-Namespace", this.nameSpace)
                    .body(requestJson.getBytes(StandardCharsets.UTF_8))
                    .connectTimeoutSeconds(config.getOpenTimeout())
                    .readTimeoutSeconds(config.getReadTimeout())
                    .sslVerification(config.getSslConfig().isVerify())
                    .sslContext(config.getSslConfig().getSslContext())
                    .post();

            // Validate restResponse
            if (restResponse.getStatus() != 200) {
                throw new VaultException(
                        "Vault responded with HTTP status code: " + restResponse.getStatus()
                                + "\nResponse body: " + new String(restResponse.getBody(),
                                StandardCharsets.UTF_8),
                        restResponse.getStatus());
            }
            final String mimeType =
                    restResponse.getMimeType() == null ? "null" : restResponse.getMimeType();
            if (!mimeType.equals("application/json")) {
                throw new VaultException("Vault responded with MIME type: " + mimeType,
                        restResponse.getStatus());
            }
            return new AuthResponse(restResponse, attempt);
        });
    }

    /**
     * <p>Basic login operation to authenticate to a LDAP backend.  Example usage:</p>
     *
     * <blockquote>
     * <pre>{@code
     * final AuthResponse response = vault.auth().loginByLDAP("test", "password");
     *
     * final String token = response.getAuthClientToken();
     * }</pre>
     * </blockquote>
     *
     * @param username The username used for authentication
     * @param password The password used for authentication
     * @return The auth token, with additional response metadata
     * @throws VaultException If any error occurs, or unexpected response received from Vault
     */
    public AuthResponse loginByLDAP(final String username, final String password)
            throws VaultException {
        // TODO:  Determine a way to feasibly add integration test coverage
        return loginByLDAP(username, password, "ldap");
    }

    /**
     * <p>Basic login operation to authenticate to a LDAP backend.  Example usage:</p>
     *
     * <blockquote>
     * <pre>{@code
     * final AuthResponse response = vault.auth().loginByLDAP("test", "password");
     *
     * final String token = response.getAuthClientToken();
     * }</pre>
     * </blockquote>
     *
     * @param username The username used for authentication
     * @param password The password used for authentication
     * @param ldapAuthMount The mount name of the ldap authentication back end.  If null, defaults
     * to "ldap"
     * @return The auth token, with additional response metadata
     * @throws VaultException If any error occurs, or unexpected response received from Vault
     */
    public AuthResponse loginByLDAP(final String username, final String password,
            final String ldapAuthMount) throws VaultException {
        final String mount = ldapAuthMount != null ? ldapAuthMount : "ldap";
        // LDAP has the same API like Username & Password backend
        return loginByUserPass(username, password, mount);
    }

    /**
     * <p>Basic login operation to authenticate to a AWS backend using EC2 authentication.  Example
     * usage:</p>
     *
     * <blockquote>
     * <pre>{@code
     * final AuthResponse response = vault.auth().loginByAwsEc2("my-role", "identity", "signature", "nonce", null);
     *
     * final String token = response.getAuthClientToken();
     * }</pre>
     * </blockquote>
     *
     * @param role Name of the role against which the login is being attempted. If role is not
     * specified, then the login endpoint looks for a role bearing the name of the AMI ID of the EC2
     * instance that is trying to login if using the ec2 auth method, or the "friendly name" (i.e.,
     * role name or username) of the IAM principal authenticated. If a matching role is not found,
     * login fails.
     * @param identity Base64 encoded EC2 instance identity document.
     * @param signature Base64 encoded SHA256 RSA signature of the instance identity document.
     * @param nonce Client nonce used for authentication. If null, a new nonce will be generated by
     * Vault
     * @param awsAuthMount AWS auth mount
     * @return The auth token, with additional response metadata
     * @throws VaultException If any error occurs, or unexpected response received from Vault
     */
    // TODO: Needs integration test coverage if possible
    public AuthResponse loginByAwsEc2(final String role, final String identity,
            final String signature, final String nonce, final String awsAuthMount)
            throws VaultException {
        final String mount = awsAuthMount != null ? awsAuthMount : "aws";

        return retry(attempt -> {
            // HTTP request to Vault
            final JsonObject request = Json.object().add("identity", identity)
                    .add("signature", signature);
            if (role != null) {
                request.add("role", role);
            }
            if (nonce != null) {
                request.add("nonce", nonce);
            }
            final String requestJson = request.toString();

            final RestResponse restResponse = new Rest()//NOPMD
                    .url(config.getAddress() + "/v1/auth/" + mount + "/login")
                    .body(requestJson.getBytes(StandardCharsets.UTF_8))
                    .header("X-Vault-Namespace", this.nameSpace)
                    .connectTimeoutSeconds(config.getOpenTimeout())
                    .readTimeoutSeconds(config.getReadTimeout())
                    .sslVerification(config.getSslConfig().isVerify())
                    .sslContext(config.getSslConfig().getSslContext())
                    .post();

            // Validate restResponse
            if (restResponse.getStatus() != 200) {
                throw new VaultException(
                        "Vault responded with HTTP status code: " + restResponse.getStatus()
                                + "\nResponse body: " + new String(restResponse.getBody(),
                                StandardCharsets.UTF_8),
                        restResponse.getStatus());
            }
            final String mimeType =
                    restResponse.getMimeType() == null ? "null" : restResponse.getMimeType();
            if (!mimeType.equals("application/json")) {
                throw new VaultException("Vault responded with MIME type: " + mimeType,
                        restResponse.getStatus());
            }
            return new AuthResponse(restResponse, attempt);
        });
    }

    /**
     * <p>Basic login operation to authenticate to a AWS backend using EC2 authentication.  Example
     * usage:</p>
     *
     * <blockquote>
     * <pre>{@code
     * final AuthResponse response = vault.auth().loginByAwsEc2("my-role", "pkcs7", "nonce", null);
     *
     * final String token = response.getAuthClientToken();
     * }</pre>
     * </blockquote>
     *
     * @param role Name of the role against which the login is being attempted. If role is not
     * specified, then the login endpoint looks for a role bearing the name of the AMI ID of the EC2
     * instance that is trying to login if using the ec2 auth method, or the "friendly name" (i.e.,
     * role name or username) of the IAM principal authenticated. If a matching role is not found,
     * login fails.
     * @param pkcs7 PKCS7 signature of the identity document with all \n characters removed.
     * @param nonce Client nonce used for authentication. If null, a new nonce will be generated by
     * Vault
     * @param awsAuthMount AWS auth mount
     * @return The auth token, with additional response metadata
     * @throws VaultException If any error occurs, or unexpected response received from Vault
     */
    // TODO: Needs integration test coverage if possible
    public AuthResponse loginByAwsEc2(final String role, final String pkcs7, final String nonce,
            final String awsAuthMount) throws VaultException {
        final String mount = awsAuthMount != null ? awsAuthMount : "aws";

        return retry(attempt -> {
            // HTTP request to Vault
            final JsonObject request = Json.object().add("pkcs7", pkcs7);
            if (role != null) {
                request.add("role", role);
            }
            if (nonce != null) {
                request.add("nonce", nonce);
            }
            final String requestJson = request.toString();
            final RestResponse restResponse = new Rest()//NOPMD
                    .url(config.getAddress() + "/v1/auth/" + mount + "/login")
                    .header("X-Vault-Namespace", this.nameSpace)
                    .body(requestJson.getBytes(StandardCharsets.UTF_8))
                    .connectTimeoutSeconds(config.getOpenTimeout())
                    .readTimeoutSeconds(config.getReadTimeout())
                    .sslVerification(config.getSslConfig().isVerify())
                    .sslContext(config.getSslConfig().getSslContext())
                    .post();

            // Validate restResponse
            if (restResponse.getStatus() != 200) {
                throw new VaultException(
                        "Vault responded with HTTP status code: " + restResponse.getStatus()
                                + "\nResponse body: " + new String(restResponse.getBody(),
                                StandardCharsets.UTF_8),
                        restResponse.getStatus());
            }

            final String mimeType =
                    restResponse.getMimeType() == null ? "null" : restResponse.getMimeType();

            if (!mimeType.equals("application/json")) {
                throw new VaultException("Vault responded with MIME type: " + mimeType,
                        restResponse.getStatus());
            }

            return new AuthResponse(restResponse, attempt);
        });
    }

    /**
     * <p>Basic login operation to authenticate to a AWS backend using IAM authentication.  Example
     * usage:</p>
     *
     * <blockquote>
     * <pre>{@code
     * final AuthResponse response = vault.auth().loginByAwsIam("my-role", "pkcs7", "nonce", null);
     *
     * final String token = response.getAuthClientToken();
     * }</pre>
     * </blockquote>
     *
     * @param role Name of the role against which the login is being attempted. If role is not
     * specified, then the login endpoint looks for a role bearing the name of the AMI ID of the EC2
     * instance that is trying to login if using the ec2 auth method, or the "friendly name" (i.e.,
     * role name or username) of the IAM principal authenticated. If a matching role is not found,
     * login fails.
     * @param iamRequestUrl PKCS7 signature of the identity document with all \n characters
     * removed.Base64-encoded HTTP URL used in the signed request. Most likely just
     * aHR0cHM6Ly9zdHMuYW1hem9uYXdzLmNvbS8= (base64-encoding of https://sts.amazonaws.com/) as most
     * requests will probably use POST with an empty URI.
     * @param iamRequestBody Base64-encoded body of the signed request. Most likely
     * QWN0aW9uPUdldENhbGxlcklkZW50aXR5JlZlcnNpb249MjAxMS0wNi0xNQ== which is the base64 encoding of
     * Action=GetCallerIdentity&amp;Version=2011-06-15.
     * @param iamRequestHeaders Request headers
     * @param awsAuthMount AWS auth mount
     * @return The auth token, with additional response metadata
     * @throws VaultException If any error occurs, or unexpected response received from Vault
     */
    public AuthResponse loginByAwsIam(final String role, final String iamRequestUrl,
            final String iamRequestBody, final String iamRequestHeaders, final String awsAuthMount)
            throws VaultException {
        final String mount = awsAuthMount != null ? awsAuthMount : "aws";

        return retry(attempt -> {
            // HTTP request to Vault
            final JsonObject request = Json.object().add("iam_request_url", iamRequestUrl)
                    .add("iam_request_body", iamRequestBody)
                    .add("iam_request_headers", iamRequestHeaders)
                    .add("iam_http_request_method", "POST");
            if (role != null) {
                request.add("role", role);
            }
            final String requestJson = request.toString();
            final RestResponse restResponse = new Rest()//NOPMD
                    .url(config.getAddress() + "/v1/auth/" + mount + "/login")
                    .header("X-Vault-Namespace", this.nameSpace)
                    .body(requestJson.getBytes(StandardCharsets.UTF_8))
                    .connectTimeoutSeconds(config.getOpenTimeout())
                    .readTimeoutSeconds(config.getReadTimeout())
                    .sslVerification(config.getSslConfig().isVerify())
                    .sslContext(config.getSslConfig().getSslContext())
                    .post();

            // Validate restResponse
            if (restResponse.getStatus() != 200) {
                throw new VaultException(
                        "Vault responded with HTTP status code: " + restResponse.getStatus()
                                + "\nResponse body: " + new String(restResponse.getBody(),
                                StandardCharsets.UTF_8),
                        restResponse.getStatus());
            }
            final String mimeType =
                    restResponse.getMimeType() == null ? "null" : restResponse.getMimeType();
            if (!mimeType.equals("application/json")) {
                throw new VaultException("Vault responded with MIME type: " + mimeType,
                        restResponse.getStatus());
            }
            return new AuthResponse(restResponse, attempt);
        });
    }

    /**
     * <p>Basic login operation to authenticate to an github backend.  Example usage:</p>
     *
     * <blockquote>
     * <pre>{@code
     * final AuthResponse response = vault.auth().loginByGithub("githubToken");
     *
     * final String token = response.getAuthClientToken();
     * }</pre>
     * </blockquote>
     *
     * @param githubToken The app-id used for authentication
     * @return The auth token, with additional response metadata
     * @throws VaultException If any error occurs, or unexpected response received from Vault
     */
    public AuthResponse loginByGithub(final String githubToken) throws VaultException {
        return loginByGithub(githubToken, "github");
    }

    /**
     * <p>Basic login operation to authenticate to an github backend.  Example usage:</p>
     *
     * <blockquote>
     * <pre>{@code
     * final AuthResponse response = vault.auth().loginByGithub("githubToken");
     *
     * final String token = response.getAuthClientToken();
     * }</pre>
     * </blockquote>
     *
     * @param githubToken The app-id used for authentication
     * @param githubAuthMount The mount name of the github authentication back end.  If null,
     * defaults to "github"
     * @return The auth token, with additional response metadata
     * @throws VaultException If any error occurs, or unexpected response received from Vault
     */
    public AuthResponse loginByGithub(final String githubToken, final String githubAuthMount)
            throws VaultException {
        // TODO:  Add (optional?) integration test coverage
        final String mount = githubAuthMount != null ? githubAuthMount : "github";

        return retry(attempt -> {
            // HTTP request to Vault
            final String requestJson = Json.object().add("token", githubToken).toString();
            final RestResponse restResponse = new Rest()//NOPMD
                    .url(config.getAddress() + "/v1/auth/" + mount + "/login")
                    .header("X-Vault-Namespace", this.nameSpace)
                    .body(requestJson.getBytes(StandardCharsets.UTF_8))
                    .connectTimeoutSeconds(config.getOpenTimeout())
                    .readTimeoutSeconds(config.getReadTimeout())
                    .sslVerification(config.getSslConfig().isVerify())
                    .sslContext(config.getSslConfig().getSslContext())
                    .post();

            // Validate restResponse
            if (restResponse.getStatus() != 200) {
                throw new VaultException(
                        "Vault responded with HTTP status code: " + restResponse.getStatus()
                                + "\nResponse body: " + new String(restResponse.getBody(),
                                StandardCharsets.UTF_8),
                        restResponse.getStatus());
            }
            final String mimeType =
                    restResponse.getMimeType() == null ? "null" : restResponse.getMimeType();
            if (!mimeType.equals("application/json")) {
                throw new VaultException("Vault responded with MIME type: " + mimeType,
                        restResponse.getStatus());
            }
            return new AuthResponse(restResponse, attempt);
        });
    }

    /**
     * <p>Basic login operation to authenticate to an JWT backend.  Example usage:</p>
     *
     * <blockquote>
     * <pre>{@code
     * final AuthResponse response = vault.auth().loginByJwt("kubernetes", "dev", "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...");
     *
     * final String token = response.getAuthClientToken();
     * }</pre>
     * </blockquote>
     *
     * @param provider Provider of JWT token.
     * @param role The gcp role used for authentication
     * @param jwt The JWT token for the role
     * @return The auth token, with additional response metadata
     * @throws VaultException If any error occurs, or unexpected response received from Vault
     */
    // TODO: Needs integration test coverage if possible
    public AuthResponse loginByJwt(final String provider, final String role, final String jwt)
            throws VaultException {
        return retry(attempt -> {
            // HTTP request to Vault
            final String requestJson = Json.object().add("role", role).add("jwt", jwt)
                    .toString();
            final RestResponse restResponse = new Rest()
                    .url(config.getAddress() + "/v1/auth/" + provider + "/login")
                    .header("X-Vault-Namespace", this.nameSpace)
                    .body(requestJson.getBytes(StandardCharsets.UTF_8))
                    .connectTimeoutSeconds(config.getOpenTimeout())
                    .readTimeoutSeconds(config.getReadTimeout())
                    .sslVerification(config.getSslConfig().isVerify())
                    .sslContext(config.getSslConfig().getSslContext())
                    .post();

            // Validate restResponse
            if (restResponse.getStatus() != 200) {
                throw new VaultException(
                        "Vault responded with HTTP status code: " + restResponse.getStatus()
                                + "\nResponse body: " + new String(restResponse.getBody(),
                                StandardCharsets.UTF_8),
                        restResponse.getStatus());
            }
            final String mimeType =
                    restResponse.getMimeType() == null ? "null" : restResponse.getMimeType();
            if (!mimeType.equals("application/json")) {
                throw new VaultException("Vault responded with MIME type: " + mimeType,
                        restResponse.getStatus());
            }
            return new AuthResponse(restResponse, attempt);
        });
    }

    /**
     * <p>Basic login operation to authenticate to an GCP backend.  Example usage:</p>
     *
     * <blockquote>
     * <pre>{@code
     * final AuthResponse response = vault.auth().loginByGCP("dev", "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...");
     *
     * final String token = response.getAuthClientToken();
     * }</pre>
     * </blockquote>
     *
     * @param role The gcp role used for authentication
     * @param jwt The JWT token for the role
     * @return The auth token, with additional response metadata
     * @throws VaultException If any error occurs, or unexpected response received from Vault
     */
    public AuthResponse loginByGCP(final String role, final String jwt) throws VaultException {
        return loginByJwt("gcp", role, jwt);
    }


    /**
     * Basic login operation to authenticate to an kubernetes backend. Example usage:
     *
     * <blockquote>
     *
     * <pre>{@code
     * final AuthResponse response =
     *     vault.auth().loginByKubernetes("dev", "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...");
     *
     * final String token = response.getAuthClientToken();
     * }</pre>
     * </blockquote>
     *
     * @param role The kubernetes role used for authentication
     * @param jwt The JWT token for the role, typically read from
     * /var/run/secrets/kubernetes.io/serviceaccount/token
     * @return The auth token, with additional response metadata
     * @throws VaultException If any error occurs, or unexpected response received from Vault
     */
    public AuthResponse loginByKubernetes(final String role, final String jwt)
            throws VaultException {
        return loginByJwt("kubernetes", role, jwt);
    }

    /**
     * <p>Basic login operation to authenticate using Vault's TLS Certificate auth backend.
     * Example usage:</p>
     *
     * <blockquote>
     * <pre>{@code
     * final SslConfig sslConfig = new SslConfig()
     *                                  .keystore("keystore.jks")
     *                                  .truststore("truststore.jks")
     *                                  .build();
     * final VaultConfig vaultConfig = new VaultConfig()
     *                                  .address("https://127.0.0.1:8200")
     *                                  .sslConfig(sslConfig)
     *                                  .build();
     * final Vault vault = new Vault(vaultConfig);
     *
     * final AuthResponse response = vault.auth().loginByCert();
     * final String token = response.getAuthClientToken();
     * }</pre>
     * </blockquote>
     *
     * @return The auth token, with additional response metadata
     * @throws VaultException If any error occurs, or unexpected response received from Vault
     */
    public AuthResponse loginByCert() throws VaultException {
        return loginByCert("cert");
    }

    /**
     * <p>Basic login operation to authenticate using Vault's TLS Certificate auth backend.
     * Example usage:</p>
     *
     * <blockquote>
     * <pre>{@code
     * final SslConfig sslConfig = new SslConfig()
     *                                  .keystore("keystore.jks")
     *                                  .truststore("truststore.jks")
     *                                  .build();
     * final VaultConfig vaultConfig = new VaultConfig()
     *                                  .address("https://127.0.0.1:8200")
     *                                  .sslConfig(sslConfig)
     *                                  .build();
     * final Vault vault = new Vault(vaultConfig);
     *
     * final AuthResponse response = vault.auth().loginByCert();
     * final String token = response.getAuthClientToken();
     * }</pre>
     * </blockquote>
     *
     * @param certAuthMount The mount name of the cert authentication back end.  If null, defaults
     * to "cert"
     * @return The auth token, with additional response metadata
     * @throws VaultException If any error occurs, or unexpected response received from Vault
     */
    public AuthResponse loginByCert(final String certAuthMount) throws VaultException {
        final String mount = certAuthMount != null ? certAuthMount : "cert";

        return retry(attempt -> {
            final RestResponse restResponse = new Rest()//NOPMD
                    .url(config.getAddress() + "/v1/auth/" + mount + "/login")
                    .header("X-Vault-Namespace", this.nameSpace)
                    .connectTimeoutSeconds(config.getOpenTimeout())
                    .readTimeoutSeconds(config.getReadTimeout())
                    .sslVerification(config.getSslConfig().isVerify())
                    .sslContext(config.getSslConfig().getSslContext())
                    .post();

            // Validate restResponse
            if (restResponse.getStatus() != 200) {
                throw new VaultException(
                        "Vault responded with HTTP status code: " + restResponse.getStatus()
                                + "\nResponse body: " + new String(restResponse.getBody(),
                                StandardCharsets.UTF_8),
                        restResponse.getStatus());
            }

            final String mimeType =
                    restResponse.getMimeType() == null ? "null" : restResponse.getMimeType();
            if (!mimeType.equals("application/json")) {
                throw new VaultException("Vault responded with MIME type: " + mimeType,
                        restResponse.getStatus());
            }

            return new AuthResponse(restResponse, attempt);
        });
    }

    /**
     * <p>Renews the lease associated with the calling token.  This version of the method tells
     * Vault to use the default lifespan for the new lease.</p>
     *
     * @return The response information returned from Vault
     * @throws VaultException If any error occurs, or unexpected response received from Vault
     */
    public AuthResponse renewSelf() throws VaultException {
        return renewSelf(-1);
    }

    /**
     * <p>Renews the lease associated with the calling token.  This version of the method accepts a
     * parameter to explicitly declare how long the new lease period should be (in seconds).  The
     * Vault documentation suggests that this value may be ignored, however.</p>
     *
     * @param increment The number of seconds requested for the new lease lifespan
     * @return The response information returned from Vault
     * @throws VaultException If any error occurs, or unexpected response received from Vault
     */
    public AuthResponse renewSelf(final long increment) throws VaultException {
        return renewSelf(increment, "token");
    }

    /**
     * <p>Renews the lease associated with the calling token.  This version of the method accepts a
     * parameter to explicitly declare how long the new lease period should be (in seconds).  The
     * Vault documentation suggests that this value may be ignored, however.</p>
     *
     * @param increment The number of seconds requested for the new lease lifespan
     * @param tokenAuthMount The mount name of the token authentication back end.  If null, defaults
     * to "token"
     * @return The response information returned from Vault
     * @throws VaultException If any error occurs, or unexpected response received from Vault
     */
    public AuthResponse renewSelf(final long increment, final String tokenAuthMount)
            throws VaultException {
        final String mount = tokenAuthMount != null ? tokenAuthMount : "token";

        return retry(attempt -> {
            // HTTP request to Vault
            final String requestJson = Json.object().add("increment", increment).toString();
            final RestResponse restResponse = new Rest()//NOPMD
                    .url(config.getAddress() + "/v1/auth/" + mount + "/renew-self")
                    .header("X-Vault-Token", config.getToken())
                    .header("X-Vault-Namespace", this.nameSpace)
                    .body(increment < 0 ? null : requestJson.getBytes(StandardCharsets.UTF_8))
                    .connectTimeoutSeconds(config.getOpenTimeout())
                    .readTimeoutSeconds(config.getReadTimeout())
                    .sslVerification(config.getSslConfig().isVerify())
                    .sslContext(config.getSslConfig().getSslContext())
                    .post();

            // Validate restResponse
            if (restResponse.getStatus() != 200) {
                throw new VaultException(
                        "Vault responded with HTTP status code: " + restResponse.getStatus()
                                + "\nResponse body: " + new String(restResponse.getBody(),
                                StandardCharsets.UTF_8),
                        restResponse.getStatus());
            }

            final String mimeType =
                    restResponse.getMimeType() == null ? "null" : restResponse.getMimeType();

            if (!mimeType.equals("application/json")) {
                throw new VaultException("Vault responded with MIME type: " + mimeType,
                        restResponse.getStatus());
            }

            return new AuthResponse(restResponse, attempt);
        });
    }

    /**
     * <p>Returns information about the current client token.</p>
     *
     * @return The response information returned from Vault
     * @throws VaultException If any error occurs, or unexpected response received from Vault
     */
    public LookupResponse lookupSelf() throws VaultException {
        return lookupSelf("token");
    }

    /**
     * <p>Returns information about the current client token.</p>
     *
     * @param tokenAuthMount The mount name of the token authentication back end.  If null, defaults
     * to "token"
     * @return The response information returned from Vault
     * @throws VaultException If any error occurs, or unexpected response received from Vault
     */
    public LookupResponse lookupSelf(final String tokenAuthMount) throws VaultException {
        final String mount = tokenAuthMount != null ? tokenAuthMount : "token";

        return retry(attempt -> {
            // HTTP request to Vault
            final RestResponse restResponse = new Rest()//NOPMD
                    .url(config.getAddress() + "/v1/auth/" + mount + "/lookup-self")
                    .header("X-Vault-Token", config.getToken())
                    .header("X-Vault-Namespace", this.nameSpace)
                    .connectTimeoutSeconds(config.getOpenTimeout())
                    .readTimeoutSeconds(config.getReadTimeout())
                    .sslVerification(config.getSslConfig().isVerify())
                    .sslContext(config.getSslConfig().getSslContext())
                    .get();

            // Validate restResponse
            if (restResponse.getStatus() != 200) {
                throw new VaultException(
                        "Vault responded with HTTP status code: " + restResponse.getStatus()
                                + "\nResponse body: " + new String(restResponse.getBody(),
                                StandardCharsets.UTF_8),
                        restResponse.getStatus());
            }
            final String mimeType = restResponse.getMimeType();
            if (!"application/json".equals(mimeType)) {
                throw new VaultException("Vault responded with MIME type: " + mimeType,
                        restResponse.getStatus());
            }
            return new LookupResponse(restResponse, attempt);
        });
    }

    /**
     * <p>Returns information about the current client token for a wrapped token, for which the
     * lookup endpoint is different at "sys/wrapping/lookup". Example usage:</p>
     *
     * <blockquote>
     * <pre>{@code
     * final String wrappingToken = "...";
     * final VaultConfig config = new VaultConfig().address(...).token(wrappingToken).build();
     * final Vault vault = new Vault(config);
     * final LogicalResponse response = vault.auth().lookupWarp();
     * // Then you can validate "path" for example ...
     * final String path = response.getData().get("path");
     * }</pre>
     * </blockquote>
     *
     * @return The response information returned from Vault
     * @throws VaultException If any error occurs, or unexpected response received from Vault
     */
    public LogicalResponse lookupWrap() throws VaultException {
        return retry(attempt -> {
            // HTTP request to Vault
            final RestResponse restResponse = new Rest()//NOPMD
                    .url(config.getAddress() + "/v1/sys/wrapping/lookup")
                    .header("X-Vault-Token", config.getToken())
                    .header("X-Vault-Namespace", this.nameSpace)
                    .connectTimeoutSeconds(config.getOpenTimeout())
                    .readTimeoutSeconds(config.getReadTimeout())
                    .sslVerification(config.getSslConfig().isVerify())
                    .sslContext(config.getSslConfig().getSslContext())
                    .get();

            // Validate restResponse
            if (restResponse.getStatus() != 200) {
                throw new VaultException(
                        "Vault responded with HTTP status code: " + restResponse.getStatus(),
                        restResponse.getStatus());
            }

            final String mimeType = restResponse.getMimeType();
            if (!"application/json".equals(mimeType)) {
                throw new VaultException("Vault responded with MIME type: " + mimeType,
                        restResponse.getStatus());
            }

            return new LogicalResponse(restResponse, attempt,
                    Logical.logicalOperations.authentication);
        });
    }

    /**
     * <p>Revokes current client token.</p>
     *
     * @throws VaultException If any error occurs, or unexpected response received from Vault
     */
    public void revokeSelf() throws VaultException {
        revokeSelf("token");
    }

    /**
     * <p>Revokes current client token.</p>
     *
     * @param tokenAuthMount The mount name of the token authentication back end.  If null, defaults
     * to "token"
     * @throws VaultException If any error occurs, or unexpected response received from Vault
     */
    public void revokeSelf(final String tokenAuthMount) throws VaultException {
        final String mount = tokenAuthMount != null ? tokenAuthMount : "token";

        retry(attempt -> {
            // HTTP request to Vault
            final RestResponse restResponse = new Rest()//NOPMD
                    .url(config.getAddress() + "/v1/auth/" + mount + "/revoke-self")
                    .header("X-Vault-Token", config.getToken())
                    .header("X-Vault-Namespace", this.nameSpace)
                    .connectTimeoutSeconds(config.getOpenTimeout())
                    .readTimeoutSeconds(config.getReadTimeout())
                    .sslVerification(config.getSslConfig().isVerify())
                    .sslContext(config.getSslConfig().getSslContext())
                    .post();

            // Validate restResponse
            if (restResponse.getStatus() != 204) {
                throw new VaultException(
                        "Vault responded with HTTP status code: " + restResponse.getStatus()
                                + "\nResponse body: " + new String(restResponse.getBody(),
                                StandardCharsets.UTF_8),
                        restResponse.getStatus());
            }

            return null;
        });
    }

    /**
     * <p>Returns the original response inside the wrapped auth token. This method is useful if you
     * need to unwrap a token without being authenticated. See {@link #unwrap(String)} if you need
     * to do that authenticated.</p>
     *
     * <p>In the example below, you cannot use twice the {@code VaultConfig}, since
     * after the first usage of the {@code wrappingToken}, it is not usable anymore. You need to use
     * the {@code unwrappedToken} in a new vault configuration to continue. Example usage:</p>
     *
     * <blockquote>
     * <pre>{@code
     * final String wrappingToken = "...";
     * final VaultConfig config = new VaultConfig().address(...).token(wrappingToken).build();
     * final Vault vault = new Vault(config);
     * final AuthResponse response = vault.auth().unwrap();
     * final String unwrappedToken = response.getAuthClientToken();
     * }</pre>
     * </blockquote>
     *
     * @return The response information returned from Vault
     * @throws VaultException If any error occurs, or unexpected response received from Vault
     * @see #unwrap(String)
     */
    public UnwrapResponse unwrap() throws VaultException {
        return unwrap(null);
    }

    /**
     * <p>Provide access to the {@code /sys/wrapping/unwrap} endpoint.</p>
     *
     * <p>Returns the original response inside the given wrapping token. Unlike simply reading
     * {@code cubbyhole/response} (which is deprecated), this endpoint provides additional
     * validation checks on the token, returns the original value on the wire rather than a JSON
     * string representation of it, and ensures that the response is properly audit-logged.</p>
     *
     * <p> This endpoint can be used by using a wrapping token as the client token in the API call,
     * in which case the token parameter is not required; or, a different token with permissions to
     * access this endpoint can make the call and pass in the wrapping token in the token parameter.
     * Do not use the wrapping token in both locations; this will cause the wrapping token to be
     * revoked but the value to be unable to be looked up, as it will basically be a double-use of
     * the token!</p>
     *
     * <p>In the example below, {@code authToken} is NOT your wrapped token, and should have
     * unwrapping permissions. The unwrapped data in {@link UnwrapResponse#getData()}. Example
     * usage:</p>
     *
     * <blockquote>
     * <pre>{@code
     * final String authToken = "...";
     * final String wrappingToken = "...";
     * final VaultConfig config = new VaultConfig().address(...).token(authToken).build();
     * final Vault vault = new Vault(config);
     *
     * final WrapResponse wrapResponse = vault.auth().wrap(
     *                 // Data to wrap
     *                 new JsonObject()
     *                         .add("foo", "bar")
     *                         .add("zoo", "zar"),
     *
     *                 // TTL of the response-wrapping token
     *                 60
     *         );
     *
     * final UnwrapResponse unwrapResponse = vault.auth().unwrap(wrapResponse.getToken());
     * final JsonObject unwrappedData = response.getData(); // original data
     * }</pre>
     * </blockquote>
     *
     * @param wrappedToken Specifies the wrapping token ID, do NOT also put this in your
     * {@link VaultConfig#getToken()}, if token is {@code null}, this method will unwrap the auth
     * token in {@link VaultConfig#getToken()}
     * @return The response information returned from Vault
     * @throws VaultException If any error occurs, or unexpected response received from Vault
     * @see #wrap(JsonObject, int)
     * @see #unwrap()
     */
    public UnwrapResponse unwrap(final String wrappedToken) throws VaultException {
        return retry(attempt -> {
            // Parse parameters to JSON
            final JsonObject jsonObject = Json.object();
            if (wrappedToken != null) {
                jsonObject.add("token", wrappedToken);
            }

            final String requestJson = jsonObject.toString();
            final String url = config.getAddress() + "/v1/sys/wrapping/unwrap";

            // HTTP request to Vault
            final RestResponse restResponse = new Rest()
                    .url(url)
                    .header("X-Vault-Token", config.getToken())
                    .header("X-Vault-Namespace", this.nameSpace)
                    .body(requestJson.getBytes(StandardCharsets.UTF_8))
                    .connectTimeoutSeconds(config.getOpenTimeout())
                    .readTimeoutSeconds(config.getReadTimeout())
                    .sslVerification(config.getSslConfig().isVerify())
                    .sslContext(config.getSslConfig().getSslContext())
                    .post();

            // Validate restResponse
            if (restResponse.getStatus() != 200) {
                throw new VaultException(
                        "Vault responded with HTTP status code: " + restResponse.getStatus()
                                + "\nResponse body: " + new String(restResponse.getBody(),
                                StandardCharsets.UTF_8),
                        restResponse.getStatus());
            }

            final String mimeType =
                    restResponse.getMimeType() == null ? "null" : restResponse.getMimeType();

            if (!mimeType.equals("application/json")) {
                throw new VaultException("Vault responded with MIME type: " + mimeType,
                        restResponse.getStatus());
            }

            return new UnwrapResponse(restResponse, attempt);
        });
    }

    /**
     * <p>Provide access to the {@code /sys/wrapping/wrap} endpoint.</p>
     *
     * <p>This provides a powerful mechanism for information sharing in many environments.
     * In the types of scenarios, often the best practical option is to provide cover for the secret
     * information, be able to detect malfeasance (interception, tampering), and limit lifetime of
     * the secret's exposure. Response wrapping performs all three of these duties:</p>
     *
     * <ul>
     *     <li>It provides cover by ensuring that the value being transmitted across the wire is
     *     not the actual secret but a reference to such a secret, namely the response-wrapping token.
     *     Information stored in logs or captured along the way do not directly see the sensitive information.
     *     </li>
     *     <li>It provides malfeasance detection by ensuring that only a single party can ever
     *     unwrap the token and see what's inside. A client receiving a token that cannot be unwrapped
     *     can trigger an immediate security incident. In addition, a client can inspect
     *     a given token before unwrapping to ensure that its origin is from the expected
     *     location in Vault.
     *     </li>
     *     <li>It limits the lifetime of secret exposure because the response-wrapping token has
     *     a lifetime that is separate from the wrapped secret (and often can be much shorter),
     *     so if a client fails to come up and unwrap the token, the token can expire very quickly.
     *     </li>
     * </ul>
     *
     * <blockquote>
     * <pre>{@code
     * final String authToken = "...";
     * final String wrappingToken = "...";
     * final VaultConfig config = new VaultConfig().address(...).token(authToken).build();
     * final Vault vault = new Vault(config);
     *
     * final WrapResponse wrapResponse = vault.auth().wrap(
     *                 // Data to wrap
     *                 new JsonObject()
     *                         .add("foo", "bar")
     *                         .add("zoo", "zar"),
     *
     *                 // TTL of the response-wrapping token
     *                 60
     *         );
     *
     * final UnwrapResponse unwrapResponse = vault.auth().unwrap(wrapResponse.getToken());
     * final JsonObject unwrappedData = response.getData(); // original data
     * }</pre>
     * </blockquote>
     *
     * @param jsonObject User data to wrap.
     * @param ttlInSec Wrap TTL in seconds
     * @return The response information returned from Vault
     * @throws VaultException If any error occurs, or unexpected response received from Vault
     * @see #unwrap(String)
     */
    public WrapResponse wrap(final JsonObject jsonObject, int ttlInSec) throws VaultException {
        Objects.requireNonNull(jsonObject);

        return retry(attempt -> {
            // Parse parameters to JSON
            final String requestJson = jsonObject.toString();
            final String url = config.getAddress() + "/v1/sys/wrapping/wrap";

            // HTTP request to Vault
            final RestResponse restResponse = new Rest()
                    .url(url)
                    .header("X-Vault-Token", config.getToken())
                    .header("X-Vault-Wrap-TTL", Integer.toString(ttlInSec))
                    .header("X-Vault-Namespace", this.nameSpace)
                    .body(requestJson.getBytes(StandardCharsets.UTF_8))
                    .connectTimeoutSeconds(config.getOpenTimeout())
                    .readTimeoutSeconds(config.getReadTimeout())
                    .sslVerification(config.getSslConfig().isVerify())
                    .sslContext(config.getSslConfig().getSslContext())
                    .post();

            // Validate restResponse
            if (restResponse.getStatus() != 200) {
                throw new VaultException(
                        "Vault responded with HTTP status code: " + restResponse.getStatus()
                                + "\nResponse body: " + new String(restResponse.getBody(),
                                StandardCharsets.UTF_8),
                        restResponse.getStatus());
            }

            final String mimeType =
                    restResponse.getMimeType() == null ? "null" : restResponse.getMimeType();
            if (!mimeType.equals("application/json")) {
                throw new VaultException("Vault responded with MIME type: " + mimeType,
                        restResponse.getStatus());
            }

            return new WrapResponse(restResponse, attempt);
        });
    }
}
