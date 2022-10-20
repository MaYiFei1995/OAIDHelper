package com.mai.oaid.helper.system.impl;

import android.os.IBinder;
import android.os.Parcel;
import android.os.RemoteException;
import android.util.Pair;

import com.mai.oaid.helper.OAIDError;
import com.mai.oaid.helper.system.base.BaseIInterface;

/**
 * 酷派(Coolpad)
 */
@SuppressWarnings("unused")
public class CoolpadIInterface extends BaseIInterface {

    private static final String DESCRIPTOR = "com.coolpad.deviceidsupport.IDeviceIdManager";
    private final String packageName;

    public CoolpadIInterface(IBinder iBinder, String packageName) {
        super(iBinder);
        this.packageName = packageName;
    }

    @Override
    public boolean isSupport() throws RemoteException {
        return true;
    }

    public String getUDID() throws RemoteException {
        Parcel _data = Parcel.obtain();
        Parcel _reply = Parcel.obtain();
        String _result;
        try {
            _data.writeInterfaceToken(DESCRIPTOR);
            _data.writeString(packageName);
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
    public Pair<String, OAIDError> getOAID() throws RemoteException {
        Parcel _data = Parcel.obtain();
        Parcel _reply = Parcel.obtain();
        String _result;
        try {
            _data.writeInterfaceToken(DESCRIPTOR);
            _data.writeString(packageName);
            boolean _status = this.iBinder.transact(2, _data, _reply, 0);
            if (!_status) {
                return new Pair<>(null, OAIDError.STATUS_ERROR);
            }
            _reply.readException();
            return new Pair<>(_reply.readString(), null);
        } finally {
            _data.recycle();
            _reply.recycle();
        }
    }

    public String getVAID() throws RemoteException {
        Parcel _data = Parcel.obtain();
        Parcel _reply = Parcel.obtain();
        String _result;
        try {
            _data.writeInterfaceToken(DESCRIPTOR);
            _data.writeString(packageName);
            boolean _status = this.iBinder.transact(3, _data, _reply, 0);
            if (!_status) {
                return "";
            }
            _reply.readException();
            _result = _reply.readString();
        } finally {
            _reply.recycle();
            _data.recycle();
        }
        return _result;
    }

    public String getAAID() throws RemoteException {
        Parcel _data = Parcel.obtain();
        Parcel _reply = Parcel.obtain();
        String _result;
        try {
            _data.writeInterfaceToken(DESCRIPTOR);
            _data.writeString(packageName);
            boolean _status = this.iBinder.transact(4, _data, _reply, 0);
            if (!_status) {
                return "";
            }
            _reply.readException();
            _result = _reply.readString();
        } finally {
            _reply.recycle();
            _data.recycle();
        }
        return _result;
    }

    public String getIMEI() throws RemoteException {
        Parcel _data = Parcel.obtain();
        Parcel _reply = Parcel.obtain();
        String _result;
        try {
            _data.writeInterfaceToken(DESCRIPTOR);
            _data.writeString(packageName);
            boolean _status = this.iBinder.transact(5, _data, _reply, 0);
            if (!_status) {
                return "";
            }
            _reply.readException();
            _result = _reply.readString();
        } finally {
            _reply.recycle();
            _data.recycle();
        }
        return _result;
    }

    public boolean isCoolOs() throws RemoteException {
        Parcel _data = Parcel.obtain();
        Parcel _reply = Parcel.obtain();
        boolean _result;
        try {
            _data.writeInterfaceToken(DESCRIPTOR);
            boolean _status = this.iBinder.transact(6, _data, _reply, 0);
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

    public String getCoolOsVersion() throws RemoteException {
        Parcel _data = Parcel.obtain();
        Parcel _reply = Parcel.obtain();
        String _result;
        try {
            _data.writeInterfaceToken(DESCRIPTOR);
            _data.writeString(packageName);
            boolean _status = this.iBinder.transact(7, _data, _reply, 0);
            if (!_status) {
                return "";
            }
            _reply.readException();
            _result = _reply.readString();
        } finally {
            _reply.recycle();
            _data.recycle();
        }
        return _result;
    }

}
