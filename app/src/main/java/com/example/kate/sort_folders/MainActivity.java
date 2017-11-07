package com.example.kate.sort_folders;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    private enum ThreadStatesEnum {
        RUNNING,
        FINISHED,
        ERROR;
    }

    Handler handler;

    @SuppressLint("HandlerLeak")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.button).setOnClickListener(onClickListener);

        handler = new Handler() {
            ThreadStatesEnum threadStatesEnum;
            ProgressBar progressBar = findViewById(R.id.progressBar);
            TextView textView = findViewById(R.id.textView);

            @Override
            public void handleMessage(Message msg) {
                threadStatesEnum = (ThreadStatesEnum) msg.obj;
                progressBar.setMax(100);
                progressBar.setProgress(msg.what);
                switch (threadStatesEnum) {
                    case RUNNING:
                        textView.setText("Запущено");
                        break;
                    case ERROR:
                        textView.setText("Ошибка" + msg.what);
                        break;
                    case FINISHED:
                        textView.setText("Завершено");
                        break;

                }
            }
        };
    }

    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            view.setClickable(false);
//            requestMultiplePermissions();
//            MyThread myThread = new MyThread();
//            myThread.start();
            Intent intent = new Intent(getApplicationContext(), ListFilesActivity.class);
            startActivity(intent);
        }
    };

    class MyThread extends Thread {
        Message message;
        int count;

        @Override
        public void run() {
            count = 2;
            message = handler.obtainMessage(count, ThreadStatesEnum.RUNNING);
            handler.sendMessage(message);
            try {
                sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            File parent = new File(String.valueOf(Environment.getExternalStoragePublicDirectory
                    (Environment.DIRECTORY_DOCUMENTS)));
            File file = new File(parent, "test.txt");
            if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
                if (!file.exists()) {
                        try {
                            file.createNewFile();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        message = handler.obtainMessage(count, ThreadStatesEnum.ERROR);
                        handler.sendMessage(message);
                    try {
                        sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
//                    file.createNewFile();
            }

            count = 10;
            message = handler.obtainMessage(count, ThreadStatesEnum.RUNNING);
            handler.sendMessage(message);
            try {
                sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            FileWriter f = null;
            try {
                f = new FileWriter(file);
                f.write("Как уже ранее было сказано мною, android приложение находится в некой " +
                        "песочнице, изолированной от воздействия со стороны других приложений по " +
                        "умолчанию. Для того, чтобы создать файл внутри этой песочницы, следует " +
                        "использовать функцию openFileOutput(). Хочу отметить 2 аргумента:\n" +
                        "\n" +
                        "1. имя файла\n" +
                        "2. режим доступа к нему со стороны чужих приложений");
                count = 60;
                message = handler.obtainMessage(count, ThreadStatesEnum.RUNNING);
                handler.sendMessage(message);
                sleep(1000);
                f.flush();
                count = 80;
                message = handler.obtainMessage(count, ThreadStatesEnum.RUNNING);
                handler.sendMessage(message);
                sleep(1000);
            } catch (IOException e) {
                e.printStackTrace();
                message = handler.obtainMessage(count, ThreadStatesEnum.ERROR);
                handler.sendMessage(message);
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                if (f != null) {
                    try {
                        f.close();
                        message = handler.obtainMessage(100, ThreadStatesEnum.FINISHED);
                        handler.sendMessage(message);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

        }
    }

    public void requestMultiplePermissions() {
        ActivityCompat.requestPermissions(this,
                new String[]{
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE
                },
                1);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode == 1 && grantResults.length == 2) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED
                    && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                MyThread myThread = new MyThread();
                myThread.start();
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
}
