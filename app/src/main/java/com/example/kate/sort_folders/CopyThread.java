package com.example.kate.sort_folders;

import android.os.Environment;
import android.os.Message;

import com.example.kate.sort_folders.models.CopyFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Kate on 08.11.2017.
 */

public class CopyThread extends Thread {

    TestCopyActivity.CopyHandler copyHandler;
    Message message;
    int count;


    CopyThread (TestCopyActivity.CopyHandler copyHandler) {
        this.copyHandler = copyHandler;
    }

    @Override
    public void run() {
        count = 0;
        message = copyHandler.obtainMessage(count, TestCopyActivity.ThreadStatesEnum.RUNNING);
        copyHandler.sendMessage(message);

        File parent = new File(String.valueOf(Environment.getExternalStoragePublicDirectory
                (Environment.DIRECTORY_DOWNLOADS)));

        List<CopyFile> list = new ArrayList<>();
        for (File file : parent.listFiles()) {
            if (file.isFile()) {
                CopyFile copyFile = new CopyFile(file.getAbsolutePath(), file.getName(), file.lastModified());
                list.add(copyFile);
            }
        }
        message = copyHandler.obtainMessage(1, list);
        copyHandler.sendMessage(message);

        copy(list);

        message = copyHandler.obtainMessage(count, TestCopyActivity.ThreadStatesEnum.FINISHED);
        copyHandler.sendMessage(message);
    }

    private void copy(List<CopyFile> copyFiles) {

        File parent = new File(String.valueOf(Environment.getExternalStorageDirectory()));
        File directoryForCopy = new File(parent, "/test");

        FileOutputStream writer = null;
        FileInputStream reader = null;

        for (CopyFile copyFile : copyFiles) {
            try {
                File fileForCopy = new File(copyFile.getPath());

                int length = (int) fileForCopy.length();
                byte[] bytes = new byte[length];

                reader = new FileInputStream(fileForCopy);
                reader.read(bytes);

                File file = new File(directoryForCopy, copyFile.getName());

                if (!file.exists()) {
                    try {
                        file.createNewFile();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    message = copyHandler.obtainMessage(count, TestCopyActivity.ThreadStatesEnum.ERROR);
                    copyHandler.sendMessage(message);
                }

                reader.close();
                writer = new FileOutputStream(file);
                writer.write(bytes);

                message = copyHandler.obtainMessage(count, TestCopyActivity.ThreadStatesEnum.RUNNING);
                copyHandler.sendMessage(message);

                writer.flush();

                message = copyHandler.obtainMessage(count, TestCopyActivity.ThreadStatesEnum.RUNNING);
                copyHandler.sendMessage(message);

            } catch (IOException e) {
                e.printStackTrace();
                message = copyHandler.obtainMessage(count, TestCopyActivity.ThreadStatesEnum.ERROR);
                copyHandler.sendMessage(message);

            } finally {
                if (writer != null) {
                    try {
                        writer.close();
                        message = copyHandler.obtainMessage(100, TestCopyActivity.ThreadStatesEnum.FINISHED);
                        copyHandler.sendMessage(message);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                if (reader != null) {
                    try {
                        reader.close();
                        message = copyHandler.obtainMessage(100, TestCopyActivity.ThreadStatesEnum.FINISHED);
                        copyHandler.sendMessage(message);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

}
