package com.gts.supplychain.spec.common.response;

import lombok.ToString;


@ToString(callSuper = true)
public class GeneralResponse extends ResponseService {

	@Deprecated
	public GeneralResponse() {
		setResult(ResultStatus.SUCCESS);
	}

	public GeneralResponse(ResultStatus resultStatus) {
		setResult(resultStatus);
	}

}
