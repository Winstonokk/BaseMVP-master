package com.barnettwong.basemvp.ui.adapter;

import android.content.Context;

import com.aspsine.irecyclerview.universaladapter.ViewHolderHelper;
import com.aspsine.irecyclerview.universaladapter.recyclerview.CommonRecycleViewAdapter;
import com.barnettwong.basemvp.R;
import com.barnettwong.basemvp.bean.MovieComment;
import com.barnettwong.basemvp.util.TextUtils;
import com.jaydenxiao.common.commonutils.ImageLoaderUtils;

public class MovieCommentAdapter extends CommonRecycleViewAdapter<MovieComment> {
    private Context mContext;

    public MovieCommentAdapter(Context context, int layoutId) {
        super(context, layoutId);
        mContext = context;
    }

    @Override
    public void convert(ViewHolderHelper helper, MovieComment movieComment) {
        helper.setText(R.id.tv_title, movieComment.getTitle());
        helper.setText(R.id.tv_desc, movieComment.getSummary());
        ImageLoaderUtils.displayRound(mContext, helper.getView(R.id.iv_avatar), movieComment.getUserImage());

        MovieComment.RelatedObjBean relatedObj = movieComment.getRelatedObj();
        if (relatedObj != null) {
            ImageLoaderUtils.display(mContext, helper.getView(R.id.iv_cover), relatedObj.getImage());
            helper.setText(R.id.tv_movie_name, relatedObj.getTitle());
        }
        helper.setText(R.id.tv_title, movieComment.getTitle());
        helper.setText(R.id.tv_desc, TextUtils.handleSpace(movieComment.getSummary()));
        helper.setText(R.id.tv_user_name, movieComment.getNickname() + "- 评");
    }

}
