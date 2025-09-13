package com.gts.supplychain.spec.common.response;

import java.io.IOException;
import java.util.Properties;

// @formatter:off
public enum ResultStatus {

	SUCCESS(100, "core.success"),
	UNKNOWN(101, "core.unknown.error"),
	DUPLICATE_REQUEST(102,"core.duplicate.request"),
	FORBIDDEN_REQUEST(103, "core.forbidden.request"),
	BAD_REQUEST(104, "core.bad.request"),
	INVALID_PARAMETER(105, "core.invalid.parameter.exception"),

	AUTHENTICATION_FAILURE(1000, "user.not.authenticated"),
	AUTHENTICATION_INVALID_CREDENTIALS(1001, "user.not.authenticated"),
	AUTHORIZATION_PROTECTED_ROUTE_EXCEPTION(1002, "user.not.authorized"),
	USER_NOT_FOUND(1003, "user.not.found"),

	SUPPLY_CHAIN_PRODUCT_NOT_FOUND(2001,"supply.chain.product.not.found");


	private final String description;
	private final Integer status;

	ResultStatus(int status, String description) {
		this.status = status;
		String errorMsg = MessageHolder.ERROR_MESSAGE_PROPERTIES.getProperty(description);
		this.description = errorMsg != null ? errorMsg : description;
	}

	public String getDescription() {
		return description;
	}

	public Integer getStatus() {
		return status;
	}

	private static final class MessageHolder {
		private static final Properties ERROR_MESSAGE_PROPERTIES = new Properties();

		static {
			try {
				ERROR_MESSAGE_PROPERTIES.load((ResultStatus.class.getResourceAsStream("/error-messages.properties")));
			} catch (IOException e) {
				throw new ExceptionInInitializerError(e);
			}
		}
	}
}
// @formatter:on
