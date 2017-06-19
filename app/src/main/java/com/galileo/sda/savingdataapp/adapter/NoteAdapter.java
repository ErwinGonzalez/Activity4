package com.galileo.sda.savingdataapp.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.galileo.sda.savingdataapp.R;
import com.galileo.sda.savingdataapp.database.NoteDAO;
import com.galileo.sda.savingdataapp.database.NoteModel;

import java.util.ArrayList;

public class NoteAdapter extends RecyclerView.Adapter<NoteAdapter.ViewHolder> {

    private ArrayList<NoteModel> notes;
    private handleClicked clickedCallback;

    public NoteAdapter(ArrayList<NoteModel> notes){
        this.notes=notes;
    }
    public void clearData(ArrayList<NoteModel> notes) {
        this.notes=notes;
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.note_item,parent,false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        NoteModel note = (NoteModel)notes.get(position);
        holder.tvTitle.setText(note.getTitle());
        holder.tvTime.setText(note.getDatetime());
        holder.tvContent.setText(note.getContent());
    }

    @Override
    public int getItemCount() {
        return notes.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener,RecyclerView.OnLongClickListener{
        TextView tvTitle;
        TextView tvTime;
        TextView tvContent;

        ViewHolder(View view) {
            super(view);
            tvTitle = (TextView) view.findViewById(R.id.noteTitle);
            tvTime = (TextView) view.findViewById(R.id.noteTime);
            tvContent = (TextView) view.findViewById(R.id.noteContent);
            view.setOnClickListener(this);
            view.setOnLongClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if(clickedCallback!=null)
                clickedCallback.editClicked(getAdapterPosition());
        }

        @Override
        public boolean onLongClick(View view) {
            if(clickedCallback!=null)
                clickedCallback.deleteClicked(getAdapterPosition());
            return true;
        }
    }
    public void setCallback(handleClicked c){
        clickedCallback = c;
    }
    public interface handleClicked{
        void editClicked(int position);
        void deleteClicked(int position);
    }
}
