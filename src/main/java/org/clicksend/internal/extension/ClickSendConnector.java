/**
 * The usage of this connector is governed by the terms in the LICENSE.md file.
 */
package org.clicksend.internal.extension;

import org.clicksend.internal.config.ClickSendConfiguration;
import org.clicksend.internal.error.MessageErrorType;
import org.mule.runtime.api.meta.Category;
import org.mule.runtime.extension.api.annotation.Configurations;
import org.mule.runtime.extension.api.annotation.Extension;
import org.mule.runtime.extension.api.annotation.dsl.xml.Xml;
import org.mule.runtime.extension.api.annotation.error.ErrorTypes;


/**
 * This is the main class of an extension, is the entry point from which configurations, connection providers, operations
 * and sources are going to be declared.
 */
@Xml(prefix = "clicksend")
@Extension(name = "clicksend", category = Category.CERTIFIED)
@ErrorTypes(MessageErrorType.class)
@Configurations(ClickSendConfiguration.class)
public class ClickSendConnector {

}
