package com.example.joousope.first;


import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.joousope.first.Adapter.ThumbnailAdapter;
import com.example.joousope.first.Interface.FiltersListFragmentListener;
import com.example.joousope.first.Utils.BitmapUtils;
import com.example.joousope.first.Utils.SpacesItemDecoration;
import com.zomato.photofilters.FilterPack;
import com.zomato.photofilters.imageprocessors.Filter;
import com.zomato.photofilters.utils.ThumbnailItem;
import com.zomato.photofilters.utils.ThumbnailsManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * A simple {@link Fragment} subclass.
 */
public class FiltersList extends BottomSheetDialogFragment implements FiltersListFragmentListener {
    @BindView(R.id.recycler_view)
    RecyclerView recylerView;
    ThumbnailAdapter adapter;
    List<ThumbnailItem> thumbnailItems;
    FiltersListFragmentListener listener;
    static FiltersList instance;
    ImageView thumbnail;
    Context context;
    static Bitmap bitmap;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    public static FiltersList getInstance(Bitmap bitmapSave){
        bitmap=bitmapSave;
        if(instance==null) {
            instance = new FiltersList();

        }
            return instance;
    }
    public void setListener(FiltersListFragmentListener listener) {
        this.listener = listener;
    }

    public FiltersList() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View itemView = inflater.inflate(R.layout.fragment_filters_list, container, false);
        //thumbnail=(ImageView)container.findViewById(R.id.thumbnail);
        thumbnailItems=new ArrayList<>();
        ButterKnife.bind(this, itemView);
        adapter=new ThumbnailAdapter(thumbnailItems,this,getActivity());
        //adapter=new ThumbnailAdapter(thumbnailItems,this,getActivity());
        recylerView=(RecyclerView)itemView.findViewById(R.id.recycler_view);

        RecyclerView.LayoutManager mLayoutManager=new LinearLayoutManager(getActivity(),LinearLayoutManager.HORIZONTAL,false);
        recylerView.setLayoutManager(mLayoutManager);
        recylerView.setHasFixedSize(true);
        recylerView.setItemAnimator(new DefaultItemAnimator());
        int space=(int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,8,getResources().getDisplayMetrics());
        recylerView.addItemDecoration(new SpacesItemDecoration(space));
        recylerView.setAdapter(adapter);
        displayThumbnail(bitmap);
        ButterKnife.bind(this, itemView);
        return itemView;
    }
    public void displayThumbnail(final Bitmap bitmap) {
        Runnable r=new Runnable(){
            public void run()
            {
                Bitmap thumbImg;
                if(bitmap==null)
                    thumbImg= BitmapUtils.getBitmapFromAssets(Objects.requireNonNull(getActivity()),MainActivity.pictureName,100,100);
                else
                    thumbImg=Bitmap.createScaledBitmap(bitmap,100,100,false);
                if(thumbImg==null)
                    return;
                ThumbnailsManager.clearThumbs();
                thumbnailItems.clear();
                thumbnailItems = new ArrayList<>();
                //add normal bitmap first
                ThumbnailItem thumbnailItem=new ThumbnailItem();
                thumbnailItem.image=thumbImg;
                thumbnailItem.filterName=getString(R.string.filter_normal);
                Log.d("Debug","asdasd");
                ThumbnailsManager.addThumb(thumbnailItem);

                List<Filter> filters= FilterPack.getFilterPack(Objects.requireNonNull(getActivity()));

                for(Filter filter:filters)
                {
                    ThumbnailItem tT=new ThumbnailItem();
                    tT.image=thumbImg;
                    tT.filter= filter;
                    tT.filterName=filter.getName();
                    ThumbnailsManager.addThumb(tT);
                }
                thumbnailItems.addAll(ThumbnailsManager.processThumbs(getActivity()));
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        adapter.notifyDataSetChanged();
                    }
                });
            }
        };
        new Thread(r).start();
    }

    @Override
    public void onFilterSelected(Filter filter) {
        if(listener!=null)
            listener.onFilterSelected(filter);
    }
}
