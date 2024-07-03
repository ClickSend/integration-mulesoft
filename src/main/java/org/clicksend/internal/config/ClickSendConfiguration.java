/**
 * The usage of this connector is governed by the terms in the LICENSE.md file.
 */
package org.clicksend.internal.config;

import org.clicksend.internal.connection.provider.ClickSendConnectionProvider;
import org.clicksend.internal.operation.ClickSendMule4sOperations;
import org.mule.runtime.extension.api.annotation.Operations;
import org.mule.runtime.extension.api.annotation.connectivity.ConnectionProviders;

/**
 * This class represents an extension configuration, values set in this class are commonly used across multiple
 * operations since they represent something core from the extension.
 */
@Operations(ClickSendMule4sOperations.class)
@ConnectionProviders(ClickSendConnectionProvider.class)
public class ClickSendConfiguration {

}
