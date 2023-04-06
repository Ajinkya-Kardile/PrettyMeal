package com.ajinkya.prettymeal.model;

public class HistoryModel {
    private String Name;
    private String TransactionNo;
    private String DateTime;

    public HistoryModel(String name, String transactionNo, String dateTime) {
        Name = name;
        TransactionNo = transactionNo;
        DateTime = dateTime;
    }



    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getTransactionNo() {
        return TransactionNo;
    }

    public void setTransactionNo(String transactionNo) {
        TransactionNo = transactionNo;
    }

    public String getDateTime() {
        return DateTime;
    }

    public void setDateTime(String dateTime) {
        DateTime = dateTime;
    }


}
