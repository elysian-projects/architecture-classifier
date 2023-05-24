package com.architecture.app.utils;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.architecture.app.R;
import com.architecture.app.components.dialog.DialogVariant;
import com.architecture.app.components.dialog.DialogWindow;
import com.architecture.app.databinding.AchievementNotificationBinding;
import com.architecture.app.databinding.RowAchievementBinding;
import com.architecture.app.viewModels.Achievement;
import com.google.android.material.snackbar.Snackbar;

import java.io.IOException;

public class AchievementsGridAdapter extends RecyclerView.Adapter<AchievementsGridAdapter.AchievementsViewHolder> {
    private final Achievement[] _achievements;

    public AchievementsGridAdapter(Achievement[] achievements) {
        _achievements = achievements;
    }

    public static class AchievementsViewHolder extends RecyclerView.ViewHolder {
        public final RowAchievementBinding binding;

        public AchievementsViewHolder(RowAchievementBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

    @NonNull
    @Override
    public AchievementsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        RowAchievementBinding binding = RowAchievementBinding.inflate(inflater, parent, false);

        return new AchievementsViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull AchievementsViewHolder holder, int position) {
        Achievement achievement = _achievements[position];
        Context context = holder.itemView.getContext();

        DialogWindow dialog = new DialogWindow(context);

        loadImageToRow(holder.binding.achievementImagePreview, achievement.preview, context);

        holder.binding.achievementImagePreview.setOnClickListener(view -> {
            try {
                dialog.setVariant(DialogVariant.INFO, context)
                    .setTitle(achievement.label)
                    .setMessage(achievement.description)
                    .setIcon(AssetsParser.readPreviewImage(context, achievement.preview))
                    .show();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }

    @Override
    public int getItemCount() {
        return _achievements.length;
    }

    private void loadImageToRow(ImageView imageView, String preview, Context context) {
        try {
            imageView.setImageBitmap(AssetsParser.readPreviewImage(context, preview));
        } catch(Exception exception) {
            Log.i("AchievementsFragment", "Could not load a preview image", exception);
        }
    }
}
