package com.pab.mooneyq.models;


public class TransactionRequest {

    private String id;
    private Integer user_id;
    private Integer category_id;
    private String type;
    private Integer amount;
    private String description;

    public TransactionRequest(String id, Integer user_id, Integer category_id, String type, Integer amount, String description) {
        this.id = id;
        this.user_id = user_id;
        this.category_id = category_id;
        this.type = type;
        this.amount = amount;
        this.description = description;
    }
}
