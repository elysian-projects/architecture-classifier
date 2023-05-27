package com.architecture.app.utils;

import android.content.Context;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.architecture.app.components.dialog.DialogVariant;
import com.architecture.app.components.dialog.DialogWindow;
import com.architecture.app.databinding.RowAchievementBinding;
import com.architecture.app.viewModels.Achievement;
import com.architecture.app.viewModels.AcquiredAchievementNode;

import java.io.IOException;

public class AchievementsGridAdapter extends RecyclerView.Adapter<AchievementsGridAdapter.AchievementsViewHolder> {
    private AcquiredAchievementNode[] _acquiredAchievements;
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
        if(_acquiredAchievements == null) {
            try {
                loadAcquiredAchievements(holder.itemView.getContext());
                Log.w("AchievementsGridAdapter", "CALL");
            } catch (IOException exception) {
                Log.e("AchievementsGridAdapter", "Could not load acquired achievements", exception);
            }
        }

        Achievement achievement = _achievements[position];
        Context context = holder.itemView.getContext();

        DialogWindow dialog = new DialogWindow(context);

        loadImageToRow(holder.binding.achievementImagePreview, achievement.preview, isAcquired(achievement), context);

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

    private void loadImageToRow(ImageView imageView, String preview, boolean acquired, Context context) {
        try {
            imageView.setImageBitmap(AssetsParser.readPreviewImage(context, preview));

            if(!acquired) {
                ColorMatrix matrix = new ColorMatrix();
                matrix.setSaturation(0);
                imageView.setColorFilter(new ColorMatrixColorFilter(matrix));
            }
        } catch(Exception exception) {
            Log.i("AchievementsFragment", "Could not load a preview image", exception);
        }
    }

    private void loadAcquiredAchievements(Context context) throws IOException {
        _acquiredAchievements = AssetsAchievements.parseAcquiredAchievementsData(context);
    }

    private boolean isAcquired(Achievement achievement) {
        for(AcquiredAchievementNode node : _acquiredAchievements) {
            if(node.condition.equalsIgnoreCase(achievement.condition) && node.acquired) {
                return true;
            }
        }

        return false;
    }
}
