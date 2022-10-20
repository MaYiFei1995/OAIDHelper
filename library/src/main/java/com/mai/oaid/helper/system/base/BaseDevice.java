package com.mai.oaid.helper.system.base;

import android.util.Pair;

import com.mai.oaid.helper.OAIDError;

public interface BaseDevice {

    boolean isSupport();

    Pair<String, OAIDError> getOAID() throws Exception;

}
