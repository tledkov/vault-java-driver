package com.bettercloud.vault;

import com.bettercloud.vault.response.LogicalResponse;
import com.bettercloud.vault.vault.VaultTestUtils;
import com.bettercloud.vault.vault.mock.RetriesMockVault;
import org.eclipse.jetty.server.Server;
import org.junit.Test;

import java.util.HashMap;

import static org.junit.Assert.assertEquals;

/**
 * TODO: Document
 */
public class RetryTests {

    @Test
    public void testRetries_Read() throws Exception {
        final RetriesMockVault retriesMockVault = new RetriesMockVault(5, 200, "{\"data\":{\"value\":\"mock\"}}");
        final Server server = VaultTestUtils.initHttpMockVault(retriesMockVault);
        server.start();

        final VaultConfig vaultConfig = new VaultConfig("http://127.0.0.1:8999", "mock_token");
        final Vault vault = new Vault(vaultConfig);
        final LogicalResponse response = vault.withRetries(5, 100).logical().read("secret/hello");
        assertEquals(5, response.getRetries());
        assertEquals("mock", response.getData().get("value"));

        VaultTestUtils.shutdownMockVault(server);
    }

    @Test
    public void testRetries_Write() throws Exception {
        final RetriesMockVault retriesMockVault = new RetriesMockVault(5, 204, null);
        final Server server = VaultTestUtils.initHttpMockVault(retriesMockVault);
        server.start();

        final VaultConfig vaultConfig = new VaultConfig("http://127.0.0.1:8999", "mock_token");
        final Vault vault = new Vault(vaultConfig);
        final LogicalResponse response = vault.withRetries(5, 100).logical()
                .write("secret/hello", new HashMap() {{ put("value", "world"); }});
        assertEquals(5, response.getRetries());

        VaultTestUtils.shutdownMockVault(server);
    }

}
