package com.burhanrashid52.photoediting;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

public class StickerBSFragment extends BottomSheetDialogFragment {

    // Image Urls from flaticon(https://www.flaticon.com/stickers-pack/food-289)
    private static String[] stickerPathList = new String[]{

            // party
            "https://cdn-icons-png.flaticon.com/256/4442/4442765.png",
            "https://cdn-icons-png.flaticon.com/256/4392/4392550.png",
            "https://cdn-icons-png.flaticon.com/256/4213/4213654.png",
            "https://cdn-icons-png.flaticon.com/256/4213/4213713.png",
            "https://cdn-icons-png.flaticon.com/256/4497/4497505.png",
            "https://cdn-icons-png.flaticon.com/256/6225/6225102.png",
            "https://cdn-icons-png.flaticon.com/256/7353/7353900.png",

            "https://cdn-icons-png.flaticon.com/256/6702/6702470.png",
            "https://cdn-icons-png.flaticon.com/256/6702/6702471.png",
            "https://cdn-icons-png.flaticon.com/256/6702/6702473.png",
            "https://cdn-icons-png.flaticon.com/256/6702/6702474.png",
            "https://cdn-icons-png.flaticon.com/256/6702/6702478.png",
            "https://cdn-icons-png.flaticon.com/256/6702/6702479.png",
            "https://cdn-icons-png.flaticon.com/256/6702/6702480.png",
            "https://cdn-icons-png.flaticon.com/256/6852/6852998.png",
            "https://cdn-icons-png.flaticon.com/256/6852/6852980.png",

            "https://cdn-icons-png.flaticon.com/256/6852/6852970.png",
            "https://cdn-icons-png.flaticon.com/256/6852/6852967.png",
            "https://cdn-icons-png.flaticon.com/256/6853/6853028.png",
            "https://cdn-icons-png.flaticon.com/256/6852/6852961.png",
            "https://cdn-icons-png.flaticon.com/256/6702/6702475.png",


            // others
            "https://cdn-icons-png.flaticon.com/256/4392/4392455.png",
            "https://cdn-icons-png.flaticon.com/256/4392/4392459.png",
            "https://cdn-icons-png.flaticon.com/256/4392/4392462.png",
            "https://cdn-icons-png.flaticon.com/256/4392/4392465.png",
            "https://cdn-icons-png.flaticon.com/256/6738/6738563.png",
            "https://cdn-icons-png.flaticon.com/256/6299/6299773.png",
            "https://cdn-icons-png.flaticon.com/256/6738/6738558.png",
    };

    public StickerBSFragment() {
        // Required empty public constructor
    }

    private StickerListener mStickerListener;

    public void setStickerListener(StickerListener stickerListener) {
        mStickerListener = stickerListener;
    }

    public interface StickerListener {
        void onStickerClick(Bitmap bitmap);
    }

    private BottomSheetBehavior.BottomSheetCallback mBottomSheetBehaviorCallback = new BottomSheetBehavior.BottomSheetCallback() {

        @Override
        public void onStateChanged(@NonNull View bottomSheet, int newState) {
            if (newState == BottomSheetBehavior.STATE_HIDDEN) {
                dismiss();
            }

        }

        @Override
        public void onSlide(@NonNull View bottomSheet, float slideOffset) {
        }
    };


    @SuppressLint("RestrictedApi")
    @Override
    public void setupDialog(Dialog dialog, int style) {
        super.setupDialog(dialog, style);
        View contentView = View.inflate(getContext(), R.layout.fragment_bottom_sticker_emoji_dialog, null);
        dialog.setContentView(contentView);
        CoordinatorLayout.LayoutParams params = (CoordinatorLayout.LayoutParams) ((View) contentView.getParent()).getLayoutParams();
        CoordinatorLayout.Behavior behavior = params.getBehavior();

        if (behavior != null && behavior instanceof BottomSheetBehavior) {
            ((BottomSheetBehavior) behavior).setBottomSheetCallback(mBottomSheetBehaviorCallback);
        }
        ((View) contentView.getParent()).setBackgroundColor(getResources().getColor(android.R.color.transparent));
        RecyclerView rvEmoji = contentView.findViewById(R.id.rvEmoji);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 3);
        rvEmoji.setLayoutManager(gridLayoutManager);
        StickerAdapter stickerAdapter = new StickerAdapter();
        rvEmoji.setAdapter(stickerAdapter);
        rvEmoji.setHasFixedSize(true);
        rvEmoji.setItemViewCacheSize(stickerPathList.length);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }

    public class StickerAdapter extends RecyclerView.Adapter<StickerAdapter.ViewHolder> {

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_sticker, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            // Load sticker image from remote url
            Glide.with(requireContext())
                    .asBitmap()
                    .load(stickerPathList[position])
                    .into(holder.imgSticker);
        }

        @Override
        public int getItemCount() {
            return stickerPathList.length;
        }

        class ViewHolder extends RecyclerView.ViewHolder {
            ImageView imgSticker;

            ViewHolder(View itemView) {
                super(itemView);
                imgSticker = itemView.findViewById(R.id.imgSticker);

                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (mStickerListener != null) {
                            Glide.with(requireContext())
                                    .asBitmap()
                                    .load(stickerPathList[getLayoutPosition()])
                                    .into(new CustomTarget<Bitmap>(256, 256) {
                                        @Override
                                        public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                                            mStickerListener.onStickerClick(resource);
                                        }

                                        @Override
                                        public void onLoadCleared(@Nullable Drawable placeholder) {

                                        }
                                    });
                        }
                        dismiss();
                    }
                });
            }
        }
    }
}