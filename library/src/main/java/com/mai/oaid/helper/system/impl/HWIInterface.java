package com.mai.oaid.helper.system.impl;

import android.os.IBinder;
import android.os.Parcel;
import android.os.RemoteException;

import com.mai.oaid.helper.system.base.BaseIInterface;

@SuppressWarnings("unused")
public class HWIInterface extends BaseIInterface {

    private static final String DESCRIPTOR = "com.uodis.opendevice.aidl.OpenDeviceIdentifierService";

    public HWIInterface(IBinder iBinder) {
        super(iBinder);
    }

    @Override
    public String getOAID() throws RemoteException {
        Parcel _data = Parcel.obtain();
        Parcel _reply = Parcel.obtain();
        String _result;
        try {
            _data.writeInterfaceToken(DESCRIPTOR);
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

    @Override
    public boolean isOaidTrackLimited() throws RemoteException {
        Parcel _data = Parcel.obtain();
        Parcel _reply = Parcel.obtain();
        boolean _result;
        try {
            _data.writeInterfaceToken(DESCRIPTOR);
            boolean _status = this.iBinder.transact(2, _data, _reply, 0);
            if (!_status) {
                return true;
            }
            _reply.readException();
            _result = _reply.readInt() == 0;
        } finally {
            _data.recycle();
            _reply.recycle();
        }
        return _result;
    }

}
