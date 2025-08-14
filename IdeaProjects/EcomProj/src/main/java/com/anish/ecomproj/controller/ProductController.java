package com.anish.ecomproj.controller;

import com.anish.ecomproj.model.Product;
import com.anish.ecomproj.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api")
@CrossOrigin
public class ProductController {

    @Autowired
    private ProductService service;

    @RequestMapping("/")
    public String greet() {
        return "Hello, welcome to the Product Controller!";
    }
    @GetMapping("/products")
    public ResponseEntity<List<Product>> getAllProducts() {
        return new ResponseEntity<>(service.getAllProducts(), HttpStatus.OK);
    }

    @GetMapping("/product/{id}")
    public ResponseEntity<Product> getProduct(@PathVariable int id) {
        Product product = service.getProduct(id);
        if (product == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        else {
            return new ResponseEntity<>(service.getProduct(id), HttpStatus.OK);
        }
    }

    @PostMapping("/product")
    public ResponseEntity<?> addProduct(@RequestPart Product product, @RequestPart MultipartFile imageFile) {
        try {
            Product product1 = service.addProduct(product, imageFile);
            return new ResponseEntity<>(product1, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);


        }
    }

    @GetMapping("/product/{productId}/image")
    public ResponseEntity<byte[]> getImageByProductId(@PathVariable int productId) {
    Product product = service.getProduct(productId);
    byte[] imageFile = product.getImageData();

    return ResponseEntity.ok()
        .contentType(MediaType.valueOf(product.getImageType()))
        .body(imageFile);
     }

     @PutMapping("/product/{id}")
     public ResponseEntity<String> updateProduct(@PathVariable int id,
                                                 @RequestPart Product product,
                                                 @RequestPart MultipartFile imageFile){

        Product product1 = service.getProduct(id);
        if(product1 != null){
            try {
                service.updateProduct(id, product, imageFile);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            return new ResponseEntity<>("Product Updated Successfully", HttpStatus.OK);
        }
        else {
            return new ResponseEntity<>("Product Not Found", HttpStatus.NOT_FOUND);
        }
     }

     @DeleteMapping("/product/{id}")
     public void deleteProduct(@PathVariable int id){
        Product product = service.getProduct(id);
        if (product != null) {
            service.deleteProduct(id);
        } else {
            throw new RuntimeException("Product not found with id: " + id);
        }
     }

     @GetMapping("/products/search")
     public ResponseEntity<List<Product>> searchProducts(@RequestParam String keyword) {
        List<Product> products = service.searchProducts(keyword);
        return new ResponseEntity<>(products, HttpStatus.OK);


     }

}
