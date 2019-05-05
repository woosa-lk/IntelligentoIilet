package com.woosa.intelligentoiilet;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import java.util.List;

public class viewAdapter extends ArrayAdapter implements View.OnClickListener {
    private final int ImageId;
    private Callback mCallback;

    public interface Callback {
        public void click(View v);
    }
    class ViewHolder{
        ImageView fruitImage;
        TextView fruitName;
        LinearLayout fruitLayout;
        Button fruitBtn;
    }

    public viewAdapter(Context context, int headImage, List<myBean> obj, Callback callback){
        super(context,headImage,obj);
        ImageId = headImage;
        mCallback = callback;
    }

    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        ViewHolder viewHolder;
        myBean myBean = (myBean) getItem(position);
        View view ;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            view = LayoutInflater.from(getContext()).inflate(ImageId,parent,false);
            viewHolder.fruitImage = view.findViewById(R.id.headimage);
            viewHolder.fruitName = view.findViewById(R.id.headtext);
            viewHolder.fruitLayout = view.findViewById(R.id.ll_view);
            viewHolder.fruitBtn = view.findViewById(R.id.view_btn_bind);
            view.setTag(viewHolder);
        } else {
            view =convertView;
            viewHolder = (ViewHolder)view.getTag();
        }
        viewHolder.fruitImage.setImageResource(myBean.getImageID());
        viewHolder.fruitName.setText(myBean.getText());
        viewHolder.fruitBtn.setTag(position);

        return view;
    }

    @Override
    public void onClick(View v) {
        mCallback.click(v);
    }

}
