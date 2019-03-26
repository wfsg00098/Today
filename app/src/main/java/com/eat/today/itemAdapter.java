package com.eat.today;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class itemAdapter extends ArrayAdapter<ItemModel> {
    private int resource;
    private List<ItemModel> listItems;
    private Context context;
    private String CurrentUser;

    // 构造方法，传入布局、context和数据源list
    public itemAdapter(Context context, int resource, List<ItemModel> objects) {
        super(context, resource, objects);
        this.resource = resource;
        this.listItems = objects;
        this.context = context;
    }


    @Override
    public long getItemId(int position) {
        return position;
    }

    @Nullable
    @Override
    public ItemModel getItem(int position) {
        return listItems.get(position);
    }

    @Override
    public int getCount() {
        if (null == listItems) {
            return 0;
        }
        return listItems.size();
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        final ItemModel item = listItems.get(position);
        final View view;
        final ViewHolder viewholder;

        if (convertView == null){
            view = LayoutInflater.from(getContext()).inflate(resource, null);
            viewholder = new ViewHolder();
            viewholder.imgHead = (ImageView) view.findViewById(R.id.imgHead);
            viewholder.tvName = (TextView) view.findViewById(R.id.tvName);
            viewholder.tvDate = (TextView) view.findViewById(R.id.tvDate);
            viewholder.tvContent = (TextView) view.findViewById(R.id.tvContent);
            viewholder.ivPhone = (ImageView) view.findViewById(R.id.ivPhone);
            viewholder.tvPhonemodel = (TextView) view.findViewById(R.id.tvPhonemodel);
            viewholder.ivComment = (ImageView) view.findViewById(R.id.ivComment);
            viewholder.tvComment = (TextView) view.findViewById(R.id.tvComment);
            viewholder.tvPhrase = (TextView) view.findViewById(R.id.tvPhrase);
            viewholder.ivPhrase = (ImageView) view.findViewById(R.id.ivPhrase);
            viewholder.ivAgreeShow = (ImageView) view.findViewById(R.id.ivAgreeShow);
            viewholder.tvAgreeShow = (TextView) view.findViewById(R.id.tvAgreeShow);
            view.setTag(viewholder);
        } else {
            view = convertView;
            viewholder = (ViewHolder) view.getTag();
        }
        viewholder.imgHead.setImageResource(item.getHeadImg());
        viewholder.tvName.setText(item.getUserName());
        viewholder.tvDate.setText(item.getShuoDate());
        viewholder.tvContent.setText(item.getShuoContent());
        viewholder.ivPhone.setImageResource(R.drawable.status_phone);
        viewholder.tvPhonemodel.setText(item.getShuoPhoneModel());
        viewholder.ivComment.setImageResource(R.drawable.comment);
        viewholder.tvComment.setText("评论");
        if (item.getIsPhrase()) {
            viewholder.ivAgreeShow.setVisibility(View.VISIBLE);
            viewholder.tvAgreeShow.setVisibility(View.VISIBLE);
            viewholder.ivPhrase.setImageResource(R.drawable.phrase);
            viewholder.tvPhrase.setText("取消赞");
            if (item.getShuoPhraseNum() > 0) {
                // 可以通过Html.fromHtml()将字体设置颜色
                viewholder.tvAgreeShow.setText(Html.fromHtml("<font color='#1C86EE'>我</font>与" + item.getShuoPhraseNum() + "人觉得很赞"));
            } else {
                viewholder.tvAgreeShow.setText(Html.fromHtml("<font color='#1C86EE'>我</font>觉得很赞"));
            }

        } else {
            viewholder.ivPhrase.setImageResource(R.drawable.unphrase);
            viewholder.tvPhrase.setText("赞");
            if (item.getShuoPhraseNum() > 0) {
                viewholder.ivAgreeShow.setVisibility(View.VISIBLE);
                viewholder.tvAgreeShow.setVisibility(View.VISIBLE);
                viewholder.tvAgreeShow.setText(item.getShuoPhraseNum() + "人觉得很赞");
            } else {
                viewholder.ivAgreeShow.setVisibility(View.GONE);
                viewholder.tvAgreeShow.setVisibility(View.GONE);
            }
        }
        viewholder.ivPhrase.setOnClickListener(new ListViewButtonOnClickListener(position));
        return view;

    }

    class ViewHolder {
        ImageView imgHead;
        TextView tvName;
        TextView tvDate;
        TextView tvContent;
        ImageView ivPhone;
        TextView tvPhonemodel;
        ImageView ivComment;
        TextView tvComment;
        TextView tvPhrase;
        ImageView ivPhrase;
        ImageView ivAgreeShow;
        TextView tvAgreeShow;
    }

    /**
     * 清除所有数据
     */
    public void clear() {
        if (listItems != null) {
            listItems.clear();
        }
        notifyDataSetChanged();
    }

    /**
     * 重新添加数据
     *
     * @author ubuntu
     *
     */
    public void refresh(List<ItemModel> list) {
        if (listItems != null) {
            listItems = list;
            notifyDataSetChanged();
        }
    }

    class ListViewButtonOnClickListener implements View.OnClickListener{
        private int position;// 记录ListView中Button所在的Item的位置
        // private ViewGroup viewgroup;
        public ListViewButtonOnClickListener(int position) {
            this.position = position;
        }

        @Override
        public void onClick(View view) {
            switch (view.getId()){
                case R.id.ivPhrase:
                    //Log.i("TAG", "说说" + position + "点赞按钮被点击了");
                    Activity activity4 = (Activity) context;
                    CurrentUser = ((TextView) activity4.findViewById(R.id.tv_userName)).getText().toString();
                    final ChangeShuoPhraseNum phrase = new ChangeShuoPhraseNum();
                    ImageView ivAgree = (ImageView) view;
                    final ItemModel item = listItems.get(position);
                    if (item.getIsPhrase()) {
                        Log.i("dianzan","该说说已经被点赞");
                        ivAgree.setImageResource(R.drawable.unphrase);
                    }else {
                        ivAgree.setImageResource(R.drawable.phrase);
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                Log.i("internet","进入网络  说说ID="+String.valueOf(item.getShuoId()));
                                phrase.addShuoPhraseNum(item.getShuoId());
                            }
                        }).start();
                    }
                    item.setIsPhrase(!item.getIsPhrase());
                    // 刷新布局
                    notifyDataSetChanged();
                    break;
            }

        }
    }
}
