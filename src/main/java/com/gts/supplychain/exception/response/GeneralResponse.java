package com.gts.supplychain.exception.response;

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
