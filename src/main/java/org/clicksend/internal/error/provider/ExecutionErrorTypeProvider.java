/**
 * The usage of this connector is governed by the terms in the LICENSE.md file.
 */
package org.clicksend.internal.error.provider;

import java.util.HashSet;
import java.util.Set;

import org.clicksend.internal.error.MessageErrorType;
import org.mule.runtime.extension.api.annotation.error.ErrorTypeProvider;
import org.mule.runtime.extension.api.error.ErrorTypeDefinition;

public class ExecutionErrorTypeProvider implements ErrorTypeProvider {

	@Override
	public Set<ErrorTypeDefinition> getErrorTypes() {
		return new HashSet<>(getMessageErrorTypes());
	}

	public Set<ErrorTypeDefinition<MessageErrorType>> getMessageErrorTypes(){
		HashSet<ErrorTypeDefinition<MessageErrorType>> errors = new HashSet<>();
		errors.add(MessageErrorType.EXECUTION);
		return errors;
	}
}
