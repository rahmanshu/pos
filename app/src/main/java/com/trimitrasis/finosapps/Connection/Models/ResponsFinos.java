package com.trimitrasis.finosapps.Connection.Models;

import java.io.Serializable;

/**
 * Created by rahman on 06/03/2017.
 */

public class ResponsFinos implements Serializable {

    private static final long serialVersionUID = 1293796654083049938L;

    protected String code;
    protected String message;
    protected String result;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }
}
