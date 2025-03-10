package io.github.jopenlibs.vault.v1_1_3.api;

import io.github.jopenlibs.vault.Vault;
import io.github.jopenlibs.vault.VaultConfig;
import io.github.jopenlibs.vault.VaultException;
import io.github.jopenlibs.vault.response.LogicalResponse;
import io.github.jopenlibs.vault.v1_1_3.util.VaultContainer;
import java.io.IOException;
import org.junit.BeforeClass;
import org.junit.ClassRule;
import org.junit.Test;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNotSame;

/**
 * Integration tests for the AppRole auth backend.
 */
public class AuthBackendAppRoleTests {

    private static String appRoleId;
    private static String secretId;

    @ClassRule
    public static final VaultContainer container = new VaultContainer();

    @BeforeClass
    public static void setupClass() throws IOException, InterruptedException, VaultException {
        container.initAndUnsealVault();
        container.setupBackendAppRole();

        final Vault vault = container.getRootVaultWithCustomVaultConfig(new VaultConfig().engineVersion(1));

        final LogicalResponse roleIdResponse = vault.logical().read("auth/approle/role/testrole/role-id");
        appRoleId = roleIdResponse.getData().get("role_id");
        final LogicalResponse secretIdResponse = vault.logical().write("auth/approle/role/testrole/secret-id", null);
        secretId = secretIdResponse.getData().get("secret_id");

        assertNotNull(appRoleId);
        assertNotNull(secretId);
    }

    /**
     * Tests authentication with the app role auth backend
     */
    @Test
    public void testLoginByAppRole() throws VaultException {
        final Vault vault = container.getVault();
        final String path = "approle";
        final String token = vault.auth().loginByAppRole(path, appRoleId, secretId).getAuthClientToken();

        assertNotNull(token);
        assertNotSame("", token.trim());
    }

}
