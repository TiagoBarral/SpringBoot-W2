package com.trainingspringboot.shoppingcart.controller;

import com.trainingspringboot.shoppingcart.entity.model.Cart;
import com.trainingspringboot.shoppingcart.entity.request.CreateCartRequest;
import com.trainingspringboot.shoppingcart.entity.response.CreateCartResponse;
import com.trainingspringboot.shoppingcart.entity.response.GetCartResponse;
import com.trainingspringboot.shoppingcart.entity.response.UpdateCartRequest;
import com.trainingspringboot.shoppingcart.service.CartService;
import com.trainingspringboot.shoppingcart.utils.annotation.ServiceOperation;
import java.util.stream.Collectors;
import javax.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("carts")
public class CartController {

	@Autowired
	private CartService cartService;

	/**
	 * @JavaDoc ModelMapper is a mapping tool easily configurable to accommodate most application defined entities check
	 * some configuration example at: http://modelmapper.org/user-manual/
	 */
	@Autowired
	private ModelMapper mapper;

	@PostMapping
	@ServiceOperation("createCart")
	public ResponseEntity<CreateCartResponse> createCart(@RequestBody @Valid CreateCartRequest request) {
		return new ResponseEntity<>(mapper.map(cartService.save(mapper.map(request, Cart.class)), CreateCartResponse.class),
				HttpStatus.CREATED);
	}

	@GetMapping("/{id}")
	@ServiceOperation("getCart")
	public ResponseEntity<GetCartResponse> getItem(@PathVariable("id") Long id) {
		return new ResponseEntity<>(mapper.map(cartService.get(id), GetCartResponse.class), HttpStatus.OK);
	}

	@PatchMapping("/{id}")
	@ServiceOperation("updateCart")
	public ResponseEntity<?> updateCart(@PathVariable("id") Long id, @RequestBody @Valid UpdateCartRequest cart) {
		cart.setCartUid(id);
		return new ResponseEntity<>(cartService.update(mapper.map(cart, Cart.class)), HttpStatus.OK);
	}

	@DeleteMapping("/{id}")
	@ServiceOperation("deleteCart")
	public ResponseEntity<HttpStatus> deleteItem(@PathVariable("id") Long id) {
		cartService.delete(id);
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}

	@GetMapping
	@ServiceOperation("listCarts")
	public ResponseEntity<Page<GetCartResponse>> listCarts(@RequestParam(value = "page", defaultValue = "0") int page,
			@RequestParam(value = "size", defaultValue = "20") int size) {
		return new ResponseEntity<>(new PageImpl<>(
				cartService.list(page, size).stream().map(c -> mapper.map(c, GetCartResponse.class))
						.collect(Collectors.toList())), HttpStatus.OK);
	}

}
