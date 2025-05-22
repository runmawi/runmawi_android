package com.atbuys.runmawi;

//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//


import tk.json.simple.JSONObject;

public class PaydunyaCheckoutInvoice extends PaydunyaCheckout {
    protected JSONObject invoice = new JSONObject();
    protected JSONObject actions = new JSONObject();
    protected JSONObject items = new JSONObject();
    protected double totalAmount = 0.0;
    protected JSONObject taxes = new JSONObject();
    protected int itemsCount = 0;
    protected int taxesCount = 0;
    protected String description = null;
    protected String currency = "fcfa";
    protected String cancelUrl = null;
    protected String returnUrl = null;
    protected String callbackUrl = null;
    protected String invoiceUrl = null;
    protected JSONObject customData = new JSONObject();
    protected String receiptUrl = null;
    protected JSONObject customer = new JSONObject();
    protected PaydunyaSetup setup;
    protected PaydunyaCheckoutStore store;
    protected PaydunyaUtility utility;

    public PaydunyaCheckoutInvoice(PaydunyaSetup paramPaydunyaSetup, PaydunyaCheckoutStore paramPaydunyaCheckoutStore) {
        this.setup = paramPaydunyaSetup;
        this.store = paramPaydunyaCheckoutStore;
        this.utility = new PaydunyaUtility(paramPaydunyaSetup);
        this.cancelUrl = paramPaydunyaCheckoutStore.getCancelUrl();
        this.returnUrl = paramPaydunyaCheckoutStore.getReturnUrl();
        this.callbackUrl = paramPaydunyaCheckoutStore.getCallbackUrl();
    }

    public void setTotalAmount(double paramDouble) {
        this.totalAmount = paramDouble;
    }

    public double getTotalAmount() {
        return this.totalAmount;
    }

    public void setReturnUrl(String paramString) {
        this.returnUrl = paramString;
    }

    public String getReturnUrl() {
        return this.returnUrl;
    }

    public void setCancelUrl(String paramString) {
        this.cancelUrl = paramString;
    }

    public String getCancelUrl() {
        return this.cancelUrl;
    }

    public void setCallbackUrl(String paramString) {
        this.callbackUrl = paramString;
    }

    public String getCallbackUrl() {
        return this.callbackUrl;
    }

    public void setInvoiceUrl(String paramString) {
        this.invoiceUrl = paramString;
    }

    public String getInvoiceUrl() {
        return this.invoiceUrl;
    }

    public void setReceiptUrl(String paramString) {
        this.receiptUrl = paramString;
    }

    public String getReceiptUrl() {
        return this.receiptUrl;
    }

    public void setDescription(String paramString) {
        this.description = paramString;
    }

    public String getDescription() {
        return this.description;
    }

    public String getItems() {
        return this.items.toString();
    }

    public String getTaxes() {
        return this.taxes.toString();
    }

    public Object getCustomData(String paramString) {
        return this.customData.get(paramString);
    }

    public Object getCustomerInfo(String paramString) {
        return this.customer.get(paramString);
    }

    public void addItem(String paramString, int paramInt, double paramDouble1, double paramDouble2) {
        JSONObject localJSONObject = new JSONObject();
        localJSONObject.put("name", paramString);
        localJSONObject.put("quantity", paramInt);
        localJSONObject.put("unit_price", paramDouble1);
        localJSONObject.put("total_price", paramDouble2);
        this.items.put("item_" + this.itemsCount, localJSONObject);
        ++this.itemsCount;
    }

    public void addItem(String paramString1, int paramInt, double paramDouble1, double paramDouble2, String paramString2) {
        JSONObject localJSONObject = new JSONObject();
        localJSONObject.put("name", paramString1);
        localJSONObject.put("quantity", paramInt);
        localJSONObject.put("unit_price", paramDouble1);
        localJSONObject.put("total_price", paramDouble2);
        localJSONObject.put("description", paramString2);
        this.items.put("item_" + this.itemsCount, localJSONObject);
        ++this.itemsCount;
    }

    public void addTax(String paramString, double paramDouble) {
        JSONObject localJSONObject = new JSONObject();
        localJSONObject.put("name", paramString);
        localJSONObject.put("amount", paramDouble);
        this.taxes.put("tax_" + this.taxesCount, localJSONObject);
        ++this.taxesCount;
    }

    public void addCustomData(String paramString, Object paramObject) {
        this.customData.put(paramString, paramObject);
    }

    public boolean create() {
        JSONObject localJSONObject1 = new JSONObject();
        this.invoice.put("items", this.items);
        this.invoice.put("taxes", this.taxes);
        this.invoice.put("total_amount", this.getTotalAmount());
        this.invoice.put("description", this.getDescription());
        localJSONObject1.put("invoice", this.invoice);
        localJSONObject1.put("custom_data", this.customData);
        localJSONObject1.put("store", this.store.getSettings());
        this.actions.put("cancel_url", this.getCancelUrl());
        this.actions.put("return_url", this.getReturnUrl());
        this.actions.put("callback_url", this.getCallbackUrl());
        localJSONObject1.put("actions", this.actions);
        JSONObject localJSONObject2 = this.utility.jsonRequest(this.setup.getCheckoutInvoiceUrl(), localJSONObject1.toString());
        this.responseText = localJSONObject2.get("response_text").toString();
        this.responseCode = localJSONObject2.get("response_code").toString();
        if (this.responseCode.equals("00")) {
            this.token = localJSONObject2.get("token").toString();
            this.responseText = localJSONObject2.get("description").toString();
            this.setInvoiceUrl(localJSONObject2.get("response_text").toString());
            this.status = SUCCESS;
            return true;
        } else {
            this.status = FAIL;
            return false;
        }
    }

    public Boolean confirm(String paramString) {
        JSONObject localJSONObject = this.utility.getRequest(this.setup.getCheckoutConfirmUrl() + paramString);
        Boolean localBoolean = false;
        if (localJSONObject.size() > 0 && localJSONObject.get("response_code").equals("00")) {
            if (localJSONObject.get("status").toString().equals(COMPLETED)) {
                this.invoice = (JSONObject)localJSONObject.get("invoice");
                this.status = localJSONObject.get("status").toString();
                this.setReceiptUrl(localJSONObject.get("receipt_url").toString());
                this.customData = this.utility.pushJSON(localJSONObject.get("custom_data"));
                this.customer = this.utility.pushJSON(localJSONObject.get("customer"));
                this.taxes = this.utility.pushJSON(this.invoice.get("taxes"));
                this.items = this.utility.pushJSON(this.invoice.get("items"));
                this.setTotalAmount(Double.parseDouble(this.invoice.get("total_amount").toString()));
                this.responseText = "Checkout Invoice has been paid";
                this.responseCode = "00";
                localBoolean = true;
            } else {
                this.invoice = (JSONObject)localJSONObject.get("invoice");
                this.status = localJSONObject.get("status").toString();
                this.customData = this.utility.pushJSON(localJSONObject.get("custom_data"));
                this.customer = this.utility.pushJSON(localJSONObject.get("customer"));
                this.taxes = this.utility.pushJSON(this.invoice.get("taxes"));
                this.items = this.utility.pushJSON(this.invoice.get("items"));
                this.setTotalAmount(Double.parseDouble(this.invoice.get("total_amount").toString()));
                this.responseText = "Checkout Invoice has not been paid";
                this.responseCode = "1003";
            }
        } else {
            this.responseText = "Invoice Not Found";
            this.responseCode = "1002";
            this.status = FAIL;
        }

        return localBoolean;
    }
}

