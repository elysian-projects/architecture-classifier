package com.architecture.app.utils;

import android.content.Context;
import android.graphics.Rect;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.architecture.app.R;
import com.architecture.app.viewModels.Achievement;

public class AchievementsGridAdapter extends BaseAdapter {
    private final Achievement[] _achievements;
    private final Context _context;
    private LayoutInflater _inflater;

    public AchievementsGridAdapter(Achievement[] achievements, Context context) {
        _achievements = achievements;
        _context = context;
    }

    @Override
    public int getCount() {
        return _achievements.length;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        if(_inflater == null) {
            _inflater = (LayoutInflater) _context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        if(view == null) {
            view = _inflater.inflate(R.layout.row_achievement, null);
        }

        TextView label = view.findViewById(R.id.achievement_label);
        ImageView preview = view.findViewById(R.id.achievement_image_preview);

        preview.setClipToOutline(true);
        preview.setClipBounds(new Rect(10, 0, 0, 0));

        Achievement currentAchievement = _achievements[position];

        label.setText(currentAchievement.label);
        loadImageToRow(preview, currentAchievement.preview);

        return view;
    }

    private void loadImageToRow(ImageView imageView, String preview) {
        try {
            imageView.setImageBitmap(AssetsParser.readPreviewImage(_context, preview));
        } catch(Exception exception) {
            Log.i("AchievementsFragment", "Could not load a preview image", exception);
        }
    }
}
