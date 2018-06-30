package com.example.dell.firebaseintro.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;


import com.example.dell.firebaseintro.Model.StoreBook;
import com.example.dell.firebaseintro.R;

import java.util.List;

public class StoreRecyclerAdapter extends RecyclerView.Adapter<StoreRecyclerAdapter.StoreViewHolder> {

    private Context context;
    private List<StoreBook> bookList;


    public StoreRecyclerAdapter(Context context,List<StoreBook> bookList){

        this.context = context;
        this.bookList = bookList;
    }

    @NonNull
    @Override
    public StoreRecyclerAdapter.StoreViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        StoreRecyclerAdapter.StoreViewHolder viewHolder= null;

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.store_row, parent, false);
        viewHolder = new StoreRecyclerAdapter.StoreViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull StoreRecyclerAdapter.StoreViewHolder holder, int position) {




        StoreBook item = bookList.get(position);

        holder.name.setText(item.getName());
        holder.author.setText("Author : " + item.getAuthor());
        holder.genre.setText("Genre : " + item.getGenre());
        holder.price.setText("Rs. " + item.getCost());
        holder.pieces.setText("Pieces : " + item.getPieces());

    }

    @Override
    public int getItemCount() {        return bookList.size();}


    public void clearData(){

        final int size = bookList.size();

        if(size > 0)
        {
            bookList.clear();
        }
        notifyItemRangeRemoved(0,size);
    }

    public class StoreViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public TextView name;
        public TextView author;
        public TextView genre;
        public TextView price;
        public TextView pieces;

        public StoreViewHolder(View itemView) {
            super(itemView);

            name = (TextView) itemView.findViewById(R.id.bookTitleList3);
            author = (TextView) itemView.findViewById(R.id.bookAuthorList3);
            genre = (TextView) itemView.findViewById(R.id.bookGenreList3);
            price = (TextView) itemView.findViewById(R.id.bookPriceList3);
            pieces = (TextView) itemView.findViewById(R.id.bookPieceList3);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {

            int position = getAdapterPosition();

            //Book item = listItems.getposition);

            //Intent intent = new Intent(context, DetailsActivity.class);
            //intent.putExtra("name",item.getName());
            //intent.putExtra("description", item.getDescription());
            //intent.putExtra("rating", item.getRating());

            //context.startActivity(intent);

            Toast.makeText(context,"" + position + " touched",Toast.LENGTH_SHORT).show();
        }

    }
}
