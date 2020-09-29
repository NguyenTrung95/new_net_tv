package net.sunniwell.sz.mop4.sdk.log;

import java.io.Serializable;
import org.xmlpull.p019v1.XmlPullParser;

/* renamed from: net.sunniwell.sz.mop4.sdk.log.LogBean */
public class LogBean implements Serializable {
    public static final int ACTION_LOG = 4;
    public static final int ERROR_LOG = 3;
    public static final int EXCEPTION_LOG = 2;
    public static final int EXECUTION_LOG = 1;
    public static final int MONITOR_LOG = 8;
    public static final int SERVER = 2;
    public static final int STATISTICS_LOG = 7;
    public static final int TERMINAL = 1;
    public static final int WARNING_LOG = 9;
    private static final long serialVersionUID = 1;
    private int cpu;
    private String description;
    private int disk_free;
    private int disk_total;
    private int disk_used;
    private int down_flow;
    private int downkb;
    private int duration_percent;
    private int duration_total;
    private int duration_watch;
    private long end_utc;
    private String extend = "unKnow";
    private String extra;

    /* renamed from: io */
    private int f339io;
    private boolean is_epg;
    private String mac;
    private String media_id;
    private int mem;
    private int mem_free;
    private int mem_total;
    private int mem_used;
    private int meta;
    private String name;
    private String new_version;
    private String ois;
    private String old_version;
    private String package_name;
    private String path;
    private String rwflag;
    private long serial;
    private long start_utc;
    private int subtype = 999;
    private String terminal_id;
    private int type = 1;
    private int up_flow;
    private String upgrade_type;
    private int upkb;
    private String url;
    private String user_id;

    /* renamed from: net.sunniwell.sz.mop4.sdk.log.LogBean$action_extend */
    public enum action_extend {
        login,
        logout,
        upgrade,
        standby,
        reboot,
        play,
        pause,
        resume,
        stop,
        install,
        favorite
    }

    /* renamed from: net.sunniwell.sz.mop4.sdk.log.LogBean$error_extend */
    public enum error_extend {
    }

    /* renamed from: net.sunniwell.sz.mop4.sdk.log.LogBean$exception_extend */
    public enum exception_extend {
        play,
        file,
        upgrade,
        install
    }

    /* renamed from: net.sunniwell.sz.mop4.sdk.log.LogBean$execution_extend */
    public enum execution_extend {
    }

    /* renamed from: net.sunniwell.sz.mop4.sdk.log.LogBean$monitor_extend */
    public enum monitor_extend {
        system
    }

    /* renamed from: net.sunniwell.sz.mop4.sdk.log.LogBean$statistics_extend */
    public enum statistics_extend {
        channel,
        vod,
        lunbo,
        ad,
        flow
    }

    /* renamed from: net.sunniwell.sz.mop4.sdk.log.LogBean$warning_extend */
    public enum warning_extend {
    }

    public int getType() {
        return this.type;
    }

    public void setType(int type2) {
        this.type = type2;
    }

    public int getSubtype() {
        return this.subtype;
    }

    public void setSubtype(int subtype2) {
        this.subtype = subtype2;
    }

    public String getExtend() {
        return this.extend;
    }

    public void setExtend(String extend2) {
        this.extend = extend2;
    }

    public String getTerminal_id() {
        return this.terminal_id;
    }

    public void setTerminal_id(String terminal_id2) {
        this.terminal_id = terminal_id2;
    }

    public String getUser_id() {
        return this.user_id;
    }

    public void setUser_id(String user_id2) {
        this.user_id = user_id2;
    }

    public String getMac() {
        return this.mac;
    }

    public void setMac(String mac2) {
        this.mac = mac2;
    }

    public String getOis() {
        return this.ois;
    }

    public void setOis(String ois2) {
        this.ois = ois2;
    }

    public String getUrl() {
        return this.url;
    }

    public void setUrl(String url2) {
        this.url = url2;
    }

    public String getName() {
        if (this.name == null) {
            this.name = XmlPullParser.NO_NAMESPACE;
        }
        return this.name;
    }

    public void setName(String name2) {
        this.name = name2;
    }

    public String getPackage_name() {
        return this.package_name;
    }

    public void setPackage_name(String package_name2) {
        this.package_name = package_name2;
    }

    public String getMedia_id() {
        return this.media_id;
    }

    public void setMedia_id(String media_id2) {
        this.media_id = media_id2;
    }

    public long getStart_utc() {
        return this.start_utc;
    }

    public void setStart_utc(long start_utc2) {
        this.start_utc = start_utc2;
    }

    public long getEnd_utc() {
        return this.end_utc;
    }

    public void setEnd_utc(long end_utc2) {
        this.end_utc = end_utc2;
    }

    public String getExtra() {
        return this.extra;
    }

    public void setExtra(String extra2) {
        this.extra = extra2;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description2) {
        this.description = description2;
    }

    public long getSerial() {
        return this.serial;
    }

    public void setSerial(long serial2) {
        this.serial = serial2;
    }

    public int getMeta() {
        return this.meta;
    }

    public void setMeta(int meta2) {
        this.meta = meta2;
    }

    public boolean isEpg() {
        return this.is_epg;
    }

    public void setEpg(boolean is_epg2) {
        this.is_epg = is_epg2;
    }

    public String getUpgrade_type() {
        return this.upgrade_type;
    }

    public void setUpgrade_type(String upgrade_type2) {
        this.upgrade_type = upgrade_type2;
    }

    public String getOld_version() {
        return this.old_version;
    }

    public void setOld_version(String old_version2) {
        this.old_version = old_version2;
    }

    public String getNew_version() {
        return this.new_version;
    }

    public void setNew_version(String new_version2) {
        this.new_version = new_version2;
    }

    public String getRwflag() {
        return this.rwflag;
    }

    public void setRwflag(String rwflag2) {
        this.rwflag = rwflag2;
    }

    public String getPath() {
        return this.path;
    }

    public void setPath(String path2) {
        this.path = path2;
    }

    public int getDuration_watch() {
        return this.duration_watch;
    }

    public void setDuration_watch(int duration_watch2) {
        this.duration_watch = duration_watch2;
    }

    public int getDuration_total() {
        return this.duration_total;
    }

    public void setDuration_total(int duration_total2) {
        this.duration_total = duration_total2;
    }

    public int getDuration_percent() {
        return this.duration_percent;
    }

    public void setDuration_percent(int duration_percent2) {
        this.duration_percent = duration_percent2;
    }

    public int getUp_flow() {
        return this.up_flow;
    }

    public void setUp_flow(int up_flow2) {
        this.up_flow = up_flow2;
    }

    public int getDown_flow() {
        return this.down_flow;
    }

    public void setDown_flow(int down_flow2) {
        this.down_flow = down_flow2;
    }

    public int getCpu() {
        return this.cpu;
    }

    public void setCpu(int cpu2) {
        this.cpu = cpu2;
    }

    public int getMem() {
        return this.mem;
    }

    public void setMem(int mem2) {
        this.mem = mem2;
    }

    public int getMem_total() {
        return this.mem_total;
    }

    public void setMem_total(int mem_total2) {
        this.mem_total = mem_total2;
    }

    public int getMem_used() {
        return this.mem_used;
    }

    public void setMem_used(int mem_used2) {
        this.mem_used = mem_used2;
    }

    public int getMem_free() {
        return this.mem_free;
    }

    public void setMem_free(int mem_free2) {
        this.mem_free = mem_free2;
    }

    public int getUpkb() {
        return this.upkb;
    }

    public void setUpkb(int upkb2) {
        this.upkb = upkb2;
    }

    public int getDownkb() {
        return this.downkb;
    }

    public void setDownkb(int downkb2) {
        this.downkb = downkb2;
    }

    public int getDisk_total() {
        return this.disk_total;
    }

    public void setDisk_total(int disk_total2) {
        this.disk_total = disk_total2;
    }

    public int getDisk_used() {
        return this.disk_used;
    }

    public void setDisk_used(int disk_used2) {
        this.disk_used = disk_used2;
    }

    public int getDisk_free() {
        return this.disk_free;
    }

    public void setDisk_free(int disk_free2) {
        this.disk_free = disk_free2;
    }

    public int getIo() {
        return this.f339io;
    }

    public void setIo(int io) {
        this.f339io = io;
    }
}
