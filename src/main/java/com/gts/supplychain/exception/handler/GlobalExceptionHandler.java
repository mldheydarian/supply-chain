package com.gts.supplychain.exception.handler;

import java.security.InvalidParameterException;

import com.gts.supplychain.exception.BusinessException;
import com.gts.supplychain.exception.response.GeneralResponse;
import com.gts.supplychain.exception.response.ResponseService;
import com.gts.supplychain.exception.response.ResultStatus;
import lombok.extern.slf4j.Slf4j;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

	@ExceptionHandler(BusinessException.class)
	public final ResponseEntity<ResponseService> handleBusinessException(BusinessException ex) {
		log.error("business error occurred:", ex);
		return ResponseEntity.unprocessableEntity().body(new GeneralResponse(ex.getResultStatus()));
	}

	@ExceptionHandler({DataIntegrityViolationException.class , DuplicateKeyException.class})
	public ResponseEntity<ResponseService> handleDataIntegrityViolation(DataIntegrityViolationException ex) {
		log.error("data integrity error occurred: {}", ex.getMessage(), ex);
		return ResponseEntity.unprocessableEntity().body(new GeneralResponse(ResultStatus.DUPLICATE_REQUEST));
	}

	@ExceptionHandler(InvalidParameterException.class)
	public final ResponseEntity<ResponseService> handleInvalidParameterException(InvalidParameterException ex) {
		log.error("invalid parameter error occurred: {}", ex.getMessage(), ex);
		return ResponseEntity.unprocessableEntity().body(new GeneralResponse(ResultStatus.INVALID_PARAMETER));
	}

	@ExceptionHandler(UnsupportedOperationException.class)
	public final ResponseEntity<ResponseService> handleUnsupportedOperationException(UnsupportedOperationException ex) {
		log.error("unsupported operation error occurred: {}", ex.getMessage(), ex);
		return new ResponseEntity<>(new GeneralResponse(ResultStatus.FORBIDDEN_REQUEST), HttpStatus.FORBIDDEN);
	}

	@ExceptionHandler(Throwable.class)
	public final ResponseEntity<ResponseService> handleGeneralException(Throwable throwable) {
		log.error("unhandled error occurred: {}", throwable.getMessage(), throwable);
		return new ResponseEntity<>(new GeneralResponse(ResultStatus.UNKNOWN), HttpStatus.UNPROCESSABLE_ENTITY);
	}




}
