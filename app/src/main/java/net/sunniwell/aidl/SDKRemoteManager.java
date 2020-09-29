package net.sunniwell.aidl;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.RemoteException;
import java.util.ArrayList;
import java.util.List;
import net.sunniwell.aidl.ISDKManagerService.Stub;
import net.sunniwell.aidl.bean.AdBean;
import net.sunniwell.aidl.bean.AreaBean;
import net.sunniwell.aidl.bean.CategoryBean;
import net.sunniwell.aidl.bean.EPGBean;
import net.sunniwell.aidl.bean.EPGUrlBean;
import net.sunniwell.aidl.bean.MediaBean;
import net.sunniwell.aidl.bean.MediaListBean;
import net.sunniwell.aidl.bean.RealUrlBean;
import net.sunniwell.app.linktaro.launcher.db.MailDbHelper;
import net.sunniwell.app.linktaro.tools.LogcatUtils;
import net.sunniwell.common.log.SWLogger;
import net.sunniwell.download.manager.DownLoadConfigUtil;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;
import org.json.JSONArray;
import org.json.JSONObject;

public class SDKRemoteManager {
    /* access modifiers changed from: private */
    public static SWLogger LOG = SWLogger.getLogger(SDKRemoteManager.class);
    private static SDKRemoteManager mSDKRemoteManager;
    private Context mContext;
    /* access modifiers changed from: private */
    public ISDKManagerService mSDKManagerService;
    private ServiceConnection mSDKServiceConnection = null;
    /* access modifiers changed from: private */
    public SDKRemoteCallBack mSdkRemoteCallBack;

    public interface SDKRemoteCallBack {
        void onServiceConnected(ComponentName componentName, IBinder iBinder);

        void onServiceDisconnected(ComponentName componentName);
    }

    private SDKRemoteManager(Context context, SDKRemoteCallBack sdkRemoteCallBack) {
        this.mContext = context;
        this.mSdkRemoteCallBack = sdkRemoteCallBack;
        bindSDKService();
    }

    public static SDKRemoteManager getInstance(Context context, SDKRemoteCallBack sdkRemoteCallBack) {
        LOG.mo8825d("getInstance");
        if (mSDKRemoteManager == null) {
            mSDKRemoteManager = new SDKRemoteManager(context, sdkRemoteCallBack);
        }
        return mSDKRemoteManager;
    }

    private void bindSDKService() {
        LOG.mo8825d("bindSDKService");
        this.mSDKServiceConnection = new ServiceConnection() {
            public void onServiceDisconnected(ComponentName name) {
                SDKRemoteManager.LOG.mo8825d("onServiceDisconnected");
                if (SDKRemoteManager.this.mSdkRemoteCallBack != null) {
                    SDKRemoteManager.this.mSdkRemoteCallBack.onServiceDisconnected(name);
                }
                SDKRemoteManager.this.mSDKManagerService = null;
            }

            public void onServiceConnected(ComponentName name, IBinder service) {
                SDKRemoteManager.this.mSDKManagerService = Stub.asInterface(service);
                SDKRemoteManager.LOG.mo8825d("onServiceConnected");
                if (SDKRemoteManager.this.mSdkRemoteCallBack != null) {
                    SDKRemoteManager.this.mSdkRemoteCallBack.onServiceConnected(name, service);
                }
            }
        };
        this.mContext.bindService(new Intent("net.sunniwell.mopsdk.WORK_SERVICE"), this.mSDKServiceConnection, 1);
    }

    public String getCurEpgUrl() {
        if (this.mSDKManagerService != null) {
            try {
                return this.mSDKManagerService.getCurEpgUrl();
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public String getCurOisUrl() {
        if (this.mSDKManagerService != null) {
            try {
                return this.mSDKManagerService.getCurOisUrl();
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public int enable() {
        if (this.mSDKManagerService != null) {
            try {
                return this.mSDKManagerService.enable();
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        return -1;
    }

    public int login() {
        LogcatUtils.m321d("starttoDoLoginAction and mSDKManagerService status is " + this.mSDKManagerService);
        if (this.mSDKManagerService != null) {
            try {
                return this.mSDKManagerService.login();
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        return -1;
    }

    public List<AdBean> getAdData(int columnId, String epg) {
        if (this.mSDKManagerService != null) {
            try {
                String result = this.mSDKManagerService.getAdData(columnId, epg);
                if (result == null) {
                    return null;
                }
                return (List) new ObjectMapper().readValue(result, (TypeReference) new TypeReference<List<AdBean>>() {
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public MediaListBean getMediaList(int columnId, int meta, String category, String area, String tag, String year, String title, String pinyin, String actor, String director, String sort, int pageindex, int pagesize, String lang, String epg) {
        if (this.mSDKManagerService != null) {
            try {
                String result = this.mSDKManagerService.getMediaList(columnId, meta, category, area, tag, year, title, pinyin, actor, director, sort, pageindex, pagesize, lang, epg);
                if (result == null) {
                    return null;
                }
                return (MediaListBean) new ObjectMapper().readValue(result, (TypeReference) new TypeReference<MediaListBean>() {
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public MediaListBean getCommandMediaList(int columnId, String mediaId, int size, String lang, String epg) {
        if (this.mSDKManagerService != null) {
            try {
                String result = this.mSDKManagerService.getConnendMediaList(columnId, mediaId, size, lang, epg);
                if (result == null) {
                    return null;
                }
                return (MediaListBean) new ObjectMapper().readValue(result, (TypeReference) new TypeReference<MediaListBean>() {
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public List<EPGBean> getRecordedProgram(String channelId, long utc, long endUtc, String lang, String epg) {
        if (this.mSDKManagerService != null) {
            try {
                return parseJsonToList(this.mSDKManagerService.getRecordedProgram(channelId, utc, endUtc, lang, epg), channelId);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public List<EPGBean> getCurentProgram(String channelId, long endUtc, String lang, String epg) {
        if (this.mSDKManagerService != null) {
            try {
                return parseJsonToList(this.mSDKManagerService.getCurentProgram(channelId, endUtc, lang, epg), channelId);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public String getOisToken() {
        if (this.mSDKManagerService != null) {
            try {
                return this.mSDKManagerService.getOisToken();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public String getEpgsToken() {
        if (this.mSDKManagerService != null) {
            try {
                return this.mSDKManagerService.getEpgsToken();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public List<EPGBean> getProgramByCategory(long utc, long endUtc, String lang, String type, String epg) {
        if (this.mSDKManagerService != null) {
            try {
                return parseJsonToList(this.mSDKManagerService.getProgramByCategory(utc, endUtc, lang, type, epg));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    private static List<EPGBean> parseJsonToList(String json, String channelId) {
        List<EPGBean> epgList = new ArrayList<>();
        if (json != null) {
            try {
                if (!json.isEmpty()) {
                    JSONArray ja = new JSONArray(json);
                    for (int i = 0; i < ja.length(); i++) {
                        JSONObject item = ja.getJSONObject(i);
                        if (item != null) {
                            EPGBean epgBean = new EPGBean();
                            epgBean.setUtc(item.getLong("utcMs"));
                            epgBean.setEndUtc(item.getLong("endUtcMs"));
                            epgBean.setTitle(item.getString("title"));
                            epgBean.setType(item.getString(MailDbHelper.TYPE));
                            epgBean.setDescription(item.getString("description"));
                            epgBean.setUrls(parseJsonToUrlList(item.getString("urls")));
                            epgBean.setChannelId(channelId);
                            epgList.add(epgBean);
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return epgList;
    }

    private static List<EPGBean> parseJsonToList(String json) {
        List<EPGBean> epgList = new ArrayList<>();
        if (json != null) {
            try {
                if (!json.isEmpty()) {
                    JSONArray ja = new JSONArray(json);
                    for (int i = 0; i < ja.length(); i++) {
                        JSONObject item = ja.getJSONObject(i);
                        if (item != null) {
                            EPGBean epgBean = new EPGBean();
                            epgBean.setUtc(item.getLong("utc"));
                            epgBean.setEndUtc(item.getLong("endUtc"));
                            epgBean.setTitle(item.getString("title"));
                            epgBean.setType(item.getString(MailDbHelper.TYPE));
                            epgBean.setDescription(item.getString("description"));
                            epgBean.setUrls(parseJsonToUrlList(item.getString("urls")));
                            epgBean.setChannelId(item.getString("channelId"));
                            epgList.add(epgBean);
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return epgList;
    }

    private static List<EPGUrlBean> parseJsonToUrlList(String json) {
        List<EPGUrlBean> urlList = new ArrayList<>();
        if (json != null) {
            try {
                if (!json.isEmpty()) {
                    JSONArray ja = new JSONArray(json);
                    for (int i = 0; i < ja.length(); i++) {
                        JSONObject item = ja.optJSONObject(i);
                        if (item != null) {
                            EPGUrlBean urlBean = new EPGUrlBean();
                            urlBean.setUrl(item.getString(DownLoadConfigUtil.KEY_URL));
                            urlBean.setDuration((long) item.getInt("duration"));
                            urlList.add(urlBean);
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return urlList;
    }

    public List<CategoryBean> getCategoryList(int columnId, String lang, String epg) {
        if (this.mSDKManagerService != null) {
            try {
                return (List) new ObjectMapper().readValue(this.mSDKManagerService.getCategoryList(columnId, lang, epg), (TypeReference) new TypeReference<List<CategoryBean>>() {
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public MediaBean getMediaDetail(int columnId, String mediaId, int pageindex, int pagesize, String provider, String lang, String epg) {
        if (this.mSDKManagerService != null) {
            try {
                return (MediaBean) new ObjectMapper().readValue(this.mSDKManagerService.getMediaDetail(columnId, mediaId, pageindex, pagesize, provider, lang, epg), (TypeReference) new TypeReference<MediaBean>() {
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public RealUrlBean getRealUrlBean(String url, int quality, String token, String epg) {
        if (this.mSDKManagerService != null) {
            try {
                return (RealUrlBean) new ObjectMapper().readValue(this.mSDKManagerService.getRealUrlBean(url, quality, token, epg), (TypeReference) new TypeReference<RealUrlBean>() {
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public int authen(String user, String terminal_id, String mediaId) {
        if (this.mSDKManagerService != null) {
            try {
                return this.mSDKManagerService.authen(user, terminal_id, mediaId);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return -1;
    }

    public List<AreaBean> getAreaList(String lang) {
        if (this.mSDKManagerService != null) {
            try {
                return (List) new ObjectMapper().readValue(this.mSDKManagerService.getAreaList(lang), (TypeReference) new TypeReference<List<AreaBean>>() {
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public String getUserName(String user) {
        if (this.mSDKManagerService != null) {
            try {
                return this.mSDKManagerService.getUserName(user);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public long getValidtoUtc(String user) {
        if (this.mSDKManagerService != null) {
            try {
                return this.mSDKManagerService.getValidtoUtc(user);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return -1;
    }

    public String getUserSubscribe(String user) {
        if (this.mSDKManagerService != null) {
            try {
                return this.mSDKManagerService.getUserSubscribe(user);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public void finish() {
        this.mContext.unbindService(this.mSDKServiceConnection);
        this.mSDKServiceConnection = null;
        this.mSDKManagerService = null;
        this.mSdkRemoteCallBack = null;
    }
}
