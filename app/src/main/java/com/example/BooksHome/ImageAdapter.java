package com.example.BooksHome;

import android.content.Context;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.BooksHome.Model.users;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.ImageViewHolder> {
    private Context mContext;
    private List<Upload> mUploads;
    private List<users> musers;
    DatabaseReference reference;
    FirebaseUser use;
    FirebaseAuth firebaseAuth;
    FirebaseDatabase firebaseDatabase;
    String userID;
    private OnItemClickListener mListener;
    public ImageAdapter(Context context, List<Upload> uploads, List<users> users) {
        mContext = context;
        mUploads = uploads;
        musers=users;
    }

    @Override
    public ImageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.image_item, parent, false);
        return new ImageViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ImageViewHolder holder, int position) {
        reference=FirebaseDatabase.getInstance().getReference("users");
        firebaseAuth= FirebaseAuth.getInstance();

        use=firebaseAuth.getCurrentUser();
        firebaseDatabase=FirebaseDatabase.getInstance();
        userID=use.getUid();

        Upload uploadCurrent = mUploads.get(position);


        holder.textViewName.setText(uploadCurrent.getName());
        holder.chv.setText(uploadCurrent.getMtype());
        holder.author.setText(uploadCurrent.getmAuthor());
        holder.phone.setText(uploadCurrent.getTel());
        holder.user.setText(uploadCurrent.getNom());
        holder.mail.setText(uploadCurrent.getMail());
        holder.cate.setText(uploadCurrent.getCate());
        Picasso.get()
                .load(uploadCurrent.getImageUrl())
                .placeholder(R.mipmap.ic_launcher)
                .fit()
                .centerCrop()
                .into(holder.imageView);
            }





    @Override
    public int getItemCount() {
        return mUploads.size();
    }

    public class ImageViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener,
            View.OnCreateContextMenuListener, MenuItem.OnMenuItemClickListener {

        public TextView textViewName;
        public ImageView imageView;
        public TextView chv;
        public TextView user ;
        public TextView phone ;
        public TextView author ;
        public TextView mail ;
        public TextView cate ;

        public ImageViewHolder(View itemView) {
            super(itemView);

            textViewName = itemView.findViewById(R.id.text_view_name);
            imageView = itemView.findViewById(R.id.image_view_upload);
            chv=itemView.findViewById(R.id.echanVendre);
             phone =itemView.findViewById(R.id.phone);
            author =itemView.findViewById(R.id.author);
            user=itemView.findViewById(R.id.user);
            mail=itemView.findViewById(R.id.email);
            cate=itemView.findViewById(R.id.cate);

            itemView.setOnClickListener(this);
            itemView.setOnCreateContextMenuListener(this);
        }

        @Override
        public void onClick(View v) {
            if (mListener != null) {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    mListener.onItemClick(position);
                }
            }
        }

        @Override
        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
            menu.setHeaderTitle("Select Action");
            MenuItem delete = menu.add(Menu.NONE, 3, 3, "Delete");

            delete.setOnMenuItemClickListener(this);

        }

        @Override
        public boolean onMenuItemClick(MenuItem item) {
            if (mListener != null) {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {

                    switch (item.getItemId()) {
                        case 1:
                            mListener.onFavoritClick(position);
                            return true;
                        case 2:
                            mListener.onPanierClick(position);
                            return true;
                        case 3 :
                            mListener.onDeleteClick(position);
                            return true;

                    }
                }
            }
            return false;
        }
    }

    public interface OnItemClickListener {
        void onItemClick(int position);
        void onFavoritClick(int position);
        void onPanierClick(int position);
        void onDeleteClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }
    /////Search
    public void filterList(ArrayList<Upload> filteredList) {
        mUploads = filteredList;
        notifyDataSetChanged();
    }
}
