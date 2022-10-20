package com.mai.oaid.helper.system.impl;

import android.os.IBinder;
import android.os.Parcel;
import android.os.RemoteException;

import com.mai.oaid.helper.system.base.BaseIInterface;

/**
 * 华硕(Asus)
 */
@SuppressWarnings("unused")
public class AsusIInterface extends BaseIInterface {

    private static final String DESCRIPTOR = "com.asus.msa.SupplementaryDID.IDidAidlInterface";

    public AsusIInterface(IBinder iBinder) {
        super(iBinder);
    }

    public boolean isSupport() throws RemoteException {
        Parcel _data = Parcel.obtain();
        Parcel _reply = Parcel.obtain();
        boolean _result;
        try {
            _data.writeInterfaceToken(DESCRIPTOR);
            boolean _status = this.iBinder.transact(1, _data, _reply, 0);
            if (!_status) {
                return false;
            }
            _reply.readException();
            _result = _reply.readInt() != 0;
        } finally {
            _data.recycle();
            _reply.recycle();
        }
        return _result;
    }

    public String getUDID() throws RemoteException {
        Parcel _data = Parcel.obtain();
        Parcel _reply = Parcel.obtain();
        String _result;
        try {
            _data.writeInterfaceToken(DESCRIPTOR);
            this.iBinder.transact(2, _data, _reply, 0);
            _reply.readException();
            _result = _reply.readString();
        } finally {
            _reply.recycle();
            _data.recycle();
        }
        return _result;
    }

    @Override
    public String getOAID() throws RemoteException {
        Parcel _data = Parcel.obtain();
        Parcel _reply = Parcel.obtain();
        String _result;
        try {
            _data.writeInterfaceToken(DESCRIPTOR);
            boolean _status = this.iBinder.transact(3, _data, _reply, 0);
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

    public String getVAID() throws RemoteException {
        Parcel _data = Parcel.obtain();
        Parcel _reply = Parcel.obtain();
        String _result;
        try {
            _data.writeInterfaceToken(DESCRIPTOR);
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

    public String getAAID() throws RemoteException {
        Parcel _data = Parcel.obtain();
        Parcel _reply = Parcel.obtain();
        String _result;
        try {
            _data.writeInterfaceToken(DESCRIPTOR);
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

}