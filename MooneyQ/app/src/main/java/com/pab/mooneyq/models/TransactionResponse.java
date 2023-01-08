package com.pab.mooneyq.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class TransactionResponse {

    @SerializedName("status")
    @Expose
    private Integer status;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("data")
    @Expose
    private List<Data> data;

    @SerializedName("total_in")
    @Expose
    private Integer total_in;
    @SerializedName("total_out")
    @Expose
    private Integer total_out;
    @SerializedName("balance")
    @Expose
    private Integer balance;

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<Data> getData() {
        return data;
    }

    public void setData(List<Data> data) {
        this.data = data;
    }

    public Integer getTotal_in() {
        return total_in;
    }

    public void setTotal_in(Integer total_in) {
        this.total_in = total_in;
    }

    public Integer getTotal_out() {
        return total_out;
    }

    public void setTotal_out(Integer total_out) {
        this.total_out = total_out;
    }

    public Integer getBalance() {
        return balance;
    }

    public void setBalance(Integer balance) {
        this.balance = balance;
    }

    public class Data implements Serializable{

        @SerializedName("id")
        @Expose
        private Integer id;
        @SerializedName("category")
        @Expose
        private String category;
        @SerializedName("description")
        @Expose
        private String description;
        @SerializedName("amount")
        @Expose
        private Integer amount;
        @SerializedName("type")
        @Expose
        private String type;
        @SerializedName("date")
        @Expose
        private String date;

        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }

        public String getCategory() {
            return category;
        }

        public void setCategory(String category) {
            this.category = category;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public Integer getAmount() {
            return amount;
        }

        public void setAmount(Integer amount) {
            this.amount = amount;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getDate() {
            return date;
        }

        public void setDate(String date) {
            this.date = date;
        }

    }

}
