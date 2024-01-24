package com.compassuol.sp.challenge.ecommerce.domain.web.controller;

import com.compassuol.sp.challenge.ecommerce.domain.product.model.Product;
import com.compassuol.sp.challenge.ecommerce.domain.product.service.ProductService;
import com.compassuol.sp.challenge.ecommerce.domain.web.dto.ProductCreateDto;
import com.compassuol.sp.challenge.ecommerce.domain.web.dto.ProductResponseDto;
import com.compassuol.sp.challenge.ecommerce.domain.web.dto.mapper.ProductMapper;
import com.compassuol.sp.challenge.ecommerce.domain.web.exception.ErrorMessage;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@Tag(name = "Products API", description = "Contém as operações relativas aos recursos de interação com o domínio produtos.")
@RequiredArgsConstructor
@RestController
@RequestMapping("/products")
public class ProductController {
    private final ProductService productService;

    @Operation(summary = "Lista todos os produtos.", description = "Recurso para listar todos os produtos.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Lista de produtos",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ProductResponseDto.class))
                    )
            }
    )

    @GetMapping
    public ResponseEntity<List<ProductResponseDto>> getAllProducts() {

        List<Product> products = productService.getAllProducts();
        List<ProductResponseDto> productResponseDtos = ProductMapper.toDtoList(products);

        return ResponseEntity.ok(productResponseDtos);
    }

    public ResponseEntity<Product> getProductById() {
        return null;
    }

    @Operation(summary = "Cria um novo produto.", description = "Recurso para criar um novo produto.",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Produto criado com sucesso.",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ProductResponseDto.class))
                    ),
                    @ApiResponse(responseCode = "400", description = "Requisição inválida.",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class))
                    ),
                    @ApiResponse(responseCode = "409", description = "Nome do produto já existente.",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class))
                    )
            }
    )
    @PostMapping
    public ResponseEntity<ProductResponseDto> createProduct(@Valid @RequestBody ProductCreateDto dto) {
        final Product product = productService.create(ProductMapper.toProduct(dto));
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ProductMapper.toDto(product));
    }

    public ResponseEntity<Product> updateProduct() {
        return null;
    }

    @Operation(summary = "Deletar produto pelo ID", description = "Recurso para deletar um produto pelo ID.",
            responses = {
                    @ApiResponse(responseCode = "204", description = "Produto deletado com sucesso"),
                    @ApiResponse(responseCode = "404", description = "Produto não encontrado",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class))
                    )
            }
    )
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable("id") Long productId) {
        productService.deleteProduct(productId);
        return ResponseEntity.noContent().build();
    }
}

