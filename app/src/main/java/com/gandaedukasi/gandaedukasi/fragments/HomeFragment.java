package com.gandaedukasi.gandaedukasi.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.gandaedukasi.gandaedukasi.R;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link HomeFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public HomeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HomeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    public class ListViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

        // Provide a reference to the views for each data item
        // Complex data items may need more than one view per item, and
        // you provide access to all the views for a data item in a view holder
        public class ListViewHolder1 extends RecyclerView.ViewHolder implements View.OnClickListener{
            // each data item is just a string in this case
            protected ImageView iv;

            public ListViewHolder1(final View v) {
                super(v);
                v.setOnClickListener(this);
            }

            @Override
            public void onClick(View v) {
            }

        }

        public class ListViewHolder2 extends RecyclerView.ViewHolder implements View.OnClickListener{
            // each data item is just a string in this case
            protected TextView tv;

            public ListViewHolder2(final View v) {
                super(v);
                v.setOnClickListener(this);
            }

            @Override
            public void onClick(View v) {
            }

        }

        @Override
        public int getItemViewType(int position) {
            if(position == 0)
                return 0;
            else
                return 1;
        }

        // Create new views (invoked by the layout manager)
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                                 int viewType) {
            // create a new view
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());

            switch (viewType) {
                case 0:
                {
                    View itemView = inflater.inflate(R.layout.activity_main, parent, false);
                    return new ListViewHolder1(itemView);
                }
                default:
                {
                    View itemView = inflater.inflate(R.layout.activity_cari_pengajar, parent, false);
                    return new ListViewHolder2(itemView);
                }
            }
        }

        // Replace the contents of a view (invoked by the layout manager)
        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            // - get element from your dataset at this position
            // - replace the contents of the view with that element

            switch (holder.getItemViewType())
            {
                case 0 :
                {
                    ListViewHolder1 lh = (ListViewHolder1)holder;
                    lh.iv = null;
                }
                default:
                {
                    ListViewHolder2 lh = (ListViewHolder2)holder;
                    lh.tv = null;
                }
            }
        }

        // Return the size of your dataset (invoked by the layout manager)
        @Override
        public int getItemCount() {
            return 0;
        }
    }
}
