package com.gts.supplychain.spec.common.request;

import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import lombok.Data;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

@Data
@Schema(description = "Paging and sorting request for list endpoints")
public class PagingRequest {

	@Min(value = 0, message = "pageNumber should not be less than 0")
	@Schema(example = "0", description = "Page number starting from 0")
	private int page = 0;

	@Min(value = 1, message = "pageSize should not be less than 1")
	@Schema(example = "10", description = "Number of items per page")
	private int size = 10;

	@Schema(example = "ASC", description = "Sorting direction: ASC or DESC")
	private SortDirection direction = SortDirection.ASC;

	@Schema(example = "id", description = "Field to sort by")
	private String sortKey = "id";

	public Pageable toPageable() {
		Sort sort = Sort.by(
				direction == SortDirection.ASC ? Sort.Direction.ASC : Sort.Direction.DESC,
				sortKey
		);
		return PageRequest.of(page, size, sort);
	}

	public enum SortDirection {
		ASC, DESC
	}
}
