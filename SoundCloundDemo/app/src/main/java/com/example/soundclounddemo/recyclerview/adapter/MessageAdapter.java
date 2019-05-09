package com.example.soundclounddemo.recyclerview.adapter;

import android.content.Context;
import android.content.Intent;
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
import com.example.soundclounddemo.model.message.MusicMessageModel;
import com.example.soundclounddemo.model.message.TextMessageModel;
import com.example.soundclounddemo.model.track.TrackModel;
import com.example.soundclounddemo.service.TrackStreamService;
import com.example.soundclounddemo.utils.ImageUtils;
import com.example.soundclounddemo.utils.MessageUtil;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

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
                : viewType == 13 ? R.layout.right_music_message_layout
                : viewType == 21 ? R.layout.left_text_message_layout
                : viewType == 22 ? R.layout.left_image_message_layout
                : viewType == 23 ? R.layout.left_music_message_layout
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
        private TextView tvRequest = null, tvTitleRequest = null;

        public MessageViewHolder(@NonNull View itemView, int viewType) {
            super(itemView);

            tvTime = (TextView) (
                    viewType == 11 ? itemView.findViewById(R.id.tv_right_text_time)
                            : viewType == 12 ? itemView.findViewById(R.id.tv_right_image_time)
                            : viewType == 13 ? itemView.findViewById(R.id.tv_right_time_music_message)
                            : viewType == 21 ? itemView.findViewById(R.id.tv_left_text_time)
                            : viewType == 22 ? itemView.findViewById(R.id.tv_left_image_time)
                            : viewType == 23 ? itemView.findViewById(R.id.tv_left_time_music_message)
                            : null
            );
            ivMessage = (ImageView) (
                    viewType == 12 ? itemView.findViewById(R.id.iv_right_image_message)
                            : viewType == 22 ? itemView.findViewById(R.id.iv_left_image_message)
                            : viewType == 13 ? itemView.findViewById(R.id.iv_right_artwork_music_message)
                            : viewType == 23 ? itemView.findViewById(R.id.iv_left_artwork_music_message)
                            : null
            );

        }

        public void onBindData(MessageModel messageModel) {

            tvTime.setText(messageModel.getTime());
            tvTime.setVisibility(View.GONE);
            int messageType = messageModel.getType();

            switch (messageType) {
                case MessageUtil.TEXT_MESSAGE:
                    onBindDataTextMessage((TextMessageModel) messageModel);
                    break;
                case MessageUtil.IMAGE_MESSAGE:
                    onBindDataImageMessage((ImageMessageModel) messageModel);
                    break;
                case MessageUtil.MUSIC_MESSAGE:
                    onBindDataMusicMessage((MusicMessageModel) messageModel);
                    break;
            }


            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (tvTime.getVisibility() == View.GONE) {
                        tvTime.setVisibility(View.VISIBLE);
                    } else {
                        tvTime.setVisibility(View.GONE);
                    }
                }
            });
        }

        private void onBindDataTextMessage(TextMessageModel textMessageModel) {
            TextView tvContent = (TextView) (textMessageModel.getOwner() == MessageUtil.OWNER
                                ? itemView.findViewById(R.id.tv_right_content_text_message)
                                : itemView.findViewById(R.id.tv_left_content_text_message));
            tvContent.setText(textMessageModel.getContent());
        }

        private void onBindDataImageMessage(ImageMessageModel imageMessageModel) {
            Uri uri =  imageMessageModel.getUri();
            String url =  imageMessageModel.getUrl();

            if (ImageUtils.checkAvailableUri(mContext, uri)) {
                ivMessage.setImageURI(uri);
            } else {
                Glide.with(mContext)
                        .load(url)
                        .placeholder(R.drawable.image)
                        .into(ivMessage);
                // Picasso.get().load(url).into(ivMessage);
            }
        }

        private void onBindDataMusicMessage(MusicMessageModel musicMessageModel) {
            Log.d(TAG, "onBindDataMusicMessage: " + musicMessageModel.getTrackModel().getTitle());
            TextView tvTrackName , tvUserName ;
            if (musicMessageModel.getOwner() == MessageUtil.OWNER) {
                tvTrackName = itemView.findViewById(R.id.tv_right_track_name_music_message);
                tvUserName = itemView.findViewById(R.id.tv_right_user_name_music_message);
                tvRequest = itemView.findViewById(R.id.tv_right_request_music_message);
                tvTitleRequest = itemView.findViewById(R.id.tv_right_title_music_message);
            } else {
                tvTrackName = itemView.findViewById(R.id.tv_left_track_name_music_message);
                tvUserName = itemView.findViewById(R.id.tv_left_user_name_music_message);
                tvRequest = itemView.findViewById(R.id.tv_left_request_music_message);
                tvTitleRequest = itemView.findViewById(R.id.tv_left_title_music_message);
            }
            final TrackModel trackModel = musicMessageModel.getTrackModel();
            Glide.with(mContext)
                    .load(trackModel.getArtworkUrl())
                    .into(ivMessage);
            tvTrackName.setText(trackModel.getTitle());
            tvUserName.setText(trackModel.getUserName());
            tvRequest.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    switch (v.getId()) {
                        case R.id.tv_right_request_music_message:
                            tvRequest.setText("Canceled");
                            tvTitleRequest.setText("You canceled invitation");
                            tvRequest.setVisibility(View.GONE);
                            break;
                        case R.id.tv_left_request_music_message:
                            Intent intent = new Intent(mContext, TrackStreamService.class);
                            intent.putExtra("track", trackModel);
                            mContext.startService(intent);
                            tvTitleRequest.setText("You are listening with your frend");
                            tvRequest.setVisibility(View.GONE);
                            break;
                    }
                }
            });
        }

    }
}
