package com.gts.supplychain.spec.common.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;


@Setter
@Getter
@ToString
public class ResponseService  {

	private Result result;

	@JsonProperty
	public void setResult(Result result) {
		this.result = result;
	}

	public void setResult(ResultStatus resultStatus) {
		if (resultStatus == null) {
			return;
		}
		Result result = new Result();
		result.setTitle(resultStatus);
		result.setMessage(resultStatus.getDescription());
		result.setStatus(resultStatus.getStatus());
		this.result = result;
	}


}
