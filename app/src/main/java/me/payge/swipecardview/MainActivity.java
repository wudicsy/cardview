package me.payge.swipecardview;

import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckedTextView;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.core.ImagePipeline;
import com.lorentzos.flingswipe.SwipeFlingAdapterView;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Random;

public class MainActivity extends AppCompatActivity implements SwipeFlingAdapterView.onFlingListener,
        SwipeFlingAdapterView.OnItemClickListener, View.OnClickListener {

    String[] headerIcons = {
            "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1504440501577&di=8ec829509c9a7472327f6716cc69ceab&imgtype=0&src=http%3A%2F%2Fp4.qhmsg.com%2Ft0164101879ede49e5e.jpg",
            "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1504440501792&di=d102032a5e52fb7cfe75e7853be1cc9e&imgtype=0&src=http%3A%2F%2Fimg3.duitang.com%2Fuploads%2Fitem%2F201509%2F23%2F20150923215704_Q3ZaM.jpeg",
            "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1504438344032&di=743c95ac8811c2c94826ecb61ff6d492&imgtype=0&src=http%3A%2F%2Fwww.005.tv%2Fuploads%2Fallimg%2F170725%2F32-1FH5102Q6429.jpg",
            "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1504438344032&di=6cfbae03302b1368b6cab870c7f1ba2a&imgtype=0&src=http%3A%2F%2Fwww.005.tv%2Fuploads%2Fallimg%2F170515%2F6-1F5151125095K.png",
    };

    String[] names = {"张三", "李四", "王五", "赵六"};

    String[] citys = {"北京", "上海", "辽宁", "黑龙家"};

    String[] edus = {"大专", "本科", "硕士", "博士"};

    String[] years = {"1年", "2年", "3年", "4年"};

    Random ran = new Random();

    private int cardWidth;
    private int cardHeight;

    private SwipeFlingAdapterView swipeView;
    private InnerAdapter adapter;
    SimpleDraweeView simpleDraweeView;
    ArrayList<Talent> list = new ArrayList<>(10);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //清除缓存
//        ImagePipeline imagePipeline = Fresco.getImagePipeline();
//        imagePipeline.clearCaches();
//
//
//        ImagePipeline imagePipeline1 = Fresco.getImagePipeline();
//        imagePipeline1.clearMemoryCaches();
//        imagePipeline1.clearDiskCaches();
//
//// combines above two lines
//        imagePipeline1.clearCaches();

        simpleDraweeView = (SimpleDraweeView) findViewById(R.id.simpleview);

        initView();
        loadData();


    }

    private void initView() {
        DisplayMetrics dm = getResources().getDisplayMetrics();
        float density = dm.density;
        cardWidth = (int) (dm.widthPixels - (2 * 18 * density));
        cardHeight = (int) (dm.heightPixels - (338 * density));


        swipeView = (SwipeFlingAdapterView) findViewById(R.id.swipe_view);
        if (swipeView != null) {
            swipeView.setIsNeedSwipe(true);
            swipeView.setFlingListener(this);
            swipeView.setOnItemClickListener(this);

            adapter = new InnerAdapter();
            swipeView.setAdapter(adapter);
        }

        if (headerIcons.length == 1) {
            swipeView.setIsNeedSwipe(false);
        }
    }


    @Override
    public void onItemClicked(MotionEvent event, View v, Object dataObject) {
    }

    @Override
    public void removeFirstObjectInAdapter() {
        adapter.remove(0);
    }

    @Override
    public void onLeftCardExit(Object dataObject) {
    }

    @Override
    public void onRightCardExit(Object dataObject) {
    }

    @Override
    public void onAdapterAboutToEmpty(int itemsInAdapter) {
        if (itemsInAdapter == 3) {
            loadData();
        }
    }

    @Override
    public void onScroll(float progress, float scrollXProgress) {
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.swipeLeft:
                break;
            case R.id.swipeRight:
        }
    }

    private void loadData() {
        new AsyncTask<Void, Void, List<Talent>>() {
            @Override
            protected List<Talent> doInBackground(Void... params) {
                Talent talent;
                for (int i = 0; i < headerIcons.length; i++) {
                    talent = new Talent();
                    talent.headerIcon = headerIcons[i % headerIcons.length];
                    talent.nickname = names[i];
                    talent.cityName = citys[i];
                    talent.educationName = edus[i];
                    talent.workYearName = years[i];
                    list.add(talent);
                }
                return list;
            }

            @Override
            protected void onPostExecute(List<Talent> list) {
                super.onPostExecute(list);
                simpleDraweeView.setImageURI(Uri.parse(list.get(0).headerIcon));
                adapter.addAll(list);
            }
        }.execute();
    }


    private class InnerAdapter extends BaseAdapter {

        ArrayList<Talent> objs;

        public InnerAdapter() {
            objs = new ArrayList<>();
        }

        public void addAll(Collection<Talent> collection) {
            objs.addAll(collection);
            notifyDataSetChanged();
        }

        public void clear() {
            objs.clear();
            notifyDataSetChanged();
        }

        public boolean isEmpty() {
            return objs.isEmpty();
        }

        public void remove(int index) {
            if (index > -1 && index < objs.size()) {
                Talent talent = objs.get(index);
                simpleDraweeView.setImageURI(Uri.parse(objs.get(index + 1).headerIcon));
                objs.remove(index);
                objs.add(talent);
                notifyDataSetChanged();
            }
        }


        @Override
        public int getCount() {
            return objs.size();
        }

        @Override
        public Talent getItem(int position) {
            if (objs == null || objs.size() == 0) return null;
            return objs.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        // TODO: getView
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            Talent talent = getItem(position);
            if (convertView == null) {
                convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_new_item, parent, false);
                holder = new ViewHolder();
                convertView.setTag(holder);
                convertView.getLayoutParams().width = cardWidth;
                holder.portraitView = (SimpleDraweeView) convertView.findViewById(R.id.simpleview);
                holder.portraitView.getLayoutParams().height = cardHeight;
                holder.nameView = (TextView) convertView.findViewById(R.id.name);
                holder.cityView = (TextView) convertView.findViewById(R.id.city);
                holder.eduView = (TextView) convertView.findViewById(R.id.education);
                holder.workView = (TextView) convertView.findViewById(R.id.work_year);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            Log.e("headerIcon", talent.headerIcon);

            holder.portraitView.setImageURI(Uri.parse(talent.headerIcon));

            holder.nameView.setText(String.format("%s", talent.nickname));
            //holder.jobView.setText(talent.jobName);

            final CharSequence no = "暂无";

            holder.cityView.setHint(no);
            holder.cityView.setText(talent.cityName);
            holder.cityView.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.home01_icon_location, 0, 0);

            holder.eduView.setHint(no);
            holder.eduView.setText(talent.educationName);
            holder.eduView.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.home01_icon_edu, 0, 0);

            holder.workView.setHint(no);
            holder.workView.setText(talent.workYearName);
            holder.workView.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.home01_icon_work_year, 0, 0);


            return convertView;
        }

    }

    private static class ViewHolder {
        SimpleDraweeView portraitView;
        TextView nameView;
        TextView cityView;
        TextView eduView;
        TextView workView;
        CheckedTextView collectView;

    }

    public static class Talent {
        public String headerIcon;
        public String nickname;
        public String cityName;
        public String educationName;
        public String workYearName;
    }

}
