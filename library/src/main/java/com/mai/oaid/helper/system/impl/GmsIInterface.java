package com.mai.oaid.helper.system.impl;

import android.os.IBinder;
import android.os.Parcel;
import android.os.RemoteException;
import android.util.Pair;

import com.mai.oaid.helper.OAIDError;
import com.mai.oaid.helper.system.base.BaseIInterface;

/**
 * Google
 */
public class GmsIInterface extends BaseIInterface {

    private static final String DESCRIPTOR = "com.google.android.gms.ads.identifier.internal.IAdvertisingIdService";

    public GmsIInterface(IBinder iBinder) {
        super(iBinder);
    }

    @Override
    public boolean isSupport() throws RemoteException {
        return isLimitAdTrackingEnabled(true);
    }

    @Override
    public Pair<String, OAIDError> getOAID() throws RemoteException {
        return getId();
    }

    private Pair<String, OAIDError> getId() throws RemoteException {
        Parcel _data = Parcel.obtain();
        Parcel _reply = Parcel.obtain();
        try {
            _data.writeInterfaceToken(DESCRIPTOR);
            boolean _status = this.iBinder.transact(1, _data, _reply, 0);
            if (!_status) {
                return new Pair<>(null, OAIDError.STATUS_ERROR);
            }
            _reply.readException();
            return new Pair<>(_reply.readString(), null);
        } finally {
            _reply.recycle();
            _data.recycle();
        }
    }

    public boolean isLimitAdTrackingEnabled(boolean bool) throws RemoteException {
        Parcel _data = Parcel.obtain();
        Parcel _reply = Parcel.obtain();
        boolean _result;
        try {
            _data.writeInterfaceToken(DESCRIPTOR);
            _data.writeInt(bool ? 1 : 0);
            boolean _status = this.iBinder.transact(2, _data, _reply, 0);
            if (!_status) {
                return false;
            }
            _reply.readException();
            _result = _reply.readInt() != 0;
        } finally {
            _reply.recycle();
            _data.recycle();
        }
        return _result;
    }

}
