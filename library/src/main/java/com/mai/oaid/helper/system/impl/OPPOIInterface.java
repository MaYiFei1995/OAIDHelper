package com.mai.oaid.helper.system.impl;

import android.os.IBinder;
import android.os.Parcel;
import android.os.RemoteException;

import com.mai.oaid.helper.system.base.BaseIInterface;

@SuppressWarnings("unused")
public class OPPOIInterface extends BaseIInterface {

    public OPPOIInterface(IBinder iBinder) {
        super(iBinder);
    }

    @Override
    public String getOAID() {
        return "";
    }

    @Override
    public String getSerID(String packageName, String signatureSha1, String type) throws RemoteException {
        Parcel _data = Parcel.obtain();
        Parcel _reply = Parcel.obtain();
        String _result;
        try {
            _data.writeInterfaceToken("com.heytap.openid.IOpenID");
            _data.writeString(packageName);
            _data.writeString(signatureSha1);
            _data.writeString(type);
            boolean _status = this.iBinder.transact(1, _data, _reply, 0);
            if (!_status) {
                return "";
            }
            _reply.readException();
            _result = _reply.readString();
        } finally {
            _data.recycle();
            _reply.recycle();
        }
        return _result;
    }

}