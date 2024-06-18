/**
 * The usage of this connector is governed by the terms in the LICENSE.md file.
 */
package org.clicksend.internal.error;

import static java.util.Optional.ofNullable;

import java.util.Optional;

import org.mule.runtime.extension.api.error.ErrorTypeDefinition;


public enum MessageErrorType implements ErrorTypeDefinition<MessageErrorType> {

	
	EXECUTION,
	JSON_PARSER_EXCEPTION(EXECUTION);

	MessageErrorType(){
		
	}
	 
	
    private ErrorTypeDefinition<?> parent;

    MessageErrorType(final ErrorTypeDefinition<?> parent) {
        this.parent = parent;
    }

   
    @Override
    public Optional<ErrorTypeDefinition<? extends Enum<?>>> getParent() {
        return ofNullable(parent);
    }
	
	
}