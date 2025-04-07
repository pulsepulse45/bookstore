package com.project.code;

import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import org.springframework.validation.ValidationUtils;

public class BookValidator implements Validator {
    
    @Override
    public boolean supports(Class<?> clazz) {
        return Book.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        Book book = (Book) target;

        // Existing validations
        if (book.getPrice() < 1) {
            errors.rejectValue("price", "price.invalid", "Price must be greater than 0");
        }
        
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "title", "title.empty", "Title cannot be empty");
        
        // Additional validations
        if (book.getISBN() != null && !book.getISBN().matches("^(978|979)\\d{10}$")) {
            errors.rejectValue("ISBN", "ISBN.invalid", "ISBN must be a valid format (13 digits starting with 978 or 979)");
        }
        
        if (book.getPublicationDate() != null && book.getPublicationDate().isBefore(LocalDate.now())) {
            errors.rejectValue("publicationDate", "publicationDate.invalid", "Publication date must be in the future");
        }
    }
}
