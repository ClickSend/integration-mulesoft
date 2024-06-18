/**
 * The usage of this connector is governed by the terms in the LICENSE.md file.
 */
package org.clicksend.internal.error.provider;

import java.util.Set;

import org.clicksend.internal.error.MessageErrorType;
import org.mule.runtime.extension.api.error.ErrorTypeDefinition;


	public class MessageErrorTypeProvider extends ExecutionErrorTypeProvider {

	    @Override
	    public Set<ErrorTypeDefinition<MessageErrorType>> getMessageErrorTypes() {
	        Set<ErrorTypeDefinition<MessageErrorType>> errors = super.getMessageErrorTypes();
	        errors.add(MessageErrorType.JSON_PARSER_EXCEPTION);
	        return errors;
	    }

}
