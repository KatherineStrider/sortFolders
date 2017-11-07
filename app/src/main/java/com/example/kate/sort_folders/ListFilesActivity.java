package com.example.kate.sort_folders;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Kate on 07.11.2017.
 */

public class ListFilesActivity extends AppCompatActivity {

    private enum ThreadStatesEnum {
        RUNNING,
        FINISHED,
        ERROR;
    }

    Handler handler;
    RecyclerView listView;

    @SuppressLint("HandlerLeak")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_list);

        listView = findViewById(R.id.list);

        handler = new Handler() {
            ThreadStatesEnum threadStatesEnum;
            ProgressBar progressBar = findViewById(R.id.list_progress);
            TextView textView = findViewById(R.id.list_error);

            List<String> list;

            @Override
            public void handleMessage(Message msg) {
                if (msg.what == 0) {
                    threadStatesEnum = (ThreadStatesEnum) msg.obj;
                }

                progressBar.setVisibility(View.GONE);
                switch (threadStatesEnum) {
                    case RUNNING:
                        progressBar.setVisibility(View.VISIBLE);
                        break;
                    case ERROR:
                        progressBar.setVisibility(View.GONE);
                        textView.setVisibility(View.VISIBLE);
                        textView.setText("Ошибка");
                        break;
                    case FINISHED:
                        progressBar.setVisibility(View.GONE);
                        textView.setVisibility(View.GONE);
                        listView.setVisibility(View.VISIBLE);
                        ListFiles listFiles = new ListFiles(list);
                        listView.setAdapter(listFiles);
                        listView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                        break;
                }
                if (msg.what == 1) {
                    list = (List<String>) msg.obj;
                    if (list.size() == 0) {
                        progressBar.setVisibility(View.GONE);
                        textView.setVisibility(View.VISIBLE);
                        textView.setText("Ошибка");
                    }
                }
            }
        };
        new MyThread().start();
    }

    class MyThread extends Thread {
        Message message;
        int count;

        @Override
        public void run() {
            count = 0;
            message = handler.obtainMessage(count, ThreadStatesEnum.RUNNING);
            handler.sendMessage(message);

            File parent = new File(String.valueOf(Environment.getExternalStoragePublicDirectory
                    (Environment.DIRECTORY_DOWNLOADS)));

            List<String> list = new ArrayList<>();
            for (File file : parent.listFiles()) {
                if (file.isFile()) {
                    list.add(file.getName());
                }
            }
            message = handler.obtainMessage(1, list);
            handler.sendMessage(message);

            message = handler.obtainMessage(count, ThreadStatesEnum.FINISHED);
            handler.sendMessage(message);
        }
    }
}
