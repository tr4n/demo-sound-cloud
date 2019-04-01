package com.example.soundclounddemo.recyclerview.adapter;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.soundclounddemo.R;
import com.example.soundclounddemo.model.message.ImageMessageModel;
import com.example.soundclounddemo.model.message.MessageModel;
import com.example.soundclounddemo.model.message.TextMessageModel;
import com.example.soundclounddemo.utils.MessageUtil;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileNotFoundException;
import java.net.URL;
import java.util.List;

import static com.android.volley.VolleyLog.TAG;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.MessageViewHolder> {

    private List<MessageModel> mMessageModelList;
    private Context mContext;

    public MessageAdapter(List<MessageModel> messageModelList, Context context) {
        this.mMessageModelList = messageModelList;
        this.mContext = context;
    }

    @NonNull
    @Override
    public MessageViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {

        int layoutId = viewType == 11 ? R.layout.right_text_message_layout
                : viewType == 12 ? R.layout.right_image_message_layout
                : viewType == 13 ? R.layout.right_music_inviting_layout
                : viewType == 21 ? R.layout.left_text_message_layout
                : viewType == 22 ? R.layout.left_image_message_layout
                : viewType == 23 ? R.layout.left_music_inviting_layout
                : 0;
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(layoutId, viewGroup, false);
        return new MessageViewHolder(view, viewType);
    }

    @Override
    public void onBindViewHolder(@NonNull MessageViewHolder messageViewHolder, int position) {
        messageViewHolder.onBindData(mMessageModelList.get(position));
    }

    @Override
    public int getItemCount() {
        return mMessageModelList.size();
    }

    @Override
    public int getItemViewType(int position) {
        MessageModel messageModel = mMessageModelList.get(position);
        return messageModel.getType() + messageModel.getOwner();

    }

    public class MessageViewHolder extends RecyclerView.ViewHolder {

        private TextView tvTime;
        private ImageView ivMessage;

        public MessageViewHolder(@NonNull View itemView, int viewType) {
            super(itemView);

            tvTime = (TextView) (
                    viewType == 11 ? itemView.findViewById(R.id.tv_right_text_time)
                            : viewType == 12 ? itemView.findViewById(R.id.tv_right_image_time)
                            : viewType == 13 ? null
                            : viewType == 21 ? itemView.findViewById(R.id.tv_left_text_time)
                            : viewType == 22 ? itemView.findViewById(R.id.tv_left_image_time)
                            : viewType == 23 ? null
                            : null
            );
            ivMessage = (ImageView) (
                    viewType == 12 ? itemView.findViewById(R.id.iv_right_image_message)
                            : viewType == 22 ? itemView.findViewById(R.id.iv_left_image_message)
                            : null
            );

        }

        public void onBindData(MessageModel messageModel) {
            try {
                tvTime.setText(messageModel.getTime());
                tvTime.setVisibility(View.INVISIBLE);
            } catch (NullPointerException e) {
                Log.e(TAG, "onBindData: " + e.getMessage());
            }
            int messageType = messageModel.getType();
            switch (messageType) {
                case MessageUtil.TEXT_MESSAGE:
                    TextMessageModel textMessageModel = (TextMessageModel) messageModel;
                    //   Log.d(TAG, "onBindData: " + textMessageModel);
                    if (messageModel.getOwner() == MessageUtil.OWNER) {
                        ((TextView) itemView.findViewById(R.id.tv_right_content_text_message))
                                .setText(textMessageModel.getContent());

                    } else if (messageModel.getOwner() == MessageUtil.FRIENDS) {
                        ((TextView) super.itemView.findViewById(R.id.tv_left_content_text_message))
                                .setText(textMessageModel.getContent());
                    }
                    break;
                case MessageUtil.IMAGE_MESSAGE:
                    ImageMessageModel imageMessageModel = (ImageMessageModel) messageModel;
                 //   ivMessage.setImageURI(((ImageMessageModel) messageModel).getUri());
                    Glide.with(mContext).load(imageMessageModel.getUri().getPath()).into(ivMessage);
                   // Picasso.get().load(imageMessageModel.getImageModel().getUrl()).into(ivMessage);

                    break;
                case MessageUtil.MUSIC_MESSAGE:
                    break;
            }

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (tvTime.getVisibility() == View.INVISIBLE) {
                        tvTime.setVisibility(View.VISIBLE);
                    } else {
                        tvTime.setVisibility(View.INVISIBLE);
                    }
                }
            });
        }


    }
}
