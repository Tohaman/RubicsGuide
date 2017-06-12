package ru.tohaman.rubicsguide.listpager;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import ru.tohaman.rubicsguide.R;

import java.util.ArrayList;
import java.util.List;

import static ru.tohaman.rubicsguide.G2FFragment.RubicPhase;

/**
 * Created by Toha on 09.04.2017.
 */

public class ListFragment extends Fragment {
    private List<ListPager> mListPagers = new ArrayList();
    private RecyclerView mRecyclerView;
    private RecycleAdapter mRecycleAdapter;
    private String Phase;
    private ListPagerLab listPagerLab;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_list, container, false);

        // Получаем синглет
        listPagerLab = ListPagerLab.get(getActivity());
        // Получаем данные о фазе от родителя и делаем выборку по фазе
        Phase = getActivity().getIntent().getStringExtra(RubicPhase);
        mListPagers = listPagerLab.getPhaseList(Phase);

        // список через РесайклВью с адаптером RecycleAdapter
        mRecyclerView = (RecyclerView) v.findViewById(R.id.recycler_view_list);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        updateUI();
        return v;
    }

    private void updateUI(){
        mListPagers = listPagerLab.getPhaseList(Phase);
        if (mRecycleAdapter == null) {
            mRecycleAdapter = new RecycleAdapter(mListPagers);
            mRecyclerView.setAdapter(mRecycleAdapter);
        } else {
            mRecycleAdapter.notifyDataSetChanged();
        }
    }

    private class RecycleViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private ImageView mImageView;
        private TextView mTextView;
        private ListPager mListPager;

        public RecycleViewHolder(View view) {
            super(view);
            itemView.setOnClickListener(this);
            mImageView = (ImageView) view.findViewById(R.id.list_item_image);
            mTextView = (TextView) view.findViewById(R.id.list_item_title_text);
        }

        public void bindRecycleView (ListPager listPager){
            mListPager = listPager;
            mTextView.setText(mListPager.getTitle());
            mImageView.setImageResource(mListPager.getIcon());

        }

        @Override
        public void onClick(View v) {
                Intent intent = PagerActivity.newIntenet(getActivity(), String.valueOf(mListPager.getId()), Phase);
                startActivity(intent);
        }
    }

    private class RecycleAdapter extends RecyclerView.Adapter<RecycleViewHolder> {
        private List<ListPager> mListPagers;

        public RecycleAdapter(List<ListPager> listPagers) {
            this.mListPagers = listPagers;
            mListPagers = listPagers;
        }

        @Override
        public RecycleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            View view = layoutInflater.inflate(R.layout.list_item, parent, false);
            return new RecycleViewHolder(view);
        }

        @Override
        public void onBindViewHolder(RecycleViewHolder holder, int position) {
            ListPager listPager = mListPagers.get(position);
            holder.bindRecycleView(listPager);
        }

        @Override
        public int getItemCount() {
            return mListPagers.size();
        }

    }

    }
