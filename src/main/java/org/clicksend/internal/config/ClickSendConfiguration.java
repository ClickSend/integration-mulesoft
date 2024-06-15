package org.clicksend.internal.config;

import org.apache.commons.lang3.JavaVersion;
import org.clicksend.internal.connection.provider.ClickSendConnectionProvider;
import org.clicksend.internal.operation.ClickSendMule4sOperations;
import org.mule.runtime.extension.api.annotation.Operations;
import org.mule.runtime.extension.api.annotation.connectivity.ConnectionProviders;
import org.mule.runtime.extension.api.annotation.param.Parameter;
import org.mule.runtime.extension.api.annotation.param.display.DisplayName;
import org.mule.runtime.extension.api.annotation.param.display.Example;
import org.mule.runtime.extension.api.annotation.param.display.Password;
import org.mule.runtime.extension.api.annotation.param.display.Placement;
import org.springframework.core.annotation.Order;

/**
 * This class represents an extension configuration, values set in this class are commonly used across multiple
 * operations since they represent something core from the extension.
 */
@Operations(ClickSendMule4sOperations.class)
@ConnectionProviders(ClickSendConnectionProvider.class)
public class ClickSendConfiguration {

}
