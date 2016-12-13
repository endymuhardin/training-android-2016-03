package com.brainmatics.bpjs.bpjskesehatan.fragment;


import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.brainmatics.bpjs.bpjskesehatan.R;
import com.brainmatics.bpjs.bpjskesehatan.adapter.TagihanListAdapter;
import com.brainmatics.bpjs.bpjskesehatan.dto.Tagihan;
import com.brainmatics.bpjs.bpjskesehatan.service.BackendService;

import java.util.List;

public class TagihanFragment extends Fragment {

    private BackendService backendService = new BackendService();

    public TagihanFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        List<Tagihan> dataTagihan = backendService.getDaftarTagihan();
        Tagihan[] data = new Tagihan[dataTagihan.size()];
        data = dataTagihan.toArray(data);

        TagihanListAdapter tagihanListAdapter = new TagihanListAdapter(getActivity(), R.layout.lv_tagihan, data);

        // Inflate the layout for this fragment
        View fragmentView = inflater.inflate(R.layout.fragment_tagihan, container, false);
        ListView lvTagihan = (ListView) fragmentView.findViewById(R.id.lvTagihan);
        lvTagihan.setAdapter(tagihanListAdapter);

        return fragmentView;
    }

}
