package com.excercise.nns.switter.utils;

import android.util.Log;
import android.widget.GridLayout;
import android.widget.ImageView;

import android.databinding.BindingAdapter;

import com.daimajia.swipe.SwipeLayout;
import com.excercise.nns.switter.R;
import com.excercise.nns.switter.contract.OnRecyclerListener;
import com.excercise.nns.switter.contract.TimelineContract;
import com.excercise.nns.switter.model.entity.TwitterStatus;
import com.excercise.nns.switter.model.usecase.FavoriteUseCase;
import com.excercise.nns.switter.model.usecase.RetweetUseCase;
import com.excercise.nns.switter.view.component.CustomFab;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import twitter4j.MediaEntity;
import twitter4j.Twitter;

/**
 * Created by nns on 2017/09/18.
 */

public class CustomBindingAdapter {
    @BindingAdapter({"imageUrl"})
    public static void loadImage(CircleImageView profileImage, String profileImageUrl) {
        Picasso.with(profileImage.getContext()).load(profileImageUrl).into(profileImage);
    }

    @BindingAdapter({"imageUrl"})
    public static void loadImage(CustomFab fab, String profileImageUrl) {
        Picasso.with(fab.getContext()).load(profileImageUrl).into(fab);
    }

    @BindingAdapter({"uploadImage"})
    public static void loadUploadImage(GridLayout layout, MediaEntity[] entities) {
        if (layout.getChildCount() > 0) {
            layout.removeAllViews();
        }
        if (entities != null) {
            for (MediaEntity entity : entities) {
                ImageView image = new ImageView(layout.getContext());
                image.setScaleType(ImageView.ScaleType.CENTER_CROP);
                GridLayout.LayoutParams params = new GridLayout.LayoutParams();
                params.width = 300;
                params.height = 300;
                params.setMargins(0, 0, 15, 15);
                image.setLayoutParams(params);
                layout.addView(image, params);
                Picasso.with(layout.getContext()).load(entity.getMediaURL()).into(image);
            }
        }
    }

    @BindingAdapter({"targetStatus", "listener", "contract"})
    public static void onClickSwipeItem(
            SwipeLayout swipeLayout, TwitterStatus status, OnRecyclerListener listener, TimelineContract contract) {
        Twitter twitter = TwitterUtils.getTwitterInstance(swipeLayout.getContext());

        swipeLayout.findViewById(R.id.goProfile).setOnClickListener(v -> {
            listener.onSwipeItemClick("goPro", status);
            swipeLayout.close();
        });
        swipeLayout.findViewById(R.id.reply).setOnClickListener(v -> {
            listener.onSwipeItemClick("reply", status);
            swipeLayout.close();
        });
        swipeLayout.findViewById(R.id.reTweet).setOnClickListener(v -> {
            RetweetUseCase useCase = new RetweetUseCase(twitter);
            useCase.getRetweetUseCase(status)
                    .subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Observer<Boolean>() {
                        @Override
                        public void onSubscribe(@NonNull Disposable d) {}

                        @Override
                        public void onNext(@NonNull Boolean isRetweet) {
                            status.isRetweeted = isRetweet;
                        }

                        @Override
                        public void onError(@NonNull Throwable e) {
                            Log.e("retweet error", e.toString());
                            contract.postActionFailed("リツイートできませんでした。もう一度行ってください。");
                        }

                        @Override
                        public void onComplete() {
                            String message = status.isRetweeted ? "Retweet Success" : "Destroyed Retweet";
                            contract.postActionSuccess(message);
                        }
                    });
            swipeLayout.close();
        });

        swipeLayout.findViewById(R.id.favorite).setOnClickListener(v -> {
            FavoriteUseCase useCase = new FavoriteUseCase(twitter);
            useCase.postFavorite(status)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Observer<Boolean>() {
                        @Override
                        public void onSubscribe(@NonNull Disposable d) {}

                        @Override
                        public void onNext(@NonNull Boolean isFavorite) {
                            status.isFavorited = isFavorite;
                        }

                        @Override
                        public void onError(@NonNull Throwable e) {
                            contract.postActionFailed("エラーが発生しました。もう一度行ってください。");
                        }

                        @Override
                        public void onComplete() {
                            String message = status.isFavorited ? "Favorite Success." : "Destroyed Favorite.";
                            contract.postActionSuccess(message);
                        }
                    });
            swipeLayout.close();
        });

    }
}
