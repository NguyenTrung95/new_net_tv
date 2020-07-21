package com.eliving.tv.aidl;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

public interface ISDKManagerService extends IInterface {

    public static abstract class Stub extends Binder implements ISDKManagerService {
        private static final String DESCRIPTOR = "net.sunniwell.aidl.ISDKManagerService";
        static final int TRANSACTION_authen = 16;
        static final int TRANSACTION_enable = 5;
        static final int TRANSACTION_getAdData = 7;
        static final int TRANSACTION_getAreaList = 17;
        static final int TRANSACTION_getCategoryList = 12;
        static final int TRANSACTION_getConnendMediaList = 15;
        static final int TRANSACTION_getCurEpgUrl = 1;
        static final int TRANSACTION_getCurOisUrl = 2;
        static final int TRANSACTION_getCurentProgram = 10;
        static final int TRANSACTION_getEpgsToken = 3;
        static final int TRANSACTION_getMediaDetail = 13;
        static final int TRANSACTION_getMediaList = 8;
        static final int TRANSACTION_getOisToken = 4;
        static final int TRANSACTION_getProgramByCategory = 11;
        static final int TRANSACTION_getRealUrlBean = 14;
        static final int TRANSACTION_getRecordedProgram = 9;
        static final int TRANSACTION_getUserName = 18;
        static final int TRANSACTION_getUserSubscribe = 20;
        static final int TRANSACTION_getValidtoUtc = 19;
        static final int TRANSACTION_login = 6;

        private static class Proxy implements ISDKManagerService {
            private IBinder mRemote;

            Proxy(IBinder remote) {
                this.mRemote = remote;
            }

            public IBinder asBinder() {
                return this.mRemote;
            }

            public String getInterfaceDescriptor() {
                return Stub.DESCRIPTOR;
            }

            public String getCurEpgUrl() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(1, _data, _reply, 0);
                    _reply.readException();
                    return _reply.readString();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public String getCurOisUrl() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(2, _data, _reply, 0);
                    _reply.readException();
                    return _reply.readString();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public String getEpgsToken() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(3, _data, _reply, 0);
                    _reply.readException();
                    return _reply.readString();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public String getOisToken() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(4, _data, _reply, 0);
                    _reply.readException();
                    return _reply.readString();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public int enable() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(5, _data, _reply, 0);
                    _reply.readException();
                    return _reply.readInt();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public int login() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(6, _data, _reply, 0);
                    _reply.readException();
                    return _reply.readInt();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public String getAdData(int type, String epg) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(type);
                    _data.writeString(epg);
                    this.mRemote.transact(7, _data, _reply, 0);
                    _reply.readException();
                    return _reply.readString();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public String getMediaList(int columnId, int meta, String category, String area, String tag, String year, String title, String pinyin, String actor, String director, String sort, int pageindex, int pagesize, String lang, String epg) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(columnId);
                    _data.writeInt(meta);
                    _data.writeString(category);
                    _data.writeString(area);
                    _data.writeString(tag);
                    _data.writeString(year);
                    _data.writeString(title);
                    _data.writeString(pinyin);
                    _data.writeString(actor);
                    _data.writeString(director);
                    _data.writeString(sort);
                    _data.writeInt(pageindex);
                    _data.writeInt(pagesize);
                    _data.writeString(lang);
                    _data.writeString(epg);
                    this.mRemote.transact(8, _data, _reply, 0);
                    _reply.readException();
                    return _reply.readString();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public String getRecordedProgram(String channelId, long utc, long endUtc, String lang, String epg) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(channelId);
                    _data.writeLong(utc);
                    _data.writeLong(endUtc);
                    _data.writeString(lang);
                    _data.writeString(epg);
                    this.mRemote.transact(9, _data, _reply, 0);
                    _reply.readException();
                    return _reply.readString();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public String getCurentProgram(String channelId, long utc, String lang, String epg) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(channelId);
                    _data.writeLong(utc);
                    _data.writeString(lang);
                    _data.writeString(epg);
                    this.mRemote.transact(10, _data, _reply, 0);
                    _reply.readException();
                    return _reply.readString();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public String getProgramByCategory(long utc, long endUtc, String lang, String type, String epg) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeLong(utc);
                    _data.writeLong(endUtc);
                    _data.writeString(lang);
                    _data.writeString(type);
                    _data.writeString(epg);
                    this.mRemote.transact(11, _data, _reply, 0);
                    _reply.readException();
                    return _reply.readString();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public String getCategoryList(int columnId, String lang, String epg) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(columnId);
                    _data.writeString(lang);
                    _data.writeString(epg);
                    this.mRemote.transact(12, _data, _reply, 0);
                    _reply.readException();
                    return _reply.readString();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public String getMediaDetail(int columnId, String mediaId, int pageindex, int pagesize, String provider, String lang, String epg) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(columnId);
                    _data.writeString(mediaId);
                    _data.writeInt(pageindex);
                    _data.writeInt(pagesize);
                    _data.writeString(provider);
                    _data.writeString(lang);
                    _data.writeString(epg);
                    this.mRemote.transact(13, _data, _reply, 0);
                    _reply.readException();
                    return _reply.readString();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public String getRealUrlBean(String url, int quality, String token, String epg) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(url);
                    _data.writeInt(quality);
                    _data.writeString(token);
                    _data.writeString(epg);
                    this.mRemote.transact(14, _data, _reply, 0);
                    _reply.readException();
                    return _reply.readString();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public String getConnendMediaList(int columnId, String mediaId, int size, String lang, String epg) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(columnId);
                    _data.writeString(mediaId);
                    _data.writeInt(size);
                    _data.writeString(lang);
                    _data.writeString(epg);
                    this.mRemote.transact(15, _data, _reply, 0);
                    _reply.readException();
                    return _reply.readString();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public int authen(String user, String terminal_id, String mediaId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(user);
                    _data.writeString(terminal_id);
                    _data.writeString(mediaId);
                    this.mRemote.transact(16, _data, _reply, 0);
                    _reply.readException();
                    return _reply.readInt();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public String getAreaList(String lang) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(lang);
                    this.mRemote.transact(17, _data, _reply, 0);
                    _reply.readException();
                    return _reply.readString();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public String getUserName(String user) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(user);
                    this.mRemote.transact(18, _data, _reply, 0);
                    _reply.readException();
                    return _reply.readString();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public long getValidtoUtc(String user) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(user);
                    this.mRemote.transact(19, _data, _reply, 0);
                    _reply.readException();
                    return _reply.readLong();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public String getUserSubscribe(String user) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(user);
                    this.mRemote.transact(20, _data, _reply, 0);
                    _reply.readException();
                    return _reply.readString();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }
        }

        public Stub() {
            attachInterface(this, DESCRIPTOR);
        }

        public static ISDKManagerService asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin == null || !(iin instanceof ISDKManagerService)) {
                return new Proxy(obj);
            }
            return (ISDKManagerService) iin;
        }

        public IBinder asBinder() {
            return this;
        }

        public boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
            switch (code) {
                case 1:
                    data.enforceInterface(DESCRIPTOR);
                    String _result = getCurEpgUrl();
                    reply.writeNoException();
                    reply.writeString(_result);
                    return true;
                case 2:
                    data.enforceInterface(DESCRIPTOR);
                    String _result2 = getCurOisUrl();
                    reply.writeNoException();
                    reply.writeString(_result2);
                    return true;
                case 3:
                    data.enforceInterface(DESCRIPTOR);
                    String _result3 = getEpgsToken();
                    reply.writeNoException();
                    reply.writeString(_result3);
                    return true;
                case 4:
                    data.enforceInterface(DESCRIPTOR);
                    String _result4 = getOisToken();
                    reply.writeNoException();
                    reply.writeString(_result4);
                    return true;
                case 5:
                    data.enforceInterface(DESCRIPTOR);
                    int _result5 = enable();
                    reply.writeNoException();
                    reply.writeInt(_result5);
                    return true;
                case 6:
                    data.enforceInterface(DESCRIPTOR);
                    int _result6 = login();
                    reply.writeNoException();
                    reply.writeInt(_result6);
                    return true;
                case 7:
                    data.enforceInterface(DESCRIPTOR);
                    String _result7 = getAdData(data.readInt(), data.readString());
                    reply.writeNoException();
                    reply.writeString(_result7);
                    return true;
                case 8:
                    data.enforceInterface(DESCRIPTOR);
                    String _result8 = getMediaList(data.readInt(), data.readInt(), data.readString(), data.readString(), data.readString(), data.readString(), data.readString(), data.readString(), data.readString(), data.readString(), data.readString(), data.readInt(), data.readInt(), data.readString(), data.readString());
                    reply.writeNoException();
                    reply.writeString(_result8);
                    return true;
                case 9:
                    data.enforceInterface(DESCRIPTOR);
                    String _result9 = getRecordedProgram(data.readString(), data.readLong(), data.readLong(), data.readString(), data.readString());
                    reply.writeNoException();
                    reply.writeString(_result9);
                    return true;
                case 10:
                    data.enforceInterface(DESCRIPTOR);
                    String _result10 = getCurentProgram(data.readString(), data.readLong(), data.readString(), data.readString());
                    reply.writeNoException();
                    reply.writeString(_result10);
                    return true;
                case 11:
                    data.enforceInterface(DESCRIPTOR);
                    String _result11 = getProgramByCategory(data.readLong(), data.readLong(), data.readString(), data.readString(), data.readString());
                    reply.writeNoException();
                    reply.writeString(_result11);
                    return true;
                case 12:
                    data.enforceInterface(DESCRIPTOR);
                    String _result12 = getCategoryList(data.readInt(), data.readString(), data.readString());
                    reply.writeNoException();
                    reply.writeString(_result12);
                    return true;
                case 13:
                    data.enforceInterface(DESCRIPTOR);
                    String _result13 = getMediaDetail(data.readInt(), data.readString(), data.readInt(), data.readInt(), data.readString(), data.readString(), data.readString());
                    reply.writeNoException();
                    reply.writeString(_result13);
                    return true;
                case 14:
                    data.enforceInterface(DESCRIPTOR);
                    String _result14 = getRealUrlBean(data.readString(), data.readInt(), data.readString(), data.readString());
                    reply.writeNoException();
                    reply.writeString(_result14);
                    return true;
                case 15:
                    data.enforceInterface(DESCRIPTOR);
                    String _result15 = getConnendMediaList(data.readInt(), data.readString(), data.readInt(), data.readString(), data.readString());
                    reply.writeNoException();
                    reply.writeString(_result15);
                    return true;
                case 16:
                    data.enforceInterface(DESCRIPTOR);
                    int _result16 = authen(data.readString(), data.readString(), data.readString());
                    reply.writeNoException();
                    reply.writeInt(_result16);
                    return true;
                case 17:
                    data.enforceInterface(DESCRIPTOR);
                    String _result17 = getAreaList(data.readString());
                    reply.writeNoException();
                    reply.writeString(_result17);
                    return true;
                case 18:
                    data.enforceInterface(DESCRIPTOR);
                    String _result18 = getUserName(data.readString());
                    reply.writeNoException();
                    reply.writeString(_result18);
                    return true;
                case 19:
                    data.enforceInterface(DESCRIPTOR);
                    long _result19 = getValidtoUtc(data.readString());
                    reply.writeNoException();
                    reply.writeLong(_result19);
                    return true;
                case 20:
                    data.enforceInterface(DESCRIPTOR);
                    String _result20 = getUserSubscribe(data.readString());
                    reply.writeNoException();
                    reply.writeString(_result20);
                    return true;
                case 1598968902:
                    reply.writeString(DESCRIPTOR);
                    return true;
                default:
                    return super.onTransact(code, data, reply, flags);
            }
        }
    }

    int authen(String str, String str2, String str3) throws RemoteException;

    int enable() throws RemoteException;

    String getAdData(int i, String str) throws RemoteException;

    String getAreaList(String str) throws RemoteException;

    String getCategoryList(int i, String str, String str2) throws RemoteException;

    String getConnendMediaList(int i, String str, int i2, String str2, String str3) throws RemoteException;

    String getCurEpgUrl() throws RemoteException;

    String getCurOisUrl() throws RemoteException;

    String getCurentProgram(String str, long j, String str2, String str3) throws RemoteException;

    String getEpgsToken() throws RemoteException;

    String getMediaDetail(int i, String str, int i2, int i3, String str2, String str3, String str4) throws RemoteException;

    String getMediaList(int i, int i2, String str, String str2, String str3, String str4, String str5, String str6, String str7, String str8, String str9, int i3, int i4, String str10, String str11) throws RemoteException;

    String getOisToken() throws RemoteException;

    String getProgramByCategory(long j, long j2, String str, String str2, String str3) throws RemoteException;

    String getRealUrlBean(String str, int i, String str2, String str3) throws RemoteException;

    String getRecordedProgram(String str, long j, long j2, String str2, String str3) throws RemoteException;

    String getUserName(String str) throws RemoteException;

    String getUserSubscribe(String str) throws RemoteException;

    long getValidtoUtc(String str) throws RemoteException;

    int login() throws RemoteException;
}
