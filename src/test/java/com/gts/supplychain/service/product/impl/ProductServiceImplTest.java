package com.gts.supplychain.service.product.impl;

import java.util.Optional;

import com.gts.supplychain.exception.BusinessException;
import com.gts.supplychain.exception.NotFoundException;
import com.gts.supplychain.model.repository.ProductRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProductServiceImplTest {


	@InjectMocks
	private ProductServiceImpl productService;

	@Mock
	private ProductRepository productRepository;

	@Test
	void getByProductId_shouldThrowNotFoundException() {
		String productId = "P-999";
		when(productRepository.findByProductId(productId)).thenReturn(Optional.empty());

		BusinessException exception = assertThrows(
				NotFoundException.class,
				() -> productService.getByProductId(productId)
		);

		assertEquals("Product not found", exception.getMessage());
		verify(productRepository, times(1)).findByProductId(productId);
	}
}