package com.gts.supplychain.exception;

import com.gts.supplychain.spec.common.response.ResultStatus;

public abstract class BusinessException extends Exception {

	public BusinessException(String message) {
		super(message);
	}

	public abstract ResultStatus getResultStatus();
}