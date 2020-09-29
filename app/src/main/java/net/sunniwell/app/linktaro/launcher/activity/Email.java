package net.sunniwell.app.linktaro.launcher.activity;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnKeyListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.fragment.app.FragmentActivity;

import com.bumptech.glide.Glide;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import net.sunniwell.app.linktaro.R;
import net.sunniwell.app.linktaro.launcher.db.MailDbHelper;
import net.sunniwell.app.linktaro.tools.LogcatUtils;
import net.sunniwell.common.tools.DateTime;
import org.xmlpull.v1.XmlPullParser;

public class Email extends FragmentActivity {
    private static final int DELETE_SUCCESS = 1;
    private final int DELETE = 5;
    private final int FRESH_DATA = 1;
    private final int FRESH_TIME = 4;
    private final int RELOAD_MAIL = 0;
    private final int SHOW_DATE = 3;
    private final int SHOW_MAIL = 2;
    private final int UNREAD = 6;
    /* access modifiers changed from: private */
    public String falg;
    /* access modifiers changed from: private */
    public MoreVodAdapter mAdapter;
    /* access modifiers changed from: private */
    public String mDateStr;
    /* access modifiers changed from: private */
    public TextView mEmailUnRead;
    /* access modifiers changed from: private */
    public TextView mEmailall;
    /* access modifiers changed from: private */
    public ListView mListView;
    private MailDbHelper mMailHelper;
    /* access modifiers changed from: private */
    public List<Map<String, String>> mMailList;
    /* access modifiers changed from: private */
    public String mTtimeStr;
    /* access modifiers changed from: private */
    public TextView mTvContent;
    /* access modifiers changed from: private */
    public TextView mTvDate;
    private TextView mTvDateDetails;
    private TextView mTvTime;
    /* access modifiers changed from: private */
    public TextView mTvTitle;
    Handler mUiHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    Email.this.mAdapter.setList(Email.this.mMailList);
                    Email.this.mAdapter.notifyDataSetChanged();
                    Email.this.mListView.requestFocus();
                    return;
                case 2:
                    LogcatUtils.m321d("SHOW_MAIL");
                    Email.this.showMail((Map) msg.obj);
                    return;
                case 3:
                    LogcatUtils.m321d("SHOW_DATE");
                    Email.this.mTvDate.setText(Email.this.mDateStr);
                    return;
                case 4:
                    LogcatUtils.m321d("FRESH_TIME");
                    return;
                case 5:
                    LogcatUtils.m321d("DELETE MAIL");
                    Email.this.mTvContent.setText(XmlPullParser.NO_NAMESPACE);
                    Email.this.mTvTitle.setText(XmlPullParser.NO_NAMESPACE);
                    return;
                case 6:
                    LogcatUtils.m321d("UNREAD MAIL");
                    Email.this.mEmailall.setText(new StringBuilder(String.valueOf(Email.this.getResources().getString(R.string.email_total))).append(Email.this.mMailList.size()).append(Email.this.getResources().getString(R.string.email_num)).toString());
                    Email.this.mEmailUnRead.setText(new StringBuilder(String.valueOf(Email.this.getResources().getString(R.string.email_no_read))).append(msg.arg1).append(Email.this.getResources().getString(R.string.email_num)).toString());
                    return;
                default:
                    return;
            }
        }
    };
    private WorkHandler mWorkHander;
    /* access modifiers changed from: private */
    public int unread = 0;

    private class MoreVodAdapter extends BaseAdapter implements OnItemClickListener, OnItemSelectedListener, OnKeyListener {
        private Context context;
        private List<Map<String, String>> list;

        public MoreVodAdapter(Context context2, List<Map<String, String>> list2) {
            this.context = context2;
            this.list = list2;
        }

        public void setList(List<Map<String, String>> list2) {
            this.list = list2;
        }

        public int getCount() {
            if (this.list == null || this.list.size() <= 0) {
                return 0;
            }
            return this.list.size();
        }

        public Object getItem(int position) {
            if (this.list == null || this.list.size() <= 0) {
                return null;
            }
            return this.list.get(position);
        }

        public long getItemId(int position) {
            return (long) position;
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            View v;
            LayoutInflater inflater = LayoutInflater.from(this.context);
            if (convertView == null) {
                v = inflater.inflate(R.layout.mail_list_item, null);
            } else {
                v = convertView;
            }
            ImageView imgFlag = (ImageView) v.findViewById(R.id.flag);
            TextView dateView = (TextView) v.findViewById(R.id.date_mail);
            TextView contentView = (TextView) v.findViewById(R.id.mail_content);
            TextView titleView = (TextView) v.findViewById(R.id.mail_title);
            Map<String, String> mail = null;
            if (position < Email.this.mMailList.size()) {
                mail = (Map) Email.this.mMailList.get(position);
            }
            if (mail != null && mail.size() > 0) {
                Email.this.falg = (String) mail.get(MailDbHelper.FLAG);
                String date = (String) mail.get(MailDbHelper.DATE);
                String name = (String) mail.get("name");
                String content = (String) mail.get(MailDbHelper.CONTENT);
                if (Email.this.falg.equals("0")) {
                    Glide.with((FragmentActivity) Email.this).load(Integer.valueOf(R.drawable.email_unchecked)).into(imgFlag);
                    Email email = Email.this;
                    email.unread = email.unread + 1;
                } else if (Email.this.falg.equals("1")) {
                    Glide.with((FragmentActivity) Email.this).load(Integer.valueOf(R.drawable.email_open)).into(imgFlag);
                }
                Message msg = new Message();
                msg.arg1 = Email.this.unread;
                msg.what = 6;
                Email.this.mUiHandler.sendEmptyMessage(msg.what);
                dateView.setText(date);
                titleView.setText(name);
                contentView.setText(content);
            }
            return v;
        }

        public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
            Email.this.mUiHandler.removeMessages(2);
            Map<String, String> mail = (Map) Email.this.mListView.getItemAtPosition(position);
            Message msg = Message.obtain();
            msg.what = 2;
            msg.obj = mail;
            Email.this.mUiHandler.sendMessage(msg);
            Glide.with((FragmentActivity) Email.this).load(Integer.valueOf(R.drawable.email_open)).into((ImageView) view.findViewById(R.id.flag));
        }

        public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
            Email.this.mUiHandler.removeMessages(2);
            Map<String, String> mail = (Map) Email.this.mListView.getItemAtPosition(position);
            Message msg = Message.obtain();
            msg.what = 2;
            msg.obj = mail;
            Email.this.mUiHandler.sendMessage(msg);
            Glide.with((FragmentActivity) Email.this).load(Integer.valueOf(R.drawable.email_open)).into((ImageView) view.findViewById(R.id.flag));
        }

        public void onNothingSelected(AdapterView<?> adapterView) {
        }

        public boolean onKey(View v, int keyCode, KeyEvent event) {
            if (keyCode == 92 || keyCode == 93) {
                return true;
            }
            return false;
        }
    }

    private class WorkHandler extends Handler {
        public WorkHandler(Looper looper) {
            super(looper);
        }

        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    Email.this.mMailList = Email.this.getMails();
                    Email.this.mUiHandler.sendEmptyMessage(1);
                    return;
                default:
                    return;
            }
        }
    }

    public /* bridge */ /* synthetic */ View onCreateView(View view, String str, Context context, AttributeSet attributeSet) {
        return super.onCreateView(view, str, context, attributeSet);
    }

    public /* bridge */ /* synthetic */ View onCreateView(String str, Context context, AttributeSet attributeSet) {
        return super.onCreateView(str, context, attributeSet);
    }

    /* access modifiers changed from: protected */
    public void onCreate(Bundle savedInstanceState) {
        LogcatUtils.m321d("...onCreate...");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mail);
        initView();
        initDate();
        this.mMailList = new ArrayList();
        this.mMailHelper = new MailDbHelper(this, null, null, 1);
        HandlerThread handlerThread = new HandlerThread("workhandler");
        handlerThread.start();
        this.mWorkHander = new WorkHandler(handlerThread.getLooper());
        this.mWorkHander.sendEmptyMessage(0);
    }

    @SuppressLint({"SimpleDateFormat"})
    private void initDate() {
        this.mDateStr = new SimpleDateFormat("MM月dd日(EEEE)").format(new Date(System.currentTimeMillis()));
        this.mUiHandler.sendEmptyMessage(3);
        initTime();
    }

    private void initTime() {
        new Timer().schedule(new TimerTask() {
            public void run() {
                Calendar calendar = Calendar.getInstance();
                int hour = calendar.get(11);
                int minute = calendar.get(12);
                Email.this.mTtimeStr = (hour < 10 ? "0" + hour : Integer.valueOf(hour)) + ":" + (minute < 10 ? "0" + minute : Integer.valueOf(minute));
                Email.this.mUiHandler.sendEmptyMessage(4);
            }
        }, 100, DateTime.MILLIS_PER_MINUTE);
    }

    /* access modifiers changed from: private */
    public List<Map<String, String>> getMails() {
        List<Map<String, String>> list = new ArrayList<>();
        Cursor allDataCursor = null;
        try {
            if (!MailDbHelper.isOpen()) {
                this.mMailHelper.openDb();
            }
            Cursor allDataCursor2 = this.mMailHelper.queryData();
            list.clear();
            while (allDataCursor2.moveToNext()) {
                String id = allDataCursor2.getString(allDataCursor2.getColumnIndex("_id"));
                String name = allDataCursor2.getString(allDataCursor2.getColumnIndex("name"));
                String date = allDataCursor2.getString(allDataCursor2.getColumnIndex(MailDbHelper.DATE));
                String dateDetails = allDataCursor2.getString(allDataCursor2.getColumnIndex(MailDbHelper.DATE_DETAILS));
                String content = allDataCursor2.getString(allDataCursor2.getColumnIndex(MailDbHelper.CONTENT));
                String flag = allDataCursor2.getString(allDataCursor2.getColumnIndex(MailDbHelper.FLAG));
                String subname = allDataCursor2.getString(allDataCursor2.getColumnIndex(MailDbHelper.SUB_NAME));
                String type = allDataCursor2.getString(allDataCursor2.getColumnIndex(MailDbHelper.TYPE));
                Map<String, String> data = new HashMap<>();
                data.put("id", id);
                data.put(MailDbHelper.FLAG, flag);
                data.put(MailDbHelper.TYPE, type);
                data.put(MailDbHelper.DATE, date);
                data.put(MailDbHelper.DATE_DETAILS, dateDetails);
                data.put("name", name);
                data.put(MailDbHelper.SUB_NAME, subname);
                data.put(MailDbHelper.CONTENT, content);
                list.add(data);
            }
            allDataCursor2.close();
        } catch (Exception e) {
            allDataCursor.close();
        }
        return list;
    }

    private void initView() {
        this.mListView = (ListView) findViewById(R.id.maillist);
        this.mListView.setDivider(null);
        this.mAdapter = new MoreVodAdapter(this, this.mMailList);
        this.mListView.setAdapter(this.mAdapter);
        this.mListView.setOnItemClickListener(this.mAdapter);
        this.mListView.setOnItemSelectedListener(this.mAdapter);
        this.mListView.setOnKeyListener(this.mAdapter);
        this.mListView.requestFocus();
        this.mEmailall = (TextView) findViewById(R.id.tv_email_all);
        this.mEmailUnRead = (TextView) findViewById(R.id.email_dontread);
        this.mTvDate = (TextView) findViewById(R.id.tv_mail_date);
        this.mTvDateDetails = (TextView) findViewById(R.id.tv_mail_date2);
        this.mTvTime = (TextView) findViewById(R.id.tv_mail_time);
        this.mTvTitle = (TextView) findViewById(R.id.tv_mail_title);
        this.mTvContent = (TextView) findViewById(R.id.tv_mail_content);
    }

    /* access modifiers changed from: private */
    public void showMail(Map<String, String> mail) {
        if (!MailDbHelper.isOpen()) {
            this.mMailHelper.openDb();
        }
        ContentValues contentValues = new ContentValues();
        contentValues.put("_id", (String) mail.get("id"));
        contentValues.put(MailDbHelper.CONTENT, (String) mail.get(MailDbHelper.CONTENT));
        contentValues.put(MailDbHelper.DATE, (String) mail.get(MailDbHelper.DATE));
        contentValues.put(MailDbHelper.DATE_DETAILS, (String) mail.get(MailDbHelper.DATE_DETAILS));
        contentValues.put(MailDbHelper.FLAG, "1");
        contentValues.put("name", (String) mail.get("name"));
        contentValues.put(MailDbHelper.SUB_NAME, (String) mail.get(MailDbHelper.SUB_NAME));
        contentValues.put(MailDbHelper.TYPE, (String) mail.get(MailDbHelper.TYPE));
        this.mMailHelper.updateData(contentValues, (String) mail.get("id"));
        this.mMailHelper.close();
        this.mMailList = getMails();
        this.mTvTitle.setText((CharSequence) mail.get("name"));
        String dataDetals = (String) mail.get(MailDbHelper.DATE_DETAILS);
        this.mTvContent.setText(((String) mail.get(MailDbHelper.CONTENT)).trim());
        this.mTvDateDetails.setText(new StringBuilder(String.valueOf(dataDetals)).append("配信").toString());
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case 184:
                int selectedItemPosition = this.mListView.getSelectedItemPosition();
                LogcatUtils.m321d("selectedItemPosition-->" + selectedItemPosition);
                if (selectedItemPosition >= 0) {
                    Map<String, String> map = (Map) this.mMailList.get(this.mListView.getSelectedItemPosition());
                    if (!MailDbHelper.isOpen()) {
                        this.mMailHelper.openDb();
                    }
                    int status = this.mMailHelper.deleteData((String) map.get("id"));
                    LogcatUtils.m321d("deleteStatus is " + status);
                    if (1 != status) {
                        this.mMailHelper.deleteDataByName((String) map.get("name"));
                    }
                    this.mMailHelper.close();
                    this.mWorkHander.sendEmptyMessage(0);
                    this.mUiHandler.sendEmptyMessage(5);
                    break;
                }
                break;
        }
        return super.onKeyDown(keyCode, event);
    }
}
