package com.gts.supplychain.exception;

import com.gts.supplychain.spec.common.response.ResultStatus;

public class NotFoundException extends BusinessException {
    public NotFoundException(String message) {
        super(message);
    }

	@Override
	public ResultStatus getResultStatus() {
		return ResultStatus.SUPPLY_CHAIN_PRODUCT_NOT_FOUND;
	}
}
