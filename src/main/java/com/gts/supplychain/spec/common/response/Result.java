package com.gts.supplychain.spec.common.response;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
@NoArgsConstructor
public class Result {

	private ResultStatus title;

	private int status;

	private String message;


}
