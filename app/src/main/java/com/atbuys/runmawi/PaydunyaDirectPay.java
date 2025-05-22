package com.atbuys.runmawi;

//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//


import tk.json.simple.JSONObject;

public class PaydunyaDirectPay extends PaydunyaCheckout {
    protected PaydunyaSetup setup;
    protected PaydunyaUtility utility;

    public PaydunyaDirectPay(PaydunyaSetup paramPaydunyaSetup) {
        this.setup = paramPaydunyaSetup;
        this.utility = new PaydunyaUtility(paramPaydunyaSetup);
    }

    public boolean creditAccount(String paramString, double paramDouble) {
        JSONObject localJSONObject1 = new JSONObject();
        localJSONObject1.put("account_alias", paramString);
        localJSONObject1.put("amount", paramDouble);
        JSONObject localJSONObject2 = this.utility.jsonRequest(this.setup.getDirectPayCreditUrl(), localJSONObject1.toString());
        this.responseText = localJSONObject2.get("response_text").toString();
        this.responseCode = localJSONObject2.get("response_code").toString();
        if (this.responseCode.equals("00")) {
            this.transactionId = localJSONObject2.get("transaction_id").toString();
            this.description = localJSONObject2.get("description").toString();
            this.status = SUCCESS;
            return true;
        } else {
            this.status = FAIL;
            return false;
        }
    }
}

