package io.github.jopenlibs.vault.response;

import io.github.jopenlibs.vault.json.Json;
import io.github.jopenlibs.vault.json.JsonArray;
import io.github.jopenlibs.vault.json.JsonObject;
import io.github.jopenlibs.vault.json.JsonValue;
import io.github.jopenlibs.vault.json.ParseException;
import io.github.jopenlibs.vault.rest.RestResponse;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

/**
 * This class is a container for the information returned by Vault in auth backend operations.
 */
public class AuthResponse extends VaultResponse {
    private Boolean renewable;
    private String authClientToken;
    private String tokenAccessor;
    private List<String> authPolicies;
    private long authLeaseDuration;
    private boolean authRenewable;
    private String appId;
    private String userId;
    private String username;
    private String nonce;

    protected JsonObject jsonResponse;

    /**
     * This constructor simply exposes the common base class constructor.
     *
     * @param restResponse The raw HTTP response from Vault.
     * @param retries      The number of retry attempts that occurred during the API call (can be zero).
     */
    public AuthResponse(final RestResponse restResponse, final int retries) {
        super(restResponse, retries);

        try {
            final String responseJson = new String(restResponse.getBody(), StandardCharsets.UTF_8);
            jsonResponse = Json.parse(responseJson).asObject();
            JsonValue authJsonVal = jsonResponse.get("auth");
            final JsonObject authJsonObject = authJsonVal != null && !authJsonVal.isNull() ? authJsonVal.asObject() : null;

            if (authJsonObject != null) {
                authLeaseDuration = authJsonObject.getInt("lease_duration", 0);
                authRenewable = authJsonObject.getBoolean("renewable", false);
                if (authJsonObject.get("metadata") != null && !authJsonObject.get("metadata")
                        .toString().equalsIgnoreCase("null")) {
                    final JsonObject metadata = authJsonObject.get("metadata").asObject();
                    appId = metadata.getString("app-id", "");
                    userId = metadata.getString("user-id", "");
                    username = metadata.getString("username", "");
                    nonce = metadata.getString("nonce", "");
                }

                authClientToken = authJsonObject.getString("client_token", "");
                tokenAccessor = authJsonObject.getString("accessor", "");

                final JsonArray authPoliciesJsonArray = authJsonObject.get("policies").asArray();
                authPolicies = new ArrayList<>();

                for (final JsonValue authPolicy : authPoliciesJsonArray) {
                    authPolicies.add(authPolicy.asString());
                }
            }

            renewable = jsonResponse.get("renewable").asBoolean();
        } catch (ParseException e) {
            // No-op.
        }
    }

    public String getUsername() {
        return username;
    }

    public Boolean getRenewable() {
        return renewable;
    }

    public String getAuthClientToken() {
        return authClientToken;
    }

    public List<String> getAuthPolicies() {
        return authPolicies;
    }

    public long getAuthLeaseDuration() {
        return authLeaseDuration;
    }

    public boolean isAuthRenewable() {
        return authRenewable;
    }

    public String getAppId() {
        return appId;
    }

    public String getUserId() {
        return userId;
    }

    public String getNonce() { return nonce; }

    public String getTokenAccessor() { return tokenAccessor; }
}
