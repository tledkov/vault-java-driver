package io.github.jopenlibs.vault.vault.mock;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.eclipse.jetty.server.Request;

/**
 * <p>This class is used to mock out a Vault server in unit tests involving open timeouts (i.e. delays before an
 * HTTP(S) connection is successfully established).  As it extends Jetty's <code>AbstractHandler</code>, it can be
 * passed to an embedded Jetty server and respond to actual (albeit localhost) HTTP requests.</p>
 *
 * <p>The basic usage pattern is as follows:</p>
 *
 * <ol>
 * <li>
 * <code>OpenTimeoutsMockVault</code> pauses for the designated number of seconds before allowing the
 * incoming HTTP(S) request to connect.
 * </li>
 * <li>
 * After the delay, <code>OpenTimeoutsMockVault</code> responds with a designated HTTP status code, and
 * a designated response body.
 * </li>
 * </ol>
 *
 * <p>Example usage:</p>
 *
 * <blockquote>
 * <pre>{@code
 * final Server server = new Server(8999);
 * server.setHandler( new OpenTimeoutsMockVault(2, 200, "{\"data\":{\"value\":\"mock\"}}") );
 * server.start();
 *
 * final VaultConfig vaultConfig = new VaultConfig("http://127.0.0.1:8999", "mock_token");
 * final VaultConfig vaultConfig = new VaultConfig()
 *                                  .address("http://127.0.0.1:8999")
 *                                  .token("mock_token")
 *                                  .openTimeout(1)
 *                                  .build();
 * try {
 *      final LogicalResponse response = vault.logical().read("secret/hello");
 * } catch (Exception e) {
 *     // An exception is thrown due to the timeout threshold being exceeded.  If the VaultConfig instance were given
 *     // an openTimeout value greater than that set for the OpenTimeoutsMockVault instance, then the call would
 *     // succeed instead.
 *     VaultTestUtils.shutdownMockVault(server);
 *     throw e;
 * }
 * }</pre>
 * </blockquote>
 */
public class OpenTimeoutsMockVault extends MockVault {

    private final int delaySeconds;
    private final int mockStatus;
    private final String mockResponse;

    public OpenTimeoutsMockVault(final int delaySeconds, final int mockStatus, final String mockResponse) {
        this.delaySeconds = delaySeconds;
        this.mockStatus = mockStatus;
        this.mockResponse = mockResponse;
    }

    @Override
    public void handle(
            final String target,
            final Request baseRequest,
            final HttpServletRequest request,
            final HttpServletResponse response
    ) throws IOException, ServletException {
        try {
            Thread.sleep((long) delaySeconds * 1000);
            response.setContentType("application/json");
            baseRequest.setHandled(true);
            System.out.println("OpenTimeoutsMockVault is sending an HTTP " + mockStatus + " code, with expected" +
                    " success payload...");
            response.setStatus(mockStatus);
            if (mockResponse != null) {
                response.getWriter().println(mockResponse);
            }
        } catch (InterruptedException e) {
            throw new ServletException(e);
        }
    }

}
