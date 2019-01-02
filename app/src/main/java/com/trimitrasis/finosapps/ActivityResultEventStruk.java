package com.trimitrasis.finosapps;

import android.content.Intent;

/**
 * Created by rahman on 22/07/2017.
 */

public class ActivityResultEventStruk {

    private int requestCode;
    private int resultCode;
    private Intent data;

    public ActivityResultEventStruk(int requestCode, int resultCode, Intent data) {
        this.requestCode = requestCode;
        this.resultCode = resultCode;
        this.data = data;
    }

    public int getRequestCode() {
        return requestCode;
    }

    public void setRequestCode(int requestCode) {
        this.requestCode = requestCode;
    }

    public Intent getData() {
        return data;
    }

    public void setData(Intent data) {
        this.data = data;
    }

    public int getResultCode() {
        return resultCode;
    }

    public void setResultCode(int resultCode) {
        this.resultCode = resultCode;
    }

}
