package com.example.kate.sort_folders;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Kate on 08.11.2017.
 */

public class TestCopyActivity extends AppCompatActivity {

    public enum ThreadStatesEnum {
        RUNNING,
        FINISHED,
        ERROR;
    }

    CopyHandler handler;
    RecyclerView listView;
    TextView textView;
    ProgressBar progressBar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        listView = findViewById(R.id.list);
        progressBar = findViewById(R.id.list_progress);
        textView = findViewById(R.id.list_error);

        listView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

        handler = new CopyHandler(progressBar, textView, listView);
        new CopyThread(handler).start();
    }

    static class CopyHandler extends Handler {
        ThreadStatesEnum threadStatesEnum;
        ProgressBar progressBar;
        TextView textView;
        RecyclerView listView;

        List<String> list;

        CopyHandler(ProgressBar progressBar, TextView textView, RecyclerView listView) {
            this.progressBar = progressBar;
            this.textView = textView;
            this.listView = listView;
        }

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

                    listView.setAdapter(new ListFiles(list));
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
    }
}
