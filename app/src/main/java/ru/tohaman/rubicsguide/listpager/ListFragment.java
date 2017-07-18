package ru.tohaman.rubicsguide.listpager;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import ru.tohaman.rubicsguide.R;

import java.util.ArrayList;
import java.util.List;

import static ru.tohaman.rubicsguide.g2f.G2FActivity.RubicPhase;

/**
 * Created by Toha on 09.04.2017.
 */

public class ListFragment extends Fragment {
    private List<ListPager> mListPagers = new ArrayList();
    private RecyclerView mRecyclerView;
    private RecycleAdapter mRecycleAdapter;
    private String Phase;
    private ListPagerLab listPagerLab;
    private Callbacks mCallbacks;

    public static ListFragment newInstance (String phase) {
        Bundle args = new Bundle();
        args.putSerializable(RubicPhase, phase);    //Передали имя фазы
        ListFragment fragment = new ListFragment();
        fragment.setArguments(args);
        return fragment;
    }

    //Обязательный интерфейс для активности-хоста
    public interface Callbacks {
        void onItemSelected (ListPager listPager);
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof Callbacks) {
            mCallbacks = (Callbacks) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement Callbacks");
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        // Получаем синглет
        listPagerLab = ListPagerLab.get(getActivity());
        // Получаем данные о фазе от родителя и делаем выборку по фазе
        Phase = (String) getArguments().getSerializable(RubicPhase);  //Приняли название фазы (PLL,OLL,...)

//        Phase = getActivity().getIntent().getStringExtra(RubicPhase);
        mListPagers = listPagerLab.getPhaseList(Phase);

        View v = inflater.inflate(R.layout.fragment_list, container, false);

        // список через РесайклВью с адаптером RecycleAdapter
        mRecyclerView = (RecyclerView) v.findViewById(R.id.recycler_view_list);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        updateUI();
        //mCallbacks.onItemSelected(mListPagers.get(0));
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
            mCallbacks.onItemSelected(mListPager);
        }
    }

    @Override
    public void onDetach () {
        super.onDetach();
        mCallbacks = null;
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
            View view;
            if (Phase.equals("BASIC")){
                view = layoutInflater.inflate(R.layout.basic_list_item, parent, false);
            } else {
                view = layoutInflater.inflate(R.layout.list_item, parent, false);
            }

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
