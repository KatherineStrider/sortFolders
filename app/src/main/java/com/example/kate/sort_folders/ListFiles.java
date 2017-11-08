package com.example.kate.sort_folders;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.kate.sort_folders.models.CopyFile;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Kate on 07.11.2017.
 */

public class ListFiles extends RecyclerView.Adapter<ListFiles.FilesHolder> {

    List<CopyFile> files;

    ListFiles(List list){
        files = new ArrayList<>(list);
    }

    @Override
    public FilesHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_folders,
                parent, false);
        return new FilesHolder(v);
    }

    @Override
    public void onBindViewHolder(FilesHolder holder, int position) {
        holder.setText(files.get(position).getName());
    }

    @Override
    public int getItemCount() {
        return files.size();
    }

    static class FilesHolder extends RecyclerView.ViewHolder {
        TextView textView;
        public FilesHolder(View itemView) {
            super(itemView);

            textView = itemView.findViewById(R.id.list_item_text);
        }

        void setText(String text){
            textView.setText(text);
        }
    }
}
