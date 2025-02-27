package org.example.jpa.controller;

import org.example.jpa.model.Customer;
import org.example.jpa.model.Feedback;
import org.example.jpa.repository.CustomerRepository;
import org.example.jpa.service.CustomerService;
import org.example.jpa.service.FeedbackService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/feedback")
public class FeedbackController {

    @Autowired
    FeedbackService feedbackService;

    @Autowired
    CustomerService customerService;


    @Autowired
    CustomerRepository customerRepository;

    @GetMapping
    public ResponseEntity<Object> allFeedback(){
        return new ResponseEntity<>(feedbackService.findAll(), HttpStatus.OK);
    }

    @PostMapping("/{id}")
    public ResponseEntity<Object> saveFeedback(@PathVariable("id") Long customerId, @RequestBody Feedback feedback) {

        Optional<Feedback> checkFeedback = customerService.findById(customerId).map(_customer -> {
            Feedback _feedback = new Feedback(_customer, feedback.getDescription());
            return feedbackService.save(_feedback);
        });

        if (checkFeedback.isEmpty())
            return new ResponseEntity<>("Feedback Not Created", HttpStatus.BAD_REQUEST);

        return new ResponseEntity<>(checkFeedback, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Object> updateFeedback (@PathVariable("id") Long feedbackId, @RequestBody Feedback feedback) {
        Optional<Feedback> checkFeedback = feedbackService.findById(feedbackId).map(_feedback -> {
           _feedback.setDescription(feedback.getDescription());
            return feedbackService.save(_feedback);
        });

        if (checkFeedback.isEmpty())
            return new ResponseEntity<>("Feedback Not Updated", HttpStatus.BAD_REQUEST);

        return new ResponseEntity<>(checkFeedback, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteFeedback (@PathVariable("id") Long feedbackId) {

        Optional<Feedback> checkFeedback = feedbackService.findById(feedbackId).map(_feedback -> {
            feedbackService.deleteById(_feedback.getId());
            return _feedback;
        });
        if (checkFeedback.isEmpty())
            return new ResponseEntity<>("Feedback not found",HttpStatus.NOT_FOUND);

        String response = String.format("%s deleted successfully", checkFeedback.get().getDescription());

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/count")
    public ResponseEntity<Object> countFeedback() {

        long count  = feedbackService.count();

        if (count <= 0)
            return new ResponseEntity<>("No feedback", HttpStatus.NOT_FOUND);

        Map<String, Object> totalFeedback = new HashMap<>();
        totalFeedback.put("total", count);

        return new ResponseEntity<>(totalFeedback, HttpStatus.OK);
    }

    // Delete all feedback that belongs to a customer
    // 1. Find the customer first OR you find the feedback first
    // 2. Delete the feedback

    //    @DeleteMapping("/allFeedback/{id}")
//    public ResponseEntity<Object> deleteAllFeedback (@PathVariable("id") Long feedbackId) {
//
//        Optional<Customer> checkCustomer = customerService.findById(feedbackId).map(_customer -> {
//            Optional<Feedback> checkFeedback = feedbackService.findById(feedbackId).map(_allFeedback -> {
//                feedbackService.deleteById(_allFeedback.getId());
//                return _allFeedback;
//            });
//            return _customer;
//        });
//
//        if (checkCustomer.isEmpty())
//            return new ResponseEntity<>("Feedback not found",HttpStatus.NOT_FOUND);
//
//        String response = String.format("%s deleted successfully", checkCustomer.get().getId());
//
//        return new ResponseEntity<>(response, HttpStatus.OK);
//    }

}

