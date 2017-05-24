package com.like.common.util;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.text.TextUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;

import au.com.bytecode.opencsv.CSVReader;
import au.com.bytecode.opencsv.CSVWriter;

/**
 * csv文件读写工具类
 * <p>
 * Manifest.permission.WRITE_EXTERNAL_STORAGE
 *
 * @author like
 * @version 1.0
 *          created on 2016/12/20 16:58
 */
public class CsvUtils {
    private static final String POSTFIX = ".csv";
    private File dir;
    private Context mContext;
    private volatile static CsvUtils sInstance = null;

    public static CsvUtils getInstance(Context context) {
        if (sInstance == null) {
            synchronized (CsvUtils.class) {
                if (sInstance == null) {
                    sInstance = new CsvUtils(context);
                }
            }
        }
        return sInstance;
    }

    private CsvUtils(Context context) {
        mContext = context.getApplicationContext();
        dir = new File(StorageUtils.InternalStorageHelper.getFilesDir(context).getAbsolutePath() + "/csv");
        if (!dir.exists()) {
            dir.mkdir();
        }
    }

    public void clear(String fileName) {
        FileUtils.deleteFile(new File(dir, fileName + POSTFIX));
    }

    public void read2ContentValues(OnResultListener<List<ContentValues>> listener, String fileName) {
        RxJavaUtils.runComputationAndUpdate(new RxJavaUtils.OnSubscribe<List<ContentValues>>() {
            @Override
            protected List<ContentValues> onSubscribeCall0() {
                return syncRead2ContentValues(fileName);
            }

            @Override
            protected void onNextCall(List<ContentValues> result) {
                if (listener != null) {
                    listener.onResult(result);
                }
            }
        });
    }

    public List<String[]> read(OnResultListener<List<String[]>> listener, String fileName) {
        RxJavaUtils.runComputationAndUpdate(new RxJavaUtils.OnSubscribe<List<String[]>>() {
            @Override
            protected List<String[]> onSubscribeCall0() {
                return syncRead(fileName);
            }

            @Override
            protected void onNextCall(List<String[]> result) {
                if (listener != null) {
                    listener.onResult(result);
                }
            }
        });
        return null;
    }

    public void write(OnResultListener<Boolean> listener, String fileName, Cursor cursor) {
        RxJavaUtils.runComputationAndUpdate(new RxJavaUtils.OnSubscribe<Boolean>() {
            @Override
            protected Boolean onSubscribeCall0() {
                return syncWrite(fileName, cursor);
            }

            @Override
            protected void onNextCall(Boolean result) {
                if (listener != null) {
                    listener.onResult(result);
                }
            }
        });
    }

    public void write(OnResultListener<Boolean> listener, String fileName, String... contents) {
        RxJavaUtils.runComputationAndUpdate(new RxJavaUtils.OnSubscribe<Boolean>() {
            @Override
            protected Boolean onSubscribeCall0() {
                return syncWrite(fileName, contents);
            }

            @Override
            protected void onNextCall(Boolean result) {
                if (listener != null) {
                    listener.onResult(result);
                }
            }
        });
    }

    public void write(OnResultListener<Boolean> listener, String fileName, List<String[]> contentList) {
        RxJavaUtils.runComputationAndUpdate(new RxJavaUtils.OnSubscribe<Boolean>() {
            @Override
            protected Boolean onSubscribeCall0() {
                return syncWrite(fileName, contentList);
            }

            @Override
            protected void onNextCall(Boolean result) {
                if (listener != null) {
                    listener.onResult(result);
                }
            }
        });
    }

    /**
     * 从csv文件中读取
     *
     * @param fileName 文件名，不包含后缀
     * @return
     */
    private List<ContentValues> syncRead2ContentValues(String fileName) {
        List<String[]> read = syncRead(fileName);

        List<ContentValues> contentValuesList = null;
        if (read != null && read.size() > 0) {
            contentValuesList = new ArrayList<>();
            for (String[] strs : read) {
                if (strs != null && strs.length >= 2) {// 必须成对
                    ContentValues contentValues = new ContentValues();
                    for (int i = 0; i < strs.length; i += 2) {
                        contentValues.put(strs[i], strs[i + 1]);// 其中0位置为key，1位置为value
                    }
                    contentValuesList.add(contentValues);
                }
            }
        }
        return contentValuesList;
    }

    /**
     * 从csv文件中读取
     *
     * @param fileName 文件名，不包含后缀
     * @return
     */
    private List<String[]> syncRead(String fileName) {
        if (TextUtils.isEmpty(fileName))
            return null;

        File file = new File(dir, fileName + POSTFIX);
        if (!file.exists()) {
            return null;
        }

        try {
            CSVReader reader = new CSVReader(new InputStreamReader(new FileInputStream(file), "GBK"), CSVWriter.DEFAULT_SEPARATOR, CSVWriter.NO_QUOTE_CHARACTER);
            List<String[]> result = reader.readAll();
            reader.close();
            return result;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 写入csv文件中
     *
     * @param fileName 文件名，不包含后缀
     * @param cursor   写入的数据
     * @return
     */
    private boolean syncWrite(String fileName, Cursor cursor) {
        if (TextUtils.isEmpty(fileName))
            return false;

        boolean result = false;
        if (cursor == null || cursor.getCount() <= 0) {
            clear(fileName);
            result = true;// 清空也认为是写入成功
        } else {
            List<String[]> contentList = new ArrayList<>();
            int columnCount = cursor.getColumnCount();
            if (columnCount > 0) {
                while (cursor.moveToNext()) {
                    int autoFiledCount = 1;// 数据库中自增长的列的数量
                    String[] row = new String[(columnCount - autoFiledCount) * 2];// 放入到csv文件中的实际的列数
                    int j = 0;
                    for (int i = autoFiledCount; i < columnCount; i++) {// 从1开始是为了避开自动id
                        row[j] = cursor.getColumnName(i);
                        row[j + 1] = cursor.getString(i);
                        j += 2;
                    }
                    contentList.add(row);
                }
                result = syncWrite(fileName, contentList);
            }
        }
        CloseableUtils.close(cursor);
        return result;
    }

    /**
     * 写入csv文件中
     *
     * @param fileName 文件名，不包含后缀
     * @param contents 写入的数据
     * @return
     */
    private boolean syncWrite(String fileName, String... contents) {
        if (TextUtils.isEmpty(fileName))
            return false;

        File file = new File(dir, fileName + POSTFIX);
        try {
            if (!file.exists()) {
                file.createNewFile();
            } else {
                CSVWriter writer = new CSVWriter(new OutputStreamWriter(new FileOutputStream(file), "GBK"), CSVWriter.DEFAULT_SEPARATOR, CSVWriter.NO_QUOTE_CHARACTER);
                writer.writeNext(contents);
                writer.flush();
                writer.close();
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 写入csv文件中
     *
     * @param fileName    文件名，不包含后缀
     * @param contentList 写入的数据
     * @return
     */
    private boolean syncWrite(String fileName, List<String[]> contentList) {
        if (TextUtils.isEmpty(fileName))
            return false;

        File file = new File(dir, fileName + POSTFIX);
        try {
            if (!file.exists()) {
                file.createNewFile();
            } else {
                CSVWriter writer = new CSVWriter(new OutputStreamWriter(new FileOutputStream(file), "GBK"), CSVWriter.DEFAULT_SEPARATOR, CSVWriter.NO_QUOTE_CHARACTER);
                writer.writeAll(contentList);
                writer.flush();
                writer.close();
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public interface OnResultListener<T> {
        void onResult(T t);
    }
}
